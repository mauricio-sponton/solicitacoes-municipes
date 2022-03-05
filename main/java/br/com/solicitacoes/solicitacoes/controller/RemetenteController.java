package br.com.solicitacoes.solicitacoes.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.solicitacoes.solicitacoes.domain.Remetente;
import br.com.solicitacoes.solicitacoes.domain.UF;
import br.com.solicitacoes.solicitacoes.service.RemetenteService;

@Controller
@RequestMapping("remetentes")
public class RemetenteController extends ParentController{
	
	@Autowired
	private RemetenteService service;

	@GetMapping("/listar")
	public String listar(Remetente remetente) {
		return "remetente/lista";
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Remetente remetente, BindingResult result, RedirectAttributes attr, ModelMap model,
			@AuthenticationPrincipal User user) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "por favor preencha os campos");
			return "/remetente/lista";
		}
		String mensagem = remetente.hasNotId() ? "Dados cadastrados com sucesso!" : "Dados alterados com sucesso!";
		service.salvar(remetente);
		attr.addFlashAttribute("sucesso", mensagem);
		

		return "redirect:/remetentes/listar";
	}
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("remetente", service.buscarPorId(id));
		return "remetente/lista";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/remetentes/listar";
	}
	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarRemetentesDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	
	@ModelAttribute("ufs")
	public UF[] getUFs() {
		return UF.values();
	}
}
