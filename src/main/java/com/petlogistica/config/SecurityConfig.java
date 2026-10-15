package com.petlogistica.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                // Páginas e arquivos públicos
                .requestMatchers(
                        "/",
                        "/login",
                        "/cadastro",
                        "/css/**",
                        "/js/**"
                ).permitAll()
                // APIs existentes: somente administrador
                // APIs existentes: somente administrador
                .requestMatchers(
                        "/documentos",
                        "/documentos/**",
                        "/imagens",
                        "/imagens/**",
                        "/animais",
                        "/animais/**",
                        "/clientes",
                        "/clientes/**"
                ).hasRole("ADMIN")
                // Área administrativa
                .requestMatchers("/admin", "/admin/**")
                .hasRole("ADMIN")
                // Área do tutor
                .requestMatchers("/tutor", "/tutor/**")
                .hasRole("TUTOR")
                // Outras rotas exigem autenticação
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(this::redirecionarPorPerfil)
                .permitAll()
                )
                .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                );

        return http.build();
    }

    private void redirecionarPorPerfil(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        boolean administrador = authentication.getAuthorities()
                .stream()
                .anyMatch(autoridade
                        -> autoridade.getAuthority()
                        .equals("ROLE_ADMIN"));

        if (administrador) {
            response.sendRedirect(
                    request.getContextPath() + "/admin");
        } else {
            response.sendRedirect(
                    request.getContextPath() + "/tutor");
        }
    }
}
