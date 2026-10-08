package com.petlogistica.controller;

import com.petlogistica.model.Cliente;
import com.petlogistica.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.petlogistica.model.Endereco;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> salvar(
            @RequestBody Cliente cliente) {

        return ResponseEntity.ok(clienteService.salvar(cliente));
    }

    @PostMapping("/{id}/enderecos")
    public ResponseEntity<Cliente> adicionarEndereco(
            @PathVariable Integer id,
            @RequestBody Endereco endereco) {

        return ResponseEntity.ok(
                clienteService.adicionarEndereco(id, endereco)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(
            @PathVariable Integer id,
            @RequestBody Cliente cliente) {

        return ResponseEntity.ok(
                clienteService.atualizar(id, cliente));
    }

    @DeleteMapping("/{idCliente}/enderecos/{idEndereco}")
    public ResponseEntity<String> excluirEndereco(
            @PathVariable Integer idCliente,
            @PathVariable Integer idEndereco) {

        clienteService.removerEndereco(idCliente, idEndereco);

        return ResponseEntity.ok("Endereço excluído com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(
            @PathVariable Integer id) {

        clienteService.excluir(id);

        return ResponseEntity.ok(
                "Cliente excluído com sucesso.");
    }
}
