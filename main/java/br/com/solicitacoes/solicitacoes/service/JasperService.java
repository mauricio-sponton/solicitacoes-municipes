package br.com.solicitacoes.solicitacoes.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.metadata.HikariDataSourcePoolMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.zaxxer.hikari.HikariDataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class JasperService {

	private static final String JASPER_DIRETORIO = "classpath:jasper/";
	private static final String JASPER_PREFIXO = "relatorios-";
	private static final String JASPER_SUFIXO = ".jasper";
	
//	@Autowired
//	private Connection connection;
	
	@Autowired
	private DataSource dataSource;
	
	private Map<String, Object> params = new HashMap<>();
	
	public JasperService() {
		this.params.put("IMAGEM_DIRETORIO", JASPER_DIRETORIO);
	}

	
	public void addParams(String key, Object value) {
		this.params.put(key, value);
	}
	
	@Transactional(readOnly = true)
	public byte[] exportarPDF(String code) throws SQLException {
		byte[] bytes = null;
		try (Connection connection = dataSource.getConnection()){
			
			File file = ResourceUtils.getFile(JASPER_DIRETORIO.concat(JASPER_PREFIXO).concat(code).concat(JASPER_SUFIXO));
			JasperPrint print = JasperFillManager.fillReport(file.getAbsolutePath(), params, connection);
			bytes = JasperExportManager.exportReportToPdf(print);
			
			Integer test = new HikariDataSourcePoolMetadata((HikariDataSource) dataSource).getActive();
			System.out.println("Conexoes ativas = " + test);
		} catch (FileNotFoundException | JRException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
}
