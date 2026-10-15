package com.petlogistica.service;

import com.petlogistica.model.Animal;
import com.petlogistica.model.Documento;
import com.petlogistica.model.Imagem;
import com.petlogistica.repository.AnimalRepository;
import com.petlogistica.repository.DocumentoRepository;
import com.petlogistica.repository.ImagemRepository;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdminArquivoService {

    private static final long LIMITE = 5L * 1024 * 1024;
    private final AnimalRepository animais;
    private final DocumentoRepository documentos;
    private final ImagemRepository imagens;

    public AdminArquivoService(AnimalRepository animais, DocumentoRepository documentos,
            ImagemRepository imagens) {
        this.animais = animais;
        this.documentos = documentos;
        this.imagens = imagens;
    }

    public Animal buscarAnimal(Integer id) {
        return animais.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Pet não encontrado."));
    }

    public List<Documento> listarDocumentos(Integer id) {
        buscarAnimal(id);
        return documentos.findByAnimal_IdAnimal(id);
    }

    public List<Imagem> listarImagens(Integer id) {
        buscarAnimal(id);
        return imagens.findByAnimal_IdAnimal(id);
    }

    public Documento buscarDocumento(Integer idAnimal, Integer idDocumento) {
        buscarAnimal(idAnimal);
        Documento documento = documentos.findById(idDocumento).orElseThrow(()
                -> new IllegalArgumentException("Documento não encontrado."));
        if (documento.getAnimal() == null
                || !idAnimal.equals(documento.getAnimal().getIdAnimal())) {
            throw new IllegalArgumentException("Documento não pertence a este pet.");
        }
        return documento;
    }

    public Imagem buscarImagem(Integer idAnimal, Integer idImagem) {
        buscarAnimal(idAnimal);
        Imagem imagem = imagens.findById(idImagem).orElseThrow(()
                -> new IllegalArgumentException("Foto não encontrada."));
        if (imagem.getAnimal() == null
                || !idAnimal.equals(imagem.getAnimal().getIdAnimal())) {
            throw new IllegalArgumentException("Foto não pertence a este pet.");
        }
        return imagem;
    }

    private byte[] validarPdf(MultipartFile arquivo) throws IOException {
        if (arquivo == null || arquivo.isEmpty() || arquivo.getSize() > LIMITE) {
            throw new IllegalArgumentException("Envie um PDF de até 5 MB.");
        }
        byte[] bytes = arquivo.getBytes();
        if (bytes.length < 5 || bytes[0] != '%' || bytes[1] != 'P'
                || bytes[2] != 'D' || bytes[3] != 'F' || bytes[4] != '-') {
            throw new IllegalArgumentException("O documento deve ser um PDF válido.");
        }
        return bytes;
    }

    private String tipoImagem(byte[] bytes) {
        boolean png = bytes.length >= 8 && (bytes[0] & 255) == 137
                && bytes[1] == 80 && bytes[2] == 78 && bytes[3] == 71
                && bytes[4] == 13 && bytes[5] == 10 && bytes[6] == 26 && bytes[7] == 10;
        boolean jpg = bytes.length >= 3 && (bytes[0] & 255) == 255
                && (bytes[1] & 255) == 216 && (bytes[2] & 255) == 255;
        if (png) {
            return "image/png";
        }
        if (jpg) {
            return "image/jpeg";
        }
        throw new IllegalArgumentException("A foto deve ser PNG ou JPG.");
    }

    private byte[] validarImagem(MultipartFile arquivo) throws IOException {
        if (arquivo == null || arquivo.isEmpty() || arquivo.getSize() > LIMITE) {
            throw new IllegalArgumentException("Envie uma imagem de até 5 MB.");
        }
        byte[] bytes = arquivo.getBytes();
        tipoImagem(bytes);
        return bytes;
    }

    @Transactional
    public void adicionarDocumento(Integer idAnimal, String nome, String tipo,
            MultipartFile arquivo) throws IOException {
        Animal animal = buscarAnimal(idAnimal);
        byte[] pdf = validarPdf(arquivo);
        Documento documento = new Documento();
        documento.setAnimal(animal);
        documento.setCliente(null);
        documento.setNome(nome == null || nome.isBlank()
                ? arquivo.getOriginalFilename() : nome.trim());
        documento.setTipo(tipo == null || tipo.isBlank() ? "OUTRO" : tipo);
        documento.setArquivo(pdf);
        documentos.save(documento);
    }

    @Transactional
    public void adicionarImagem(Integer idAnimal, String nome,
            MultipartFile arquivo) throws IOException {
        Animal animal = buscarAnimal(idAnimal);
        byte[] foto = validarImagem(arquivo);
        Imagem imagem = new Imagem();
        imagem.setAnimal(animal);
        imagem.setCliente(null);
        imagem.setNome(nome == null || nome.isBlank()
                ? arquivo.getOriginalFilename() : nome.trim());
        imagem.setTipo("FOTO_ANIMAL");
        imagem.setContentType(tipoImagem(foto));
        imagem.setArquivo(foto);
        imagens.save(imagem);
    }

    @Transactional
    public void substituirDocumento(Integer idAnimal, Integer idDocumento,
            MultipartFile arquivo) throws IOException {
        byte[] pdf = validarPdf(arquivo);
        Documento documento = buscarDocumento(idAnimal, idDocumento);
        documento.setArquivo(pdf);
        documentos.save(documento);
    }

    @Transactional
    public void substituirImagem(Integer idAnimal, Integer idImagem,
            MultipartFile arquivo) throws IOException {
        byte[] foto = validarImagem(arquivo);
        Imagem imagem = buscarImagem(idAnimal, idImagem);
        imagem.setArquivo(foto);
        imagem.setContentType(tipoImagem(foto));
        imagens.save(imagem);
    }

    @Transactional
    public void excluirDocumento(Integer idAnimal, Integer idDocumento) {
        documentos.delete(buscarDocumento(idAnimal, idDocumento));
    }

    @Transactional
    public void excluirImagem(Integer idAnimal, Integer idImagem) {
        imagens.delete(buscarImagem(idAnimal, idImagem));
    }
}
