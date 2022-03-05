package br.com.solicitacoes.solicitacoes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Reuniao;

public interface ReuniaoRepository extends JpaRepository<Reuniao, Long>, JpaSpecificationExecutor<Reuniao>{

	@Query("select r from Reuniao r where r.assunto like :search%")
	Page<Reuniao> findByAssunto(String search, Pageable pageable);

}
