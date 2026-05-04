package br.edu.ifg.estoque.dao;

import br.edu.ifg.estoque.model.Fornecedor;
import br.edu.ifg.estoque.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    public boolean salvar(Fornecedor fornecedor) {
        String sql = "INSERT INTO fornecedor (nome_fantasia, razao_social, cnpj) VALUES (?,?,?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, fornecedor.getNomeFantasia());
            ps.setString(2, fornecedor.getRazaoSocial());
            ps.setString(3, fornecedor.getCnpj());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) fornecedor.setId(rs.getInt(1));

            System.out.println("Fornecedor salvo: " + fornecedor);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar fornecedor: " + e.getMessage());
            return false;
        }
    }

    public boolean alterar(Fornecedor fornecedor) {
        String sql = "UPDATE fornecedor SET nome_fantasia=?, razao_social=?, cnpj=? WHERE id=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fornecedor.getNomeFantasia());
            ps.setString(2, fornecedor.getRazaoSocial());
            ps.setString(3, fornecedor.getCnpj());
            ps.setInt(4, fornecedor.getId());
            ps.executeUpdate();

            System.out.println("Fornecedor alterado: " + fornecedor);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar fornecedor: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM fornecedor WHERE id = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("Fornecedor excluído, id=" + id);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir fornecedor: " + e.getMessage());
            return false;
        }
    }

    public Fornecedor pesquisar(int id) {
        String sql = "SELECT * FROM fornecedor WHERE id = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar fornecedor: " + e.getMessage());
        }
        return null;
    }

    public List<Fornecedor> listarTodos() {
        List<Fornecedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor ORDER BY nome_fantasia";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar fornecedores: " + e.getMessage());
        }
        return lista;
    }

    private Fornecedor mapear(ResultSet rs) throws SQLException {
        Fornecedor f = new Fornecedor();
        f.setId(rs.getInt("id"));
        f.setNomeFantasia(rs.getString("nome_fantasia"));
        f.setRazaoSocial(rs.getString("razao_social"));
        f.setCnpj(rs.getString("cnpj"));
        return f;
    }
}
