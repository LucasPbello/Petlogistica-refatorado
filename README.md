# 💻 PetLogistica-refatorado

Projeto desenvolvido para o curso Técnico em Desenvolvimento de Sistemas do SENAC EAD.

## 🏦 Título do projeto

🧱 PetLogística


## 📘 Desenvolvimento

Este projeto representa uma nova versão refatorada do sistema PetLogística, desenvolvido anteriormente utilizando Java Swing.

A proposta desta etapa é analisar o projeto anterior e reorganizar suas funcionalidades, aplicando princípios de programação orientada a objetos, SOLID e separação de responsabilidades.


### Objetivos

✅ Refatorar partes do projeto integrador anterior;

✅ Separar as responsabilidades das classes;

✅ Aplicar principalmente o princípio SRP (Single Responsibility Principle);

✅ Criar uma arquitetura em camadas utilizando Controller, Service e Repository;

✅ Substituir os antigos DAOs por Spring Data JPA;

✅ Utilizar MySQL para persistência dos dados;

✅ Criar uma API REST para as funcionalidades do sistema;

✅ Criar DTOs para separar os dados recebidos pela API das entidades;

✅ Implementar tratamento de exceções;

✅ Implementar cadastro, consulta, atualização e exclusão de clientes;

✅ Implementar cadastro, consulta, atualização e exclusão de animais;

✅ Implementar gerenciamento de endereços dos clientes;

✅ Permitir clientes sem endereço;

✅ Permitir múltiplos endereços para um mesmo cliente;

✅ Implementar gerenciamento de documentos relacionados aos animais;

✅ Implementar gerenciamento de imagens relacionadas aos animais;

✅ Implementar upload e download de arquivos;

✅ Realizar testes utilizando o Postman;

✅ Criar testes por meio de uma classe com método `main()`;

✅ Utilizar Git e GitHub para versionamento do projeto.

🚧 Projeto em desenvolvimento.


## 🏗️ Arquitetura

A aplicação foi organizada utilizando uma arquitetura em camadas:
```text
Controller
     ↓
Service
     ↓
Repository
     ↓
MySQL
```

## 📂 Estrutura do projeto

**Model**

Representa os dados e entidades do sistema:

- Cliente
- Endereco
- Animal
- Documento
- Imagem

**Controller**

Recebe as requisições da API e retorna as respostas para o usuário.

**Service**

Responsável pelas regras de negócio e pelas operações realizadas no sistema.

**Repository**

Responsável pelo acesso ao banco de dados utilizando Spring Data JPA.

**DTO**

Utilizado para transportar informações específicas entre a API e o sistema.

**Exception**

Responsável pelo tratamento de erros, como quando um cliente ou animal não é encontrado.

**Test**

Contém a classe TestePetLogistica, responsável por executar testes através do método main().


## 🐾 Funcionalidades implementadas

### 👤 Cliente

✅ Cadastro;

✅ Consulta;

✅ Atualização;

✅ Exclusão;

✅ Tratamento de cliente não encontrado;

✅ Associação com múltiplos endereços;

✅ Cadastro de cliente sem endereço;

✅ Adição de endereço posteriormente;

✅ Exclusão de um endereço específico.


### 🏠 Endereço

✅ Cadastro de endereços vinculados aos clientes;

✅ Consulta de endereços;

✅ Atualização de endereços;

✅ Exclusão de endereços;

✅ Permite que um cliente possua múltiplos endereços;

✅ Permite cadastrar o cliente sem endereço e adicioná-lo posteriormente.


### 🐶 Animal

✅ Cadastro;

✅ Consulta;

✅ Atualização;

✅ Exclusão;

✅ Associação com cliente;

✅ Tratamento de animal não encontrado.


### 📄 Documento

✅ Cadastro;

✅ Consulta;

✅ Atualização;

✅ Exclusão;

✅ Upload de arquivos PDF;

✅ Download dos arquivos.


### 🖼️ Imagem

✅ Cadastro;

✅ Consulta;

✅ Atualização;

