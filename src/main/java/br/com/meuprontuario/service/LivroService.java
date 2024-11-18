package br.com.meuprontuario.service;

import br.com.meuprontuario.entity.Livro;
import br.com.meuprontuario.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public void salvar(Livro livro) {
        this.livroRepository.save(livro);
    }

    public List<Livro> encontrarTodos() {
        return this.livroRepository.findAll();
    }

    public Page<Livro> encontrarPaginado(int pagina, int tamanho) {
        return this.livroRepository.findAll(PageRequest.of(pagina, tamanho));
    }

    public Livro encontrarPorId(Long id) {
        return this.livroRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        this.livroRepository.deleteById(id);
    }
}
