package com.petlogistica.controller;

import com.petlogistica.model.Animal;
import com.petlogistica.model.Cliente;
import com.petlogistica.model.Usuario;
import com.petlogistica.repository.AnimalRepository;
import com.petlogistica.repository.UsuarioRepository;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tutor/animais")
public class TutorAnimalController {

    private final AnimalRepository animalRepository;
    private final UsuarioRepository usuarioRepository;

    public TutorAnimalController(
            AnimalRepository animalRepository,
            UsuarioRepository usuarioRepository) {
        this.animalRepository = animalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(
            Authentication authentication,
            Model model) {

        Usuario usuario = buscarUsuario(authentication);
        Cliente cliente = usuario.getCliente();

        if (cliente == null) {
            return "redirect:/tutor";
        }

        model.addAttribute(
                "animais",
                animalRepository.findByCliente_IdCliente(
                        cliente.getIdCliente()));

        return "tutor-animais";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("animal", new Animal());
        return "tutor-animal-form";
    }

    @PostMapping
    public String cadastrar(
            @RequestParam String nome,
            @RequestParam String especie,
            @RequestParam(required = false) String raca,
            @RequestParam String sexo,
            @RequestParam(required = false) String cor,
            @RequestParam double peso,
            @RequestParam double tamanho,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataNascimento,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (nome.isBlank()
                || especie.isBlank()
                || sexo.isBlank()
                || peso <= 0
                || tamanho <= 0 )
                 {

            redirectAttributes.addFlashAttribute(
                    "erro", "Confira os dados informados.");
            return "redirect:/tutor/animais/novo";
        }

        Usuario usuario = buscarUsuario(authentication);
        Cliente cliente = usuario.getCliente();

        if (cliente == null) {
            redirectAttributes.addFlashAttribute(
                    "erro", "Sua conta não possui um cadastro de tutor associado.");
            return "redirect:/tutor";
        }

        Animal animal = new Animal();
        animal.setNome(nome.trim());
        animal.setEspecie(especie.trim());
        animal.setRaca(raca);
        animal.setSexo(sexo);
        animal.setCor(cor);
        animal.setPeso(peso);
        animal.setTamanho(tamanho);
        animal.setDataNascimento(dataNascimento);

        // O cliente vem da conta autenticada, nunca do formulário.
        animal.setCliente(cliente);

        animalRepository.save(animal);

        redirectAttributes.addFlashAttribute(
                "sucesso", "Animal cadastrado com sucesso!");

        return "redirect:/tutor/animais";
    }

    private Usuario buscarUsuario(Authentication authentication) {
        return usuarioRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException(
                        "Usuário autenticado não encontrado."));
    }
}
