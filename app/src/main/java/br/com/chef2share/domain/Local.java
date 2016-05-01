package br.com.chef2share.domain;

import com.android.utils.lib.utils.StringUtils;

import java.io.Serializable;
import java.util.List;


public class Local implements Serializable {

    private String id;
    private String nome;
    private String descricao;
    private String estacionamento;
    private String cep;
    private String estado;
    private String cidade;
    private String bairro;
    private String logradouro;
    private String numero;
    private double latitude;
    private double longitude;
    private List<ImagemLocal> imagensLocal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEstacionamento() {
        return estacionamento;
    }

    public void setEstacionamento(String estacionamento) {
        this.estacionamento = estacionamento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<ImagemLocal> getImagensLocal() {
        return imagensLocal;
    }

    public void setImagensLocal(List<ImagemLocal> imagensLocal) {
        this.imagensLocal = imagensLocal;
    }

    public String getEnderecoDesc() {
        StringBuffer sb = new StringBuffer("");

        if(StringUtils.isNotEmpty(logradouro)){
            sb.append(logradouro).append(", ");
        }

        if(StringUtils.isNotEmpty(numero)){
            sb.append(numero).append(" - ");
        }

        if(StringUtils.isNotEmpty(bairro)){
            sb.append(bairro).append(" - ");
        }
        if(StringUtils.isNotEmpty(cidade)){
            sb.append(cidade).append(" - ");
        }
        if(StringUtils.isNotEmpty(estado)){
            sb.append(estado);
        }

        return sb.toString();
    }
}
