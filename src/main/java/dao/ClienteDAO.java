package br.edu.ifg.estoque.dao;

import br.edu.ifg.estoque.model.Cliente;
import br.edu.ifg.estoque.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean salvar(Cliente cliente) {//método para salvar um novo cliente no banco de dados
        String sql = "INSERT INTO cliente (nome, cpf, rg, endereco, telefone) VALUES (?,?,?,?,?)";//comando sql que insere um novo cliente
        try (Connection con = Conexao.getConexao();//tenta conexao com o banco
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getRg());
            ps.setString(4, cliente.getEndereco());
            ps.setString(5, cliente.getTelefone());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) cliente.setId(rs.getInt(1));

            System.out.println("Cliente salvo: " + cliente);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome=?, cpf=?, rg=?, endereco=?, telefone=? WHERE id=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getRg());
            ps.setString(4, cliente.getEndereco());
            ps.setString(5, cliente.getTelefone());
            ps.setInt(6, cliente.getId());
            ps.executeUpdate();

            System.out.println("Cliente alterado: " + cliente);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("Cliente excluído, id=" + id);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir cliente: " + e.getMessage());
            return false;
        }
    }

    public Cliente pesquisar(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar cliente: " + e.getMessage());
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY nome";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        }
        return lista;
    }

    // conta vendas realizadas para um CPF no mesmo mês/ano
    public int contarVendasNoPeriodo(String cpf, int mes, int ano) {
        String sql = """
                SELECT COUNT(*) AS total
                FROM venda v
                JOIN cliente c ON c.id = v.id_cliente
                WHERE c.cpf = ?
                  AND EXTRACT(MONTH FROM v.data_venda) = ?
                  AND EXTRACT(YEAR FROM v.data_venda) = ?
                """;
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ps.setInt(2, mes);
            ps.setInt(3, ano);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("total");

        } catch (SQLException e) {
            System.err.println("Erro ao contar vendas: " + e.getMessage());
        }
        return 0;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setRg(rs.getString("rg"));
        c.setEndereco(rs.getString("endereco"));
        c.setTelefone(rs.getString("telefone"));
        return c;
    }
}
