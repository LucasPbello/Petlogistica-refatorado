# 🐾 PETCARGO --- PetLogística Web

**Projeto Integrador --- Técnico em Desenvolvimento de Sistemas \| SENAC
EAD**

O **PETCARGO** é um sistema web para gerenciamento de tutores, animais,
documentos e imagens. Foi desenvolvido com Java e Spring Boot, a partir
da refatoração do projeto PetLogística original, construído em Java
Swing.

## 📘 Evolução e objetivos

O projeto começou como aplicação desktop Java Swing. A refatoração
buscou aplicar separação de responsabilidades, principalmente o
princípio **SRP (Single Responsibility Principle)**, substituir DAOs por
Spring Data JPA e organizar as classes em camadas.

A evolução incluiu uma API REST, testada com Postman, e posteriormente
uma aplicação web com páginas Thymeleaf, autenticação e controle de
acesso.

### Objetivos alcançados

-   Separar as responsabilidades entre Controller, Service, Repository e
    Model.
-   Utilizar MySQL para persistência dos dados.
-   Implementar operações de cadastro, consulta, alteração e exclusão.
-   Permitir upload e recuperação de documentos e imagens.
-   Criar interfaces web para tutor e administrador.
-   Proteger os dados por autenticação e autorização.
-   Registrar testes, bugs e histórico de alterações no GitHub.

## 🛠️ Tecnologias utilizadas

  Tecnologia                    Utilização
  ----------------------------- ----------------------------------
  Java 26                       Linguagem principal
  Spring Boot 4.1.1             Framework da aplicação
  Spring MVC / Spring Web       Requisições e rotas
  Spring Security               Autenticação e autorização
  Spring Data JPA e Hibernate   Persistência
  MySQL 8                       Banco de dados
  Thymeleaf                     Templates HTML
  HTML5, CSS3 e JavaScript      Interface web
  Maven / Maven Wrapper         Build e dependências
  JUnit                         Testes unitários
  Postman                       Testes de API REST
  NetBeans                      IDE
  Git e GitHub                  Versionamento e registro de bugs

## 🏗️ Arquitetura

A aplicação utiliza uma organização em camadas:

``` text
Navegador (HTML / CSS / JavaScript)
              |
           Thymeleaf
              |
          Controller
              |
            Service
              |
          Repository
              |
             MySQL
```

-   **Model:** entidades e relacionamentos.
-   **Controller:** rotas HTTP e respostas ou páginas.
-   **Service:** regras de negócio, validações e operações.
-   **Repository:** acesso ao banco de dados com Spring Data JPA.
-   **DTO:** transporte de dados específicos.
-   **Exception:** tratamento de erros e recursos inexistentes.
-   **Security:** regras de login e acesso por perfil.
-   **Templates:** páginas HTML renderizadas com Thymeleaf.
-   **Static:** arquivos CSS, JavaScript e recursos públicos.

### Princípio SOLID --- SRP

As responsabilidades que anteriormente estavam concentradas nas telas
Swing foram distribuídas entre as camadas. Controllers recebem
requisições; Services tratam regras de negócio; Repositories cuidam da
persistência. O projeto também utiliza injeção de dependências e
interfaces.

## 🐾 Funcionalidades da versão web

### 👤 Tutor

-   Criar conta com nome, e-mail e senha.
-   Fazer login e logout.
-   Consultar e atualizar seus dados pessoais.
-   Informar CPF e telefone posteriormente, com validação.
-   Cadastrar, consultar, editar e excluir seus próprios animais.
-   Gerenciar documentos PDF e imagens vinculados aos animais.
-   Enviar, visualizar, substituir e excluir arquivos.
-   Utilizar a interface com tema claro ou escuro.

### 🛡️ Administrador

-   Acessar painel administrativo próprio.
-   Listar e gerenciar tutores.
-   Editar e excluir registros de tutores conforme as regras do sistema.
-   Consultar animais vinculados a cada tutor.
-   Gerenciar informações e arquivos de animais nas telas
    administrativas.

### 🐶 Animal

O cadastro inclui nome, espécie, raça, sexo, cor, peso, tamanho e data
de nascimento, com vínculo ao cliente/tutor responsável.

### 📄 Documento

-   Associação de PDFs aos animais.
-   Upload, consulta, download, substituição e exclusão.
-   Validação de formato e tamanho de arquivo.

### 🖼️ Imagem

-   Associação de imagens aos animais.
-   Upload, visualização, download, substituição e exclusão.
-   Validação de formatos PNG e JPG.

## 🔨 Funcionalidades e refatorações anteriores

Na etapa da API REST, foram implementadas e testadas operações de:

-   Cadastro, consulta, atualização e exclusão de clientes.
-   Cadastro, consulta, atualização e exclusão de animais.
-   Associação entre clientes e animais.
-   Gerenciamento de endereços vinculados aos clientes.
-   Cadastro de clientes sem endereço.
-   Associação posterior de endereços e suporte a múltiplos endereços.
-   Cadastro, consulta, atualização e exclusão de documentos e imagens.
-   Upload e download de arquivos.
-   Tratamento de recursos não encontrados.

Essas operações documentam a evolução do projeto; **nem todas possuem
necessariamente uma tela específica na versão web atual**.

### Refatorações realizadas

