package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jonas on 21/11/2015.
 */
public class BuscaVO implements Serializable{

    private Filtro filtro;
    private List<Evento> eventos;

    public Filtro getFiltro() {
        return filtro;
    }

    public void setFiltro(Filtro filtro) {
        this.filtro = filtro;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }
}
