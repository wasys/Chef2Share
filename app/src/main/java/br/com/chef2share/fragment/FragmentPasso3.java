package br.com.chef2share.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.CriarEventoPasso1Activity_;
import br.com.chef2share.activity.CriarEventoPasso3Activity_;
import br.com.chef2share.domain.Cadastro;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.listener.OnClickPasso;

/**
 *
 */
@EFragment(R.layout.fragment_criar_evento_passo_tres)
public class FragmentPasso3 extends FragmentPasso implements OnClickPasso{

    @ViewById public LinearLayout headerPasso;
    @ViewById public TextView txtIndicadorPasso;

    @ViewById public LinearLayout conteudoPasso3;
    @ViewById public ImageView imgPassoOk;
    @ViewById public TextView txtDataValor;

    @Override
    public void init() {
        super.init();
        initPasso3(headerPasso, txtIndicadorPasso, this);

        Passo3 passo3 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso3();
        imgPassoOk.setVisibility(passo3.isInformacoesCompletas() ? View.VISIBLE : View.GONE);

        if (StringUtils.isNotEmpty(passo3.getDataInicio())) {
            txtDataValor.setText(passo3.getDataInicioFormatadaPasso3() + " / " + (passo3.getForma() == Passo3.Forma.PAGO ? passo3.getValorFormatado(true) : passo3.getForma().getDesc()));
        }

        /**
         * Verifica se o ultimo passo preenchido é esse
         */
        if(SuperApplication.getInstance().getSuperCache().getEvento().isPasso(Passo.PASSO_3)) {
            super.updateView(headerPasso, R.drawable.bt_criar_evento_on);
            super.updateView(txtIndicadorPasso, R.drawable.bg_indicador_passo_on);
        }else{
            super.updateView(headerPasso, R.drawable.bt_criar_evento_off);
            super.updateView(txtIndicadorPasso, R.drawable.bg_indicador_passo_off);
        }

        TextView txtSubtitulo = (TextView) getActivity().findViewById(R.id.txtSubTitulo);
        txtSubtitulo.setText(R.string.criar_evento_passo_tres_desc);
    }


    @Click(R.id.headerPasso)
    public void onClickHeader(View v){
        onClickPasso(Passo.PASSO_3);
    }

    @Override
    public void onClick(Passo passo) {
        switch (passo){
            case PASSO_3:
                onResume();
                SuperUtil.show(getContext(), CriarEventoPasso3Activity_.class);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}
