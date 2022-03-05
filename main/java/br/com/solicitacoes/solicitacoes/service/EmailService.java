package br.com.solicitacoes.solicitacoes.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import br.com.solicitacoes.solicitacoes.domain.Cliente;


@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private SpringTemplateEngine template;
	
	
	
	public void enviarPedidoRededinicaoSenha(String destino, String verificador) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = 
				new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
		Context context = new Context();
		context.setVariable("titulo", "Redefinição de senha");
		context.setVariable("texto", "Para redefinir sua senha use o código de verificação quando exigido no formulário:");
		context.setVariable("verificador", verificador);
		
		String html = template.process("email/confirmacao", context);
		helper.setTo(destino);
		helper.setText(html, true);
		helper.setSubject("Redefinição de senha");
		helper.setFrom("nao-responder@clinica.com.br");
		
		
		
		mailSender.send(message); 
	}

	public void enviarEmailsPersonalizados(List<String> emails, String assunto, String corpo) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = 
				new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
		Context context = new Context();
		context.setVariable("titulo", assunto);
		context.setVariable("texto", corpo);
		
		String[] array = emails.toArray(new String[emails.size()]);
		
		String html = template.process("email/personalizado", context);
		helper.setTo(array);	
		helper.setText(html, true);
		helper.setSubject(assunto);
		helper.setFrom("nao-responder@clinica.com.br");
		
		
		mailSender.send(message); 
		
	}

	public void enviarEmailsPersonalizadosComNome(Cliente[] clientes, String assunto, String corpo,
			MultipartFile[] files) throws MessagingException, UnsupportedEncodingException {
		
		
		for(Cliente c : clientes) {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = 
					new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
			Context context = new Context();
//			context.setVariable("titulo", assunto);
			context.setVariable("texto", corpo);
			context.setVariable("nome", c.getNome());
			
			if(files.length >= 1) {
				for(MultipartFile file : files) {
					helper.addAttachment(file.getOriginalFilename(), new InputStreamSource() {
						
						@Override
						public InputStream getInputStream() throws IOException {					
							return file.getInputStream();
						}
					});
				}
			}
			
			String html = template.process("email/personalizado", context);
			helper.setTo(c.getEmail());	
			helper.setText(html, true);
			helper.setSubject(assunto);
			helper.setFrom("nao-responder@clinica.com.br");
			
			
			mailSender.send(message); 
		}
		
		
		
	}
	
	public void enviarEmailsPersonalizadosSemImagem(Cliente[] clientes, String assunto, String corpo
			) throws MessagingException, UnsupportedEncodingException {
		
		
		for(Cliente c : clientes) {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = 
					new MimeMessageHelper(message, true, "UTF-8");
			Context context = new Context();

			context.setVariable("texto", corpo);
			context.setVariable("nome", c.getNome());
			
			
			String html = template.process("email/personalizado", context);
			helper.setTo(c.getEmail());	
			helper.setText(html, true);
			helper.setSubject(assunto);
			helper.setFrom("nao-responder@clinica.com.br");
			
			
			mailSender.send(message); 
		}
		
		
		
	}

	
}
