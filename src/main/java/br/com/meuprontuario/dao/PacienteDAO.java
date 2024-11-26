package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Endereco;
import br.com.meuprontuario.model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * DAO (Data Access Object) para gerenciar operações relacionadas à entidade
 * Paciente.
 */
@Repository
public class PacienteDAO {

    private final EnderecoDAO enderecoDAO = new EnderecoDAO(); // DAO auxiliar para manipular endereços

    /**
     * Busca um paciente pelo ID.
     *
     * @param id ID do paciente.
     * @return Objeto Paciente correspondente ao ID ou null se não encontrado.
     */
    public Paciente buscarPorId(int id) {
        String sql = "SELECT * FROM PACIENTE WHERE ID_PACIENTE = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Busca o endereço associado ao paciente
                    Endereco endereco = enderecoDAO.buscarPorId(rs.getInt("ID_ENDERECO"));

                    return new Paciente(
                            rs.getInt("ID_PACIENTE"),
                            rs.getString("NOME"),
                            rs.getString("DATA_NASCIMENTO"),
                            rs.getString("EMAIL"),
                            rs.getString("TELEFONE"),
                            rs.getString("CPF"),
                            endereco);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar paciente por ID", e);
        }

        return null;
    }

    /**
     * Salva ou atualiza um paciente no banco de dados.
     *
     * @param paciente Objeto Paciente a ser salvo ou atualizado.
     */
    public void salvar(Paciente paciente) {
        String sqlPaciente = paciente.getIdPaciente() != 0
                ? "UPDATE PACIENTE SET NOME = ?, EMAIL = ?, TELEFONE = ?, DATA_NASCIMENTO = ?, CPF = ?, ID_ENDERECO = ? WHERE ID_PACIENTE = ?"
                : "INSERT INTO PACIENTE (NOME, EMAIL, TELEFONE, DATA_NASCIMENTO, CPF, ID_ENDERECO) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmtPaciente = conexao.prepareStatement(sqlPaciente,
                        Statement.RETURN_GENERATED_KEYS)) {

            stmtPaciente.setString(1, paciente.getNomePaciente());
            stmtPaciente.setString(2, paciente.getEmail());
            stmtPaciente.setString(3, paciente.getTelefone());
            stmtPaciente.setString(4, paciente.getDataNascimento());
            stmtPaciente.setString(5, paciente.getCpf());
            stmtPaciente.setInt(6, paciente.getEndereco().getIdEndereco());

            if (paciente.getIdPaciente() != 0) {
                stmtPaciente.setInt(7, paciente.getIdPaciente());
            }

            stmtPaciente.executeUpdate();

            // Obtém o ID do paciente inserido
            if (paciente.getIdPaciente() == 0) {
                try (ResultSet generatedKeys = stmtPaciente.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        paciente.setIdPaciente(generatedKeys.getInt(1));
                    }
                }

                // Insere o usuário apenas se for um novo paciente
                inserirUsuario(paciente.getCpf(), "123", "PACIENTE", paciente.getIdPaciente(), null);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o paciente", e);
        }
    }

    /**
     * Insere um registro na tabela USUARIO relacionado a um paciente.
     *
     * @param login      Login do usuário.
     * @param senha      Senha do usuário.
     * @param tipo       Tipo de usuário (ex.: PACIENTE).
     * @param idPaciente ID do paciente associado.
     * @param idHospital ID do hospital associado (pode ser null).
     */
    private void inserirUsuario(String login, String senha, String tipo, Integer idPaciente, Integer idHospital) {
        String sqlUsuario = "INSERT INTO USUARIO (LOGIN, SENHA, TIPO, ID_PACIENTE, ID_HOSPITAL) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmtUsuario = conexao.prepareStatement(sqlUsuario)) {

            stmtUsuario.setString(1, login);
            stmtUsuario.setString(2, senha);
            stmtUsuario.setString(3, tipo);
            stmtUsuario.setObject(4, idPaciente);
            stmtUsuario.setObject(5, idHospital);

            stmtUsuario.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário na tabela USUARIO.", e);
        }
    }

    /**
     * Exclui um paciente pelo ID.
     *
     * @param id ID do paciente a ser excluído.
     */
    public void excluir(int id) {
        String sql = "DELETE FROM PACIENTE WHERE ID_PACIENTE = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir paciente", e);
        }
    }

    /**
     * Lista todos os pacientes cadastrados.
     *
     * @return Lista de objetos Paciente representando todos os registros.
     */
    public List<Paciente> listarTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM PACIENTE";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Endereco endereco = enderecoDAO.buscarPorId(rs.getInt("ID_ENDERECO"));
                Paciente paciente = new Paciente(
                        rs.getInt("ID_PACIENTE"),
                        rs.getString("NOME"),
                        rs.getString("DATA_NASCIMENTO"),
                        rs.getString("EMAIL"),
                        rs.getString("TELEFONE"),
                        rs.getString("CPF"),
                        endereco);
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes.", e);
        }

        return pacientes;
    }

    /**
     * Lista pacientes de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página.
     * @return Lista de objetos Paciente na página solicitada.
     */
    public List<Paciente> listarPorPagina(int page, int pageSize) {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM PACIENTE LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Endereco endereco = enderecoDAO.buscarPorId(rs.getInt("ID_ENDERECO"));
                    Paciente paciente = new Paciente(
                            rs.getInt("ID_PACIENTE"),
                            rs.getString("NOME"),
                            rs.getString("DATA_NASCIMENTO"),
                            rs.getString("EMAIL"),
                            rs.getString("TELEFONE"),
                            rs.getString("CPF"),
                            endereco);
                    pacientes.add(paciente);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes por página", e);
        }

        return pacientes;
    }

    /**
     * Conta o número total de pacientes cadastrados.
     *
     * @return Número total de pacientes.
     */
    public int contarPacientes() {
        String sql = "SELECT COUNT(*) FROM PACIENTE";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar pacientes", e);
        }

        return 0;
    }
}
