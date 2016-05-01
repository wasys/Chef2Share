package br.com.chef2share.domain.listener;

import br.com.chef2share.domain.Cozinha;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.TipoEvento;
import br.com.chef2share.domain.Valor;

/**
 * Created by Jonas on 06/12/2015.
 */
public interface OnClickFotoLocal {

    public void onClickDelete(ImagemPasso imagemPasso);
    public void onClickAddFoto();
}
