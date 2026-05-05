package br.edu.ifg.estoque.dao;

import br.edu.ifg.estoque.model.*;
import br.edu.ifg.estoque.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    public boolean salvar(Venda venda) {
        String sqlVenda = "INSERT INTO venda (data_venda, valor_total, id_cliente) VALUES (?,?,?)";
        String sqlItem  = "INSERT INTO item_venda (id_venda, id_produto, quantidade, valor_unit) VALUES (?,?,?,?)";

        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false); // inicia transação

            // 1. Salva o cabeçalho da venda
            PreparedStatement psVenda = con.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
            psVenda.setDate(1, Date.valueOf(venda.getDataVenda()));
            psVenda.setDouble(2, venda.getValorTotal());
            psVenda.setInt(3, venda.getCliente().getId());
            psVenda.executeUpdate();

            ResultSet rs = psVenda.getGeneratedKeys();
            if (rs.next()) venda.setId(rs.getInt(1));

            // 2. Salva os itens
            PreparedStatement psItem = con.prepareStatement(sqlItem);
            for (ItemVenda item : venda.getItens()) {
                psItem.setInt(1, venda.getId());
                psItem.setInt(2, item.getProduto().getId());
                psItem.setDouble(3, item.getQuantidade());
                psItem.setDouble(4, item.getValorUnit());
                psItem.addBatch();
            }
            psItem.executeBatch();

            con.commit();
            System.out.println("Venda salva: " + venda);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar venda: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { /* ignora */ }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* ignora */ }
        }
    }

    public boolean alterar(Venda venda) {
        String sql = "UPDATE venda SET data_venda=?, valor_total=?, id_cliente=? WHERE id=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(venda.getDataVenda()));
            ps.setDouble(2, venda.getValorTotal());
            ps.setInt(3, venda.getCliente().getId());
            ps.setInt(4, venda.getId());
            ps.executeUpdate();

            System.out.println("Venda alterada: " + venda);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar venda: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sqlItens = "DELETE FROM item_venda WHERE id_venda = ?";
        String sqlVenda = "DELETE FROM venda WHERE id = ?";

        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            PreparedStatement ps1 = con.prepareStatement(sqlItens);
            ps1.setInt(1, id);
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(sqlVenda);
            ps2.setInt(1, id);
            ps2.executeUpdate();

            con.commit();
            System.out.println("Venda excluída, id=" + id);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir venda: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { /* ignora */ }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* ignora */ }
        }
    }

    public Venda pesquisar(int id) {
        String sql = "SELECT v.*, c.nome AS cli_nome, c.cpf, c.rg, c.endereco, c.telefone " +
            "FROM venda v " +
            "JOIN cliente c ON c.id = v.id_cliente " +
            "WHERE v.id = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Venda venda = mapear(rs);
                venda.setItens(buscarItens(con, id));
                return venda;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar venda: " + e.getMessage());
        }
        return null;
    }

    public List<Venda> listarTodos() {
        List<Venda> lista = new ArrayList<>();
        String sql = "SELECT v.*, c.nome AS cli_nome, c.cpf, c.rg, c.endereco, c.telefone " +
            "FROM venda v " +
            "JOIN cliente c ON c.id = v.id_cliente " +
            "ORDER BY v.data_venda DESC";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar vendas: " + e.getMessage());
        }
        return lista;
    }

    private List<ItemVenda> buscarItens(Connection con, int idVenda) throws SQLException {
        List<ItemVenda> itens = new ArrayList<>();
        String sql = "SELECT iv.*, p.nome AS prod_nome, p.preco_medio, p.qtde_estoque, " +
            "       p.valor_ultima_compra, p.valor_ultima_venda, p.id_categoria, " +
            "       c.nome AS cat_nome " +
            "FROM item_venda iv " +
            "JOIN produto p  ON p.id = iv.id_produto " +
            "JOIN categoria c ON c.id = p.id_categoria " +
            "WHERE iv.id_venda = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idVenda);
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

            ItemVenda item = new ItemVenda();
            item.setId(rs.getInt("id"));
            item.setProduto(prod);
            item.setQuantidade(rs.getDouble("quantidade"));
            item.setValorUnit(rs.getDouble("valor_unit"));
            itens.add(item);
        }
        return itens;
    }

    private Venda mapear(ResultSet rs) throws SQLException {
        Venda v = new Venda();
        v.setId(rs.getInt("id"));
        v.setDataVenda(rs.getDate("data_venda").toLocalDate());
        v.setValorTotal(rs.getDouble("valor_total"));

        Cliente c = new Cliente();
        c.setId(rs.getInt("id_cliente"));
        c.setNome(rs.getString("cli_nome"));
        c.setCpf(rs.getString("cpf"));
        c.setRg(rs.getString("rg"));
        c.setEndereco(rs.getString("endereco"));
        c.setTelefone(rs.getString("telefone"));
        v.setCliente(c);

        return v;
    }
}
