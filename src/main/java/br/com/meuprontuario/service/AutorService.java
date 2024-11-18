package br.com.meuprontuario.service;

import br.com.meuprontuario.dao.AutorDAO;
import br.com.meuprontuario.model.Autor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AutorService {
    private AutorDAO autorDAO = new AutorDAO();

    public List<Autor> obterListaAutores() {
        return autorDAO.listarTodos();
    }
}