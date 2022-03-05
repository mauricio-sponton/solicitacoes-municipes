package br.com.solicitacoes.solicitacoes.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.solicitacoes.solicitacoes.domain.HistoricoSolicitacao;
import br.com.solicitacoes.solicitacoes.domain.Indicacao;
import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.SolicitacaoStatus;
import br.com.solicitacoes.solicitacoes.domain.Solucao;
import br.com.solicitacoes.solicitacoes.domain.Usuario;
import br.com.solicitacoes.solicitacoes.historicos.AddHistorico;
import br.com.solicitacoes.solicitacoes.service.HistoricoSolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.SolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.SolucaoService;
import br.com.solicitacoes.solicitacoes.service.UsuarioService;

@Controller
@RequestMapping("solucoes")
public class SolucaoController extends ParentController{
	
	@Autowired
	private SolicitacaoService solicitacaoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private SolucaoService service;
	
	@Autowired
	private HistoricoSolicitacaoService historicoSolicitacaoService;

	@PostMapping("/salvar/{idSolicitacao}")
	public String salvar(@Valid Solucao solucao, BindingResult result, @PathVariable("idSolicitacao") Long idSolicitacao, RedirectAttributes attr,
			@AuthenticationPrincipal User user, ModelMap model) {
		Solicitacao solicitacao = solicitacaoService.buscarPorId(idSolicitacao);
		if(result.hasErrors()) {
			model.addAttribute("erro", "Por favor preencha os dados");
			model.addAttribute("solicitacao", solicitacao);
			model.addAttribute("indicacao", new Indicacao());
			model.addAttribute("historico", historicoSolicitacaoService.buscarHistoricoPorSolicitacaoId(solicitacao.getId()));
			return "solicitacao/visualizar";
		}
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		
		HistoricoSolicitacao newSolucao = null;
		HistoricoSolicitacao edicaoSolucao = null;
		AddHistorico addHistorico = new AddHistorico();
		
		
		if(solucao.hasId()) {
			Solucao s = service.buscarPorId(solucao.getId());
			solucao.setData(s.getData());
			solucao.setHora(s.getHora());
			edicaoSolucao = addHistorico.edicaoSolucao(usuario, solicitacao, s, solucao);
			
		}else {
			newSolucao = addHistorico.novaSolucao(usuario, solicitacao);
			solucao.setData(LocalDate.now());
			solucao.setHora(LocalTime.now());
		}
		
		
		
		solucao.setUsuario(usuario.getNome());
		solucao.setSolicitacao(solicitacao);
		solicitacao.setSolucao(solucao);
		if(!solicitacao.getStatus().equals(SolicitacaoStatus.FINALIZADO)) {
			solicitacao.setStatus(SolicitacaoStatus.PENDENTE);
		}
		
		String mensagem = solucao.hasId() ? "Dados alterados com sucesso!" : "Solução cadastrada com sucesso";
		service.salvar(solucao);
		
		if(newSolucao != null) {
			historicoSolicitacaoService.salvar(newSolucao);
		}
		
		if(edicaoSolucao != null) {
			historicoSolicitacaoService.salvar(edicaoSolucao);
		}
		
		attr.addFlashAttribute("sucesso", mensagem);
		
		return "redirect:/solicitacoes/visualizar/{idSolicitacao}";
	}
	@GetMapping("/buscar/{id}")
	public ResponseEntity<?> solucaoPorSolicitacaoId(@PathVariable("id") Long id) {
		Solucao solucao = service.buscarPorSolcitacaoId(id);
		return ResponseEntity.ok(solucao);
	}
	
	
	
	
}
