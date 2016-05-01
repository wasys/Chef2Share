package br.com.chef2share.domain.request;

import br.com.chef2share.domain.Evento;
import br.com.chef2share.enums.TipoIngresso;

public class Ingresso {

    private TipoIngresso tipo;
    private int quantidade;

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
}
