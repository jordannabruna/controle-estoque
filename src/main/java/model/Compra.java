package br.edu.ifg.estoque.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    private int id;
    private LocalDate dataCompra;
    private double valorTotal;
    
    private Fornecedor fornecedor;//compra pertence a um fornecedor

    private List<ItemCompra> itens = new ArrayList<>();//compra contém vários produtos como itens de compra

    public Compra() {}

    public Compra(LocalDate dataCompra, Fornecedor fornecedor) {// construtor para criar novas compras
    //id gerado automaticamente no banco
        this.dataCompra = dataCompra;
        this.fornecedor = fornecedor;
    }

    public void adicionarItem(ItemCompra item) {// método para adicionar um item à compra
        itens.add(item);
    }

    public void calcularTotal() {// método para calcular o valor total da compra somando os subtotais dos itens
        valorTotal = itens.stream().mapToDouble(ItemCompra::getSubtotal).sum();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate d) {
        this.dataCompra = d;
    }

    public double getValorTotal() { 
        return valorTotal;
    }

    public void setValorTotal(double v) {
        this.valorTotal = v;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor f) {
        this.fornecedor = f;
    }

    public List<ItemCompra> getItens() {
        return itens;
    }

    public void setItens(List<ItemCompra> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "Compra{id=" + id
                + ", data=" + dataCompra
                + ", fornecedor=" + (fornecedor != null ? fornecedor.getNomeFantasia() : "null")
                + ", total=" + valorTotal + "}";
    }
}
