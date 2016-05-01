package br.com.chef2share.domain;

import java.io.Serializable;

public class ImagemCardapio implements Serializable {

    private String id;
    private boolean principal;
    private boolean divulgacao;
    private Imagem imagem;

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public boolean isDivulgacao() {
        return divulgacao;
    }

    public void setDivulgacao(boolean divulgacao) {
        this.divulgacao = divulgacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }
}
