package br.com.solicitacoes.solicitacoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Solucao;

public interface SolucaoRepository extends JpaRepository<Solucao, Long>{

	@Query("select s from Solucao s where s.solicitacao.id = :id")
	Solucao findBySolicitacaoId(Long id);

	@Query("select s from Solucao s where extract (year from s.data) = :ano")
	List<Solucao> findByAno(int ano);

	@Query("select s from Solucao s where s.resultado = 'Positivo' and extract (month from s.data) = :mes and extract (year from s.data) = :ano")
	List<Solucao> findByResultadoPositivoAndMesAndAno(int mes, int ano);

	@Query("select s from Solucao s where s.resultado = 'Negativo' and extract (month from s.data) = :mes and extract (year from s.data) = :ano")
	List<Solucao> findByResultadoNegativoAndMesAndAno(int mes, int ano);

	@Query("select s from Solucao s where s.aviso = true and extract (month from s.data) = :mes and extract (year from s.data) = :ano")
	List<Solucao> findByAvisoSimAndMesEAno(int mes, int ano);
	
	@Query("select s from Solucao s where s.aviso = false and extract (month from s.data) = :mes and extract (year from s.data) = :ano")
	List<Solucao> findByAvisoNaoAndMesEAno(int mes, int ano);

	

}
