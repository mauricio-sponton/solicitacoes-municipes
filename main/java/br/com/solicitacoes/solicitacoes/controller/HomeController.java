package br.com.solicitacoes.solicitacoes.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.solicitacoes.solicitacoes.domain.ChartDTO;
import br.com.solicitacoes.solicitacoes.domain.ChartPieDTO;
import br.com.solicitacoes.solicitacoes.domain.Cliente;
import br.com.solicitacoes.solicitacoes.domain.HistoricoSolicitacao;
import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.SolicitacaoStatus;
import br.com.solicitacoes.solicitacoes.domain.Usuario;
import br.com.solicitacoes.solicitacoes.historicos.AddHistorico;
import br.com.solicitacoes.solicitacoes.service.ClienteService;
import br.com.solicitacoes.solicitacoes.service.HistoricoSolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.SolicitacaoService;
import br.com.solicitacoes.solicitacoes.service.SolucaoService;
import br.com.solicitacoes.solicitacoes.service.UsuarioService;

@Controller
public class HomeController{

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private SolicitacaoService solicitacaoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private HistoricoSolicitacaoService historicoSolicitacaoService;

	@Autowired
	private SolucaoService solucaoService;
	
	@GetMapping({ "/home", "/buscar/data/{ano}" })
	public String home(ModelMap model, @PathVariable(value = "ano", required = false) Integer ano, @AuthenticationPrincipal User user) {
		Calendar calendar = Calendar.getInstance();
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		model.addAttribute("nome_usuario", usuario.getNome());
		
		if (ano != null) {
			model.addAttribute("grafico", getChartData(ano));
			model.addAttribute("avisos", getChartAvisos(ano));
			model.addAttribute("status", getChartStatus(ano));
		} else {
			model.addAttribute("grafico", getChartData(calendar.get(Calendar.YEAR)));
			model.addAttribute("avisos", getChartAvisos(calendar.get(Calendar.YEAR)));
			model.addAttribute("status", getChartStatus(calendar.get(Calendar.YEAR)));
		}

		getAniversarios(model);
		getCards(model);
		solicitacoesAtrasadas(model);
		model.addAttribute("solicitacoesAtrasadas", solicitacaoService.buscarSolicitacaoPorStatusAtrasado());
		model.addAttribute("solicitacoesPendentes", solicitacaoService.buscarSolicitacaoPorStatusPendente());
		return "home";
	}

	private List<ChartPieDTO> getChartStatus(int ano) {
		List<ChartPieDTO> valores = new ArrayList<>();
		
		int pendentes = solicitacaoService.buscarSolicitacaoPorStatusPendenteEAno(ano).size();
		int atrasadas = solicitacaoService.buscarSolicitacaoPorStatusAtrasadoEAno(ano).size();
		int abertas = solicitacaoService.buscarSolicitacaoPorStatusAbertoEAno(ano).size();
		int finalizadas = solicitacaoService.buscarSolicitacaoPorStatusFinalizadoEAno(ano).size();
		if(pendentes != 0) {
			valores.add(new ChartPieDTO("Pendentes", pendentes));
		}
		if(atrasadas != 0) {
			valores.add(new ChartPieDTO("Atrasadas", atrasadas));
		}
		
		if(finalizadas != 0) {
			valores.add(new ChartPieDTO("Finalizadas", finalizadas));
		}
		
		if(abertas != 0) {
			valores.add(new ChartPieDTO("Abertas", abertas));
		}
			
		return valores;
	}
	
	private List<ChartDTO> getChartAvisos(int ano) {
		List<ChartDTO> valores = new ArrayList<>();
		String mes = "";
	
		for(int i = 0; i < 12; i++) {
			mes = meses(mes, i);
			int sim = solucaoService.buscarPorAvisoSimEMesEAno(i, ano).size();
			int nao = solucaoService.buscarPorAvisoNaoEMesEAno(i, ano).size();
	
			if(sim != 0 || nao != 0) {
				valores.add(new ChartDTO(mes, sim, nao));
			}
		}
		return valores;
	}

