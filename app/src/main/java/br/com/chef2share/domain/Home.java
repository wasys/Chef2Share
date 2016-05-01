package br.com.chef2share.domain;

import java.util.List;

/**
 * Created by Jonas on 19/11/2015.
 */
public class Home {

    private String banner;
    private List<String> titulos;
    private List<EventoHome> eventos;


    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public List<String> getTitulos() {
        return titulos;
    }

    public void setTitulos(List<String> titulos) {
        this.titulos = titulos;
    }

    public List<EventoHome> getEventosHome() {
        return eventos;
    }

    public void setEventosHome(List<EventoHome> eventos) {
        this.eventos = eventos;
    }
}
