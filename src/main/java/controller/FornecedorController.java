package br.edu.ifg.estoque.controller;

import br.edu.ifg.estoque.dao.FornecedorDAO;
import br.edu.ifg.estoque.model.Fornecedor;

import java.util.List;

public class FornecedorController {

    private final FornecedorDAO dao = new FornecedorDAO();

    public boolean salvar(Fornecedor fornecedor) {
        if (fornecedor.getNomeFantasia() == null || fornecedor.getNomeFantasia().isBlank()) {
            System.err.println("Nome fantasia não pode ser vazio.");
            return false;
        }
        if (fornecedor.getCnpj() == null || fornecedor.getCnpj().isBlank()) {
            System.err.println("CNPJ não pode ser vazio.");
            return false;
        }
        return dao.salvar(fornecedor);
    }

    public boolean alterar(Fornecedor fornecedor) {
        if (fornecedor.getId() <= 0) {
            System.err.println("ID inválido para alteração.");
            return false;
        }
        return dao.alterar(fornecedor);
    }

    public boolean excluir(int id) {
        return dao.excluir(id);
    }

    public Fornecedor pesquisar(int id) {
        return dao.pesquisar(id);
    }

    public List<Fornecedor> listarTodos() {
        return dao.listarTodos();
    }
}
