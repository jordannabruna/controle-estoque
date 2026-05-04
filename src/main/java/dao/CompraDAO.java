package br.edu.ifg.estoque.dao;

import br.edu.ifg.estoque.model.*;
import br.edu.ifg.estoque.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    public boolean salvar(Compra compra) {
        String sqlCompra = "INSERT INTO compra (data_compra, valor_total, id_fornecedor) VALUES (?,?,?)";
        String sqlItem   = "INSERT INTO item_compra (id_compra, id_produto, quantidade, valor_unit) VALUES (?,?,?,?)";

        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            PreparedStatement psCompra = con.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
            psCompra.setDate(1, Date.valueOf(compra.getDataCompra()));
            psCompra.setDouble(2, compra.getValorTotal());
            psCompra.setInt(3, compra.getFornecedor().getId());
            psCompra.executeUpdate();

            ResultSet rs = psCompra.getGeneratedKeys();
            if (rs.next()) compra.setId(rs.getInt(1));

            PreparedStatement psItem = con.prepareStatement(sqlItem);
            for (ItemCompra item : compra.getItens()) {
                psItem.setInt(1, compra.getId());
                psItem.setInt(2, item.getProduto().getId());
                psItem.setDouble(3, item.getQuantidade());
                psItem.setDouble(4, item.getValorUnit());
                psItem.addBatch();
            }
            psItem.executeBatch();

            con.commit();
            System.out.println("Compra salva: " + compra);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar compra: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { /* ignora */ }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* ignora */ }
        }
    }

    public boolean alterar(Compra compra) {
        String sql = "UPDATE compra SET data_compra=?, valor_total=?, id_fornecedor=? WHERE id=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(compra.getDataCompra()));
            ps.setDouble(2, compra.getValorTotal());
            ps.setInt(3, compra.getFornecedor().getId());
            ps.setInt(4, compra.getId());
            ps.executeUpdate();

            System.out.println("Compra alterada: " + compra);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar compra: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sqlItens  = "DELETE FROM item_compra WHERE id_compra = ?";
        String sqlCompra = "DELETE FROM compra WHERE id = ?";

        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            PreparedStatement ps1 = con.prepareStatement(sqlItens);
            ps1.setInt(1, id);
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(sqlCompra);
            ps2.setInt(1, id);
            ps2.executeUpdate();

            con.commit();
            System.out.println("Compra excluída, id=" + id);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir compra: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { /* ignora */ }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* ignora */ }
        }
    }

    public Compra pesquisar(int id) {
        String sql = """
                SELECT cp.*, f.nome_fantasia, f.razao_social, f.cnpj
                FROM compra cp
                JOIN fornecedor f ON f.id = cp.id_fornecedor
                WHERE cp.id = ?
                """;
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Compra compra = mapear(rs);
                compra.setItens(buscarItens(con, id));
                return compra;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar compra: " + e.getMessage());
        }
        return null;
    }

    public List<Compra> listarTodos() {
        List<Compra> lista = new ArrayList<>();
        String sql = """
                SELECT cp.*, f.nome_fantasia, f.razao_social, f.cnpj
                FROM compra cp
                JOIN fornecedor f ON f.id = cp.id_fornecedor
                ORDER BY cp.data_compra DESC
                """;
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar compras: " + e.getMessage());
        }
        return lista;
    }

    // RNF007 — retorna a média de preços de compra de um produto
    public double calcularPrecoMedio(int idProduto) {
        String sql = "SELECT AVG(valor_unit) AS media FROM item_compra WHERE id_produto = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProduto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("media");

        } catch (SQLException e) {
            System.err.println("Erro ao calcular preço médio: " + e.getMessage());
        }
        return 0;
    }

    private List<ItemCompra> buscarItens(Connection con, int idCompra) throws SQLException {
        List<ItemCompra> itens = new ArrayList<>();
        String sql = """
                SELECT ic.*, p.nome AS prod_nome, p.preco_medio, p.qtde_estoque,
                       p.valor_ultima_compra, p.valor_ultima_venda, p.id_categoria,
                       c.nome AS cat_nome
                FROM item_compra ic
                JOIN produto p  ON p.id = ic.id_produto
                JOIN categoria c ON c.id = p.id_categoria
                WHERE ic.id_compra = ?
                """;
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idCompra);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Produto prod = new Produto();
            prod.setId(rs.getInt("id_produto"));
            prod.setNome(rs.getString("prod_nome"));
            prod.setQtdeEstoque(rs.getDouble("qtde_estoque"));
            prod.setPrecoMedio(rs.getDouble("preco_medio"));
            prod.setValorUltimaCompra(rs.getDouble("valor_ultima_compra"));
            prod.setValorUltimaVenda(rs.getDouble("valor_ultima_venda"));

            Categoria cat = new Categoria();
            cat.setId(rs.getInt("id_categoria"));
            cat.setNome(rs.getString("cat_nome"));
            prod.setCategoria(cat);

            ItemCompra item = new ItemCompra();
            item.setId(rs.getInt("id"));
            item.setProduto(prod);
            item.setQuantidade(rs.getDouble("quantidade"));
            item.setValorUnit(rs.getDouble("valor_unit"));
            itens.add(item);
        }
        return itens;
    }

    private Compra mapear(ResultSet rs) throws SQLException {
        Compra c = new Compra();
        c.setId(rs.getInt("id"));
        c.setDataCompra(rs.getDate("data_compra").toLocalDate());
        c.setValorTotal(rs.getDouble("valor_total"));

        Fornecedor f = new Fornecedor();
        f.setId(rs.getInt("id_fornecedor"));
        f.setNomeFantasia(rs.getString("nome_fantasia"));
        f.setRazaoSocial(rs.getString("razao_social"));
        f.setCnpj(rs.getString("cnpj"));
        c.setFornecedor(f);

        return c;
    }
}
