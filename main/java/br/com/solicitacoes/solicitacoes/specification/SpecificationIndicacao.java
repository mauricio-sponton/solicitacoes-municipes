package br.com.solicitacoes.solicitacoes.specification;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.solicitacoes.solicitacoes.domain.Indicacao;

public class SpecificationIndicacao {
	
	public static Specification<Indicacao> dataIgual(LocalDate data){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("data"), data);
	}
	@SuppressWarnings("serial")
	public static Specification<Indicacao> dataBetween(LocalDate inicio, LocalDate fim){
		return new Specification<Indicacao>() {
			@Override
			public Predicate toPredicate(Root<Indicacao> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("data")));
				return criteriaBuilder.between(root.get("data"), inicio, fim);
			}
		};
	}
	public static Specification<Indicacao> assunto(String assunto){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.get("assunto"), "%" + assunto + "%");
	}
	public static Specification<Indicacao> sessao(String sessao){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(root.get("sessao"), "%" + sessao + "%");
	}
}
