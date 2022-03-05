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

import br.com.solicitacoes.solicitacoes.domain.Requerimento;
import br.com.solicitacoes.solicitacoes.domain.RespostaRequerimento;
import br.com.solicitacoes.solicitacoes.service.RequerimentoService;
import br.com.solicitacoes.solicitacoes.service.RespostaRequerimentoService;

@Controller
@RequestMapping("requerimentos")
public class RequerimentoController extends ParentController{

	@Autowired
	private RequerimentoService service;

	@Autowired
	private RespostaRequerimentoService respostaRequerimentoService;

	@GetMapping("/listar")
	public String listar(Requerimento requerimento) {
		return "requerimento/lista";
	}

	@PostMapping({ "/salvar", "/salvar/{id}" })
	public String salvar(@Valid Requerimento requerimento, BindingResult result, RedirectAttributes attr,
			ModelMap model, @AuthenticationPrincipal User user, @PathVariable(value = "id", required = false) Long id) {

		if (result.hasErrors() && id != null) {
			model.addAttribute("erro_requerimento", "por favor preencha os campos");
			RespostaRequerimento respostaRequerimento = respostaRequerimentoService.buscarPorRequerimentoId(id);
			if (respostaRequerimento != null) {
				model.addAttribute("respostaRequerimento", respostaRequerimento);
			} else {
				model.addAttribute("respostaRequerimento", new RespostaRequerimento());
			}
			return "/requerimento/visualizar";
		}

		if (result.hasErrors()) {
			model.addAttribute("erro", "por favor preencha os campos");
			return "/requerimento/lista";
		}

		if(requerimento.hasId()) {
			RespostaRequerimento respostaRequerimento = respostaRequerimentoService.buscarPorRequerimentoId(requerimento.getId());
			if(respostaRequerimento != null) {
				requerimento.setResposta(respostaRequerimento);
			}
		}
		
		String mensagem = requerimento.hasNotId() ? "Dados cadastrados com sucesso!" : "Dados alterados com sucesso!";
		
		service.salvar(requerimento);
		attr.addFlashAttribute("sucesso", mensagem);

		return id == null ? "redirect:/requerimentos/listar" : "redirect:/requerimentos/visualizar/{id}";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("requerimento", service.buscarPorId(id));
		return "requerimento/lista";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/requerimentos/listar";
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarRequerimentoDatatables(HttpServletRequest request,
			@RequestParam(required = false, value = "dataInicial") @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicial,
			@RequestParam(required = false, value = "dataFinal") @DateTimeFormat(iso = ISO.DATE) LocalDate dataFinal,
			@RequestParam(required = false, value = "buscaAssunto") String buscaAssunto) {
		return ResponseEntity.ok(service.buscarTodos(request, dataInicial, dataFinal, buscaAssunto));
	}

	@GetMapping("/visualizar/{id}")
	public String visualizar(@PathVariable("id") Long id, ModelMap model) {
		Requerimento requerimento = service.buscarPorId(id);
		RespostaRequerimento respostaRequerimento = respostaRequerimentoService.buscarPorRequerimentoId(id);
		if (respostaRequerimento != null) {
			model.addAttribute("respostaRequerimento", respostaRequerimento);
		} else {
			model.addAttribute("respostaRequerimento", new RespostaRequerimento());
		}
		model.addAttribute("requerimento", requerimento);
		return "requerimento/visualizar";
	}
}
