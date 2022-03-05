package br.com.solicitacoes.solicitacoes.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.datatables.Datatables;
import br.com.solicitacoes.solicitacoes.datatables.DatatablesColunas;
import br.com.solicitacoes.solicitacoes.domain.Lei;
import br.com.solicitacoes.solicitacoes.repository.LeiRepository;
import br.com.solicitacoes.solicitacoes.specification.SpecificationLei;

@Service
public class LeiService {

	@Autowired
	private Datatables datatables;

	@Autowired
	private LeiRepository repository;

	@Transactional(readOnly = false)
	public void salvar(@Valid Lei lei) {
		repository.save(lei);
	}

	@Transactional(readOnly = true)
	public Lei buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Lei> buscarTodasLeis() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request, String buscaAssunto, LocalDate buscaData,
			LocalDate buscaDataFinal, String status) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.LEIS);
		Page<Lei> page = null;
		Specification<Lei> data = null;
		Specification<Lei> situacao = null;
		if (datatables.getSearch().isEmpty()) {
			page = repository.findAll(datatables.getPageable());
		}
		if (!datatables.getSearch().isEmpty()) {
			page = repository.findByAssunto(datatables.getSearch(), datatables.getPageable());
		}

		if (buscaData != null) {
			data = SpecificationLei.dataIgual(buscaData);

		}
		if (buscaData != null && buscaDataFinal != null) {
			data = SpecificationLei.dataBetween(buscaData, buscaDataFinal);

		}
		
		if (status != null) {
			switch (status) {
			case "CRIADA":
				situacao = SpecificationLei.statusCriado(status);
				break;
			case "PROPOSITURA":
				situacao = SpecificationLei.statusPropositura(status);
				break;
			case "APROVADO_PREFEITO":
				situacao = SpecificationLei.statusAprovadoPrefeito(status);
				break;
			case "VETADO_PREFEITO":
				situacao = SpecificationLei.statusVetadoPrefeito(status);
				break;
			case "VETADO_VEREADORES":
				situacao = SpecificationLei.statusVetadoVereadores(status);
				break;
			case "APROVADO_VEREADORES":
				situacao = SpecificationLei.statusAprovadoVereadores(status);
				break;
			case "NOVA_VOTACAO":
				situacao = SpecificationLei.statusNovaVotacao(status);
				break;
			}
		}
		
		if ((!buscaAssunto.isEmpty() || buscaData != null || status != null)) {
			page = repository.findAll(
					Specification.where(SpecificationLei.assunto(buscaAssunto)).and(situacao).and(data),
					datatables.getPageable());
		}

		return datatables.getResponse(page);
	}
}
