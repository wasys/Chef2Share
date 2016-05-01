package br.com.chef2share.domain;

import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.chef2share.enums.Genero;

/**
 *
 */
public class PerfilChef implements Serializable{

    private Chef chef;
    private List<PerfilChefLocal> locais;
    private List<PerfilChefTratamento> tratamentos;

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public List<PerfilChefLocal> getLocais() {
        if(locais == null){
            locais = new ArrayList<PerfilChefLocal>();
        }
        locais.add(0, PerfilChefLocal.getDefault());
        return locais;
    }

    public void setLocais(List<PerfilChefLocal> locais) {
        this.locais = locais;
    }

    public List<PerfilChefTratamento> getTratamentos() {
        if(tratamentos == null){
            tratamentos = new ArrayList<PerfilChefTratamento>();
        }
        tratamentos.add(0, PerfilChefTratamento.getDefault());
        return tratamentos;
    }

    public void setTratamentos(List<PerfilChefTratamento> tratamentos) {
        this.tratamentos = tratamentos;
    }

    public int getPositionTratamento(String tratamento) {
        int idx = 0;
        for(PerfilChefTratamento pTratamento : tratamentos){
            if(StringUtils.equals(pTratamento.getValue(), tratamento)){
                return idx;
            }
            idx++;
        }
        return 0;
    }
    public int getPositionLocalPrincipal(PerfilChefLocal localPrincipal) {
        int idx = 0;
        for(PerfilChefLocal pLocal : locais){
            if(localPrincipal != null && StringUtils.isNotEmpty(pLocal.getValue()) && StringUtils.isNotEmpty(localPrincipal.getId()) && StringUtils.equals(pLocal.getValue(), localPrincipal.getId())){
                return idx;
            }
            idx++;
        }
        return 0;
    }
}
