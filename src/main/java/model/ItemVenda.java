package br.edu.ifg.estoque.model;

public class ItemVenda {

    private int id;
    private Produto produto;//relacionamento entre item de venda e produto
    private double quantidade;
    private double valorUnit;

    public ItemVenda() {}

    public ItemVenda(Produto produto, double quantidade, double valorUnit) {//construtor para criar um item de venda, relaciona a um produto, quantidade e valor unitário
        this.produto    = produto;
        this.quantidade = quantidade;
        this.valorUnit  = valorUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto p) {
        this.produto = p;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double q) {
        this.quantidade = q;
    }

    public double getValorUnit() {
        return valorUnit;
    }

    public void setValorUnit(double v) {
        this.valorUnit = v;
    }

    public double getSubtotal() {
        return quantidade * valorUnit;
    }

    @Override//notação para indicar que este método sobrescreve um método da classe pai
    public String toString() {//sobrescreve o método toString para exibir as informações do item de venda de forma legível
        return "ItemVenda{produto=" + (produto != null ? produto.getNome() : "null")
                + ", qtd=" + quantidade + ", valorUnit=" + valorUnit + "}";
    }
}
