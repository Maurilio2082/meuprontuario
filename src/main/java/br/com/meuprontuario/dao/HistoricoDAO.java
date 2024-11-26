package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.*;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável por gerenciar as operações relacionadas
 * à entidade Historico no banco de dados.
 */
@Repository
public class HistoricoDAO {

    // DAOs auxiliares para buscar as entidades relacionadas
    private final EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final HospitalDAO hospitalDAO = new HospitalDAO();
    private final CidDAO cidDAO = new CidDAO();
    private final TabelaTissDAO tabelaTissDAO = new TabelaTissDAO();

    /**
     * Busca um histórico pelo ID.
     *
     * @param id ID do histórico a ser buscado.
     * @return Objeto Historico correspondente ao ID ou null se não encontrado.
     */
    public Historico buscarPorId(int id) {
        String sql = "SELECT * FROM historico WHERE id_historico = ?";
        Historico historico = null;

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    historico = criarHistorico(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar histórico pelo ID.", e);
        }
        return historico;
    }

    /**
     * Salva ou atualiza um histórico no banco de dados.
     *
     * @param historico Objeto Historico a ser salvo.
     */
    public void salvar(Historico historico) {
        String sql = historico.getIdHistorico() != 0
                ? "UPDATE historico SET data_consulta = ?, observacao = ?, id_paciente = ?, id_hospital = ?, id_medico = ?, id_especialidade = ?, cod_cid = ?, cod_tiss = ? WHERE id_historico = ?"
                : "INSERT INTO historico (data_consulta, observacao, id_paciente, id_hospital, id_medico, id_especialidade, cod_cid, cod_tiss) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Preenche os parâmetros da consulta
            stmt.setString(1, historico.getDataConsulta());
            stmt.setString(2, historico.getObservacao());
            stmt.setInt(3, historico.getIdPaciente().getIdPaciente());
            stmt.setInt(4, historico.getIdHospital().getIdHospital());
            stmt.setInt(5, historico.getIdMedico().getIdMedico());
            stmt.setInt(6, historico.getIdEspecialidade().getId());
            stmt.setString(7, historico.getCid().getCodCid());
            stmt.setLong(8, historico.getTabelaTiss().getCodigoTermo());

            if (historico.getIdHistorico() != 0) {
                stmt.setInt(9, historico.getIdHistorico());
            }

            stmt.executeUpdate();

            // Recupera o ID gerado caso seja um novo registro
            if (historico.getIdHistorico() == 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        historico.setIdHistorico(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar histórico.", e);
        }
    }

    /**
     * Exclui um histórico pelo ID.
     *
     * @param id ID do histórico a ser excluído.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM historico WHERE id_historico = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir o histórico.", e);
        }
    }

    /**
     * Lista todos os históricos cadastrados.
     *
     * @return Lista de objetos Historico representando todos os registros.
     */
    public List<Historico> listarTodos() {
        String sql = "SELECT * FROM historico";
        List<Historico> historicos = new ArrayList<>();

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Historico historico = criarHistorico(rs);
                historicos.add(historico);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar históricos.", e);
        }

        return historicos;
    }

    /**
     * Cria um objeto Historico a partir de um ResultSet.
     *
     * @param rs ResultSet contendo os dados do histórico.
     * @return Objeto Historico criado a partir do ResultSet.
     * @throws SQLException Se houver erro ao acessar os dados do ResultSet.
     */
    protected Historico criarHistorico(ResultSet rs) throws SQLException {
        // Busca as entidades relacionadas pelo ID
        Paciente paciente = pacienteDAO.buscarPorId(rs.getInt("id_paciente"));
        Hospital hospital = hospitalDAO.buscarPorId(rs.getInt("id_hospital"));
        Medico medico = medicoDAO.buscarPorId(rs.getInt("id_medico"));
        Especialidade especialidade = especialidadeDAO.buscarPorId(rs.getInt("id_especialidade"));
        Cid cid = cidDAO.buscarPorCodigo(rs.getString("cod_cid"));
        TabelaTiss tabelaTiss = tabelaTissDAO.buscarPorCodigo(rs.getLong("cod_tiss"));

        // Retorna um objeto Historico preenchido
        return new Historico(
                rs.getInt("id_historico"),
                rs.getString("data_consulta"),
                rs.getString("observacao"),
                paciente,
                hospital,
                medico,
                especialidade,
                cid,
                tabelaTiss);
    }

    /**
     * Lista históricos de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página (número de itens por página).
     * @return Lista de objetos Historico na página solicitada.
     */
    public List<Historico> listarPorPagina(int page, int pageSize) {
        String sql = "SELECT * FROM historico LIMIT ? OFFSET ?";
        List<Historico> historicos = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Historico historico = criarHistorico(rs);
                    historicos.add(historico);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar históricos por página.", e);
        }

        return historicos;
    }

    /**
     * Conta o número total de históricos cadastrados.
     *
     * @return O número total de históricos no banco de dados.
     */
    public int contarHistoricos() {
        String sql = "SELECT COUNT(*) FROM historico";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar históricos.", e);
        }
        return 0;
    }
}
