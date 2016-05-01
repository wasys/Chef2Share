package br.com.chef2share.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.MensagemRecyclerViewAdapter;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Mensagem;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.request.Avaliacao;
import br.com.chef2share.domain.request.DetalheMsg;
import br.com.chef2share.domain.request.RespostaMsg;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.activity_detalhes_mensagem)
public class DetalhesMensagemActivity extends SuperActivityMainMenu {

    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtSubTitulo;
    @ViewById public EditText txtMensagem;
    @ViewById public LinearLayout layoutMsgParaChef;
    @ViewById public ProgressBar progressImgChef;
    @ViewById public RoundedImageView imgChef;
    @ViewById public TextView txtNomeChef;

    @ViewById public RecyclerView recyclerViewMensagens;
    private LinearLayoutManager layoutManager;
    private MensagemRecyclerViewAdapter adapter;

    @Extra("msg")
    public Mensagem mensagem;

    @Extra("chef")
    public Chef chef;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        txtSubTitulo.setVisibility(View.GONE);

        if(mensagem != null) {
            setTextString(txtTitulo, mensagem.getRemetente().getNome());
        }

        if(chef != null){
            setTextString(txtTitulo, chef.getNome());
        }

        if(mensagem != null) {

            SuperApplication.getSuperCache().setQtdeNovasMsgs(0);

            recyclerViewMensagens.setVisibility(View.VISIBLE);
            layoutMsgParaChef.setVisibility(View.GONE);

            recyclerViewMensagens.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getBaseContext());
            recyclerViewMensagens.setLayoutManager(layoutManager);

            adapter = new MensagemRecyclerViewAdapter(getBaseContext(), false);
            recyclerViewMensagens.setAdapter(adapter);

            doInBackground(transacaoLoadDetalhesMensagens(), true, R.string.msg_aguarde_carregando_mensagem, false);
        }

        if(chef != null){
            layoutMsgParaChef.setVisibility(View.VISIBLE);
            recyclerViewMensagens.setVisibility(View.GONE);

            setTextString(txtNomeChef, chef.getNome());

            String urlImgChef = SuperCloudinery.getUrlImgPessoa(getBaseContext(), chef.getImagem());
            if(StringUtils.isNotEmpty(urlImgChef)) {
                Picasso.with(getBaseContext()).load(urlImgChef).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(imgChef, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressImgChef.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressImgChef.setVisibility(View.GONE);
                    }
                });
            }else{
                imgChef.setImageResource(R.drawable.userpic);
                progressImgChef.setVisibility(View.GONE);
            }
        }
    }

    private Transacao transacaoLoadDetalhesMensagens() {
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
                adapter.refresh(response.getResult().getMensagens());
            }

            @Override
            public void execute() throws Exception {

                DetalheMsg detalheMsg = new DetalheMsg();
                detalheMsg.setNome(mensagem.getRemetente().getNome());
                JSONObject json = SuperGson.toJSONObject(detalheMsg);
                superService.mensagemConversa(getBaseContext(), superActivity, superActivity, mensagem.getRemetente().getId());
            }
        };
    }

    @Click(R.id.btnEnviarMnsagem)
    public void onClickEnviarMsg(View v){
        String msg = getTextString(txtMensagem);

        if(StringUtils.isEmpty(msg)){
            SuperUtil.alertDialog(activity, R.string.msg_menssagem_obrigatoria);
            return;
        }

        Usuario destinatario = new Usuario();

        if(mensagem != null) {
            destinatario.setId(mensagem.getRemetente().getId());
        }

        if(chef != null){
            destinatario.setId(chef.getId());
        }

        final RespostaMsg respostaMsg = new RespostaMsg(destinatario, msg);

        SuperUtil.alertDialogConfirmacao(activity, getResources().getString(R.string.msg_confirma_envio_msg), R.string.sim, new Runnable() {
            @Override
            public void run() {
                doInBackground(getTransacaoEnviarMsg(respostaMsg), true);
            }
        }, R.string.nao, new Runnable() {
            @Override
            public void run() {
            }
        });

    }

    private Transacao getTransacaoEnviarMsg(final RespostaMsg respostaMsg) {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                SuperUtil.alertDialog(activity, getResources().getString(R.string.msg_mensagem_enviada_com_sucesso), new Runnable() {
                    @Override
                    public void run() {
                        if(mensagem != null) {
                            Bundle param = new Bundle();
                            param.putSerializable("msg", mensagem);
                            SuperUtil.showTop(activity, DetalhesMensagemActivity_.class, param);
                        }
                        finish();
                    }
                });
            }

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(respostaMsg);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.MENSAGEM_ENVIAR, json);
            }
        };
    }
}
