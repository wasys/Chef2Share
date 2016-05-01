package br.com.chef2share.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.ExpandableListAdapter;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Convite;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Ingresso;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Pedido;
import br.com.chef2share.domain.QRCode;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.request.Avaliacao;
import br.com.chef2share.domain.request.PedidoFiltro;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.enums.StatusPedido;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;

@EActivity(R.layout.alert_avaliacao)
public class AlertAvaliacaoEventoActivity extends SuperActivity {


    @ViewById public ImageView imgBackground;
    @ViewById public TextView txtTituloEvento;
    @ViewById public RatingBar ratingAvaliacaoLocal;
    @ViewById public RatingBar ratingAvaliacaoComida;
    @ViewById public RatingBar ratingAvaliacaoAtendimento;
    @ViewById public EditText txtComentario;

    @Extra("pedido")
    public Pedido pedido;


    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.GONE);

        imgBackground.getLayoutParams().width = SuperUtils.getWidthImagemFundoThumb(getBaseContext());
        imgBackground.getLayoutParams().height = SuperUtils.getHeightImagemFundoThumb(getBaseContext());

        Evento evento = pedido.getEvento();
        Chef chef = evento.getChef();

        Passo1 passo1 = evento.getPasso1();
        Passo2 passo2 = evento.getPasso2();
        Passo3 passo3 = evento.getPasso3();


        setTextString(txtTituloEvento, passo2.getTitulo());

        String url = SuperCloudinery.getUrl(getBaseContext(), passo2.getImagemPrincipal(), SuperUtils.getWidthImagemFundoThumb(getBaseContext()), SuperUtils.getHeightImagemFundoThumb(getBaseContext()));
        if (StringUtils.isNotEmpty(url)) {
            Picasso.with(getBaseContext()).load(url).resize(SuperUtils.getWidthImagemFundo(getBaseContext()), SuperUtils.getHeightImagemFundo(getBaseContext())).centerCrop().into(imgBackground);
        }
    }

    @Click(R.id.btnEnviarAvaliacao)
    public void onClickEnviarAvaliacao(View v){

        final Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAtendimento(ratingAvaliacaoAtendimento.getRating());
        avaliacao.setComida(ratingAvaliacaoComida.getRating());
        avaliacao.setLocal(ratingAvaliacaoLocal.getRating());
        avaliacao.setComentario(getTextString(txtComentario));

        Evento evento = new Evento();
        evento.setId(pedido.getEvento().getId());
        avaliacao.setEvento(evento);

        SuperUtil.alertDialogConfirmacao(activity, getResources().getString(R.string.msg_confirma_envio_avaliacao), R.string.sim, new Runnable() {
            @Override
            public void run() {
                doInBackground(getTransacaoAvaliar(avaliacao), true);
            }
        }, R.string.nao, new Runnable() {
            @Override
            public void run() {
            }
        });

    }

    private Transacao getTransacaoAvaliar(final Avaliacao avaliacao) {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                SuperUtil.alertDialog(activity, getResources().getString(R.string.msg_avaliacao_enviada_com_sucesso), new Runnable() {
                    @Override
                    public void run() {
                        SuperUtil.showTop(activity, HomeActivity_.class);
                        finish();
                    }
                });
            }

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(avaliacao);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.AVALIAR_EVENTO, json);
            }
        };
    }
}
