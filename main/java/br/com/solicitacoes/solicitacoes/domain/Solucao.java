package br.com.solicitacoes.solicitacoes.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@SuppressWarnings("serial")
@Entity
@Table(name = "solucoes")
public class Solucao extends AbstractEntity{
	

	@Column(name="data")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data;
	

	@Column(name="hora")
	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime hora;
	
	@Lob
	@NotEmpty(message = "Digite a descrição da solução")
	@Column(name="descricao", nullable = false)
	private String descricao;
	
	private String usuario;
	
	
	@OneToOne(mappedBy = "solucao")
	private Solicitacao solicitacao;
	
	@NotBlank(message="Insira o resultado")
	@Column(name="resultado", nullable = false)
	private String resultado;
	
	@Column(name = "aviso", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean aviso;
	
	public boolean isAviso() {
		return aviso;
	}

	public String condicao() {
		if(isAviso() == true) {
			return "Sim";
		}else {
			return "Não";
		}
	}
	
	public void setAviso(boolean aviso) {
		this.aviso = aviso;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	

}
