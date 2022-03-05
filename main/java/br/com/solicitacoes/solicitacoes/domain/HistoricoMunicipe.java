package br.com.solicitacoes.solicitacoes.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@SuppressWarnings("serial")
@Entity
@Table(name = "historico_cliente")
public class HistoricoMunicipe extends AbstractEntity{

	
	@Column(name="data", nullable = false)
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data;
	
	
	@Column(name="hora", nullable = false)
	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime hora;
	
	@ManyToOne
	private Cliente cliente;
	
	private String usuario;
	
	@Lob
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	private HistoricoTipo tipo;

	public HistoricoTipo getTipo() {
		return tipo;
	}

	public void setTipo(HistoricoTipo tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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


	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	
}
