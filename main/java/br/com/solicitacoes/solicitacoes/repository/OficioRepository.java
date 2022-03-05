package br.com.solicitacoes.solicitacoes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Oficio;

public interface OficioRepository extends JpaRepository<Oficio, Long>, JpaSpecificationExecutor<Oficio> {

	@Query("select o from Oficio o where o.assunto like :search%")
	Page<Oficio> findByAssunto(String search, Pageable pageable);

}
