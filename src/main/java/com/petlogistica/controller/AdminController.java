package com.petlogistica.controller;

import com.petlogistica.exception.RecursoNaoEncontradoException;
import com.petlogistica.model.Animal;
import com.petlogistica.model.Cliente;
import com.petlogistica.service.AnimalService;
import com.petlogistica.service.ClienteService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.petlogistica.service.UsuarioService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ClienteService clienteService;
    private final AnimalService animalService;
    private final UsuarioService usuarioService;

    public AdminController(
            ClienteService clienteService,
            AnimalService animalService,
            UsuarioService usuarioService) {

        this.clienteService = clienteService;
        this.animalService = animalService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String painelAdmin() {
        return "admin";
    }

    // LISTAR TUTORES
    @GetMapping("/tutores")
    public String listarTutores(Model model) {
        model.addAttribute("tutores", clienteService.listarTodos());
        return "admin-tutores";
    }

    @GetMapping("/tutores/novo")
    public String novoTutor() {
        return "admin-tutor-cadastro";
    }

    @PostMapping("/tutores/novo")
    public String cadastrarTutor(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String confirmarSenha,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não coincidem.");
            model.addAttribute("nome", nome);
            model.addAttribute("email", email);
            return "admin-tutor-cadastro";
        }

        try {
            usuarioService.cadastrarTutor(nome, email, senha);

            redirectAttributes.addFlashAttribute(
                    "sucesso", "Tutor cadastrado com sucesso!");

            return "redirect:/admin/tutores";

        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("nome", nome);
            model.addAttribute("email", email);
            return "admin-tutor-cadastro";
        }
    }

    // ABRIR FORMULÁRIO DE EDIÇÃO DO TUTOR
    @GetMapping("/tutores/{id}/editar")
    public String editarTutor(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute("tutor", clienteService.buscarPorId(id));
        return "admin-tutor-form";
    }

    // SALVAR ALTERAÇÕES DO TUTOR
    @PostMapping("/tutores/{id}/editar")
    public String atualizarTutor(
            @PathVariable Integer id,
            @ModelAttribute("tutor") Cliente dados,
            RedirectAttributes redirectAttributes) {

        try {
            clienteService.atualizarDadosTutor(
                    id,
                    dados.getNome(),
                    dados.getEmail(),
                    dados.getCpf(),
                    dados.getTelefone()
            );

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Tutor atualizado com sucesso!"
            );

            return "redirect:/admin/tutores";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );

            return "redirect:/admin/tutores/" + id + "/editar";
        }
    }

    // CONSULTAR ANIMAIS DE UM TUTOR
    @GetMapping("/tutores/{id}/animais")
    public String listarAnimaisDoTutor(
            @PathVariable Integer id,
            Model model) {

        Cliente tutor = clienteService.buscarPorId(id);

        model.addAttribute("tutor", tutor);
        model.addAttribute(
                "animais",
                animalService.listarPorCliente(id));

        return "admin-tutor-animais";
    }

    // LISTAR TODOS OS ANIMAIS
    @GetMapping("/animais")
    public String listarAnimais(Model model) {
        model.addAttribute("animais", animalService.listarTodos());
        return "admin-animais";
    }

    // FORMULÁRIO PARA CADASTRAR ANIMAL
    @GetMapping("/animais/novo")
    public String novoAnimal(Model model) {
        model.addAttribute("animal", new Animal());
        model.addAttribute("novo", true);
        model.addAttribute("tutores", clienteService.listarTodos());
        return "admin-animal-form";
    }

    // CADASTRAR ANIMAL
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

    // FORMULÁRIO PARA EDITAR ANIMAL
    @GetMapping("/animais/{id}/editar")
    public String editarAnimal(
            @PathVariable int id,
            Model model) {

        model.addAttribute("animal", animalService.buscarPorId(id));
        model.addAttribute("novo", false);
        model.addAttribute("tutores", clienteService.listarTodos());

        return "admin-animal-form";
    }

    // ATUALIZAR ANIMAL
    @PostMapping("/animais/{id}/editar")
    public String atualizarAnimal(
            @PathVariable int id,
            @ModelAttribute Animal animal,
            @RequestParam(required = false) Integer idCliente,
            RedirectAttributes redirectAttributes) {

        if (idCliente != null) {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(idCliente);
            animal.setCliente(cliente);
        }

        animalService.atualizar(id, animal)
                .orElseThrow(() -> new IllegalArgumentException(
                "Animal não encontrado: " + id));

        redirectAttributes.addFlashAttribute(
                "sucesso", "Animal atualizado com sucesso!");

        return "redirect:/admin/animais";
    }

    // EXCLUIR ANIMAL
    @PostMapping("/animais/{id}/excluir")
    public String excluirAnimal(
            @PathVariable int id,
            RedirectAttributes redirectAttributes) {

        try {
            animalService.excluir(id);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Pet e seus arquivos excluídos com sucesso!");

        } catch (RecursoNaoEncontradoException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage());
        }

        return "redirect:/admin/animais";
    }

    @PostMapping("/tutores/{id}/excluir")
    public String excluirTutor(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            clienteService.excluir(id);

            redirectAttributes.addFlashAttribute(
                    "sucesso", "Tutor excluído com sucesso!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "erro", e.getMessage());
        }

        return "redirect:/admin/tutores";
    }
}
