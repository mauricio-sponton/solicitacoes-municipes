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

import br.com.solicitacoes.solicitacoes.domain.AndamentoLei;
import br.com.solicitacoes.solicitacoes.domain.Lei;
import br.com.solicitacoes.solicitacoes.domain.LeiStatus;
import br.com.solicitacoes.solicitacoes.service.AndamentoLeiService;
import br.com.solicitacoes.solicitacoes.service.LeiService;

@Controller
@RequestMapping("leis")
public class LeiController extends ParentController{

	@Autowired
	private LeiService service;

	@Autowired
	private AndamentoLeiService andamentoLeiService;

	@GetMapping("/listar")
	public String listar(Lei lei) {
		return "lei/lista";
	}

	@PostMapping({ "/salvar", "/salvar/{id}" })
	public String salvar(@Valid Lei lei, BindingResult result, RedirectAttributes attr, ModelMap model,
			@AuthenticationPrincipal User user, @PathVariable(value = "id", required = false) Long id) {

		if (result.hasErrors() && id != null) {
			model.addAttribute("erro_lei", "por favor preencha os campos");
			model.addAttribute("andamentoLei", new AndamentoLei());
			model.addAttribute("andamentos", andamentoLeiService.buscarPorLeiId(id));
			return "/lei/visualizar";
		}

		if (result.hasErrors()) {
			model.addAttribute("erro", "por favor preencha os campos");
			return "/lei/lista";
		}
		String mensagem = lei.hasNotId() ? "Dados cadastrados com sucesso!" : "Dados alterados com sucesso!";

		if (lei.hasNotId()) {
			lei.setStatus(LeiStatus.CRIADA);
		} else {
			Lei gravada = service.buscarPorId(lei.getId());
			lei.setStatus(gravada.getStatus());
		}

		service.salvar(lei);
		attr.addFlashAttribute("sucesso", mensagem);

		return id == null ? "redirect:/leis/listar" : "redirect:/leis/visualizar/{id}";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("lei", service.buscarPorId(id));
		return "lei/lista";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/leis/listar";
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarAssuntosDatatables(HttpServletRequest request,
			@RequestParam(required = false, value = "buscaAssunto") String buscaAssunto, 
			@RequestParam(required = false, value = "buscaData") @DateTimeFormat(iso = ISO.DATE) LocalDate buscaData,
			@RequestParam(required = false, value = "buscaDataFinal") @DateTimeFormat(iso = ISO.DATE) LocalDate buscaDataFinal,
			@RequestParam(required = false, value = "status") String status) {
		return ResponseEntity.ok(service.buscarTodos(request, buscaAssunto, buscaData, buscaDataFinal, status));
	}

	@GetMapping("/visualizar/{id}")
	public String visualizar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("lei", service.buscarPorId(id));
		model.addAttribute("andamentoLei", new AndamentoLei());
		model.addAttribute("andamentos", andamentoLeiService.buscarPorLeiId(id));
		return "/lei/visualizar";
	}
}
