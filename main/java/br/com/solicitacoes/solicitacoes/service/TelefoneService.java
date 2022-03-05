package br.com.solicitacoes.solicitacoes.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import br.com.solicitacoes.solicitacoes.domain.Cliente;
import br.com.solicitacoes.solicitacoes.domain.Telefone;
import br.com.solicitacoes.solicitacoes.domain.TelefoneDTO;
import br.com.solicitacoes.solicitacoes.repository.TelefoneRepository;

@Service
public class TelefoneService {

	@Autowired
	private TelefoneRepository repository;
	
	@Transactional(readOnly = true)
	public List<Telefone> buscarTelefonePorMunicipeId(Long id) {
		return repository.findTelefoneByMunicipeId(id);
	}

	@Transactional(readOnly = false)
	public void salvarTodos(List<Telefone> telefones) {
		repository.saveAll(telefones);
		
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);
	}

	public void addFone(ModelMap model, int condicao) {
		TelefoneDTO novosFones = new TelefoneDTO();
		for (int i = 1; i <= condicao; i++) {
			novosFones.addTelefone(new Telefone());
		}
		model.addAttribute("novosFones", novosFones);	
	}

	public void editFone(ModelMap model, @Valid Cliente cliente) {
		List<Telefone> fonesCadastrados = new ArrayList<>();
		buscarTelefonePorMunicipeId(cliente.getId()).iterator()
				.forEachRemaining(fonesCadastrados::add);
		model.addAttribute("fonesCadastrados", new TelefoneDTO(fonesCadastrados));
		
	}

	public List<Telefone> buscarTodos() {
		return repository.findAll();
	}

}
