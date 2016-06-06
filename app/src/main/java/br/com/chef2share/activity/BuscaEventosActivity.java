package br.com.chef2share.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.EventoRecyclerViewAdapter;
import br.com.chef2share.domain.Busca;
import br.com.chef2share.domain.BuscaVO;
import br.com.chef2share.domain.Cozinha;
import br.com.chef2share.domain.Filtro;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.TipoEvento;
import br.com.chef2share.domain.Valor;
import br.com.chef2share.domain.listener.OnSelectedBuscaFiltroOptions;
import br.com.chef2share.domain.request.BuscaFiltro;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;

@EActivity(R.layout.activity_busca_eventos)
public class BuscaEventosActivity extends SuperActivity implements OnSelectedBuscaFiltroOptions{

    @ViewById public LinearLayout layoutNenhumRegistro;
    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtSubTitulo;
    @ViewById public RecyclerView recyclerViewEventos;
    @ViewById public LinearLayout layoutAguarde;
    private LinearLayoutManager layoutManager;
    private EventoRecyclerViewAdapter eventoadapter;
    private BuscaFiltro buscaFiltro = new BuscaFiltro();
    private BuscaVO buscaVo = new BuscaVO();
    private Busca busca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();

        eventoadapter = new EventoRecyclerViewAdapter(BuscaEventosActivity.this, BuscaEventosActivity.this);

        setVisibilityBotaoVoltar(View.VISIBLE);
        setTextString(txtTitulo, getResources().getString(R.string.titulo_buscar_eventos));
        setTextString(txtSubTitulo, "");
        txtSubTitulo.setVisibility(View.GONE);

        recyclerViewEventos.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerViewEventos.setLayoutManager(layoutManager);


