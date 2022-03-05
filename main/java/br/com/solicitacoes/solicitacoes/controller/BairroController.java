package br.com.solicitacoes.solicitacoes.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import br.com.solicitacoes.solicitacoes.domain.Bairro;
import br.com.solicitacoes.solicitacoes.service.BairroService;

@Controller
@RequestMapping("bairros")
public class BairroController extends ParentController{
	
	@Autowired
	private BairroService service;
	
	@GetMapping("/listar")
	public String listar(Bairro bairro) {
		return "bairro/lista";
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Bairro bairro, BindingResult result, RedirectAttributes attr, ModelMap model,
			@AuthenticationPrincipal User user) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "por favor preencha os campos");
			return "/bairro/lista";
		}
		String mensagem = bairro.hasNotId() ? "Dados cadastrados com sucesso!" : "Dados alterados com sucesso!";
		try {
			
			service.salvar(bairro);
			attr.addFlashAttribute("sucesso", mensagem);
		} catch (DataIntegrityViolationException ex) {
			attr.addFlashAttribute("falha", "Cadastro não realizado pois o bairro já existe");
		}

		return "redirect:/bairros/listar";
	}
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("bairro", service.buscarPorId(id));
		return "bairro/lista";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/bairros/listar";
	}
	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarBairrosDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	@GetMapping("/titulo")
	public ResponseEntity<?> getAssuntosPorTermo(@RequestParam("termo") String termo) {
		List<String> assuntos = service.buscarBairroByTermo(termo);
		return ResponseEntity.ok(assuntos);
	}
}
