package br.com.chef2share.domain;

import com.android.utils.lib.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jonas on 20/11/2015.
 */
public class Avaliacao implements Serializable {

    private String data;
    private String nome;
    private String titulo;
    private String comentario;
    private Imagem imagem;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    public String getDataFormatada(){
        return DateUtils.toString(DateUtils.toDate(data, "yyyy-MM-dd"), DateUtils.DATE);
    }
}
