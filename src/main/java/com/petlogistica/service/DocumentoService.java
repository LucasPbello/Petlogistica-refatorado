package com.petlogistica.service;

import com.petlogistica.model.Documento;
import com.petlogistica.model.Animal;
import com.petlogistica.repository.DocumentoRepository;
import com.petlogistica.repository.AnimalRepository;
import org.springframework.stereotype.Service;
import com.petlogistica.dto.DocumentoDTO;
import org.springframework.web.multipart.MultipartFile;
import com.petlogistica.exception.RecursoNaoEncontradoException;
import com.petlogistica.model.Cliente;
import com.petlogistica.repository.ClienteRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final AnimalRepository animalRepository;
    private final ClienteRepository clienteRepository;

    public DocumentoService(
            DocumentoRepository documentoRepository,
            AnimalRepository animalRepository,
            ClienteRepository clienteRepository) {

        this.documentoRepository = documentoRepository;
        this.animalRepository = animalRepository;
        this.clienteRepository = clienteRepository;
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

    @Transactional
    public Documento salvarDocumentoTutor(
            Integer idCliente,
            String nome,
            String tipo,
            MultipartFile arquivo) throws Exception {

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                "Tutor não encontrado."));

        if (nome == null || nome.isBlank()
                || tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException(
                    "Informe o nome e o tipo do documento.");
        }

        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Selecione um arquivo.");
        }

        Documento documento = new Documento();

        documento.setNome(nome.trim());
        documento.setTipo(tipo.trim());
        documento.setCliente(cliente);
        documento.setAnimal(null);
        documento.setArquivo(arquivo.getBytes());

        return documentoRepository.save(documento);
    }

    @Transactional
    public Documento substituirArquivo(
            Integer idDocumento,
            MultipartFile novoArquivo) throws Exception {

        Documento documento = documentoRepository.findById(idDocumento)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                "Documento não encontrado."));

        if (novoArquivo == null || novoArquivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Selecione um arquivo válido.");
        }

        documento.setArquivo(novoArquivo.getBytes());

        return documentoRepository.save(documento);
    }

}
