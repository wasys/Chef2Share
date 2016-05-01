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

import java.util.Collections;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.CriarEventoPasso1Activity;
import br.com.chef2share.activity.CriarEventoPasso1Activity_;
import br.com.chef2share.domain.Cadastro;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemLocal;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Local;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.listener.OnClickPasso;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.FragmentUtil;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;

/**
 *
 */
@EFragment(R.layout.fragment_criar_evento_passo_um)
public class FragmentPasso1 extends FragmentPasso implements OnClickPasso{

    @ViewById public LinearLayout conteudoPasso1;
    @ViewById public LinearLayout conteudoEnderecosCadastrados;
    @ViewById public ImageView imgPassoOk;
    @ViewById public LinearLayout headerPasso;
    @ViewById public TextView txtIndicadorPasso;
    @ViewById public TextView txtLocal;

    @Override
    public void init() {
        super.init();
        conteudoPasso1.setVisibility(View.GONE);
        initPasso1(headerPasso, txtIndicadorPasso, this);

        Passo1 passo1 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso1();
        imgPassoOk.setVisibility(passo1.isInformacoesCompletas() ? View.VISIBLE : View.GONE);

        /**
         * Verifica se o ultimo passo preenchido é esse
         */
        if(SuperApplication.getInstance().getSuperCache().getEvento().isPasso(Passo.PASSO_1)){
            onClick(Passo.PASSO_1);
            super.updateView(headerPasso, R.drawable.bt_criar_evento_on);
            super.updateView(txtIndicadorPasso, R.drawable.bg_indicador_passo_on);
        }else{
            super.updateView(headerPasso, R.drawable.bt_criar_evento_off);
            super.updateView(txtIndicadorPasso, R.drawable.bg_indicador_passo_off);
        }


        if(StringUtils.isNotEmpty(passo1.getNome())) {
            txtLocal.setText(passo1.getNome());
        }

        TextView txtSubtitulo = (TextView) getActivity().findViewById(R.id.txtSubTitulo);
        txtSubtitulo.setText(R.string.criar_evento_passo_um_desc);

        Cadastro cadastro = SuperApplication.getInstance().getSuperCache().getCadastro();
        if(cadastro != null){
            addLocaisCadastrados(cadastro.getLocais());
        }
    }

    private void addLocaisCadastrados(List<Local> locais) {
        if(locais != null && locais.size() > 0){
            conteudoEnderecosCadastrados.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            for(Local local : locais) {
                LinearLayout layoutMeuLocal = (LinearLayout) inflater.inflate(R.layout.item_meu_local, null);
                TextView txtNomeMeuLocal = (TextView) layoutMeuLocal.findViewById(R.id.txtNomeMeuLocal);
                TextView txtEnderecoMeuLocal = (TextView) layoutMeuLocal.findViewById(R.id.txtEnderecoMeuLocal);
                txtNomeMeuLocal.setText(local.getNome());
                txtEnderecoMeuLocal.setText(local.getEnderecoDesc());
                layoutMeuLocal.setOnClickListener(onClickMeuLocal(local));
                conteudoEnderecosCadastrados.addView(layoutMeuLocal);
            }
        }
    }

    private View.OnClickListener onClickMeuLocal(final Local local) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SuperUtil.alertDialogConfirmacao(getSuperActivity(), getResources().getString(R.string.msg_confirma_meu_local, local.getNome()), R.string.sim, new Runnable() {
//                    @Override
//                    public void run() {

                        SuperApplication.getInstance().getSuperCache().getEvento().setPasso1(null);
                        Passo1 passo1 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso1();
                        passo1.setLocal(local);

                        //doInBackground(transacaoProsseguir(), true, R.string.msg_aguarde_salvando_local_evento, false);
                        SuperUtil.show(getContext(), CriarEventoPasso1Activity_.class);

//                    }
//                }, R.string.cancelar, new Runnable() {
//                    @Override
//                    public void run() {
//                    }
//                });
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Click(R.id.headerPasso)
    public void onClickHeader(View v){
        onClickPasso(Passo.PASSO_1);
    }


    @Click(R.id.btnCadastrarNovoLocal)
    public void onClickCadastrarNovoLocal(View v) {
        SuperApplication.getInstance().getSuperCache().getEvento().setPasso1(null);
        SuperUtil.show(getContext(), CriarEventoPasso1Activity_.class);
    }



    @Override
    public void onClick(Passo passo) {
        switch (passo){
            case PASSO_1:
                conteudoPasso1.setVisibility(View.VISIBLE);
                break;

            default:
                conteudoPasso1.setVisibility(View.GONE);
                break;
        }
    }

    private Transacao transacaoProsseguir() {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {

            }

            @Override
            public void onSuccess(Response response) {
//                doInBackground(transacaoRefreshEvento(), true, R.string.msg_recuperando_dados_evento, false);
                SuperApplication.getSuperCache().setCadastro(response.getCadastro());
                SuperApplication.getSuperCache().setEvento(response.getCadastro().getEvento());
                onResume();
            }

            @Override
            public void execute() throws Exception {
                Evento evento = SuperApplication.getInstance().getSuperCache().getEvento();
                evento.setPasso2(null);
                evento.setPasso3(null);
                JSONObject json = SuperGson.toJSONObject(evento);
                superService.sendRequest(getContext(), getSuperFragment(), getSuperFragment(), SuperService.TipoTransacao.PASSO1_PROSSEGUIR, json);
            }
        };
    }
}
