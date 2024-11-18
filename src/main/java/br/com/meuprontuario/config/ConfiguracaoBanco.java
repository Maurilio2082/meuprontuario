package br.com.meuprontuario.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfiguracaoBanco {

    private static final String URL = "jdbc:mysql://localhost:3306/meuprontuario";
    private static final String USUARIO = "root";
    private static final String SENHA = "123456";

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
