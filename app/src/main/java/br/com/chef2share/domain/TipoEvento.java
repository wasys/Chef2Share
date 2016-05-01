package br.com.chef2share.domain;

/**
 * Created by Jonas on 20/11/2015.
 */
public class TipoEvento {

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
}
