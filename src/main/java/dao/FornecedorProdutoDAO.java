package br.edu.ifg.estoque.dao;

import br.edu.ifg.estoque.model.Categoria;
import br.edu.ifg.estoque.model.Fornecedor;
import br.edu.ifg.estoque.model.FornecedorProduto;
import br.edu.ifg.estoque.model.Produto;
import br.edu.ifg.estoque.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorProdutoDAO {

    public boolean salvar(FornecedorProduto fornecedorProduto) {
        String sql = "INSERT INTO fornecedor_produto (id_fornecedor, id_produto) VALUES (?,?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, fornecedorProduto.getFornecedor().getId());
            ps.setInt(2, fornecedorProduto.getProduto().getId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) fornecedorProduto.setId(rs.getInt(1));

            System.out.println("FornecedorProduto salvo: " + fornecedorProduto);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar fornecedorProduto: " + e.getMessage());
            return false;
        }
    }

    public boolean alterar(FornecedorProduto fornecedorProduto) {
        String sql = "UPDATE fornecedor_produto SET id_fornecedor=?, id_produto=? WHERE id=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, fornecedorProduto.getFornecedor().getId());
            ps.setInt(2, fornecedorProduto.getProduto().getId());
            ps.setInt(3, fornecedorProduto.getId());
            ps.executeUpdate();

            System.out.println("FornecedorProduto alterado: " + fornecedorProduto);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar fornecedorProduto: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM fornecedor_produto WHERE id = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("FornecedorProduto excluído, id=" + id);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir fornecedorProduto: " + e.getMessage());
            return false;
        }
    }

    public FornecedorProduto pesquisar(int id) {
        String sql = "SELECT fp.*, " +
            "f.nome_fantasia, f.razao_social, f.cnpj, " +
            "p.nome AS prod_nome, p.preco_medio, p.qtde_estoque, " +
            "p.valor_ultima_compra, p.valor_ultima_venda, p.id_categoria, " +
            "c.nome AS cat_nome " +
            "FROM fornecedor_produto fp " +
            "JOIN fornecedor f ON f.id = fp.id_fornecedor " +
            "JOIN produto p ON p.id = fp.id_produto " +
            "JOIN categoria c ON c.id = p.id_categoria " +
            "WHERE fp.id = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar fornecedorProduto: " + e.getMessage());
        }
        return null;
    }

    public List<FornecedorProduto> listarTodos() {
        List<FornecedorProduto> lista = new ArrayList<>();
        String sql = "SELECT fp.*, " +
            "f.nome_fantasia, f.razao_social, f.cnpj, " +
            "p.nome AS prod_nome, p.preco_medio, p.qtde_estoque, " +
            "p.valor_ultima_compra, p.valor_ultima_venda, p.id_categoria, " +
            "c.nome AS cat_nome " +
            "FROM fornecedor_produto fp " +
            "JOIN fornecedor f ON f.id = fp.id_fornecedor " +
            "JOIN produto p ON p.id = fp.id_produto " +
            "JOIN categoria c ON c.id = p.id_categoria " +
            "ORDER BY f.nome_fantasia, p.nome";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar fornecedorProduto: " + e.getMessage());
        }
        return lista;
    }

    private FornecedorProduto mapear(ResultSet rs) throws SQLException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(rs.getInt("id_fornecedor"));
        fornecedor.setNomeFantasia(rs.getString("nome_fantasia"));
        fornecedor.setRazaoSocial(rs.getString("razao_social"));
        fornecedor.setCnpj(rs.getString("cnpj"));

        Produto produto = new Produto();
        produto.setId(rs.getInt("id_produto"));
        produto.setNome(rs.getString("prod_nome"));
        produto.setPrecoMedio(rs.getDouble("preco_medio"));
        produto.setQtdeEstoque(rs.getDouble("qtde_estoque"));
        produto.setValorUltimaCompra(rs.getDouble("valor_ultima_compra"));
        produto.setValorUltimaVenda(rs.getDouble("valor_ultima_venda"));

        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id_categoria"));
        categoria.setNome(rs.getString("cat_nome"));
        produto.setCategoria(categoria);

        FornecedorProduto fornecedorProduto = new FornecedorProduto();
        fornecedorProduto.setId(rs.getInt("id"));
        fornecedorProduto.setFornecedor(fornecedor);
        fornecedorProduto.setProduto(produto);
        return fornecedorProduto;
    }
}