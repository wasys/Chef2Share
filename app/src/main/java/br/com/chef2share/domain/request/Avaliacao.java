package br.com.chef2share.domain.request;

import java.io.Serializable;

import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Pedido;

/**
 * Created by Jonas on 21/11/2015.
 */
public class Avaliacao implements Serializable {

    private float local;
    private float comida;
    private float atendimento;
    private String comentario;
    private Evento evento;

    public float getLocal() {
        return local;
    }

    public void setLocal(float local) {
        this.local = local;
    }

    public float getComida() {
        return comida;
    }

    public void setComida(float comida) {
        this.comida = comida;
    }

    public float getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(float atendimento) {
        this.atendimento = atendimento;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
