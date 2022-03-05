package br.com.solicitacoes.solicitacoes.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.datatables.Datatables;
import br.com.solicitacoes.solicitacoes.datatables.DatatablesColunas;
import br.com.solicitacoes.solicitacoes.domain.Remetente;
import br.com.solicitacoes.solicitacoes.repository.RemetenteRepository;

@Service
public class RemetenteService {
	
	@Autowired
	private RemetenteRepository repository;
	
	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvar(@Valid Remetente remetente) {
		repository.save(remetente);
	}

	@Transactional(readOnly = true)
	public Remetente buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);
		
	}
	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.REMETENTES);
		Page<Remetente> page = null;
		if(datatables.getSearch().isEmpty()) { 
			page = repository.findAll(datatables.getPageable());
		}
		if(!datatables.getSearch().isEmpty()) {
			page = repository.findByNome(datatables.getSearch(), datatables.getPageable());
		}
				
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public List<Remetente> buscarTodosRemetentes() {
		return repository.findAll();
	}

}
