package com.petlogistica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Lob;

@Entity
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idImagem;

    private String nome;
    private String tipo;
    private String contentType;

    @Lob
    private byte[] arquivo;

    @ManyToOne
    private Animal animal;

    public Imagem() {
    }

    public Imagem(Integer idImagem, String nome, String tipo, String contentType, byte[] arquivo, Animal animal) {
        this.idImagem = idImagem;
        this.nome = nome;
        this.tipo = tipo;
        this.contentType = contentType;
        this.arquivo = arquivo;
        this.animal = animal;
    }

    public Integer getIdImagem() {
        return idImagem;
    }

    public void setIdImagem(Integer idImagem) {
        this.idImagem = idImagem;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
}
