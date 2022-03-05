package br.com.solicitacoes.solicitacoes.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.datatables.Datatables;
import br.com.solicitacoes.solicitacoes.datatables.DatatablesColunas;
import br.com.solicitacoes.solicitacoes.domain.Cliente;
import br.com.solicitacoes.solicitacoes.repository.ClienteRepository;
import br.com.solicitacoes.solicitacoes.specification.SpecificationMunicipe;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository repository;

	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvar(@Valid Cliente cliente) {
		repository.save(cliente);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request, String buscaCliente, String buscaBairro,
			String buscaEmail, Integer buscaMes, Integer buscaAno, Integer buscaDia, String apoiador) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.CLIENTES);
		Page<Cliente> page = null;
		Specification<Cliente> apoiadores = null;
		Specification<Cliente> data = null;

		if (datatables.getSearch().isEmpty()) {
			page = repository.findAll(datatables.getPageable());
		}
		if (!datatables.getSearch().isEmpty()) {
			page = repository.findByNameOrEmail(datatables.getSearch(), datatables.getPageable());
		}

		if (apoiador != null) {
			if (apoiador.equals("true")) {
				apoiadores = SpecificationMunicipe.apoiadorTrue(apoiador);
			} else {
				apoiadores = SpecificationMunicipe.apoiadorFalse(apoiador);
			}
		}
		if (buscaDia != null) {
			data = SpecificationMunicipe.dia(buscaDia);
		}

		if (buscaMes != null) {
			data = SpecificationMunicipe.mes(buscaMes);
		}
		if (buscaAno != null) {
			data = SpecificationMunicipe.ano(buscaAno);
		}

		if (buscaMes != null && buscaAno != null && buscaDia != null) {
			data = SpecificationMunicipe.diaMesAno(buscaMes, buscaAno, buscaDia);
		} else {
			if (buscaMes != null && buscaAno != null) {
				data = SpecificationMunicipe.mesAno(buscaMes, buscaAno);
			}
			if (buscaMes != null && buscaDia != null) {
				data = SpecificationMunicipe.diaMes(buscaMes, buscaDia);
			}
			if (buscaDia != null && buscaAno != null) {
				data = SpecificationMunicipe.diaAno(buscaAno, buscaDia);
			}
		}

		if (!buscaCliente.isEmpty() || !buscaBairro.isEmpty() || !buscaEmail.isEmpty() || apoiador != null
				|| buscaMes != null || buscaAno != null || buscaDia != null) {
			page = repository.findAll(Specification.where(SpecificationMunicipe.nome(buscaCliente))
					.and(SpecificationMunicipe.bairro(buscaBairro)).and(SpecificationMunicipe.email(buscaEmail))
					.and(data).and(apoiadores), datatables.getPageable());
		}

		return datatables.getResponse(page);
	}
	
	public List<Cliente> buscarTodosClientesComFiltro(String buscaCliente, String buscaBairro,
			String buscaEmail, Integer buscaMes, Integer buscaAno, Integer buscaDia, String apoiador) {
		List<Cliente> page = null;
		Specification<Cliente> apoiadores = null;
		Specification<Cliente> data = null;
		System.out.println(buscaBairro);
		if (apoiador != null) {
			if (apoiador.equals("true")) {
				apoiadores = SpecificationMunicipe.apoiadorTrue(apoiador);
			} else {
				apoiadores = SpecificationMunicipe.apoiadorFalse(apoiador);
			}
		}
		if (buscaDia != null) {
			data = SpecificationMunicipe.dia(buscaDia);
		}

		if (buscaMes != null) {
			data = SpecificationMunicipe.mes(buscaMes);
		}
		if (buscaAno != null) {
			data = SpecificationMunicipe.ano(buscaAno);
		}

		if (buscaMes != null && buscaAno != null && buscaDia != null) {
			data = SpecificationMunicipe.diaMesAno(buscaMes, buscaAno, buscaDia);
		} else {
			if (buscaMes != null && buscaAno != null) {
				data = SpecificationMunicipe.mesAno(buscaMes, buscaAno);
			}
			if (buscaMes != null && buscaDia != null) {
				data = SpecificationMunicipe.diaMes(buscaMes, buscaDia);
			}
			if (buscaDia != null && buscaAno != null) {
				data = SpecificationMunicipe.diaAno(buscaAno, buscaDia);
			}
		}

		if (!buscaCliente.isEmpty() || !buscaBairro.isEmpty() || !buscaEmail.isEmpty() || apoiador != null
				|| buscaMes != null || buscaAno != null || buscaDia != null) {
			page = repository.findAll(Specification.where(SpecificationMunicipe.nome(buscaCliente))
					.and(SpecificationMunicipe.bairro(buscaBairro)).and(SpecificationMunicipe.email(buscaEmail))
					.and(data).and(apoiadores));
		}else {
			page = repository.findAll();
		}
		
		return page;
	}
	

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodosSemFiltro(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.CLIENTES);
		Page<Cliente> page = null;

		if (datatables.getSearch().isEmpty()) {
			page = repository.findAll(datatables.getPageable());
		}
		if (!datatables.getSearch().isEmpty()) {
			page = repository.findByNameOrEmail(datatables.getSearch(), datatables.getPageable());
		}

		return datatables.getResponse(page);
	}

	public Cliente buscarPorId(Long id) {

		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);

	}

	@Transactional(readOnly = true)
	public List<Cliente> buscarTodosClientes() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Set<Cliente> buscarPorTitulos(String[] titulos) {
		return repository.findByTitulos(titulos);
	}

	@Transactional(readOnly = true)
	public List<String> buscarClienteByTermo(String termo) {
		return repository.findClienteByTermo(termo);
	}

	@Transactional(readOnly = true)
	public List<Cliente> buscarSolicitacoesAbertas(Long id) {
		return repository.findBySolicitacaoAberta(id);
	}

	
	
	
//	@Transactional(readOnly = false)
//	public void somarSolicitacoes() {
//		List<Cliente> municipes = repository.findAll();
//		for(Cliente c : municipes) {
//			c.setAbertas(c.getSolicitacoesAbertas() == null ? 0 : c.getSolicitacoesAbertas());
//			c.setAtrasadas(c.getSolicitacoesAtrasadas() == null ? 0 : c.getSolicitacoesAtrasadas());
//			c.setFinalizadas(c.getSolicitacoesFinalizadas() == null ? 0 : c.getSolicitacoesFinalizadas());
//			c.setPendentes(c.getSolicitacoesPendentes() == null ? 0 : c.getSolicitacoesPendentes());
//			repository.save(c);
//		}
//		
//	}

}