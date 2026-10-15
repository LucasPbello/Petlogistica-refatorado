package com.petlogistica.repository;

import com.petlogistica.model.Documento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository
        extends JpaRepository<Documento, Integer> {

    // Documentos pessoais do tutor
    List<Documento> findByCliente_IdCliente(
            Integer idCliente);

    // Documentos de um animal
    List<Documento> findByAnimal_IdAnimal(
            Integer idAnimal);
}
