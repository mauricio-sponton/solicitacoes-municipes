package br.com.solicitacoes.solicitacoes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.RespostaRequerimento;

public interface RespostaRequerimentoRepository extends JpaRepository<RespostaRequerimento, Long>{

	@Query("select r from RespostaRequerimento r where r.requerimento.id = :id")
	RespostaRequerimento findByRequerimentoId(Long id);

}
