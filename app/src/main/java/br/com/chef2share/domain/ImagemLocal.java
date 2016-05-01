package br.com.chef2share.domain;

import java.io.Serializable;

public class ImagemLocal implements Serializable {

    private String id;
    private Imagem imagem;

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
