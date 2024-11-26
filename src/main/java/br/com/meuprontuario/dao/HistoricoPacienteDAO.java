package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Historico;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável por operações relacionadas aos históricos de pacientes no banco de dados.
 */
@Repository
public class HistoricoPacienteDAO {

    // Dependência para manipulação de históricos
    private final HistoricoDAO historicoDAO = new HistoricoDAO();

    /**
     * Lista os históricos médicos de um paciente específico.
     *
     * @param idPaciente ID do paciente cujos históricos serão buscados.
     * @return Lista de objetos Historico associados ao paciente.
     */
    public List<Historico> listarPorPaciente(int idPaciente) {
        String sql = "SELECT * FROM historico WHERE id_paciente = ?";
        List<Historico> historicos = new ArrayList<>();

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            // Define o parâmetro do ID do paciente na consulta
            stmt.setInt(1, idPaciente);

            try (ResultSet rs = stmt.executeQuery()) {
                // Processa os resultados da consulta
                while (rs.next()) {
                    // Cria um objeto Historico para cada registro encontrado
                    Historico historico = historicoDAO.criarHistorico(rs);
                    historicos.add(historico);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar históricos por paciente.", e);
        }

        return historicos;
    }
}
