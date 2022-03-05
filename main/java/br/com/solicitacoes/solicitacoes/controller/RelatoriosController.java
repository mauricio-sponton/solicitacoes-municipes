package br.com.solicitacoes.solicitacoes.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.solicitacoes.solicitacoes.domain.Assunto;
import br.com.solicitacoes.solicitacoes.domain.Bairro;
import br.com.solicitacoes.solicitacoes.domain.Cliente;
import br.com.solicitacoes.solicitacoes.domain.HistoricoMunicipe;
import br.com.solicitacoes.solicitacoes.domain.HistoricoSolicitacao;
import br.com.solicitacoes.solicitacoes.domain.Indicacao;
import br.com.solicitacoes.solicitacoes.domain.Remetente;
import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.SolicitacaoStatus;
import br.com.solicitacoes.solicitacoes.domain.Solucao;
import br.com.solicitacoes.solicitacoes.domain.Usuario;
import br.com.solicitacoes.solicitacoes.historicos.AddHistorico;
import br.com.solicitacoes.solicitacoes.service.AssuntoService;
import br.com.solicitacoes.solicitacoes.service.BairroService;
import br.com.solicitacoes.solicitacoes.service.ClienteService;
import br.com.solicitacoes.solicitacoes.service.ExcelService;
import br.com.solicitacoes.solicitacoes.service.HistoricoMunicipeService;
import br.com.solicitacoes.solicitacoes.service.HistoricoSolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.IndicacaoService;
import br.com.solicitacoes.solicitacoes.service.JasperService;
import br.com.solicitacoes.solicitacoes.service.RemetenteService;
import br.com.solicitacoes.solicitacoes.service.SolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.SolucaoService;
import br.com.solicitacoes.solicitacoes.service.UsuarioService;

@Controller
@RequestMapping("relatorios")
public class RelatoriosController extends ParentController {

	@Autowired
	private SolicitacaoService solicitacaoService;
	
	@Autowired
	private SolucaoService solucaoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private HistoricoMunicipeService historicoMunicipeService;

	@Autowired
	private HistoricoSolicitacaoService historicoSolicitacaoService;

	@Autowired
	private AssuntoService assuntoService;

	@Autowired
	private BairroService bairroService;

	@Autowired
	private JasperService jasperService;

	@Autowired
	private RemetenteService remetenteService;
	
	@Autowired
	private IndicacaoService indicacaoService;

	@GetMapping({ "/listar/{screen}" })
	public String relatorios(ModelMap model, @PathVariable("screen") String screen, HttpServletResponse resp) {

		Integer tamanho = Integer.parseInt(screen);

		if (tamanho >= 1000) {
			model.addAttribute("solicitacoesAbertas", solicitacaoService.buscarSolicitacaoPorStatusAberto());
			model.addAttribute("solicitacoesFinalizadas", solicitacaoService.buscarSolicitacaoPorStatusFinalizado());
			model.addAttribute("solicitacoesAtrasadas", solicitacaoService.buscarSolicitacaoPorStatusAtrasado());
			model.addAttribute("solicitacoesPendentes", solicitacaoService.buscarSolicitacaoPorStatusPendente());
			model.addAttribute("solicitacao", new Solicitacao());
			return "relatorio/lista";
		} else {
			model.addAttribute("status", 403);
			model.addAttribute("error", "Acesso negado");
			model.addAttribute("message", "Essa página só pode ser acessada por um dispositivo com resolução maior");
			return "error";
		}

	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarRelatoriosDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(clienteService.buscarTodosSemFiltro(request));
	}

	@GetMapping("/bairros/datatables/server")
	public ResponseEntity<?> listarRelatoriosDatatablesBairros(HttpServletRequest request) {
		return ResponseEntity.ok(bairroService.buscarTodos(request));
	}

	@GetMapping("/solicitacao/excluir/{id}")
	public String excluirSolicitacaoPorRelatorio(@PathVariable("id") Long id, RedirectAttributes attr) {
		System.out.println(id);
		solicitacaoService.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/relatorios/listar";
	}

