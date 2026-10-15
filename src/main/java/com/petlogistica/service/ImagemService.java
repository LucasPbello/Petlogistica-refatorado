package com.petlogistica.service;

import com.petlogistica.dto.ImagemDTO;
import com.petlogistica.exception.RecursoNaoEncontradoException;
import com.petlogistica.model.Animal;
import com.petlogistica.model.Cliente;
import com.petlogistica.model.Imagem;
import com.petlogistica.repository.AnimalRepository;
import com.petlogistica.repository.ClienteRepository;
import com.petlogistica.repository.ImagemRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagemService {

    private static final long TAMANHO_MAXIMO = 5 * 1024 * 1024;

    private final ImagemRepository imagemRepository;
    private final AnimalRepository animalRepository;
    private final ClienteRepository clienteRepository;

    public ImagemService(
            ImagemRepository imagemRepository,
            AnimalRepository animalRepository,
            ClienteRepository clienteRepository) {

        this.imagemRepository = imagemRepository;
        this.animalRepository = animalRepository;
        this.clienteRepository = clienteRepository;
    }

    private Animal buscarAnimal(Integer idAnimal) {
        return animalRepository.findById(idAnimal)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException(
                        "Animal não encontrado."));
    }

    private Cliente buscarCliente(Integer idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException(
                        "Tutor não encontrado."));
    }

    private String validarImagem(MultipartFile arquivo)
            throws IOException {

        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Selecione uma imagem.");
        }

        if (arquivo.getSize() > TAMANHO_MAXIMO) {
            throw new IllegalArgumentException(
                    "A imagem deve ter no máximo 5 MB.");
        }

        byte[] bytes = arquivo.getBytes();

        boolean png = bytes.length >= 8
                && (bytes[0] & 0xFF) == 0x89
                && (bytes[1] & 0xFF) == 0x50
                && (bytes[2] & 0xFF) == 0x4E
                && (bytes[3] & 0xFF) == 0x47
                && (bytes[4] & 0xFF) == 0x0D
                && (bytes[5] & 0xFF) == 0x0A
                && (bytes[6] & 0xFF) == 0x1A
                && (bytes[7] & 0xFF) == 0x0A;

        boolean jpeg = bytes.length >= 3
                && (bytes[0] & 0xFF) == 0xFF
                && (bytes[1] & 0xFF) == 0xD8
                && (bytes[2] & 0xFF) == 0xFF;

        if (!png && !jpeg) {
            throw new IllegalArgumentException(
                    "Formato inválido. Envie apenas PNG ou JPG.");
        }

        return png ? "image/png" : "image/jpeg";
    }

    // Cadastro existente de imagem do animal
    @Transactional
    public Imagem salvar(ImagemDTO dto, MultipartFile arquivo)
            throws Exception {

        Animal animal = buscarAnimal(dto.getIdAnimal());

        String contentType = validarImagem(arquivo);

        Imagem imagem = new Imagem();
        imagem.setNome(dto.getNome());
        imagem.setTipo(dto.getTipo());
        imagem.setAnimal(animal);
        imagem.setCliente(null);
        imagem.setArquivo(arquivo.getBytes());
        imagem.setContentType(contentType);

        return imagemRepository.save(imagem);
    }

    // Novo: cadastrar foto pessoal do tutor
    @Transactional
    public Imagem salvarImagemTutor(
            Integer idCliente,
            String nome,
            String tipo,
            MultipartFile arquivo) throws Exception {

        Cliente cliente = buscarCliente(idCliente);

        String contentType = validarImagem(arquivo);

        Imagem imagem = new Imagem();
        imagem.setNome(nome);
        imagem.setTipo(tipo);
        imagem.setCliente(cliente);
        imagem.setAnimal(null);
        imagem.setArquivo(arquivo.getBytes());
        imagem.setContentType(contentType);

        return imagemRepository.save(imagem);
    }

    // Novo: substituir uma imagem existente
    @Transactional
    public Imagem substituirArquivo(
            Integer idImagem,
            MultipartFile novoArquivo) throws Exception {

        Imagem imagem = imagemRepository.findById(idImagem)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException(
                        "Imagem não encontrada."));

        String contentType = validarImagem(novoArquivo);

        imagem.setArquivo(novoArquivo.getBytes());
        imagem.setContentType(contentType);

        return imagemRepository.save(imagem);
    }

    public List<Imagem> listarTodos() {
        return imagemRepository.findAll();
    }

    public Optional<Imagem> buscarPorId(int id) {
        return imagemRepository.findById(id);
    }

    // Método antigo preservado
    @Transactional
    public Optional<Imagem> atualizar(int id, ImagemDTO dto) {

        return imagemRepository.findById(id)
                .map(imagemExistente -> {

                    Animal animal = buscarAnimal(dto.getIdAnimal());

                    imagemExistente.setNome(dto.getNome());
                    imagemExistente.setTipo(dto.getTipo());
                    imagemExistente.setAnimal(animal);
                    imagemExistente.setCliente(null);

                    return imagemRepository.save(imagemExistente);
                });
    }

    public void excluir(int id) {
        imagemRepository.deleteById(id);
    }
}
