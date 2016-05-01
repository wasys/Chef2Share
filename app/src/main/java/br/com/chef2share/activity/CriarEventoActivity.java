package br.com.chef2share.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.fragment.FragmentListaPassos;
import br.com.chef2share.fragment.FragmentListaPassos_;
import br.com.chef2share.infra.FragmentUtil;
import br.com.chef2share.infra.Transacao;

@EActivity(R.layout.activity_criar_evento)
public class CriarEventoActivity extends SuperActivityMainMenu {

    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtSubTitulo;
    @ViewById public LinearLayout conteudoHome;

    @Extra("eventoId")
    public String eventoId;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        setTextString(txtTitulo, getString(R.string.titulo_criar_evento));
        txtSubTitulo.setVisibility(View.GONE);

        if(SuperApplication.getSuperCache().getCadastro() == null) {
            doInBackground(getTransacaoEventoCadastro(), true, R.string.msg_aguarde_carregando_meus_locais_cardapios, false);
        }
    }

    private Transacao getTransacaoEventoCadastro() {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro, new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
                    }
                });
            }

            @Override
            public void onSuccess(Response response) {

                SuperApplication.getInstance().getSuperCache().setCadastro(response.getCadastro());

                //caso não seja edição, já carrega a lista de passos em branco
                if(StringUtils.isEmpty(eventoId)) {
                    FragmentListaPassos fragmentListaPassos = FragmentListaPassos_.builder().build();
                    FragmentUtil.replace(activity, R.id.conteudoHome, fragmentListaPassos, true);

                // se for uma edição, recupera os detalhes do evento
                }else{
                    doInBackground(transacaoRefreshEvento(eventoId), true, R.string.msg_recuperando_dados_evento, false);
                }
            }

            @Override
            public void execute() throws Exception {
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.EVENTO_CADASTRO, null);
            }
        };
    }

    @Override
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    private Transacao transacaoRefreshEvento(final String eventoId) {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro, new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
                    }
                });
            }

            @Override
            public void onSuccess(Response response) {
                SuperApplication.getSuperCache().setCadastro(response.getCadastro());
                SuperApplication.getSuperCache().setEvento(response.getCadastro().getEvento());

                FragmentListaPassos fragmentListaPassos = FragmentListaPassos_.builder().build();
                FragmentUtil.replace(activity, R.id.conteudoHome, fragmentListaPassos, true);
            }

            @Override
            public void execute() throws Exception {
                superService.detalhesRascunho(getBaseContext(), superActivity, superActivity, eventoId);
            }
        };
    }
}
