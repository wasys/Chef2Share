package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonas on 21/11/2015.
 */
public class AcompanhaEvento implements Serializable{

    private String pagos;
    private String reservados;
    private String visualizados;
    private boolean checkinEnabled;
    private Evento evento;
    private List<Usuario> participantes;
    private Map<String, String> dicionario;

    public String getPagos() {
        return pagos;
    }

    public void setPagos(String pagos) {
        this.pagos = pagos;
    }

    public String getReservados() {
        return reservados;
    }

    public void setReservados(String reservados) {
        this.reservados = reservados;
    }

    public String getVisualizados() {
        return visualizados;
    }

    public void setVisualizados(String visualizados) {
        this.visualizados = visualizados;
    }

    public boolean isCheckinEnabled() {
        return checkinEnabled;
    }

    public void setCheckinEnabled(boolean checkinEnabled) {
        this.checkinEnabled = checkinEnabled;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<Usuario> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Usuario> participantes) {
        this.participantes = participantes;
    }

    public Map<String, String> getDicionario() {
        return dicionario;
    }

    public void setDicionario(Map<String, String> dicionario) {
        this.dicionario = dicionario;
    }
}
