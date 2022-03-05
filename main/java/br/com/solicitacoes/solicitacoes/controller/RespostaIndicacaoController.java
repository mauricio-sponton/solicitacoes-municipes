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

import br.com.solicitacoes.solicitacoes.domain.Indicacao;
import br.com.solicitacoes.solicitacoes.domain.RespostaIndicacao;
import br.com.solicitacoes.solicitacoes.service.IndicacaoService;
import br.com.solicitacoes.solicitacoes.service.RespostaIndicacaoService;

@Controller
@RequestMapping("resposta-indicacao")
public class RespostaIndicacaoController extends ParentController{

	@Autowired
	private IndicacaoService indicacaoService;
	
	@Autowired
	private RespostaIndicacaoService service;

	@PostMapping("/salvar/{idIndicacao}")
	public String salvar(@Valid RespostaIndicacao respostaIndicacao, BindingResult result, @PathVariable("idIndicacao") Long idIndicacao,
			RedirectAttributes attr, ModelMap model) {

		Indicacao indicacao = indicacaoService.buscarPorId(idIndicacao);
		if (result.hasErrors()) {
			model.addAttribute("erro", "por favor preencha os campos");
			model.addAttribute("indicacao", indicacao);
			
			
			return "indicacao/visualizar";
		}
		
		String mensagem = respostaIndicacao.hasNotId() ? "A resposta foi cadastrada!" : "Dados alterados com sucesso!";
		
		
//		if(resposta.hasNotId()) {
//			indicacao.setResposta(resposta);
//		}
		respostaIndicacao.setIndicacao(indicacao);
		indicacao.setResposta(respostaIndicacao);
		
		service.salvar(respostaIndicacao);
		attr.addFlashAttribute("sucesso", mensagem);

		return "redirect:/indicacoes/visualizar/{idIndicacao}";
	}
}
