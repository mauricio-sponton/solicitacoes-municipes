package br.com.solicitacoes.solicitacoes.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.solicitacoes.solicitacoes.domain.Atraso;
import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.SolicitacaoStatus;
import br.com.solicitacoes.solicitacoes.domain.Solucao;
import br.com.solicitacoes.solicitacoes.domain.Usuario;
import br.com.solicitacoes.solicitacoes.service.AtrasoService;
import br.com.solicitacoes.solicitacoes.service.SolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.SolucaoService;
import br.com.solicitacoes.solicitacoes.service.UsuarioService;

@Controller
@RequestMapping("atrasos")
public class AtrasoController extends ParentController {

	@Autowired
	private SolicitacaoService solicitacaoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private AtrasoService service;

	@Autowired
	private SolucaoService solucaoService;

	@PostMapping("/salvar/{idSolicitacao}")
	public String salvar(@Valid Atraso atraso, BindingResult result, @PathVariable("idSolicitacao") Long idSolicitacao,
			RedirectAttributes attr, @AuthenticationPrincipal User user, ModelMap model) {
		Solicitacao solicitacao = solicitacaoService.buscarPorId(idSolicitacao);
		if (result.hasErrors()) {
			model.addAttribute("erro", "Por favor preencha os dados");
			model.addAttribute("solicitacao", solicitacao);
			model.addAttribute("atraso", new Atraso());
			model.addAttribute("solucao", new Solucao());
			return "solicitacao/visualizar";
		}
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());

		atraso.setData(LocalDate.now());
		atraso.setHora(LocalTime.now());
		atraso.setUsuario(usuario.getNome());
		atraso.setSolicitacao(solicitacao);
		if (solicitacao.getStatus().equals(SolicitacaoStatus.ABERTO)
				|| solicitacao.getStatus().equals(SolicitacaoStatus.ATRASADO)) {
			solicitacao.setStatus(SolicitacaoStatus.ATRASADO);
		} else {
			model.addAttribute("falha", "Essa solicitação está pendente ou já foi finalizada!");
			model.addAttribute("solicitacao", solicitacao);
			model.addAttribute("atraso", new Atraso());
			model.addAttribute("solucao", solucaoService.buscarPorSolcitacaoId(idSolicitacao));
			return "solicitacao/visualizar";
		}

		service.salvar(atraso);
		attr.addAttribute("sucesso", "A solicitação foi finalizada!");

		return "redirect:/solicitacoes/visualizar/{idSolicitacao}";
	}
}
