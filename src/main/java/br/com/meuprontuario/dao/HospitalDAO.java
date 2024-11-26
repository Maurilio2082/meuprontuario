package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Endereco;
import br.com.meuprontuario.model.Hospital;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável por gerenciar operações relacionadas
 * à entidade Hospital no banco de dados.
 */
@Repository
public class HospitalDAO {

    private final EnderecoDAO enderecoDAO = new EnderecoDAO(); // DAO auxiliar para manipular endereços

    /**
     * Busca um hospital pelo ID.
     *
     * @param id ID do hospital a ser buscado.
     * @return Objeto Hospital correspondente ao ID ou null se não encontrado.
     */
    public Hospital buscarPorId(int id) {
        Hospital hospital = null;
        String sql = "SELECT id_hospital, razao_social, cnpj, email, telefone, categoria, id_endereco, cnes FROM hospital WHERE id_hospital = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Busca o endereço associado ao hospital
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

    /**
     * Salva ou atualiza um hospital no banco de dados.
     *
     * @param hospital Objeto Hospital a ser salvo ou atualizado.
     */
    public void salvar(Hospital hospital) {
        String sqlHospital = hospital.getIdHospital() != 0
                ? "UPDATE HOSPITAL SET RAZAO_SOCIAL = ?, CNPJ = ?, EMAIL = ?, TELEFONE = ?, CATEGORIA = ?, ID_ENDERECO = ?, CNES = ? WHERE ID_HOSPITAL = ?"
                : "INSERT INTO HOSPITAL (RAZAO_SOCIAL, CNPJ, EMAIL, TELEFONE, CATEGORIA, ID_ENDERECO, CNES) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmtHospital = conexao.prepareStatement(sqlHospital,
                        Statement.RETURN_GENERATED_KEYS)) {

            // Preenche os parâmetros do SQL
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

            // Obtém o ID gerado automaticamente para um novo registro
            if (hospital.getIdHospital() == 0) {
                try (ResultSet generatedKeys = stmtHospital.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        hospital.setIdHospital(generatedKeys.getInt(1));
                    }
                }
                // Insere o hospital como usuário apenas para novos registros
                inserirUsuario(hospital.getCnpj(), "123", "HOSPITAL", null, hospital.getIdHospital());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar hospital", e);
        }
    }

    /**
     * Insere um usuário associado a um hospital.
     *
     * @param login      Login do usuário.
     * @param senha      Senha do usuário.
     * @param tipo       Tipo de usuário (ex.: HOSPITAL).
     * @param idPaciente ID do paciente associado (pode ser null).
     * @param idHospital ID do hospital associado.
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
     * Exclui um hospital pelo ID.
     *
     * @param id ID do hospital a ser excluído.
     */
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

    /**
     * Lista todos os hospitais cadastrados.
     *
     * @return Lista de objetos Hospital representando todos os registros.
     */
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

    /**
     * Lista hospitais de forma paginada.
     *
     * @param page     Número da página.
     * @param pageSize Tamanho da página (quantidade de registros).
     * @return Lista de objetos Hospital na página solicitada.
     */
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

    /**
     * Conta o número total de hospitais cadastrados.
     *
     * @return O número total de hospitais.
     */
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
