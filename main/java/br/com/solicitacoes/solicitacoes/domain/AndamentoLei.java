package br.com.solicitacoes.solicitacoes.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@SuppressWarnings("serial")
@Entity
@Table(name = "andamentos")
public class AndamentoLei extends AbstractEntity{
	

	@NotNull(message = "Informe a data")
	@Column(name="data")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data;
	
	@Lob
	@NotEmpty(message = "Digite a descrição")
	@Column(name="descricao", nullable = false)
	private String descricao;
	
	@ManyToOne
	private Lei lei;
	
	//@NotEmpty(message = "Dê a resposta")
	@Column(name="tipo")
	private String tipo;

	public Lei getLei() {
		return lei;
	}

	public void setLei(Lei lei) {
		this.lei = lei;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