-   Separação das responsabilidades antes concentradas nas telas Swing.
-   Substituição de DAOs por Spring Data JPA.
-   Criação de Controller, Service e Repository.
-   Criação de DTOs e tratamento de exceções.
-   Separação dos arquivos de documentos e imagens da entidade Animal.
-   Validação dos formatos de imagens.
-   Criação de endpoints específicos de arquivos.
-   Organização de operações de endereço.
-   Uso de transações em operações que exigem consistência.
-   Inclusão de autenticação e autorização na versão web.

## 🔒 Segurança

A aplicação utiliza **Spring Security**, com senhas codificadas por
**BCrypt**.

  Perfil    Acesso
  --------- -----------------------------------------------------
  `TUTOR`   Seus próprios dados, animais e arquivos autorizados
  `ADMIN`   Funcionalidades administrativas

A verificação de propriedade dos animais também é realizada na camada de
serviço. Nos testes manuais, tentativas de acessar páginas de edição e
arquivos de animais pertencentes a outro tutor retornaram **HTTP 403 ---
Forbidden**.

**Observação:** testes de download direto de documentos e imagens
pertencentes a outros tutores não foram executados nessa rodada e não
são apresentados como aprovados.

## 📂 Estrutura do projeto

Estrutura simplificada de referência:

``` text
PETCARGO/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
├── PLANO_DE_TESTES.md
├── evidencias/
└── src/
    ├── main/
    │   ├── java/com/petlogistica/
    │   │   ├── controller/
    │   │   ├── service/
    │   │   ├── repository/
    │   │   ├── model/
    │   │   └── ... (DTO, exceções e segurança)
    │   └── resources/
    │       ├── templates/
    │       ├── static/
    │       │   ├── css/
    │       │   └── js/
    │       └── application.properties
    └── test/java/
```

A organização exata dos pacotes pode variar conforme o código. Entre as
entidades estão **Usuario, Cliente, Endereco, Animal, Documento e
Imagem**.

## 🗃️ Banco de dados

O PETCARGO utiliza **MySQL 8**, com persistência via **Spring Data JPA e
Hibernate**.

Os relacionamentos associam usuários a clientes/tutores, animais aos
responsáveis e documentos e imagens aos animais.

As credenciais de conexão são específicas de cada ambiente e **não devem
ser publicadas no repositório**.

## ▶️ Como executar

### Pré-requisitos

-   JDK 26.
-   MySQL 8 em execução.
-   NetBeans ou IDE compatível.
-   Maven ou Maven Wrapper.

### Passos

1.  Clonar o repositório ou abrir a cópia local.
2.  Configurar o banco MySQL utilizado pela aplicação.
3.  Ajustar localmente as propriedades de conexão do
    `application.properties`, sem publicar credenciais.
4.  Abrir o projeto Maven no NetBeans.
5.  Executar a aplicação Spring Boot pela IDE ou pelo Maven Wrapper.
6.  Acessar `http://localhost:8080` no navegador.

Exemplo de execução no Windows, a partir da pasta do projeto:

``` bash
.\mvnw.cmd spring-boot:run
```

No Git Bash:

``` bash
./mvnw spring-boot:run
```

A execução depende da configuração local do banco de dados. Não há
credenciais padrão documentadas neste README.

## 🧪 Testes

### Testes unitários --- JUnit

-   **TU01:** salvar animal com cliente existente.
-   **TU02:** tentar salvar animal com cliente inexistente.

Os testes foram executados com sucesso no NetBeans/Maven durante a
validação da versão web.

### Testes manuais

Foram realizados testes de autenticação, navegação, gerenciamento de
animais, arquivos e permissões entre tutores.

A versão web também foi validada com cenários de acesso indevido a
páginas de outros tutores, retornando HTTP 403.

O planejamento, os resultados e os casos não executados são documentados
em [`PLANO_DE_TESTES.md`](PLANO_DE_TESTES.md). As capturas de tela ficam
na pasta `evidencias/`.

### Testes anteriores da API REST

Na etapa de refatoração, o **Postman** foi utilizado para testar
operações de clientes, animais, endereços, documentos, imagens, upload,
download e tratamento de erros.

## 🐛 Registro de bugs --- GitHub Issues

Durante o desenvolvimento, foram documentados problemas reais e suas
correções:

1.  **Erro SQL:** coluna `caixa_transporte` permaneceu no banco após
    remoção do atributo da entidade Animal.
2.  **Página Gerenciar Animais:** erro causado pela ausência do template
    na implementação inicial.
3.  **CSS:** chave de fechamento ausente em um bloco `@media`,
    prejudicando a estilização.

As Issues encerradas e suas correções foram registradas como evidências
do Projeto Integrador.

## 🔄 Versionamento

O projeto utiliza Git e GitHub. O histórico de commits documenta as
refatorações, implementação web, testes e correções.

O repositório utilizado é **`Petlogistica-refatorado`**, preservando o
histórico das etapas anteriores.

## 📌 Status e escopo

**Versão web implementada e preparada para entrega acadêmica**, com
documentação, testes e evidências.

Algumas funcionalidades da aplicação desktop original, como o módulo de
**vacinas**, não foram incluídas no escopo atual da versão web.

## 👨‍💻 Desenvolvedor

**Lucas Bello**\
Projeto acadêmico --- **SENAC EAD**\
Curso Técnico em Desenvolvimento de Sistemas
