package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonas on 21/11/2015.
 */
public class UploadImagem implements Serializable{

    private String extensao;
    private String base64;

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
