package br.edu.ifg.estoque.dao;

import br.edu.ifg.estoque.model.Categoria;
import br.edu.ifg.estoque.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public boolean salvar(Categoria categoria) {//método para salvar uma nova categoria no banco de dados
        String sql = "INSERT INTO categoria (nome) VALUES (?)";//comando sql para inserir uma nova categoria, o nome é um parâmetro a ser definido
        try (Connection con = Conexao.getConexao();//tenta estabelecer conexao com o banco de dados
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNome());//define o nome da categoria a ser salva
            ps.executeUpdate();//executa o comando sql que insere uma nova categoria

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) categoria.setId(rs.getInt(1));

            System.out.println("Categoria salva: " + categoria);
            return true;

        } catch (SQLException e) {//se ocorrer um erro na execução do comando sql uma mensagem de erro é exibida
            System.err.println("Erro ao salvar categoria: " + e.getMessage());//retorna a mensagem de erro
            return false;
        }
    }

    public boolean alterar(Categoria categoria) {//método para alterar uma categoria já existente no banco
        String sql = "UPDATE categoria SET nome = ? WHERE id = ?";//comando sql para atualizar o nome de uma categoria, usa o id para identificar qual categoria deve ser atualizada
        try (Connection con = Conexao.getConexao();//tenta estabelecer conexao com o banco de dados
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNome());//define o nome da categoria a ser atualizado
            ps.setInt(2, categoria.getId());//define o id da categoria a ser atualizada
            ps.executeUpdate();//executa o comando sql para atualizar a categoria

            System.out.println("Categoria alterada: " + categoria);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar categoria: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {//método para excluir uma categoria do banco de dados, recebe o id da categoria a ser excluída como parâmetro
        String sql = "DELETE FROM categoria WHERE id = ?";//comando sql para excluir uma categoria, usa o id para identificar qual categoria será excluída
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);//id da categoria a ser excluída
            ps.executeUpdate();//executa o comando para excluir a categoria

            System.out.println("Categoria excluída, id=" + id);
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir categoria: " + e.getMessage());
            return false;
        }
    }

    public Categoria pesquisar(int id) {//método para pesquisar uma categoria no banco de dados, recebe o id da categoria a ser pesquisada como parâmetro
        String sql = "SELECT * FROM categoria WHERE id = ?";//comando sql para selecionar uma categoria, usa o id para identificar qual categoria deve ser selecionada
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);//define o id da categoria a ser pesquisada
            ResultSet rs = ps.executeQuery();//executa o comando que pesquisa a categoria e armazena o resultado em um ResultSet

            if (rs.next()) return mapear(rs);//se a categoria for encontrada, o método mapear é chamado para criar um objeto Categoria a partir dos dados do ResultSet e retorná-lo

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar categoria: " + e.getMessage());
        }
        return null;
    }

    public List<Categoria> listarTodos() {//método para listar todas as categorias do banco de dados, retorna uma lista de objetos Categoria
        List<Categoria> lista = new ArrayList<>();//cria uma lista vazia para armazenar as categorias
        String sql = "SELECT * FROM categoria ORDER BY nome";//comando sql para selecionar todas as categorias, ordenadas por nome
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {//executa o comando para listar as categorias e armazena o resultado em um ResultSet

            while (rs.next()) lista.add(mapear(rs));//para cada categoria encontrada no ResultSet, o método mapear é chamado para criar um objeto Categoria a partir dos dados do ResultSet e adicioná-lo à lista

        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }
        return lista;
    }

    private Categoria mapear(ResultSet rs) throws SQLException {//método privado para mapear os dados de um ResultSet para um objeto Categoria, recebe um ResultSet como parâmetro e retorna um objeto Categoria
        Categoria c = new Categoria();//cria um novo objeto Categoria
        c.setId(rs.getInt("id"));//define o id da categoria a partir do valor da coluna "id" do ResultSet
        c.setNome(rs.getString("nome"));//define o nome da categoria a partir do valor da coluna "nome" do ResultSet
        return c;//retorna o objeto Categoria criado a partir dos dados do ResultSet
    }
}
