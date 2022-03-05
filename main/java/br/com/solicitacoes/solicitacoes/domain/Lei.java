package br.com.solicitacoes.solicitacoes.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "leis")
public class Lei extends AbstractEntity{

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
	
	@Enumerated(EnumType.STRING)
	private LeiStatus status;
	
	@JsonIgnore
	@OneToMany(mappedBy = "lei", cascade = CascadeType.REMOVE)
	private List<AndamentoLei> andamentos;

	public List<AndamentoLei> getAndamentos() {
		return andamentos;
	}

	public void setAndamentos(List<AndamentoLei> andamentos) {
		this.andamentos = andamentos;
	}

	public LeiStatus getStatus() {
		return status;
	}

	public void setStatus(LeiStatus status) {
		this.status = status;
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
	
	
}
