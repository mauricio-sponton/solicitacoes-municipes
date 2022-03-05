package br.com.solicitacoes.solicitacoes.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.SolicitacaoStatus;
import br.com.solicitacoes.solicitacoes.domain.Solucao;
import br.com.solicitacoes.solicitacoes.domain.UF;
import br.com.solicitacoes.solicitacoes.domain.Usuario;
import br.com.solicitacoes.solicitacoes.historicos.AddHistorico;
import br.com.solicitacoes.solicitacoes.service.AssuntoService;
import br.com.solicitacoes.solicitacoes.service.BairroService;
import br.com.solicitacoes.solicitacoes.service.ClienteService;
import br.com.solicitacoes.solicitacoes.service.HistoricoMunicipeService;
import br.com.solicitacoes.solicitacoes.service.HistoricoSolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.IndicacaoService;
import br.com.solicitacoes.solicitacoes.service.SolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.SolucaoService;
import br.com.solicitacoes.solicitacoes.service.TelefoneService;
import br.com.solicitacoes.solicitacoes.service.UsuarioService;

@Controller
@RequestMapping("solicitacoes")
public class SolicitacaoController extends ParentController{

	@Autowired
	private SolicitacaoService service;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private SolucaoService solucaoService;

	@Autowired
	private HistoricoMunicipeService historicoService;

	@Autowired
	private HistoricoSolicitacaoService historicoSolicitacaoService;

	@Autowired
	private AssuntoService assuntoService;

	@Autowired
	private BairroService bairroService;

	@Autowired
	private TelefoneService telefoneService;

	@Autowired
	private IndicacaoService indicacaoService;

	
	
	@GetMapping({ "/listar", "/listar/{statusUrl}" })
	public String listar(Solicitacao solicitacao, ModelMap model, @AuthenticationPrincipal User user,
			@PathVariable(required = false, value = "statusUrl") String statusUrl) {
		if (statusUrl != null) {
			model.addAttribute("statusUrl", statusUrl);
		}

		if (!model.containsAttribute("cliente")) {
			model.addAttribute("cliente", new Cliente());
		}
		if (model.containsAttribute("viaSolicitacao")) {
			model.addAttribute("controle", model.get("viaSolicitacao"));

		}
//		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.ADMIN.getDesc()))) {
//			model.addAttribute("autoridade", "autoridade");
//		}
		
		model.addAttribute("solucoes", solucaoService.buscarTodasSolucoes());
		telefoneService.addFone(model, 1);
		return "solicitacao/lista";
	}

