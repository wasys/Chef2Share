package br.com.chef2share.domain;

import com.android.utils.lib.utils.StringUtils;

import java.io.Serializable;
import java.util.Date;

import br.com.chef2share.SuperApplication;
import br.com.chef2share.fragment.FragmentPasso;

/**
 * Created by Jonas on 20/11/2015.
 */
public class Evento implements Serializable {

    private String id;
    private String titulo;
    private String status;
    private Imagem imagem;
    private String dataCriacao;
    private String dataPublicacao;
    private double avaliacaoMedia;
    private double avaliacaoLocal;
    private double avaliacaoComida;
    private double avaliacaoAtendimento;
    private String totalAvaliadores;
    private String totalComentarios;
    private Chef chef;
    private Passo1 passo1;
    private Passo2 passo2;
    private Passo3 passo3;
    private int comprados;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public double getAvaliacaoLocal() {
        return avaliacaoLocal;
    }

    public void setAvaliacaoLocal(double avaliacaoLocal) {
        this.avaliacaoLocal = avaliacaoLocal;
    }

    public double getAvaliacaoComida() {
        return avaliacaoComida;
    }

    public void setAvaliacaoComida(double avaliacaoComida) {
        this.avaliacaoComida = avaliacaoComida;
    }

    public double getAvaliacaoAtendimento() {
        return avaliacaoAtendimento;
    }

    public void setAvaliacaoAtendimento(double avaliacaoAtendimento) {
        this.avaliacaoAtendimento = avaliacaoAtendimento;
    }

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public String getTotalAvaliadores() {
        return totalAvaliadores;
    }

    public int getComprados() {
        return comprados;
    }

    public void setComprados(int comprados) {
        this.comprados = comprados;
    }

    public void setTotalAvaliadores(String totalAvaliadores) {
        this.totalAvaliadores = totalAvaliadores;
    }

    public String getTotalComentarios() {
        return totalComentarios;
    }

    public void setTotalComentarios(String totalComentarios) {
        this.totalComentarios = totalComentarios;
    }

    public Passo1 getPasso1() {
        if (passo1 == null) {
            passo1 = new Passo1();
        }
        return passo1;
    }

    public void setPasso1(Passo1 passo1) {
        this.passo1 = passo1;
    }

    public Passo2 getPasso2() {
        if (passo2 == null) {
            passo2 = new Passo2();
        }
        return passo2;
    }

    public void setPasso2(Passo2 passo2) {
        this.passo2 = passo2;
    }

    public Passo3 getPasso3() {
        if (passo3 == null) {
            passo3 = new Passo3();
        }
        return passo3;
    }

    public void setPasso3(Passo3 passo3) {
        this.passo3 = passo3;
    }

    public boolean isPasso(FragmentPasso.Passo passo) {

        if (passo == FragmentPasso.Passo.PASSO_1) {
            if(!getPasso1().isInformacoesCompletas() && !getPasso2().isInformacoesCompletas() && !getPasso3().isInformacoesCompletas() && !isPublicado()){
                return true;
            }
        }

        if (passo == FragmentPasso.Passo.PASSO_2) {
            if(getPasso1().isInformacoesCompletas() && !getPasso2().isInformacoesCompletas() && !getPasso3().isInformacoesCompletas() && !isPublicado()){
                return true;
            }
        }

        if (passo == FragmentPasso.Passo.PASSO_3) {
            if(getPasso1().isInformacoesCompletas() && getPasso2().isInformacoesCompletas() && !getPasso3().isInformacoesCompletas() && !isPublicado()){
                return true;
            }
        }
        if (passo == FragmentPasso.Passo.PASSO_4) {
            if(getPasso1().isInformacoesCompletas() && getPasso2().isInformacoesCompletas() && getPasso3().isInformacoesCompletas() && !isPublicado()){
                return true;
            }
        }

        if (passo == FragmentPasso.Passo.PASSO_5) {
            if(getPasso1().isInformacoesCompletas() && getPasso2().isInformacoesCompletas() && getPasso3().isInformacoesCompletas() && isPublicado()){
                return true;
            }
        }

        return false;
    }

    public boolean isPublicado(){
        return StringUtils.equals(status, "PUBLICADO");
    }
}
