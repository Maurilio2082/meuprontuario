package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Endereco;
import br.com.meuprontuario.model.Hospital;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HospitalDAO {

    private final EnderecoDAO enderecoDAO = new EnderecoDAO();

    public Hospital buscarPorId(int id) {
        Hospital hospital = null;
        String sql = "SELECT id_hospital, razao_social, cnpj, email, telefone, categoria, id_endereco,cnes FROM hospital WHERE id_hospital = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Endereco endereco = enderecoDAO.buscarPorId(rs.getInt("id_endereco"));
                    hospital = new Hospital(
                            rs.getInt("id_hospital"),
                            rs.getString("razao_social"),
                            rs.getString("cnpj"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            rs.getString("categoria"),
                            endereco,
                            rs.getString("cnes"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar hospital pelo ID.", e);
        }

        return hospital;
    }

    public void salvar(Hospital hospital) {
        String sqlHospital = hospital.getIdHospital() != 0
                ? "UPDATE HOSPITAL SET RAZAO_SOCIAL = ?, CNPJ = ?, EMAIL = ?, TELEFONE = ?, CATEGORIA = ?, ID_ENDERECO = ?, CNES = ? WHERE ID_HOSPITAL = ?"
                : "INSERT INTO HOSPITAL (RAZAO_SOCIAL, CNPJ, EMAIL, TELEFONE, CATEGORIA, ID_ENDERECO, CNES) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmtHospital = conexao.prepareStatement(sqlHospital,
                        Statement.RETURN_GENERATED_KEYS)) {

            stmtHospital.setString(1, hospital.getRazaoSocial());
            stmtHospital.setString(2, hospital.getCnpj());
            stmtHospital.setString(3, hospital.getEmail());
            stmtHospital.setString(4, hospital.getTelefone());
            stmtHospital.setString(5, hospital.getCategoria());
            stmtHospital.setInt(6, hospital.getEndereco().getIdEndereco());
            stmtHospital.setString(7, hospital.getCnes());

            if (hospital.getIdHospital() != 0) {
                stmtHospital.setInt(8, hospital.getIdHospital());
            }

            stmtHospital.executeUpdate();

            // Obtém o ID do hospital inserido
            if (hospital.getIdHospital() == 0) {
                try (ResultSet generatedKeys = stmtHospital.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        hospital.setIdHospital(generatedKeys.getInt(1));
                    }
                }
            }

            // Adiciona o usuário na tabela usuario
            inserirUsuario(hospital.getCnpj(), "123", "HOSPITAL", null, hospital.getIdHospital());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar hospital", e);
        }
    }

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

    public void excluir(int id) {
        String sql = "DELETE FROM hospital WHERE id_hospital = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir o hospital.", e);
        }
    }

    public List<Hospital> listarTodos() {
        List<Hospital> hospitais = new ArrayList<>();
        String sql = "SELECT id_hospital, razao_social, cnpj, email, telefone, categoria, id_endereco, cnes FROM hospital";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Endereco endereco = enderecoDAO.buscarPorId(rs.getInt("id_endereco"));
                Hospital hospital = new Hospital(
                        rs.getInt("id_hospital"),
                        rs.getString("razao_social"),
                        rs.getString("cnpj"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("categoria"),
                        endereco,
                        rs.getString("cnes"));
                hospitais.add(hospital);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar hospitais.", e);
        }

        return hospitais;
    }

    public List<Hospital> listarPorPagina(int page, int pageSize) {
        List<Hospital> hospitais = new ArrayList<>();
        String sql = "SELECT id_hospital, razao_social, cnpj, email, telefone, categoria, id_endereco, cnes FROM hospital LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Endereco endereco = enderecoDAO.buscarPorId(rs.getInt("id_endereco"));
                    Hospital hospital = new Hospital(
                            rs.getInt("id_hospital"),
                            rs.getString("razao_social"),
                            rs.getString("cnpj"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            rs.getString("categoria"),
                            endereco,
                            rs.getString("cnes"));
                    hospitais.add(hospital);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar hospitais por página.", e);
        }

        return hospitais;
    }

    public int contarHospitais() {
        String sql = "SELECT COUNT(*) FROM hospital";
        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar hospitais.", e);
        }
        return 0;
    }

}
