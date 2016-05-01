package br.com.chef2share.domain.listener;

import br.com.chef2share.domain.Cozinha;
import br.com.chef2share.domain.TipoEvento;
import br.com.chef2share.domain.Valor;
import br.com.chef2share.domain.request.PedidoFiltro;


public interface OnSelectedBuscaFiltroEventosQueVouOptions {

    public void onSelectedMax(String max);
    public void onSelectedMin(String min);
    public void filtrar();
}
