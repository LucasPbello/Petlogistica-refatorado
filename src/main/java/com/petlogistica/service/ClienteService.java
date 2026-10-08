package com.petlogistica.service;

import com.petlogistica.exception.RecursoNaoEncontradoException;
import com.petlogistica.model.Cliente;
import com.petlogistica.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import com.petlogistica.model.Endereco;
import com.petlogistica.repository.EnderecoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;

    public ClienteService(
            ClienteRepository clienteRepository,
            EnderecoRepository enderecoRepository) {

        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado"));
    }

    public Cliente atualizar(Integer id, Cliente dados) {

        Cliente cliente = buscarPorId(id);

        cliente.setNome(dados.getNome());
        cliente.setCpf(dados.getCpf());
        cliente.setEmail(dados.getEmail());
        cliente.setTelefone(dados.getTelefone());

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente adicionarEndereco(Integer idCliente, Endereco endereco) {
        Cliente cliente = buscarPorId(idCliente);

        cliente.getEnderecos().add(endereco);

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void removerEndereco(Integer idCliente, Integer idEndereco) {

        Cliente cliente = buscarPorId(idCliente);

        Endereco endereco = enderecoRepository.findById(idEndereco)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException("Endereço não encontrado"));

        cliente.getEnderecos().remove(endereco);

        clienteRepository.save(cliente);
        enderecoRepository.delete(endereco);
    }

    public void excluir(Integer id) {

        Cliente cliente = buscarPorId(id);

        clienteRepository.delete(cliente);
    }
}
