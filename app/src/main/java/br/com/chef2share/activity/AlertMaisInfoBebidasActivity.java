package br.com.chef2share.activity;

import android.view.View;
import android.widget.LinearLayout;
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
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Detalhes;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.alert_mais_info_bebidas)
public class AlertMaisInfoBebidasActivity extends SuperActivity {

    @ViewById public TextView txtInfoMenu;
    @ViewById public TextView txtInfoBebidas;
    @ViewById public LinearLayout layoutBebidas;

    @Extra("detalhes")
    public Detalhes detalhes;

    @Override
    public void init() {
        super.init();

        Evento evento = detalhes.getEvento();
        Chef chef = evento.getChef();

        Passo1 passo1 = detalhes.getPasso1();
        Passo2 passo2 = detalhes.getPasso2();
        Passo3 passo3 = detalhes.getPasso3();

        setTextString(txtInfoMenu, passo2.getDescricao());

        if(StringUtils.isNotEmpty(passo2.getBebida())){
            layoutBebidas.setVisibility(View.VISIBLE);
            setTextString(txtInfoBebidas, passo2.getBebida());
        }else{
            layoutBebidas.setVisibility(View.GONE);
        }
    }

    @Click(R.id.btnFecharMaisInfoBebidas)
    public void onClickFechar(View v){
        finish();
    }
}
