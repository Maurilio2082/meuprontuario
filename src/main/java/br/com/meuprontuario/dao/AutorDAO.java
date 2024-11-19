package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    public void salvar(Autor autor) {
        String sql;
        boolean isUpdate = autor.getId() != null;

        if (isUpdate) {
            sql = "UPDATE autor SET nome = ? WHERE id = ?";
        } else {
            sql = "INSERT INTO autor (nome) VALUES (?)";
        }

        try (Connection conn = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, autor.getNome());
            if (isUpdate) {
                stmt.setLong(2, autor.getId());
            }

            stmt.executeUpdate();

            if (!isUpdate) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        autor.setId(generatedKeys.getLong(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o autor", e);
        }
    }

    public List<Autor> listarPorPagina(int page, int pageSize) {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT id, nome FROM autor LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;

        try (Connection conn = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Autor autor = new Autor();
                    autor.setId(rs.getLong("id"));
                    autor.setNome(rs.getString("nome"));
                    autores.add(autor);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar autores", e);
        }
        return autores;
    }

    public int contarAutores() {
        String sql = "SELECT COUNT(*) FROM autor";
        try (Connection conn = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar autores", e);
        }
        return 0;
    }

    public Autor buscarPorId(Long id) {
        String sql = "SELECT id, nome FROM autor WHERE id = ?";
        try (Connection conn = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Autor autor = new Autor();
                    autor.setId(rs.getLong("id"));
                    autor.setNome(rs.getString("nome"));
                    return autor;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar autor por ID", e);
        }
        return null;
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM autor WHERE id = ?";
        try (Connection conn = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir autor", e);
        }
    }


    public List<Autor> listarTodos() {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT id, nome FROM autor";

        try (Connection conn = ConfiguracaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Autor autor = new Autor();
                autor.setId(rs.getLong("id"));
                autor.setNome(rs.getString("nome"));
                autores.add(autor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autores;
    }
}
