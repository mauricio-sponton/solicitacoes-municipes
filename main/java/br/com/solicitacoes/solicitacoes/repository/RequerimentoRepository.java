package br.com.solicitacoes.solicitacoes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Requerimento;

public interface RequerimentoRepository extends JpaRepository<Requerimento, Long>, JpaSpecificationExecutor<Requerimento>{

	@Query("select r from Requerimento r where r.assunto like :search% or r.sessao like :search%")
	Page<Requerimento> findByAssuntoOrSessao(String search, Pageable pageable);

}
