package com.petlogistica.repository;

import com.petlogistica.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    Optional<Usuario> findByCliente_IdCliente(Integer idCliente);

    Optional<Usuario> findByNomeUsuarioIgnoreCase(String nomeUsuario);

    boolean existsByNomeUsuarioIgnoreCase(String nomeUsuario);

    boolean existsByEmailIgnoreCase(String email);
}