        doInBackground(getTransacaoLoadCamposPesquisa(), true, R.string.infra_msg_aguarde, false);
    }

    private Transacao getTransacaoLoadCamposPesquisa() {
        return new Transacao() {

            @Override
            public void execute() throws Exception {
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.EVENTO_FILTRO, null);
            }

            @Override
            public void onSuccess(Response response) {
                Filtro filtro = response.getFiltro();
                buscaVo.setFiltro(filtro);
                eventoadapter.refresh(buscaVo);
                recyclerViewEventos.setAdapter(eventoadapter);

                doInBackground(getTransacaoBuscaFiltro(buscaFiltro), layoutAguarde, recyclerViewEventos);
            }

            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(getParent(), msgErro);
            }
        };
    }

    public Transacao getTransacaoBuscaFiltro(final BuscaFiltro buscaFiltro){
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
//                if(response.getBusca().getPublicados() == null || response.getBusca().getPublicados().size() == 0){
//                    layoutNenhumRegistro.setVisibility(View.VISIBLE);
//                    return;
//                }
//                layoutNenhumRegistro.setVisibility(View.GONE);

                busca = response.getBusca();

                buscaVo.setEventos(busca.getPublicados());
                eventoadapter.refresh(buscaVo);
                recyclerViewEventos.setAdapter(eventoadapter);
            }

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(buscaFiltro);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.EVENTO_BUSCA, json);
            }
        };
    }

    @Override
    public void onClickVoltar(View v) {
        finish();
    }

    @Click(R.id.btnMapa)
    public void onClickMapa(){
        if(busca == null || busca.getPublicados() == null){
            SuperUtil.toast(getBaseContext(), "Nenhum evento localizado.");
            return;
        }
        Bundle param = new Bundle();
        param.putSerializable("buscaFiltro", buscaFiltro);
        param.putSerializable("busca", busca);
        SuperUtil.showTop(getBaseContext(), MapaActivity_.class, param);
    }

    @Override
    public void onSelectedMax(String max) {
        buscaFiltro.setDataMax(max);

        boolean pesquisa = false;
        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEvento().max) && !StringUtils.equalsIgnoreCase(max, SuperApplication.getSuperCache().getPesquisaEvento().max)){
            pesquisa = true;
        }

        if(StringUtils.isEmpty(SuperApplication.getSuperCache().getPesquisaEvento().max)){
            pesquisa = true;
        }

        SuperApplication.getSuperCache().getPesquisaEvento().max = max;

        if(pesquisa){
            doInBackground(getTransacaoBuscaFiltro(buscaFiltro), true);
        }
    }

    @Override
    public void onSelectedMin(String min) {
        buscaFiltro.setDataMin(min);

        boolean pesquisa = false;
        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEvento().min) && !StringUtils.equalsIgnoreCase(min, SuperApplication.getSuperCache().getPesquisaEvento().min)){
            pesquisa = true;
        }

        if(StringUtils.isEmpty(SuperApplication.getSuperCache().getPesquisaEvento().min)){
            pesquisa = true;
        }

        SuperApplication.getSuperCache().getPesquisaEvento().min = min;

        if(pesquisa){
            doInBackground(getTransacaoBuscaFiltro(buscaFiltro), true);
        }
    }

    @Override
    public void onSelectTipoEvento(TipoEvento tipoEvento) {
        buscaFiltro.setTipo(tipoEvento.getValue());

        boolean pesquisa = false;
        if(SuperApplication.getSuperCache().getPesquisaEvento().tipoEvento != null && SuperApplication.getSuperCache().getPesquisaEvento().tipoEvento != tipoEvento){
            pesquisa = true;
        }

        SuperApplication.getSuperCache().getPesquisaEvento().tipoEvento = tipoEvento;
        setSubtituloFiltro();

        if(pesquisa) {
            doInBackground(getTransacaoBuscaFiltro(buscaFiltro), true);
        }
    }

    @Override
    public void onSelectTipoCozinha(Cozinha cozinha) {
        buscaFiltro.setCozinhaId(cozinha.getValue());

        boolean pesquisa = false;
        if(SuperApplication.getSuperCache().getPesquisaEvento().cozinha != null && SuperApplication.getSuperCache().getPesquisaEvento().cozinha != cozinha){
            pesquisa = true;
        }

        SuperApplication.getSuperCache().getPesquisaEvento().cozinha = cozinha;
        setSubtituloFiltro();

        if(pesquisa) {
            doInBackground(getTransacaoBuscaFiltro(buscaFiltro), true);
        }
    }

    @Override
    public void onSelectValorMaximo(Valor valor) {
        buscaFiltro.setValorMaximo(valor.getValue());

        boolean pesquisa = false;
        if(SuperApplication.getSuperCache().getPesquisaEvento().valorMaximo != null && SuperApplication.getSuperCache().getPesquisaEvento().valorMaximo != valor){
            pesquisa = true;
        }

        SuperApplication.getSuperCache().getPesquisaEvento().valorMaximo = valor;
        setSubtituloFiltro();

        if(pesquisa) {
            doInBackground(getTransacaoBuscaFiltro(buscaFiltro), true);
        }
    }

    @Override
    public void onSelectedEndereco(String all, String uf, String cidade, String bairro) {
        buscaFiltro.setEstado(uf);
        buscaFiltro.setCidade(cidade);
        buscaFiltro.setBairro(bairro);

        boolean pesquisa = false;
        if(StringUtils.isNotEmpty(all) && !StringUtils.equalsIgnoreCase(all, SuperApplication.getSuperCache().getPesquisaEvento().enderecoSearch)){
            pesquisa = true;
        }

        SuperApplication.getSuperCache().getPesquisaEvento().enderecoSearch = all;
        SuperApplication.getSuperCache().getPesquisaEvento().bairro = bairro;
        SuperApplication.getSuperCache().getPesquisaEvento().cidade = cidade;
        SuperApplication.getSuperCache().getPesquisaEvento().estado = uf;

        if(pesquisa) {
            doInBackground(getTransacaoBuscaFiltro(buscaFiltro), true);
        }
    }

    @Override
    public void onClickPesquisarFiltro() {
        doInBackground(getTransacaoBuscaFiltro(buscaFiltro), true);
    }

    public void setSubtituloFiltro(){
        StringBuffer sb = new StringBuffer();

        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEvento().enderecoSearch)){
            sb.append(SuperApplication.getSuperCache().getPesquisaEvento().enderecoSearch).append(" / ");
        }

        if(SuperApplication.getSuperCache().getPesquisaEvento().tipoEvento != null && StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEvento().tipoEvento.getValue())){
            sb.append(SuperApplication.getSuperCache().getPesquisaEvento().tipoEvento.getLabel()).append(" / ");
        }

        if(SuperApplication.getSuperCache().getPesquisaEvento().valorMaximo != null && StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEvento().valorMaximo.getValue())){
            sb.append(SuperApplication.getSuperCache().getPesquisaEvento().valorMaximo.toString()).append(" / ");
        }

        if(StringUtils.isNotEmpty(sb.toString())){
            txtSubTitulo.setVisibility(View.VISIBLE);
            setTextString(txtSubTitulo, sb.toString());
        }else{
            txtSubTitulo.setVisibility(View.GONE);
        }

    }

    @Override
    public void finish() {
        super.finish();

        SuperApplication.getSuperCache().getPesquisaEvento().min = null;
        SuperApplication.getSuperCache().getPesquisaEvento().bairro = null;
        SuperApplication.getSuperCache().getPesquisaEvento().cidade = null;
        SuperApplication.getSuperCache().getPesquisaEvento().cozinha = null;
        SuperApplication.getSuperCache().getPesquisaEvento().enderecoSearch = null;
        SuperApplication.getSuperCache().getPesquisaEvento().estado = null;
        SuperApplication.getSuperCache().getPesquisaEvento().max = null;
        SuperApplication.getSuperCache().getPesquisaEvento().tipoEvento = null;
        SuperApplication.getSuperCache().getPesquisaEvento().valorMaximo = null;
    }
}
