package br.edu.ifg.estoque.controller;

import br.edu.ifg.estoque.dao.ClienteDAO;
import br.edu.ifg.estoque.model.Cliente;

import java.util.List;

public class ClienteController {

    private final ClienteDAO dao = new ClienteDAO();

    public boolean salvar(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            System.err.println("Nome do cliente não pode ser vazio.");
            return false;
        }
        if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
            System.err.println("CPF do cliente não pode ser vazio.");
            return false;
        }
        return dao.salvar(cliente);
    }

    public boolean alterar(Cliente cliente) {
        if (cliente.getId() <= 0) {
            System.err.println("ID inválido para alteração.");
            return false;
        }
        return dao.alterar(cliente);
    }

    public boolean excluir(int id) {
        return dao.excluir(id);
    }

    public Cliente pesquisar(int id) {
        return dao.pesquisar(id);
    }

    public List<Cliente> listarTodos() {
        return dao.listarTodos();
    }
}
