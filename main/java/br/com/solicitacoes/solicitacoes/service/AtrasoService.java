package br.com.solicitacoes.solicitacoes.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.domain.Atraso;
import br.com.solicitacoes.solicitacoes.repository.AtrasoRepository;

@Service
public class AtrasoService {

	@Autowired
	private AtrasoRepository repository;
	
	@Transactional(readOnly = false)
	public void salvar(@Valid Atraso atraso) {
		repository.save(atraso);	
	}

	@Transactional(readOnly = true)
	public List<Atraso> buscarPorSolicitacao(Long id) {
		return repository.findBySolicitacaoId(id);
	}

}
