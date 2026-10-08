
# Plano de Testes — PETCARGO

## 1. Objetivo

Este documento apresenta o plano de testes do sistema PETCARGO,
desenvolvido para o gerenciamento de clientes, endereços, animais,
documentos e imagens.

O objetivo é verificar se as principais funcionalidades do sistema
funcionam conforme os requisitos definidos.

## 2. Tipos de testes

Serão utilizados dois tipos principais de testes:

- Testes unitários utilizando JUnit;
- Testes manuais das funcionalidades do sistema.

Os testes unitários serão utilizados principalmente para validar regras
de negócio que não dependem diretamente do banco de dados.

Os testes manuais serão utilizados para verificar o funcionamento
completo das funcionalidades da aplicação.

## 3. Testes unitários

### TU01 — Salvar animal com cliente existente

**Objetivo:** verificar se um animal é salvo quando o cliente informado
existe no sistema.

**Resultado esperado:** o cliente é localizado e o animal é salvo
corretamente.

**Resultado:** Aprovado.

### TU02 — Tentar salvar animal com cliente inexistente

**Objetivo:** verificar o comportamento do sistema quando o animal é
associado a um cliente que não existe.

**Resultado esperado:** o sistema deve lançar uma
`RecursoNaoEncontradoException` e o animal não deve ser salvo.

**Resultado:** Aprovado.

## 4. Testes manuais