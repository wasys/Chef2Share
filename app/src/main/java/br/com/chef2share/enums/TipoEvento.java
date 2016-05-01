package br.com.chef2share.enums;

/**
 * Created by Jonas on 16/11/2015.
 */
public enum TipoEvento {

    SELECIONE("-- Selecione --"),
    CAFE("Café"),
    ALMOCO("Almoço"),
    JANTAR("Jantar"),
    CURSO("Curso"),
    VOUCHER("Voucher"),
    AULA_SHOW("Aula show");

    private String s;

    TipoEvento(String i){
        this.s = i;
    }

    public String getStringLabel() {
        return s;
    }

    @Override
    public String toString() {
        return getStringLabel();
    }
}
