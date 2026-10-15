package com.petlogistica.service;

import com.petlogistica.exception.RecursoNaoEncontradoException;
import com.petlogistica.model.Cliente;
import com.petlogistica.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import com.petlogistica.model.Endereco;
import com.petlogistica.repository.EnderecoRepository;
import org.springframework.transaction.annotation.Transactional;
import com.petlogistica.model.Usuario;
import com.petlogistica.repository.UsuarioRepository;
import com.petlogistica.repository.AnimalRepository;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AnimalRepository animalRepository;

    public ClienteService(
            ClienteRepository clienteRepository,
            EnderecoRepository enderecoRepository,
            UsuarioRepository usuarioRepository,
            AnimalRepository animalRepository) {

        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.usuarioRepository = usuarioRepository;
        this.animalRepository = animalRepository;
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

    @Transactional
    public void excluir(Integer id) {

        Cliente cliente = buscarPorId(id);

        if (!animalRepository.findByCliente_IdCliente(id).isEmpty()) {
            throw new IllegalArgumentException(
                    "Não é possível excluir este tutor enquanto houver "
                    + "pets vinculados. Transfira os pets primeiro.");
        }

        usuarioRepository.findByCliente_IdCliente(id)
                .ifPresent(usuarioRepository::delete);

        clienteRepository.delete(cliente);
    }

    @Transactional
    public Cliente atualizarDadosTutor(
            Integer idCliente,
            String nome,
            String email,
            String cpf,
            String telefone) {

        if (nome == null || nome.isBlank()
                || email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Nome e e-mail são obrigatórios.");
        }

        String novoEmail = email.trim().toLowerCase();
        String novoNome = nome.trim();

        Cliente cliente = buscarPorId(idCliente);

        Usuario usuario = usuarioRepository
                .findByCliente_IdCliente(idCliente)
                .orElseThrow(() -> new IllegalArgumentException(
                "Conta do tutor não encontrada."));

        if (!novoEmail.equalsIgnoreCase(usuario.getEmail())) {

            if (usuarioRepository.existsByEmailIgnoreCase(novoEmail)
                    || clienteRepository.existsByEmailIgnoreCase(novoEmail)
                    || usuarioRepository.existsByNomeUsuarioIgnoreCase(novoEmail)) {
                throw new IllegalArgumentException(
                        "Este e-mail já está cadastrado.");
            }
        }

        String cpfNormalizado
                = ValidadorDadosTutor.validarCpf(cpf);

        String telefoneNormalizado
                = ValidadorDadosTutor.validarTelefone(telefone);

        if (cpfNormalizado != null) {

            var clienteComCpf
                    = clienteRepository.findByCpf(cpfNormalizado);

            if (clienteComCpf.isPresent()
                    && !clienteComCpf.get().getIdCliente()
                            .equals(idCliente)) {

                throw new IllegalArgumentException(
                        "Este CPF já está cadastrado.");
            }
        }

        cliente.setNome(novoNome);
        cliente.setEmail(novoEmail);
        cliente.setCpf(cpfNormalizado);
        cliente.setTelefone(telefoneNormalizado);

        usuario.setEmail(novoEmail);

        clienteRepository.save(cliente);
        usuarioRepository.save(usuario);

        return cliente;
    }

}
