package br.com.solicitacoes.solicitacoes.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.solicitacoes.solicitacoes.domain.Requerimento;
import br.com.solicitacoes.solicitacoes.domain.RespostaRequerimento;
import br.com.solicitacoes.solicitacoes.service.RequerimentoService;
import br.com.solicitacoes.solicitacoes.service.RespostaRequerimentoService;

@Controller
@RequestMapping("resposta-requerimento")
public class RespostaRequerimentoController extends ParentController{

	@Autowired
	private RequerimentoService requerimentoService;
	
	@Autowired
	private RespostaRequerimentoService service;

	@PostMapping("/salvar/{id}")
	public String salvar(@Valid RespostaRequerimento respostaRequerimento, BindingResult result, @PathVariable("id") Long id,
			RedirectAttributes attr, ModelMap model) {

		Requerimento requerimento = requerimentoService.buscarPorId(id);
		if (result.hasErrors()) {
			model.addAttribute("erro", "por favor preencha os campos");
			model.addAttribute("requerimento", requerimento);
			
			return "requerimento/visualizar";
		}
		
		String mensagem = respostaRequerimento.hasNotId() ? "A resposta foi cadastrada!" : "Dados alterados com sucesso!";
		
		respostaRequerimento.setRequerimento(requerimento);
		requerimento.setResposta(respostaRequerimento);
		service.salvar(respostaRequerimento);
		attr.addFlashAttribute("sucesso", mensagem);

		return "redirect:/requerimentos/visualizar/{id}";
	}
}
