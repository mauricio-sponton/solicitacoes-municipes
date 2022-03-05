package br.com.solicitacoes.solicitacoes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.solicitacoes.solicitacoes.domain.PerfilTipo;
import br.com.solicitacoes.solicitacoes.domain.Usuario;
import br.com.solicitacoes.solicitacoes.service.UsuarioService;

@Controller
public class ParentController {

	@Autowired
	private UsuarioService usuarioService;

	@ModelAttribute("autoridade")
	public String getAutoridade(@AuthenticationPrincipal User user) {
		if (user != null) {
			if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.ADMIN.getDesc()))) {
				return "autoridade";
			}
			if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.USUARIO.getDesc()))) {
				return "usuario";
			}
		}
		return null;
	}

	@ModelAttribute("nome_usuario")
	public String getNomeUsuario(@AuthenticationPrincipal User user) {
		if (user != null) {
			Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
			return usuario.getNome();
		} else {
			return "vazio";
		}

	}
}
