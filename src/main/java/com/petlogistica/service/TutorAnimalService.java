package com.petlogistica.service;

import com.petlogistica.model.Animal;
import com.petlogistica.model.Cliente;
import com.petlogistica.model.Documento;
import com.petlogistica.model.Imagem;
import com.petlogistica.model.Usuario;
import com.petlogistica.repository.AnimalRepository;
import com.petlogistica.repository.DocumentoRepository;
import com.petlogistica.repository.ImagemRepository;
import com.petlogistica.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TutorAnimalService {

    private static final long LIMITE = 5L * 1024 * 1024;

    private final AnimalRepository animais;
    private final DocumentoRepository documentos;
    private final ImagemRepository imagens;
    private final UsuarioRepository usuarios;

    public TutorAnimalService(
            AnimalRepository animais,
            DocumentoRepository documentos,
            ImagemRepository imagens,
            UsuarioRepository usuarios) {

        this.animais = animais;
        this.documentos = documentos;
        this.imagens = imagens;
        this.usuarios = usuarios;
    }

    public Cliente tutor(Authentication auth) {
        Usuario usuario = usuarios
                .findByEmailIgnoreCase(auth.getName())
                .orElseThrow(() -> new AccessDeniedException(
                "Usuário não encontrado."));

        if (!"TUTOR".equals(usuario.getPerfil())
                || usuario.getCliente() == null) {
            throw new AccessDeniedException(
                    "Tutor sem cadastro associado.");
        }

        return usuario.getCliente();
    }

    public List<Animal> listar(Authentication auth) {
        Integer id = tutor(auth).getIdCliente();
        return animais.findByCliente_IdCliente(id);
    }

    public Animal buscarAnimal(
            Integer idAnimal, Authentication auth) {

        Animal animal = animais.findById(idAnimal)
                .orElseThrow(() -> new IllegalArgumentException(
                "Animal não encontrado."));

        Integer idTutor = tutor(auth).getIdCliente();

        if (animal.getCliente() == null
                || !idTutor.equals(
                        animal.getCliente().getIdCliente())) {
            throw new AccessDeniedException(
                    "Você não pode acessar este animal.");
        }

        return animal;
    }

    private void validarAnimal(Animal animal) {
        if (animal.getNome() == null
                || animal.getNome().isBlank()
                || animal.getNome().length() > 100
                || animal.getEspecie() == null
                || animal.getEspecie().isBlank()
                || animal.getSexo() == null
                || animal.getSexo().isBlank()
                || !Double.isFinite(animal.getPeso())
                || !Double.isFinite(animal.getTamanho())
                || animal.getPeso() <= 0
                || animal.getTamanho() <= 0
                || animal.getDataNascimento() == null
                || animal.getDataNascimento()
                        .isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Confira os dados do animal.");
        }
    }

    private boolean temArquivo(MultipartFile arquivo) {
        return arquivo != null && !arquivo.isEmpty();
    }

    private byte[] validarPdf(MultipartFile arquivo)
            throws IOException {

        if (!temArquivo(arquivo)
                || arquivo.getSize() > LIMITE) {
            throw new IllegalArgumentException(
                    "Envie um PDF de até 5 MB.");
        }

        byte[] bytes = arquivo.getBytes();

        if (bytes.length < 5
                || bytes[0] != '%'
                || bytes[1] != 'P'
                || bytes[2] != 'D'
                || bytes[3] != 'F'
                || bytes[4] != '-') {
            throw new IllegalArgumentException(
                    "O documento deve ser um PDF válido.");
        }

        return bytes;
    }

    private String tipoImagem(byte[] bytes) {
        boolean png = bytes.length >= 8
                && (bytes[0] & 255) == 137
                && bytes[1] == 80
                && bytes[2] == 78
                && bytes[3] == 71
                && bytes[4] == 13
                && bytes[5] == 10
                && bytes[6] == 26
                && bytes[7] == 10;

        boolean jpg = bytes.length >= 3
                && (bytes[0] & 255) == 255
                && (bytes[1] & 255) == 216
                && (bytes[2] & 255) == 255;

        if (png) {
            return "image/png";
        }

        if (jpg) {
            return "image/jpeg";
        }

        throw new IllegalArgumentException(
                "A foto deve ser PNG ou JPG.");
    }

    private byte[] validarImagem(MultipartFile arquivo)
            throws IOException {

        if (!temArquivo(arquivo)
                || arquivo.getSize() > LIMITE) {
            throw new IllegalArgumentException(
                    "Envie uma imagem de até 5 MB.");
        }

        byte[] bytes = arquivo.getBytes();
        tipoImagem(bytes);
        return bytes;
    }

    private void copiarDados(Animal destino, Animal origem) {
        destino.setNome(origem.getNome().trim());
        destino.setEspecie(origem.getEspecie().trim());
        destino.setRaca(origem.getRaca());
        destino.setSexo(origem.getSexo());
        destino.setCor(origem.getCor());
        destino.setPeso(origem.getPeso());
        destino.setTamanho(origem.getTamanho());
        destino.setDataNascimento(origem.getDataNascimento());
    }

    @Transactional
    public Animal salvar(
            Animal dados,
            Integer idAnimal,
            MultipartFile documentoArquivo,
            String nomeDocumento,
            String tipoDocumento,
            MultipartFile imagemArquivo,
            String nomeImagem,
            Authentication auth) throws IOException {

        validarAnimal(dados);

        // Validar os arquivos antes de modificar o banco.
        byte[] pdf = temArquivo(documentoArquivo)
                ? validarPdf(documentoArquivo) : null;

        byte[] foto = temArquivo(imagemArquivo)
                ? validarImagem(imagemArquivo) : null;

        Animal animal;

        if (idAnimal == null) {
            animal = new Animal();
            animal.setCliente(tutor(auth));
        } else {
            animal = buscarAnimal(idAnimal, auth);
        }

        copiarDados(animal, dados);
        animal = animais.save(animal);

        if (pdf != null) {
            Documento documento = new Documento();
            documento.setAnimal(animal);
            documento.setCliente(null);
            documento.setNome(
                    nomeDocumento == null
                    || nomeDocumento.isBlank()
                    ? documentoArquivo.getOriginalFilename()
                    : nomeDocumento.trim());
            documento.setTipo(
                    tipoDocumento == null
                    || tipoDocumento.isBlank()
                    ? "OUTRO" : tipoDocumento);
            documento.setArquivo(pdf);
            documentos.save(documento);
        }

        if (foto != null) {
            Imagem imagem = new Imagem();
            imagem.setAnimal(animal);
            imagem.setCliente(null);
            imagem.setNome(
                    nomeImagem == null
                    || nomeImagem.isBlank()
                    ? imagemArquivo.getOriginalFilename()
                    : nomeImagem.trim());
            imagem.setTipo("FOTO_ANIMAL");
            imagem.setContentType(tipoImagem(foto));
            imagem.setArquivo(foto);
            imagens.save(imagem);
        }

        return animal;
    }

    public List<Documento> listarDocumentos(
            Integer idAnimal, Authentication auth) {
        buscarAnimal(idAnimal, auth);
        return documentos.findByAnimal_IdAnimal(idAnimal);
    }

    public List<Imagem> listarImagens(
            Integer idAnimal, Authentication auth) {
        buscarAnimal(idAnimal, auth);
        return imagens.findByAnimal_IdAnimal(idAnimal);
    }

    public Documento buscarDocumento(
            Integer idAnimal,
            Integer idDocumento,
            Authentication auth) {

        buscarAnimal(idAnimal, auth);

        Documento documento = documentos.findById(idDocumento)
                .orElseThrow(() -> new IllegalArgumentException(
                "Documento não encontrado."));

        if (documento.getAnimal() == null
                || !idAnimal.equals(
                        documento.getAnimal().getIdAnimal())) {
            throw new AccessDeniedException(
                    "Documento não pertence a este animal.");
        }

        return documento;
    }

    public Imagem buscarImagem(
            Integer idAnimal,
            Integer idImagem,
            Authentication auth) {

        buscarAnimal(idAnimal, auth);

        Imagem imagem = imagens.findById(idImagem)
                .orElseThrow(() -> new IllegalArgumentException(
                "Imagem não encontrada."));

        if (imagem.getAnimal() == null
                || !idAnimal.equals(
                        imagem.getAnimal().getIdAnimal())) {
            throw new AccessDeniedException(
                    "Imagem não pertence a este animal.");
        }

        return imagem;
    }

    @Transactional
    public void substituirDocumento(
            Integer idAnimal,
            Integer idDocumento,
            MultipartFile arquivo,
            Authentication auth) throws IOException {

        byte[] pdf = validarPdf(arquivo);

        Documento documento = buscarDocumento(
                idAnimal, idDocumento, auth);

        documento.setArquivo(pdf);
        documentos.save(documento);
    }

    @Transactional
    public void substituirImagem(
            Integer idAnimal,
            Integer idImagem,
            MultipartFile arquivo,
            Authentication auth) throws IOException {

        byte[] foto = validarImagem(arquivo);

        Imagem imagem = buscarImagem(
                idAnimal, idImagem, auth);

        imagem.setArquivo(foto);
        imagem.setContentType(tipoImagem(foto));
        imagens.save(imagem);
    }

    @Transactional
    public void excluirDocumento(
            Integer idAnimal,
            Integer idDocumento,
            Authentication auth) {

        Documento documento = buscarDocumento(
                idAnimal, idDocumento, auth);

        documentos.delete(documento);
    }

    @Transactional
    public void excluirImagem(
            Integer idAnimal,
            Integer idImagem,
            Authentication auth) {

        Imagem imagem = buscarImagem(
                idAnimal, idImagem, auth);

        imagens.delete(imagem);
    }

    @Transactional
    public void excluirAnimal(
            Integer idAnimal, Authentication auth) {

        Animal animal = buscarAnimal(idAnimal, auth);

        documentos.deleteAll(
                documentos.findByAnimal_IdAnimal(idAnimal));

        imagens.deleteAll(
                imagens.findByAnimal_IdAnimal(idAnimal));

        documentos.flush();
        imagens.flush();

        animais.delete(animal);
    }

    @Transactional
    public void adicionarDocumento(
            Integer idAnimal,
            String nome,
            String tipo,
            MultipartFile arquivo,
            Authentication auth) throws IOException {

        // Primeiro verifica se o animal pertence ao tutor.
        Animal animal = buscarAnimal(idAnimal, auth);

        // Reutiliza a validação de PDF e limite de 5 MB.
        byte[] pdf = validarPdf(arquivo);

        Documento documento = new Documento();
        documento.setAnimal(animal);
        documento.setCliente(null);
        documento.setNome(
                nome == null || nome.isBlank()
                ? arquivo.getOriginalFilename()
                : nome.trim()
        );
        documento.setTipo(
                tipo == null || tipo.isBlank()
                ? "OUTRO"
                : tipo
        );
        documento.setArquivo(pdf);

        documentos.save(documento);
    }

    @Transactional
    public void adicionarImagem(
            Integer idAnimal,
            String nome,
            MultipartFile arquivo,
            Authentication auth) throws IOException {

        // Primeiro verifica se o animal pertence ao tutor.
        Animal animal = buscarAnimal(idAnimal, auth);

        // Reutiliza a validação de imagem e limite de 5 MB.
        byte[] foto = validarImagem(arquivo);

        Imagem imagem = new Imagem();
        imagem.setAnimal(animal);
        imagem.setCliente(null);
        imagem.setNome(
                nome == null || nome.isBlank()
                ? arquivo.getOriginalFilename()
                : nome.trim()
        );
        imagem.setTipo("FOTO_ANIMAL");
        imagem.setContentType(tipoImagem(foto));
        imagem.setArquivo(foto);

        imagens.save(imagem);
    }

}
