package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;

public class Reserva implements Serializable{

    private String url;
    private Pedido pedido;
    private List<Ingresso> ingressos;

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

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public void setIngressos(List<Ingresso> ingressos) {
        this.ingressos = ingressos;
    }
}
