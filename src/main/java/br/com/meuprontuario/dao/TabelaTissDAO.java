package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.TabelaTiss;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
