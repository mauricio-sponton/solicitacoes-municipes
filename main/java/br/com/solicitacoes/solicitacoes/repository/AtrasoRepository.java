package br.com.solicitacoes.solicitacoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Atraso;

public interface AtrasoRepository extends JpaRepository<Atraso, Long>{

	@Query("select a from Atraso a where a.solicitacao.id = :id")
	List<Atraso> findBySolicitacaoId(Long id);

}
