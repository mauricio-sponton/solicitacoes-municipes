package br.com.solicitacoes.solicitacoes.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.domain.RespostaIndicacao;
import br.com.solicitacoes.solicitacoes.repository.RespostaIndicacaoRepository;

@Service
public class RespostaIndicacaoService {
	
	@Autowired
	private RespostaIndicacaoRepository repository;
	
	@Transactional(readOnly = false)
	public void salvar(@Valid RespostaIndicacao resposta) {
		repository.save(resposta);
	}

	@Transactional(readOnly = true)
	public RespostaIndicacao buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public RespostaIndicacao buscarPorIndicacaoId(Long id) {
		return repository.findByIndicacaoId(id);
	}

}
