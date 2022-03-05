package br.com.solicitacoes.solicitacoes.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.solicitacoes.solicitacoes.domain.AndamentoLei;
import br.com.solicitacoes.solicitacoes.domain.Lei;
import br.com.solicitacoes.solicitacoes.domain.LeiStatus;
import br.com.solicitacoes.solicitacoes.service.AndamentoLeiService;
import br.com.solicitacoes.solicitacoes.service.LeiService;

@Controller
@RequestMapping("andamentos")
public class AndamentoLeiController extends ParentController{

	@Autowired
	private LeiService leiService;

	@Autowired
	private AndamentoLeiService service;

	@PostMapping("/salvar/{id}")
	public String salvar(@Valid AndamentoLei andamentoLei, BindingResult result, @PathVariable("id") Long id,
			RedirectAttributes attr, ModelMap model) {

		Lei lei = leiService.buscarPorId(id);

		if (result.hasErrors()) {
			if(andamentoLei.hasNotId()) {
				model.addAttribute("erro", "por favor preencha os campos");
			}else {
				model.addAttribute("erro_edit", "por favor preencha os campos");
			}
			
			model.addAttribute("lei", lei);
			model.addAttribute("andamentos", service.buscarPorLeiId(id));
			return "lei/visualizar";
		}

		String mensagem = andamentoLei.hasNotId() ? "O andamento foi cadastrado!" : "Dados alterados com sucesso!";

		if (andamentoLei.hasNotId()) {

			if (andamentoLei.getTipo().equals("propositura")) {
				lei.setStatus(LeiStatus.PROPOSITURA);
			}
			
			if (andamentoLei.getTipo().equals("Nao")) {
				lei.setStatus(LeiStatus.VETADO_PREFEITO);
			}
			if (andamentoLei.getTipo().equals("Sim")) {
				lei.setStatus(LeiStatus.APROVADO_PREFEITO);
			}
			if (andamentoLei.getTipo().equals("nova_votacao")) {
				lei.setStatus(LeiStatus.NOVA_VOTACAO);
			}
			if (andamentoLei.getTipo().equals("sim_vereadores")) {
				lei.setStatus(LeiStatus.APROVADO_VEREADORES);
			}
			if (andamentoLei.getTipo().equals("nao_vereadores")) {
				lei.setStatus(LeiStatus.VETADO_VEREADORES);
			}

		} else {
			if (andamentoLei.getTipo() == null) {
				AndamentoLei andamento = service.buscarPorId(andamentoLei.getId());
				andamentoLei.setTipo(andamento.getTipo());
			}
		}
		andamentoLei.setLei(lei);

		service.salvar(andamentoLei);
		attr.addFlashAttribute("sucesso", mensagem);

		return "redirect:/leis/visualizar/{id}";
	}

	@GetMapping("/editar/{id}/lei/{idLei}")
	public String preEditar(@PathVariable("id") Long id, @PathVariable("idLei") Long idLei, ModelMap model) {
		model.addAttribute("lei", leiService.buscarPorId(idLei));
		model.addAttribute("andamentoLei", service.buscarPorId(id));
		model.addAttribute("andamentos", service.buscarPorLeiId(idLei));
		return "lei/visualizar";
	}

}
