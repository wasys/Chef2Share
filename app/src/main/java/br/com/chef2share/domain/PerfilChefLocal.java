package br.com.chef2share.domain;

import java.io.Serializable;

import br.com.chef2share.enums.Genero;

/**
 *
 */
public class PerfilChefLocal implements Serializable{

    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return label;
    }

    public static PerfilChefLocal getDefault() {
        PerfilChefLocal pLocal = new PerfilChefLocal();
        pLocal.setLabel("-- Selecione --");
        return pLocal;
    }
}
