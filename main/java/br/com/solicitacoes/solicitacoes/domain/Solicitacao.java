package br.com.solicitacoes.solicitacoes.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "solicitacoes")
public class Solicitacao extends AbstractEntity{
	

	@Column(name="data")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data;
	

	@Column(name="hora")
	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime hora;
	
	@Lob
	@NotEmpty(message = "Digite a descrição da solicitação")
	@Column(name="descricao", nullable = false)
	private String descricao;

	private String usuario;
	
	@ManyToOne
	@JoinColumn(name = "cliente_fk")
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name = "assunto_fk")
	private Assunto assunto;
	
	@ManyToOne
	@JoinColumn(name = "bairro_fk")
	private Bairro bairro;
	
	@Enumerated(EnumType.STRING)
	private SolicitacaoStatus status;
	
	@Column(name = "indicado", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean indicado;
	
	@JsonIgnore
	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.REMOVE)
	private List<HistoricoSolicitacao> historicos;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "solucao_id_fk", referencedColumnName = "id", nullable = true)
	private Solucao solucao;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "indicacao_id_fk", referencedColumnName = "id", nullable = true)
	private Indicacao indicacao;
	
	public Indicacao getIndicacao() {
		return indicacao;
	}

	public void setIndicacao(Indicacao indicacao) {
		this.indicacao = indicacao;
	}

	public boolean isIndicado() {
		return indicado;
	}

	public void setIndicado(boolean indicado) {
		this.indicado = indicado;
	}

	public Solucao getSolucao() {
		return solucao;
	}

	public void setSolucao(Solucao solucao) {
		this.solucao = solucao;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public List<HistoricoSolicitacao> getHistoricos() {
		return historicos;
	}
	
	public Assunto getAssunto() {
		return assunto;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}



	public void setHistoricos(List<HistoricoSolicitacao> historicos) {
		this.historicos = historicos;
	}

	public SolicitacaoStatus getStatus() {
		return status;
	}

	public void setStatus(SolicitacaoStatus status) {
		this.status = status;
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

//	public @Valid Assunto getAssunto() {
//		return assunto;
//	}
//
//	public void setAssunto(@Valid Assunto assunto) {
//		this.assunto = assunto;
//	}

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

	@JsonIgnore
	public boolean isAtrasado() {
		return this.getData().plusDays(29).get(ChronoField.DAY_OF_YEAR) <= LocalDate.now().get(ChronoField.DAY_OF_YEAR);
	}
	
	

}
