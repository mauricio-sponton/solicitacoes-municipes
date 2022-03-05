package br.com.solicitacoes.solicitacoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.HistoricoSolicitacao;

public interface HistoricoSolicitacaoRepository
		extends JpaRepository<HistoricoSolicitacao, Long>, JpaSpecificationExecutor<HistoricoSolicitacao> {

	@Query("select h from HistoricoSolicitacao h where h.solicitacao.id = :id order by h.id desc")
	List<HistoricoSolicitacao> findBySolicitacaoId(Long id);

}
