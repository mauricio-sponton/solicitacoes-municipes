package br.com.solicitacoes.solicitacoes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Remetente;

public interface RemetenteRepository extends JpaRepository<Remetente, Long>{

	@Query("select r from Remetente r where r.nome like :search%")
	Page<Remetente> findByNome(String search, Pageable pageable);

}