	@GetMapping("/solicitacao/editar/{id}")
	public String preEditarSolicitacaoPorRelatorio(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("solicitacoesAbertas", solicitacaoService.buscarSolicitacaoPorStatusAberto());
		model.addAttribute("solicitacoesFinalizadas", solicitacaoService.buscarSolicitacaoPorStatusFinalizado());
		model.addAttribute("solicitacoesAtrasadas", solicitacaoService.buscarSolicitacaoPorStatusAtrasado());
		model.addAttribute("solicitacoesPendentes", solicitacaoService.buscarSolicitacaoPorStatusPendente());
		model.addAttribute("solicitacao", solicitacaoService.buscarPorId(id));
		return "relatorio/lista";
	}

	@PostMapping("/solicitacao/salvar")
	public String salvar(@Valid Solicitacao solicitacao, BindingResult result, RedirectAttributes attr,
			@AuthenticationPrincipal User user, ModelMap model) {

		if (result.hasErrors() || solicitacao.getCliente().getNome().isEmpty()
				|| solicitacao.getAssunto().getDescricao().isEmpty()
				|| solicitacao.getBairro().getDescricao().isEmpty()) {
			model.addAttribute("erro", "Por favor preencha os dados");
			model.addAttribute("solicitacoesAbertas", solicitacaoService.buscarSolicitacaoPorStatusAberto());
			model.addAttribute("solicitacoesFinalizadas", solicitacaoService.buscarSolicitacaoPorStatusFinalizado());
			model.addAttribute("solicitacoesAtrasadas", solicitacaoService.buscarSolicitacaoPorStatusAtrasado());
			model.addAttribute("solicitacoesPendentes", solicitacaoService.buscarSolicitacaoPorStatusPendente());
			return "relatorio/lista";
		}

		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());

		String titulo = solicitacao.getCliente().getNome();
		Cliente cliente = clienteService.buscarPorTitulos(new String[] { titulo }).stream().findFirst().get();

		String buscaAssunto = solicitacao.getAssunto().getDescricao();
		Assunto assunto = assuntoService.buscarPorTitulos(new String[] { buscaAssunto }).stream().findFirst().get();

		String buscaBairro = solicitacao.getBairro().getDescricao();
		Bairro bairro = bairroService.buscarPorTitulos(new String[] { buscaBairro }).stream().findFirst().get();

		String mensagem = solicitacao.hasId() ? "Dados alterados com sucesso!" : "Dados cadastrados com sucesso";
		HistoricoMunicipe historico = null;
		HistoricoSolicitacao historicoSolicitacao = null;
		AddHistorico addHistorico = new AddHistorico();

		if (solicitacao.hasId()) {
			Solicitacao solicitacaoId = solicitacaoService.buscarPorId(solicitacao.getId());
			Solucao solucao = solucaoService.buscarPorSolcitacaoId(solicitacao.getId());
			Indicacao indicacao = indicacaoService.buscarPorSolicitacaoId(solicitacao.getId());
			if(indicacao != null) {
				solicitacao.setIndicacao(indicacao);
			}
			if(solucao != null) {
				solicitacao.setSolucao(solucao);
			}
			if (solicitacaoId.isIndicado()) {
				solicitacao.setIndicado(true);
			}
			solicitacao.setStatus(solicitacaoId.getStatus());
		} else {
			historico = new HistoricoMunicipe();
			historicoSolicitacao = new HistoricoSolicitacao();
			solicitacao.setStatus(SolicitacaoStatus.ABERTO);

		}
		solicitacao.setAssunto(assunto);
		solicitacao.setBairro(bairro);
		solicitacao.setData(LocalDate.now());
		solicitacao.setHora(LocalTime.now());
		solicitacao.setCliente(cliente);
		solicitacao.setUsuario(usuario.getNome());
		solicitacaoService.salvar(solicitacao);

		if (historico != null && historicoSolicitacao != null) {
			historico = addHistorico.novaSolicitacaoPorMunicipe(usuario, cliente, solicitacao);
			historicoSolicitacao = addHistorico.novaSolicitacao(solicitacao, usuario, cliente);
			historicoMunicipeService.salvar(historico);
			historicoSolicitacaoService.salvar(historicoSolicitacao);
		}

