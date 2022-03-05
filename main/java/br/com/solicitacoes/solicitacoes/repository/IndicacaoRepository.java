package br.com.solicitacoes.solicitacoes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Indicacao;

public interface IndicacaoRepository extends JpaRepository<Indicacao, Long>, JpaSpecificationExecutor<Indicacao>{

	@Query("select i from Indicacao i where i.assunto like :search% or i.sessao like :search%")
	Page<Indicacao> findByAssuntoOrSessao(String search, Pageable pageable);

	@Query("select i from Indicacao i where i.solicitacao.id = :id")
	Indicacao findBySolicitacaoId(Long id);
}
