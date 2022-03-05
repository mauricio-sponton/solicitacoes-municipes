package br.com.solicitacoes.solicitacoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.AndamentoLei;

public interface AndamentoLeiRepository extends JpaRepository<AndamentoLei, Long>{

	@Query("select a from AndamentoLei a where a.lei.id = :id")
	List<AndamentoLei> findByLeiId(Long id);

}
