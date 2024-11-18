package br.com.meuprontuario.model;

public class Livro {

    private Long id;
    private String nome;
    private Autor autor;

    public Livro() {
    }

    public Livro(Long id, String nome, Autor autor) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}

