package br.com.chef2share.domain;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Jonas on 21/11/2015.
 */
public class ImagemPasso implements Serializable {

    private String id;
    private String temp;
    private File tempFile;
    private boolean principal;
    private boolean divulgacao;
    private Imagem imagem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public boolean isDivulgacao() {
        return divulgacao;
    }

    public void setDivulgacao(boolean divulgacao) {
        this.divulgacao = divulgacao;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public File getTempFile() {
        return tempFile;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }
}
