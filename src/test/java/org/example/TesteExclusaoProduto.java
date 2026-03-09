package org.example;


import org.example.dao.ProdutoDAO;
import org.example.db.ConexaoDB;
import org.example.db.CriadorTabela;
import org.example.model.Produto;
import java.sql.Connection;
import java.util.List;

public class TesteExclusaoProduto {

    public static void main(String[] args) {

        CriadorTabela.criarTabela();

        try (Connection conexao = ConexaoDB.conectar()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(conexao);

            // Lista todos os produtos (Deve estar vazio neste ponto)
            mostrarProdutos(produtoDAO);

            // excluir por id
            //produtoDAO.excluir(3);

            // excluir todos
            produtoDAO.excluirTodos();

            System.out.println("Lista após excluir todos");

            // lista todos produtos
            mostrarProdutos(produtoDAO);

        } catch (Exception e) {
            System.err.println("Erro geral: " + e.getMessage());
        }


    }

    private static void mostrarProdutos(ProdutoDAO produtoDAO) {
        List<Produto> todosProdutos = produtoDAO.listarTodos();
        if (todosProdutos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
        } else {
            System.out.println("Lista de produtos:");
            for (Produto p : todosProdutos) {
                System.out.println(p.getId() + ": " + p.getNome() + " - " + p.getPreco());
            }
        }
    }
}
