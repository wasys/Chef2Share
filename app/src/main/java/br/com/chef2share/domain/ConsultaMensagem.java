package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;

public class ConsultaMensagem implements Serializable {

    private List<Mensagem> mensagens;

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
}