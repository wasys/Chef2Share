package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;


public class Cadastro implements Serializable {

    private List<Cardapio> cardapios;
    private Evento evento;
    private List<Local> locais;

    public List<Cardapio> getCardapios() {
        return cardapios;
    }

    public void setCardapios(List<Cardapio> cardapios) {
        this.cardapios = cardapios;
    }

    public List<Local> getLocais() {
        return locais;
    }

    public void setLocais(List<Local> locais) {
        this.locais = locais;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
