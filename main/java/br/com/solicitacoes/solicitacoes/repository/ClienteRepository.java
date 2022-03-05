package br.com.solicitacoes.solicitacoes.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente>{

	@Query("select c from Cliente c where c.nome like :search% or c.email like :search%")
	Page<Cliente> findByNameOrEmail(String search, Pageable pageable);

	@Query("select c from Cliente c where c.nome IN :titulos")
	Set<Cliente> findByTitulos(String[] titulos);

	@Query("select c.nome from Cliente c where c.nome like :termo%")
	List<String> findClienteByTermo(String termo);

	@Query("select c from Cliente c join c.solicitacoes s where s.cliente.id = :id and s.status = 'ABERTO'")
	List<Cliente> findBySolicitacaoAberta(Long id);

	@Query("select c from Cliente c join c.solicitacoes s")
	Page<Cliente> findAllJoin(Pageable pageable);

	@Query("select distinct c from Cliente c join c.telefones t where t.cliente.id = c.id")
	List<Cliente> findAllETel();



}
