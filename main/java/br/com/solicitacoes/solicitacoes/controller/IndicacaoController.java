package br.com.solicitacoes.solicitacoes.controller;

import java.time.LocalDate;
import java.util.List;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.solicitacoes.solicitacoes.domain.Indicacao;
import br.com.solicitacoes.solicitacoes.domain.RespostaIndicacao;
import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.Solucao;
import br.com.solicitacoes.solicitacoes.service.HistoricoSolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.IndicacaoService;
import br.com.solicitacoes.solicitacoes.service.RespostaIndicacaoService;
import br.com.solicitacoes.solicitacoes.service.SolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.SolucaoService;

@Controller
@RequestMapping("indicacoes")
public class IndicacaoController extends ParentController{

	@Autowired
	private IndicacaoService service;

	@Autowired
	private SolicitacaoService solicitacaoService;

	@Autowired
	private HistoricoSolicitacaoService historicoSolicitacaoService;

	@Autowired
	private SolucaoService solucaoService;

	@Autowired
	private RespostaIndicacaoService respostaIndicacaoService;

	@GetMapping("/listar")
	public String listar(Indicacao indicacao) {
		return "indicacao/lista";
	}

	@PostMapping({ "/salvar", "/salvar/codigo/{id}" })
	public String salvar(@Valid Indicacao indicacao, BindingResult result, RedirectAttributes attr, ModelMap model,
			@AuthenticationPrincipal User user, @PathVariable(value = "id", required = false) Long id) {

		if (result.hasErrors() && id != null) {
			model.addAttribute("erro_indicacao", "por favor preencha os campos");
			atribuirResposta(id, model);
			return "/indicacao/visualizar";
		}

		if (result.hasErrors()) {
			model.addAttribute("erro", "por favor preencha os campos");
			return "/indicacao/lista";
		}

		String mensagem = indicacao.hasNotId() ? "Dados cadastrados com sucesso!" : "Dados alterados com sucesso!";
		
		if(indicacao.hasId()) {
			RespostaIndicacao resposta = respostaIndicacaoService.buscarPorIndicacaoId(indicacao.getId());
			if(resposta != null) {
				indicacao.setResposta(resposta);
			}
		}

		if (indicacao.hasId() && indicacao.getSolicitacao() != null) {
			Solicitacao solicitacao = solicitacaoService.buscarPorId(indicacao.getSolicitacao().getId());
			Indicacao i2 = service.buscarPorId(indicacao.getId());

			if (solicitacao.isIndicado()) {
				if (i2.getSolicitacao() == null) {
					return redirecionamento(model, id);
				}
				if (!solicitacao.getId().equals(i2.getSolicitacao().getId())) {
					return redirecionamento(model, id);
				}
				
			} else {
				if (i2.getSolicitacao() != null) {
					if (!solicitacao.getId().equals(i2.getSolicitacao().getId())) {
						i2.getSolicitacao().setIndicado(false);
						i2.getSolicitacao().setIndicacao(null);
					}
					i2.getSolicitacao().setIndicado(false);
					i2.getSolicitacao().setIndicacao(null);
				}
				solicitacao.setIndicado(true);
				solicitacao.setIndicacao(indicacao);
			}
		}
		if (indicacao.hasId() && indicacao.getSolicitacao() == null) {
			//Solicitacao solicitacao = solicitacaoService.buscarPorId(indicacao.getSolicitacao().getId());
			Indicacao i2 = service.buscarPorId(indicacao.getId());
			if (i2.getSolicitacao() != null) {
				i2.getSolicitacao().setIndicado(false);
				i2.getSolicitacao().setIndicacao(null);
				
			}

		}
		if (indicacao.hasNotId() && indicacao.getSolicitacao() != null) {
			Solicitacao solicitacao = solicitacaoService.buscarPorId(indicacao.getSolicitacao().getId());

			if (solicitacao.isIndicado()) {
				return redirecionamento(model, id);
			} else {
				solicitacao.setIndicado(true);
				solicitacao.setIndicacao(indicacao);
			}
		}

		service.salvar(indicacao);
		attr.addFlashAttribute("sucesso", mensagem);

		return id == null ? "redirect:/indicacoes/listar" : "redirect:/indicacoes/visualizar/{id}";
	}

