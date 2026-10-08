package com.petlogistica.service;

import com.petlogistica.exception.RecursoNaoEncontradoException;
import com.petlogistica.model.Endereco;
import com.petlogistica.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    public Endereco buscarPorId(Integer id) {
        return enderecoRepository.findById(id)
                .orElseThrow(()
                        -> new RecursoNaoEncontradoException(
                        "Endereço não encontrado"));
    }

    public Endereco atualizar(Integer id, Endereco dados) {

        Endereco endereco = buscarPorId(id);

        endereco.setCep(dados.getCep());
        endereco.setLogradouro(dados.getLogradouro());
        endereco.setNumero(dados.getNumero());
        endereco.setComplemento(dados.getComplemento());
        endereco.setBairro(dados.getBairro());
        endereco.setCidade(dados.getCidade());
        endereco.setEstado(dados.getEstado());

        return enderecoRepository.save(endereco);
    }

}
