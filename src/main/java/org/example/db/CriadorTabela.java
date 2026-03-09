package org.example.db;

import java.sql.Connection;
import java.sql.SQLException;

// Importa o objeto que envia comandos SQL para o banco executar.
import java.sql.Statement;

public class CriadorTabela {

    // Metodo que cria a tabela no banco de dados.
    // Usado para criar ou conectar tabela existente.
    public static void criarTabela() {

        // Colocando os comandos SQL numa variável.
        // Aspas triplas são usadas para escrever várias linhas.
        // Quando definido como primaryKey ele incrementa o ID(Identificador, não índice) sozinho.
        String sql = """
                CREATE TABLE IF NOT EXISTS produtos (
                 id_produto INTEGER PRIMARY KEY,
                 nome_produto TEXT NOT NULL,
                 quantidade INTEGER,
                 preco REAL,
                 status TEXT
                 )
                """;


        try (
                // Try with-resources abre tudo e fecha tudo mesmo se der erro.
                // Aqui eu abro a conexão com o banco de dados usando o metodo da ConexãoDB.
                Connection conexao = ConexaoDB.conectar();

                // Statement é o objeto que envia comandos SQL para o banco, mensageiro que envia o sql para a tabela.
                // Este vai usar a conexão que abri acima.
                Statement stmt = conexao.createStatement();
        ) {
            // Comando do Statement é enviado para o banco
            // Neste exato momento a tabela é criada
            stmt.execute(sql);
            System.out.println("A tabela foi criada // ou já existia");
        } catch (SQLException e) {
            System.err.println("Deu ruim ao criar tabela de produtos!");
            // StackTrace mostra o caminho do erro como uma pilha
            e.printStackTrace();
        }
    }
}
