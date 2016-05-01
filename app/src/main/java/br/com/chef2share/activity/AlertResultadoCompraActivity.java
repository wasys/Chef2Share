package br.com.chef2share.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Pedido;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;

@EActivity(R.layout.alert_resultado_compra)
public class AlertResultadoCompraActivity extends SuperActivity {


    @ViewById public ImageView imgStatus;
    @ViewById public TextView txtInfo;

    @Extra("pedidoId")
    public String pedidoId;


    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.GONE);
        setTextString(txtInfo, getString(R.string.msg_pagamento_realizado_sucesso));
    }

    @Click(R.id.btnEventosQueVou)
    public void onClickEventosQueVou(View v){
        Bundle param = new Bundle();
        param.putSerializable("pedidoId", pedidoId);
        SuperUtil.showTop(activity, EventosQueVouActivity_.class);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
