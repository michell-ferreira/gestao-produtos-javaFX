package org.example;


import org.example.dao.ProdutoDAO;
import org.example.db.ConexaoDB;
import org.example.db.CriadorTabela;
import org.example.model.Produto;
import java.sql.Connection;
import java.util.List;

public class TesteInsercaoConsultaProduto {

    public static void main(String[] args) {

        // Cria a tabela de forma static sem precisar do objeto.
        CriadorTabela.criarTabela();


        // retorna conexão.
        try (Connection conexao = ConexaoDB.conectar()) {
            // entrega a conexão para o DAO.
            ProdutoDAO produtoDAO = new ProdutoDAO(conexao);

            // Lista todos os produtos (Deve estar vazio neste ponto)
            mostrarProdutos(produtoDAO);

            // Exemplo de inserção de produtos
            Produto novoProduto1 = new Produto("Notebook", 10, 3999.99, "Em estoque");
            Produto novoProduto2 = new Produto("Smartphone", 20, 1499.99, "Estoque baixo");
            Produto novoProduto3 = new Produto("Tablet", 15, 799.99, "Estoque baixo");

            produtoDAO.inserir(novoProduto1);
            produtoDAO.inserir(novoProduto2);
            produtoDAO.inserir(novoProduto3);

            // Lista todos os produtos após a inserção
            mostrarProdutos(produtoDAO);

            // Exemplo de consulta por ID (consultando o produto com ID 1)
            Produto produtoConsultado = produtoDAO.consultarPorID(1);
            if (produtoConsultado != null) {
                System.out.println("Produto encontrado " + produtoConsultado.getNome());
            } else {
                System.out.println("Produto não encontrado.");
            }

        } catch (Exception e) {
            System.err.println("Erro geral: " + e.getMessage());
        }


    }

    private static void mostrarProdutos(ProdutoDAO produtoDAO) {
        // .todosProdutos vai armazenar o ArrayList criado pelo metodo (.listarTodos)
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
