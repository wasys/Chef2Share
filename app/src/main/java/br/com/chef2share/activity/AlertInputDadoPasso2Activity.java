package br.com.chef2share.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.enums.Genero;
import br.com.chef2share.enums.TipoEvento;

@EActivity(R.layout.alert_input_dado)
public class AlertInputDadoPasso2Activity extends SuperActivity {


    @ViewById public TextView txtNomeCampo;
    @ViewById public TextView txtCampoObrigatorio;
    @ViewById public EditText txtValorCampo;
    @ViewById public Spinner spinnerValorCampo;

    @Extra("campo")
    public Passo2.Campo campo;
    private Passo2 passo2;


    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.GONE);

        passo2 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso2();

        txtCampoObrigatorio.setVisibility(campo.isObrigatorio() ? View.VISIBLE : View.GONE);

        switch (campo){
            case TITULO:
                txtNomeCampo.setText(R.string.cadastro_cardapio_titulo);
                txtValorCampo.setText(passo2.getTitulo());
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                break;

            case TIPO:
                txtNomeCampo.setText(R.string.cadastro_cardapio_tipo);
                spinnerValorCampo.setVisibility(View.VISIBLE);
                txtValorCampo.setVisibility(View.GONE);

                ArrayAdapter adapter = new ArrayAdapter<TipoEvento>(this, R.layout.item_spinner, TipoEvento.values());
                adapter.setDropDownViewResource(R.layout.item_spinner_drodown);
                spinnerValorCampo.setAdapter(adapter);

                int idx = 0;
                for(int i = 0; i< TipoEvento.values().length; i++){
                    if(StringUtils.equals(TipoEvento.values()[i].name(), passo2.getTipo())){
                        idx = i;
                    }
                }

                spinnerValorCampo.setSelection(idx, true);

                break;

            case MENU:
                txtNomeCampo.setText(R.string.cadastro_cardapio_menu);
                txtValorCampo.setText(passo2.getMenu());
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                break;

            case DESCRICAO:
                txtNomeCampo.setText(R.string.cadastro_cardapio_descricao);
                txtValorCampo.setHint(R.string.hint_descricao_cardapio);
                txtValorCampo.setText(passo2.getDescricao());
                txtValorCampo.setLines(5);
                txtValorCampo.setMaxLines(5);
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                break;

            case BEBIDAS:
                txtNomeCampo.setText(R.string.cadastro_cardapio_bebidas);
                txtValorCampo.setText(passo2.getBebida());
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                break;

            case TIPO_COZINHA:
                txtNomeCampo.setText(R.string.cadastro_cardapio_tipo_cozinha);
                if(passo2.getCozinha() != null) {
                    txtValorCampo.setText(passo2.getCozinha().getLabel());
                }
                spinnerValorCampo.setVisibility(View.VISIBLE);
                txtValorCampo.setVisibility(View.GONE);

                int idxC = 0;
                for(int i = 0; i< TipoEvento.values().length; i++){
                    if(StringUtils.equals(TipoEvento.values()[i].name(), passo2.getTipo())){
                        idxC = i;
                    }
                }

                spinnerValorCampo.setSelection(idxC, true);

                break;

            case OUTRAS_INFORMACOES:
                txtNomeCampo.setText(R.string.cadastro_cardapio_outras_informacoes);
                txtValorCampo.setText(passo2.getOutros());
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Click(R.id.btnSalvar)
    public void onClickSalvar(View v){
        String valor = getTextString(txtValorCampo);

        switch (campo){
            case TITULO:
                passo2.setTitulo(valor);
                break;

            case TIPO:
                TipoEvento tipoEvento = (TipoEvento) spinnerValorCampo.getSelectedItem();
                passo2.setTipo(tipoEvento.name());
                break;

            case MENU:
                passo2.setMenu(valor);
                break;

            case DESCRICAO:
                passo2.setDescricao(valor);
                break;

            case BEBIDAS:
                passo2.setBebida(valor);
                break;

            case TIPO_COZINHA:
                passo2.setCozinha(null);
                break;

            case OUTRAS_INFORMACOES:
                passo2.setOutros(valor);
                break;
        }

        Intent intent = new Intent();
        Bundle param = new Bundle();
        param.putSerializable("passo2", passo2);
        intent.putExtras(param);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
