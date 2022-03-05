package br.com.solicitacoes.solicitacoes.specification;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.SolicitacaoStatus;

public class SpecificationSolicitacao {
	public static Specification<Solicitacao> nomeCliente(String cliente){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.join("cliente").get("nome"), "%" + cliente + "%");
	}
	public static Specification<Solicitacao> assunto(String assunto){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.join("assunto").get("descricao"), "%" + assunto + "%");
	}
	public static Specification<Solicitacao> resultadoPositivo(String resultado){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.join("solucao").get("resultado"), "Positivo");
	}
	public static Specification<Solicitacao> resultadoNegativo(String resultado){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.join("solucao").get("resultado"), "Negativo");
	}
	public static Specification<Solicitacao> avisoTrue(String aviso){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.join("solucao").get("aviso"), true);
	}
	public static Specification<Solicitacao> indicadaTrue(String indicada){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("indicado"), true);
	}
	public static Specification<Solicitacao> indicadaFalse(String indicada){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("indicado"), false);
	}
	public static Specification<Solicitacao> avisoFalse(String aviso){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.join("solucao").get("aviso"), false);
	}
	public static Specification<Solicitacao> bairro(String bairro){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.join("bairro").get("descricao"), "%" + bairro + "%");
	}
	public static Specification<Solicitacao> usuario(String usuario){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.get("usuario"), "%" + usuario + "%");
	}
	public static Specification<Solicitacao> dataIgual(LocalDate data){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("data"), data);
	}
	@SuppressWarnings("serial")
	public static Specification<Solicitacao> dataBetween(LocalDate inicio, LocalDate fim){
		return new Specification<Solicitacao>() {
			@Override
			public Predicate toPredicate(Root<Solicitacao> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("data")));
				return criteriaBuilder.between(root.get("data"), inicio, fim);
			}
		};
	}
	public static Specification<Solicitacao> dataMaior(LocalDate data){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.greaterThanOrEqualTo(root.get("data"), data);
	}
	public static Specification<Solicitacao> dataMenor(LocalDate data){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.lessThanOrEqualTo(root.get("data"), data);
	}
	public static Specification<Solicitacao> statusAberto(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), SolicitacaoStatus.ABERTO);
	}
	public static Specification<Solicitacao> statusAtrasado(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), SolicitacaoStatus.ATRASADO);
	}
	public static Specification<Solicitacao> statusPendente(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), SolicitacaoStatus.PENDENTE);
	}
	public static Specification<Solicitacao> statusFinalizado(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), SolicitacaoStatus.FINALIZADO);
	}
	public static Specification<Solicitacao> dataPorAno(int ano){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("data")), ano);
	}
}