	private List<ChartDTO> getChartData(int ano) {
		List<ChartDTO> valores = new ArrayList<>();
		String mes = "";

		for (int i = 0; i <= 12; i++) {
			mes = meses(mes, i);
			int positivas = solucaoService.buscarPorResultadoPositivoEMesEAno(i, ano).size();
			int negativas = solucaoService.buscarPorResultadoNegativoEMesEAno(i, ano).size();
			if (positivas != 0 || negativas != 0) {
				valores.add(new ChartDTO(mes, positivas, negativas));
			}

		}

//		valores.add(new ChartDTO("Janeiro", solucaoService.buscarPorResultadoPositivoEMesEAno(1).size(),
//				solucaoService.buscarPorResultadoNegativoEMesEAno(1).size()));
//		valores.add(new ChartDTO("Fevereiro", solucaoService.buscarPorResultadoPositivoEMesEAno(2).size(),
//				solucaoService.buscarPorResultadoNegativoEMesEAno(2).size()));
//		valores.add(new ChartDTO("Março", solucaoService.buscarPorResultadoPositivoEMesEAno(3).size(), solucaoService.buscarPorResultadoNegativoEMesEAno(3).size()));
//		valores.add(new ChartDTO("Abril", solucaoService.buscarPorResultadoPositivoEMesEAno(4).size(), solucaoService.buscarPorResultadoNegativoEMesEAno(4).size()));
//		valores.add(new ChartDTO("Maio", solucaoService.buscarPorResultadoPositivoEMesEAno(5).size(), solucaoService.buscarPorResultadoNegativoEMesEAno(5).size()));
//		valores.add(new ChartDTO("Junho", solucaoService.buscarPorResultadoPositivoEMesEAno(6).size(), solucaoService.buscarPorResultadoNegativoEMesEAno(6).size()));
//		valores.add(new ChartDTO("Julho", solucaoService.buscarPorResultadoPositivoEMesEAno(7).size(), solucaoService.buscarPorResultadoNegativoEMesEAno(7).size()));
//		valores.add(new ChartDTO("Agosto", solucaoService.buscarPorResultadoPositivoEMesEAno(8).size(), solucaoService.buscarPorResultadoNegativoEMesEAno(8).size()));
//		valores.add(new ChartDTO("Setembro", solucaoService.buscarPorResultadoPositivoEMesEAno(9).size(), solucaoService.buscarPorResultadoNegativoEMesEAno(9).size()));
//		valores.add(new ChartDTO("Outubro", solucaoService.buscarPorResultadoPositivoEMesEAno(10).size(), solucaoService.buscarPorResultadoNegativoEMesEAno(10).size()));
//		valores.add(new ChartDTO("Novembro", solucaoService.buscarPorResultadoPositivoEMesEAno(11).size(), solucaoService.buscarPorResultadoNegativoEMesEAno(11).size()));
//		valores.add(new ChartDTO("Dezembro", solucaoService.buscarPorResultadoPositivoEMesEAno(12).size(), solucaoService.buscarPorResultadoNegativoEMesEAno(12).size()));

		return valores;
	}

	private String meses(String mes, int i) {
		switch (i) {
		case 1:
			mes = "Janeiro";
			break;
		case 2:
			mes = "Fevereiro";
			break;
		case 3:
			mes = "Março";
			break;
		case 4:
			mes = "Abril";
			break;
		case 5:
			mes = "Maio";
			break;
		case 6:
			mes = "Junho";
			break;
		case 7:
			mes = "Julho";
			break;
		case 8:
			mes = "Agosto";
			break;
		case 9:
			mes = "Setembro";
			break;
		case 10:
			mes = "Outubro";
			break;
		case 11:
			mes = "Novembro";
			break;
		case 12:
			mes = "Dezembro";
			break;
		}
		return mes;
	}

	@GetMapping({ "/", "/login" })
	public String login() {

		return "login";
	}

