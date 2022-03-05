package br.com.solicitacoes.solicitacoes.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.domain.AndamentoLei;
import br.com.solicitacoes.solicitacoes.repository.AndamentoLeiRepository;

@Service
public class AndamentoLeiService {

	@Autowired
	private AndamentoLeiRepository repository;

	@Transactional(readOnly = false)
	public void salvar(@Valid AndamentoLei andamento) {
		repository.save(andamento);
	}

	@Transactional(readOnly = true)
	public AndamentoLei buscarPorId(Long id) {
		return repository.findById(id).get();
	}
	
	@Transactional(readOnly = true)
	public List<AndamentoLei> buscarPorLeiId(Long id) {
		return repository.findByLeiId(id);
	}
}