	private String redirecionamento(ModelMap model, Long id) {
		model.addAttribute("erroSolicitacao", "Essa solicitação já foi indicada, por favor escolha outra.");
		if (id == null) {
			return "/indicacao/lista";
		} else {
			atribuirResposta(id, model);
			return "/indicacao/visualizar";
		}
	}

	@PostMapping("/salvar/{id}")
	public String salvarPorSolicitacao(@Valid Indicacao indicacao, BindingResult result, RedirectAttributes attr,
			ModelMap model, @AuthenticationPrincipal User user, @PathVariable("id") Long id) {
		Solicitacao solicitacao = solicitacaoService.buscarPorId(id);
		if (result.hasErrors()) {
			model.addAttribute("erroIndicar", "por favor preencha os campos");
			model.addAttribute("solicitacao", solicitacao);
			model.addAttribute("historico",
					historicoSolicitacaoService.buscarHistoricoPorSolicitacaoId(solicitacao.getId()));
			Solucao solucao = solucaoService.buscarPorSolcitacaoId(solicitacao.getId());
			if (solucao == null) {
				model.addAttribute("solucao", new Solucao());
			} else {
				model.addAttribute("solucao", solucao);
			}
			return "solicitacao/visualizar";
		}

		try {
			indicacao.setSolicitacao(solicitacao);
			solicitacao.setIndicado(true);
			solicitacao.setIndicacao(indicacao);
			service.salvar(indicacao);

			attr.addFlashAttribute("sucesso", "Solicitação indicada com sucesso!");
			attr.addFlashAttribute("link", indicacao.getId());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "redirect:/solicitacoes/visualizar/{id}";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("indicacao", service.buscarPorId(id));
		// model.addAttribute("solicitacoes", getSolicitacoes(id));
		return "indicacao/lista";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		Solicitacao solicitacao = solicitacaoService.buscarPorIndicacaoId(id);
		if (solicitacao != null) {
			
			if (solicitacao.isIndicado()) {
				solicitacao.setIndicado(false);
				solicitacao.setIndicacao(null);
			}
		}

		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/indicacoes/listar";
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarAssuntosDatatables(HttpServletRequest request,
			@RequestParam(required = false, value = "dataInicial") @DateTimeFormat(iso = ISO.DATE) LocalDate dataInicial,
			@RequestParam(required = false, value = "dataFinal") @DateTimeFormat(iso = ISO.DATE) LocalDate dataFinal,
			@RequestParam(required = false, value = "buscaAssunto") String buscaAssunto, 
			@RequestParam(required = false, value = "buscaSessao") String buscaSessao) {
		return ResponseEntity.ok(service.buscarTodos(request, dataInicial, dataFinal, buscaAssunto, buscaSessao));
	}

	@GetMapping("/visualizar/{id}")
	public String visualizar(@PathVariable("id") Long id, ModelMap model) {
		Indicacao indicacao = service.buscarPorId(id);
		atribuirResposta(id, model);
		model.addAttribute("indicacao", indicacao);
		return "indicacao/visualizar";
	}

	private void atribuirResposta(Long id, ModelMap model) {
		RespostaIndicacao respostaIndicacao = respostaIndicacaoService.buscarPorIndicacaoId(id);
		if (respostaIndicacao != null) {
			model.addAttribute("respostaIndicacao", respostaIndicacao);
		} else {
			model.addAttribute("respostaIndicacao", new RespostaIndicacao());
		}
	}

	@ModelAttribute("solicitacoes")
	public List<Solicitacao> getSolicitacoes() {
		return solicitacaoService.buscarTodasSolicitacoes();
	}
}
