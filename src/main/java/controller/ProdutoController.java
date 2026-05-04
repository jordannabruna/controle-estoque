package br.edu.ifg.estoque.controller;

import br.edu.ifg.estoque.dao.ProdutoDAO;
import br.edu.ifg.estoque.model.Produto;

import java.util.List;

public class ProdutoController {

    private final ProdutoDAO dao = new ProdutoDAO();

    public boolean salvar(Produto produto) {
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            System.err.println("Nome do produto não pode ser vazio.");
            return false;
        }
        if (produto.getCategoria() == null || produto.getCategoria().getId() <= 0) {
            System.err.println("Produto deve ter uma categoria válida (RF005).");
            return false;
        }
        return dao.salvar(produto);
    }

    public boolean alterar(Produto produto) {
        if (produto.getId() <= 0) {
            System.err.println("ID inválido para alteração.");
            return false;
        }
        return dao.alterar(produto);
    }

    public boolean excluir(int id) {
        return dao.excluir(id);
    }

    public Produto pesquisar(int id) {
        return dao.pesquisar(id);
    }

    public List<Produto> listarTodos() {
        return dao.listarTodos();
    }
}
