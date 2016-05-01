package br.com.chef2share.domain.listener;

import br.com.chef2share.domain.ImagemPasso;

/**
 * Created by Jonas on 06/12/2015.
 */
public interface OnClickFotoCardapio {

    public void onClickDelete(ImagemPasso imagemPasso);
    public void onClickFotoDivulgacao(ImagemPasso imagemPasso, boolean isChecked);
    public void onClickFotoPrincipal(ImagemPasso imagemPasso, boolean isChecked);
    public void onClickAddFoto();
}
