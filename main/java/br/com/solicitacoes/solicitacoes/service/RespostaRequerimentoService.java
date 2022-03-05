package br.com.solicitacoes.solicitacoes.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.domain.RespostaRequerimento;
import br.com.solicitacoes.solicitacoes.repository.RespostaRequerimentoRepository;

@Service
public class RespostaRequerimentoService {
	
	@Autowired
	private RespostaRequerimentoRepository repository;

	@Transactional(readOnly = false)
	public void salvar(@Valid RespostaRequerimento resposta) {
		repository.save(resposta);
	}

	@Transactional(readOnly = true)
	public RespostaRequerimento buscarPorId(Long id) {
		return repository.findById(id).get();
	}
	
	@Transactional(readOnly = true)
	public RespostaRequerimento buscarPorRequerimentoId(Long id) {
		return repository.findByRequerimentoId(id);
	}
}
