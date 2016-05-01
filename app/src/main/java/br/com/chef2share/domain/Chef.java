package br.com.chef2share.domain;

import java.io.Serializable;

/**
 * Created by Jonas on 19/11/2015.
 */
public class Chef implements Serializable {

    private String id;
    private String nome;
    private String resumo;
    private String homologacao;
    private Double avaliacaoMedia;
    private Double avaliacaoLocal;
    private Double avaliacaoComida;
    private Double avaliacaoAtendimento;
    private String totalAvaliadores;
    private String totalComentarios;
    private Imagem imagem;

    private String twitter;
    private String linkedin;
    private String youtube;
    private String instagram;
    private String tratamento;
    private PerfilChefLocal localPrincipal;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public String getHomologacao() {
        return homologacao;
    }

    public void setHomologacao(String homologacao) {
        this.homologacao = homologacao;
    }


    public void setAvaliacaoLocal(double avaliacaoLocal) {
        this.avaliacaoLocal = avaliacaoLocal;
    }

    public void setAvaliacaoMedia(Double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public Double getAvaliacaoLocal() {
        return avaliacaoLocal;
    }

    public void setAvaliacaoLocal(Double avaliacaoLocal) {
        this.avaliacaoLocal = avaliacaoLocal;
    }

    public Double getAvaliacaoComida() {
        return avaliacaoComida;
    }

    public void setAvaliacaoComida(Double avaliacaoComida) {
        this.avaliacaoComida = avaliacaoComida;
    }

    public Double getAvaliacaoAtendimento() {
        return avaliacaoAtendimento;
    }

    public void setAvaliacaoAtendimento(Double avaliacaoAtendimento) {
        this.avaliacaoAtendimento = avaliacaoAtendimento;
    }

    public void setAvaliacaoComida(double avaliacaoComida) {
        this.avaliacaoComida = avaliacaoComida;
    }


    public void setAvaliacaoAtendimento(double avaliacaoAtendimento) {
        this.avaliacaoAtendimento = avaliacaoAtendimento;
    }

    public String getTotalAvaliadores() {
        return totalAvaliadores;
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

    public PerfilChefLocal getLocalPrincipal() {
        return localPrincipal;
    }

    public void setLocalPrincipal(PerfilChefLocal localPrincipal) {
        this.localPrincipal = localPrincipal;
    }

    public String getTratamento() {
        return tratamento;
    }

    public void setTratamento(String tratamento) {
        this.tratamento = tratamento;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
}
