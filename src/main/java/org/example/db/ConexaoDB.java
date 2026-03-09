package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    // A String é o "endereço" do banco de dados.
    // O banco de dados no SQLite é apenas um arquivo.
    // Se ele existir, ele abre o arquivo.
    // Se este arquivo não existir, ele vai ser criado.
    // "jdbc:" protocolo principal, avisa que vou me conctar ao banco de dados.
    // "sqlite:" sub-protocolo que avisa qual tipo de banco de dados vai ser usado.
    //"meu_banco_de_dados.db" nome do arquivo no projeto que fica na pasta target.
    private static final String URL = "jdbc:sqlite:meu_banco_de_dados.db";


    // Abre conexão com o banco de dados.
    // O Connection abre uma conexão que envia a linguagem SQL para o banco de dados(SQLite).
    // throws SQLExeception força o tratamento de erro.
    public static Connection conectar() throws SQLException {

        // DriveManager é uma classe nativa do java que gerência todos os drivers de banco de dados.
        // Entrega a conexão para quem o chamou.
        // getConnection é o pedido para pegar a URL(endereço).
        return DriverManager.getConnection(URL);
    }
}
