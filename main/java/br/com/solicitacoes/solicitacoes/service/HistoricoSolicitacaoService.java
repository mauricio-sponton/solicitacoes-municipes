package br.com.solicitacoes.solicitacoes.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.domain.HistoricoSolicitacao;
import br.com.solicitacoes.solicitacoes.repository.HistoricoSolicitacaoRepository;
import br.com.solicitacoes.solicitacoes.specification.SpecificationHistoricoSolicitacao;

@Service
public class HistoricoSolicitacaoService {

	@Autowired
	private HistoricoSolicitacaoRepository repository;

	@Transactional(readOnly = false)
	public void salvar(HistoricoSolicitacao historico) {
		repository.save(historico);

	}

	@Transactional(readOnly = true)
	public List<HistoricoSolicitacao> buscarHistoricoPorSolicitacaoId(Long id) {
		return repository.findBySolicitacaoId(id);
	}

	@Transactional(readOnly = true)
	public List<HistoricoSolicitacao> buscarPorParametros(Long idSolicitacao, String tipo, LocalDate dataInicial,
			LocalDate dataFinal) {

		Specification<HistoricoSolicitacao> data = null;
		List<HistoricoSolicitacao> lista = null;
		Specification<HistoricoSolicitacao> situacao = null;

		if (!tipo.isEmpty()) {
			switch (tipo) {
			case "SOLICITACAO_ABERTA":
				situacao = SpecificationHistoricoSolicitacao.solicitacaoAberta(tipo);
				break;
			case "SOLICITACAO_PENDENTE":
				situacao = SpecificationHistoricoSolicitacao.solicitacaoPendente(tipo);
				break;
			case "SOLICITACAO_ATRASADA":
				situacao = SpecificationHistoricoSolicitacao.solicitacaoAtrasada(tipo);
				break;
			case "SOLICITACAO_EDIT":
				situacao = SpecificationHistoricoSolicitacao.solicitacaoEdit(tipo);
				break;
			case "SOLUCAO_EDIT":
				situacao = SpecificationHistoricoSolicitacao.solucaoEdit(tipo);
				break;
			}
		}

		if (dataInicial != null)

		{
			data = SpecificationHistoricoSolicitacao.dataIgual(dataInicial);
		}
		if (dataInicial != null && dataFinal != null) {
			data = SpecificationHistoricoSolicitacao.dataBetween(dataInicial, dataFinal);
		}
		if (!tipo.isEmpty() || dataInicial != null) {
			lista = repository.findAll(Specification
					.where(SpecificationHistoricoSolicitacao.idSolicitacao(idSolicitacao)).and(situacao).and(data),
					Sort.by("data").descending().and(Sort.by("hora").descending()));
		}
		return lista;
	}

}
