package com.petlogistica.service;

import com.petlogistica.model.Animal;
import com.petlogistica.model.Cliente;
import com.petlogistica.repository.AnimalRepository;
import com.petlogistica.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import com.petlogistica.exception.RecursoNaoEncontradoException;
import com.petlogistica.repository.DocumentoRepository;
import com.petlogistica.repository.ImagemRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ClienteRepository clienteRepository;
    private final DocumentoRepository documentoRepository;
    private final ImagemRepository imagemRepository;

    public AnimalService(
            AnimalRepository animalRepository,
            ClienteRepository clienteRepository,
            DocumentoRepository documentoRepository,
            ImagemRepository imagemRepository) {

        this.animalRepository = animalRepository;
        this.clienteRepository = clienteRepository;
        this.documentoRepository = documentoRepository;
        this.imagemRepository = imagemRepository;
    }

    public Animal salvar(Animal animal) {

        if (animal.getCliente() != null
                && animal.getCliente().getIdCliente() != null) {

            Cliente cliente = buscarCliente(
                    animal.getCliente().getIdCliente()
            );

            animal.setCliente(cliente);
        }

        return animalRepository.save(animal);
    }

    private Cliente buscarCliente(Integer idCliente) {
        return clienteRepository
                .findById(idCliente)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    public List<Animal> listarTodos() {
        return animalRepository.findAll();
    }

    public List<Animal> listarPorCliente(Integer idCliente) {
        return animalRepository.findByCliente_IdCliente(idCliente);
    }

    public Animal buscarPorId(int id) {
        return animalRepository.findById(id)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException("Animal não encontrado"));
    }

    public Optional<Animal> atualizar(int id, Animal animal) {

        return animalRepository.findById(id)
                .map(animalExistente -> {

                    animalExistente.setNome(animal.getNome());
                    animalExistente.setEspecie(animal.getEspecie());
                    animalExistente.setRaca(animal.getRaca());
                    animalExistente.setSexo(animal.getSexo());
                    animalExistente.setCor(animal.getCor());
                    animalExistente.setPeso(animal.getPeso());
                    animalExistente.setTamanho(animal.getTamanho());
                    animalExistente.setDataNascimento(animal.getDataNascimento());

                    if (animal.getCliente() != null
                            && animal.getCliente().getIdCliente() != null) {

                        Cliente cliente = buscarCliente(
                                animal.getCliente().getIdCliente()
                        );

                        animalExistente.setCliente(cliente);
                    }

                    return animalRepository.save(animalExistente);
                });
    }

    @Transactional
    public void excluir(int id) {

        // Verifica se o pet existe.
        Animal animal = buscarPorId(id);

        // Localiza os documentos associados ao pet.
        var documentos = documentoRepository
                .findByAnimal_IdAnimal(id);

        // Localiza as fotos associadas ao pet.
        var imagens = imagemRepository
                .findByAnimal_IdAnimal(id);

        // Remove os arquivos relacionados.
        documentoRepository.deleteAll(documentos);
        imagemRepository.deleteAll(imagens);

        // Executa as exclusões antes de remover o pet.
        documentoRepository.flush();
        imagemRepository.flush();

        // Remove o pet.
        animalRepository.delete(animal);
    }
}
