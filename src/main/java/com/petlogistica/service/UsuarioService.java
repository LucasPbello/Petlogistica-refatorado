package com.petlogistica.service;

import com.petlogistica.model.Cliente;
import com.petlogistica.model.Usuario;
import com.petlogistica.repository.ClienteRepository;
import com.petlogistica.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método temporário para manter os cadastros antigos funcionando
    @Transactional
    public Usuario cadastrarTutor(
            String nome, String email, String senha) {

        return cadastrarTutor(nome, null, email, senha);
    }

    // Novo cadastro com nome de usuário
    @Transactional
    public Usuario cadastrarTutor(
            String nome,
            String nomeUsuario,
            String email,
            String senha) {

        if (nome == null || nome.isBlank()
                || email == null || email.isBlank()
                || senha == null || senha.length() < 8) {

            throw new IllegalArgumentException(
                    "Informe nome, e-mail e senha com pelo menos 8 caracteres.");
        }

        String nomeNormalizado = nome.trim();
        String emailNormalizado = email.trim().toLowerCase();

        String usuarioNormalizado = null;

        if (nomeUsuario != null && !nomeUsuario.isBlank()) {

            usuarioNormalizado = nomeUsuario.trim().toLowerCase();

            if (!usuarioNormalizado.matches("[a-z0-9._]{4,20}")) {
                throw new IllegalArgumentException(
                        "O nome de usuário deve ter de 4 a 20 caracteres, "
                        + "utilizando letras, números, pontos ou underline.");
            }

            if (usuarioRepository.existsByNomeUsuarioIgnoreCase(
                    usuarioNormalizado)) {

                throw new IllegalArgumentException(
                        "Este nome de usuário já está em uso.");
            }
        }

        if (usuarioRepository.existsByEmailIgnoreCase(emailNormalizado)
                || clienteRepository.existsByEmailIgnoreCase(emailNormalizado)) {

            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado.");
        }

        // Impede ambiguidades entre nomes de usuário e e-mails
        if (usuarioNormalizado != null
                && usuarioRepository.existsByEmailIgnoreCase(
                        usuarioNormalizado)) {

            throw new IllegalArgumentException(
                    "Este nome de usuário não está disponível.");
        }

        if (usuarioRepository.existsByNomeUsuarioIgnoreCase(
                emailNormalizado)) {

            throw new IllegalArgumentException(
                    "Este e-mail não está disponível.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(nomeNormalizado);
        cliente.setEmail(emailNormalizado);

        Cliente clienteSalvo = clienteRepository.save(cliente);

        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(usuarioNormalizado);
        usuario.setEmail(emailNormalizado);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfil("TUTOR");
        usuario.setCliente(clienteSalvo);

        return usuarioRepository.save(usuario);
    }
}
