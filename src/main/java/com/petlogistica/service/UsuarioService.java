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

    @Transactional
    public Usuario cadastrarTutor(
            String nome, String email, String senha) {

        if (nome == null || nome.isBlank()
                || email == null || email.isBlank()
                || senha == null || senha.length() < 8) {
            throw new IllegalArgumentException(
                    "Informe nome, e-mail e senha com pelo menos 8 caracteres.");
        }

        String nomeNormalizado = nome.trim();
        String emailNormalizado = email.trim().toLowerCase();

        if (usuarioRepository.existsByEmailIgnoreCase(emailNormalizado)
                || clienteRepository.existsByEmailIgnoreCase(emailNormalizado)) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(nomeNormalizado);
        cliente.setEmail(emailNormalizado);

        Cliente clienteSalvo = clienteRepository.save(cliente);

        Usuario usuario = new Usuario();
        usuario.setEmail(emailNormalizado);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfil("TUTOR");
        usuario.setCliente(clienteSalvo);

        return usuarioRepository.save(usuario);
    }
}




