package br.com.solicitacoes.solicitacoes.specification;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.solicitacoes.solicitacoes.domain.Reuniao;

public class SpecificationReuniao {
	
	public static Specification<Reuniao> dataIgual(LocalDate data){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("data"), data);
	}
	@SuppressWarnings("serial")
	public static Specification<Reuniao> dataBetween(LocalDate inicio, LocalDate fim){
		return new Specification<Reuniao>() {
			@Override
			public Predicate toPredicate(Root<Reuniao> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("data")));
				return criteriaBuilder.between(root.get("data"), inicio, fim);
			}
		};
	}
	public static Specification<Reuniao> assunto(String assunto){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.get("assunto"), "%" + assunto + "%");
	}
}
