package com.petlogistica.controller;

import com.petlogistica.model.Cliente;
import com.petlogistica.model.Usuario;
import com.petlogistica.repository.UsuarioRepository;
import com.petlogistica.service.ClienteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import com.petlogistica.repository.DocumentoRepository;
import com.petlogistica.repository.ImagemRepository;

@Controller
public class TutorController {

    private final UsuarioRepository usuarioRepository;
    private final ClienteService clienteService;
    private final DocumentoRepository documentoRepository;
    private final ImagemRepository imagemRepository;

    public TutorController(
            UsuarioRepository usuarioRepository,
            ClienteService clienteService,
            DocumentoRepository documentoRepository,
            ImagemRepository imagemRepository) {

        this.usuarioRepository = usuarioRepository;
        this.clienteService = clienteService;
        this.documentoRepository = documentoRepository;
        this.imagemRepository = imagemRepository;
    }

    @GetMapping("/tutor")
    public String painelTutor() {
        return "tutor";
    }

    @GetMapping("/tutor/dados")
    public String meusDados(
            Authentication authentication,
            Model model) {

        Usuario usuario = buscarUsuario(authentication);

        Cliente cliente = clienteService.buscarPorId(
                usuario.getCliente().getIdCliente());

        model.addAttribute("nome", cliente.getNome());
        model.addAttribute("email", usuario.getEmail());
        model.addAttribute("cpf", cliente.getCpf());
        model.addAttribute("telefone", cliente.getTelefone());
        model.addAttribute(
                "documentos",
                documentoRepository.findByCliente_IdCliente(
                        cliente.getIdCliente()));
        model.addAttribute(
        "imagens",
        imagemRepository.findByCliente_IdCliente(
                cliente.getIdCliente()));

        return "tutor-dados";
    }

    @PostMapping("/tutor/dados")
    public String atualizarDados(
            Authentication authentication,
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String telefone,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = buscarUsuario(authentication);

        // Guardamos o e-mail antigo antes da atualização
        String emailAntigo = usuario.getEmail();

        try {
            clienteService.atualizarDadosTutor(
                    usuario.getCliente().getIdCliente(),
                    nome,
                    email,
                    cpf,
                    telefone);

            boolean emailAlterado
                    = !emailAntigo.equalsIgnoreCase(email.trim());

            if (emailAlterado) {

                // Encerra a sessão para evitar inconsistências
                new SecurityContextLogoutHandler().logout(
                        request, response, authentication);

                redirectAttributes.addFlashAttribute(
                        "sucesso",
                        "E-mail atualizado! Entre novamente com seus novos dados.");

                return "redirect:/login?emailAtualizado";
            }

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Dados atualizados com sucesso!");

            return "redirect:/tutor/dados";

        } catch (IllegalArgumentException ex) {

            redirectAttributes.addFlashAttribute(
                    "erro", ex.getMessage());

            return "redirect:/tutor/dados";
        }
    }

    private Usuario buscarUsuario(Authentication authentication) {

        Usuario usuario = usuarioRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException(
                "Usuário autenticado não encontrado."));

        if (!"TUTOR".equals(usuario.getPerfil())
                || usuario.getCliente() == null) {

            throw new IllegalArgumentException(
                    "Conta de tutor inválida.");
        }

        return usuario;
    }
}
