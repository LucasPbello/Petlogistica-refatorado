package com.petlogistica.service;

import com.petlogistica.exception.RecursoNaoEncontradoException;
import com.petlogistica.model.Cliente;
import com.petlogistica.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(int id) {
        return clienteRepository.findById(id)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    public Optional<Cliente> atualizar(int id, Cliente cliente) {

        return clienteRepository.findById(id)
                .map(clienteExistente -> {

                    clienteExistente.setNome(cliente.getNome());
                    clienteExistente.setCpf(cliente.getCpf());
                    clienteExistente.setEmail(cliente.getEmail());
                    clienteExistente.setTelefone(cliente.getTelefone());

                    return clienteRepository.save(clienteExistente);
                });
    }

    public void excluir(int id) {
        clienteRepository.deleteById(id);
    }
}
