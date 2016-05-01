package br.com.chef2share.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.MoneyUtils;
import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.enums.TipoPrivacidadeEvento;
import br.com.chef2share.infra.MoneyMaskEditTextChangeListener;
import br.com.chef2share.infra.SuperUtils;

@EActivity(R.layout.alert_input_dado)
public class AlertInputDadoPasso3Activity extends SuperActivity {

    @ViewById public TextView txtNomeCampo;
    @ViewById public TextView txtCampoObrigatorio;
    @ViewById public TextView txtNomeCampoDireita;
    @ViewById public EditText txtValorCampo;
    @ViewById public Spinner spinnerValorCampo;
    @ViewById public LinearLayout layoutCamposDireita;
    @ViewById public Button btnDataAntecipado;

    private DatePickerDialog datePicker;

    @Extra("campo")
    public Passo3.Campo campo;
    private Passo3 passo3;


    @Override
    public void init() {
        super.init();

        layoutCamposDireita.setVisibility(View.GONE);
        setVisibilityBotaoVoltar(View.GONE);

        setDateTimeField();

        passo3 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso3();

        txtCampoObrigatorio.setVisibility(campo.isObrigatorio() ? View.VISIBLE : View.GONE);

        switch (campo){
            case TIPO:
                txtNomeCampo.setText(R.string.cadastro_data_valor_tipo_evento);
                ArrayAdapter adapter = new ArrayAdapter<TipoPrivacidadeEvento>(this, R.layout.item_spinner, TipoPrivacidadeEvento.values());
                adapter.setDropDownViewResource(R.layout.item_spinner_drodown);
                spinnerValorCampo.setAdapter(adapter);

                spinnerValorCampo.setVisibility(View.VISIBLE);
                txtValorCampo.setVisibility(View.GONE);

                int idx = 0;
                for(int i = 0; i< TipoPrivacidadeEvento.values().length; i++){
                    if(StringUtils.equals(TipoPrivacidadeEvento.values()[i].name(), passo3.getTipo())){
                        idx = i;
                    }
                }

                spinnerValorCampo.setSelection(idx, true);

                break;

            case INICIO:
                txtNomeCampo.setText(R.string.cadastro_data_valor_inicio);
                if(StringUtils.isNotEmpty(passo3.getDataInicio())) {
                    txtValorCampo.setText(passo3.getDataPorExtenso());
                }
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                break;
            case TERMINO:
                txtNomeCampo.setText(R.string.cadastro_data_valor_inicio);
                if(StringUtils.isNotEmpty(passo3.getDataTermino())) {
                    txtValorCampo.setText(passo3.getDataTerminoPorExtenso());
                }
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                break;

            case FORMA:
                txtNomeCampo.setText(R.string.cadastro_data_valor_forma);
                ArrayAdapter adapterForma = new ArrayAdapter<Passo3.Forma>(this, R.layout.item_spinner, Passo3.Forma.values());
                adapterForma.setDropDownViewResource(R.layout.item_spinner_drodown);
                spinnerValorCampo.setAdapter(adapterForma);

                spinnerValorCampo.setVisibility(View.VISIBLE);
                txtValorCampo.setVisibility(View.GONE);

                int idxForma = 0;
                for(int i = 0; i< Passo3.Forma.values().length; i++){
                    if(Passo3.Forma.values()[i] == passo3.getForma()){
                        idxForma = i;
                    }
                }

                spinnerValorCampo.setSelection(idxForma, true);
                break;


            case VALOR:
                txtNomeCampo.setText(R.string.cadastro_data_valor_valor);
                if(passo3.getValor() != null && passo3.getValor() > 0) {
                    txtValorCampo.setText(passo3.getValorFormatado(true));
                }

                txtValorCampo.setInputType(InputType.TYPE_CLASS_PHONE);
                txtValorCampo.addTextChangedListener(new MoneyMaskEditTextChangeListener(txtValorCampo));
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                break;

            case NUMERO_MAX_CONVIDADOS:
                txtNomeCampo.setText(R.string.cadastro_data_valor_numero_max_convidados);
                txtValorCampo.setText(passo3.getMaximo() > 0 ? String.valueOf(passo3.getMaximo()) : "");
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                txtValorCampo.setInputType(InputType.TYPE_CLASS_PHONE);
                break;

            case PROMOCAO_DUAS_PESSOAS:
                txtNomeCampo.setText(R.string.cadastro_data_valor_promocao_duas_pessoas);
                if((passo3.getValorDuplo() != null && passo3.getValorDuplo() > 0)){
                    txtValorCampo.setText(passo3.getValorDuploFormatado());
                }
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                txtValorCampo.addTextChangedListener(new MoneyMaskEditTextChangeListener(txtValorCampo));

                txtValorCampo.setInputType(InputType.TYPE_CLASS_PHONE);
                break;

            case PROMOCAO_COMPRA_ANTECIPADA:
                txtNomeCampo.setText(R.string.cadastro_data_valor_promocional);
                txtNomeCampoDireita.setText(R.string.cadastro_data_valor_data_compra_antecipada);
                if(passo3.getValorAntecipado() != null && passo3.getValorAntecipado() > 0) {
                    txtValorCampo.setText(passo3.getValorAntecipadoFormatado());
                }

                if(StringUtils.isNotEmpty(passo3.getDataAntecipado())){
                    btnDataAntecipado.setText(passo3.getDataAntecipadaFormatadaPasso3());
                }

                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);
                layoutCamposDireita.setVisibility(View.VISIBLE);

                txtValorCampo.addTextChangedListener(new MoneyMaskEditTextChangeListener(txtValorCampo));
                txtValorCampo.setInputType(InputType.TYPE_CLASS_PHONE);
                break;

            case POLITICA_CANCELAMENTO:
                txtNomeCampo.setText(R.string.cadastro_data_valor_politica_cancelamento);
                txtValorCampo.setText(passo3.getPoliticaCancelamento());
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);

