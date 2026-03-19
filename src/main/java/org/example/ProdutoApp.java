package org.example;

// Base do JavaFX, permite criar uma aplicação com interface gráfica
import javafx.application.Application;

// Usado para criar margem (tipo padding no css) nos layouts
import javafx.geometry.Insets;

// Representa o conteúdo que fica dentro da janela
import javafx.scene.Scene;

// Botões, campos de texto, labels, tabela e outros componentes da tela
import javafx.scene.control.*;

// Layout que organiza elementos horizontalmente
import javafx.scene.layout.HBox;

// Layout que organiza elementos verticalmente
import javafx.scene.layout.VBox;

// Janela da aplicação
import javafx.stage.Stage;

// Usado para criar listas usadas pela interface
import javafx.collections.FXCollections;

// Tipo de lista especial do JavaFX que atualiza a interface quando muda
import javafx.collections.ObservableList;

// Vincula o atributo do objeto a uma coluna da tabela (TableView)
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.ProdutoDAO;
import org.example.db.ConexaoDB;
import org.example.model.Produto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Application cria app
 * Stage é janela
 * Scene é conteúdo
 * VBox/HBox organizam
 * control são componentes
 * ObservableList atualiza tabela
 * PropertyValueFactory liga objeto à coluna
 */



public class ProdutoApp extends Application {
    private ProdutoDAO produtoDAO;

    private ObservableList<Produto> produtos;
    private TableView<Produto> tableView;
    private TextField nomeInput, quantidadeInput, precoInput;
    private ComboBox<String> statusComboBox;
    private Connection conexaoDB;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            conexaoDB = ConexaoDB.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        produtoDAO = new ProdutoDAO(conexaoDB);
        produtos = FXCollections.observableList(produtoDAO.listarTodos());

        stage.setTitle("Gestão de Estoque de Produtos");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setSpacing(10);

        HBox nomeProdutoBox = new HBox();
        nomeProdutoBox.setSpacing(10);
        Label nomeLabel = new Label("Produto:");
        nomeInput = new TextField();
        nomeProdutoBox.getChildren().addAll(nomeLabel, nomeInput);

        HBox quantidadeBox = new HBox();
        quantidadeBox.setSpacing(10);
        Label quantidadeLabel = new Label("Quantidade");
        quantidadeInput = new TextField();
        quantidadeBox.getChildren().addAll(quantidadeLabel, quantidadeInput);

        HBox precoBox = new HBox();
        precoBox.setSpacing(10);
        Label precoLabel = new Label("Preço");
        precoInput = new TextField();
        precoBox.getChildren().addAll(precoLabel, precoInput);

        HBox statusBox = new HBox();
        statusBox.setSpacing(10);
        Label statusLabel = new Label("Status:");
        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Estoque normal", "Estoque Baixo");
        statusBox.getChildren().addAll(statusLabel, statusComboBox);

        Button addButton = new Button("Adicionar");
        addButton.setOnAction(e -> {
            String preco = precoInput.getText().replace(',', '.');
            Produto produto = new Produto(nomeInput.getText(),
                    Integer.parseInt(quantidadeInput.getText()),
                    Double.parseDouble(preco), statusComboBox.getValue());
            produtoDAO.inserir(produto);
            produtos.setAll(produtoDAO.listarTodos()); // Atualiza a lista de produtos na tela
            limparCampos();
        });

        Button updateButton = new Button("Atualizar");
        updateButton.setOnAction(e -> {
            Produto selectedProduto = tableView.getSelectionModel().getSelectedItem(); // Obtém o produto selecionado
            if (selectedProduto != null) {
                selectedProduto.setNome(nomeInput.getText());
                selectedProduto.setQuantidade(Integer.parseInt((quantidadeInput.getText())));
                String preco = precoInput.getText().replace(',', '.');
                selectedProduto.setPreco(Double.parseDouble(preco));
                selectedProduto.setStatus(statusComboBox.getValue());
                produtoDAO.atualizar(selectedProduto); // atualiza o produto no banco de dados
                produtos.setAll(produtoDAO.listarTodos()); // atualiza a lista de produtos
                limparCampos();
            }
        });

        Button deletedButton = new Button("Excluir");
        deletedButton.setOnAction(e -> {
            Produto selectedProduto = tableView.getSelectionModel().getSelectedItem(); // Obtém produto selecionado
            if (selectedProduto != null) {
                produtoDAO.excluir(selectedProduto.getId()); // Exclui o produto do banco de dados
                produtos.setAll(produtoDAO.listarTodos()); // Atualiza a lista de produtos
                limparCampos();
            }
        });

        Button clearButton = new Button("Limpar");
        clearButton.setOnAction(e -> limparCampos());

        tableView = new TableView<>();
        tableView.setItems(produtos); // Define a lista de produtos na tabela
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); // Ajusta o tamanho das colunas
        List<TableColumn<Produto, ?>> columns = List.of (
                criarColuna("ID", "id"),
                criarColuna("Produto", "nome"),
                criarColuna("Quantidade", "quantidade"),
                criarColuna("Preço", "preco"),
                criarColuna("Status", "status")
        );
        tableView.getColumns().addAll(columns);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nomeInput.setText(newSelection.getNome());
                quantidadeInput.setText(String.valueOf(newSelection.getQuantidade()));
                precoInput.setText(String.valueOf(newSelection.getPreco()));
                statusComboBox.setValue(newSelection.getStatus());
            }
        });

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(addButton, updateButton,deletedButton, clearButton); // Adiciona os botões ao HBox

        vbox.getChildren().addAll(nomeProdutoBox, quantidadeBox, precoBox, statusBox, buttonBox, tableView);

        Scene scene = new Scene(vbox, 800, 600);
        //scene.getStylesheets().add("styles-produtos.css") // Adiciona folha de estilos
        stage.setScene(scene);
        stage.show();

    }

    /**
     *  O metodo stop é chamado automaticamente quando a aplicação JAVAFX é encerrada
     */
    @Override
    public void stop() {
        try {
            conexaoDB.close(); // Fecha a conexão com o banco de dados
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão " + e.getMessage());
        }
    }

    /**
     * Limpa os campos de entrada do formulário.
     * Este metodo é chamado após adicionar, atualizar ou excluir um produto
     * para garantir que os campoos de entrada estejam prontos para uma nova entrada.
     */
    private void limparCampos() {
        nomeInput.clear();
        quantidadeInput.clear();
        precoInput.clear();
        statusComboBox.setValue(null);
    }

    /**
     * Cria uma coluna para a TableView.
     * @param title O titulo da coluna que sera exibido no cabeçalho.
     * @param property A propriedade do objeto do Produto que esta coluna deve exibir.
     * @return A coluna configurada para a TableView
     */

    private TableColumn<Produto, String> criarColuna(String title, String property) {
        TableColumn<Produto, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property)); // Define a propriedade da coluna
        return col;
    };
}
