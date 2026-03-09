package org.example;


import org.example.dao.ProdutoDAO;
import org.example.db.ConexaoDB;
import org.example.db.CriadorTabela;
import org.example.model.Produto;
import java.sql.Connection;
import java.util.List;

public class TesteAtualizacaoProduto {

    // Achei o código daqui tranquilo, por isso evitei documentar.
    // Ele busca por id no banco de dados tras o objeto pra memória, edita e depois envia pro banco.

    public static void main(String[] args) {

        CriadorTabela.criarTabela();

        try (Connection conexao = ConexaoDB.conectar()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(conexao);

            // Lista todos os produtos (Deve estar vazio neste ponto)
            mostrarProdutos(produtoDAO);

            // Exemplo de consulta por ID (consultando o produto com ID 1)
            Produto produtoConsultado = produtoDAO.consultarPorID(1);
            if (produtoConsultado != null) {
                produtoConsultado.setNome("Laptop");
                System.out.println("Novo nome do Produto: " + produtoConsultado.getNome());
                produtoDAO.atualizar(produtoConsultado);
                System.out.println("A base de dados ficou assim: ");
                mostrarProdutos(produtoDAO);
            } else {
                System.out.println("Produto não encontrado.");
            }

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
