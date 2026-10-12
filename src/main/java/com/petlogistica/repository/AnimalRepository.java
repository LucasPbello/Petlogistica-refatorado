package com.petlogistica.repository;

import com.petlogistica.model.Animal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    List<Animal> findByCliente_IdCliente(Integer idCliente);
}
