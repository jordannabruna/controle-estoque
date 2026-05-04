package br.edu.ifg.estoque.controller;

import br.edu.ifg.estoque.dao.CategoriaDAO;
import br.edu.ifg.estoque.model.Categoria;

import java.util.List;

public class CategoriaController {

    private final CategoriaDAO dao = new CategoriaDAO();

    public boolean salvar(Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().isBlank()) {
            System.err.println("Nome da categoria não pode ser vazio.");
            return false;
        }
        return dao.salvar(categoria);
    }

    public boolean alterar(Categoria categoria) {
        if (categoria.getId() <= 0) {
            System.err.println("ID inválido para alteração.");
            return false;
        }
        return dao.alterar(categoria);
    }

    public boolean excluir(int id) {
        return dao.excluir(id);
    }

    public Categoria pesquisar(int id) {
        return dao.pesquisar(id);
    }

    public List<Categoria> listarTodos() {
        return dao.listarTodos();
    }
}
