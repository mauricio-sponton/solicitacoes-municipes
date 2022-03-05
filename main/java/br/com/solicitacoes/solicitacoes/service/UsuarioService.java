package br.com.solicitacoes.solicitacoes.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.solicitacoes.solicitacoes.datatables.Datatables;
import br.com.solicitacoes.solicitacoes.datatables.DatatablesColunas;
import br.com.solicitacoes.solicitacoes.domain.Perfil;
import br.com.solicitacoes.solicitacoes.domain.Usuario;
import br.com.solicitacoes.solicitacoes.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private Datatables datatables;
	
	@Autowired
	private EmailService emailService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmailEAtivo(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário " + username + "não encontrado."));
		return new User(usuario.getEmail(), usuario.getSenha(),
				AuthorityUtils.createAuthorityList(getAuthorities(usuario.getPerfis())));
	}

	private String[] getAuthorities(List<Perfil> perfis) {
		String[] authorities = new String[perfis.size()];
		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}
		return authorities;
	}

	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmailEAtivo(String email) {
		return repository.findByEmailEAtivo(email);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.USUARIOS);
		Page<Usuario> page = datatables.getSearch().isEmpty() ? repository.findAll(datatables.getPageable())
				: repository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return repository.findByEmail(email);
	}

	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		repository.save(usuario);
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	public static boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaArmazenada);
	}

	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		repository.save(usuario);
	}

	@Transactional(readOnly = true)
	public List<Usuario> buscarTodosUsuarios() {
		return repository.findAll();
	}

	@Transactional(readOnly = false)
	public boolean pedidoRedefinicaoDeSenha(String email) throws MessagingException {
		Usuario usuario = buscarPorEmailEAtivo(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário " + email + " não encontrado!"));
		
		String verificador = RandomStringUtils.randomAlphanumeric(6);
		usuario.setCodigoVerificador(verificador);
		emailService.enviarPedidoRededinicaoSenha(email, verificador);
		return true;

	}

	@Transactional(readOnly = false)
	public void salvarUsuarioCadastrado(@Valid Usuario usuario) {
		Usuario u = repository.findById(usuario.getId()).get();
		usuario.setSenha(u.getSenha());
		repository.save(usuario);
		
	}

}
