package br.com.solicitacoes.solicitacoes.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Bairro;

public interface BairroRepository extends JpaRepository<Bairro, Long>{

	@Query("select b from Bairro b where b.descricao like :search%")
	Page<Bairro> findByDescricao(String search, Pageable pageable);

	@Query("select b from Bairro b where b.descricao IN :titulos")
	Set<Bairro> findByTitulos(String[] titulos);

	@Query("select b.descricao from Bairro b where b.descricao like :termo% and b.ativo = true")
	List<String> findBairroByTermo(String termo);

	@Query("select b from Bairro b order by b.descricao")
	Page<Bairro> findAllByDescricao(Pageable pageable);

}
