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
        String sql = hospital.getIdHospital() != 0
                ? "UPDATE HOSPITAL SET RAZAO_SOCIAL = ?, CNPJ = ?, EMAIL = ?, TELEFONE = ?, CATEGORIA = ?, ID_ENDERECO = ?, CNES = ? WHERE ID_HOSPITAL = ?"
                : "INSERT INTO HOSPITAL (RAZAO_SOCIAL, CNPJ, EMAIL, TELEFONE, CATEGORIA, ID_ENDERECO, CNES) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, hospital.getRazaoSocial());
            stmt.setString(2, hospital.getCnpj());
            stmt.setString(3, hospital.getEmail());
            stmt.setString(4, hospital.getTelefone());
            stmt.setString(5, hospital.getCategoria());
            stmt.setInt(6, hospital.getEndereco().getIdEndereco());
            stmt.setString(7, hospital.getCnes()); // CNES

            if (hospital.getIdHospital() != 0) {
                stmt.setInt(8, hospital.getIdHospital());
            }

            stmt.executeUpdate();

            if (hospital.getIdHospital() == 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        hospital.setIdHospital(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar hospital", e);
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
