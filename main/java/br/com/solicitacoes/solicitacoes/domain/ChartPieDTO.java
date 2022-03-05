package br.com.solicitacoes.solicitacoes.domain;

public class ChartPieDTO {

	 private String valor;
	 private Integer quantidade;
	
	 public ChartPieDTO(String valor, Integer quantidade) {
			this.valor = valor;
			this.quantidade = quantidade;
		
	}

	public String getValor() {
		return valor;
	}

	public Integer getQuantidade() {
		return quantidade;
	}
 
	 
}
