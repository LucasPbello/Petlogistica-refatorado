package com.petlogistica.test;

import com.petlogistica.model.Animal;
import com.petlogistica.model.Cliente;
import com.petlogistica.service.AnimalService;
import com.petlogistica.service.ClienteService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.petlogistica.exception.RecursoNaoEncontradoException;


public class TestePetLogistica {

    public static void main(String[] args) {

        ConfigurableApplicationContext contexto
                = SpringApplication.run(
                        com.petlogistica.PetlogisticaApplication.class,
                        args
                );

        ClienteService clienteService
                = contexto.getBean(ClienteService.class);

        AnimalService animalService
                = contexto.getBean(AnimalService.class);

        System.out.println("=================================");
        System.out.println(" TESTES PETLOGISTICA");
        System.out.println("=================================");

        // TESTE 1 - Criar cliente
        Cliente cliente = new Cliente();

        cliente.setNome("Cliente Teste");
        cliente.setCpf("11122233344");
        cliente.setEmail("teste@email.com");
        cliente.setTelefone("11999999999");

        Cliente clienteSalvo = clienteService.salvar(cliente);

        if (clienteSalvo.getIdCliente() != null) {
            System.out.println("TESTE 1 - Criar cliente: OK");
        } else {
            System.out.println("TESTE 1 - Criar cliente: FALHOU");
        }

        // TESTE 2 - Buscar cliente
        Cliente clienteEncontrado
                = clienteService.buscarPorId(clienteSalvo.getIdCliente());

        if (clienteEncontrado != null) {
            System.out.println("TESTE 2 - Buscar cliente: OK");
        } else {
            System.out.println("TESTE 2 - Buscar cliente: FALHOU");
        }

        // TESTE 3 - Atualizar cliente
        clienteEncontrado.setNome("Cliente Teste Atualizado");

        Cliente clienteAtualizado
                = clienteService.atualizar(
                        clienteSalvo.getIdCliente(),
                        clienteEncontrado
                ).orElse(null);

        if (clienteAtualizado != null
                && clienteAtualizado.getNome()
                        .equals("Cliente Teste Atualizado")) {

            System.out.println("TESTE 3 - Atualizar cliente: OK");

        } else {
            System.out.println("TESTE 3 - Atualizar cliente: FALHOU");
        }

        // TESTE 4 - Criar animal associado ao cliente
        Animal animal = new Animal();

        animal.setNome("Animal Teste");
        animal.setEspecie("Cachorro");
        animal.setRaca("SRD");
        animal.setSexo("Macho");
        animal.setCor("Preto");
        animal.setPeso(10);
        animal.setTamanho(40);
        animal.setCaixaTransporte(300);

        animal.setCliente(clienteAtualizado);

        Animal animalSalvo = animalService.salvar(animal);

        if (animalSalvo.getIdAnimal() != null
                && animalSalvo.getCliente() != null) {

            System.out.println("TESTE 4 - Criar animal: OK");

        } else {
            System.out.println("TESTE 4 - Criar animal: FALHOU");
        }

        // TESTE 5 - Cliente inexistente
        try {

            Animal animalTeste = new Animal();

            animalTeste.setNome("Animal Exceção");
            animalTeste.setEspecie("Cachorro");

            Cliente clienteInexistente = new Cliente();
            clienteInexistente.setIdCliente(999999);

            animalTeste.setCliente(clienteInexistente);

            animalService.salvar(animalTeste);

            System.out.println(
                    "TESTE 5 - Cliente inexistente: FALHOU"
            );

        } catch (RecursoNaoEncontradoException e) {

            System.out.println(
                    "TESTE 5 - Cliente inexistente: OK"
            );
        }

        System.out.println("=================================");
        System.out.println(" TESTES FINALIZADOS");
        System.out.println("=================================");

        contexto.close();
    }
}
