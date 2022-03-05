package br.com.solicitacoes.solicitacoes.historicos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;

import br.com.solicitacoes.solicitacoes.domain.Bairro;
import br.com.solicitacoes.solicitacoes.domain.Cliente;
import br.com.solicitacoes.solicitacoes.domain.HistoricoMunicipe;
import br.com.solicitacoes.solicitacoes.domain.HistoricoSolicitacao;
import br.com.solicitacoes.solicitacoes.domain.HistoricoTipo;
import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.Solucao;
import br.com.solicitacoes.solicitacoes.domain.Telefone;
import br.com.solicitacoes.solicitacoes.domain.Usuario;

public class AddHistorico {

	private LocalDate data = LocalDate.now();
	private LocalTime hora = LocalTime.now();
	private HistoricoMunicipe historico = new HistoricoMunicipe();
	private HistoricoSolicitacao hSolicitacao = new HistoricoSolicitacao();

	

	public HistoricoMunicipe novaSolicitacaoPorMunicipe(Usuario usuario, Cliente cliente, Solicitacao solicitacao) {

		historico.setUsuario(usuario.getNome());
		historico.setData(data);
		historico.setHora(hora);
		historico.setCliente(cliente);
		historico
				.setDescricao("Uma nova solicitação foi aberta com o assunto : " + "<a href=\"/solicitacoes/visualizar/"
						+ solicitacao.getId() + "\">" + solicitacao.getAssunto().getDescricao() + "</a>" + "; "
						+ "Responsável pela solicitação: " + usuario.getNome());
		historico.setTipo(HistoricoTipo.SOLICITACAO_ABERTA);
		return historico;
	}

	public HistoricoSolicitacao solicitacaoAtrasada(Solicitacao s) {
		hSolicitacao.setData(data);
		hSolicitacao.setHora(hora);
		hSolicitacao.setSolicitacao(s);
		hSolicitacao.setUsuario(s.getUsuario());
		hSolicitacao.setDescricao("30 dias se passaram desde a data: "
				+ s.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "." + "; "
				+ "Solicitação realizada pelo munícipe: " + "<a href=\"/clientes/visualizar/" + s.getCliente().getId()
				+ "\">" + s.getCliente().getNome() + "</a>" + "; " + "Responsável pela solicitação: " + s.getUsuario());
		hSolicitacao.setTipo(HistoricoTipo.SOLICITACAO_ATRASADA);
		return hSolicitacao;
	}

	public HistoricoMunicipe cadastroDeMunicipe(Usuario usuario, Cliente cliente) {

		historico.setCliente(cliente);
		historico.setUsuario(usuario.getNome());
		historico.setData(data);
		historico.setHora(hora);
		historico.setTipo(HistoricoTipo.CLIENTE_NEW);
		historico.setDescricao("Cliente cadastrado no sistema!");
		return historico;
	}

	public HistoricoSolicitacao novaSolicitacao(Solicitacao s, Usuario usuario, Cliente cliente) {
		hSolicitacao.setData(data);
		hSolicitacao.setHora(hora);
		hSolicitacao.setSolicitacao(s);
		hSolicitacao.setUsuario(usuario.getNome());
		hSolicitacao.setDescricao(
				"Nova solicitação aberta pelo munícipe: " + "<a href=\"/clientes/visualizar/" + cliente.getId() + "\">"
						+ cliente.getNome() + "</a>" + "; " + "Responsável pela solicitação: " + usuario.getNome());
		hSolicitacao.setTipo(HistoricoTipo.SOLICITACAO_ABERTA);
		return hSolicitacao;
	}

	public HistoricoSolicitacao edicaoSolicitacao(Usuario usuario, Solicitacao solicitacaoId,
			@Valid Solicitacao solicitacao, Cliente cliente) {
		StringBuilder mud = new StringBuilder();

		mud.append("Os dados da solicitação foram alterados pelo usuário: " + "<b>" + usuario.getNome() + "</b>" + ";");
		if (!solicitacaoId.getAssunto().getDescricao().equals(solicitacao.getAssunto().getDescricao())) {
			mud.append("O assunto foi mudado de " + "<b>" + solicitacaoId.getAssunto().getDescricao() + "</b>"
					+ " para " + "<b>" + solicitacao.getAssunto().getDescricao() + "</b>" + ";");
			hSolicitacao.setDescricao(mud.toString());
		}
		if (!solicitacaoId.getBairro().getDescricao().equals(solicitacao.getBairro().getDescricao())) {

			mud.append("O bairro foi mudado de " + "<b>" + solicitacaoId.getBairro().getDescricao() + "</b>" + " para "
					+ "<b>" + solicitacao.getBairro().getDescricao() + "</b>" + ";");
			hSolicitacao.setDescricao(mud.toString());
		}

		if (!solicitacaoId.getCliente().equals(cliente)) {

			mud.append("O munícipe foi mudado de " + "<b>" + "<a href=\"/clientes/visualizar/"
					+ solicitacaoId.getCliente().getId() + "\">" + solicitacaoId.getCliente().getNome() + "</a>"
					+ "</b>" + " para " + "<b>" + "<a href=\"/clientes/visualizar/" + cliente.getId() + "\">"
					+ cliente.getNome() + "</a>" + "</b>" + ";");
			hSolicitacao.setDescricao(mud.toString());
		}
		if (!solicitacaoId.getDescricao().equals(solicitacao.getDescricao())) {

			mud.append("A descrição da solicitação foi alterada");
			hSolicitacao.setDescricao(mud.toString());
		}
		if (hSolicitacao.getDescricao() != null) {
			hSolicitacao.setData(data);
			hSolicitacao.setHora(hora);
			hSolicitacao.setSolicitacao(solicitacao);
			hSolicitacao.setUsuario(usuario.getNome());
			hSolicitacao.setTipo(HistoricoTipo.SOLICITACAO_EDIT);
			return hSolicitacao;
		}
		return null;

	}

