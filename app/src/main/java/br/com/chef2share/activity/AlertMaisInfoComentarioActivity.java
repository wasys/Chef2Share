package br.com.chef2share.activity;

import android.view.View;
import android.widget.Button;
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

import br.com.chef2share.R;
import br.com.chef2share.domain.Avaliacao;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Detalhes;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.alert_mais_info_comentario)
public class AlertMaisInfoComentarioActivity extends SuperActivity {

    @ViewById public RoundedImageView imgUsuario;
    @ViewById public TextView txtNomeUsuario;
    @ViewById public ProgressBar progressImgUsuario;
    @ViewById public TextView txtDataComentario;
    @ViewById public TextView txtEventoComentario;
    @ViewById public TextView txtComentario;

    @Extra("avaliacao")
    public Avaliacao avaliacao;

    @Override
    public void init() {
        super.init();

        txtNomeUsuario.setText(avaliacao.getNome());
        if(StringUtils.isNotEmpty(avaliacao.getComentario())) {
            txtComentario.setText("\"" + avaliacao.getComentario() + "\"");
        }else{
            txtComentario.setText("");
        }
        txtEventoComentario.setText(avaliacao.getTitulo());
        txtDataComentario.setText(avaliacao.getDataFormatada());


        String urlImgChef = SuperCloudinery.getUrlImgPessoa(getBaseContext(), avaliacao.getImagem());

        if(StringUtils.isNotEmpty(urlImgChef)) {
            Picasso.with(getBaseContext()).load(urlImgChef).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(imgUsuario, new Callback() {
                @Override
                public void onSuccess() {
                    progressImgUsuario.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressImgUsuario.setVisibility(View.GONE);
                }
            });
        }else{
            imgUsuario.setImageResource(R.drawable.userpic);
            progressImgUsuario.setVisibility(View.GONE);
        }
    }

    @Click(R.id.btnVoltar)
    public void onClickFechar(View v){
        finish();
    }
}
