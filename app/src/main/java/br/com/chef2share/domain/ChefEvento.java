package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ChefEvento implements Serializable{

    private List<Evento> eventos;
    private Map<String, String> dicionario;


    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public Map<String, String> getDicionario() {
        return dicionario;
    }

    public void setDicionario(Map<String, String> dicionario) {
        this.dicionario = dicionario;
    }
}
