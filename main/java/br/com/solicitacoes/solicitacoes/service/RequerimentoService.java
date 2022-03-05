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
import br.com.solicitacoes.solicitacoes.domain.Requerimento;
import br.com.solicitacoes.solicitacoes.repository.RequerimentoRepository;
import br.com.solicitacoes.solicitacoes.specification.SpecificationRequerimento;

@Service
public class RequerimentoService {

	@Autowired
	private Datatables datatables;

	@Autowired
	private RequerimentoRepository repository;

	@Transactional(readOnly = false)
	public void salvar(@Valid Requerimento requerimento) {
		repository.save(requerimento);
	}

	@Transactional(readOnly = true)
	public Requerimento buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Requerimento> buscarTodosAssuntos() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request, LocalDate dataInicial, LocalDate dataFinal,
			String buscaAssunto) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.REQUERIMENTOS);
		Specification<Requerimento> data = null;
		Page<Requerimento> page = null;
		if (datatables.getSearch().isEmpty()) {
			page = repository.findAll(datatables.getPageable());
		}
		if (!datatables.getSearch().isEmpty()) {
			page = repository.findByAssuntoOrSessao(datatables.getSearch(), datatables.getPageable());
		}
		
		if (dataInicial != null) {
			data = SpecificationRequerimento.dataIgual(dataInicial);
		}

		if (dataInicial != null && dataFinal != null) {
			data = SpecificationRequerimento.dataBetween(dataInicial, dataFinal);
		}

		if (dataInicial != null || !buscaAssunto.isEmpty()) {
			page = repository.findAll(Specification.where(data).and(SpecificationRequerimento.assunto(buscaAssunto)),
					datatables.getPageable());
		}

		return datatables.getResponse(page);
	}

}
