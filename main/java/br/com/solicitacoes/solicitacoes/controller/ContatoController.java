package br.com.solicitacoes.solicitacoes.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.solicitacoes.solicitacoes.domain.Cliente;
import br.com.solicitacoes.solicitacoes.service.ClienteService;
import br.com.solicitacoes.solicitacoes.service.EmailService;

@RequestMapping("contatos")
@Controller
public class ContatoController extends ParentController{
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;

	@GetMapping("/mensagens")
	public String mensagens() {
		
		return "contato/mensagens";
	}
	
	@PostMapping("/emails")
	public String enviarEmails(ModelMap model, @RequestParam(required = false, value="enviar_emails[]")  Cliente[] clientes,
			@RequestParam("assunto")  String assunto,
			@RequestParam("corpo") String corpo, RedirectAttributes attr,
			@RequestParam(value = "files", required = false) MultipartFile[] files)
			throws MessagingException, UnsupportedEncodingException {
		
		if(assunto.isEmpty() || corpo.isEmpty() || clientes.length <= 0) {
			model.addAttribute("erro", "Preencha os campos!");
			return "contato/mensagens";
		}
		
		if(files != null ) {
			emailService.enviarEmailsPersonalizadosComNome(clientes, assunto, corpo, files);
		}else {
			emailService.enviarEmailsPersonalizadosSemImagem(clientes, assunto, corpo);
		}
		
		attr.addFlashAttribute("sucesso", "Enviado!");
		return "redirect:/contatos/mensagens";
	}
	
	@ModelAttribute("emails")
	public List<Cliente> getClientes(){
		return clienteService.buscarTodosClientes();
	}
}
