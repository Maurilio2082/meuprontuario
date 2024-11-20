package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Endereco;
import br.com.meuprontuario.model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository

public class PacienteDAO {

    private EnderecoDAO enderecoDAO = new EnderecoDAO();

    public Paciente buscarPorId(int id) {
        String sql = "SELECT * FROM PACIENTE WHERE ID_PACIENTE = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Buscar o endereço do paciente
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

    public void salvar(Paciente paciente) {
        String sql = paciente.getIdPaciente() != 0
                ? "UPDATE PACIENTE SET NOME = ?, EMAIL = ?, TELEFONE = ?, DATA_NASCIMENTO = ?, CPF = ?, ID_ENDERECO = ? WHERE ID_PACIENTE = ?"
                : "INSERT INTO PACIENTE (NOME, EMAIL, TELEFONE, DATA_NASCIMENTO, CPF, ID_ENDERECO) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, paciente.getNomePaciente());
            stmt.setString(2, paciente.getEmail());
            stmt.setString(3, paciente.getTelefone());
            stmt.setString(4, paciente.getDataNascimento());
            stmt.setString(5, paciente.getCpf());
            stmt.setInt(6, paciente.getEndereco().getIdEndereco());

            if (paciente.getIdPaciente() != 0) {
                stmt.setInt(7, paciente.getIdPaciente());
            }

            stmt.executeUpdate();

            if (paciente.getIdPaciente() == 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        paciente.setIdPaciente(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o paciente", e);
        }
    }

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

    public List<Paciente> listarTodos() {
        List<Paciente> pacientes = new ArrayList<>();

        String sql = "SELECT * FROM paciente";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Endereco endereco = enderecoDAO.buscarPorId(rs.getInt("id_endereco"));
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
