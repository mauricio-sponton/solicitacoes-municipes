package br.com.solicitacoes.solicitacoes.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

import br.com.solicitacoes.solicitacoes.domain.Bairro;
import br.com.solicitacoes.solicitacoes.domain.Cliente;
import br.com.solicitacoes.solicitacoes.domain.HistoricoMunicipe;
import br.com.solicitacoes.solicitacoes.domain.PerfilTipo;
import br.com.solicitacoes.solicitacoes.domain.Remetente;
import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.Telefone;
import br.com.solicitacoes.solicitacoes.domain.TelefoneDTO;
import br.com.solicitacoes.solicitacoes.domain.UF;
import br.com.solicitacoes.solicitacoes.domain.Usuario;
import br.com.solicitacoes.solicitacoes.historicos.AddHistorico;
import br.com.solicitacoes.solicitacoes.service.BairroService;
import br.com.solicitacoes.solicitacoes.service.ClienteService;
import br.com.solicitacoes.solicitacoes.service.HistoricoMunicipeService;
import br.com.solicitacoes.solicitacoes.service.RemetenteService;
import br.com.solicitacoes.solicitacoes.service.SolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.SolucaoService;
import br.com.solicitacoes.solicitacoes.service.TelefoneService;
import br.com.solicitacoes.solicitacoes.service.UsuarioService;

@Controller
@RequestMapping("clientes")
public class MunicipeController extends ParentController{

	@Autowired
	private ClienteService service;

	@Autowired
	private SolicitacaoService solicitacaoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private HistoricoMunicipeService historicoService;

	@Autowired
	private BairroService bairroService;

	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private RemetenteService remetenteService;
	
	@Autowired
	private SolucaoService solucaoService;

