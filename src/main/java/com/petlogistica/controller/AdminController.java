package com.petlogistica.controller;

import com.petlogistica.model.Animal;
import com.petlogistica.model.Cliente;
import com.petlogistica.service.AnimalService;
import com.petlogistica.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ClienteService clienteService;
    private final AnimalService animalService;

    public AdminController(
            ClienteService clienteService,
            AnimalService animalService) {
        this.clienteService = clienteService;
        this.animalService = animalService;
    }

    @GetMapping
    public String painelAdmin() {
        return "admin";
    }

    @GetMapping("/tutores")
    public String listarTutores(Model model) {
        model.addAttribute("tutores", clienteService.listarTodos());
        return "admin-tutores";
    }

    @GetMapping("/animais")
    public String listarAnimais(Model model) {
        model.addAttribute("animais", animalService.listarTodos());
        return "admin-animais";
    }

    @GetMapping("/animais/novo")
    public String novoAnimal(Model model) {
        model.addAttribute("animal", new Animal());
        model.addAttribute("novo", true);
        model.addAttribute("tutores", clienteService.listarTodos());
        return "admin-animal-form";
    }

    @PostMapping("/animais")
    public String cadastrarAnimal(
            @ModelAttribute Animal animal,
            @RequestParam(required = false) Integer idCliente,
            RedirectAttributes redirectAttributes) {

        if (idCliente != null) {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(idCliente);
            animal.setCliente(cliente);
        }

        animalService.salvar(animal);

        redirectAttributes.addFlashAttribute(
                "sucesso", "Animal cadastrado com sucesso!");

        return "redirect:/admin/animais";
    }

    @GetMapping("/animais/{id}/editar")
    public String editarAnimal(
            @PathVariable int id,
            Model model) {

        model.addAttribute("animal", animalService.buscarPorId(id));
        model.addAttribute("novo", false);

        return "admin-animal-form";
    }

    @PostMapping("/animais/{id}/editar")
    public String atualizarAnimal(
            @PathVariable int id,
            @ModelAttribute Animal animal,
            RedirectAttributes redirectAttributes) {

        animalService.atualizar(id, animal)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Animal não encontrado: " + id));

        redirectAttributes.addFlashAttribute(
                "sucesso", "Animal atualizado com sucesso!");

        return "redirect:/admin/animais";
    }

    @PostMapping("/animais/{id}/excluir")
    public String excluirAnimal(
            @PathVariable int id,
            RedirectAttributes redirectAttributes) {

        animalService.excluir(id);

        redirectAttributes.addFlashAttribute(
                "sucesso", "Animal excluído com sucesso!");

        return "redirect:/admin/animais";
    }
}
