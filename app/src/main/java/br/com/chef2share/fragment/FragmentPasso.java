package br.com.chef2share.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.listener.OnClickPasso;

/**
 *
 */
public class FragmentPasso extends SuperFragment {

    public static LinearLayout headerPasso1;
    public static TextView txtIndicadorPasso1;
    public static OnClickPasso onClickPasso1;

    public static LinearLayout headerPasso2;
    public static TextView txtIndicadorPasso2;
    public static OnClickPasso onClickPasso2;

    public static LinearLayout headerPasso3;
    public static TextView txtIndicadorPasso3;
    public static OnClickPasso onClickPasso3;

    public static LinearLayout headerPasso4;
    public static TextView txtIndicadorPasso4;
    public static OnClickPasso onClickPasso4;

    public static LinearLayout headerPasso5;
    public static TextView txtIndicadorPasso5;
    public static OnClickPasso onClickPasso5;


    private Passo passo;

    public enum Passo{
        PASSO_1,
        PASSO_2,
        PASSO_3,
        PASSO_4,
        PASSO_5,
    }

    @Override
    public void init() {
    }

    public void initPasso1(LinearLayout headerPasso, TextView txtIndicadorPasso, OnClickPasso onClickPasso) {
        this.headerPasso1 = headerPasso;
        this.txtIndicadorPasso1 = txtIndicadorPasso;
        this.onClickPasso1 = onClickPasso;
    }
    public void initPasso2(LinearLayout headerPasso, TextView txtIndicadorPasso, OnClickPasso onClickPasso) {
        this.headerPasso2 = headerPasso;
        this.txtIndicadorPasso2 = txtIndicadorPasso;
        this.onClickPasso2 = onClickPasso;
    }
    public void initPasso3(LinearLayout headerPasso, TextView txtIndicadorPasso, OnClickPasso onClickPasso) {
        this.headerPasso3 = headerPasso;
        this.txtIndicadorPasso3 = txtIndicadorPasso;
        this.onClickPasso3 = onClickPasso;
    }
    public void initPasso4(LinearLayout headerPasso, TextView txtIndicadorPasso, OnClickPasso onClickPasso) {
        this.headerPasso4 = headerPasso;
        this.txtIndicadorPasso4 = txtIndicadorPasso;
        this.onClickPasso4 = onClickPasso;
    }
    public void initPasso5(LinearLayout headerPasso, TextView txtIndicadorPasso, OnClickPasso onClickPasso) {
        this.headerPasso5 = headerPasso;
        this.txtIndicadorPasso5 = txtIndicadorPasso;
        this.onClickPasso5 = onClickPasso;
    }

    public void onClickPasso(Passo passo){

        /**
         * Se já esta publicado não faz mais nada
         */
        if(SuperApplication.getSuperCache().getEvento().isPublicado()) {
            switch (passo){
                case PASSO_1:
                case PASSO_2:
                case PASSO_3:
                case PASSO_4:
                    return;
            }
        }

        this.passo = passo;

        /**
         * Atualiza o estado de cada passo (ON/OFF)
         */
        switch (this.passo){
            case PASSO_1:

                updateView(headerPasso1, R.drawable.bt_criar_evento_on);
                updateView(txtIndicadorPasso1, R.drawable.bg_indicador_passo_on);

                updateView(headerPasso2, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso2, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso3, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso3, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso4, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso4, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso5, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso5, R.drawable.bg_indicador_passo_off);

                break;

            case PASSO_2:

                Passo1 passo1 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso1();
                if(!passo1.isInformacoesCompletas()){
                    SuperUtil.toast(getContext(), getResources().getString(R.string.msg_validacao_fluxo_passo, "1"));
                    return;
                }

                updateView(headerPasso2, R.drawable.bt_criar_evento_on);
                updateView(txtIndicadorPasso2, R.drawable.bg_indicador_passo_on);

                updateView(headerPasso1, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso1, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso3, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso3, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso4, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso4, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso5, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso5, R.drawable.bg_indicador_passo_off);


                break;

            case PASSO_3:
                Passo2 passo2 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso2();
                if(!passo2.isInformacoesCompletas()){
                    SuperUtil.toast(getContext(), getResources().getString(R.string.msg_validacao_fluxo_passo, "2"));
                    return;
                }

                updateView(headerPasso3, R.drawable.bt_criar_evento_on);
                updateView(txtIndicadorPasso3, R.drawable.bg_indicador_passo_on);

                updateView(headerPasso1, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso1, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso2, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso2, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso4, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso4, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso5, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso5, R.drawable.bg_indicador_passo_off);

                break;

            case PASSO_4:
                Passo3 passo3 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso3();
                if(!passo3.isInformacoesCompletas()){
                    SuperUtil.toast(getContext(), getResources().getString(R.string.msg_validacao_fluxo_passo, "3"));
                    return;
                }

                updateView(headerPasso4, R.drawable.bt_criar_evento_on);
                updateView(txtIndicadorPasso4, R.drawable.bg_indicador_passo_on);

                updateView(headerPasso1, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso1, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso2, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso2, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso3, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso3, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso5, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso5, R.drawable.bg_indicador_passo_off);
                break;

            case PASSO_5:
                Evento evento = SuperApplication.getSuperCache().getEvento();

                if(!StringUtils.equals(evento.getStatus(), "PUBLICADO")){
                    SuperUtil.toast(getContext(), getResources().getString(R.string.msg_validacao_fluxo_passo, "4"));
                    return;
                }

                updateView(headerPasso5, R.drawable.bt_criar_evento_on);
                updateView(txtIndicadorPasso5, R.drawable.bg_indicador_passo_on);

                updateView(headerPasso1, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso1, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso2, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso2, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso3, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso3, R.drawable.bg_indicador_passo_off);
                updateView(headerPasso4, R.drawable.bt_criar_evento_off);
                updateView(txtIndicadorPasso4, R.drawable.bg_indicador_passo_off);
                break;
        }

        /**
         * Avisa os listener que houve um click em uma opção
         */
        onClickPasso1.onClick(passo);
        onClickPasso2.onClick(passo);
        onClickPasso3.onClick(passo);
        onClickPasso4.onClick(passo);
        onClickPasso5.onClick(passo);

    }



    public void updateView(View view, int resource) {
        view.setBackgroundResource(resource);
    }

    public Passo getPasso(){
        return this.passo;
    }
}
