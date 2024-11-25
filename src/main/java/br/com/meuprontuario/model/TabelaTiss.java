package br.com.meuprontuario.model;

public class TabelaTiss {
    private long codigoTermo;
    private String descricao;

    // Construtores
    public TabelaTiss() {
    }

    public TabelaTiss(long codigoTermo, String descricao) {
        this.codigoTermo = codigoTermo;
        this.descricao = descricao;
    }

    // Getters e Setters
    public long getCodigoTermo() {
        return codigoTermo;
    }

    public void setCodigoTermo(long codigoTermo) {
        this.codigoTermo = codigoTermo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
