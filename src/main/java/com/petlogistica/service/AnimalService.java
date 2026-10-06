package com.petlogistica.service;

import com.petlogistica.model.Animal;
import com.petlogistica.model.Cliente;
import com.petlogistica.repository.AnimalRepository;
import com.petlogistica.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import com.petlogistica.exception.RecursoNaoEncontradoException;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ClienteRepository clienteRepository;

    public AnimalService(AnimalRepository animalRepository,
            ClienteRepository clienteRepository) {
        this.animalRepository = animalRepository;
        this.clienteRepository = clienteRepository;
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
                    animalExistente.setCaixaTransporte(animal.getCaixaTransporte());
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

    public void excluir(int id) {
        animalRepository.deleteById(id);
    }
}
