package br.com.meuprontuario.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por configurar e fornecer conexões com o banco de dados
 * MySQL.
 */
public class ConfiguracaoBanco {

    // URL de conexão com o banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/meuprontuario"; // nome do seu banco de dados

    // Usuário do banco de dados
    private static final String USUARIO = "root"; // usuário do banco
    // Senha do banco de dados
    private static final String SENHA = "123456"; // senha do banco

    /**
     * Obtém uma conexão com o banco de dados usando as configurações fornecidas.
     * 
     * @return Uma instância de {@link Connection} conectada ao banco de dados.
     * @throws SQLException Caso ocorra um erro ao tentar se conectar.
     */
    public static Connection obterConexao() throws SQLException {
        // Estabelece e retorna uma conexão usando o DriverManager
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
