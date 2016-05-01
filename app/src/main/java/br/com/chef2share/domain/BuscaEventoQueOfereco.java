package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jonas on 21/11/2015.
 */
public class BuscaEventoQueOfereco implements Serializable {

    private String periodoDe;
    private String periodoAte;
    private List<Evento> eventos;

    public String getPeriodoDe() {
        return periodoDe;
    }

    public void setPeriodoDe(String periodoDe) {
        this.periodoDe = periodoDe;
    }

    public String getPeriodoAte() {
        return periodoAte;
    }

    public void setPeriodoAte(String periodoAte) {
        this.periodoAte = periodoAte;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }
}
