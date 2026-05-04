package br.edu.ifg.estoque.model;

public class ItemCompra {

    private int id;
    private Produto produto;// associação com a classe Produto
    private double quantidade;
    private double valorUnit;

    public ItemCompra() {}

    public ItemCompra(Produto produto, double quantidade, double valorUnit) {// construtor para criar novos itens
    // o id é gerado automaticamente pelo banco de dados, por isso não esta sendo passado como parâmetro
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

    @Override
    public String toString() {
        return "ItemCompra{produto=" + (produto != null ? produto.getNome() : "null")
                + ", qtd=" + quantidade + ", valorUnit=" + valorUnit + "}";
    }
}
