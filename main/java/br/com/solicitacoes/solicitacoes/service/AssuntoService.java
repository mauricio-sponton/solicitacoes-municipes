package br.com.solicitacoes.solicitacoes.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.datatables.Datatables;
import br.com.solicitacoes.solicitacoes.datatables.DatatablesColunas;
import br.com.solicitacoes.solicitacoes.domain.Assunto;
import br.com.solicitacoes.solicitacoes.repository.AssuntoRepository;

@Service
public class AssuntoService {

	@Autowired
	private AssuntoRepository repository;
	
	@Autowired
	private Datatables datatables;
	
	@Transactional(readOnly = false)
	public void salvar(@Valid Assunto assunto) {
		repository.save(assunto);
	}

	@Transactional(readOnly = true)
	public Assunto buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Assunto> buscarTodosAssuntos() {
		return repository.findAll();
	}
	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.ASSUNTOS);
		Page<Assunto> page = null;
		if(datatables.getSearch().isEmpty()) { 
			page = repository.findAll(datatables.getPageable());
		}
		if(!datatables.getSearch().isEmpty()) {
			page = repository.findByDescricao(datatables.getSearch(), datatables.getPageable());
		}
				
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Set<Assunto> buscarPorTitulos(String[] titulos) {
		return repository.findByTitulos(titulos);
	}

	@Transactional(readOnly = true)
	public List<String> buscarAssuntoByTermo(String termo) {
		return repository.findAssuntoByTermo(termo);
	}

}
