package br.com.chef2share.infra;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.android.utils.lib.utils.StringUtils;
import com.google.android.gms.location.places.Place;

import java.util.List;

import br.com.chef2share.SuperUtil;

/**
 * Created by Jonas on 21/11/2015.
 */
public class SuperGeocoder {

    private String logradouro;
    private String cidade;
    private String estado;
    private String bairro;
    private String cep;
    private double latitude;
    private double longitude;

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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
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

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public SuperGeocoder(Context context, Place place){
        Geocoder geoCoder = new Geocoder(context);
        try {
            List<Address> address = geoCoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
            if(address != null && address.size() > 0) {
                Address a = address.get(0);
                if (a != null) {
                    this.logradouro = a.getThoroughfare();
                    this.estado = a.getAdminArea();
                    this.cidade = a.getLocality();
                    this.bairro = a.getSubLocality();
                    this.cep = StringUtils.rightPad(a.getPostalCode(), 8, '0');
                    this.latitude = a.getLatitude();
                    this.longitude = a.getLongitude();
                }
            }
        }catch(Exception e){

        }
    }

}
