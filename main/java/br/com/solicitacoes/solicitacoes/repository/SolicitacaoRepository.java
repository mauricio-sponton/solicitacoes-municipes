package br.com.solicitacoes.solicitacoes.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.solicitacoes.solicitacoes.domain.Solicitacao;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>, JpaSpecificationExecutor<Solicitacao>{

	@Query("select s from Solicitacao s where s.assunto.descricao like :search% or s.cliente.nome like :search% or s.usuario like :search%")
	Page<Solicitacao> findByAssuntoOrUsuarioOrCliente(String search, Pageable pageable);
	
	@Query("select s from Solicitacao s where s.cliente.id = :id")
	List<Solicitacao> findSolicitacaoPorClienteId(Long id);
	
	@Query("select s from Solicitacao s where extract (year from s.data) = :ano")
	List<Solicitacao> findSolicitacaoPorAno(int ano);

	@Query("select s from Solicitacao s where s.cliente.nome like :buscaAssunto%")
	Page<Solicitacao> findByCliente(Pageable pageable, String buscaAssunto);
	
	@Query("select s from Solicitacao s where s.status = 'ABERTO'")
	List<Solicitacao> findByStatusAberto();
	
	@Query("select s from Solicitacao s where s.status = 'FINALIZADO'")
	List<Solicitacao> findByStatusFinalizado();
	
	@Query("select s from Solicitacao s where s.status = 'ATRASADO'")
	List<Solicitacao> findByStatusAtrasado();
	
	@Query("select s from Solicitacao s where s.status = 'PENDENTE'")
	List<Solicitacao> findByStatusPendente();

	@Query("select s from Solicitacao s where s.cliente.id = :id")
	Page<Solicitacao> findTodasSolicitacoesDoMunicipe(Pageable pageable, Long id);

	@Query("select s from Solicitacao s where s.status = 'ABERTO' and s.cliente.id = :id")
	Page<Solicitacao> findTodasSolicitacoesAbertas(Pageable pageable, Long id);
	
	@Query("select s from Solicitacao s where s.status = 'ATRASADO' and s.cliente.id = :id")
	Page<Solicitacao> findTodasSolicitacoesAtrasadas(Pageable pageable, Long id);

	@Query("select s.assunto.descricao as assunto, s.bairro.descricao as bairro, s.usuario as usuario, s.data as data, s.solucao.resultado as resultado, s.solucao.aviso as aviso, s.id as id, s.status as situacao from Solicitacao s where s.status = 'PENDENTE' and s.cliente.id = :id")
	Page<Solicitacao> findTodasSolicitacoesPendentes(Pageable pageable, Long id);

	@Query("select s.assunto.descricao as assunto, s.bairro.descricao as bairro, s.usuario as usuario, s.data as data, s.solucao.resultado as resultado, s.solucao.aviso as aviso, s.id as id, s.status as situacao from Solicitacao s where s.status = 'FINALIZADO' and s.cliente.id = :id")
	Page<Solicitacao> findTodasSolicitacoesFinalizadas(Pageable pageable, Long id);
	
	@Query("select s from Solicitacao s where s.status = 'ABERTO' and s.bairro.id = :id")
	Page<Solicitacao> findTodasSolicitacoesAbertasDoBairro(Pageable pageable, Long id);
	
	@Query("select s from Solicitacao s where s.status = 'ATRASADO' and s.bairro.id = :id")
	Page<Solicitacao> findTodasSolicitacoesAtrasadasDoBairro(Pageable pageable, Long id);

	@Query("select s.cliente.nome as nome, s.assunto.descricao as assunto, s.usuario as usuario, s.data as data, s.solucao.resultado as resultado, s.solucao.aviso as aviso, s.id as id, s.status as situacao from Solicitacao s where s.status = 'PENDENTE' and s.bairro.id = :id")
	Page<Solicitacao> findTodasSolicitacoesPendentesDoBairro(Pageable pageable, Long id);

	@Query("select s.cliente.nome as nome, s.assunto.descricao as assunto, s.usuario as usuario, s.data as data, s.solucao.resultado as resultado, s.solucao.aviso as aviso, s.id as id, s.status as situacao from Solicitacao s where s.status = 'FINALIZADO' and s.bairro.id = :id")
	Page<Solicitacao> findTodasSolicitacoesFinalizadasDoBairro(Pageable pageable, Long id);

	@Query("select s from Solicitacao s where (s.assunto like '%'||:search||'%' or s.usuario like concat('%', concat(:search, '%'))) and s.status = 'ABERTO' and s.cliente.id = :id")
	Page<Solicitacao> findByAssuntoOrUsuario(String search, Pageable pageable, Long id);

	@Query("select s from Solicitacao s where s.solucao.resultado = 'Positivo'")
	List<Solicitacao> findByResultadosPositivos();
	
	@Query("select s from Solicitacao s where s.solucao.resultado = 'Negativo'")
	List<Solicitacao> findByResultadosNegativos();

	@Query("select s from Solicitacao s where s.solucao.resultado = 'Negativo' and s.id = :id")
	List<Solicitacao> findByResultadosNegativosById(Long id);

	@Query("select s from Solicitacao s where s.solucao.resultado = 'Positivo' and s.id = :id")
	List<Solicitacao> findByResultadosPositivosById(Long id);

	@Query("select s from Solicitacao s where s.solucao.aviso = true")
	List<Solicitacao> findByMunicipesAvisados();
	
	@Query("select s from Solicitacao s where s.solucao.aviso = false")
	List<Solicitacao> findByMunicipesNaoAvisados();

	@Query("select s from Solicitacao s where s.status = 'PENDENTE' and extract (year from s.data) = :ano")
	List<Solicitacao> findByStatusPendenteAndAno(int ano);

	@Query("select s from Solicitacao s where s.status = 'ATRASADO' and extract (year from s.data) = :ano")
	List<Solicitacao> findByStatusAtrasadoAndAno(int ano);

	@Query("select s from Solicitacao s where s.status = 'ABERTO' and extract (year from s.data) = :ano")
	List<Solicitacao> findByStatusAbertoAndAno(int ano);

	@Query("select s from Solicitacao s where s.status = 'FINALIZADO' and extract (year from s.data) = :ano")
	List<Solicitacao> findByStatusFinalizadoAndAno(int ano);

	@Query("select s from Solicitacao s where s.indicacao.id = :id")
	Solicitacao findByIndicacaoId(Long id);

	@Query("select s from Solicitacao s where s.indicado = false and s.indicacao.id = :id")
	List<Solicitacao> findAllNaoIndicadas(Long id);
	
}
