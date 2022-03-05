package br.com.solicitacoes.solicitacoes.specification;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.solicitacoes.solicitacoes.domain.Lei;
import br.com.solicitacoes.solicitacoes.domain.LeiStatus;

public class SpecificationLei {
	
	public static Specification<Lei> dataIgual(LocalDate data){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("data"), data);
	}
	@SuppressWarnings("serial")
	public static Specification<Lei> dataBetween(LocalDate inicio, LocalDate fim){
		return new Specification<Lei>() {
			@Override
			public Predicate toPredicate(Root<Lei> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("data")));
				return criteriaBuilder.between(root.get("data"), inicio, fim);
			}
		};
	}
	public static Specification<Lei> statusCriado(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), LeiStatus.CRIADA);
	}
	public static Specification<Lei> statusPropositura(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), LeiStatus.PROPOSITURA);
	}
	public static Specification<Lei> statusVetadoPrefeito(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), LeiStatus.VETADO_PREFEITO);
	}
	public static Specification<Lei> statusVetadoVereadores(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), LeiStatus.VETADO_VEREADORES);
	}
	public static Specification<Lei> statusNovaVotacao(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), LeiStatus.NOVA_VOTACAO);
	}
	public static Specification<Lei> statusAprovadoPrefeito(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), LeiStatus.APROVADO_PREFEITO);
	}
	public static Specification<Lei> statusAprovadoVereadores(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("status"), LeiStatus.APROVADO_VEREADORES);
	}
	public static Specification<Lei> assunto(String assunto){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.get("assunto"), "%" + assunto + "%");
	}
}
