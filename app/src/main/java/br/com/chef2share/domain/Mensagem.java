package br.com.chef2share.domain;

import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by Jonas on 20/11/2015.
 */
public class Mensagem implements Serializable {

    private String id;
    private String data;
    private boolean lida;
    private String conteudo;
    private Usuario remetente;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

    public String getDataString() {
        String t =  StringUtils.replace(data, "T", " ");
        t = StringUtils.replace(t, "UTC", "");
        t = DateUtils.toString(DateUtils.toDate(t, "yyyy-MM-dd HH:mm:ss"), DateUtils.DATE_TIME_24h);
        return t;

    }
}