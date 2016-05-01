package br.com.chef2share.domain;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by Jonas on 21/11/2015.
 */
public class Detalhes implements Serializable{

    private Evento evento;
    private Compartilhe compartilhe;
    private String restantes;
    private List<Usuario> participantes;
    private List<Evento> outrosEventos;
    private List<Avaliacao> avaliadores;
    private List<OutrasDatas> outrasDatas;
    private Passo1 passo1;
    private Passo2 passo2;
    private Passo3 passo3;
    private Map<String, String> dicionario;

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Map<String, String> getDicionario() {
        return dicionario;
    }

    public void setDicionario(Map<String, String> dicionario) {
        this.dicionario = dicionario;
    }

    public Passo1 getPasso1() {
        return passo1;
    }

    public void setPasso1(Passo1 passo1) {
        this.passo1 = passo1;
    }

    public Passo2 getPasso2() {
        return passo2;
    }

    public void setPasso2(Passo2 passo2) {
        this.passo2 = passo2;
    }

    public Passo3 getPasso3() {
        return passo3;
    }

    public void setPasso3(Passo3 passo3) {
        this.passo3 = passo3;
    }

    public Compartilhe getCompartilhe() {
        return compartilhe;
    }

    public void setCompartilhe(Compartilhe compartilhe) {
        this.compartilhe = compartilhe;
    }

    public String getRestantes() {
        return restantes;
    }

    public void setRestantes(String restantes) {
        this.restantes = restantes;
    }

    public List<Usuario> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Usuario> participantes) {
        this.participantes = participantes;
    }

    public List<OutrasDatas> getOutrasDatas() {
        return outrasDatas;
    }

    public void setOutrasDatas(List<OutrasDatas> outrasDatas) {
        this.outrasDatas = outrasDatas;
    }

    public List<Evento> getOutrosEventos() {
        return outrosEventos;
    }

    public void setOutrosEventos(List<Evento> outrosEventos) {
        this.outrosEventos = outrosEventos;
    }

    public List<Avaliacao> getAvaliadores() {
        return avaliadores;
    }

    public void setAvaliadores(List<Avaliacao> avaliadores) {
        this.avaliadores = avaliadores;
    }

    public String getQuantidadeConfirmada(){
        return String.valueOf(passo3.getMaximo() - Integer.parseInt(getRestantes()));
    }

    public String getOutrasDatasPorExtenso() {
        if(outrasDatas == null || outrasDatas.size() <= 0){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        ListIterator<OutrasDatas> it = outrasDatas.listIterator();
        while (it.hasNext()){
            sb.append(it.next().getDataPorExtenso());
            if(it.hasNext()){
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}
