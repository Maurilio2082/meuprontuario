package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Especialidade;
import br.com.meuprontuario.model.Hospital;
import br.com.meuprontuario.model.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository

public class MedicoDAO {

    private HospitalDAO hospitalDAO = new HospitalDAO();
    private EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();

    public void salvar(Medico medico) {
        String sql = medico.getIdMedico() != 0
                ? "UPDATE MEDICO SET NOME = ?, CONSELHO = ?, ID_HOSPITAL = ?, ID_ESPECIALIDADE = ?, CBO = ? WHERE ID_MEDICO = ?"
                : "INSERT INTO MEDICO (NOME, CONSELHO, ID_HOSPITAL, ID_ESPECIALIDADE, CBO) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, medico.getNomeMedico());
            stmt.setString(2, medico.getConselho());
            stmt.setInt(3, medico.getHospital().getIdHospital());
            stmt.setInt(4, medico.getEspecialidade().getId());
            stmt.setString(5, medico.getCbo()); // Certifique-se de que CBO está vindo corretamente do formulário

            if (medico.getIdMedico() != 0) {
                stmt.setInt(6, medico.getIdMedico());
            }

            stmt.executeUpdate();

            if (medico.getIdMedico() == 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        medico.setIdMedico(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o médico", e);
        }
    }

    public Medico buscarPorId(int id) {
        String sql = "SELECT * FROM MEDICO WHERE ID_MEDICO = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Hospital hospital = hospitalDAO.buscarPorId(rs.getInt("ID_HOSPITAL"));
                    Especialidade especialidade = especialidadeDAO.buscarPorId(rs.getInt("ID_ESPECIALIDADE"));
                    return new Medico(
                            rs.getInt("ID_MEDICO"),
                            rs.getString("NOME"),
                            rs.getString("CONSELHO"),
                            hospital,
                            especialidade,
                            rs.getString("CBO"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar médico por ID", e);
        }

        return null;
    }

    public List<Medico> listarTodos() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM MEDICO";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Hospital hospital = hospitalDAO.buscarPorId(rs.getInt("ID_HOSPITAL"));
                Especialidade especialidade = especialidadeDAO.buscarPorId(rs.getInt("ID_ESPECIALIDADE"));
                medicos.add(new Medico(
                        rs.getInt("ID_MEDICO"),
                        rs.getString("NOME"),
                        rs.getString("CONSELHO"),
                        hospital,
                        especialidade,
                        rs.getString("CBO")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar médicos", e);
        }

        return medicos;
    }

    public List<Medico> listarPorPagina(int page, int pageSize) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT id_medico, nome, conselho, id_hospital, id_especialidade, CBO FROM medico LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Hospital hospital = hospitalDAO.buscarPorId(rs.getInt("id_hospital"));
                    Especialidade especialidade = especialidadeDAO.buscarPorId(rs.getInt("id_especialidade"));

                    Medico medico = new Medico(
                            rs.getInt("id_medico"),
                            rs.getString("nome"),
                            rs.getString("conselho"),
                            hospital,
                            especialidade,
                            rs.getString("CBO"));
                    medicos.add(medico);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar médicos por página.", e);
        }
        return medicos;
    }

    public int contarMedicos() {
        String sql = "SELECT COUNT(*) FROM medico";
        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar medicos", e);
        }
        return 0;
    }

    public void excluir(int id) {
        String sql = "DELETE FROM MEDICO WHERE ID_MEDICO = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir médico", e);
        }
    }

    public List<Medico> listarPorHospital(int hospitalId) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medico WHERE id_hospital = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, hospitalId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Medico medico = new Medico(
                            rs.getInt("id_medico"),
                            rs.getString("nome"),
                            rs.getString("conselho"),
                            null, // Especialidade será preenchida em outro momento
                            null, // Hospital será preenchido em outro momento
                            rs.getString("cbo"));
                    medicos.add(medico);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar médicos por hospital.", e);
        }

        return medicos;
    }

    public Especialidade buscarEspecialidadePorMedico(int medicoId) {
        String sql = "SELECT e.ID_ESPECIALIDADE, e.NOME_ESPECIALIDADE " +
                     "FROM especialidade e " +
                     "JOIN medico m ON m.id_especialidade = e.ID_ESPECIALIDADE " +
                     "WHERE m.id_medico = ?";
    
        try (Connection conexao = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
    
            stmt.setInt(1, medicoId);
    
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Especialidade(
                            rs.getInt("ID_ESPECIALIDADE"), // Mapeia o ID da especialidade
                            rs.getString("NOME_ESPECIALIDADE") // Mapeia o nome da especialidade
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar especialidade para o médico ID: " + medicoId, e);
        }
    
        return null; // Retorna null se não encontrar nenhuma especialidade para o médico
    }
    

}
