package br.com.solicitacoes.solicitacoes.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import br.com.solicitacoes.solicitacoes.domain.Cliente;
import br.com.solicitacoes.solicitacoes.domain.Solicitacao;
import br.com.solicitacoes.solicitacoes.domain.Telefone;

@Service
public class ExcelService {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
//	private List<Cliente> clientes;
//	private List<Solicitacao> solicitacoes;

	public ExcelService() {
		// this.clientes = clientes;
		workbook = new XSSFWorkbook();
	}

//	public ExcelService(List<Solicitacao> solicitacoes) {
//		this.solicitacoes = solicitacoes;
//		workbook = new XSSFWorkbook();
//	}

	private void writeHeaderLine(List<Cliente> listUsers) {
		sheet = workbook.createSheet("Users");

		Row row = sheet.createRow(0);

		CellStyle style = formatacaoHeader();

		createCell(row, 0, "Nome", style);
		createCell(row, 1, "E-mail", style);
		createCell(row, 2, "Data de Nascimento", style);
		createCell(row, 3, "RG", style);
		createCell(row, 4, "Telefones", style);
		createCell(row, 5, "Endereço", style);
		createCell(row, 6, "Apoiador?", style);
		createCell(row, 7, "Descrição do Apoio", style);

	}

	private void writeHeaderLineSolicitacoes(List<Solicitacao> listUsers) {
		sheet = workbook.createSheet("Solicitações");

		Row row = sheet.createRow(0);

		CellStyle style = formatacaoHeader();

		createCell(row, 0, "Id", style);
		createCell(row, 1, "Munícipe", style);
		createCell(row, 2, "Bairro", style);
		createCell(row, 3, "Assunto", style);
		createCell(row, 4, "Indicada?", style);
		createCell(row, 5, "Status", style);
		createCell(row, 6, "Cadastrada por:", style);
		createCell(row, 7, "Data da Solicitação", style);
		createCell(row, 8, "Horário da Solicitação", style);
		createCell(row, 9, "Munícipe comunidado?", style);
		createCell(row, 10, "Resultado", style);
		createCell(row, 11, "Solução Cadastrada por:", style);
		createCell(row, 12, "Data da Solução", style);
		createCell(row, 13, "Horário da Solução", style);

	}

	private CellStyle formatacaoHeader() {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);
		return style;
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);

		} else if (value instanceof LocalDate) {
			cell.setCellValue(value instanceof LocalDate);
		} else if (value instanceof LocalTime) {
			cell.setCellValue(value instanceof LocalTime);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines(List<Cliente> clientes) {
		int rowCount = 1;

		CellStyle style = formatacaoLinhas();

		for (Cliente c : clientes) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			String data = c.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String endereco = c.getEndereco().getLogradouro() + " Nº" + c.getEndereco().getNumero() + ", "
					+ c.getEndereco().getBairro().getDescricao() + " - " + c.getEndereco().getCidade() + " /"
					+ c.getEndereco().getUf();
			List<String> tels = new ArrayList<>();

			for (Telefone t : c.getTelefones()) {
				tels.add(t.getNumero());
			}

			createCell(row, columnCount++, c.getNome(), style);
			createCell(row, columnCount++, c.getEmail(), style);
			createCell(row, columnCount++, data, style);

			if (c.getRg().isEmpty()) {
				createCell(row, columnCount++, " - ", style);
			} else {
				createCell(row, columnCount++, c.getRg(), style);
			}
			createCell(row, columnCount++, tels.toString().replace("[", "").replace("]", ""), style);
			createCell(row, columnCount++, endereco, style);

			if (c.isApoiador()) {
				createCell(row, columnCount++, "Sim", style);
			} else {
				createCell(row, columnCount++, "Não", style);
			}

			createCell(row, columnCount++, c.getApoiador_desc(), style);

		}
	}

	private void writeDataLinesSolicitacoes(List<Solicitacao> solicitacoes) {
		int rowCount = 1;

		CellStyle style = formatacaoLinhas();

		for (Solicitacao s : solicitacoes) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			String data = s.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String horario = s.getHora().format(DateTimeFormatter.ofPattern("HH:mm"));

			createCell(row, columnCount++, s.getId(), style);
			createCell(row, columnCount++, s.getCliente().getNome(), style);
			createCell(row, columnCount++, s.getBairro().getDescricao(), style);
			createCell(row, columnCount++, s.getAssunto().getDescricao(), style);

			if (s.isIndicado()) {
				createCell(row, columnCount++, "Sim", style);
			} else {
				createCell(row, columnCount++, "Não", style);
			}

			switch (s.getStatus().toString()) {
			case "ABERTO":
				createCell(row, columnCount++, "Aberto", style);
				break;
			case "PENDENTE":
				createCell(row, columnCount++, "Pendente", style);
				break;
			case "FINALIZADO":
				createCell(row, columnCount++, "Finalizado", style);
				break;
			case "ATRASADO":
				createCell(row, columnCount++, "Atrasado", style);
				break;

			}
			createCell(row, columnCount++, s.getUsuario(), style);
			createCell(row, columnCount++, data, style);
			createCell(row, columnCount++, horario, style);

			if (s.getSolucao() != null) {
				if(s.getSolucao().isAviso()) {
					createCell(row, columnCount++, "Sim", style);
				}else {
					createCell(row, columnCount++, "Não", style);
				}
				createCell(row, columnCount++, s.getSolucao().getResultado(), style);
				createCell(row, columnCount++, s.getSolucao().getUsuario(), style);
				createCell(row, columnCount++,s.getSolucao().getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), style);
				createCell(row, columnCount++, s.getSolucao().getHora().format(DateTimeFormatter.ofPattern("HH:mm")), style);

			} else {
				createCell(row, columnCount++, " - ", style);
				createCell(row, columnCount++, " - ", style);
				createCell(row, columnCount++, " - ", style);
				createCell(row, columnCount++, " - ", style);
				createCell(row, columnCount++, " - ", style);
			}

		}
	}

	private CellStyle formatacaoLinhas() {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		return style;
	}

	public void export(HttpServletResponse response, List<Cliente> listUsers) throws IOException {
		writeHeaderLine(listUsers);
		writeDataLines(listUsers);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

	public void exportSolicitacao(HttpServletResponse response, List<Solicitacao> listUsers) throws IOException {
		writeHeaderLineSolicitacoes(listUsers);
		writeDataLinesSolicitacoes(listUsers);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

}
