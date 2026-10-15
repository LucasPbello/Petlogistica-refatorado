package com.petlogistica.controller;

import com.petlogistica.model.Animal;
import com.petlogistica.model.Documento;
import com.petlogistica.model.Imagem;
import com.petlogistica.service.TutorAnimalService;
import java.io.IOException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tutor/animais")
public class TutorAnimalController {

    private final TutorAnimalService service;

    public TutorAnimalController(TutorAnimalService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(
            Authentication auth, Model model) {

        model.addAttribute("animais", service.listar(auth));
        return "tutor-animais";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("animal", new Animal());
        return "tutor-animal-form";
    }

    @GetMapping("/{id}/editar")
    public String editar(
            @PathVariable Integer id,
            Authentication auth,
            Model model) {

        model.addAttribute(
                "animal", service.buscarAnimal(id, auth));

        return "tutor-animal-form";
    }

    @PostMapping
    public String cadastrar(
            @ModelAttribute Animal animal,
            @RequestParam(required = false) MultipartFile documentoArquivo,
            @RequestParam(required = false) String nomeDocumento,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) MultipartFile imagemArquivo,
            @RequestParam(required = false) String nomeImagem,
            Authentication auth,
            RedirectAttributes redirect) throws IOException {

        try {
            service.salvar(
                    animal, null,
                    documentoArquivo,
                    nomeDocumento,
                    tipoDocumento,
                    imagemArquivo,
                    nomeImagem,
                    auth);

            redirect.addFlashAttribute(
                    "sucesso", "Animal cadastrado com sucesso!");

            return "redirect:/tutor/animais";

        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
            return "redirect:/tutor/animais/novo";
        }
    }

    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Integer id,
            @ModelAttribute Animal animal,
            @RequestParam(required = false) MultipartFile documentoArquivo,
            @RequestParam(required = false) String nomeDocumento,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) MultipartFile imagemArquivo,
            @RequestParam(required = false) String nomeImagem,
            Authentication auth,
            RedirectAttributes redirect) throws IOException {

        try {
            service.salvar(
                    animal, id,
                    documentoArquivo,
                    nomeDocumento,
                    tipoDocumento,
                    imagemArquivo,
                    nomeImagem,
                    auth);

            redirect.addFlashAttribute(
                    "sucesso", "Animal atualizado com sucesso!");

            return "redirect:/tutor/animais";

        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
            return "redirect:/tutor/animais/" + id + "/editar";
        }
    }

    @GetMapping("/{id}/arquivos")
    public String arquivos(
            @PathVariable Integer id,
            Authentication auth,
            Model model) {

        model.addAttribute(
                "animal", service.buscarAnimal(id, auth));

        model.addAttribute(
                "documentos", service.listarDocumentos(id, auth));

        model.addAttribute(
                "imagens", service.listarImagens(id, auth));

        return "tutor-animal-arquivos";
    }

    @GetMapping("/{id}/documentos/{arquivoId}/arquivo")
    public ResponseEntity<byte[]> baixarDocumento(
            @PathVariable Integer id,
            @PathVariable Integer arquivoId,
            Authentication auth) {

        Documento documento = service.buscarDocumento(
                id, arquivoId, auth);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"documento.pdf\"")
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(CacheControl.noStore())
                .body(documento.getArquivo());
    }

    @GetMapping("/{id}/imagens/{arquivoId}/arquivo")
    public ResponseEntity<byte[]> visualizarImagem(
            @PathVariable Integer id,
            @PathVariable Integer arquivoId,
            Authentication auth) {

        Imagem imagem = service.buscarImagem(
                id, arquivoId, auth);

        String tipo = imagem.getContentType();

        if (!"image/png".equals(tipo)
                && !"image/jpeg".equals(tipo)) {
            throw new IllegalArgumentException(
                    "Tipo de imagem inválido.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(tipo))
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(CacheControl.noStore())
                .body(imagem.getArquivo());
    }

    @PostMapping("/{id}/documentos/{arquivoId}/substituir")
    public String substituirDocumento(
            @PathVariable Integer id,
            @PathVariable Integer arquivoId,
            @RequestParam("arquivo") MultipartFile arquivo,
            Authentication auth,
            RedirectAttributes redirect) throws IOException {

        try {
            service.substituirDocumento(
                    id, arquivoId, arquivo, auth);
            redirect.addFlashAttribute(
                    "sucesso", "Documento substituído!");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
        }

        return "redirect:/tutor/animais/" + id + "/arquivos";
    }

    @PostMapping("/{id}/imagens/{arquivoId}/substituir")
    public String substituirImagem(
            @PathVariable Integer id,
            @PathVariable Integer arquivoId,
            @RequestParam("arquivo") MultipartFile arquivo,
            Authentication auth,
            RedirectAttributes redirect) throws IOException {

        try {
            service.substituirImagem(
                    id, arquivoId, arquivo, auth);
            redirect.addFlashAttribute(
                    "sucesso", "Foto substituída!");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
        }

        return "redirect:/tutor/animais/" + id + "/arquivos";
    }

    @PostMapping("/{id}/documentos/{arquivoId}/excluir")
    public String excluirDocumento(
            @PathVariable Integer id,
            @PathVariable Integer arquivoId,
            Authentication auth,
            RedirectAttributes redirect) {

        service.excluirDocumento(id, arquivoId, auth);

        redirect.addFlashAttribute(
                "sucesso", "Documento excluído!");

        return "redirect:/tutor/animais/" + id + "/arquivos";
    }

    @PostMapping("/{id}/imagens/{arquivoId}/excluir")
    public String excluirImagem(
            @PathVariable Integer id,
            @PathVariable Integer arquivoId,
            Authentication auth,
            RedirectAttributes redirect) {

        service.excluirImagem(id, arquivoId, auth);

        redirect.addFlashAttribute(
                "sucesso", "Foto excluída!");

        return "redirect:/tutor/animais/" + id + "/arquivos";
    }

    @PostMapping("/{id}/excluir")
    public String excluirAnimal(
            @PathVariable Integer id,
            Authentication auth,
            RedirectAttributes redirect) {

        service.excluirAnimal(id, auth);

        redirect.addFlashAttribute(
                "sucesso", "Animal e seus anexos excluídos!");

        return "redirect:/tutor/animais";
    }

    @PostMapping("/{id}/documentos/adicionar")
    public String adicionarDocumento(
            @PathVariable Integer id,
            @RequestParam(required = false) String nomeDocumento,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam("arquivo") MultipartFile arquivo,
            Authentication auth,
            RedirectAttributes redirect) throws IOException {

        try {
            service.adicionarDocumento(
                    id, nomeDocumento, tipoDocumento, arquivo, auth);

            redirect.addFlashAttribute(
                    "sucesso", "Documento adicionado com sucesso!");

        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
        }

        return "redirect:/tutor/animais/" + id + "/arquivos";
    }

    @PostMapping("/{id}/imagens/adicionar")
    public String adicionarImagem(
            @PathVariable Integer id,
            @RequestParam(required = false) String nomeImagem,
            @RequestParam("arquivo") MultipartFile arquivo,
            Authentication auth,
            RedirectAttributes redirect) throws IOException {

        try {
            service.adicionarImagem(id, nomeImagem, arquivo, auth);

            redirect.addFlashAttribute(
                    "sucesso", "Foto adicionada com sucesso!");

        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
        }

        return "redirect:/tutor/animais/" + id + "/arquivos";
    }

    @GetMapping("/{id}/imagens/{arquivoId}/download")
    public ResponseEntity<byte[]> baixarImagem(
            @PathVariable Integer id,
            @PathVariable Integer arquivoId,
            Authentication auth) {

        Imagem imagem = service.buscarImagem(
                id, arquivoId, auth);

        String tipo = imagem.getContentType();

        if (!"image/png".equals(tipo)
                && !"image/jpeg".equals(tipo)) {
            throw new IllegalArgumentException(
                    "Formato de imagem inválido.");
        }

        String extensao = "image/png".equals(tipo)
                ? ".png" : ".jpg";

        String nomeArquivo = "foto-pet-" + arquivoId + extensao;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(tipo))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + nomeArquivo + "\""
                )
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(CacheControl.noStore())
                .body(imagem.getArquivo());
    }

}
