package com.petlogistica.service;

import com.petlogistica.model.Documento;
import com.petlogistica.model.Animal;
import com.petlogistica.repository.DocumentoRepository;
import com.petlogistica.repository.AnimalRepository;
import org.springframework.stereotype.Service;
import com.petlogistica.dto.DocumentoDTO;
import org.springframework.web.multipart.MultipartFile;
import com.petlogistica.exception.RecursoNaoEncontradoException;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final AnimalRepository animalRepository;

    public DocumentoService(DocumentoRepository documentoRepository,
            AnimalRepository animalRepository) {
        this.documentoRepository = documentoRepository;
        this.animalRepository = animalRepository;
    }

    public Documento salvar(DocumentoDTO dto, MultipartFile arquivo) throws Exception {

        Animal animal = buscarAnimal(dto.getIdAnimal());

        Documento documento = new Documento();

        documento.setNome(dto.getNome());
        documento.setTipo(dto.getTipo());
        documento.setAnimal(animal);
        documento.setArquivo(arquivo.getBytes());

        return documentoRepository.save(documento);
    }

    private Animal buscarAnimal(Integer idAnimal) {
        return animalRepository
                .findById(idAnimal)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException("Animal não encontrado"));
    }

    public List<Documento> listarTodos() {
        return documentoRepository.findAll();
    }

    public Optional<Documento> buscarPorId(int id) {
        return documentoRepository.findById(id);
    }

    public Optional<Documento> atualizar(int id, DocumentoDTO dto) {

        return documentoRepository.findById(id)
                .map(documentoExistente -> {

                    Animal animal = buscarAnimal(dto.getIdAnimal());

                    documentoExistente.setNome(dto.getNome());
                    documentoExistente.setTipo(dto.getTipo());
                    documentoExistente.setAnimal(animal);

                    return documentoRepository.save(documentoExistente);
                });
    }

    public void excluir(int id) {
        documentoRepository.deleteById(id);
    }
}
