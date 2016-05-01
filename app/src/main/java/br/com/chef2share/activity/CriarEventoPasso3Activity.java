package br.com.chef2share.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;

@EActivity(R.layout.activity_criar_evento_passo_tres)
public class CriarEventoPasso3Activity extends SuperActivityMainMenu {

    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtSubTitulo;
    @ViewById public Button btnProximo;
    @ViewById public LinearLayout layoutCampos;

    private DatePickerDialog datePickerInicio;
    private DatePickerDialog datePickerFim;
    private TimePickerDialog timePickerInicio;
    private TimePickerDialog timePickerFim;

    public static final int INPUT_DADO = 888;
    private Passo3 passo3;

    //DateUtils.toString(newDate.getTime(), DateUtils.DATE)

    private Calendar dataInicio = Calendar.getInstance();
    private Calendar dataFim = Calendar.getInstance();

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        txtSubTitulo.setVisibility(View.VISIBLE);

        setTextString(txtTitulo, getString(R.string.titulo_criar_evento));
        setTextString(txtSubTitulo, getString(R.string.criar_evento_passo_tres_desc));

        passo3 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso3();

        criaCampos(passo3);

        initDatePickers();
    }

    private void criaCampos(Passo3 passo3) {

        layoutCampos.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        
        List<Passo3.Campo> campos = passo3.campos();
        for (Passo3.Campo campo : campos){
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.item_campo_criar_evento, null);
            TextView txtNomeCampo = (TextView) layout.findViewById(R.id.txtNomeCampo);
            TextView txtCampoObrigatorio = (TextView) layout.findViewById(R.id.txtCampoObrigatorio);
            TextView txtValorCampo = (TextView) layout.findViewById(R.id.txtValorCampo);

            txtCampoObrigatorio.setVisibility(campo.isObrigatorio() ? View.VISIBLE : View.GONE);

            switch (campo){
                case TIPO:
                    txtNomeCampo.setText(R.string.cadastro_data_valor_tipo_evento);
                    if(passo3.getTipo() != null) {
                        br.com.chef2share.enums.TipoPrivacidadeEvento tipoEvento = br.com.chef2share.enums.TipoPrivacidadeEvento.valueOf(passo3.getTipo());
                        txtValorCampo.setText(tipoEvento.getStringLabel());
                    }
                    break;

                case INICIO:
                    txtNomeCampo.setText(R.string.cadastro_data_valor_inicio);
                    if(StringUtils.isNotEmpty(passo3.getDataInicio())) {
                        txtValorCampo.setText(passo3.getDataInicioFormatadaPasso3());
                    }
                    break;
                case TERMINO:
                    txtNomeCampo.setText(R.string.cadastro_data_valor_termino);
                    if(StringUtils.isNotEmpty(passo3.getDataTermino())) {
                        txtValorCampo.setText(passo3.getDataTerminoFormatadaPasso3());
                    }
                    break;

                case FORMA:
                    txtNomeCampo.setText(R.string.cadastro_data_valor_forma);
                    if(passo3.getForma() != null) {
                        txtValorCampo.setText(passo3.getForma().getDesc());
                    }
                    break;

                case VALOR:
                    txtNomeCampo.setText(R.string.cadastro_data_valor_valor);
                    if(passo3.getValor() != null && passo3.getValor() > 0) {
                        txtValorCampo.setText(passo3.getValorFormatado(true));
                    }
                    break;
                case NUMERO_MAX_CONVIDADOS:
                    txtNomeCampo.setText(R.string.cadastro_data_valor_numero_max_convidados);
                    txtValorCampo.setText(passo3.getMaximo() > 0 ? String.valueOf(passo3.getMaximo()) : "");
                    break;
                case PROMOCAO_DUAS_PESSOAS:
                    txtNomeCampo.setText(R.string.cadastro_data_valor_promocao_duas_pessoas);
                    if(passo3.getValorDuplo() != null && passo3.getValorDuplo() > 0) {
                        txtValorCampo.setText(passo3.getValorDuploFormatado());
                    }
                    break;
                case PROMOCAO_COMPRA_ANTECIPADA:
                    txtNomeCampo.setText(R.string.cadastro_data_valor_promocao_compra_antecipada);
                    if(passo3.getValorAntecipado() != null && passo3.getValorAntecipado() > 0) {
                        txtValorCampo.setText(passo3.getValorAntecipadoFormatado() + " até " + passo3.getDataAntecipadaFormatadaPasso3());
                    }
                    break;
                case POLITICA_CANCELAMENTO:
                    txtNomeCampo.setText(R.string.cadastro_data_valor_politica_cancelamento);
                    txtValorCampo.setText(passo3.getPoliticaCancelamento());
                    break;
                case POLITICA_NAO_COMPARECIIMENTO:
                    txtNomeCampo.setText(R.string.cadastro_data_valor_politica_nao_comparecimento);
                    txtValorCampo.setText(passo3.getPoliticaNaoComparecimento());
                    break;

            }
            layout.setOnClickListener(onClickCampo(campo));
            layoutCampos.addView(layout);
        }
    }

    private View.OnClickListener onClickCampo(final Passo3.Campo campo) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (campo){

                    case INICIO:
                        timePickerInicio.show();
                        datePickerInicio.show();
                        break;

                    case TERMINO:
                        timePickerFim.show();
                        datePickerFim.show();
                        break;

                    default:
                        Intent i = new Intent(activity, AlertInputDadoPasso3Activity_.class);
                        Bundle param = new Bundle();
                        param.putSerializable("campo", campo);
                        i.putExtras(param);
                        startActivityForResult(i, INPUT_DADO);
                        break;
                }

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == INPUT_DADO) {
            passo3 = (Passo3) data.getSerializableExtra("passo3");
            criaCampos(passo3);
        }
    }

    @Click({R.id.btnProximo})
    public void onClickProximo(View v){
        if(!passo3.isInformacoesCompletas()){
            SuperUtil.toast(getBaseContext(), getResources().getString(R.string.msg_informe_dados_cardapio));
            return;
        }
        Date dataInicioEvento = DateUtils.toDate(passo3.getDataInicio(), DateUtils.DATE_BD);
        Date dataTerminoEvento = DateUtils.toDate(passo3.getDataTermino(), DateUtils.DATE_BD);
        if (DateUtils.isMaior(dataInicioEvento, dataTerminoEvento)) {
            SuperUtil.alertDialog(superActivity, R.string.msg_passo_tres_data_termino_deve_ser_maior_que_data_inicio);
            return;
        }

        if(passo3.getValorAntecipado() != null && passo3.getValorAntecipado() > 0 && StringUtils.isEmpty(passo3.getDataAntecipado())){
            SuperUtil.alertDialog(superActivity, R.string.msg_passo_tres_informe_data_compra_antecipada);
            return;
        }

        if(passo3.getValorAntecipado() != null && passo3.getValorAntecipado() > 0) {
            Date dataAntecipados = DateUtils.toDate(passo3.getDataAntecipado(), DateUtils.DATE_BD);

            if (DateUtils.isMaior(dataAntecipados, dataInicioEvento)) {
                SuperUtil.alertDialog(superActivity, R.string.msg_passo_tres_data_antecipada_deve_ser_menor_que_data_do_evento);
                return;
            }
        }

        doInBackground(transacaoProsseguir(), true, R.string.msg_aguarde_salvando_dados_evento, false);
    }

    private void initDatePickers() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerInicio = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dataInicio.set(year, monthOfYear, dayOfMonth);
                passo3.setDataInicio(DateUtils.toString(dataInicio.getTime(), DateUtils.DATE_BD));
                SuperApplication.getSuperCache().getEvento().setPasso3(passo3);
                criaCampos(passo3);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerFim = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dataFim.set(year, monthOfYear, dayOfMonth);
                passo3.setDataTermino(DateUtils.toString(dataFim.getTime(), DateUtils.DATE_BD));
                SuperApplication.getSuperCache().getEvento().setPasso3(passo3);
                criaCampos(passo3);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        timePickerInicio = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dataInicio.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dataInicio.set(Calendar.MINUTE, minute);

                passo3.setHoraInicio(DateUtils.toString(dataInicio.getTime(), DateUtils.TIME_24h));
                SuperApplication.getSuperCache().getEvento().setPasso3(passo3);
                criaCampos(passo3);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

        timePickerFim = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dataFim.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dataFim.set(Calendar.MINUTE, minute);

                passo3.setHoraTermino(DateUtils.toString(dataFim.getTime(), DateUtils.TIME_24h));
                SuperApplication.getSuperCache().getEvento().setPasso3(passo3);
                criaCampos(passo3);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
    }

    private Transacao transacaoProsseguir() {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                SuperApplication.getSuperCache().setCadastro(response.getCadastro());
                SuperApplication.getSuperCache().setEvento(response.getCadastro().getEvento());
                finish();
            }

            @Override
            public void execute() throws Exception {
                Evento evento = SuperApplication.getInstance().getSuperCache().getEvento();
                evento.setPasso1(null);
                evento.setPasso2(null);
                JSONObject json = SuperGson.toJSONObject(evento);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.PASSO3_PROSSEGUIR, json);
            }
        };
    }
}
