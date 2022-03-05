package br.com.solicitacoes.solicitacoes.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.domain.Solucao;
import br.com.solicitacoes.solicitacoes.repository.SolucaoRepository;

@Service
public class SolucaoService {
	
	@Autowired
	private SolucaoRepository repository;

	@Transactional(readOnly = false)
	public void salvar(@Valid Solucao solucao) {
		repository.save(solucao);
	}

	@Transactional(readOnly = true)
	public Solucao buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Solucao buscarPorSolcitacaoId(Long id) {
		return repository.findBySolicitacaoId(id);
	}

	@Transactional(readOnly = true)
	public List<Solucao> buscarTodasSolucoes() {	
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Solucao> buscarPorResultadoPositivoEMesEAno(int mes, int ano) {
		return repository.findByResultadoPositivoAndMesAndAno(mes, ano);
	}

	@Transactional(readOnly = true)
	public List<Solucao> buscarPorResultadoNegativoEMesEAno(int mes, int ano) {
		return repository.findByResultadoNegativoAndMesAndAno(mes, ano);
	}

	@Transactional(readOnly = true)
	public List<Solucao> buscarPorAvisoSimEMesEAno(int mes, int ano) {
		return repository.findByAvisoSimAndMesEAno(mes, ano);
	}
	@Transactional(readOnly = true)
	public List<Solucao> buscarPorAvisoNaoEMesEAno(int mes, int ano) {
		return repository.findByAvisoNaoAndMesEAno(mes, ano);
	}

}
