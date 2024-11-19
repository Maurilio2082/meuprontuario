package br.com.meuprontuario.dao;

import br.com.meuprontuario.config.ConfiguracaoBanco;
import br.com.meuprontuario.model.Autor;
import br.com.meuprontuario.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class LivroDAO {

    public List<Livro> listarPorPagina(int page, int pageSize) {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT l.id, l.nome, a.id AS autor_id, a.nome AS autor_nome " +
                "FROM livros l JOIN autor a ON l.autor_id = a.id " +
                "LIMIT ? OFFSET ?";

        int offset = (page - 1) * pageSize;

        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = new Livro();
                    livro.setId(rs.getLong("id"));
                    livro.setNome(rs.getString("nome"));

                    Autor autor = new Autor(rs.getLong("autor_id"), rs.getString("autor_nome"));
                    livro.setAutor(autor);

                    livros.add(livro);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livros;
    }

    public int contarLivros() {
        String sql = "SELECT COUNT(*) FROM livros";
        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Retorna 0 se houver erro
    }

    public Livro buscarPorId(Long id) {
        String sql = "SELECT l.id, l.nome, a.id AS autor_id, a.nome AS autor_nome FROM livros l "
                + "JOIN autor a ON l.autor_id = a.id WHERE l.id = ?";

        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = new Livro();
                    livro.setId(rs.getLong("id"));
                    livro.setNome(rs.getString("nome"));

                    Autor autor = new Autor();
                    autor.setId(rs.getLong("autor_id"));
                    autor.setNome(rs.getString("autor_nome"));

                    livro.setAutor(autor);

                    return livro;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o livros por ID", e);
        }

        return null; // Retorna null caso o livro não seja encontrado
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir o livro", e);
        }
    }

    public void salvar(Livro livro) {
        String sql;
        boolean isUpdate = livro.getId() != null; // Verifica se é atualização ou inserção

        if (isUpdate) {
            sql = "UPDATE livros SET nome = ?, autor_id = ? WHERE id = ?";
        } else {
            sql = "INSERT INTO livros (nome, autor_id) VALUES (?, ?)";
        }

        try (Connection conn = ConfiguracaoBanco.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, livro.getNome());
            stmt.setLong(2, livro.getAutor().getId());

            if (isUpdate) {
                stmt.setLong(3, livro.getId()); // Aqui você está garantindo que o ID seja passado para a atualização
            }

            stmt.executeUpdate();

            if (!isUpdate) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        livro.setId(generatedKeys.getLong(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o livro", e);
        }
    }

}
