package br.com.solicitacoes.solicitacoes.specification;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.solicitacoes.solicitacoes.domain.Cliente;

public class SpecificationMunicipe {

	public static Specification<Cliente> bairro(String bairro) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
				.like(root.join("endereco").join("bairro").get("descricao"), "%" + bairro + "%");
	}

	public static Specification<Cliente> nome(String nome) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
	}

	public static Specification<Cliente> email(String email) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("email"), "%" + email + "%");
	}

	public static Specification<Cliente> apoiadorTrue(String apoiador) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("apoiador"), true);
	}

	public static Specification<Cliente> apoiadorFalse(String apoiador) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("apoiador"), false);
	}

	public static Specification<Cliente> mes(Integer data) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
				.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("dataNascimento")), data);
	}

	public static Specification<Cliente> ano(Integer data) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
				.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("dataNascimento")), data);
	}

	public static Specification<Cliente> mesAno(Integer mes, Integer ano) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
				criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("dataNascimento")), ano),
				criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("dataNascimento")),
						mes));

	}

	public static Specification<Cliente> diaAno(Integer ano, Integer dia) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
				criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("dataNascimento")), ano),
				criteriaBuilder.equal(criteriaBuilder.function("DAY", Integer.class, root.get("dataNascimento")), dia));

	}

	public static Specification<Cliente> diaMes(Integer mes, Integer dia) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
				criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("dataNascimento")),
						mes),
				criteriaBuilder.equal(criteriaBuilder.function("DAY", Integer.class, root.get("dataNascimento")), dia));

	}

	public static Specification<Cliente> diaMesAno(Integer mes, Integer ano, Integer dia) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
				criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("dataNascimento")),
						mes),
				criteriaBuilder.equal(criteriaBuilder.function("DAY", Integer.class, root.get("dataNascimento")), dia),
				criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("dataNascimento")),
						ano));

	}

	public static Specification<Cliente> dataIgual(LocalDate data) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("data"), data);
	}

	@SuppressWarnings("serial")
	public static Specification<Cliente> dataBetween(LocalDate inicio, LocalDate fim) {
		return new Specification<Cliente>() {
			@Override
			public Predicate toPredicate(Root<Cliente> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("data")));
				return criteriaBuilder.between(root.get("data"), inicio, fim);
			}
		};
	}

	public static Specification<Cliente> dia(Integer data) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
				.equal(criteriaBuilder.function("DAY", Integer.class, root.get("dataNascimento")), data);
	}
}
