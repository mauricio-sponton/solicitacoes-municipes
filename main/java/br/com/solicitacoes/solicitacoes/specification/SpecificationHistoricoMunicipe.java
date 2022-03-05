package br.com.solicitacoes.solicitacoes.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import br.com.solicitacoes.solicitacoes.domain.HistoricoMunicipe;
import br.com.solicitacoes.solicitacoes.domain.HistoricoTipo;

public class SpecificationHistoricoMunicipe {

	public static Specification<HistoricoMunicipe> idMunicipe(Long id){
		return (root, criteriaQuery, criteriaBuilder) -> 
		criteriaBuilder.equal(root.join("cliente").get("id"), id);
	}
	public static Specification<HistoricoMunicipe> solicitacaoAberta(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("tipo"), HistoricoTipo.SOLICITACAO_ABERTA);
	}
	public static Specification<HistoricoMunicipe> novoMunicipe(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("tipo"), HistoricoTipo.CLIENTE_NEW);
	}
	public static Specification<HistoricoMunicipe> editMunicipe(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("tipo"), HistoricoTipo.CLIENTE_EDIT);
	}
	public static Specification<HistoricoMunicipe> dataIgual(LocalDate data){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("data"), data);
	}
	
	public static Specification<HistoricoMunicipe> dataBetween(LocalDate inicio, LocalDate fim){			
				return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("data"), inicio, fim);	
	}
}
