package br.com.solicitacoes.solicitacoes.domain;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "requerimentos")
public class Requerimento extends AbstractEntity{

	@NotNull(message = "Informe a data")
	@Column(name="data")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data;
	
	@Lob
	@NotEmpty(message = "Digite a descrição")
	@Column(name="descricao", nullable = false)
	private String descricao;
	
	@NotEmpty(message = "Digite o assunto")
	@Column(name="assunto", nullable = false)
	private String assunto;
	
	@NotEmpty(message = "Digite a sessão")
	@Column(name="sessao", nullable = false)
	private String sessao;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "resposta_id_fk", referencedColumnName = "id", nullable = true)
	private RespostaRequerimento resposta;

	public RespostaRequerimento getResposta() {
		return resposta;
	}

	public void setResposta(RespostaRequerimento resposta) {
		this.resposta = resposta;
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

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getSessao() {
		return sessao;
	}

	public void setSessao(String sessao) {
		this.sessao = sessao;
	}
	
}
