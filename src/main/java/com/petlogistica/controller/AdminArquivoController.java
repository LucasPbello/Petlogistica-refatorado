package com.petlogistica.controller;

import com.petlogistica.model.Documento;
import com.petlogistica.model.Imagem;
import com.petlogistica.service.AdminArquivoService;
import java.io.IOException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/animais/{id}/arquivos")
public class AdminArquivoController {

    private final AdminArquivoService service;

    public AdminArquivoController(AdminArquivoService service) {
        this.service = service;
    }

    @GetMapping
    public String arquivos(@PathVariable Integer id, Model model) {
        model.addAttribute("animal", service.buscarAnimal(id));
        model.addAttribute("documentos", service.listarDocumentos(id));
        model.addAttribute("imagens", service.listarImagens(id));
        return "admin-animal-arquivos";
    }

    @GetMapping("/documentos/{arquivoId}/arquivo")
    public ResponseEntity<byte[]> baixarDocumento(@PathVariable Integer id,
            @PathVariable Integer arquivoId) {
        Documento documento = service.buscarDocumento(id, arquivoId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"documento-" + arquivoId + ".pdf\"")
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(CacheControl.noStore())
                .body(documento.getArquivo());
    }

    @GetMapping("/imagens/{arquivoId}/arquivo")
    public ResponseEntity<byte[]> visualizarImagem(@PathVariable Integer id,
            @PathVariable Integer arquivoId) {
        Imagem imagem = service.buscarImagem(id, arquivoId);
        String tipo = validarTipoImagem(imagem);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(tipo))
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(CacheControl.noStore())
                .body(imagem.getArquivo());
    }

    @GetMapping("/imagens/{arquivoId}/download")
    public ResponseEntity<byte[]> baixarImagem(@PathVariable Integer id,
            @PathVariable Integer arquivoId) {
        Imagem imagem = service.buscarImagem(id, arquivoId);
        String tipo = validarTipoImagem(imagem);
        String extensao = "image/png".equals(tipo) ? ".png" : ".jpg";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(tipo))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"foto-pet-" + arquivoId + extensao + "\"")
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(CacheControl.noStore())
                .body(imagem.getArquivo());
    }

    private String validarTipoImagem(Imagem imagem) {
        String tipo = imagem.getContentType();
        if (!"image/png".equals(tipo) && !"image/jpeg".equals(tipo)) {
            throw new IllegalArgumentException("Formato de imagem inválido.");
        }
        return tipo;
    }

    @PostMapping("/documentos/adicionar")
    public String adicionarDocumento(@PathVariable Integer id,
            @RequestParam(required = false) String nomeDocumento,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam("arquivo") MultipartFile arquivo,
            RedirectAttributes redirect) throws IOException {
        try {
            service.adicionarDocumento(id, nomeDocumento, tipoDocumento, arquivo);
            redirect.addFlashAttribute("sucesso", "Documento adicionado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return voltar(id);
    }

    @PostMapping("/imagens/adicionar")
    public String adicionarImagem(@PathVariable Integer id,
            @RequestParam(required = false) String nomeImagem,
            @RequestParam("arquivo") MultipartFile arquivo,
            RedirectAttributes redirect) throws IOException {
        try {
            service.adicionarImagem(id, nomeImagem, arquivo);
            redirect.addFlashAttribute("sucesso", "Foto adicionada com sucesso!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return voltar(id);
    }

    @PostMapping("/documentos/{arquivoId}/substituir")
    public String substituirDocumento(@PathVariable Integer id,
            @PathVariable Integer arquivoId,
            @RequestParam("arquivo") MultipartFile arquivo,
            RedirectAttributes redirect) throws IOException {
        try {
            service.substituirDocumento(id, arquivoId, arquivo);
            redirect.addFlashAttribute("sucesso", "Documento substituído!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return voltar(id);
    }

    @PostMapping("/imagens/{arquivoId}/substituir")
    public String substituirImagem(@PathVariable Integer id,
            @PathVariable Integer arquivoId,
            @RequestParam("arquivo") MultipartFile arquivo,
            RedirectAttributes redirect) throws IOException {
        try {
            service.substituirImagem(id, arquivoId, arquivo);
            redirect.addFlashAttribute("sucesso", "Foto substituída!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return voltar(id);
    }

    @PostMapping("/documentos/{arquivoId}/excluir")
    public String excluirDocumento(@PathVariable Integer id,
            @PathVariable Integer arquivoId,
            RedirectAttributes redirect) {
        try {
            service.excluirDocumento(id, arquivoId);
            redirect.addFlashAttribute("sucesso", "Documento excluído!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return voltar(id);
    }

    @PostMapping("/imagens/{arquivoId}/excluir")
    public String excluirImagem(@PathVariable Integer id,
            @PathVariable Integer arquivoId,
            RedirectAttributes redirect) {
        try {
            service.excluirImagem(id, arquivoId);
            redirect.addFlashAttribute("sucesso", "Foto excluída!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return voltar(id);
    }

    private String voltar(Integer id) {
        return "redirect:/admin/animais/" + id + "/arquivos";
    }
}
