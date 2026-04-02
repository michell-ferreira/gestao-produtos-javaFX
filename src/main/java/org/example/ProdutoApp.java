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

// Vincula o atributo do objeto a uma coluna da tabela (TableView) (Faz a tabela puxar os dados do obj)
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


// Application diz que esta classe é um app JavaFX.
public class ProdutoApp extends Application {
    //Classe que fala com o banco.
    private ProdutoDAO produtoDAO;
    // Lista de produtos que atualiza a tabela.
    private ObservableList<Produto> produtos;
    // Tabela da tela.
    private TableView<Produto> tableView;
    // Campos de texto.
    private TextField nomeInput, quantidadeInput, precoInput;
    // Campo de seleção, é estilo dropdown.
    private ComboBox<String> statusComboBox;
    // Conexão com o banco de dados.
    private Connection conexaoDB;

    // launch(args) - Inicia o JavaFX.
    public static void main(String[] args) {
        launch(args);
    }

    // Metodo principal do javaFX
    @Override
    public void start(Stage stage) {
        // Abrindo conexão com o banco de dados.
        try {
            conexaoDB = ConexaoDB.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
            // Se não consegue abrir para tudo com return.
            return;
        }

        // Cria objeto DAO com a conexão
        produtoDAO = new ProdutoDAO(conexaoDB);
        // Puxa produtos do banco, joga numa lista que atualiza a UI.
        produtos = FXCollections.observableList(produtoDAO.listarTodos());
        // Define titulo da janela
        stage.setTitle("Gestão de Estoque de Produtos");

        // Cria layout vertical (Tudo vai ser empilhado de cima pra baixo), container principal da tela.
        VBox vbox = new VBox();
        // Igual padding do CSS. Aqui é para afastar tudo que ta dentro da VBox das paredes dela.
        vbox.setPadding(new Insets(10,10,10,10));
        // Aqui é o espaço entre os elementos.
        vbox.setSpacing(10);

        // Linha horizontal para organizar label + input.
        HBox nomeProdutoBox = new HBox();
        nomeProdutoBox.setSpacing(10);
        Label nomeLabel = new Label("Produto:");
        nomeInput = new TextField();
        // Adicionando Label e Input dentro da Hbox.
        nomeProdutoBox.getChildren().addAll(nomeLabel, nomeInput);

        HBox quantidadeBox = new HBox();
        quantidadeBox.setSpacing(10);
        Label quantidadeLabel = new Label("Quantidade:");
        quantidadeInput = new TextField();
        quantidadeBox.getChildren().addAll(quantidadeLabel, quantidadeInput);

        HBox precoBox = new HBox();
        precoBox.setSpacing(10);
        Label precoLabel = new Label("Preço:");
        precoInput = new TextField();
        precoBox.getChildren().addAll(precoLabel, precoInput);

        HBox statusBox = new HBox();
        statusBox.setSpacing(10);
        Label statusLabel = new Label("Status:");
        // Input do tipo seleção.
        statusComboBox = new ComboBox<>();
        // Opções da seleção.
        statusComboBox.getItems().addAll("Estoque normal", "Estoque Baixo");
        statusBox.getChildren().addAll(statusLabel, statusComboBox);


        Button addButton = new Button("Adicionar");
        // Botão de evento, quando clicar faça o {}.
        addButton.setOnAction(e -> {
            // Se vier "," no preço, troque por ".".
            String preco = precoInput.getText().replace(',', '.');
            // Cria objeto na memória usando os campos digitados.
            Produto produto = new Produto(nomeInput.getText(),
                    Integer.parseInt(quantidadeInput.getText()),
                    Double.parseDouble(preco),
                    statusComboBox.getValue());
            // Aqui ele vira SQL!
            produtoDAO.inserir(produto);
            // SetAll substitui todos os itens da lista pelos dados atualizados do banco.
            produtos.setAll(produtoDAO.listarTodos());
            limparCampos();
        });

        Button updateButton = new Button("Atualizar");
        updateButton.setOnAction(e -> {
            // Pega o produto q o usuário clicou na tabela.
            Produto selectedProduto = tableView.getSelectionModel().getSelectedItem();
            // Se nada tiver selecionado, evita erro.
            if (selectedProduto != null) {
                // Atualiza o objeto na memoria.
                selectedProduto.setNome(nomeInput.getText());
                selectedProduto.setQuantidade(Integer.parseInt((quantidadeInput.getText())));
                String preco = precoInput.getText().replace(',', '.');
                selectedProduto.setPreco(Double.parseDouble(preco));
                selectedProduto.setStatus(statusComboBox.getValue());
                // Atualiza o produto no banco de dados.
                produtoDAO.atualizar(selectedProduto);
                // Atualiza a lista de produtos.
                produtos.setAll(produtoDAO.listarTodos());
                limparCampos();
            }
        });

        Button deletedButton = new Button("Excluir");
        deletedButton.setOnAction(e -> {
            Produto selectedProduto = tableView.getSelectionModel().getSelectedItem();
            if (selectedProduto != null) {
                // Exclui o produto pelo ID.
                produtoDAO.excluir(selectedProduto.getId());
                produtos.setAll(produtoDAO.listarTodos());
                limparCampos();
            }
        });

        // Limpa o formulário.
        Button clearButton = new Button("Limpar");
        clearButton.setOnAction(e -> limparCampos());

        // Aqui é criada a tabela.
        // Cria tabela .
        tableView = new TableView<>();
        // Liga a tabela à lista produtos.
        // A tabela não guarda dados, ela só mostra o que tá na lista.
        tableView.setItems(produtos);
        // Ajusta automaticamente o tamanho das colunas, colunas ocupam o espaço da tabela.
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        // Cria lista de colunas.
        List<TableColumn<Produto, ?>> columns = List.of (
                // Cada coluna aponta para um atributo do objeto Produto.
                criarColuna("ID", "id"),
                criarColuna("Produto", "nome"),
                criarColuna("Quantidade", "quantidade"),
                criarColuna("Preço", "preco"),
                criarColuna("Status", "status")
        );
        // Coloca as colunas na tabela.
        tableView.getColumns().addAll(columns);

        // Detecta quando o usuário clica em uma linha.
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // Se tiver algo selecionado.
            if (newSelection != null) {
                nomeInput.setText(newSelection.getNome());
                quantidadeInput.setText(String.valueOf(newSelection.getQuantidade()));
                precoInput.setText(String.valueOf(newSelection.getPreco()));
                statusComboBox.setValue(newSelection.getStatus());
            }
        });


        HBox buttonBox = new HBox();
        // Espaço entre os botões.
        buttonBox.setSpacing(10);
        // Adiciona os botões ao Hbox(container horizontal).
        buttonBox.getChildren().addAll(addButton, updateButton,deletedButton, clearButton);

        //
        vbox.getChildren().addAll(
                // Colocando todas as Hbox dentro do Layout principal a VBox.
                // Aqui por exemplo, posso colocar a table view em cima dos botões.
                nomeProdutoBox,
                quantidadeBox,
                precoBox,
                statusBox,
                buttonBox,
                tableView);


        // Cria cena(Conteúdo da janela)
        Scene scene = new Scene(vbox, 800, 600);
        //scene.getStylesheets().add("styles-produtos.css") // Adiciona folha de estilos
        // Coloca a cena dentro da janela(stage)
        stage.setScene(scene);
        // Mostra a janela.
        stage.show();

    }

    /**
     *  O metodo stop é chamado automaticamente quando a aplicação JAVAFX é encerrada.
     */
    @Override
    public void stop() {
        try {
            // Fecha a conexão com o banco de dados.
            conexaoDB.close();
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

    // Retorna uma coluna de objetos Produto que mostra String.
    private TableColumn<Produto, String> criarColuna(String title, String property) {
        // Cria coluna com o nome no topo.
        TableColumn<Produto, String> col = new TableColumn<>(title);
        // Define qual atributo do objeto será exibido na coluna.
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        return col;
    };
}
