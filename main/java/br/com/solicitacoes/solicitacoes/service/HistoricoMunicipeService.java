package br.com.solicitacoes.solicitacoes.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.domain.HistoricoMunicipe;
import br.com.solicitacoes.solicitacoes.repository.HistoricoMunicipeRepository;
import br.com.solicitacoes.solicitacoes.specification.SpecificationHistoricoMunicipe;

@Service
public class HistoricoMunicipeService {
	
	@Autowired
	private HistoricoMunicipeRepository repository;

	@Transactional(readOnly = false)
	public void salvar(HistoricoMunicipe historico) {
		repository.save(historico);
	}

	@Transactional(readOnly = true)
	public List<HistoricoMunicipe> buscarHistoricoPorClienteId(Long id) {
		return repository.findHistoricoByClienteId(id);
	}

	@Transactional(readOnly = true)
	public List<HistoricoMunicipe> buscarPorParametros(Long idMunicipe, String tipo, LocalDate dataInicial, LocalDate dataFinal) {
		Specification<HistoricoMunicipe> data = null;
		List<HistoricoMunicipe> lista = null;
		Specification<HistoricoMunicipe> situacao = null;

		if (!tipo.isEmpty()) {
			switch (tipo) {
			case "SOLICITACAO_ABERTA":
				situacao = SpecificationHistoricoMunicipe.solicitacaoAberta(tipo);
				break;
			case "CLIENTE_NEW":
				situacao = SpecificationHistoricoMunicipe.novoMunicipe(tipo);
				break;
			case "CLIENTE_EDIT":
				situacao = SpecificationHistoricoMunicipe.editMunicipe(tipo);
				break;
			}
		}

		if (dataInicial != null)

		{
			data = SpecificationHistoricoMunicipe.dataIgual(dataInicial);
		}
		if (dataInicial != null && dataFinal != null) {
			data = SpecificationHistoricoMunicipe.dataBetween(dataInicial, dataFinal);
		}
		if (!tipo.isEmpty() || dataInicial != null) {
			lista = repository.findAll(Specification
					.where(SpecificationHistoricoMunicipe.idMunicipe(idMunicipe)).and(situacao).and(data),
					Sort.by("id").descending());
		}
		return lista;
	
	}

}
