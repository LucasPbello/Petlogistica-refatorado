
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

### 4.1 Cliente

#### TM01 — Cadastrar cliente

**Objetivo:** verificar se é possível cadastrar um novo cliente.

**Pré-condição:** sistema em funcionamento.

**Passos:**
1. Acessar a funcionalidade de cadastro de cliente.
2. Informar os dados obrigatórios.
3. Confirmar o cadastro.

**Resultado esperado:** o cliente deve ser cadastrado e seus dados devem
ser armazenados corretamente.

---

#### TM02 — Consultar cliente

**Objetivo:** verificar se os dados de um cliente cadastrado podem ser
consultados.

**Passos:**
1. Acessar a consulta de clientes.
2. Localizar um cliente cadastrado.

**Resultado esperado:** os dados do cliente devem ser apresentados
corretamente.

---

#### TM03 — Alterar cliente

**Objetivo:** verificar a alteração dos dados de um cliente.

**Passos:**
1. Localizar um cliente cadastrado.
2. Alterar seus dados.
3. Salvar as alterações.

**Resultado esperado:** os dados alterados devem ser armazenados e
apresentados corretamente.

---

#### TM04 — Excluir cliente

**Objetivo:** verificar se um cliente pode ser excluído.

**Passos:**
1. Localizar um cliente cadastrado.
2. Solicitar a exclusão.
3. Confirmar a operação.

**Resultado esperado:** o cliente deve ser removido do sistema.

---

### 4.2 Endereço

#### TM05 — Cadastrar endereço

**Objetivo:** verificar se um endereço pode ser cadastrado e associado
corretamente ao cliente.

**Passos:**
1. Selecionar um cliente.
2. Informar os dados do endereço.
3. Salvar o endereço.

**Resultado esperado:** o endereço deve ser cadastrado e associado ao
cliente correto.

---

#### TM06 — Alterar endereço

**Objetivo:** verificar a alteração dos dados de um endereço.

**Passos:**
1. Localizar o endereço cadastrado.
2. Alterar seus dados.
3. Salvar as alterações.

**Resultado esperado:** o endereço deve apresentar os novos dados
corretamente.

---

#### TM07 — Excluir endereço

**Objetivo:** verificar se um endereço pode ser excluído.

**Passos:**
1. Localizar o endereço.
2. Solicitar a exclusão.
3. Confirmar a operação.

**Resultado esperado:** o endereço deve ser removido do sistema.

---

### 4.3 Animal

#### TM08 — Cadastrar animal

**Objetivo:** verificar se um animal pode ser cadastrado.

**Passos:**
1. Acessar o cadastro de animal.
2. Informar os dados do animal.
3. Selecionar o cliente responsável.
4. Confirmar o cadastro.

**Resultado esperado:** o animal deve ser cadastrado e associado ao
cliente selecionado.

---

#### TM09 — Consultar animal

**Objetivo:** verificar se os dados de um animal cadastrado podem ser
consultados.

**Passos:**
1. Acessar a consulta de animais.
2. Localizar um animal cadastrado.

**Resultado esperado:** os dados do animal devem ser apresentados
corretamente, incluindo sua associação com o cliente.

---

#### TM10 — Alterar animal

**Objetivo:** verificar se os dados de um animal podem ser alterados.

**Passos:**
1. Localizar um animal cadastrado.
2. Alterar seus dados.
3. Salvar as alterações.

**Resultado esperado:** os novos dados devem ser armazenados
corretamente.

---

#### TM11 — Excluir animal

**Objetivo:** verificar se um animal pode ser excluído.

**Passos:**
1. Localizar um animal cadastrado.
2. Solicitar a exclusão.
3. Confirmar a operação.

**Resultado esperado:** o animal deve ser removido do sistema.

---

#### TM12 — Cadastrar animal com cliente inexistente

**Objetivo:** verificar o comportamento do sistema ao tentar associar
um animal a um cliente que não existe.

**Passos:**
1. Informar os dados de um animal.
2. Informar um cliente inexistente.
3. Tentar salvar o animal.

**Resultado esperado:** o sistema deve impedir o cadastro e informar que
o cliente não foi encontrado.

---

### 4.4 Documento

#### TM13 — Cadastrar documento

**Objetivo:** verificar se um documento pode ser cadastrado para um
animal.

**Passos:**
1. Selecionar um animal.
2. Informar ou enviar o documento.
3. Confirmar o cadastro.

**Resultado esperado:** o documento deve ser armazenado e associado ao
animal correto.

---

#### TM14 — Consultar documento

**Objetivo:** verificar se um documento cadastrado pode ser consultado
ou recuperado.

**Passos:**
1. Localizar o animal.
2. Acessar seus documentos.
3. Selecionar um documento.

**Resultado esperado:** o documento correto deve ser disponibilizado.

---

#### TM15 — Excluir documento

**Objetivo:** verificar se um documento pode ser removido.

**Passos:**
1. Localizar o documento.
2. Solicitar a exclusão.
3. Confirmar a operação.

**Resultado esperado:** o documento deve ser removido do sistema.

---

### 4.5 Imagem

#### TM16 — Cadastrar imagem

**Objetivo:** verificar se uma imagem pode ser cadastrada para um animal.

**Passos:**
1. Selecionar um animal.
2. Selecionar uma imagem.
3. Confirmar o cadastro.

**Resultado esperado:** a imagem deve ser armazenada e associada ao
animal correto.

---

#### TM17 — Visualizar imagem

**Objetivo:** verificar se uma imagem cadastrada pode ser visualizada.

**Passos:**
1. Localizar o animal.
2. Acessar suas imagens.
3. Selecionar uma imagem.

**Resultado esperado:** a imagem correta deve ser apresentada.

---

#### TM18 — Excluir imagem

**Objetivo:** verificar se uma imagem pode ser removida.

**Passos:**
1. Localizar a imagem.
2. Solicitar a exclusão.
3. Confirmar a operação.

**Resultado esperado:** a imagem deve ser removida do sistema.

---

## 5. Critérios de aprovação

Um teste será considerado aprovado quando o resultado obtido no sistema
corresponder ao resultado esperado definido neste plano.

Os testes unitários devem ser executados sem falhas ou erros.

Os testes manuais devem apresentar o comportamento esperado para cada
funcionalidade.

## 6. Registro dos resultados

Os resultados dos testes manuais deverão ser registrados durante a
execução, indicando se cada caso de teste foi aprovado ou reprovado.

Em caso de reprovação, deverá ser registrada uma descrição do problema
encontrado para posterior correção.

## 7. Conclusão

O plano de testes contempla as principais funcionalidades implementadas
no sistema PETCARGO, abrangendo clientes, endereços, animais, documentos
e imagens.

Também foram definidos testes unitários utilizando JUnit para validar
regras de negócio relacionadas ao cadastro de animais e à associação
com clientes.