	@GetMapping("/pesquisa/historico/{idSolicitacao}")
	public ResponseEntity<?> listarHistorico(@PathVariable("idSolicitacao") Long idSolicitacao,
			@RequestParam(required = false, value = "tipo") String tipo,
			@RequestParam(required = false, value = "dataInicial") @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicial,
			@RequestParam(required = false, value = "dataFinal") @DateTimeFormat(iso = ISO.DATE) LocalDate dataFinal) {
		return ResponseEntity
				.ok(historicoSolicitacaoService.buscarPorParametros(idSolicitacao, tipo, dataInicial, dataFinal));
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarSolicitacoesDatatables(HttpServletRequest request,
			@RequestParam(required = false, value = "buscaAssunto") String buscaAssunto,
			@RequestParam(required = false, value = "buscaCliente") String buscaCliente,
			@RequestParam(required = false, value = "buscaUsuario") String buscaUsuario,
			@RequestParam(required = false, value = "buscaBairro") String buscaBairro,
			@RequestParam(required = false, value = "buscaData") @DateTimeFormat(iso = ISO.DATE) LocalDate buscaData,
			@RequestParam(required = false, value = "buscaDataFinal") @DateTimeFormat(iso = ISO.DATE) LocalDate buscaDataFinal,
			@RequestParam(required = false, value = "status") String status,
			@RequestParam(required = false, value = "resultado") String resultado,
			@RequestParam(required = false, value = "aviso") String aviso, 
			@RequestParam(required = false, value = "indicada") String indicada, ModelMap model) {

		return ResponseEntity.ok(service.buscarTodos(request, buscaAssunto, buscaCliente, buscaUsuario, buscaBairro,
				buscaData, buscaDataFinal, status, resultado, aviso, indicada));
	}

	@GetMapping("/datatables/server/{id}")
	public ResponseEntity<?> listarSolicitacoesAbertasDatatables(HttpServletRequest request,
			@PathVariable("id") Long id, @RequestParam(required = false, value = "status") String status) {
	
		return ResponseEntity.ok(service.buscarTodasSolicitacoesAbertas(request, id, status));
	}

	@GetMapping("/datatables/server/bairro/{id}")
	public ResponseEntity<?> listarSolicitacoesPorBairroDatatables(HttpServletRequest request,
			@PathVariable("id") Long id, @RequestParam(required = false, value = "status") String status) {
		System.out.println(id);
		return ResponseEntity.ok(service.buscarTodasSolicitacoesPorBairro(request, id, status));
	}

	@PostMapping("/salvar/codigo/{id}")
	public String salvarPorIdSolicitacao(@PathVariable("id") Long id, @Valid Solicitacao solicitacao,
			BindingResult result, RedirectAttributes attr, @AuthenticationPrincipal User user, ModelMap model) {

		if ((result.hasErrors() || solicitacao.getCliente().getNome().isEmpty()
				|| solicitacao.getBairro().getDescricao().isEmpty()
				|| solicitacao.getAssunto().getDescricao().isEmpty())) {

			model.addAttribute("erro_solicitacao", "Por favor preencha os dados");
			// model.addAttribute("solicitacao", service.buscarPorId(id));
			model.addAttribute("historico", historicoSolicitacaoService.buscarHistoricoPorSolicitacaoId(id));

			model.addAttribute("indicacao", new Indicacao());
			Solucao solucao = solucaoService.buscarPorSolcitacaoId(id);

			if (solucao == null) {
				model.addAttribute("solucao", new Solucao());
			} else {
				model.addAttribute("solucao", solucao);
			}

			return "solicitacao/visualizar";
		}

		String titulo = solicitacao.getCliente().getNome();
		Cliente cliente = clienteService.buscarPorTitulos(new String[] { titulo }).stream().findFirst().get();

		String buscaAssunto = solicitacao.getAssunto().getDescricao();
		Assunto assunto = assuntoService.buscarPorTitulos(new String[] { buscaAssunto }).stream().findFirst().get();

		String buscaBairro = solicitacao.getBairro().getDescricao();
		Bairro bairro = bairroService.buscarPorTitulos(new String[] { buscaBairro }).stream().findFirst().get();

		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		AddHistorico addHistorico = new AddHistorico();
		HistoricoSolicitacao edicaoSolicitacao = null;
		Solicitacao solicitacaoId = service.buscarPorId(solicitacao.getId());
		solicitacao.setStatus(solicitacaoId.getStatus());
		edicaoSolicitacao = addHistorico.edicaoSolicitacao(usuario, solicitacaoId, solicitacao, cliente);
		
		if(solicitacao.hasId()) {
			Solucao solucao = solucaoService.buscarPorSolcitacaoId(solicitacao.getId());
			Indicacao indicacao = indicacaoService.buscarPorSolicitacaoId(solicitacao.getId());
			if(indicacao != null) {
				solicitacao.setIndicacao(indicacao);
			}
			if(solucao != null) {
				solicitacao.setSolucao(solucao);
			}
			
		}
		
		if (solicitacaoId.isIndicado()) {
			solicitacao.setIndicado(true);
		}
		solicitacao.setBairro(bairro);
		solicitacao.setAssunto(assunto);
		solicitacao.setData(solicitacaoId.getData());
		solicitacao.setHora(solicitacaoId.getHora());
		solicitacao.setCliente(cliente);
		solicitacao.setUsuario(usuario.getNome());
		service.salvar(solicitacao);

		if (edicaoSolicitacao != null) {
			historicoSolicitacaoService.salvar(edicaoSolicitacao);
		}

		attr.addFlashAttribute("sucesso", "Dados alterados com sucesso!");

		return "redirect:/solicitacoes/visualizar/{id}";
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Solicitacao solicitacao, BindingResult result, RedirectAttributes attr,
			@AuthenticationPrincipal User user, ModelMap model,
			@RequestParam(required = false, value = "controle") String controle,
			@RequestParam(required = false, value = "url") String url) {

		Cliente cliente = null;

		if ((result.hasErrors() || solicitacao.getCliente().getNome().isEmpty()
				|| solicitacao.getBairro().getDescricao().isEmpty()
				|| solicitacao.getAssunto().getDescricao().isEmpty())) {
			model.addAttribute("erro", "Por favor preencha os dados");
			model.addAttribute("cliente", new Cliente());
			return "solicitacao/lista";
		}

		String titulo = solicitacao.getCliente().getNome();
		cliente = clienteService.buscarPorTitulos(new String[] { titulo }).stream().findFirst().get();

		String buscaAssunto = solicitacao.getAssunto().getDescricao();
		Assunto assunto = assuntoService.buscarPorTitulos(new String[] { buscaAssunto }).stream().findFirst().get();

		String buscaBairro = solicitacao.getBairro().getDescricao();
		Bairro bairro = bairroService.buscarPorTitulos(new String[] { buscaBairro }).stream().findFirst().get();

		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());

		String mensagem = solicitacao.hasId() ? "Dados alterados com sucesso!" : "Dados cadastrados com sucesso";
		HistoricoMunicipe historico = null;
		HistoricoSolicitacao edicaoSolicitacao = null;
		HistoricoSolicitacao historicoSolicitacao = null;
		AddHistorico addHistorico = new AddHistorico();

		if (solicitacao.hasId()) {
			Solicitacao solicitacaoId = service.buscarPorId(solicitacao.getId());
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
			edicaoSolicitacao = addHistorico.edicaoSolicitacao(usuario, solicitacaoId, solicitacao, cliente);

		} else {
			historico = new HistoricoMunicipe();
			historicoSolicitacao = new HistoricoSolicitacao();
			solicitacao.setStatus(SolicitacaoStatus.ABERTO);

		}

		solicitacao.setBairro(bairro);
		solicitacao.setAssunto(assunto);
		solicitacao.setData(LocalDate.now());
		solicitacao.setHora(LocalTime.now());
		solicitacao.setCliente(cliente);
		solicitacao.setUsuario(usuario.getNome());
		service.salvar(solicitacao);

		if (historico != null && historicoSolicitacao != null) {
			historico = addHistorico.novaSolicitacaoPorMunicipe(usuario, cliente, solicitacao);
			historicoSolicitacao = addHistorico.novaSolicitacao(solicitacao, usuario, cliente);
			historicoService.salvar(historico);
			historicoSolicitacaoService.salvar(historicoSolicitacao);
		}
		if (edicaoSolicitacao != null) {
			historicoSolicitacaoService.salvar(edicaoSolicitacao);
		}

		attr.addFlashAttribute("sucesso", mensagem);

		if (controle != null) {
			return "redirect:/solicitacoes/listar";
		}
		if (url != null) {
			return "redirect:/clientes/visualizar/" + solicitacao.getCliente().getId();
		}
		return "redirect:/solicitacoes/listar";
	}