✅ Exclusão;

✅ Upload de imagens;

✅ Validação de arquivos PNG e JPG;

✅ Identificação automática do tipo de imagem;

✅ Visualização e download das imagens.


## 🧠 SOLID

O projeto aplica principalmente o princípio:

### SRP - Single Responsibility Principle

As responsabilidades foram separadas entre diferentes camadas da aplicação.

Os Controllers são responsáveis pelas requisições HTTP, os Services pelas regras e operações da aplicação e os Repositories pela persistência dos dados.

Também foram utilizados conceitos relacionados a outros princípios
SOLID, como injeção de dependências e utilização de interfaces.


## 🔨 Refatorações realizadas

🔹 Separação das responsabilidades anteriormente concentradas nas telas Swing;

🔹 Substituição dos antigos DAOs por Spring Data JPA;

🔹 Criação da camada Service;

🔹 Criação da camada Controller;

🔹 Criação de DTOs;

🔹 Separação de documentos e imagens da entidade Animal;

🔹 Criação de tratamento global de exceções;

🔹 Criação da exceção RecursoNaoEncontradoException;

🔹 Extração de responsabilidades relacionadas ao acesso ao banco;

🔹 Utilização de Integer para IDs gerados pelo banco;

🔹 Validação dos formatos de imagens;

🔹 Criação de endpoints específicos para arquivos;

🔹 Melhoria das respostas da API para recursos não encontrados;

🔹 Retorno de mensagens de sucesso nas operações de exclusão;

🔹 Separação do gerenciamento de endereços da atualização básica do cliente;

🔹 Permissão para clientes sem endereço;

🔹 Permissão para múltiplos endereços por cliente;

🔹 Criação de operação específica para adicionar endereço posteriormente;

🔹 Criação de operação específica para excluir um endereço;

🔹 Uso de @Transactional na operação de associação de endereço ao cliente;

🔹 Remoção do cadastro independente de endereço como fluxo principal da aplicação.


## 🧪 Testes

Os endpoints da API foram testados utilizando o Postman.

Também foi criada a classe TestePetLogistica, que realiza testes utilizando o método main().

Foram testadas operações de:

✅ CRUD de clientes;

✅ CRUD de animais;

✅ Gerenciamento de endereços;

✅ Associação entre cliente e endereço;

✅ Associação entre cliente e animal;

✅ Tratamento de recursos inexistentes;

✅ Upload de documentos;

✅ Download de documentos;

✅ Upload de imagens;

✅ Download de imagens;

✅ Validação de formatos de imagem.


## 🗃️ Banco de dados

O projeto utiliza:

🪛 MySQL

A persistência dos dados é realizada utilizando:

🪛 Spring Data JPA

🪛 Hibernate


## ⚒️ Tecnologias utilizadas

🪛 Java 26;

🪛 Spring Boot 4.1.1;

🪛 Spring Web;

🪛 Spring Data JPA;

🪛 MySQL;

🪛 Maven;

🪛 NetBeans;

🪛 Postman;

🪛 Git;

🪛 GitHub.


## 📦 Dependências utilizadas

🪛 Spring Web;

🪛 Spring Data JPA;

🪛 MySQL Driver.


## 🔄 Projeto original

Este projeto foi desenvolvido a partir da análise e refatoração de partes do projeto PetLogística original.

O projeto original foi desenvolvido utilizando Java Swing e possuía responsabilidades concentradas em classes de interface, acesso ao banco de dados e regras de negócio.

A nova versão busca separar essas responsabilidades e preparar o sistema para uma arquitetura web mais organizada, reutilizável e de fácil manutenção.


## 🚧 Próximas etapas

O projeto continua em desenvolvimento.

Algumas funcionalidades existentes no projeto original ainda poderão ser migradas e refatoradas em etapas futuras, como:

⏳ Vacinas;

⏳ Outras funcionalidades do sistema original.


## 🧗‍♂️ Desenvolvedor

📍 Lucas Bello

🎓 Projeto acadêmico - SENAC EAD
