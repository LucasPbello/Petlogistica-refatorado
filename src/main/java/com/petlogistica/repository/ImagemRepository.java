package com.petlogistica.repository;

import com.petlogistica.model.Imagem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemRepository
        extends JpaRepository<Imagem, Integer> {

    // Fotos pessoais do tutor
    List<Imagem> findByCliente_IdCliente(
            Integer idCliente);

    // Fotos de um animal
    List<Imagem> findByAnimal_IdAnimal(
            Integer idAnimal);
}
