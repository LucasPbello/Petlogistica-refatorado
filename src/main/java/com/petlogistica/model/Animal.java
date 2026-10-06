package com.petlogistica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAnimal;

    private String nome;
    private String especie;
    private String raca;
    private String sexo;
    private String cor;
    private double peso;
    private double tamanho;
    private int caixaTransporte;
    private LocalDate dataNascimento;

    @ManyToOne
    private Cliente cliente;

    public Animal() {
    }

    public Animal(Integer idAnimal, String nome, String especie, String raca,
            String sexo, String cor, double peso, double tamanho,
            int caixaTransporte, LocalDate dataNascimento) {
        this.idAnimal = idAnimal;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.sexo = sexo;
        this.cor = cor;
        this.peso = peso;
        this.tamanho = tamanho;
        this.caixaTransporte = caixaTransporte;
        this.dataNascimento = dataNascimento;
    }

    public Integer getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getTamanho() {
        return tamanho;
    }

    public void setTamanho(double tamanho) {
        this.tamanho = tamanho;
    }

    public int getCaixaTransporte() {
        return caixaTransporte;
    }

    public void setCaixaTransporte(int caixaTransporte) {
        this.caixaTransporte = caixaTransporte;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
