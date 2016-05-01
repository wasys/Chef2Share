package br.com.chef2share.domain.request;

import java.io.Serializable;

/**
 * Created by Jonas on 21/11/2015.
 */
public class BuscaFiltro implements Serializable{

    private String dataMin;
    private String dataMax;
    private String tipo;
    private String cozinhaId;
    private String valorMaximo;
    private String cidade;
    private String estado;
    private String bairro;
    private double latitudeNorte;
    private double longitudeNorte;
    private double latitudeSul;
    private double longitudeSul;

    public String getDataMin() {
        return dataMin;
    }

    public void setDataMin(String dataMin) {
        this.dataMin = dataMin;
    }

    public String getDataMax() {
        return dataMax;
    }

    public void setDataMax(String dataMax) {
        this.dataMax = dataMax;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(String valorMaximo) {
        this.valorMaximo = valorMaximo;

    }

    public String getCozinhaId() {
        return cozinhaId;
    }

    public void setCozinhaId(String cozinhaId) {
        this.cozinhaId = cozinhaId;
    }

    public double getLatitudeNorte() {
        return latitudeNorte;
    }

    public void setLatitudeNorte(double latitudeNorte) {
        this.latitudeNorte = latitudeNorte;
    }

    public double getLongitudeNorte() {
        return longitudeNorte;
    }

    public void setLongitudeNorte(double longitudeNorte) {
        this.longitudeNorte = longitudeNorte;
    }

    public double getLatitudeSul() {
        return latitudeSul;
    }

    public void setLatitudeSul(double latitudeSul) {
        this.latitudeSul = latitudeSul;
    }

    public double getLongitudeSul() {
        return longitudeSul;
    }

    public void setLongitudeSul(double longitudeSul) {
        this.longitudeSul = longitudeSul;
    }
}
