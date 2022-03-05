package br.com.solicitacoes.solicitacoes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.RespostaIndicacao;

public interface RespostaIndicacaoRepository extends JpaRepository<RespostaIndicacao, Long>{

	@Query("select r from RespostaIndicacao r where r.indicacao.id =:id")
	RespostaIndicacao findByIndicacaoId(Long id);

}
