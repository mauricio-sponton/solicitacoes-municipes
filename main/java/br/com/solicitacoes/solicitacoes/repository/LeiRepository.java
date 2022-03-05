package br.com.solicitacoes.solicitacoes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Lei;

public interface LeiRepository extends JpaRepository<Lei, Long>, JpaSpecificationExecutor<Lei>{

	@Query("select l from Lei l where l.assunto like :search%")
	Page<Lei> findByAssunto(String search, Pageable pageable);

}
