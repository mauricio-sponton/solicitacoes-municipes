package br.com.solicitacoes.solicitacoes.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "clientes")
public class Cliente extends AbstractEntity{

	@NotBlank(message="Informe seu nome")
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Email(message="Email deve ser válido")
	@NotBlank(message="Informe seu email")
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "rg")
	private String rg;
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_id_fk", referencedColumnName = "id")
	private Endereco endereco;
	
	@NotNull(message = "Informe uma data de nascimento")
	@PastOrPresent(message = "Informe uma data válida")
	@Column(name="data_nasc")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dataNascimento;

	@JsonIgnore
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.REMOVE)
	private List<Solicitacao> solicitacoes;
	
	@JsonIgnore
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.REMOVE)
	private List<HistoricoMunicipe> historicos;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.REMOVE)
	private List<Telefone> telefones;

	@Column(name = "apoiador", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean apoiador;
	
	@Column(name = "apoiador_descricao")
	private String apoiador_desc;
	
	public String getApoiador_desc() {
		return apoiador_desc;
	}

	public void setApoiador_desc(String apoiador_desc) {
		this.apoiador_desc = apoiador_desc;
	}

	public void setApoiador(boolean apoiador) {
		this.apoiador = apoiador;
	}

	public boolean isApoiador() {
		return apoiador;
	}


	public List<HistoricoMunicipe> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(List<HistoricoMunicipe> historicos) {
		this.historicos = historicos;
	}

	public List<Solicitacao> getSolicitacoes() {
		return solicitacoes;
	}
	
	
	public void setSolicitacoes(List<Solicitacao> solicitacoes) {
		this.solicitacoes = solicitacoes;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
}
