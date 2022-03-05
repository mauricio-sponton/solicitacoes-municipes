package br.com.solicitacoes.solicitacoes.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import br.com.solicitacoes.solicitacoes.domain.Oficio;
import br.com.solicitacoes.solicitacoes.service.OficioService;

@Controller
@RequestMapping("oficios")
public class OficioController extends ParentController {

	@Autowired
	private OficioService service;
	

	@GetMapping("/listar")
	public String listar(Oficio oficio) {
		return "oficio/lista";
	}

	@PostMapping({ "/salvar", "/salvar/{id}" })
	public String salvar(@Valid Oficio oficio, BindingResult result, RedirectAttributes attr, ModelMap model,
			@AuthenticationPrincipal User user, @PathVariable(value = "id", required = false) Long id) {
		if (result.hasErrors() && id != null) {
			model.addAttribute("erro", "por favor preencha os campos");
			return "/oficio/visualizar";
		}

		if (result.hasErrors()) {
			model.addAttribute("erro", "por favor preencha os campos");
			System.out.println(result.getAllErrors());
			return "/oficio/lista";
		}
		
		
		String mensagem = oficio.hasNotId() ? "Dados cadastrados com sucesso!" : "Dados alterados com sucesso!";

		service.salvar(oficio);
		attr.addFlashAttribute("sucesso", mensagem);

		return id == null ? "redirect:/oficios/listar" : "redirect:/oficios/visualizar/{id}";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("oficio", service.buscarPorId(id));
		return "oficio/lista";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/oficios/listar";
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarAssuntosDatatables(HttpServletRequest request,
			@RequestParam(required = false, value = "dataInicial") @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicial,
			@RequestParam(required = false, value = "dataFinal") @DateTimeFormat(iso = ISO.DATE) LocalDate dataFinal) {
		return ResponseEntity.ok(service.buscarTodos(request, dataInicial, dataFinal));
	}

	@GetMapping("/visualizar/{id}")
	public String visualizar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("oficio", service.buscarPorId(id));
		return "/oficio/visualizar";
	}
}