                txtValorCampo.setSingleLine(false);
                txtValorCampo.setMaxLines(5);
                txtValorCampo.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                break;

            case POLITICA_NAO_COMPARECIIMENTO:
                txtNomeCampo.setText(R.string.cadastro_data_valor_politica_nao_comparecimento);
                txtValorCampo.setText(passo3.getPoliticaNaoComparecimento());
                spinnerValorCampo.setVisibility(View.GONE);
                txtValorCampo.setVisibility(View.VISIBLE);

                txtValorCampo.setSingleLine(false);
                txtValorCampo.setMaxLines(5);
                txtValorCampo.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                break;

        }
    }

    @Click(R.id.btnSalvar)
    public void onClickSalvar(View v){
        String valor = getTextString(txtValorCampo);
            switch (campo) {
                case TIPO:
                    TipoPrivacidadeEvento tipoEvento = (TipoPrivacidadeEvento) spinnerValorCampo.getSelectedItem();
                    passo3.setTipo(tipoEvento.name());
                    break;

                case INICIO:
//                passo3.setDataInicio();
                    break;

                case FORMA:
                    Passo3.Forma forma = (Passo3.Forma) spinnerValorCampo.getSelectedItem();
                    passo3.setForma(forma);

                    switch (forma){
                        case PAGO:
                            break;
                        case GRATUITO:
                            passo3.setValor(null);
                            passo3.setDataAntecipado(null);
                            passo3.setValorAntecipado(null);
                            passo3.setValorDuplo(null);
                            break;
                    }

                    break;
                case VALOR:
                    if(StringUtils.isNotEmpty(valor)) {
                        passo3.setValor(SuperUtils.stringRSToDouble(valor));
                    }
                    break;

                case NUMERO_MAX_CONVIDADOS:
                    if(StringUtils.isNotEmpty(valor)) {
                        passo3.setMaximo(Integer.parseInt(valor));
                        passo3.setMinimo(1);
                    }
                    break;

                case PROMOCAO_DUAS_PESSOAS:
                    if(StringUtils.isNotEmpty(valor)) {
                        passo3.setValorDuplo(SuperUtils.stringRSToDouble(valor));
                    }
                    break;

                case PROMOCAO_COMPRA_ANTECIPADA:
                    if(StringUtils.isNotEmpty(valor)) {
                        passo3.setValorAntecipado(SuperUtils.stringRSToDouble(valor));
                        String dataAntecipada = getTextString(btnDataAntecipado);
                        if(StringUtils.isNotEmpty(dataAntecipada)){
                            passo3.setDataAntecipado(DateUtils.toString(DateUtils.toDate(dataAntecipada, DateUtils.DATE), DateUtils.DATE_BD));
                        }
                    }
                    break;

                case POLITICA_CANCELAMENTO:
                    passo3.setPoliticaCancelamento(valor);
                    break;

                case POLITICA_NAO_COMPARECIIMENTO:
                    passo3.setPoliticaNaoComparecimento(valor);
                    break;
            }

        Intent intent = new Intent();
        Bundle param = new Bundle();
        param.putSerializable("passo3", passo3);
        intent.putExtras(param);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();

        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                btnDataAntecipado.setText(DateUtils.toString(newDate.getTime(), DateUtils.DATE));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Click(R.id.btnDataAntecipado)
    public void onClickDataAntecipada(View v){
        AndroidUtils.closeVirtualKeyboard(getBaseContext(), v);
        datePicker.show();
    }
}
