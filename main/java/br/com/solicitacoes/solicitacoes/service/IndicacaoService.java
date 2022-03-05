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
import br.com.solicitacoes.solicitacoes.domain.Indicacao;
import br.com.solicitacoes.solicitacoes.repository.IndicacaoRepository;
import br.com.solicitacoes.solicitacoes.specification.SpecificationIndicacao;

@Service
public class IndicacaoService {

	@Autowired
	private IndicacaoRepository repository;

	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvar(@Valid Indicacao indicacao) {
		repository.save(indicacao);
	}

	@Transactional(readOnly = true)
	public Indicacao buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Indicacao> buscarTodosAssuntos() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request, LocalDate dataInicial, LocalDate dataFinal,
			String buscaAssunto, String buscaSessao) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.INDICACOES);
		Page<Indicacao> page = null;
		Specification<Indicacao> data = null;
		if (datatables.getSearch().isEmpty()) {
			page = repository.findAll(datatables.getPageable());
		}
		if (!datatables.getSearch().isEmpty()) {
			page = repository.findByAssuntoOrSessao(datatables.getSearch(), datatables.getPageable());
		}

		if (dataInicial != null) {
			data = SpecificationIndicacao.dataIgual(dataInicial);
		}

		if (dataInicial != null && dataFinal != null) {
			data = SpecificationIndicacao.dataBetween(dataInicial, dataFinal);
		}

		if (dataInicial != null || !buscaAssunto.isEmpty() || !buscaSessao.isEmpty()) {
			page = repository
					.findAll(
							Specification.where(data)
									.and(SpecificationIndicacao.assunto(buscaAssunto)
											.and(SpecificationIndicacao.sessao(buscaSessao))),
							datatables.getPageable());
		}

		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Indicacao buscarPorSolicitacaoId(Long id) {
		return repository.findBySolicitacaoId(id);
	}

}