	@PostMapping("/salvar/{idMunicipe}")
	public String salvarPorIdMunicipe(@Valid Solicitacao solicitacao, BindingResult result, RedirectAttributes attr,
			@AuthenticationPrincipal User user, ModelMap model, HttpServletRequest request,
			@RequestParam(required = false, value = "controle") String controle,
			@PathVariable(required = false, value = "idMunicipe") Long idMunicipe,
			@RequestParam(required = false, value = "url") String url) {

		Cliente cliente = clienteService.buscarPorId(idMunicipe);

	

		if ((result.hasErrors() || solicitacao.getAssunto().getDescricao().isEmpty()
				|| solicitacao.getBairro().getDescricao().isEmpty()) && controle != null) {

			model.addAttribute("erro_solicitacao_comMunicipe", "Por favor preencha os dados");
			model.addAttribute("cliente", new Cliente());
			model.addAttribute("emMemoria", clienteService.buscarPorId(idMunicipe));
			model.addAttribute("controle", controle);
			return "solicitacao/lista";
		}
		if ((result.hasErrors() || solicitacao.getAssunto().getDescricao().isEmpty()
				|| solicitacao.getBairro().getDescricao().isEmpty()) && url != null) {
			model.addAttribute("url", request.getRequestURL());
			model.addAttribute("erro_solicitacao_comMunicipe", "Por favor preencha os dados");
			model.addAttribute("cliente", clienteService.buscarPorId(idMunicipe));
			model.addAttribute("historico", historicoService.buscarHistoricoPorClienteId(idMunicipe));
			return "cliente/visualizar";
		}
		if ((result.hasErrors() || solicitacao.getAssunto().getDescricao().isEmpty()
				|| solicitacao.getBairro().getDescricao().isEmpty())) {
			model.addAttribute("erro_solicitacao", "Por favor preencha os dados");
			model.addAttribute("cliente", new Cliente());
			model.addAttribute("emMemoria", clienteService.buscarPorId(idMunicipe));
			return "/cliente/lista";
		}
		String buscaAssunto = solicitacao.getAssunto().getDescricao();
		Assunto assunto = assuntoService.buscarPorTitulos(new String[] { buscaAssunto }).stream().findFirst().get();

		String buscaBairro = solicitacao.getBairro().getDescricao();
		Bairro bairro = bairroService.buscarPorTitulos(new String[] { buscaBairro }).stream().findFirst().get();

		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());

