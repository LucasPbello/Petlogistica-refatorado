package com.petlogistica.service;

import com.petlogistica.exception.RecursoNaoEncontradoException;
import com.petlogistica.model.Animal;
import com.petlogistica.model.Cliente;
import com.petlogistica.repository.AnimalRepository;
import com.petlogistica.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private AnimalService animalService;

    @Test
    void deveSalvarAnimalComClienteExistente() {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1);

        Animal animal = new Animal();
        animal.setCliente(cliente);

        when(clienteRepository.findById(1))
                .thenReturn(Optional.of(cliente));

        when(animalRepository.save(animal))
                .thenReturn(animal);

        Animal resultado = animalService.salvar(animal);

        assertEquals(animal, resultado);

        verify(clienteRepository).findById(1);
        verify(animalRepository).save(animal);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistir() {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(99);

        Animal animal = new Animal();
        animal.setCliente(cliente);

        when(clienteRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> animalService.salvar(animal)
        );

        verify(clienteRepository).findById(99);

        verify(animalRepository, never()).save(any(Animal.class));
    }
}
