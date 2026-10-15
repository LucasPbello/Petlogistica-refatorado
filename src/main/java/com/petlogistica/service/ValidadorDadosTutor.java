package com.petlogistica.service;

public final class ValidadorDadosTutor {

    private ValidadorDadosTutor() {
    }

    public static String validarCpf(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            return null;
        }

        String numeros = cpf.replaceAll("\\D", "");

        if (!numeros.matches("\\d{11}")) {
            throw new IllegalArgumentException(
                    "O CPF deve conter 11 números.");
        }

        if (numeros.chars().distinct().count() == 1) {
            throw new IllegalArgumentException(
                    "CPF inválido.");
        }

        for (int digito = 9; digito < 11; digito++) {

            int soma = 0;

            for (int i = 0; i < digito; i++) {
                soma += Character.getNumericValue(
                        numeros.charAt(i)) * (digito + 1 - i);
            }

            int resto = (soma * 10) % 11;

            if (resto == 10) {
                resto = 0;
            }

            if (resto != Character.getNumericValue(
                    numeros.charAt(digito))) {
                throw new IllegalArgumentException(
                        "CPF inválido. Verifique os números informados.");
            }
        }

        return numeros;
    }

    public static String validarTelefone(String telefone) {

        if (telefone == null || telefone.isBlank()) {
            return null;
        }

        String numeros = telefone.replaceAll("\\D", "");

        if (!numeros.matches("\\d{10,11}")) {
            throw new IllegalArgumentException(
                    "Informe um telefone com DDD e 10 ou 11 números.");
        }

        int ddd = Integer.parseInt(numeros.substring(0, 2));

        if (ddd < 11 || ddd > 99
                || ddd % 10 == 0
                || (ddd >= 20 && ddd <= 20)
                || (ddd >= 30 && ddd <= 30)) {
            throw new IllegalArgumentException(
                    "DDD inválido.");
        }

        if (numeros.length() == 11
                && numeros.charAt(2) != '9') {
            throw new IllegalArgumentException(
                    "O celular deve começar com 9 após o DDD.");
        }

        if (numeros.length() == 10
                && numeros.charAt(2) != '2'
                && numeros.charAt(2) != '3'
                && numeros.charAt(2) != '4'
                && numeros.charAt(2) != '5') {
            throw new IllegalArgumentException(
                    "Número de telefone fixo inválido.");
        }

        return numeros;
    }
}
