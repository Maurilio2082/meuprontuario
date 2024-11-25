package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Historico;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HistoricoPacienteDAO {
    private final HistoricoDAO historicoDAO = new HistoricoDAO();

    public List<Historico> listarPorPaciente(int idPaciente) {
        String sql = "SELECT * FROM historico WHERE id_paciente = ?";
        List<Historico> historicos = new ArrayList<>();

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Historico historico = historicoDAO.criarHistoricoAPartirDoResultSet(rs);
                    historicos.add(historico);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar históricos por paciente.", e);
        }

        return historicos;
    }
}
