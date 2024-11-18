package br.com.meuprontuario.repository;

import br.com.meuprontuario.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
}
