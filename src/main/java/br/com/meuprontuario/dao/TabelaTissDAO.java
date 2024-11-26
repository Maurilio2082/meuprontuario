package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.TabelaTiss;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository

public class TabelaTissDAO {

    public List<TabelaTiss> listarTodos() {
        List<TabelaTiss> tissList = new ArrayList<>();
        String sql = "SELECT * FROM tabelatiss";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TabelaTiss tiss = new TabelaTiss(
                        rs.getLong("codigo_termo"),
                        rs.getString("descricao"));
                tissList.add(tiss);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Tabela TISS.", e);
        }

        return tissList;
    }

    public TabelaTiss buscarPorCodigo(long codigoTermo) {
        String sql = "SELECT * FROM tabelatiss WHERE codigo_termo = ?";
        TabelaTiss tiss = null;

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, codigoTermo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tiss = new TabelaTiss(
                            rs.getLong("codigo_termo"),
                            rs.getString("descricao"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Tabela TISS.", e);
        }

        return tiss;
    }

    public List<TabelaTiss> listarPorPagina(int offset, int limit) {
        List<TabelaTiss> tissList = new ArrayList<>();
        String sql = "SELECT * FROM tabelatiss LIMIT ? OFFSET ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TabelaTiss tiss = new TabelaTiss(
                            rs.getLong("codigo_termo"),
                            rs.getString("descricao"));
                    tissList.add(tiss);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Tabela TISS por página.", e);
        }

        return tissList;
    }

    public List<TabelaTiss> listarPorTermo(String termo, int offset, int limit) {
        List<TabelaTiss> tissList = new ArrayList<>();
        String sql = "SELECT * FROM tabelatiss WHERE descricao LIKE ? OR codigo_termo LIKE ? LIMIT ? OFFSET ?";

        try (Connection conexao = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            String filtro = "%" + termo + "%";
            stmt.setString(1, filtro);
            stmt.setString(2, filtro);
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TabelaTiss tiss = new TabelaTiss(
                            rs.getLong("codigo_termo"),
                            rs.getString("descricao"));
                    tissList.add(tiss);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Tabela TISS com filtro.", e);
        }

        return tissList;
    }

}