	public HistoricoSolicitacao novaSolucao(Usuario usuario, Solicitacao solicitacao) {
		hSolicitacao.setData(data);
		hSolicitacao.setHora(hora);
		hSolicitacao.setSolicitacao(solicitacao);
		hSolicitacao.setUsuario(usuario.getNome());
		hSolicitacao.setDescricao("A solicitação entrou em estado pendente pelo usuário:  " + "<b>" + usuario.getNome()
				+ "</b>" + ";" + "Aguardando um Administrador para finalizar.");
		hSolicitacao.setTipo(HistoricoTipo.SOLICITACAO_PENDENTE);
		return hSolicitacao;
	}
	
	public HistoricoSolicitacao finalizada(Usuario usuario, Solicitacao solicitacao) {
		hSolicitacao.setData(LocalDate.now());
		hSolicitacao.setHora(LocalTime.now());
		hSolicitacao.setSolicitacao(solicitacao);
		hSolicitacao.setUsuario(usuario.getNome());
		hSolicitacao.setDescricao("A solicitação foi finalizada pelo usuário:  " + "<b>" + usuario.getNome()
				+ "</b>" + ";");
		hSolicitacao.setTipo(HistoricoTipo.SOLICITACAO_FINALIZADA);
		return hSolicitacao;
	}

	public HistoricoSolicitacao edicaoSolucao(Usuario usuario, Solicitacao solicitacao, Solucao antiga,
			@Valid Solucao nova) {
		StringBuilder mud = new StringBuilder();

		mud.append("Os dados da solução foram alterados pelo usuário: " + "<b>" + usuario.getNome() + "</b>" + ";");

		


		if (!antiga.getResultado().equals(nova.getResultado())) {
			mud.append("O resultado foi mudado de " + "<b>" + antiga.getResultado() + "</b>" + " para " + "<b>"
					+ nova.getResultado() + "</b>" + ";");
			hSolicitacao.setDescricao(mud.toString());
		}
		if (antiga.isAviso() != nova.isAviso()) {

			mud.append("O aviso ao munícipe foi mudado de " + "<b>" + antiga.condicao() + "</b>" + " para " + "<b>"
					+ nova.condicao() + "</b>" + ";");
			hSolicitacao.setDescricao(mud.toString());
		}

		if (!antiga.getDescricao().equals(nova.getDescricao())) {

			mud.append("A descrição da solução foi alterada");
			hSolicitacao.setDescricao(mud.toString());
		}
		if (hSolicitacao.getDescricao() != null) {
			
			hSolicitacao.setData(data);
			hSolicitacao.setHora(hora);
			hSolicitacao.setSolicitacao(solicitacao);
			hSolicitacao.setUsuario(usuario.getNome());
			hSolicitacao.setTipo(HistoricoTipo.SOLUCAO_EDIT);
			return hSolicitacao;
		}
		return null;
	}

