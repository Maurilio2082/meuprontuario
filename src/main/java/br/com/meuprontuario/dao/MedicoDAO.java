package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Especialidade;
import br.com.meuprontuario.model.Hospital;
import br.com.meuprontuario.model.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * DAO (Data Access Object) para gerenciar as operações relacionadas à entidade
 * Medico.
 */
@Repository
public class MedicoDAO {

    private final HospitalDAO hospitalDAO = new HospitalDAO(); // DAO auxiliar para manipular hospitais
    private final EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO(); // DAO auxiliar para especialidades

    /**
     * Salva ou atualiza um médico no banco de dados.
     *
     * @param medico Objeto Medico a ser salvo ou atualizado.
     */
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
            stmt.setString(5, medico.getCbo());

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

    /**
     * Busca um médico pelo ID.
     *
     * @param id ID do médico a ser buscado.
     * @return Objeto Medico correspondente ao ID ou null se não encontrado.
     */
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

    /**
     * Lista todos os médicos cadastrados.
     *
     * @return Lista de objetos Medico.
     */
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

    /**
     * Lista médicos de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos Medico na página solicitada.
     */
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

    /**
     * Conta o número total de médicos cadastrados.
     *
     * @return O número total de médicos.
     */
    public int contarMedicos() {
        String sql = "SELECT COUNT(*) FROM medico";

        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar médicos", e);
        }
        return 0;
    }

    /**
     * Exclui um médico pelo ID.
     *
     * @param id ID do médico a ser excluído.
     */
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

    /**
     * Lista médicos associados a um hospital específico.
     *
     * @param hospitalId ID do hospital.
     * @return Lista de objetos Medico associados ao hospital.
     */
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

    /**
     * Busca a especialidade de um médico pelo ID.
     *
     * @param medicoId ID do médico.
     * @return Objeto Especialidade correspondente ou null se não encontrado.
     */
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
                            rs.getInt("ID_ESPECIALIDADE"),
                            rs.getString("NOME_ESPECIALIDADE"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar especialidade para o médico ID: " + medicoId, e);
        }

        return null;
    }
}
