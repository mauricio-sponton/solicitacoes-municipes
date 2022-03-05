package br.com.solicitacoes.solicitacoes.domain;

import java.util.ArrayList;
import java.util.List;

public class TelefoneDTO {

	private List<Telefone> telefones;
	
	public TelefoneDTO() {
		this.telefones = new ArrayList<>();
	}
	public TelefoneDTO(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
	public void addTelefone(Telefone telefone) {
		this.telefones.add(telefone);
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
}