		String mensagem = solicitacao.hasId() ? "Dados alterados com sucesso!" : "Dados cadastrados com sucesso";
		HistoricoMunicipe historico = null;
		HistoricoSolicitacao historicoSolicitacao = null;
		HistoricoSolicitacao edicaoSolicitacao = null;
		AddHistorico addHistorico = new AddHistorico();
		if (solicitacao.hasId()) {
			Solicitacao solicitacaoId = service.buscarPorId(solicitacao.getId());
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
			edicaoSolicitacao = addHistorico.edicaoSolicitacao(usuario, solicitacaoId, solicitacao, cliente);
		} else {
			historico = new HistoricoMunicipe();
			historicoSolicitacao = new HistoricoSolicitacao();
			solicitacao.setStatus(SolicitacaoStatus.ABERTO);

		}

		solicitacao.setBairro(bairro);
		solicitacao.setAssunto(assunto);
		solicitacao.setData(LocalDate.now());
		solicitacao.setHora(LocalTime.now());
		solicitacao.setCliente(cliente);
		solicitacao.setUsuario(usuario.getNome());
		service.salvar(solicitacao);

		if (historico != null && historicoSolicitacao != null) {
			historico = addHistorico.novaSolicitacaoPorMunicipe(usuario, cliente, solicitacao);
			historicoSolicitacao = addHistorico.novaSolicitacao(solicitacao, usuario, cliente);
			historicoService.salvar(historico);
			historicoSolicitacaoService.salvar(historicoSolicitacao);
		}
		if (edicaoSolicitacao != null) {
			historicoSolicitacaoService.salvar(edicaoSolicitacao);
		}

		attr.addFlashAttribute("sucesso", mensagem);

