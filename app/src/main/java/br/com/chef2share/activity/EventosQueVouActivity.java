package br.com.chef2share.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.EventoQueVouRecyclerViewAdapter;
import br.com.chef2share.domain.Pedido;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.UsuarioPedido;
import br.com.chef2share.domain.listener.OnSelectedBuscaFiltroEventosQueVouOptions;
import br.com.chef2share.domain.request.PedidoFiltro;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;

@EActivity(R.layout.activity_eventos_que_vou)
public class EventosQueVouActivity extends SuperActivity implements OnSelectedBuscaFiltroEventosQueVouOptions {

    @ViewById public LinearLayout layoutNenhumEvento;
    @ViewById public TextView txtTitulo;
    @ViewById public RecyclerView recyclerViewEventosQueVou;
    private LinearLayoutManager layoutManager;

    private EventoQueVouRecyclerViewAdapter adapter;

    private PedidoFiltro pedidoFiltro= new PedidoFiltro();
    private final EventosQueVouActivity activity = this;

    private final int PAGAR_AGORA = 99;
    private final int VER_CONVITES = 98;
    private final int AVALIAR = 97;


    @Override
    public void init() {
        super.init();

        adapter = new EventoQueVouRecyclerViewAdapter(activity, this);

        setVisibilityBotaoVoltar(View.VISIBLE);
        setTextString(txtTitulo, getResources().getString(R.string.titulo_eventos_que_vou));

        recyclerViewEventosQueVou.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewEventosQueVou.setLayoutManager(layoutManager);

        /**
         * //TODO a web nao permite trazer tudo, esse filtro maloqueiro deve ser removido quando a web permitir consultar sem range de data
         *
         */
        pedidoFiltro.setPeriodoDe("01/01/2015");
        pedidoFiltro.setPeriodoAte(DateUtils.toString(DateUtils.addDia(new Date(), 365)));
        doInBackground(getTransacaoPesquisa(pedidoFiltro), true);

    }

    public Transacao getTransacaoPesquisa(final PedidoFiltro pedidoFiltro){

        return new Transacao() {

            @Override
            public void onError(String error) {
                SuperUtil.alertDialog(activity, error);
            }

            @Override
            public void onSuccess(Response response) {

//                if(response.getUsuarioPedido().getPedidos() == null || response.getUsuarioPedido().getPedidos().size() <= 0){
//                    layoutNenhumEvento.setVisibility(View.VISIBLE);
//                    return;
//                }
//                layoutNenhumEvento.setVisibility(View.GONE);
                UsuarioPedido usuarioPedido = response.getUsuarioPedido();
                adapter.refresh(usuarioPedido);
                recyclerViewEventosQueVou.setAdapter(adapter);
            }

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(pedidoFiltro);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.EVENTOS_QUE_VOU, json);
            }
        };
    }

    @Override
    public void onClickVoltar(View v) {
        finish();
    }

    public void onClickVerConvites(UsuarioPedido usuarioPedido, Pedido pedido){
        doInBackground(getResumoPedido(pedido, VER_CONVITES), true, R.string.msg_processando_dados_pedido, true);
    }

    public void onClickPagarAgora(UsuarioPedido usuarioPedido, final Pedido pedido){
       doInBackground(getResumoPedido(pedido, PAGAR_AGORA), true, R.string.msg_processando_dados_pedido, true);
    }

    private Transacao getResumoPedido(final Pedido pedido, final int acao) {
        return new Transacao() {

            @Override
            public void execute() throws Exception {
                superService.detalhesPedido(getBaseContext(), superActivity, superActivity, pedido.getId());
            }

            @Override
            public void onSuccess(Response response) {

                switch (acao){

                    case VER_CONVITES: {
                        Bundle param = new Bundle();
                        param.putSerializable("pedido", response.getResumo().getPedido());
                        SuperUtil.showTop(getBaseContext(), AlertConvitesActivity_.class, param);
                    }break;

                    case AVALIAR: {
                        Bundle param = new Bundle();
                        param.putSerializable("pedido", response.getResumo().getPedido());
                        SuperUtil.showTop(getBaseContext(), AlertAvaliacaoEventoActivity_.class, param);
                    }break;

                    case PAGAR_AGORA: {
                        Bundle param = new Bundle();
                        param.putSerializable("resumo", response.getResumo());
                        SuperUtil.showTop(getBaseContext(), PagamentoActivity_.class, param);
                    }break;

                }
            }

            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }
        };
    }

    public void onClickAvalie(UsuarioPedido usuarioPedido, Pedido pedido){
        doInBackground(getResumoPedido(pedido, AVALIAR), true, R.string.msg_processando_dados_pedido, true);
    }

    public void onClickDetalhes(UsuarioPedido usuarioPedido, Pedido pedido){
        Bundle params = new Bundle();
        params.putSerializable("pedidoId", pedido.getId());
        SuperUtil.show(getBaseContext(),DetalheEventoQueVouActivity_.class, params);
    }

    @Override
    public void onSelectedMax(String max) {
        pedidoFiltro.setPeriodoAte(max);

//        boolean pesquisa = false;
//        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEventoQueVou().periodoAte) && !StringUtils.equalsIgnoreCase(max, SuperApplication.getSuperCache().getPesquisaEventoQueVou().periodoAte)){
//            pesquisa = true;
//        }

        SuperApplication.getSuperCache().getPesquisaEventoQueVou().periodoAte = max;

//        if(pesquisa){
//            doInBackground(getTransacaoPesquisa(pedidoFiltro), true);
//        }
    }

    @Override
    public void onSelectedMin(String min) {
        pedidoFiltro.setPeriodoDe(min);

//        boolean pesquisa = false;
//        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEventoQueVou().periodoDe) && !StringUtils.equalsIgnoreCase(min, SuperApplication.getSuperCache().getPesquisaEventoQueVou().periodoDe)){
//            pesquisa = true;
//        }

        SuperApplication.getSuperCache().getPesquisaEventoQueVou().periodoDe = min;

//        if(pesquisa){
//            doInBackground(getTransacaoPesquisa(pedidoFiltro), true);
//        }
    }

    @Override
    public void filtrar() {
        doInBackground(getTransacaoPesquisa(pedidoFiltro), true);
    }
}
