package com.petlogistica.repository;

import com.petlogistica.model.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemRepository extends JpaRepository<Imagem, Integer> {
}
