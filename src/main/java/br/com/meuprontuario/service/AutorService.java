package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.AutorDAO;
import br.com.meuprontuario.model.Autor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {
    private AutorDAO autorDAO = new AutorDAO();

    public void salvar(Autor autor) {
        if (autor.getNome() == null || autor.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome do autor não pode ser vazio.");
        }
        autorDAO.salvar(autor);
    }

    public List<Autor> listarPorPagina(int page, int pageSize) {
        return autorDAO.listarPorPagina(page, pageSize);
    }

    public List<Autor> obterListaAutores() {
        return autorDAO.listarTodos();
    }

    public int contarAutores() {
        return autorDAO.contarAutores();
    }

    public Autor buscarPorId(Long id) {
        return autorDAO.buscarPorId(id);
    }

    public void excluir(Long id) {
        autorDAO.excluir(id);
    }
}
