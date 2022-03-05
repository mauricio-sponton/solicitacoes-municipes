package br.com.solicitacoes.solicitacoes.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Assunto;

public interface AssuntoRepository extends JpaRepository<Assunto, Long>{

	@Query("select s from Assunto s where s.descricao like :search%")
	Page<Assunto> findByDescricao(String search, Pageable pageable);

	@Query("select s from Assunto s where s.descricao IN :titulos")
	Set<Assunto> findByTitulos(String[] titulos);

	@Query("select s.descricao from Assunto s where s.descricao like :termo% and s.ativo = true")
	List<String> findAssuntoByTermo(String termo);

	
}
