package br.com.chef2share.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.AcompanhaEvento;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Pedido;
import br.com.chef2share.domain.QRCodeResult;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.request.Avaliacao;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.alert_entrada_qrcode)
public class AlertEntradaQRCode extends SuperActivity {


    @ViewById public TextView txtNomeConvidado;
    @ViewById public TextView txtEmail;
    @ViewById public RoundedImageView imgConvidado;
    @ViewById public ImageView imgCheckVerde;
    @ViewById public ImageView imgCheckCinza;
    @ViewById public ImageView imgEmail;
    @ViewById public ProgressBar progressImgConvidado;
    @ViewById public TextView txtTipoIngresso;
    @ViewById public TextView txtStatus;
    @ViewById public TextView txtNumeroIngresso;

    @Extra("qrCodeResult")
    public QRCodeResult qrCodeResult;

    @Extra("acompanhaEvento")
    public AcompanhaEvento acompanhaEvento;


    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.GONE);
        imgCheckCinza.setVisibility(View.GONE);
        imgCheckVerde.setVisibility(View.VISIBLE);
        txtEmail.setVisibility(View.GONE);
        imgEmail.setVisibility(View.GONE);

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{qrCodeResult.participante.getEmail()});
                i.putExtra(Intent.EXTRA_SUBJECT, "Chef2Share - " + acompanhaEvento.getEvento().getPasso2().getTitulo());
                startActivity(Intent.createChooser(i, "Enviar email..."));
            }
        });


        String url = SuperCloudinery.getUrlImgPessoa(getBaseContext(), qrCodeResult.participante.getImagem());
        if (StringUtils.isNotEmpty(url)) {
            Picasso.with(getBaseContext()).load(url).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(imgConvidado, new Callback() {
                @Override
                public void onSuccess() {
                    progressImgConvidado.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressImgConvidado.setVisibility(View.GONE);
                }
            });

        } else {
            imgConvidado.setImageResource(R.drawable.userpic);
            progressImgConvidado.setVisibility(View.GONE);
        }

        setTextString(txtNomeConvidado, qrCodeResult.participante.getNome());
        setTextString(txtEmail, qrCodeResult.participante.getEmail());
        setTextString(txtTipoIngresso, qrCodeResult.entrada.getNome());
        setTextString(txtStatus, acompanhaEvento.getDicionario().get(qrCodeResult.ingresso.getStatus()));
        setTextString(txtNumeroIngresso, qrCodeResult.entrada.getReferencia());
    }

    @Click(R.id.btnConfirmar)
    public void onClickConfirmar(View v){
        Bundle param = new Bundle();
        param.putSerializable("qrCodeResult", qrCodeResult);
        param.putBoolean("confirmaChekin", true);
        Intent returnIntent = new Intent();
        returnIntent.putExtras(param);

        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
