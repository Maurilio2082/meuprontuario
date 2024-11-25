package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Cid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CidDAO {

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
}
