package br.edu.ifg.estoque.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:postgresql://localhost:5432/controle_estoque_prog2_jordanna";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456789";

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}