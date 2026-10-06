package com.petlogistica.controller;

import com.petlogistica.dto.ImagemDTO;
import com.petlogistica.model.Imagem;
import com.petlogistica.service.ImagemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/imagens")
public class ImagemController {

    private final ImagemService imagemService;

    public ImagemController(ImagemService imagemService) {
        this.imagemService = imagemService;
    }

    @GetMapping
    public List<Imagem> listarTodos() {
        return imagemService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imagem> buscarPorId(@PathVariable int id) {
        return imagemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "multipart/form-data")
    public Imagem salvar(
            @RequestParam String nome,
            @RequestParam String tipo,
            @RequestParam Integer idAnimal,
            @RequestParam MultipartFile arquivo) throws Exception {

        ImagemDTO dto = new ImagemDTO();

        dto.setNome(nome);
        dto.setTipo(tipo);
        dto.setIdAnimal(idAnimal);

        return imagemService.salvar(dto, arquivo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Imagem> atualizar(
            @PathVariable int id,
            @RequestBody ImagemDTO dto) {

        return imagemService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/arquivo")
    public ResponseEntity<byte[]> baixarArquivo(@PathVariable int id) {
        return imagemService.buscarPorId(id)
                .filter(imagem -> imagem.getArquivo() != null)
                .map(imagem -> ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + imagem.getNome() + "\""
                )
                .contentType(MediaType.parseMediaType(imagem.getContentType()))
                .body(imagem.getArquivo())
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable int id) {
        imagemService.excluir(id);
        return ResponseEntity.ok("Imagem excluída com sucesso.");
    }
}
