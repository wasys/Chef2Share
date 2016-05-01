package br.com.chef2share.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.ColorUtils;
import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.AcompanharEventoActivity_;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.listener.OnClickPasso;

/**
 *
 */
@EFragment(R.layout.fragment_criar_evento_passo_cinco)
public class FragmentPasso5 extends FragmentPasso implements OnClickPasso{

    @ViewById
    public LinearLayout headerPasso;
    @ViewById public TextView txtIndicadorPasso;
    @ViewById public TextView txtAcompanhe;

    @Override
    public void init() {
        super.init();
        initPasso5(headerPasso, txtIndicadorPasso, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SuperApplication.getSuperCache().getEvento().isPublicado()){
            headerPasso.setBackgroundDrawable(getResources().getDrawable(R.drawable.bt_verde_musgo_background));
            txtAcompanhe.setTextColor(ColorUtils.getColor(getContext(), R.color.branco));
        }
    }

    @Click(R.id.headerPasso)
    public void onClickHeader(View v){
        onClickPasso(Passo.PASSO_5);
    }

    @Override
    public void onClick(Passo passo) {
        switch (passo){
            case PASSO_5:
                Evento evento = SuperApplication.getSuperCache().getEvento();
                Bundle params = new Bundle();
                params.putString("eventoId", evento.getId());
                SuperUtil.show(getSuperActivity(), AcompanharEventoActivity_.class, params);
                break;
        }
    }
}
