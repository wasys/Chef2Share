package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;


public class Cardapio implements Serializable {

    private String id;
    private String tipo;
    private String titulo;
    private String bebida;
    private String descricao;
    private List<ImagemCardapio> imagensCardapio;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getBebida() {
        return bebida;
    }

    public void setBebida(String bebida) {
        this.bebida = bebida;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<ImagemCardapio> getImagensCardapio() {
        return imagensCardapio;
    }

    public void setImagensCardapio(List<ImagemCardapio> imagensCardapio) {
        this.imagensCardapio = imagensCardapio;
    }
}
