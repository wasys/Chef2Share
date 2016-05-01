package br.com.chef2share.domain;

import com.android.utils.lib.utils.StringUtils;

import java.util.List;

/**
 * Created by Jonas on 20/11/2015.
 */
public class Filtro {

    private List<TipoEvento> tipos;
    private List<Valor> valores;
    private List<Cozinha> cozinhas;
    private Periodo periodo;

    public List<TipoEvento> getTipos() {
        return tipos;
    }

    public void setTipos(List<TipoEvento> tipos) {
        this.tipos = tipos;
    }

    public List<Valor> getValores() {
        return valores;
    }

    public void setValores(List<Valor> valores) {
        this.valores = valores;
    }

    public List<Cozinha> getCozinhas() {
        return cozinhas;
    }

    public void setCozinhas(List<Cozinha> cozinhas) {
        this.cozinhas = cozinhas;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public int getIdxTipoEvento(String tipoEvento){
        if(StringUtils.isNotEmpty(tipoEvento)) {
            int idx = 0;
            for (TipoEvento t : tipos) {
                if (StringUtils.equals(t.getValue(), tipoEvento)) {
                    return idx;
                }
                idx++;
            }
        }
        return 0;
    }

    public int getIdxValor(String valor){
        if(StringUtils.isNotEmpty(valor)) {
            int idx = 0;
            for (Valor t : valores) {
                if (StringUtils.equals(t.getValue(), valor)) {
                    return idx;
                }
                idx++;
            }
        }
        return 0;
    }
}