	public HistoricoMunicipe edicaoDadosMunicipe(Usuario usuario, @Valid Cliente novo, Cliente antigo, Bairro bairro,
			List<Telefone> telefones) {
		StringBuilder mud = new StringBuilder();

		mud.append("Os dados do munícipe foram alterados pelo usuário: " + "<b>" + usuario.getNome() + "</b>" + ";");

		if (!antigo.getNome().equals(novo.getNome())) {
			mud.append("O nome foi mudado de " + "<b>" + antigo.getNome() + "</b>" + " para " + "<b>" + novo.getNome()
					+ "</b>" + ";");
			historico.setDescricao(mud.toString());
		}
		if (!antigo.getEmail().equals(novo.getEmail())) {
			mud.append("O email foi mudado de " + "<b>" + antigo.getEmail() + "</b>" + " para " + "<b>"
					+ novo.getEmail() + "</b>" + ";");
			historico.setDescricao(mud.toString());
		}
		if (!antigo.getRg().equals(novo.getRg())) {
			if (antigo.getRg().isEmpty()) {
				mud.append("Um novo RG foi adicionado " + ";");
			} else {
				mud.append("O RG foi mudado de " + "<b>" + antigo.getRg() + "</b>" + " para " + "<b>" + novo.getRg()
						+ "</b>" + ";");
			}
			historico.setDescricao(mud.toString());
		}
		if (!antigo.getDataNascimento().equals(novo.getDataNascimento())) {
			mud.append("A data de nascimento foi mudada de " + "<b>"
					+ antigo.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</b>"
					+ " para " + "<b>"
					+ novo.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</b>"
					+ ";");
			historico.setDescricao(mud.toString());
		}
		if (!antigo.getEndereco().getBairro().getDescricao().equals(bairro.getDescricao())) {
			mud.append("O bairro foi mudado de " + "<b>" + antigo.getEndereco().getBairro().getDescricao() + "</b>"
					+ " para " + "<b>" + bairro.getDescricao() + "</b>" + ";");
			historico.setDescricao(mud.toString());
		}
		if (!antigo.getEndereco().getLogradouro().equals(novo.getEndereco().getLogradouro())) {
			mud.append("O endereço foi mudado de " + "<b>" + antigo.getEndereco().getLogradouro() + "</b>" + " para "
					+ "<b>" + novo.getEndereco().getLogradouro() + "</b>" + ";");
			historico.setDescricao(mud.toString());
		}
		if (!antigo.getEndereco().getCidade().equals(novo.getEndereco().getCidade())) {
			mud.append("A cidade foi mudada de " + "<b>" + antigo.getEndereco().getCidade() + "</b>" + " para " + "<b>"
					+ novo.getEndereco().getCidade() + "</b>" + ";");
			historico.setDescricao(mud.toString());
		}
		if (!antigo.getEndereco().getNumero().equals(novo.getEndereco().getNumero())) {
			mud.append("O número do endereço foi mudado de " + "<b>" + antigo.getEndereco().getNumero() + "</b>"
					+ " para " + "<b>" + novo.getEndereco().getNumero() + "</b>" + ";");
			historico.setDescricao(mud.toString());
		}
		if (!antigo.getEndereco().getCep().equals(novo.getEndereco().getCep())) {
			mud.append("O CEP foi mudado de " + "<b>" + antigo.getEndereco().getCep() + "</b>" + " para " + "<b>"
					+ novo.getEndereco().getCep() + "</b>" + ";");
			historico.setDescricao(mud.toString());
		}
		if (!antigo.getEndereco().getComplemento().equals(novo.getEndereco().getComplemento())) {
			if (antigo.getEndereco().getComplemento().isEmpty()) {
				mud.append("Um novo complemento foi adicionado" + ";");
			} else {
				mud.append("O endereço foi mudado de " + "<b>" + antigo.getEndereco().getComplemento() + "</b>"
						+ " para " + "<b>" + novo.getEndereco().getComplemento() + "</b>" + ";");
			}

			historico.setDescricao(mud.toString());
		}
		if (!antigo.getEndereco().getUf().getSigla().equals(novo.getEndereco().getUf().getSigla())) {
			mud.append("O endereço foi mudado de " + "<b>" + antigo.getEndereco().getUf().getSigla() + "</b>" + " para "
					+ "<b>" + novo.getEndereco().getUf().getSigla() + "</b>" + ";");
			historico.setDescricao(mud.toString());
		}

		if (novo.getTelefones().size() > telefones.size()) {

			List<Telefone> novos = new ArrayList<>(novo.getTelefones());
			novos.removeAll(telefones);

			for (Telefone t : novos) {
				mud.append("O telefone com número: " + "<b>" + t.getNumero() + "</b>" + " foi adicionado" + ";");
				historico.setDescricao(mud.toString());
			}

			List<Telefone> diferentes = new ArrayList<>(novo.getTelefones());
			diferentes.removeIf(e -> e.hasNotId());

			Javers javers = JaversBuilder.javers().build();
			Diff diff = javers.compareCollections(telefones, diferentes, Telefone.class);

			List<ValueChange> valueChange = diff.getChangesByType(ValueChange.class);
			if (!valueChange.isEmpty()) {
				for (ValueChange v : valueChange) {
					if (v.getPropertyName().equalsIgnoreCase("numero")) {
						mud.append("O número " + "<b>" + v.getLeft() + "</b>" + " foi mudado para " + "<b>"
								+ v.getRight() + "</b>" + ";");
					}
					historico.setDescricao(mud.toString());
				}
			}
		}

		if (novo.getTelefones().equals(telefones)) {
			Javers javers = JaversBuilder.javers().build();
			Diff diff = javers.compareCollections(telefones, novo.getTelefones(), Telefone.class);

			List<ValueChange> valueChange = diff.getChangesByType(ValueChange.class);
			if (!valueChange.isEmpty()) {
				for (ValueChange v : valueChange) {
					if (v.getPropertyName().equalsIgnoreCase("numero")) {
						mud.append("O número " + "<b>" + v.getLeft() + "</b>" + " foi mudado para " + "<b>"
								+ v.getRight() + "</b>" + ";");
					}
					historico.setDescricao(mud.toString());
				}
			}

		}

		if (historico.getDescricao() != null) {
			historico.setData(data);
			historico.setHora(hora);
			historico.setCliente(novo);
			historico.setUsuario(usuario.getNome());
			historico.setTipo(HistoricoTipo.CLIENTE_EDIT);
			return historico;
		}
		return null;
	}

}
