package br.edu.ifg.estoque.model;

public class FornecedorProduto {

    private int id;
    private Fornecedor fornecedor;
    private Produto produto;

    public FornecedorProduto() {}

    public FornecedorProduto(Fornecedor fornecedor, Produto produto) {
        this.fornecedor = fornecedor;
        this.produto = produto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "FornecedorProduto{id=" + id
                + ", fornecedor=" + (fornecedor != null ? fornecedor.getNomeFantasia() : "null")
                + ", produto=" + (produto != null ? produto.getNome() : "null")
                + "}";
    }
}