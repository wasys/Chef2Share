package br.com.chef2share.domain.request;

import java.util.List;

import br.com.chef2share.domain.Evento;

public class PedidoFiltro {

    private String periodoDe;
    private String periodoAte;

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
}
