package br.com.solicitacoes.solicitacoes.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "bairros")
public class Bairro extends AbstractEntity{

	@NotBlank(message="Informe a descrição do assunto")
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "ativo", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean ativo;
	
	@JsonIgnore
	@OneToMany(mappedBy = "bairro", cascade = CascadeType.REMOVE)
	private List<Solicitacao> solicitacoes;
	
	@JsonIgnore
	@OneToMany(mappedBy = "bairro", cascade = CascadeType.REMOVE)
	private List<Endereco> enderecos;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Solicitacao> getSolicitacoes() {
		return solicitacoes;
	}

	public void setSolicitacoes(List<Solicitacao> solicitacoes) {
		this.solicitacoes = solicitacoes;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	
	
	
}
