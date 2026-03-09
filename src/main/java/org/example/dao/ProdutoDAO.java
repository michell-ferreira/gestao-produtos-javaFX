package org.example.dao;

import org.example.model.Produto;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * O DAO (Data Access Object) é o "tradutor" do projeto.
 * Ele serve para falar com o banco de dados.
 * Java não fala direto com banco.
 * Ele manda o DAO falar com o banco.
 * Transformando objetos Java em linhas no banco e vice-versa.
 * Aqui que é onde a persistência acontece.
 */
public class ProdutoDAO {

    // Váriavel iniciada para guardar a conexão.
    private final Connection CONEXAO_DB;

    // Construtor que inicializa a conexão com o banco de dados, usando a váriavel acima.
    public ProdutoDAO(Connection conexaoDb) {
        CONEXAO_DB = conexaoDb;
    }

    // -Metodo para inserir um novo produto no banco de dados.-
    public void inserir (Produto produto) {

        // Comando sql basicamente diz, inserir na tabela produtos os atributos em ordem.
        // O VALUES(?,?,?,?), são espaços reservados respetivamente para os atributos citados.
        String sql = "INSERT INTO produtos (nome_produto, quantidade, preco, status) VALUES(?,?,?,?)";

        // stmt - é o mensageiro.
        // CONEXAO_DB.prepareStatement(sql) - conectando o mensageiro ao banco de dados para injetar o sql abaixo.
        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            // Agora vai preencher kd "?", com as informações do produto criado
            // // O número indica a posição da "?" no comando SQL acima.
            stmt.setString(1, produto.getNome());
            stmt.setInt(2,produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getStatus());
            // executeUpdate - confirma e salva as atualizações
            int linhas = stmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Produto inserido com sucesso.");
            } else {
                System.out.println("Falha ao inserir produto.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
        }
    }

    // -Metodo para excluir todos os produtos do banco de dados.-
    public void excluirTodos() {
        // Comando para deletar tudo na tabela não precisa de *(all).
        String sql = "DELETE FROM produtos";
        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao excluir todos os produtos: " + e.getMessage());
        }
    }


    // -Metodo para consultar um produto pelo seu ID. Banco p/ Objeto.-
    public Produto consultarPorID(int id) {
        // "?" é alguma coisa que não foi definida (um espaço em branco).
        String sql = "SELECT * FROM produtos WHERE id_produto = ?";
        // Enviando comando para o banco de dados e armazenando no stmt.
        // até aq só preparou tudo e não realizou nada no banco de dados.
        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {

            // 1 aqui é referência ao "?" do slq
            // aqui, neste exato momento, o comando lá em cima virou |SELECT * FROM produtos WHERE id_produto = 10|
            stmt.setInt(1, id);

            // Finalmente executando a consulta
            // Manda o SQL pro banco e recebe o resultado
            // Guarda o resultado dentro do result, a variável rs.
            // Result começa na linha 0.
            try (ResultSet rs = stmt.executeQuery()) {
                // com o rs.next ele vai para primeira linha e retorna true se a linha existir.
                if (rs.next()) {
                    // produto vazio criado
                    Produto produto = new Produto();

                    // pegando valores da tabela e settando no produto.
                    produto.setId(rs.getInt("id_produto"));
                    produto.setNome(rs.getString("nome_produto"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setStatus(rs.getString("status"));
                    return produto;
                }
            }
        } catch (SQLException e) {
            System.err.print("Erro ao consultar produto por ID: "+ e.getMessage());
        }
        return null;
    }

    // -Metodo para atualizar as informações de um produto no banco de dados. OBJ p/ banco.-
    // "Where id_produto = ?" se não existisse atualizaria td o banco de dados.
    public void atualizar (Produto produto) {
        String sql = "UPDATE produtos set nome_produto = ?, quantidade = ?, preco = ?, status = ? WHERE id_produto = ?";
        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getStatus());
            stmt.setInt(5, produto.getId());

            // ExecuteUpdate retorna(pd ser usada em variável) int de quantas linhas foram modificadas, se retornar 0, não alterou nada.
            int linhas = stmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Produto atualizado com sucesso.");
            } else {
                System.out.println("Nenhum produto foi encontrado com este ID");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    // -Metodo para excluir um produto pelo seu ID.-
    public void excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";
        try(PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Produto excluído com sucesso.");
            } else {
                System.out.println("Nenhum produto encontrado com este ID.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
        }
    }

    // -Metodo para listar todos os produtos no banco de dados.-
    public List<Produto> listarTodos(){
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while(rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome_produto"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setStatus(rs.getString("status"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

}
