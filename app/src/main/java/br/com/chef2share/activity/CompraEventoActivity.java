package br.com.chef2share.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.utils.lib.utils.MoneyUtils;
import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.ViewPagerAdapter;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Detalhes;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Pedido;
import br.com.chef2share.domain.Reserva;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.request.Compra;
import br.com.chef2share.domain.request.Ingresso;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.enums.StatusPedido;
import br.com.chef2share.enums.TipoIngresso;
import br.com.chef2share.fragment.FragmentViewPagerFotosDetalheEvento;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.activity_comprar)
public class CompraEventoActivity extends SuperActivity  {


    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtSubTitulo;
    @ViewById public TextView txtTituloEvento;
    @ViewById public TextView txtOndeEndereco;
    @ViewById public TextView txtValorIndividual;
    @ViewById public TextView txtValorDuplo;
    @ViewById public TextView txtValorTotal;
    @ViewById public ProgressBar progressImgChef;
    @ViewById public RatingBar ratingAvaliacaoChef;
    @ViewById public RoundedImageView imgChef;
    @ViewById public TextView txtNomeChef;
    @ViewById public Button btnComprar;
    @ViewById public Spinner spinnerQuantidadeIndividual;
    @ViewById public Spinner spinnerQuantidadeDuplo;
    @ViewById public LinearLayout layoutDuplo;

    @ViewById public ViewPager viewPagerFotosEvento;

    @Extra("detalhes")
    public Detalhes detalhes;

