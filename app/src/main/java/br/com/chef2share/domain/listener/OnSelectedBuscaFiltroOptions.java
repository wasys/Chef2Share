package br.com.chef2share.domain.listener;

import java.util.Date;

import br.com.chef2share.domain.Cozinha;
import br.com.chef2share.domain.TipoEvento;
import br.com.chef2share.domain.Valor;
import br.com.chef2share.fragment.FragmentPasso;

/**
 * Created by Jonas on 06/12/2015.
 */
public interface OnSelectedBuscaFiltroOptions {

    public void onSelectedMax(String max);
    public void onSelectedMin(String min);
    public void onSelectTipoEvento(TipoEvento tipoEvento);
    public void onSelectTipoCozinha(Cozinha cozinha);
    public void onSelectValorMaximo(Valor valor);
    public void onClickPesquisarFiltro();
    public void onSelectedEndereco(String all, String uf, String cidade, String bairro);
}
