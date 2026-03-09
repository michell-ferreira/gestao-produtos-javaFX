package org.example.model;

public class Produto {
    private int id;
    private String nome;
    private int quantidade;
    private double preco;
    private String status;

    // Não tem id pq é primaryKey, este é o SQLite que vai gerênciar (ele vai gerar o número).
    public Produto(String nome, int quantidade, double preco, String status) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.status = status;
    }

    // Necessário para o DAO reconstruir o objeto com dados vindos do banco.
    public Produto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
