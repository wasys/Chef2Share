package br.com.chef2share.domain.request;

import br.com.chef2share.domain.Usuario;

public class RespostaMsg {

    private Usuario destinatario;
    private String conteudo;

    public RespostaMsg(Usuario destinatario, String conteudo){
        this.destinatario = destinatario;
        this.conteudo = conteudo;
    }

    public RespostaMsg(){}

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
