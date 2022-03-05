package br.com.solicitacoes.solicitacoes.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@SuppressWarnings("serial")
@Entity
@Table(name = "respostas_requerimentos")
public class RespostaRequerimento extends AbstractEntity{

	@NotNull(message = "Informe a data")
	@Column(name="data")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data;
	
	@Lob
	@NotEmpty(message = "Digite a descrição")
	@Column(name="descricao", nullable = false)
	private String descricao;
	
	@OneToOne(mappedBy = "resposta")
	private Requerimento requerimento;

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
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