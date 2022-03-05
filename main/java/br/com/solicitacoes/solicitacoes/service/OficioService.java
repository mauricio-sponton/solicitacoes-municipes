package br.com.solicitacoes.solicitacoes.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.datatables.Datatables;
import br.com.solicitacoes.solicitacoes.datatables.DatatablesColunas;
import br.com.solicitacoes.solicitacoes.domain.Oficio;
import br.com.solicitacoes.solicitacoes.repository.OficioRepository;
import br.com.solicitacoes.solicitacoes.specification.SpecificationOficio;

@Service
public class OficioService {
	
	@Autowired
	private OficioRepository repository;

	@Autowired
	private Datatables datatables;
	
	@Transactional(readOnly = false)
	public void salvar(@Valid Oficio oficio) {
		repository.save(oficio);
	}

	@Transactional(readOnly = true)
	public Oficio buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Oficio> buscarTodosAssuntos() {
		return repository.findAll();
	}
	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request, LocalDate dataInicial, LocalDate dataFinal) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.OFICIOS);
		Page<Oficio> page = null;
		Specification<Oficio> data = null;
		if(datatables.getSearch().isEmpty()) { 
			page = repository.findAll(datatables.getPageable());
		}
		if(!datatables.getSearch().isEmpty()) {
			page = repository.findByAssunto(datatables.getSearch(), datatables.getPageable());
		}
		
		if (dataInicial != null) {
			data = SpecificationOficio.dataIgual(dataInicial);
		}

		if (dataInicial != null && dataFinal != null) {
			data = SpecificationOficio.dataBetween(dataInicial, dataFinal);
		}

		if (dataInicial != null) {
			page = repository.findAll(Specification.where(data),
					datatables.getPageable());
		}
				
		return datatables.getResponse(page);
	}

}
