package br.com.chef2share.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Date;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.EventoQueOferecoRecyclerViewAdapter;
import br.com.chef2share.adapter.EventoRecyclerViewAdapter;
import br.com.chef2share.domain.Busca;
import br.com.chef2share.domain.BuscaEventoQueOfereco;
import br.com.chef2share.domain.BuscaEventoQueOfereco;
import br.com.chef2share.domain.ChefEvento;
import br.com.chef2share.domain.Cozinha;
import br.com.chef2share.domain.Filtro;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.TipoEvento;
import br.com.chef2share.domain.Valor;
import br.com.chef2share.domain.listener.OnSelectedBuscaFiltroEventosQueOfereco;
import br.com.chef2share.domain.listener.OnSelectedBuscaFiltroOptions;
import br.com.chef2share.domain.request.BuscaFiltro;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;

@EActivity(R.layout.activity_eventos_que_ofereco)
public class EventosQueOferecoActivity extends SuperActivity implements OnSelectedBuscaFiltroEventosQueOfereco {

    @ViewById public LinearLayout layoutNenhumRegistro;
    @ViewById public TextView txtTitulo;
    @ViewById public RecyclerView recyclerViewEventos;
    @ViewById public LinearLayout layoutAguarde;
    private LinearLayoutManager layoutManager;
    private EventoQueOferecoRecyclerViewAdapter eventoadapter;
    private BuscaEventoQueOfereco buscaVo = new BuscaEventoQueOfereco();
    private ChefEvento chefEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();

        buscaVo.setPeriodoDe(DateUtils.toString(DateUtils.addDia(new Date(), -7), DateUtils.DATE_BD));
        buscaVo.setPeriodoAte(DateUtils.toString(DateUtils.addDia(new Date(), 30), DateUtils.DATE_BD));

        eventoadapter = new EventoQueOferecoRecyclerViewAdapter(this, this);

        setVisibilityBotaoVoltar(View.VISIBLE);
        setTextString(txtTitulo, getResources().getString(R.string.titulo_eventos_que_ofereco));

        recyclerViewEventos.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerViewEventos.setLayoutManager(layoutManager);


        doInBackground(getTransacaoEvetosQueOfereco(), layoutAguarde, recyclerViewEventos);
    }

    private Transacao getTransacaoEvetosQueOfereco() {
        return new Transacao() {
            @Override
            public void onError(String error) {
                SuperUtil.alertDialog(activity, error, new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }

            @Override
            public void onSuccess(Response response) {
                chefEvento = response.getChefEvento();

                if(chefEvento == null) {
                    layoutNenhumRegistro.setVisibility(View.VISIBLE);
                    return;
                }
                buscaVo.setEventos(chefEvento.getEventos());
                eventoadapter.refresh(chefEvento, buscaVo);
                recyclerViewEventos.setAdapter(eventoadapter);
            }

            @Override
            public void execute() throws Exception {
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.CHEF_EVENTO, null);
            }
        };
    }

    public Transacao getTransacaoBuscaFiltro(final BuscaEventoQueOfereco buscaVo){
        return new Transacao() {
            @Override
            public void onError(String error) {
                SuperUtil.alertDialog(activity, error, new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }

            @Override
            public void onSuccess(Response response) {
                chefEvento = response.getChefEvento();

                if(chefEvento == null) {
                    layoutNenhumRegistro.setVisibility(View.VISIBLE);
                    return;
                }
                buscaVo.setEventos(chefEvento.getEventos());
                eventoadapter.refresh(chefEvento, buscaVo);
                recyclerViewEventos.setAdapter(eventoadapter);
            }

            @Override
            public void execute() throws Exception {
                buscaVo.setEventos(null);
                JSONObject json = SuperGson.toJSONObject(buscaVo);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.CHEF_EVENTO_FILTRAR, json);
            }
        };
    }

    @Override
    public void onClickVoltar(View v) {
        finish();
    }

    @Override
    public void onSelectedMax(String max) {
        buscaVo.setPeriodoAte(max);
        SuperApplication.getSuperCache().getPesquisaEventoQueOfereco().periodoAte = max;
    }

    @Override
    public void onSelectedMin(String min) {
       buscaVo.setPeriodoDe(min);
        SuperApplication.getSuperCache().getPesquisaEventoQueOfereco().periodoDe = min;
    }

    @Override
    public void onClickPesquisarFiltro() {
        doInBackground(getTransacaoBuscaFiltro(buscaVo), true);
    }
}
