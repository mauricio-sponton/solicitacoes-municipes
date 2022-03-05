package br.com.solicitacoes.solicitacoes.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import br.com.solicitacoes.solicitacoes.domain.HistoricoSolicitacao;
import br.com.solicitacoes.solicitacoes.domain.HistoricoTipo;

public class SpecificationHistoricoSolicitacao {

	public static Specification<HistoricoSolicitacao> idSolicitacao(Long id){
		return (root, criteriaQuery, criteriaBuilder) -> 
		criteriaBuilder.equal(root.join("solicitacao").get("id"), id);
	}
	public static Specification<HistoricoSolicitacao> solicitacaoAberta(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("tipo"), HistoricoTipo.SOLICITACAO_ABERTA);
	}
	public static Specification<HistoricoSolicitacao> solicitacaoAtrasada(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("tipo"), HistoricoTipo.SOLICITACAO_ATRASADA);
	}
	public static Specification<HistoricoSolicitacao> solicitacaoPendente(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("tipo"), HistoricoTipo.SOLICITACAO_PENDENTE);
	}
	public static Specification<HistoricoSolicitacao> solicitacaoEdit(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("tipo"), HistoricoTipo.SOLICITACAO_EDIT);
	}
	public static Specification<HistoricoSolicitacao> solucaoEdit(String status){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("tipo"), HistoricoTipo.SOLUCAO_EDIT);
	}
	public static Specification<HistoricoSolicitacao> dataIgual(LocalDate data){
		return (root, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("data"), data);
	}
	public static Specification<HistoricoSolicitacao> dataBetween(LocalDate inicio, LocalDate fim){			
				return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("data"), inicio, fim);	
	}
}
