package com.petlogistica.test;

import com.petlogistica.model.Animal;
import com.petlogistica.model.Cliente;
import com.petlogistica.service.AnimalService;
import com.petlogistica.service.ClienteService;
import com.petlogistica.service.EnderecoService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.petlogistica.exception.RecursoNaoEncontradoException;
import com.petlogistica.model.Endereco;

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

        EnderecoService enderecoService
                = contexto.getBean(EnderecoService.class);

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

        Cliente clienteAtualizado = clienteService.atualizar(
                clienteSalvo.getIdCliente(),
                clienteEncontrado
        );

        if (clienteAtualizado != null
                && clienteAtualizado.getNome()
                        .equals("Cliente Teste Atualizado")) {

            System.out.println("OK - Cliente atualizado com sucesso.");
        } else {
            System.out.println("ERRO - Não foi possível atualizar o cliente.");
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

        // TESTE 6 - Adicionar endereço ao cliente
        Endereco endereco = new Endereco(
                null,
                "01001-000",
                "Casa",
                "Praça da Sé",
                "100",
                "Apto 10",
                "Sé",
                "São Paulo",
                "SP"
        );

        clienteSalvo = clienteService.adicionarEndereco(
                clienteSalvo.getIdCliente(),
                endereco
        );

        if (clienteSalvo.getEnderecos().size() == 1
                && clienteSalvo.getEnderecos().get(0).getDescricao().equals("Casa")) {

            System.out.println("TESTE 6 - Adicionar endereço: OK");

        } else {

            System.out.println("TESTE 6 - Adicionar endereço: FALHOU");
        }

        // TESTE 7 - Adicionar segundo endereço ao mesmo cliente
        Endereco endereco2 = new Endereco(
                null,
                "01310-100",
                "Trabalho",
                "Avenida Paulista",
                "1500",
                "Sala 10",
                "Bela Vista",
                "São Paulo",
                "SP"
        );

        clienteSalvo = clienteService.adicionarEndereco(
                clienteSalvo.getIdCliente(),
                endereco2
        );

        if (clienteSalvo.getEnderecos().size() == 2
                && clienteSalvo.getEnderecos().get(0).getDescricao().equals("Casa")
                && clienteSalvo.getEnderecos().get(1).getDescricao().equals("Trabalho")) {

            System.out.println("TESTE 7 - Múltiplos endereços: OK");

        } else {

            System.out.println("TESTE 7 - Múltiplos endereços: FALHOU");
        }

        // TESTE 8 - Criar cliente sem endereço
        Cliente clienteSemEndereco = new Cliente();

        clienteSemEndereco.setNome("Cliente Sem Endereço");
        clienteSemEndereco.setCpf("99988877766");
        clienteSemEndereco.setEmail("semendereco@email.com");
        clienteSemEndereco.setTelefone("11977776666");

        Cliente clienteSemEnderecoSalvo = clienteService.salvar(clienteSemEndereco);

        if (clienteSemEnderecoSalvo.getIdCliente() != null
                && clienteSemEnderecoSalvo.getEnderecos().isEmpty()) {

            System.out.println("TESTE 8 - Cliente sem endereço: OK");

        } else {

            System.out.println("TESTE 8 - Cliente sem endereço: FALHOU");
        }

        // TESTE 9 - Adicionar endereço posteriormente
        Endereco enderecoPosterior = new Endereco(
                null,
                "04567-000",
                "Casa",
                "Rua das Flores",
                "250",
                "",
                "Vila Olímpia",
                "São Paulo",
                "SP"
        );

        Cliente clienteComEndereco = clienteService.adicionarEndereco(
                clienteSemEnderecoSalvo.getIdCliente(),
                enderecoPosterior
        );

        if (clienteComEndereco.getEnderecos().size() == 1
                && clienteComEndereco.getEnderecos().get(0).getDescricao().equals("Casa")) {

            System.out.println("TESTE 9 - Adicionar endereço posteriormente: OK");

        } else {

            System.out.println("TESTE 9 - Adicionar endereço posteriormente: FALHOU");
        }

        // TESTE 10 - Excluir endereço
        Integer idEnderecoExcluir
                = clienteComEndereco.getEnderecos().get(0).getIdEndereco();

        try {

            clienteService.removerEndereco(
                    clienteComEndereco.getIdCliente(),
                    idEnderecoExcluir
            );

            enderecoService.buscarPorId(idEnderecoExcluir);

            System.out.println(
                    "TESTE 10 - Excluir endereço: FALHOU"
            );

        } catch (RecursoNaoEncontradoException e) {

            System.out.println(
                    "TESTE 10 - Excluir endereço: OK"
            );
        }

        System.out.println("=================================");
        System.out.println(" TESTES FINALIZADOS");
        System.out.println("=================================");

        contexto.close();
    }
}
