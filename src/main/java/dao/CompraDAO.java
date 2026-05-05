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
            con.setAutoCommit(false);// tira o auto commit para as operações ocorrem por completo,
            // se algo der errado em alguma das operações volta ao estado anterior

            //este primeiro bloco é responsável por inserir a compra na tabela compra e obter o id gerado para essa compra
            //aqui o id será usado como chave estrangeira na tabela item_compra
            PreparedStatement psCompra = con.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
            psCompra.setDate(1, Date.valueOf(compra.getDataCompra()));
            psCompra.setDouble(2, compra.getValorTotal());
            psCompra.setInt(3, compra.getFornecedor().getId());
            psCompra.executeUpdate();

            //obtem-se o id da compra
            ResultSet rs = psCompra.getGeneratedKeys();
            if (rs.next()) compra.setId(rs.getInt(1));

            //este bloco é responsável por inserir os itens da compra em item_compra
            // usa o id obtido anteriormente para relacionar os itens com a compra
            PreparedStatement psItem = con.prepareStatement(sqlItem);
            for (ItemCompra item : compra.getItens()) {
                psItem.setInt(1, compra.getId());
                psItem.setInt(2, item.getProduto().getId());
                psItem.setDouble(3, item.getQuantidade());
                psItem.setDouble(4, item.getValorUnit());
                psItem.addBatch();
            }
            psItem.executeBatch();//este comando serve para fazer as insercoes de uma vez

            con.commit();//salva as alterações no banco de dados
            System.out.println("Compra salva: " + compra);
            return true;// deu certo!

        } catch (SQLException e) {//em caso de erro
            System.err.println("Erro ao salvar compra: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { }// volta ao estado anterior
            return false;// compra não foi salva
        } finally {// de toda forma
            try { if (con != null) con.close(); } catch (SQLException ex) { }// fecha a conexão
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

        Connection con = null;// a conexão é declarada aq para ser usada nos dois blocos de exclusão
        try {
            //aqui temos dois blocos de exclusão
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            //bloco para excluir itens da compra
            PreparedStatement ps1 = con.prepareStatement(sqlItens);
            ps1.setInt(1, id);
            ps1.executeUpdate();

            //bloco para excluir a compra
            PreparedStatement ps2 = con.prepareStatement(sqlCompra);
            ps2.setInt(1, id);
            ps2.executeUpdate();

            con.commit();//salva as alterações no banco de dados
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
        String sql = "SELECT cp.*, f.nome_fantasia, f.razao_social, f.cnpj " +
            "FROM compra cp " +
            "JOIN fornecedor f ON f.id = cp.id_fornecedor " +
            "WHERE cp.id = ?";
        try (Connection con = Conexao.getConexao();//estabelece conexão
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();//executa a consulta
            if (rs.next()) {//se encontrou a compra
                Compra compra = mapear(rs);// mapeia os dados da compra e do fornecedor para um objeto Compra
                compra.setItens(buscarItens(con, id));// busca os itens da compra e os adiciona ao objeto Compra
                return compra;//retorna a compra encontrada
            }

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar compra: " + e.getMessage());
        }
        return null;
    }

    public List<Compra> listarTodos() {
        List<Compra> lista = new ArrayList<>();// lista para armazenar as compras encontradas
        String sql = "SELECT cp.*, f.nome_fantasia, f.razao_social, f.cnpj " +
            "FROM compra cp " +
            "JOIN fornecedor f ON f.id = cp.id_fornecedor " +
            "ORDER BY cp.data_compra DESC";
        try (Connection con = Conexao.getConexao();//conecta
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));//para cada compra, retorne os dados e adicione à lista

        } catch (SQLException e) {
            System.err.println("Erro ao listar compras: " + e.getMessage());
        }
        return lista;//retorna a lista de compras
    }

    // retorna a média de preços de compra de um produto
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

    //metodo que busca os itens de uma compra
    private List<ItemCompra> buscarItens(Connection con, int idCompra) throws SQLException {
        List<ItemCompra> itens = new ArrayList<>();// lista para armazenar os itens encontrados
        String sql = "SELECT ic.*, p.nome AS prod_nome, p.preco_medio, p.qtde_estoque, " +
            "       p.valor_ultima_compra, p.valor_ultima_venda, p.id_categoria, " +
            "       c.nome AS cat_nome " +
            "FROM item_compra ic " +
            "JOIN produto p  ON p.id = ic.id_produto " +
            "JOIN categoria c ON c.id = p.id_categoria " +
            "WHERE ic.id_compra = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idCompra);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Produto prod = new Produto();// mapeia os dados do produto para um objeto Produto
            prod.setId(rs.getInt("id_produto"));
            prod.setNome(rs.getString("prod_nome"));
            prod.setQtdeEstoque(rs.getDouble("qtde_estoque"));
            prod.setPrecoMedio(rs.getDouble("preco_medio"));
            prod.setValorUltimaCompra(rs.getDouble("valor_ultima_compra"));
            prod.setValorUltimaVenda(rs.getDouble("valor_ultima_venda"));

            Categoria cat = new Categoria();// mapeia os dados da categoria para um objeto Categoria
            cat.setId(rs.getInt("id_categoria"));
            cat.setNome(rs.getString("cat_nome"));
            prod.setCategoria(cat);//associa a um produto

            ItemCompra item = new ItemCompra();// mapeia os dados do item de compra para um objeto ItemCompra
            item.setId(rs.getInt("id"));
            item.setProduto(prod);
            item.setQuantidade(rs.getDouble("quantidade"));
            item.setValorUnit(rs.getDouble("valor_unit"));
            itens.add(item);//adiciona o item à lista de itens
        }
        return itens;//retorna a lista de itens encontrados
    }

    // mapeia os dados da compra e do fornecedor para um objeto Compra
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
