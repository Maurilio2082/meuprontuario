# 🩺 MeuProntuário

**MeuProntuário** é um sistema completo de gerenciamento de pacientes, hospitais, médicos e históricos médicos. Ele foi desenvolvido para atender às demandas das disciplinas **Projeto Integrador II** e **Linguagem de Programação Orientada a Objetos** do curso de **Sistemas de Informação** na **Faculdade FAESA**.

O projeto utiliza práticas do **Spring Boot** para criar uma aplicação fácil de usar, com persistência no banco de dados **MySQL**.


## 📋 Funcionalidades

### ✅ Pacientes
- Cadastro, edição, listagem e exclusão de pacientes.
- Associado a um endereço completo.
- Histórico médico vinculado.

### ✅ Hospitais
- Cadastro de hospitais com CNPJ, CNES e informações de contato.
- Associado a médicos e especialidades.

### ✅ Médicos
- Gerenciamento de médicos vinculados a hospitais e especialidades.
- Integração com a tabela TISS e CID.

### ✅ Histórico Médico
- Cadastro e importação de históricos médicos em formato **XML**.
- Associações com pacientes, hospitais e médicos.
- Gerenciamento de diagnósticos com base no **CID**.

### ✅ Login e Sessão
- Autenticação baseada em usuários para hospitais e pacientes.
- Gerenciamento de sessão para acesso seguro.


## 🚀 Tecnologias Utilizadas

| Categoria         | Tecnologia                      |
|-------------------|----------------------------------|
| **Framework**     | Spring Boot                     |
| **Lógica**        | Spring MVC, Spring Data JPA     |
| **Banco de Dados**| MySQL, JDBC                     |
| **Frontend**      | Thymeleaf, HTML, CSS            |
| **Segurança**     | Servlet API (`HttpSession`)     |
| **Processamento** | XML (DOM, DocumentBuilder)      |
| **Configuração**  | Maven                           |



## 🛠️ Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas no seu ambiente:

- **Java JDK 17+**
- **Maven**
- **MySQL Server**
- Um IDE como **IntelliJ IDEA**, **Eclipse** ou **VsCode**(previamente configurado com as extenções do spring)


## ⚙️ Configuração do Banco de Dados

- Crie o banco de dados junto com suas tabelas no MySQL execultando o arquivo:
[MeuProntuario.sql](src/main/resources/static/sql/MeuProntuario.sql)

- Na classe ConfiguracaoBanco, dentro do pacote config, deve-se adicionar o nome da database e um usuário e senha com permissão para usar a base:

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfiguracaoBanco {

    private static final String URL = "jdbc:mysql://localhost:3306/meuprontuario";
    private static final String USUARIO = "usuario";
    private static final String SENHA = "senha";

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
```

- O projeto está configurado para rodar na porta `http://localhost:8082/`, edite o arquivo `application.properties` caso precise alterar.

## 🌟 Recursos Visuais

### 📄 Páginas Principais
- **Dashboard de Login**: Página inicial que redireciona usuários (hospital/paciente) após autenticação.
- **Formulários Dinâmicos**: Para cadastro de pacientes, hospitais e médicos.
- **Histórico Médico**: Interface interativa para visualizar e gerenciar históricos.

### 🗂️ Organização de Código
- **Controllers**: Gerenciam rotas HTTP.
- **Services**: Centralizam regras de negócios.
- **Repositories**: Fazem a conexão com o banco de dados.

## 📦 Estrutura do Projeto

```plaintext
meuprontuario
├── src/main/java/br/com/meuprontuario
│   ├── config          # Configurações do projeto
│   ├── controller      # Controladores das rotas
│   ├── dao             # Acesso ao banco de dados (Repositories)
│   ├── model           # Classes de domínio (Entidades)
│   ├── service         # Lógica de negócios
│   └── util            # Classes utilitárias
├── src/main/resources
│   ├── templates       # Templates Thymeleaf (HTML)
│   ├── application.properties # Configurações principais
└── pom.xml             # Dependências e configurações do Maven
```

## 📧 Contato

- **Nome**: [Maurilio Marques]
- **LinkedIn**: [https://linkedin.com/in/maurilio-marques](https://linkedin.com/in/maurilio-marques)




