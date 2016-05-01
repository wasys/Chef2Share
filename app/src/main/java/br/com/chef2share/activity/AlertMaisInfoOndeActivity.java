package br.com.chef2share.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;

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

@EActivity(R.layout.alert_mais_info_onde)
public class AlertMaisInfoOndeActivity extends SuperActivity {

    @ViewById public TextView txtOnde;
    @ViewById public TextView txtOndeEndereco;
    @ViewById public TextView txtOndeDescricao;
    @ViewById public LinearLayout layoutOndeDescricao;

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

        setTextString(txtOnde, passo1.getNome());
        setTextString(txtOndeEndereco, passo1.getEnderecoCompleto(false));

        if(StringUtils.isNotEmpty(passo1.getDescricao())){
            layoutOndeDescricao.setVisibility(View.VISIBLE);
            setTextString(txtOndeDescricao, passo1.getDescricao());
        } else {
            layoutOndeDescricao.setVisibility(View.GONE);
        }
    }

    @Click(R.id.btnFecharMaisInfoOnde)
    public void onClickFechar(View v){
        finish();
    }
}
