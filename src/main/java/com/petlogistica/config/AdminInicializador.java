package com.petlogistica.config;

import com.petlogistica.model.Usuario;
import com.petlogistica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInicializador {

    @Value("${petcargo.admin.email:}")
    private String emailAdmin;

    @Value("${petcargo.admin.password:}")
    private String senhaAdmin;

    @Bean
    public CommandLineRunner criarAdministrador(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (emailAdmin.isBlank() || senhaAdmin.isBlank()) {
                System.out.println(
                        "Administrador inicial não configurado.");
                return;
            }

            if (senhaAdmin.length() < 8) {
                throw new IllegalStateException(
                        "A senha inicial do administrador deve ter pelo menos 8 caracteres.");
            }

            String email = emailAdmin.trim().toLowerCase();

            if (usuarioRepository.existsByEmailIgnoreCase(email)) {
                System.out.println(
                        "A conta administrativa já existe; nenhum dado foi alterado.");
                return;
            }

            Usuario admin = new Usuario();
            admin.setEmail(email);
            admin.setSenha(passwordEncoder.encode(senhaAdmin));
            admin.setPerfil("ADMIN");
            admin.setCliente(null);

            usuarioRepository.save(admin);

            System.out.println(
                    "Administrador inicial criado com sucesso.");
        };
    }
}
