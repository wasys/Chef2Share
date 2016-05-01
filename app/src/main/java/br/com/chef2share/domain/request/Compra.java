package br.com.chef2share.domain.request;

import java.util.List;

import br.com.chef2share.domain.Evento;

public class Compra {

    private Evento evento;
    private List<Ingresso> ingressos;

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public void setIngressos(List<Ingresso> ingressos) {
        this.ingressos = ingressos;
    }
}
