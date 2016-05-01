package br.com.chef2share.domain;

import com.android.utils.lib.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by Jonas on 20/11/2015.
 */
public class Cozinha implements Serializable {

    private String label;
    private String value;
    private String id;
    private String descricao;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        if(StringUtils.isNotEmpty(descricao)){
            return descricao;
        }
        return label;
    }
}
