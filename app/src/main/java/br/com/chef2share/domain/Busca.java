package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonas on 21/11/2015.
 */
public class Busca implements Serializable{

    private List<Evento> publicados;
    private Map<String, String> dicionario;

    public List<Evento> getPublicados() {
        return publicados;
    }

    public void setPublicados(List<Evento> publicados) {
        this.publicados = publicados;
    }

    public Map<String, String> getDicionario() {
        return dicionario;
    }

    public void setDicionario(Map<String, String> dicionario) {
        this.dicionario = dicionario;
    }
}
