package br.com.solicitacoes.solicitacoes.domain;

public class ChartDTO {

	 private String mes;
	 private Integer positivas;
	 private Integer negativas;
	
	 public ChartDTO(String mes, Integer positivas, Integer negativas) {
			this.mes = mes;
			this.positivas = positivas;
			this.negativas = negativas;
		}
	 
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public Integer getPositivas() {
		return positivas;
	}
	public void setPositivas(Integer positivas) {
		this.positivas = positivas;
	}
	public Integer getNegativas() {
		return negativas;
	}
	public void setNegativas(Integer negativas) {
		this.negativas = negativas;
	}
	
	
	
	
	 
	 
	 
}
