package com.petlogistica.service;

import com.petlogistica.model.Animal;
import com.petlogistica.model.Imagem;
import com.petlogistica.repository.AnimalRepository;
import com.petlogistica.repository.ImagemRepository;
import org.springframework.stereotype.Service;
import com.petlogistica.dto.ImagemDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import com.petlogistica.exception.RecursoNaoEncontradoException;

@Service
public class ImagemService {

    private final ImagemRepository imagemRepository;
    private final AnimalRepository animalRepository;

    public ImagemService(ImagemRepository imagemRepository,
            AnimalRepository animalRepository) {
        this.imagemRepository = imagemRepository;
        this.animalRepository = animalRepository;
    }

    private Animal buscarAnimal(Integer idAnimal) {
        return animalRepository
                .findById(idAnimal)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException("Animal não encontrado"));
    }

    public Imagem salvar(ImagemDTO dto, MultipartFile arquivo) throws Exception {

        Animal animal = buscarAnimal(dto.getIdAnimal());

        String nomeArquivo = arquivo.getOriginalFilename();

        if (nomeArquivo == null) {
            throw new RuntimeException("Arquivo não informado.");
        }

        String extensao = nomeArquivo.substring(
                nomeArquivo.lastIndexOf(".") + 1
        ).toLowerCase();

        if (!extensao.equals("png")
                && !extensao.equals("jpg")
                && !extensao.equals("jpeg")) {

            throw new RuntimeException(
                    "Formato inválido. Envie apenas PNG ou JPG."
            );
        }

        String contentType = extensao.equals("png")
                ? "image/png"
                : "image/jpeg";

        Imagem imagem = new Imagem();

        imagem.setNome(dto.getNome());
        imagem.setTipo(dto.getTipo());
        imagem.setAnimal(animal);
        imagem.setArquivo(arquivo.getBytes());
        imagem.setContentType(contentType);

        return imagemRepository.save(imagem);
    }

    public List<Imagem> listarTodos() {
        return imagemRepository.findAll();
    }

    public Optional<Imagem> buscarPorId(int id) {
        return imagemRepository.findById(id);
    }

    public Optional<Imagem> atualizar(int id, ImagemDTO dto) {

        return imagemRepository.findById(id)
                .map(imagemExistente -> {

                    Animal animal = buscarAnimal(dto.getIdAnimal());

                    imagemExistente.setNome(dto.getNome());
                    imagemExistente.setTipo(dto.getTipo());
                    imagemExistente.setAnimal(animal);

                    return imagemRepository.save(imagemExistente);
                });
    }

    public void excluir(int id) {
        imagemRepository.deleteById(id);
    }
}