		attr.addFlashAttribute("sucesso", mensagem);
		return "redirect:/relatorios/listar";
	}

	@GetMapping("/pdf/all")
	public void exibirTodosRelatorios(@RequestParam("code") String code, HttpServletResponse response)
			throws IOException, SQLException {
		byte[] bytes = jasperService.exportarPDF(code);
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader("Content-disposition", "inline; filename=relatorio-" + code + ".pdf");
		response.getOutputStream().write(bytes);
	}

	@GetMapping("/municipes/export/excel")
	public void exportToExcel(HttpServletResponse response,
			@RequestParam(required = false, value = "relatorioNome") String nome,
			@RequestParam(required = false, value = "relatorioBairro") String bairro,
			@RequestParam(required = false, value = "relatorioEmail") String email,
			@RequestParam(required = false, value = "relatorioMes") Integer mes,
			@RequestParam(required = false, value = "relatorioAno") Integer ano,
			@RequestParam(required = false, value = "relatorioDia") Integer dia,
			@RequestParam(required = false, value = "relatorioApoiador") String apoiador) throws IOException {
		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=municipes_" + System.currentTimeMillis() + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<Cliente> listUsers = clienteService.buscarTodosClientesComFiltro(nome, bairro, email, mes, ano, dia,
				apoiador);

		ExcelService excelExporter = new ExcelService();

		excelExporter.export(response, listUsers);
	}

	@GetMapping("/solicitacoes/export/excel")
	public void exportToExcel(HttpServletResponse response,
			@RequestParam(required = false, value = "relatorioCliente") String cliente,
			@RequestParam(required = false, value = "relatorioAssunto") String assunto,
			@RequestParam(required = false, value = "relatorioBairro") String bairro,
			@RequestParam(required = false, value = "relatorioUsuario") String usuario,
			@RequestParam(required = false, value = "relatorioDataInicial") @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicial,
			@RequestParam(required = false, value = "relatorioDataFinal") @DateTimeFormat(iso = ISO.DATE) LocalDate dataFinal,
			@RequestParam(required = false, value = "relatorioStatus") String status,
			@RequestParam(required = false, value = "relatorioResultado") String resultado,
			@RequestParam(required = false, value = "relatorioAviso") String aviso,
			@RequestParam(required = false, value = "relatorioIndicada") String indicada) throws IOException {
		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=solicitacoes_" + System.currentTimeMillis() + ".xlsx";
		response.setHeader(headerKey, headerValue);
		
		List<Solicitacao> listUsers = solicitacaoService.buscarTodasSolicitacoesComFiltro(cliente, assunto, bairro,
				usuario, dataInicial, dataFinal, status, resultado, aviso, indicada);

		ExcelService excelExporter = new ExcelService();

		excelExporter.exportSolicitacao(response, listUsers);
	}

	@GetMapping("/municipes/pdf/personalizado")
	public void relatoriosDeMunicipesPersonalizadas(@RequestParam("02") String code,
			@RequestParam(required = false, value = "relatorioNome") String nome,
			@RequestParam(required = false, value = "relatorioBairro") String bairro,
			@RequestParam(required = false, value = "relatorioEmail") String email,
			@RequestParam(required = false, value = "relatorioMes") Integer mes,
			@RequestParam(required = false, value = "relatorioAno") Integer ano,
			@RequestParam(required = false, value = "relatorioDia") Integer dia,
			@RequestParam(required = false, value = "relatorioApoiador") Boolean apoiador, HttpServletResponse response)
			throws IOException, SQLException {

		jasperService.addParams("EMAIL", email == null ? null : email);
		jasperService.addParams("MUNICIPE_NOME", nome.isEmpty() ? null : nome);
		jasperService.addParams("BAIRRO_DESC", bairro.isEmpty() ? null : bairro);
		jasperService.addParams("MESES", mes == null ? null : mes);
		jasperService.addParams("ANO", ano == null ? null : ano);
		jasperService.addParams("DIA", dia == null ? null : dia);
		jasperService.addParams("APOIADOR", apoiador == null ? null : apoiador);

		byte[] bytes = jasperService.exportarPDF(code);
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader("Content-disposition", "inline; filename=relatorio-" + code + ".pdf");
		response.getOutputStream().write(bytes);
	}

	@GetMapping("/pdf/personalizado")
	public void relatoriosDeSolicitacoesPersonalizadas(@RequestParam("carregaGroup") String code,
			@RequestParam(required = false, value = "relatorioCliente") String cliente,
			@RequestParam(required = false, value = "relatorioAssunto") String assunto,
			@RequestParam(required = false, value = "relatorioBairro") String bairro,
			@RequestParam(required = false, value = "relatorioUsuario") String usuario,
			@RequestParam(required = false, value = "relatorioDataInicial") @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicial,
			@RequestParam(required = false, value = "relatorioDataFinal") @DateTimeFormat(iso = ISO.DATE) LocalDate dataFinal,
			@RequestParam(required = false, value = "relatorioStatus") String status,
			@RequestParam(required = false, value = "relatorioResultado") String resultado,
			@RequestParam(required = false, value = "relatorioAviso") Boolean aviso,
			@RequestParam(required = false, value = "relatorioIndicada") Boolean indicada, HttpServletResponse response)
			throws IOException, SQLException {
		
		List<Solucao> solucoes = solucaoService.buscarTodasSolucoes();
		
		if(solucoes.isEmpty()) {
			code = code.concat("-nosolucoes");
			
		}

		jasperService.addParams("STATUS_DESC", status == null ? null : status);
		jasperService.addParams("MUNICIPE_NOME", cliente.isEmpty() ? null : cliente);
		jasperService.addParams("BAIRRO_DESC", bairro.isEmpty() ? null : bairro);
		jasperService.addParams("ASSUNTO_DESC", assunto.isEmpty() ? null : assunto);
		jasperService.addParams("USUARIO_NOME", usuario.isEmpty() ? null : usuario);
		jasperService.addParams("DATA_INICIAL", dataInicial == null ? null : Date.valueOf(dataInicial));
		jasperService.addParams("DATA_FINAL", dataFinal == null ? null : Date.valueOf(dataFinal));
		jasperService.addParams("RESULTADO", resultado == null ? null : resultado);
		jasperService.addParams("AVISO", aviso == null ? null : aviso);
		jasperService.addParams("INDICADO", indicada == null ? null : indicada);

		byte[] bytes = jasperService.exportarPDF(code);
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader("Content-disposition", "inline; filename=relatorio-" + code + ".pdf");
		response.getOutputStream().write(bytes);
	}

	@GetMapping("/pdf/codigo/{id}")
	public void exibirRelatorio09(@PathVariable("id") Long id, @RequestParam("code") String code,
			@RequestParam("tipo") String tipo, HttpServletResponse response) throws IOException, SQLException {

		if (tipo.equals("solicitacao")) {
			Solicitacao solicitacao = solicitacaoService.buscarPorId(id);
			jasperService.addParams("SOLICITACAO_ID", solicitacao.getId());
		}

		if (tipo.equals("municipe")) {
			Cliente cliente = clienteService.buscarPorId(id);
			jasperService.addParams("ID_CLIENTE", cliente.getId());
		}

		byte[] bytes = jasperService.exportarPDF(code);
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader("Content-disposition", "inline; filename=relatorio-" + code + ".pdf");
		response.getOutputStream().write(bytes);
	}

	@GetMapping("/pdf/etiqueta/{id}")
	public void exibirEtiquetas(@PathVariable("id") Long id, @RequestParam("code") String code,
			@RequestParam("tipo") String tipo, @RequestParam(required = false, value = "remetente") String remetente,
			HttpServletResponse response) throws IOException, SQLException {

		if (tipo.equals("etiqueta")) {
			Cliente cliente = clienteService.buscarPorId(id);
			jasperService.addParams("ID_CLIENTE", cliente.getId());
		}

		if (!remetente.isEmpty()) {
			long remetenteId = Long.parseLong(remetente);
			Remetente r = remetenteService.buscarPorId(remetenteId);
			jasperService.addParams("REMETENTE_ID", r.getId());
		} else {
			jasperService.addParams("REMETENTE_ID", null);
		}

		byte[] bytes = jasperService.exportarPDF(code);
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader("Content-disposition", "inline; filename=relatorio-" + code + ".pdf");
		response.getOutputStream().write(bytes);
	}
	@GetMapping("/pdf/etiqueta/all")
	public void exibirTodasEtiquetas(
			 @RequestParam(required = false, value = "remetente") String remetente,
			HttpServletResponse response) throws IOException, SQLException {
		String code = "";

		if (!remetente.isEmpty()) {
			long remetenteId = Long.parseLong(remetente);
			Remetente r = remetenteService.buscarPorId(remetenteId);
			jasperService.addParams("REMETENTE_ID", r.getId());
			code = "etiquetas-municipes";
		} else {
			jasperService.addParams("REMETENTE_ID", null);
			code = "etiquetas-municipes-sem-remetente";
		}

		byte[] bytes = jasperService.exportarPDF(code);
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader("Content-disposition", "inline; filename=relatorio-" + code + ".pdf");
		response.getOutputStream().write(bytes);
	}

}
