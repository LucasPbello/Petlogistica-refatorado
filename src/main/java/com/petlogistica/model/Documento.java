package com.petlogistica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Lob;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDocumento;

    private String nome;
    private String tipo;

    @JsonIgnore
    @Lob
    private byte[] arquivo;

    @ManyToOne
    private Animal animal;

    @ManyToOne
    private Cliente cliente;

    public Documento() {
    }

    public Documento(Integer idDocumento, String nome, String tipo, byte[] arquivo, Animal animal) {
        this.idDocumento = idDocumento;
        this.nome = nome;
        this.tipo = tipo;
        this.arquivo = arquivo;
        this.animal = animal;
    }

    public Integer getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
