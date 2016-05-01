package br.com.chef2share.domain.listener;

import br.com.chef2share.domain.Cozinha;
import br.com.chef2share.domain.TipoEvento;
import br.com.chef2share.domain.Valor;

/**
 * Created by Jonas on 06/12/2015.
 */
public interface OnSelectedBuscaFiltroEventosQueOfereco {

    public void onSelectedMax(String max);
    public void onSelectedMin(String min);
    public void onClickPesquisarFiltro();
}
