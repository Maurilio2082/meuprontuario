package br.com.meuprontuario.model;

public class Cid {
    private String codCid;
    private String descricao;
    private String descricaoAbreviada;

    // Construtores
    public Cid() {
    }

    public Cid(String codCid, String descricao, String descricaoAbreviada) {
        this.codCid = codCid;
        this.descricao = descricao;
        this.descricaoAbreviada = descricaoAbreviada;
    }

    // Getters e Setters
    public String getCodCid() {
        return codCid;
    }

    public void setCodCid(String codCid) {
        this.codCid = codCid;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricaoAbreviada() {
        return descricaoAbreviada;
    }

    public void setDescricaoAbreviada(String descricaoAbreviada) {
        this.descricaoAbreviada = descricaoAbreviada;
    }
}
