package br.com.solicitacoes.solicitacoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SolicitacoesApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SolicitacoesApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("Gekkomoria@666"));

	}	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SolicitacoesApplication.class);
	}

}
