package br.com.chef2share.domain;

import com.android.utils.lib.utils.StringUtils;

/**
 * Created by Jonas on 20/11/2015.
 */
public class Valor {

    private String value;
    private String label;

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
        if(StringUtils.isNotEmpty(value)){
            return "R$ " + label;
        }
        return label;
    }
}