		if (controle != null) {
			return "redirect:/solicitacoes/listar";
		}
		if (url != null) {
			return "redirect:/clientes/visualizar/" + solicitacao.getCliente().getId();
		}
		return "redirect:/clientes/listar";
	}

	@GetMapping("/editar/{id}/municipe/{idMunicipe}")
	public String preEditarPorMunicipeId(@PathVariable("idMunicipe") Long idMunicipe, @PathVariable("id") Long id,
			ModelMap model, HttpServletRequest request) {
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("cliente", clienteService.buscarPorId(idMunicipe));
		model.addAttribute("historico", historicoService.buscarHistoricoPorClienteId(idMunicipe));
		model.addAttribute("solicitacao", service.buscarPorId(id));
		return "cliente/visualizar";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("cliente", new Cliente());
		model.addAttribute("solicitacao", service.buscarPorId(id));
		telefoneService.addFone(model, 1);
		return "solicitacao/lista";
	}

	@GetMapping({ "/excluir/{id}", "excluir/{id}/municipe/{idMunicipe}" })
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr,
			@PathVariable(required = false, value = "idMunicipe") Long idMunicipe) {

		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return idMunicipe != null ? "redirect:/clientes/visualizar/{idMunicipe}" : "redirect:/solicitacoes/listar";
	}

	@GetMapping("/visualizar/{id}")
	public String visualizar(@PathVariable("id") Long id, ModelMap model) {
		Solicitacao solicitacao = service.buscarPorId(id);

		Solucao solucao = null;

		if (solicitacao.getStatus().equals(SolicitacaoStatus.ABERTO)) {
			solucao = new Solucao();
			model.addAttribute("solucao", solucao);

		} else if (solicitacao.getStatus().equals(SolicitacaoStatus.PENDENTE)
				|| solicitacao.getStatus().equals(SolicitacaoStatus.FINALIZADO)) {

			solucao = solucaoService.buscarPorSolcitacaoId(id);
			model.addAttribute("solucao", solucao);

		} else if (solicitacao.getStatus().equals(SolicitacaoStatus.ATRASADO)) {
			solucao = new Solucao();
			model.addAttribute("solucao", solucao);

		}

		model.addAttribute("historico", historicoSolicitacaoService.buscarHistoricoPorSolicitacaoId(id));
		model.addAttribute("solicitacao", solicitacao);
		model.addAttribute("indicacao", new Indicacao());
		if (solicitacao.isIndicado()) {
			Indicacao indicacao = indicacaoService.buscarPorSolicitacaoId(solicitacao.getId());
			model.addAttribute("indicacaoLink", indicacao.getId());
		}

		return "solicitacao/visualizar";
	}

	@GetMapping("/buscar/data/{ano}")
	public ResponseEntity<?> getPorData(@PathVariable("ano") int ano, ModelMap model) {
		Object[] solicitacoes = service.buscarDataPorAno(ano);
		return ResponseEntity.ok(solicitacoes);
	}

	@GetMapping("/buscar/resultados/{id}")
	public ResponseEntity<?> getPorResultados(@PathVariable("id") Long id, ModelMap model) {
		Object[] solicitacoes = service.buscarPorResultados(id);
		return ResponseEntity.ok(solicitacoes);
	}

	@ModelAttribute
	public List<Assunto> getAssuntos() {
		return assuntoService.buscarTodosAssuntos();
	}

	@ModelAttribute("ufs")
	public UF[] getUFs() {
		return UF.values();
	}

	@PostMapping("/finalizar/{id}")
	public String finalizarSolicitacao(@PathVariable("id") Long id, RedirectAttributes attr,
			@AuthenticationPrincipal User user, ModelMap model) {
		Solicitacao solicitacao = service.buscarPorId(id);
		AddHistorico addHistorico = null;
		HistoricoSolicitacao historicoSolicitacao = null;

		if (solicitacao.getStatus().equals(SolicitacaoStatus.PENDENTE)) {
			solicitacao.setStatus(SolicitacaoStatus.FINALIZADO);
			addHistorico = new AddHistorico();
			historicoSolicitacao = new HistoricoSolicitacao();
			
			Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());

			service.salvar(solicitacao);

			if (historicoSolicitacao != null) {
				historicoSolicitacao = addHistorico.finalizada(usuario, solicitacao);
				historicoSolicitacaoService.salvar(historicoSolicitacao);
			}
			attr.addFlashAttribute("sucesso", "Solicitação finalizada com sucesso!");
		} else {
			attr.addFlashAttribute("falha", "A solicitação precisa ser solucionada antes de ser finalizada!");
		}

		return "redirect:/solicitacoes/visualizar/{id}";
	}

}
