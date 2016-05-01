package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by Jonas on 21/11/2015.
 */
public class Resumo implements Serializable{

    private Pedido pedido;
    private String url;
    private Map<String, String> dicionario;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Map<String, String> getDicionario() {
        return dicionario;
    }

    public void setDicionario(Map<String, String> dicionario) {
        this.dicionario = dicionario;
    }
}
