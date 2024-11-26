package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Especialidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * DAO (Data Access Object) responsável por gerenciar as operações relacionadas
 * à entidade Especialidade no banco de dados.
 */
@Repository
public class EspecialidadeDAO {

    /**
     * Busca uma especialidade pelo ID.
     *
     * @param id ID da especialidade a ser buscada.
     * @return Objeto Especialidade correspondente ao ID fornecido ou null se não
     *         encontrado.
     */
    public Especialidade buscarPorId(int id) {
        String sql = "SELECT * FROM ESPECIALIDADE WHERE ID_ESPECIALIDADE = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Especialidade(
                            rs.getInt("ID_ESPECIALIDADE"),
                            rs.getString("NOME_ESPECIALIDADE"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar especialidade por ID", e);
        }

        return null;
    }

    /**
     * Lista todas as especialidades cadastradas.
     *
     * @return Lista de objetos Especialidade representando todos os registros.
     */
    public List<Especialidade> listarTodas() {
        List<Especialidade> especialidades = new ArrayList<>();
        String sql = "SELECT * FROM ESPECIALIDADE";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                especialidades.add(new Especialidade(
                        rs.getInt("ID_ESPECIALIDADE"),
                        rs.getString("NOME_ESPECIALIDADE")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar especialidades", e);
        }

        return especialidades;
    }

    /**
     * Lista as especialidades de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Quantidade de itens por página.
     * @return Lista de objetos Especialidade correspondentes à página solicitada.
     */
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
                    Especialidade especialidade = new Especialidade(
                            rs.getInt("id_especialidade"),
                            rs.getString("nome_especialidade"));
                    especialidades.add(especialidade);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar especialidades", e);
        }
        return especialidades;
    }

    /**
     * Conta o número total de especialidades cadastradas.
     *
     * @return O número total de especialidades.
     */
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

    /**
     * Salva ou atualiza uma especialidade no banco de dados.
     *
     * @param especialidade Objeto Especialidade a ser salvo ou atualizado.
     */
    public void salvar(Especialidade especialidade) {
        String sql = especialidade.getId() != 0
                ? "UPDATE ESPECIALIDADE SET NOME_ESPECIALIDADE = ? WHERE ID_ESPECIALIDADE = ?"
                : "INSERT INTO ESPECIALIDADE (NOME_ESPECIALIDADE) VALUES (?)";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, especialidade.getNome());
            if (especialidade.getId() != 0) {
                stmt.setInt(2, especialidade.getId());
            }

            stmt.executeUpdate();

            if (especialidade.getId() == 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        especialidade.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar especialidade", e);
        }
    }

    /**
     * Exclui uma especialidade pelo ID.
     *
     * @param id ID da especialidade a ser excluída.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM ESPECIALIDADE WHERE ID_ESPECIALIDADE = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir especialidade", e);
        }
    }
}
