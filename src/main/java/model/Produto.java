
package br.edu.ifg.estoque.model;

public class Produto {

    private int id;
    private String nome;
    private double precoMedio;
    private double qtdeEstoque;
    private double valorUltimaCompra;
    private double valorUltimaVenda;

    private Categoria categoria;//produto pertence a uma categoria

    public Produto() {}//construtor vazio

    public Produto(String nome, Categoria categoria) {//construtor para criar um produto com nome e categoria
        this.nome = nome;
        this.categoria = categoria;
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

    public double getPrecoMedio() { 
        return precoMedio;
    }

    public void setPrecoMedio(double precoMedio) {
        this.precoMedio = precoMedio;
    }

    public double getQtdeEstoque() {
        return qtdeEstoque; 
    }

    public void setQtdeEstoque(double qtdeEstoque) {
        this.qtdeEstoque = qtdeEstoque;
    }

    public double getValorUltimaCompra() {
        return valorUltimaCompra;
    }

    public void setValorUltimaCompra(double valorUltimaCompra) {
        this.valorUltimaCompra = valorUltimaCompra; }

    public double getValorUltimaVenda() {
        return valorUltimaVenda;
    }

    public void setValorUltimaVenda(double valorUltimaVenda) {
        this.valorUltimaVenda = valorUltimaVenda;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {//sobrescreve o método toString para exibir as informações do produto de forma legível
        return "Produto{id=" + id
                + ", nome='" + nome + "'"
                + ", estoque=" + qtdeEstoque
                + ", precoMedio=" + precoMedio
                + ", categoria=" + (categoria != null ? categoria.getNome() : "null")
                + "}";
    }
}
