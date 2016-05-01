package br.com.chef2share.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.CriarEventoPasso1Activity_;
import br.com.chef2share.activity.CriarEventoPasso2Activity_;
import br.com.chef2share.domain.Cadastro;
import br.com.chef2share.domain.Cardapio;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Local;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.listener.OnClickPasso;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;

/**
 *
 */
@EFragment(R.layout.fragment_criar_evento_passo_dois)
public class FragmentPasso2 extends FragmentPasso implements OnClickPasso {

    @ViewById public LinearLayout conteudoPasso2;
    @ViewById public LinearLayout conteudoCardapiosCadastrados;
    @ViewById public ImageView imgPassoOk;
    @ViewById public LinearLayout headerPasso;
    @ViewById public TextView txtIndicadorPasso;
    @ViewById public TextView txtCardapio;

    @Override
    public void init() {
        super.init();
        conteudoPasso2.setVisibility(View.GONE);
        initPasso2(headerPasso, txtIndicadorPasso, this);

        Passo2 passo2 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso2();
        imgPassoOk.setVisibility(passo2.isInformacoesCompletas() ? View.VISIBLE : View.GONE);

        if(StringUtils.isNotEmpty(passo2.getTitulo())) {
            txtCardapio.setText(passo2.getTitulo());
        }

        /**
         * Verifica se o ultimo passo preenchido é esse
         */
        if(SuperApplication.getInstance().getSuperCache().getEvento().isPasso(Passo.PASSO_2)) {
            onClick(Passo.PASSO_2);
            super.updateView(headerPasso, R.drawable.bt_criar_evento_on);
            super.updateView(txtIndicadorPasso, R.drawable.bg_indicador_passo_on);
        }else{
            super.updateView(headerPasso, R.drawable.bt_criar_evento_off);
            super.updateView(txtIndicadorPasso, R.drawable.bg_indicador_passo_off);
        }

        TextView txtSubtitulo = (TextView) getActivity().findViewById(R.id.txtSubTitulo);
        txtSubtitulo.setText(R.string.criar_evento_passo_dois_desc);

        Cadastro cadastro = SuperApplication.getInstance().getSuperCache().getCadastro();
        if(cadastro != null){
            addCardapiosCadastrados(cadastro.getCardapios());
        }

    }

    private void addCardapiosCadastrados(List<Cardapio> cardapios) {
        if(cardapios != null && cardapios.size() > 0){
            conteudoCardapiosCadastrados.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            for(Cardapio cardapio : cardapios) {
                LinearLayout layoutMeuCardapio = (LinearLayout) inflater.inflate(R.layout.item_meu_cardapio, null);
                TextView txtNomeMeuCardapio = (TextView) layoutMeuCardapio.findViewById(R.id.txtNomeMeuCardapio);
                txtNomeMeuCardapio.setText(cardapio.getTitulo());
                layoutMeuCardapio.setOnClickListener(onClickMeuCardapio(cardapio));
                conteudoCardapiosCadastrados.addView(layoutMeuCardapio);
            }
        }
    }

    private View.OnClickListener onClickMeuCardapio(final Cardapio cardapio) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SuperUtil.alertDialogConfirmacao(getActivity(), getResources().getString(R.string.msg_confirma_meu_cardapio, cardapio.getTitulo()), R.string.sim, new Runnable() {
//                    @Override
//                    public void run() {

                    SuperApplication.getInstance().getSuperCache().getEvento().setPasso2(null);
                    Passo2 passo2 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso2();
                    passo2.setCardapio(cardapio);

                        //doInBackground(transacaoProsseguir(), true, R.string.msg_aguarde_salvando_local_evento, false);
                        SuperUtil.show(getContext(), CriarEventoPasso2Activity_.class);

//                    }
//                }, R.string.cancelar, new Runnable() {
//                    @Override
//                    public void run() {
//                    }
//                });
            }
        };
    };

    @Override
    public void onResume() {
        super.onResume();
        init();
    }


    @Click(R.id.headerPasso)
    public void onClickHeader(View v){
        onClickPasso(Passo.PASSO_2);
    }

    @Override
    public void onClick(Passo passo) {
        switch (passo){
            case PASSO_2:
                conteudoPasso2.setVisibility(View.VISIBLE);
                break;

            default:
                conteudoPasso2.setVisibility(View.GONE);
                break;
        }
    }

    @Click(R.id.btnCadastrarNovoCardapio)
    public void onClickCadastrarNovoCaradapio(View v) {
        SuperApplication.getInstance().getSuperCache().getEvento().setPasso2(null);
        SuperUtil.show(getContext(), CriarEventoPasso2Activity_.class);
    }

    private Transacao transacaoProsseguir() {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {

            }

            @Override
            public void onSuccess(Response response) {
                SuperApplication.getSuperCache().setCadastro(response.getCadastro());
                SuperApplication.getSuperCache().setEvento(response.getCadastro().getEvento());
                onResume();
            }

            @Override
            public void execute() throws Exception {
                Evento evento = SuperApplication.getInstance().getSuperCache().getEvento();
                evento.setPasso1(null);
                evento.setPasso3(null);
                JSONObject json = SuperGson.toJSONObject(evento);
                superService.sendRequest(getContext(), getSuperFragment(), getSuperFragment(), SuperService.TipoTransacao.PASSO2_PROSSEGUIR, json);
            }
        };
    }
}
