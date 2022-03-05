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
import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.SolicitacaoStatus;
import br.com.solicitacoes.solicitacoes.domain.Solucao;
import br.com.solicitacoes.solicitacoes.repository.SolicitacaoRepository;
import br.com.solicitacoes.solicitacoes.repository.SolucaoRepository;
import br.com.solicitacoes.solicitacoes.specification.SpecificationSolicitacao;

@Service
public class SolicitacaoService {

	@Autowired
	private Datatables datatables;

	@Autowired
	private SolicitacaoRepository repository;

	@Autowired
	private SolucaoRepository solucaoRepository;

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request, String buscaAssunto, String buscaCliente,
			String buscaUsuario, String buscaBairro, LocalDate buscaData, LocalDate buscaDataFinal, String status,
			String resultado, String aviso, String indicada) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.SOLICITACOES);
		Page<Solicitacao> page = null;
		Specification<Solicitacao> situacao = null;
		Specification<Solicitacao> resultado_solucao = null;
		Specification<Solicitacao> aviso_solucao = null;
		Specification<Solicitacao> data = null;
		Specification<Solicitacao> indicacao = null;

		if (datatables.getSearch().isEmpty()) {
			page = repository.findAll(datatables.getPageable());
		}
		if (!datatables.getSearch().isEmpty()) {
			page = repository.findByAssuntoOrUsuarioOrCliente(datatables.getSearch(), datatables.getPageable());
		}
		if (resultado != null) {
			if (resultado.equals("Positivo")) {
				resultado_solucao = SpecificationSolicitacao.resultadoPositivo(resultado);
			} else {
				resultado_solucao = SpecificationSolicitacao.resultadoNegativo(resultado);
			}

		}
		if (aviso != null) {
			if (aviso.equals("true")) {
				aviso_solucao = SpecificationSolicitacao.avisoTrue(aviso);
			} else {
				aviso_solucao = SpecificationSolicitacao.avisoFalse(aviso);
			}

		}
		if (indicada != null) {
			if (indicada.equals("true")) {
				indicacao = SpecificationSolicitacao.indicadaTrue(indicada);
			} else {
				indicacao = SpecificationSolicitacao.indicadaFalse(indicada);
			}

		}
		if (status != null) {
			switch (status) {
			case "finalizado":
				situacao = SpecificationSolicitacao.statusFinalizado(status);
				break;
			case "atrasado":
				situacao = SpecificationSolicitacao.statusAtrasado(status);
				break;
			case "pendente":
				situacao = SpecificationSolicitacao.statusPendente(status);
				break;
			case "aberto":
				situacao = SpecificationSolicitacao.statusAberto(status);
				break;

			}
		}
		if (buscaData != null) {
			data = SpecificationSolicitacao.dataIgual(buscaData);

		}
		if (buscaData != null && buscaDataFinal != null) {
			data = SpecificationSolicitacao.dataBetween(buscaData, buscaDataFinal);

		}

		if ((!buscaAssunto.isEmpty() || !buscaBairro.isEmpty() || !buscaCliente.isEmpty() || !buscaUsuario.isEmpty())
				|| buscaData != null || status != null || resultado != null || aviso != null || indicada != null) {
			page = repository.findAll(Specification.where(SpecificationSolicitacao.assunto(buscaAssunto))
					.and(SpecificationSolicitacao.nomeCliente(buscaCliente))
					.and(SpecificationSolicitacao.usuario(buscaUsuario))
					.and(SpecificationSolicitacao.bairro(buscaBairro)).and(situacao).and(data).and(resultado_solucao)
					.and(aviso_solucao).and(indicacao), datatables.getPageable());
		}

		return datatables.getResponse(page);
	}

	@Transactional(readOnly = false)
	public void salvar(@Valid Solicitacao solicitacao) {
		repository.save(solicitacao);
	}

	@Transactional(readOnly = true)
	public Solicitacao buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);

	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarSolicitacaoPorClienteId(Long id) {
		return repository.findSolicitacaoPorClienteId(id);
	}

	@Transactional(readOnly = true)
	public Object[] buscarDataPorAno(int ano) {
		List<Solicitacao> solicitacoes = repository.findSolicitacaoPorAno(ano);
		List<Solucao> solucoes = solucaoRepository.findByAno(ano);
		return new Object[] { solicitacoes, solucoes };
	}

	@Transactional(readOnly = true)
	public Object[] buscarPorResultados(Long id) {
		List<Solicitacao> positivos = repository.findByResultadosPositivosById(id);
		List<Solicitacao> negativos = repository.findByResultadosNegativosById(id);
		return new Object[] { positivos, negativos };
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarTodasSolicitacoes() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarSolicitacaoPorStatusAberto() {
		return repository.findByStatusAberto();
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarSolicitacaoPorStatusFinalizado() {
		return repository.findByStatusFinalizado();
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarSolicitacaoPorStatusAtrasado() {
		return repository.findByStatusAtrasado();
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarSolicitacaoPorStatusPendente() {
		return repository.findByStatusPendente();
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodasSolicitacoesDoMunicipe(HttpServletRequest request, Long id) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.SOLICITACOES);
		Page<Solicitacao> page = null;
		if (datatables.getSearch().isEmpty()) {
			page = repository.findTodasSolicitacoesDoMunicipe(datatables.getPageable(), id);
		}
		if (!datatables.getSearch().isEmpty()) {
			page = repository.findByAssuntoOrUsuarioOrCliente(datatables.getSearch(), datatables.getPageable());
		}

		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodasSolicitacoesAbertas(HttpServletRequest request, Long id, String status) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.SOLICITACOES);
		Page<Solicitacao> page = null;

		if (!datatables.getSearch().isEmpty()) {
			page = repository.findByAssuntoOrUsuario(datatables.getSearch(), datatables.getPageable(), id);
		}

		if (status.equals(SolicitacaoStatus.ABERTO.toString())) {
			page = repository.findTodasSolicitacoesAbertas(datatables.getPageable(), id);
		}
		if (status.equals(SolicitacaoStatus.ATRASADO.toString())) {
			page = repository.findTodasSolicitacoesAtrasadas(datatables.getPageable(), id);
		}
		if (status.equals(SolicitacaoStatus.PENDENTE.toString())) {
			page = repository.findTodasSolicitacoesPendentes(datatables.getPageable(), id);
		}
		if (status.equals(SolicitacaoStatus.FINALIZADO.toString())) {
			page = repository.findTodasSolicitacoesFinalizadas(datatables.getPageable(), id);

		}

		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodasSolicitacoesPorBairro(HttpServletRequest request, Long id, String status) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.SOLICITACOES);
		Page<Solicitacao> page = null;

		if (status.equals(SolicitacaoStatus.ABERTO.toString())) {
			page = repository.findTodasSolicitacoesAbertasDoBairro(datatables.getPageable(), id);
		}
		if (status.equals(SolicitacaoStatus.ATRASADO.toString())) {
			page = repository.findTodasSolicitacoesAtrasadasDoBairro(datatables.getPageable(), id);
		}
		if (status.equals(SolicitacaoStatus.PENDENTE.toString())) {
			page = repository.findTodasSolicitacoesPendentesDoBairro(datatables.getPageable(), id);
		}
		if (status.equals(SolicitacaoStatus.FINALIZADO.toString())) {
			page = repository.findTodasSolicitacoesFinalizadasDoBairro(datatables.getPageable(), id);
		}
		if (!datatables.getSearch().isEmpty()) {
			page = repository.findByAssuntoOrUsuario(datatables.getSearch(), datatables.getPageable(), id);
		}

		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarPorResultadosPositivos() {
		return repository.findByResultadosPositivos();
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarPorResultadosNegativos() {
		return repository.findByResultadosNegativos();
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarPorMunicipesAvisados() {
		return repository.findByMunicipesAvisados();
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarPorMunicipesNaoAvisados() {
		return repository.findByMunicipesNaoAvisados();
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarSolicitacaoPorStatusPendenteEAno(int ano) {
		return repository.findByStatusPendenteAndAno(ano);
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarSolicitacaoPorStatusAtrasadoEAno(int ano) {
		return repository.findByStatusAtrasadoAndAno(ano);
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarSolicitacaoPorStatusFinalizadoEAno(int ano) {
		return repository.findByStatusFinalizadoAndAno(ano);
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarSolicitacaoPorStatusAbertoEAno(int ano) {
		return repository.findByStatusAbertoAndAno(ano);
	}

	@Transactional(readOnly = true)
	public Solicitacao buscarPorIndicacaoId(Long id) {
		return repository.findByIndicacaoId(id);
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarTodasSolicitacoesNaoIndicadas(Long id) {
		return repository.findAllNaoIndicadas(id);
	}

	@Transactional(readOnly = true)
	public List<Solicitacao> buscarTodasSolicitacoesComFiltro(String buscaCliente, String buscaAssunto, String buscaBairro,
			String buscaUsuario, LocalDate buscaData, LocalDate buscaDataFinal, String status, String resultado, String aviso,
			String indicada) {
		List<Solicitacao> page = null;
		Specification<Solicitacao> situacao = null;
		Specification<Solicitacao> resultado_solucao = null;
		Specification<Solicitacao> aviso_solucao = null;
		Specification<Solicitacao> data = null;
		Specification<Solicitacao> indicacao = null;

		if (resultado != null) {
			if (resultado.equals("Positivo")) {
				resultado_solucao = SpecificationSolicitacao.resultadoPositivo(resultado);
			} else {
				resultado_solucao = SpecificationSolicitacao.resultadoNegativo(resultado);
			}

		}
		if (aviso != null) {
			if (aviso.equals("true")) {
				aviso_solucao = SpecificationSolicitacao.avisoTrue(aviso);
			} else {
				aviso_solucao = SpecificationSolicitacao.avisoFalse(aviso);
			}

		}
		if (indicada != null) {
			if (indicada.equals("true")) {
				indicacao = SpecificationSolicitacao.indicadaTrue(indicada);
			} else {
				indicacao = SpecificationSolicitacao.indicadaFalse(indicada);
			}

		}
		if (status != null) {
			switch (status) {
			case "FINALIZADO":
				situacao = SpecificationSolicitacao.statusFinalizado(status);
				break;
			case "ATRASADO":
				situacao = SpecificationSolicitacao.statusAtrasado(status);
				break;
			case "PENDENTE":
				situacao = SpecificationSolicitacao.statusPendente(status);
				break;
			case "ABERTO":
				situacao = SpecificationSolicitacao.statusAberto(status);
				break;

			}
		}
		if (buscaData != null) {
			data = SpecificationSolicitacao.dataIgual(buscaData);

		}
		if (buscaData != null && buscaDataFinal != null) {
			data = SpecificationSolicitacao.dataBetween(buscaData, buscaDataFinal);

		}

		if ((!buscaAssunto.isEmpty() || !buscaBairro.isEmpty() || !buscaCliente.isEmpty() || !buscaUsuario.isEmpty())
				|| buscaData != null || status != null || resultado != null || aviso != null || indicada != null) {
			page = repository.findAll(Specification.where(SpecificationSolicitacao.assunto(buscaAssunto))
					.and(SpecificationSolicitacao.nomeCliente(buscaCliente))
					.and(SpecificationSolicitacao.usuario(buscaUsuario))
					.and(SpecificationSolicitacao.bairro(buscaBairro)).and(situacao).and(data).and(resultado_solucao)
					.and(aviso_solucao).and(indicacao));
		}else {
			page = repository.findAll();
		}
		return page;
	}


}
