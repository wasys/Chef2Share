package br.com.chef2share.enums;

/**
 * Created by Jonas on 16/11/2015.
 */
public enum TipoCozinha {

    SELECIONE("-- Selecione --"),
    CAFE("Café"),
    ALMOCO("Almoço"),
    JANTAR("Jantar"),
    CURSO("Curso"),
    AULA_SHOW("Aula show");

    private String s;

    TipoCozinha(String i){
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
