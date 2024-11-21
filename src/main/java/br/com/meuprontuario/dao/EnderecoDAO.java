package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Endereco;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EnderecoDAO {

    public Endereco buscarPorId(int id) {
        String sql = "SELECT * FROM ENDERECO WHERE ID_ENDERECO = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Endereco(
                            rs.getInt("ID_ENDERECO"),
                            rs.getString("LOGRADOURO"),
                            rs.getString("NUMERO"),
                            rs.getString("BAIRRO"),
                            rs.getString("CIDADE"),
                            rs.getString("ESTADO"),
                            rs.getString("CEP"),
                            rs.getInt("CODIGO_MUNICIPIO"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar endereço por ID", e);
        }

        return null;
    }

    public void salvar(Endereco endereco) {
        String sql = endereco.getIdEndereco() != 0
                ? "UPDATE ENDERECO SET LOGRADOURO = ?, NUMERO = ?, BAIRRO = ?, CIDADE = ?, ESTADO = ?, CEP = ?, CODIGO_MUNICIPIO = ? WHERE ID_ENDERECO = ?"
                : "INSERT INTO ENDERECO (LOGRADOURO, NUMERO, BAIRRO, CIDADE, ESTADO, CEP, CODIGO_MUNICIPIO) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, endereco.getLogradouro());
            stmt.setString(2, endereco.getNumero());
            stmt.setString(3, endereco.getBairro());
            stmt.setString(4, endereco.getCidade());
            stmt.setString(5, endereco.getEstado());
            stmt.setString(6, endereco.getCep());
            stmt.setInt(7, endereco.getCodigoMunicipio());

            if (endereco.getIdEndereco() != 0) {
                stmt.setInt(8, endereco.getIdEndereco());
            }

            stmt.executeUpdate();

            if (endereco.getIdEndereco() == 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        endereco.setId_endereco(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o endereço", e);
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM endereco WHERE id_endereco = ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir o endereço.", e);
        }
    }

    public List<Endereco> listarTodos() {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT id_endereco, logradouro, numero, bairro, cidade, estado, cep FROM endereco";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Endereco endereco = new Endereco(
                        rs.getInt("id_endereco"),
                        rs.getString("logradouro"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("cep"),
                        rs.getInt("CODIGO_MUNICIPIO")

                );
                enderecos.add(endereco);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar endereços.", e);
        }

        return enderecos;
    }
}
