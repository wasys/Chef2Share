package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;

import br.com.chef2share.enums.TipoIngresso;

public class Ingresso implements Serializable {

    private String id;
    private TipoIngresso tipo;
    private String status;
    private int quantidade;
    private double valorUnitario;
    private List<Convite> convites, entradas;

    public Ingresso(){}

    public Ingresso(TipoIngresso tipo, int qtde) {
        this.tipo = tipo;
        this.quantidade = qtde;
    }

    public TipoIngresso getTipo() {
        return tipo;
    }

    public void setTipo(TipoIngresso tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public List<Convite> getConvites() {
        return convites;
    }

    public void setConvites(List<Convite> convites) {
        this.convites = convites;
    }

    public List<Convite> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Convite> entradas) {
        this.entradas = entradas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
