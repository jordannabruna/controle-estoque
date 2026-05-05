package br.edu.ifg.estoque.dao;

import br.edu.ifg.estoque.model.Categoria;
import br.edu.ifg.estoque.model.Produto;
import br.edu.ifg.estoque.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public boolean salvar(Produto produto) {
        String sql = "INSERT INTO produto (nome, preco_medio, qtde_estoque, " +
            "valor_ultima_compra, valor_ultima_venda, id_categoria) " +
            "VALUES (?,?,?,?,?,?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPrecoMedio());
            ps.setDouble(3, produto.getQtdeEstoque());
            ps.setDouble(4, produto.getValorUltimaCompra());
            ps.setDouble(5, produto.getValorUltimaVenda());
            ps.setInt(6, produto.getCategoria().getId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) produto.setId(rs.getInt(1));

            System.out.println("Produto salvo: " + produto);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar produto: " + e.getMessage());
            return false;
        }
    }

    public boolean alterar(Produto produto) {
        String sql = "UPDATE produto SET nome=?, preco_medio=?, qtde_estoque=?, " +
            "valor_ultima_compra=?, valor_ultima_venda=?, id_categoria=? " +
            "WHERE id=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPrecoMedio());
            ps.setDouble(3, produto.getQtdeEstoque());
            ps.setDouble(4, produto.getValorUltimaCompra());
            ps.setDouble(5, produto.getValorUltimaVenda());
            ps.setInt(6, produto.getCategoria().getId());
            ps.setInt(7, produto.getId());
            ps.executeUpdate();

            System.out.println("Produto alterado: " + produto);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar produto: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("Produto excluído, id=" + id);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
            return false;
        }
    }

    public Produto pesquisar(int id) {
        String sql = "SELECT p.*, c.nome AS cat_nome " +
            "FROM produto p " +
            "JOIN categoria c ON c.id = p.id_categoria " +
            "WHERE p.id = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar produto: " + e.getMessage());
        }
        return null;
    }

    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nome AS cat_nome " +
            "FROM produto p " +
            "JOIN categoria c ON c.id = p.id_categoria " +
            "ORDER BY p.nome";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

    // Atualiza somente os campos de estoque e valores calculados
    public boolean atualizarEstoqueEValores(Produto produto) {
        String sql = "UPDATE produto " +
            "SET qtde_estoque=?, preco_medio=?, " +
            "    valor_ultima_compra=?, valor_ultima_venda=? " +
            "WHERE id=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, produto.getQtdeEstoque());
            ps.setDouble(2, produto.getPrecoMedio());
            ps.setDouble(3, produto.getValorUltimaCompra());
            ps.setDouble(4, produto.getValorUltimaVenda());
            ps.setInt(5, produto.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
            return false;
        }
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setPrecoMedio(rs.getDouble("preco_medio"));
        p.setQtdeEstoque(rs.getDouble("qtde_estoque"));
        p.setValorUltimaCompra(rs.getDouble("valor_ultima_compra"));
        p.setValorUltimaVenda(rs.getDouble("valor_ultima_venda"));

        Categoria cat = new Categoria();
        cat.setId(rs.getInt("id_categoria"));
        cat.setNome(rs.getString("cat_nome"));
        p.setCategoria(cat);

        return p;
    }
}
