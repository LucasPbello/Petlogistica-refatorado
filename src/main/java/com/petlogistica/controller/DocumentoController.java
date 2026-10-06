package com.petlogistica.controller;

import com.petlogistica.model.Documento;
import com.petlogistica.service.DocumentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.petlogistica.dto.DocumentoDTO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @GetMapping
    public List<Documento> listarTodos() {
        return documentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documento> buscarPorId(@PathVariable int id) {
        return documentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "multipart/form-data")
    public Documento salvar(
            @RequestParam String nome,
            @RequestParam String tipo,
            @RequestParam Integer idAnimal,
            @RequestParam MultipartFile arquivo) throws Exception {

        DocumentoDTO dto = new DocumentoDTO();

        dto.setNome(nome);
        dto.setTipo(tipo);
        dto.setIdAnimal(idAnimal);

        return documentoService.salvar(dto, arquivo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Documento> atualizar(
            @PathVariable int id,
            @RequestBody DocumentoDTO dto) {

        return documentoService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/arquivo")
    public ResponseEntity<byte[]> baixarArquivo(@PathVariable int id) {

        return documentoService.buscarPorId(id)
                .filter(documento -> documento.getArquivo() != null)
                .map(documento -> ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + documento.getNome() + ".pdf\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(documento.getArquivo())
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable int id) {
        documentoService.excluir(id);
        return ResponseEntity.ok("Documento excluído com sucesso.");
    }
}