	@GetMapping({ "/login-error" })
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais inválidas!");
		model.addAttribute("texto", "Login ou senha inválidos, tente novamente");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados");
		return "login";
	}
	
	@GetMapping({ "/expired" })
	public String sessaoExpirada(ModelMap model) {
		model.addAttribute("expirado", "erro");
		model.addAttribute("titulo", "Acesso recusado!");
		model.addAttribute("texto", "Sua sessão expirou.");
		model.addAttribute("subtexto", "Você logou em outro dispositivo.");
		return "login";
	}

	// acesso negado
	@GetMapping({ "/acesso-negado" })
	public String acessoNegado(ModelMap model, HttpServletResponse resp) {
		model.addAttribute("status", resp.getStatus());
		model.addAttribute("error", "Acesso negado");
		model.addAttribute("message", "Você não tem permissão para acesso a esta área ou ação");
		return "error";
	}

	private void getAniversarios(ModelMap model) {
		List<Cliente> aniversarios = clienteService.buscarTodosClientes();
		List<Cliente> aniversarioHoje = new ArrayList<>();
		List<Cliente> aniversarioAmanha = new ArrayList<>();
		Integer mesAtual = LocalDate.now().getMonth().getValue();
		Integer diaAtual = LocalDate.now().getDayOfMonth();
		for (Cliente c : aniversarios) {		
			Integer diaAniversario = c.getDataNascimento().getDayOfMonth();
			Integer mesAniversario = c.getDataNascimento().getMonth().getValue();
			
			if (diaAniversario.equals(diaAtual) && mesAniversario.equals(mesAtual)) {
				aniversarioHoje.add(c);
			}
			if (diaAniversario.equals(diaAtual + 1) && mesAniversario.equals(mesAtual)) {
				aniversarioAmanha.add(c);
			}
		}
		model.addAttribute("aniversarioAmanha", aniversarioAmanha);
		model.addAttribute("aniversarioHoje", aniversarioHoje);
	}

	private void getCards(ModelMap model) {
		List<Cliente> clientes = clienteService.buscarTodosClientes();
		List<Solicitacao> solicitacoes = solicitacaoService.buscarTodasSolicitacoes();
		List<Solicitacao> solicitacoesAbertas = solicitacaoService.buscarSolicitacaoPorStatusAberto();
		List<Solicitacao> solicitacoesFinalizadas = solicitacaoService.buscarSolicitacaoPorStatusFinalizado();
		List<Solicitacao> solicitacoesAtrasadas = solicitacaoService.buscarSolicitacaoPorStatusAtrasado();
		List<Solicitacao> solicitacoesPendentes = solicitacaoService.buscarSolicitacaoPorStatusPendente();
		List<Solicitacao> positivas = solicitacaoService.buscarPorResultadosPositivos();
		List<Solicitacao> negativos = solicitacaoService.buscarPorResultadosNegativos();
		List<Solicitacao> avisados = solicitacaoService.buscarPorMunicipesAvisados();
		List<Solicitacao> naoAvisados = solicitacaoService.buscarPorMunicipesNaoAvisados();
		List<Usuario> usuarios = usuarioService.buscarTodosUsuarios();

		model.addAttribute("clientes", clientes);
		model.addAttribute("solicitacoesAbertas", solicitacoesAbertas.size());
		model.addAttribute("solicitacoesFinalizadas", solicitacoesFinalizadas.size());
		model.addAttribute("solicitacoesPendentes", solicitacoesPendentes.size());
		model.addAttribute("solicitacoesAtrasadas", solicitacoesAtrasadas.size());
		model.addAttribute("solicitacoes", solicitacoes);
		model.addAttribute("positivas", positivas.size());
		model.addAttribute("negativos", negativos.size());
		model.addAttribute("avisados", avisados.size());
		model.addAttribute("naoAvisados", naoAvisados.size());
		model.addAttribute("usuarios", usuarios);
	}

	private void solicitacoesAtrasadas(ModelMap model) {
		List<Solicitacao> solicitacoes = solicitacaoService.buscarTodasSolicitacoes();
		List<Solicitacao> atrasadas = new ArrayList<Solicitacao>();
		for (Solicitacao s : solicitacoes) {
			if (s.isAtrasado() && s.getStatus().equals(SolicitacaoStatus.ABERTO)) {
				AddHistorico addHistorico = new AddHistorico();
				HistoricoSolicitacao historico = addHistorico.solicitacaoAtrasada(s);
				if (historico != null) {
					historicoSolicitacaoService.salvar(historico);
				}
			}
			if (s.isAtrasado() && (s.getStatus().equals(SolicitacaoStatus.ABERTO)
					|| s.getStatus().equals(SolicitacaoStatus.ATRASADO))) {
				s.setStatus(SolicitacaoStatus.ATRASADO);
				solicitacaoService.salvar(s);
				atrasadas.add(s);
			}

		}
		model.addAttribute("atrasadas", atrasadas);
		
	}
}
