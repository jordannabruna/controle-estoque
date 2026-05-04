package br.edu.ifg.estoque.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venda {

    private int id;
    private LocalDate dataVenda;
    private double valorTotal;

    private Cliente cliente;//relacionamento entre venda e cliente

    private List<ItemVenda> itens = new ArrayList<>();//relacionamento entre venda e produtos (uma venda pode ter vários produtos)

    public Venda() {}

    public Venda(LocalDate dataVenda, Cliente cliente) {//construtor para criar uma venda relaciona a um cliente
        this.dataVenda = dataVenda;
        this.cliente   = cliente;
    }

    public void adicionarItem(ItemVenda item) {//método que adiciona um item a venda
        itens.add(item);
    }

    public void calcularTotal() {//método que calcula o valor total de uma venda
        valorTotal = itens.stream().mapToDouble(ItemVenda::getSubtotal).sum();//calcula o valor total somando o subtotal de cada item da venda (em uma venda pode ter mais de um item de um mesmo produto)
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate d) {
        this.dataVenda = d; 
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double v) {
        this.valorTotal = v;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente c) {
        this.cliente = c;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {//
        this.itens = itens;
    }

    @Override
    public String toString() {//sobrescreve o método toString para exibir as informações da venda de forma legível
        return "Venda{id=" + id
                + ", data=" + dataVenda
                + ", cliente=" + (cliente != null ? cliente.getNome() : "null")
                + ", total=" + valorTotal + "}";
    }
}
