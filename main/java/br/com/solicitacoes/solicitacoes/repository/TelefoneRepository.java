package br.com.solicitacoes.solicitacoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Telefone;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

	@Query("select t from Telefone t where t.cliente.id = :id")
	List<Telefone> findTelefoneByMunicipeId(Long id);

}
