package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Especialidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadeDAO {

    public void salvar(Especialidade especialidade) {
        String sql = especialidade.getId() != null ? "UPDATE especialidade SET nome_especialidade = ? WHERE id_especialidade = ?"
                : "INSERT INTO especialidade (nome_especialidade) VALUES (?)";

        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, especialidade.getNome());
            if (especialidade.getId() != null) {
                stmt.setLong(2, especialidade.getId());
            }

            stmt.executeUpdate();

            if (especialidade.getId() == null) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        especialidade.setId(generatedKeys.getLong(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar a especialidade", e);
        }
    }

    public List<Especialidade> listarPorPagina(int page, int pageSize) {
        List<Especialidade> especialidades = new ArrayList<>();
        String sql = "SELECT id_especialidade, nome_especialidade FROM especialidade LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;

        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Especialidade especialidade = new Especialidade();
                    especialidade.setId(rs.getLong("id_especialidade"));
                    especialidade.setNome(rs.getString("nome_especialidade"));
                    especialidades.add(especialidade);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar especialidades", e);
        }
        return especialidades;
    }

    public int contarEspecialidades() {
        String sql = "SELECT COUNT(*) FROM especialidade";
        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar especialidades", e);
        }
        return 0;
    }

    public Especialidade buscarPorId(Long id) {
        String sql = "SELECT id_especialidade, nome_especialidade FROM especialidade WHERE id_especialidade = ?";
        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Especialidade especialidade = new Especialidade();
                    especialidade.setId(rs.getLong("id_especialidade"));
                    especialidade.setNome(rs.getString("nome_especialidade"));
                    return especialidade;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar especialidade por ID", e);
        }
        return null;
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM especialidade WHERE id_especialidade = ?";
        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir especialidade", e);
        }
    }
}
