package br.com.chef2share.domain;

import java.io.Serializable;

/**
 *
 */
public class PerfilChefTratamento implements Serializable{

    private String label;
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return label;
    }

    public static PerfilChefTratamento getDefault() {
        PerfilChefTratamento pTratamento = new PerfilChefTratamento();
        pTratamento.setLabel("-- Selecione --");
        return pTratamento;
    }
}
