package br.com.solicitacoes.solicitacoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.HistoricoMunicipe;

public interface HistoricoMunicipeRepository
		extends JpaRepository<HistoricoMunicipe, Long>, JpaSpecificationExecutor<HistoricoMunicipe> {

	@Query("select h from HistoricoMunicipe h where h.cliente.id = :id order by h.id desc")
	List<HistoricoMunicipe> findHistoricoByClienteId(Long id);

}