	@GetMapping("/listar")
	public String listar(Cliente cliente, Solicitacao solicitacao, ModelMap model) {
		telefoneService.addFone(model, 1);
		model.addAttribute("telefones_tabela", telefoneService.buscarTodos());
		return "cliente/lista";
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarClientesDatatables(HttpServletRequest request, 
			@RequestParam(required = false, value = "buscaCliente") String buscaCliente,
			@RequestParam(required = false, value = "buscaBairro") String buscaBairro,
			@RequestParam(required = false, value = "buscaEmail") String buscaEmail,
			@RequestParam(required = false, value = "buscaMes") Integer buscaMes,	
			@RequestParam(required = false, value = "buscaAno") Integer buscaAno,
			@RequestParam(required = false, value = "buscaDia") Integer buscaDia,
			@RequestParam(required = false, value = "apoiador") String apoiador) {
		return ResponseEntity.ok(service.buscarTodos(request, buscaCliente, buscaBairro, buscaEmail, buscaMes, buscaAno, buscaDia, apoiador));
	}

	@PostMapping("/salvar")
	public String salvar(@ModelAttribute TelefoneDTO fones, @Valid Cliente cliente, BindingResult result,
			RedirectAttributes attr, ModelMap model, @AuthenticationPrincipal User user,
			@RequestParam(required = false, value = "url") String url, HttpServletRequest request) {
		
		if (result.hasErrors() || cliente.getEndereco().getBairro().getDescricao().isEmpty()) {
			
			List<Telefone> telefones = telefoneService.buscarTelefonePorMunicipeId(cliente.getId());
			if(telefones.isEmpty()) {
				telefoneService.addFone(model, 1);
			}else {
				telefoneService.editFone(model, cliente);
			}
			model.addAttribute("erro", "por favor preencha os campos");
			model.addAttribute("solicitacao", new Solicitacao());
			
			if (url != null) {
				model.addAttribute("url", request.getRequestURL());
				model.addAttribute("historico", historicoService.buscarHistoricoPorClienteId(cliente.getId()));
			}
			return url != null ? "/cliente/visualizar" : "/cliente/lista";
		}

		HistoricoMunicipe historico = null;
		HistoricoMunicipe editHistorico = null;
		AddHistorico addHistorico = new AddHistorico();
		String mensagem = cliente.hasNotId() ? "Munícipe cadastrado com sucesso!" : "Dados alterados com sucesso!";

		String buscaBairro = cliente.getEndereco().getBairro().getDescricao();
		Bairro bairro = bairroService.buscarPorTitulos(new String[] { buscaBairro }).stream().findFirst().get();

		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		
		try {
			if (cliente.hasNotId()) {
				
				historico = addHistorico.cadastroDeMunicipe(usuario, cliente);
				attr.addFlashAttribute("emMemoria", cliente);
				attr.addFlashAttribute("sucessoModal", mensagem);
			} else {
				Cliente antigo = service.buscarPorId(cliente.getId());
				List<Telefone> telefones = telefoneService.buscarTelefonePorMunicipeId(antigo.getId());
				editHistorico = addHistorico.edicaoDadosMunicipe(usuario, cliente, antigo, bairro, telefones);
				attr.addFlashAttribute("sucesso", mensagem);
				
			}
			cliente.getEndereco().setBairro(bairro);
	
			service.salvar(cliente);
			for (Telefone t : fones.getTelefones()) {
				t.setCliente(cliente);
			}
			telefoneService.salvarTodos(fones.getTelefones());

			if (historico != null) {
				historicoService.salvar(historico);
			}
			if (editHistorico != null) {
				historicoService.salvar(editHistorico);
			}

		} catch (DataIntegrityViolationException ex) {
			System.out.println(ex.getMessage());
			attr.addFlashAttribute("falha", "Cadastro não realizado pois o email ou RG já existe");
		}
		if (url != null) {
			return "redirect:/clientes/visualizar/" + cliente.getId();
		}
		return "redirect:/clientes/listar";
	}

	@PostMapping("/salvar-por-solicitacao")
	public String salvarMunicipePorPaginaDeSolicitacao(@ModelAttribute TelefoneDTO fones, @Valid Cliente cliente, BindingResult result,
			RedirectAttributes attr, ModelMap model, @AuthenticationPrincipal User user,
			@RequestParam(required = false, value = "viaSolicitacao") String viaSolicitacao,
			@RequestParam(required = false, value = "url") String url, HttpServletRequest request) {

		if ((result.hasErrors() || cliente.getEndereco().getBairro().getDescricao().isEmpty())
				&& viaSolicitacao != null) {
			
			List<Telefone> telefones = telefoneService.buscarTelefonePorMunicipeId(cliente.getId());
			if(telefones.isEmpty()) {
				telefoneService.addFone(model, 1);
			}
			attr.addFlashAttribute("erro_municipe", "por favor preencha os campos");
			attr.addFlashAttribute("org.springframework.validation.BindingResult.cliente", result);
			attr.addFlashAttribute("cliente", cliente);
			return "redirect:/solicitacoes/listar";
		}

		HistoricoMunicipe historico = null;
		HistoricoMunicipe editHistorico = null;
		AddHistorico addHistorico = new AddHistorico();
		String mensagem = cliente.hasNotId() ? "Munícipe cadastrado com sucesso!" : "Dados alterados com sucesso!";

		String buscaBairro = cliente.getEndereco().getBairro().getDescricao();
		Bairro bairro = bairroService.buscarPorTitulos(new String[] { buscaBairro }).stream().findFirst().get();
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		try {
			if (cliente.hasNotId()) {
				
				historico = addHistorico.cadastroDeMunicipe(usuario, cliente);
				attr.addFlashAttribute("emMemoria", cliente);
				attr.addFlashAttribute("sucessoModal", mensagem);
				if (viaSolicitacao != null) {
					attr.addFlashAttribute("viaSolicitacao", viaSolicitacao);
				}
			} else {
				Cliente antigo = service.buscarPorId(cliente.getId());
				List<Telefone> telefones = telefoneService.buscarTelefonePorMunicipeId(antigo.getId());
				editHistorico = addHistorico.edicaoDadosMunicipe(usuario, cliente, antigo, bairro, telefones);
				attr.addFlashAttribute("sucesso", mensagem);
			}
			cliente.getEndereco().setBairro(bairro);
			
			for (Telefone t : fones.getTelefones()) {
				t.setCliente(cliente);
			}
			
			service.salvar(cliente);
			telefoneService.salvarTodos(fones.getTelefones());

			if (historico != null) {
				historicoService.salvar(historico);
			}
			if (editHistorico != null) {
				historicoService.salvar(editHistorico);
			}

		} catch (DataIntegrityViolationException ex) {
			attr.addFlashAttribute("falha", "Cadastro não realizado pois o email ou RG já existe");
		}
		if (url != null) {
			return "redirect:/clientes/visualizar/" + cliente.getId();
		}
		return "redirect:/solicitacoes/listar";
	}

	@ModelAttribute("ufs")
	public UF[] getUFs() {
		return UF.values();
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		
		Cliente cliente = service.buscarPorId(id);
		List<Telefone> telefones = telefoneService.buscarTelefonePorMunicipeId(cliente.getId());
		if (telefones.size() > 0) {
			telefoneService.addFone(model, 0);
			telefoneService.editFone(model, cliente);
		}
		if(telefones.isEmpty()){
			telefoneService.addFone(model, 1);
			//editFone(model, cliente);
		}
		model.addAttribute("cliente", cliente);
		return "cliente/lista";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/clientes/listar";
	}

	@ModelAttribute("remetentes")
	public List<Remetente> getRemetentes(){
		return remetenteService.buscarTodosRemetentes();
	}
	
	@GetMapping("/visualizar/{id}")
	public String visualizar(@PathVariable("id") Long id, ModelMap model, HttpServletRequest request, @AuthenticationPrincipal User user) {
		
		LocalDate data = LocalDate.now();
		
		
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("solicitacao", new Solicitacao());
		Cliente cliente = service.buscarPorId(id);
		model.addAttribute("cliente", cliente);
		
		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.ADMIN.getDesc()))) {
			model.addAttribute("autoridade", "autoridade");
		}
		
		model.addAttribute("idade", Period.between(cliente.getDataNascimento(), data).getYears());
		
		model.addAttribute("solicitacoes", solicitacaoService.buscarSolicitacaoPorClienteId(id));
		model.addAttribute("historico", historicoService.buscarHistoricoPorClienteId(id));
		model.addAttribute("solucoes", solucaoService.buscarTodasSolucoes());
		List<Telefone> telefones = telefoneService.buscarTelefonePorMunicipeId(cliente.getId());
		if (telefones.size() > 0) {
			telefoneService.addFone(model, 0);
			telefoneService.editFone(model, cliente);
		}
		if(telefones.isEmpty()){
			telefoneService.addFone(model, 1);
			//editFone(model, cliente);
		}
		model.addAttribute("telefones", telefones);
		return "/cliente/visualizar";
	}

	@GetMapping("/datatables/server/municipe/{id}")
	public ResponseEntity<?> listarSolicitacoesDoMunicipeDatatables(HttpServletRequest request,
			@PathVariable("id") Long id) {
		return ResponseEntity.ok(solicitacaoService.buscarTodasSolicitacoesDoMunicipe(request, id));
	}
	@GetMapping("/telefones/{id}")
	public ResponseEntity<?> listarTelefones(
			@PathVariable("id") Long id) {
		return ResponseEntity.ok(telefoneService.buscarTelefonePorMunicipeId(id));
	}

	@GetMapping("/titulo")
	public ResponseEntity<?> getClientesPorTermo(@RequestParam("termo") String termo) {
		List<String> clientes = service.buscarClienteByTermo(termo);
		return ResponseEntity.ok(clientes);
	}
	
	@GetMapping("/excluir/telefone/{id}/cliente/{clienteId}")
	public String excluirFone(@PathVariable("id") Long id, @PathVariable("clienteId") Long clienteId, RedirectAttributes attr) {		
			telefoneService.remover(id);
		return "redirect:/clientes/editar/{clienteId}";
	}
	
	@GetMapping("/pesquisa/historico/{idMunicipe}")
	public ResponseEntity<?> listarHistorico(@PathVariable("idMunicipe") Long idMunicipe,
			@RequestParam(required = false, value = "tipo") String tipo,
			@RequestParam(required = false, value = "dataInicial") @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicial,
			@RequestParam(required = false, value = "dataFinal") @DateTimeFormat(iso = ISO.DATE) LocalDate dataFinal){
		
		return ResponseEntity.ok(historicoService.buscarPorParametros(idMunicipe, tipo, dataInicial, dataFinal));
	}
	

}
