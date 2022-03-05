package br.com.solicitacoes.solicitacoes.specification;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.solicitacoes.solicitacoes.domain.Requerimento;

public class SpecificationRequerimento {
	
	public static Specification<Requerimento> dataIgual(LocalDate data){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("data"), data);
	}
	@SuppressWarnings("serial")
	public static Specification<Requerimento> dataBetween(LocalDate inicio, LocalDate fim){
		return new Specification<Requerimento>() {
			@Override
			public Predicate toPredicate(Root<Requerimento> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("data")));
				return criteriaBuilder.between(root.get("data"), inicio, fim);
			}
		};
	}
	public static Specification<Requerimento> assunto(String assunto){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.get("assunto"), "%" + assunto + "%");
	}
}
