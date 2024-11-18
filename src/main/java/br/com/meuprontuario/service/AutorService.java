package br.com.meuprontuario.service;

import br.com.meuprontuario.entity.Autor;
import br.com.meuprontuario.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.util.Streamable;

import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> obterListaAutores() {
        Iterable<Autor> autorIterable = this.autorRepository.findAll();
        return Streamable.of(autorIterable).toList();
    }
}
