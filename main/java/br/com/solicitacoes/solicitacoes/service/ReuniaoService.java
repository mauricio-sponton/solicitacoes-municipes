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
import br.com.solicitacoes.solicitacoes.domain.Reuniao;
import br.com.solicitacoes.solicitacoes.repository.ReuniaoRepository;
import br.com.solicitacoes.solicitacoes.specification.SpecificationReuniao;

@Service
public class ReuniaoService {

	@Autowired
	private ReuniaoRepository repository;

	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvar(@Valid Reuniao reuniao) {
		repository.save(reuniao);
	}

	@Transactional(readOnly = true)
	public Reuniao buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Reuniao> buscarTodosAssuntos() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request, LocalDate dataInicial, LocalDate dataFinal,
			String buscaAssunto) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.REUNIOES);
		Page<Reuniao> page = null;
		Specification<Reuniao> data = null;

		if (datatables.getSearch().isEmpty()) {
			page = repository.findAll(datatables.getPageable());
		}
		if (!datatables.getSearch().isEmpty()) {
			page = repository.findByAssunto(datatables.getSearch(), datatables.getPageable());
		}

		if (dataInicial != null) {
			data = SpecificationReuniao.dataIgual(dataInicial);
		}

		if (dataInicial != null && dataFinal != null) {
			data = SpecificationReuniao.dataBetween(dataInicial, dataFinal);
		}

		if (dataInicial != null || !buscaAssunto.isEmpty()) {
			page = repository.findAll(Specification.where(data).and(SpecificationReuniao.assunto(buscaAssunto)),
					datatables.getPageable());
		}

		return datatables.getResponse(page);
	}

}
