package com.petlogistica.controller;

import com.petlogistica.model.Animal;
import com.petlogistica.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public List<Animal> listarTodos() {
        return animalService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(animalService.buscarPorId(id));
    }

    @PostMapping
    public Animal salvar(@RequestBody Animal animal) {
        return animalService.salvar(animal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> atualizar(
            @PathVariable int id,
            @RequestBody Animal animal) {

        return animalService.atualizar(id, animal)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable int id) {
        animalService.excluir(id);
        return ResponseEntity.ok("Animal excluído com sucesso.");
    }
}
