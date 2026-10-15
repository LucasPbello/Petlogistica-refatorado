package com.petlogistica.controller;

import com.petlogistica.model.Cliente;
import com.petlogistica.model.Documento;
import com.petlogistica.model.Usuario;
import com.petlogistica.repository.DocumentoRepository;
import com.petlogistica.repository.UsuarioRepository;
import com.petlogistica.service.DocumentoService;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.petlogistica.model.Imagem;
import com.petlogistica.repository.ImagemRepository;
import com.petlogistica.service.ImagemService;

@Controller
@RequestMapping("/tutor")
public class TutorArquivoController {

    private final UsuarioRepository usuarioRepository;
    private final DocumentoRepository documentoRepository;
    private final DocumentoService documentoService;
    private final ImagemRepository imagemRepository;
    private final ImagemService imagemService;

    public TutorArquivoController(
            UsuarioRepository usuarioRepository,
            DocumentoRepository documentoRepository,
            DocumentoService documentoService,
            ImagemRepository imagemRepository,
            ImagemService imagemService) {

        this.usuarioRepository = usuarioRepository;
        this.documentoRepository = documentoRepository;
        this.documentoService = documentoService;
        this.imagemRepository = imagemRepository;
        this.imagemService = imagemService;
    }

    private Cliente buscarTutor(Authentication authentication) {

        Usuario usuario = usuarioRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException(
                "Usuário autenticado não encontrado."));

        if (!"TUTOR".equals(usuario.getPerfil())
                || usuario.getCliente() == null) {

            throw new IllegalArgumentException(
                    "Conta de tutor inválida.");
        }

        return usuario.getCliente();
    }

    @PostMapping
    public String adicionarDocumento(
            Authentication authentication,
            @RequestParam String nome,
            @RequestParam String tipo,
            @RequestParam MultipartFile arquivo,
            RedirectAttributes redirectAttributes) {

        Cliente tutor = buscarTutor(authentication);

        try {
            if (arquivo == null || arquivo.isEmpty()) {
                throw new IllegalArgumentException(
                        "Selecione um documento.");
            }

            if (arquivo.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException(
                        "O documento deve ter no máximo 5 MB.");
            }

            byte[] bytes = arquivo.getBytes();

            boolean pdf = bytes.length >= 5
                    && bytes[0] == '%'
                    && bytes[1] == 'P'
                    && bytes[2] == 'D'
                    && bytes[3] == 'F'
                    && bytes[4] == '-';

            if (!pdf) {
                throw new IllegalArgumentException(
                        "Envie apenas documentos em PDF.");
            }

            documentoService.salvarDocumentoTutor(
                    tutor.getIdCliente(),
                    nome,
                    tipo,
                    arquivo);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Documento cadastrado com sucesso!");

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    ex.getMessage());
        }

        return "redirect:/tutor/dados";
    }

    @GetMapping("/{id}/arquivo")
    public ResponseEntity<byte[]> baixarDocumento(
            Authentication authentication,
            @PathVariable Integer id) {

        Cliente tutor = buscarTutor(authentication);

        Documento documento = documentoRepository.findById(id)
                .orElse(null);

        if (documento == null
                || documento.getCliente() == null
                || !tutor.getIdCliente().equals(
                        documento.getCliente().getIdCliente())) {

            return ResponseEntity.notFound().build();
        }

        if (documento.getArquivo() == null) {
            return ResponseEntity.notFound().build();
        }

        String nomeSeguro = documento.getNome() == null
                ? "documento"
                : documento.getNome().replaceAll(
                        "[^a-zA-Z0-9._-]", "_");

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(nomeSeguro + ".pdf")
                                .build()
                                .toString()
                )
                .contentType(MediaType.APPLICATION_PDF)
                .header("X-Content-Type-Options", "nosniff")
                .body(documento.getArquivo());
    }

    @PostMapping("/{id}/excluir")
    public String excluirDocumento(
            Authentication authentication,
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        Cliente tutor = buscarTutor(authentication);

        Documento documento = documentoRepository.findById(id)
                .orElse(null);

        if (documento == null
                || documento.getCliente() == null
                || !tutor.getIdCliente().equals(
                        documento.getCliente().getIdCliente())) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Documento não encontrado.");

            return "redirect:/tutor/dados";
        }

        documentoRepository.delete(documento);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Documento excluído com sucesso!");

        return "redirect:/tutor/dados";
    }
    
@PostMapping("/imagens")
public String adicionarImagem(
        Authentication authentication,
        @RequestParam String nome,
        @RequestParam String tipo,
        @RequestParam MultipartFile arquivo,
        RedirectAttributes redirectAttributes) {

    Cliente tutor = buscarTutor(authentication);

    try {
        if (nome == null || nome.isBlank()
                || tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException(
                    "Informe o nome e o tipo da foto.");
        }

        imagemService.salvarImagemTutor(
                tutor.getIdCliente(),
                nome.trim(),
                tipo.trim(),
                arquivo);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Foto cadastrada com sucesso!");

    } catch (Exception ex) {
        redirectAttributes.addFlashAttribute(
                "erro",
                ex.getMessage());
    }

    return "redirect:/tutor/dados";
}

@GetMapping("/imagens/{id}/arquivo")
public ResponseEntity<byte[]> visualizarImagem(
        Authentication authentication,
        @PathVariable Integer id) {

    Cliente tutor = buscarTutor(authentication);

    Imagem imagem = imagemRepository.findById(id)
            .orElse(null);

    if (imagem == null
            || imagem.getCliente() == null
            || !tutor.getIdCliente().equals(
                    imagem.getCliente().getIdCliente())) {

        return ResponseEntity.notFound().build();
    }

    if (imagem.getArquivo() == null
            || imagem.getArquivo().length == 0) {
        return ResponseEntity.notFound().build();
    }

    String contentType = imagem.getContentType();

    if (!"image/png".equals(contentType)
            && !"image/jpeg".equals(contentType)) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header("X-Content-Type-Options", "nosniff")
            .header("Cache-Control", "no-store")
            .body(imagem.getArquivo());
}

@PostMapping("/imagens/{id}/excluir")
public String excluirImagem(
        Authentication authentication,
        @PathVariable Integer id,
        RedirectAttributes redirectAttributes) {

    Cliente tutor = buscarTutor(authentication);

    Imagem imagem = imagemRepository.findById(id)
            .orElse(null);

    if (imagem == null
            || imagem.getCliente() == null
            || !tutor.getIdCliente().equals(
                    imagem.getCliente().getIdCliente())) {

        redirectAttributes.addFlashAttribute(
                "erro",
                "Foto não encontrada.");

        return "redirect:/tutor/dados";
    }

    imagemRepository.delete(imagem);

    redirectAttributes.addFlashAttribute(
            "sucesso",
            "Foto excluída com sucesso!");

    return "redirect:/tutor/dados";
}

}
