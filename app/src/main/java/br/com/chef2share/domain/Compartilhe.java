package br.com.chef2share.domain;

import java.io.Serializable;

/**
 * Created by Jonas on 23/11/2015.
 */
public class Compartilhe implements Serializable {

    private String facebook;

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}
