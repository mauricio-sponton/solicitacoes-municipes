package br.com.solicitacoes.solicitacoes.datatables;

public class DatatablesColunas {
	
	public static final String[] USUARIOS = {"email", "ativo", "perfis"};
	public static final String[] ASSUNTOS = {"id","descricao", "ativo"};
	public static final String[] CLIENTES = {"nome", "email", "telefones"};
	public static final String[] SOLICITACOES = {"id","cliente.nome", "bairro.descricao", "assunto.descricao", "usuario", "status", "data", "solucao.aviso"};
	public static final String[] BAIRROS = {"descricao", "ativo"};
	public static final String[] REMETENTES = {"nome", "logradouro"};
	public static final String[] REUNIOES = {"assunto", "descricao", "data"};
	public static final String[] INDICACOES = {"assunto", "descricao", "data", "sessao"};
	public static final String[] REQUERIMENTOS = {"assunto", "descricao", "data", "sessao"};
	public static final String[] OFICIOS = {"assunto", "descricao", "data"};
	public static final String[] LEIS ={"assunto", "descricao", "data", "status"};;	
	
}
