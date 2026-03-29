# Gestão de Estoque de Produtos 📦

![Java](https://img.shields.io/badge/Language-Java_21-orange)
![JavaFX](https://img.shields.io/badge/GUI-JavaFX_21-blue)
![Database](https://img.shields.io/badge/Database-SQLite-lightgrey)
![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-green)

Projeto de uma aplicação desktop desenvolvida em JavaFX integrada a um banco de dados SQLite. Criada para aplicar e consolidar conceitos fundamentais de Java, persistência de dados (CRUD) com a arquitetura DAO e interfaces gráficas reativas.

---

## 📸 Demonstração em vídeo

<a href="https://youtu.be/ZZXqkXe_7kA">Assista a uma demonstração da aplicação em funcionamento</a>

---

## 📜 Sobre o Projeto
> **Nota de Contexto**
>
> Este projeto foi desenvolvido como um laboratório prático para dominar a ponte entre a lógica orientada a objetos no Java e a persistência relacional de dados. O foco principal foi criar uma arquitetura limpa, separando a interface gráfica (App) das regras de acesso ao banco (DAO), garantindo uma base sólida para sistemas de gerenciamento.
-------------------------------------------------------------

A aplicação oferece um sistema de gerenciamento de estoque local, dispensando configurações complexas de servidores graças ao uso do SQLite, que cria o banco de dados dinamicamente no momento da execução.

O sistema permite:
- Cadastrar novos produtos no estoque.
- Visualizar todos os itens em uma tabela atualizada em tempo real.
- Atualizar dados de produtos já existentes.
- Excluir registros obsoletos.

## ✨ Funcionalidades

* **CRUD Completo:** Operações de Inserção, Leitura, Atualização e Exclusão.
* **Banco de Dados Autônomo:** Criação automática do arquivo `meu_banco_de_dados.db` e das tabelas caso não existam.
* **Interface Reativa:** Uso de `ObservableList` do JavaFX para refletir as mudanças do banco de dados na `TableView` instantaneamente.
* **Prevenção contra SQL Injection:** Consultas construídas com segurança através de `PreparedStatement`.
* **Organização Estrutural:** Código dividido em pacotes lógicos (`dao`, `db`, `model`, `app`).

## 💡 Destaque da Implementação

O principal marco deste projeto é a integração fluida entre o **Data Access Object (DAO)** e a interface gráfica. 

A aplicação demonstra de forma clara:
- O ciclo de vida da conexão JDBC (abertura, uso e fechamento seguro com *try-with-resources*).
- O trânsito de entidades (Objetos `Produto`) entre a memória do Java e os registros do SQLite.
- O mapeamento de colunas da interface gráfica utilizando `PropertyValueFactory`.

## 🚀 Como Executar o Projeto

Certifique-se de ter o **Java JDK 21** e o **Maven** instalados na sua máquina.

```bash
# 1. Clone o repositório
git clone [https://github.com/michell-ferreira/gestao-produtos-java.git](https://github.com/michell-ferreira/gestao-produtos-java.git)

# 2. Acesse a pasta do projeto
cd gestao-produtos-java

# 3. Compile e execute a aplicação via Maven
mvn clean javafx:run

```

## ✒️ Autor

**Michell Ferreira**

* **GitHub:** [michell-ferreira](https://github.com/michell-ferreira)
* **LinkedIn:** [ferreira-michel](https://www.linkedin.com/in/ferreira-michel/)

---
