package br.com.chef2share.enums;

/**
 * Created by Jonas on 16/11/2015.
 */
public enum TipoPrivacidadeEvento {

    SELECIONE("-- Selecione --"),
    PUBLICO("Público"),
    PRIVADO("Privado");

    private String s;

    TipoPrivacidadeEvento(String i){
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
