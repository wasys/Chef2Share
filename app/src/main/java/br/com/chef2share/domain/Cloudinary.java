package br.com.chef2share.domain;

import java.io.Serializable;

/**
 * Created by Jonas on 18/11/2015.
 */
public class Cloudinary implements Serializable {

    private String name;
    private String apiKey;
    private String apiSecret;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }
}
