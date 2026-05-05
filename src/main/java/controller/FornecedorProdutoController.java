package br.edu.ifg.estoque.controller;

import br.edu.ifg.estoque.dao.FornecedorProdutoDAO;
import br.edu.ifg.estoque.model.FornecedorProduto;

import java.util.List;

public class FornecedorProdutoController {

    private final FornecedorProdutoDAO dao = new FornecedorProdutoDAO();

    public boolean salvar(FornecedorProduto fornecedorProduto) {
        if (fornecedorProduto.getFornecedor() == null || fornecedorProduto.getFornecedor().getId() <= 0) {
            System.err.println("Fornecedor deve ser válido.");
            return false;
        }
        if (fornecedorProduto.getProduto() == null || fornecedorProduto.getProduto().getId() <= 0) {
            System.err.println("Produto deve ser válido.");
            return false;
        }
        return dao.salvar(fornecedorProduto);
    }

    public boolean alterar(FornecedorProduto fornecedorProduto) {
        if (fornecedorProduto.getId() <= 0) {
            System.err.println("ID inválido para alteração.");
            return false;
        }
        if (fornecedorProduto.getFornecedor() == null || fornecedorProduto.getFornecedor().getId() <= 0) {
            System.err.println("Fornecedor deve ser válido.");
            return false;
        }
        if (fornecedorProduto.getProduto() == null || fornecedorProduto.getProduto().getId() <= 0) {
            System.err.println("Produto deve ser válido.");
            return false;
        }
        return dao.alterar(fornecedorProduto);
    }

    public boolean excluir(int id) {
        return dao.excluir(id);
    }

    public FornecedorProduto pesquisar(int id) {
        return dao.pesquisar(id);
    }

    public List<FornecedorProduto> listarTodos() {
        return dao.listarTodos();
    }
}