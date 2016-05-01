package br.com.chef2share.enums;

import br.com.chef2share.R;

/**
 * Created by Jonas on 16/11/2015.
 */
public enum Genero {

    MASCULINO("Masculino"),
    FEMININO("Feminino"),
    OUTROS("Outros");

    private String s;

    Genero(String i){
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
