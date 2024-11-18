package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.LivroDAO;
import br.com.meuprontuario.model.Livro;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LivroService {

    private LivroDAO livroDAO = new LivroDAO();

    public void salvar(Livro livro) {
        if (livro.getNome() == null || livro.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome do livro não pode ser vazio.");
        }
        if (livro.getAutor() == null || livro.getAutor().getId() == null) {
            throw new IllegalArgumentException("É necessário selecionar um autor.");
        }
        livroDAO.salvar(livro);
    }

    public List<Livro> listarTodos() {
        return livroDAO.listarTodos();
    }

    public Livro buscarPorId(Long id) {
        return livroDAO.buscarPorId(id);
    }

    public void excluir(Long id) {
        livroDAO.excluir(id);
    }
}
