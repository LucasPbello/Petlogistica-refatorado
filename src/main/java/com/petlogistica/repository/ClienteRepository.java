package com.petlogistica.repository;

import com.petlogistica.model.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    boolean existsByCpf(String cpf);

    boolean existsByEmailIgnoreCase(String email);

    Optional<Cliente> findByCpf(String cpf);
}

