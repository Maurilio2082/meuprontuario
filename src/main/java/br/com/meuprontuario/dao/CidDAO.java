package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Cid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * DAO (Data Access Object) para gerenciar operações relacionadas à entidade CID no banco de dados.
 */
@Repository
public class CidDAO {

    /**
     * Lista todos os registros da tabela CID.
     *
     * @return Lista de objetos Cid representando todos os registros da tabela.
     */
    public List<Cid> listarTodos() {
        List<Cid> cids = new ArrayList<>();
        String sql = "SELECT * FROM cid";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cid cid = new Cid(
                        rs.getString("cod_cid"),
                        rs.getString("descricao"),
                        rs.getString("descricao_abreviada"));
                cids.add(cid);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar CIDs.", e);
        }

        return cids;
    }

    /**
     * Busca um registro da tabela CID pelo código.
     *
     * @param codCid Código do CID a ser buscado.
     * @return Objeto Cid representando o registro encontrado ou null caso não exista.
     */
    public Cid buscarPorCodigo(String codCid) {
        String sql = "SELECT * FROM cid WHERE cod_cid = ?";
        Cid cid = null;

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, codCid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cid = new Cid(
                            rs.getString("cod_cid"),
                            rs.getString("descricao"),
                            rs.getString("descricao_abreviada"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar CID.", e);
        }

        return cid;
    }

    /**
     * Lista os registros da tabela CID de forma paginada.
     *
     * @param offset Quantidade de registros a pular (início da página).
     * @param limit  Quantidade de registros por página.
     * @return Lista de objetos Cid representando os registros da página.
     */
    public List<Cid> listarPorPagina(int offset, int limit) {
        List<Cid> cids = new ArrayList<>();
        String sql = "SELECT * FROM cid LIMIT ? OFFSET ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cid cid = new Cid(
                            rs.getString("cod_cid"),
                            rs.getString("descricao"),
                            rs.getString("descricao_abreviada"));
                    cids.add(cid);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar CIDs por página.", e);
        }

        return cids;
    }

    /**
     * Lista os registros da tabela CID com base em um termo de busca e de forma paginada.
     *
     * @param termo  Termo de busca (filtro) a ser aplicado.
     * @param offset Quantidade de registros a pular (início da página).
     * @param limit  Quantidade de registros por página.
     * @return Lista de objetos Cid representando os registros filtrados.
     */
    public List<Cid> listarPorTermo(String termo, int offset, int limit) {
        List<Cid> cids = new ArrayList<>();
        String sql = "SELECT * FROM cid WHERE descricao_abreviada LIKE ? OR cod_cid LIKE ? LIMIT ? OFFSET ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            String filtro = "%" + termo + "%"; // Adiciona wildcards para busca parcial
            stmt.setString(1, filtro);
            stmt.setString(2, filtro);
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cid cid = new Cid(
                            rs.getString("cod_cid"),
                            rs.getString("descricao"),
                            rs.getString("descricao_abreviada"));
                    cids.add(cid);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar CIDs com filtro.", e);
        }

        return cids;
    }
}
