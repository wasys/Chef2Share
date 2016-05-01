package br.com.chef2share.domain;

import java.io.Serializable;

/**
 * Created by Jonas on 18/11/2015.
 */
public class Imagem implements Serializable{

    private String id;
    private String url;
    private String temp;
    private Cloudinary cloudinary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    public void setCloudinary(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
