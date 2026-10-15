package com.petlogistica.controller;

import com.petlogistica.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AcessoController {

    private final UsuarioService usuarioService;

    public AcessoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @RequestParam String nome,
            @RequestParam String nomeUsuario,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String confirmarSenha,
            RedirectAttributes redirectAttributes) {

        if (!senha.equals(confirmarSenha)) {
            redirectAttributes.addFlashAttribute(
                    "erro", "As senhas não são iguais.");

            return "redirect:/cadastro";
        }

        try {

            usuarioService.cadastrarTutor(
                    nome,
                    nomeUsuario,
                    email,
                    senha);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Cadastro realizado com sucesso!");

            return "redirect:/login?cadastro";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro", e.getMessage());

            return "redirect:/cadastro";
        }
    }
}
