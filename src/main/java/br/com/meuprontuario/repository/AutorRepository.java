package br.com.meuprontuario.repository;

import br.com.meuprontuario.entity.Autor;
import org.springframework.data.repository.CrudRepository;

public interface AutorRepository extends CrudRepository<Autor, Long> {
}
