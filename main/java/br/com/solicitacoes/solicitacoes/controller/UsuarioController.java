package br.com.solicitacoes.solicitacoes.controller;

import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.solicitacoes.solicitacoes.domain.Perfil;
import br.com.solicitacoes.solicitacoes.domain.Usuario;
import br.com.solicitacoes.solicitacoes.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController extends ParentController{

	@Autowired
	private UsuarioService service;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/listar")
	public String listar(Usuario usuario) {
		return "usuario/lista";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr,
			ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "Por favor preencha os campos");
			return "usuario/lista";
		}

		List<Perfil> perfis = usuario.getPerfis();
		System.out.println(perfis);
		if (perfis.size() > 1) {
			attr.addFlashAttribute("falha",
					"Não é possível cadastrar um usuário com mais de 1 perfil");
			attr.addFlashAttribute("usuario", usuario);
		} else {
			try {
				if(usuario.hasNotId()) {
					service.salvarUsuario(usuario);
				}else {
					service.salvarUsuarioCadastrado(usuario);
				}
				
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso");
			} catch (DataIntegrityViolationException ex) {
				attr.addFlashAttribute("falha", "Cadastro não realizado pois o email já existe");
			}
		}

		return "redirect:/u/listar";
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/cadastro/salvar/senhas")
	public String salvarUsuariosSenhas(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr,
			ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "Por favor preencha os campos");
			return "usuario/lista";
		}

		List<Perfil> perfis = usuario.getPerfis();
		System.out.println(perfis);
		if (perfis.size() > 1) {
			attr.addFlashAttribute("falha",
					"Não é possível cadastrar um usuário com mais de 1 perfil");
			attr.addFlashAttribute("usuario", usuario);
		} else {
			try {
					service.salvarUsuario(usuario);
				
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso");
			} catch (DataIntegrityViolationException ex) {
				attr.addFlashAttribute("falha", "Cadastro não realizado pois o email já existe");
			}
		}

		return "redirect:/u/listar";
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/editar/credenciais/usuario/{id}")
	public String preEditarCredenciais(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("control", "credenciais");
		model.addAttribute("usuario", service.buscarPorId(id));
		return "usuario/lista";
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/editar/senha/usuario/{id}")
	public String preEditarSenhas(@PathVariable("id") Long id, ModelMap model) {
		Usuario u = service.buscarPorId(id);
		model.addAttribute("control", "senhas");
		model.addAttribute("perfil", u.getPerfis());
		model.addAttribute("usuario", service.buscarPorId(id));
		return "usuario/lista";
	}

	@GetMapping("/editar/senha")
	public String abrirEditarSenha() {
		return "usuario/editar-senha";
	}

	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2,
			@RequestParam("senha3") String s3, @AuthenticationPrincipal User user, RedirectAttributes attr) {
		if (!s1.equals(s2)) {
			attr.addFlashAttribute("falha", "Senhas não conferem, tente novamente");
			return "redirect:/u/editar/senha";
		}
		if(!s1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,}$")) {
			attr.addFlashAttribute("falha", "A senha não se adequa aos requisitos, tente novamente");
			return "redirect:/u/editar/senha";
		}
		Usuario u = service.buscarPorEmail(user.getUsername());
		if (!UsuarioService.isSenhaCorreta(s3, u.getSenha())) {
			attr.addFlashAttribute("falha", "Senha atual não confere, tente novamente");
			return "redirect:/u/editar/senha";
		}
		service.alterarSenha(u, s1);
		attr.addFlashAttribute("sucesso", "Senha alterada com sucesso");
		return "redirect:/u/editar/senha";
	}
	@GetMapping("/p/redefinir/senha")
	public String pedidoRedefinirSenha(ModelMap model) {
		return "usuario/pedido-recuperar-senha";
	}
	// form de pedido de recuperar senha
	@GetMapping("/p/recuperar/senha")
	public String redefinirSenha(String email, ModelMap model) throws MessagingException {
		if(service.pedidoRedefinicaoDeSenha(email)) {
			model.addAttribute("sucesso",
					"Em instantes você receberá um email para prosseguir com a redefinição de sua senha.");
			model.addAttribute("usuario", new Usuario(email));
		}else {
			model.addAttribute("falha",
					"Usuário não encontrado!");
		}
		
		return "usuario/recuperar-senha";
	}

	// salvar nova senha via recuperacao de senha
	@PostMapping("/p/nova/senha")
	public String confirmacaoRedefinicaoSenha(Usuario usuario, ModelMap model, RedirectAttributes attr) {
		Usuario u = service.buscarPorEmail(usuario.getEmail());
		if (!usuario.getCodigoVerificador().equals(u.getCodigoVerificador())) {
			model.addAttribute("falha", "Código verificador não confere.");
			return "usuario/recuperar-senha";
		}
		u.setCodigoVerificador(null);
		service.alterarSenha(u, usuario.getSenha());
		attr.addFlashAttribute("sucesso", "Senha redefinida! Você já pode logar no sistema.");
		
		return "redirect:/login";
	}
}
