package br.com.chef2share.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Passo1;

@EActivity(R.layout.alert_input_dado)
public class AlertInputDadoActivity extends SuperActivity {


    @ViewById public TextView txtNomeCampo;
    @ViewById public TextView txtCampoObrigatorio;
    @ViewById public EditText txtValorCampo;
    @ViewById public Spinner spinnerValorCampo;

    @Extra("campo")
    public Passo1.Campo campo;
    private Passo1 passo1;


    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.GONE);

        passo1 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso1();

        txtCampoObrigatorio.setVisibility(campo.isObrigatorio() ? View.VISIBLE : View.GONE);
        spinnerValorCampo.setVisibility(View.GONE);
        txtValorCampo.setVisibility(View.VISIBLE);

        switch (campo){
            case NOME:
                txtNomeCampo.setText(R.string.cadastro_local_nome);
                txtValorCampo.setLines(1);
                txtValorCampo.setMaxLines(3);
                txtValorCampo.setText(passo1.getNome());
                break;

            case DESCRICAO:
                txtNomeCampo.setText(R.string.cadastro_local_descricao);
                txtValorCampo.setLines(5);
                txtValorCampo.setMaxLines(5);
                txtValorCampo.setText(passo1.getDescricao());
                break;

            case ESTACIONAMENTO:
                txtNomeCampo.setText(R.string.cadastro_local_estacionamento);
                txtValorCampo.setLines(1);
                txtValorCampo.setMaxLines(2);
                txtValorCampo.setText(passo1.getEstacionamento());
                break;
        }
    }

    @Click(R.id.btnSalvar)
    public void onClickSalvar(View v){
        String valor = getTextString(txtValorCampo);

        switch (campo){
            case NOME:
                passo1.setNome(valor);
                break;

            case DESCRICAO:
                passo1.setDescricao(valor);
                break;

            case ESTACIONAMENTO:
                passo1.setEstacionamento(valor);
                break;
        }

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