    private double valorIndividual;
    private double valorDuplo;
    private int qtdeIndividual;
    private int qtdeDuplo;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        populaTela(detalhes);
    }

    private Transacao getTransacaoConfirmaCompra(final Compra compra) {
        return new Transacao() {

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(compra);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.EVENTO_COMPRAR, json);
            }

            @Override
            public void onSuccess(Response response) {
                final Reserva reserva = response.getRealizado();
                if(reserva.getPedido().getStatus() == StatusPedido.PAGO){

                    Bundle param = new Bundle();
                    param.putSerializable("pedidoId", reserva.getPedido().getId());
                    param.putSerializable("status", reserva.getPedido().getStatus().name());
                    SuperUtil.showTop(getBaseContext(), AlertResultadoCompraActivity_.class, param);
                    finish();
                    return;
                }
                if(reserva.getPedido().getStatus() == StatusPedido.RESERVADO){
//                    SuperUtil.alertDialog(activity, "Sua reserva foi realizada com sucesso. Você será direcionado para a página de pagamento.", new Runnable() {
//                        @Override
//                        public void run() {
                            doInBackground(getResumoPedido(reserva.getPedido()), true);
//                        }
//                    });
//                    return;
                }
            }

            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }
        };
    }

    private void populaTela(Detalhes detalhe) {

        Evento evento = detalhe.getEvento();
        Chef chef = evento.getChef();

        Passo1 passo1 = detalhe.getPasso1();
        Passo2 passo2 = detalhe.getPasso2();
        Passo3 passo3 = detalhe.getPasso3();

        setTextString(txtTitulo, passo2.getTitulo());
        setTextString(txtSubTitulo, passo3.getDataPorExtenso());

        /**
         * se nao tem desconto antecipado, ou tem desnconto antecipado mas já passou a data,
         * OU  nem valor promocional duplo, não mostra o layout promocional.
         */
        if(passo3.isPossuiValorAntecipado()) {
            valorIndividual = passo3.getValorAntecipado();
        }else{
            valorIndividual = passo3.getValor();
        }

        if(passo3.getValorDuplo() != null && passo3.getValorDuplo() != 0){
            layoutDuplo.setVisibility(View.VISIBLE);
            valorDuplo = passo3.getValorDuplo();
        }else {
            layoutDuplo.setVisibility(View.GONE);
        }

        spinnerQuantidadeIndividual.setAdapter(getAdapterQuantidadeDisponivel());
        spinnerQuantidadeDuplo.setAdapter(getAdapterQuantidadeDisponivel());

        setTextString(txtOndeEndereco, passo1.getEnderecoCompleto(false));

        ratingAvaliacaoChef.setRating((float) chef.getAvaliacaoMedia());
        setTextString(txtNomeChef, chef.getNome());

        String urlImgChef = SuperCloudinery.getUrlImgPessoa(getBaseContext(), chef.getImagem());
        if(StringUtils.isNotEmpty(urlImgChef)) {
            Picasso.with(getBaseContext()).load(urlImgChef).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(imgChef, new Callback() {
                @Override
                public void onSuccess() {
                    progressImgChef.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressImgChef.setVisibility(View.GONE);
                }
            });
        }else{
            imgChef.setImageResource(R.drawable.userpic);
            progressImgChef.setVisibility(View.GONE);
        }

        List<Fragment> fragmentsFotoEvento = getFragmentsFotoEvento(passo2);
        if(fragmentsFotoEvento != null && fragmentsFotoEvento.size() > 0) {
            viewPagerFotosEvento.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentsFotoEvento));
            viewPagerFotosEvento.setVisibility(View.VISIBLE);
        }else{
            viewPagerFotosEvento.setVisibility(View.GONE);
        }
    }

    /**
     * De acordo com a quantidade selecionada vai sendo alterado a disponibilidade de convites.
     * @return
     */
    private SpinnerAdapter getAdapterQuantidadeDisponivel() {
        List<Integer> valores = new ArrayList<Integer>();

        int quantidade = Integer.parseInt(detalhes.getRestantes());
        for (int i = 0; i <= quantidade; i++){
            valores.add(i);
        }

        ArrayAdapter adapter = new ArrayAdapter<Integer>(this, R.layout.item_spinner, valores);
        adapter.setDropDownViewResource(R.layout.item_spinner_drodown);
        return adapter;
    }

    private List<Fragment> getFragmentsFotoEvento(Passo2 passo2) {
        List<Fragment> list = new ArrayList<Fragment>();
        List<ImagemPasso> imagensPasso2 = passo2.getImagensPasso2();
        if(imagensPasso2 != null && imagensPasso2.size() > 0){
            for (ImagemPasso i : imagensPasso2) {
                list.add(FragmentViewPagerFotosDetalheEvento.newInstance(i.getImagem()));
            }
        }
        return list;
    }

    @Click(R.id.btnComprar)
    public void onClickComprar(View v){

        Compra compra = new Compra();

        Evento evento = new Evento();
        evento.setId(detalhes.getEvento().getId());
        compra.setEvento(evento);

        List<Ingresso> ingressos = new ArrayList<Ingresso>();
        if(qtdeIndividual > 0) {
            ingressos.add(new Ingresso(TipoIngresso.INDIVIDUAL, qtdeIndividual));
        }

        if(qtdeDuplo > 0){
            ingressos.add(new Ingresso(TipoIngresso.DUPLO, qtdeDuplo));
        }

        if(ingressos.size() == 0){
            SuperUtil.alertDialog(this, R.string.msg_validacao_qtde_ingressos);
            return;
        }

        compra.setIngressos(ingressos);
        doInBackground(getTransacaoConfirmaCompra(compra));
    }

    @Override
    public void onClickVoltar(View v) {
        finish();
    }

    @ItemSelect(R.id.spinnerQuantidadeIndividual)
    public void onSelectQuantidadeIndividual(boolean selected, Integer qtde){
        this.qtdeIndividual = qtde;
        setTextString(txtValorIndividual, MoneyUtils.formatReais(getValorTotalIndividual(), 2, true));
        aplicaValorTotalCompra();
    }

    private void aplicaValorTotalCompra() {
        setTextString(txtValorTotal, MoneyUtils.formatReais(getValorTotal(), 2, true));
    }

    private double getValorTotal() {
        double valorTotal = getValorTotalDuplo() + getValorTotalIndividual();
        return valorTotal;
    }

    @ItemSelect(R.id.spinnerQuantidadeDuplo)
    public void onSelectQuantidadeDuplo(boolean selected, Integer qtde){
        this.qtdeDuplo = qtde;
        setTextString(txtValorDuplo, MoneyUtils.formatReais(getValorTotalDuplo(), 2, true));
        aplicaValorTotalCompra();
    }

    private double getValorTotalIndividual() {
        return qtdeIndividual * valorIndividual;
    }

    private double getValorTotalDuplo() {
        return qtdeDuplo * valorDuplo;
    }

    private Transacao getResumoPedido(final Pedido pedido) {
        return new Transacao() {

            @Override
            public void execute() throws Exception {
                superService.detalhesPedido(getBaseContext(), superActivity, superActivity, pedido.getId());
            }

            @Override
            public void onSuccess(Response response) {
                Bundle param = new Bundle();
                param.putSerializable("resumo", response.getResumo());
                SuperUtil.showTop(getBaseContext(), PagamentoActivity_.class, param);
                finish();
            }

            /**
             * Se der algum problema para obter o resumo do pedido, direciona o usuario para a tela de evento que vou
             * para não correr o risco dele pagar 2 vezes pelo mesmo pedido.
             * @param msgErro
             */
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro, new Runnable() {
                    @Override
                    public void run() {
                        SuperUtil.showTop(activity, EventosQueVouActivity_.class);
                        finish();
                    }
                });
            }
        };
    }
}
