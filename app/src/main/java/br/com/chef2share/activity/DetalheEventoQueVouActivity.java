package br.com.chef2share.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.ExpandableListAdapter;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Convite;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Ingresso;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Pedido;
import br.com.chef2share.domain.QRCode;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.Resumo;
import br.com.chef2share.enums.StatusPedido;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.activity_detalhes_evento_que_vou)
public class DetalheEventoQueVouActivity extends SuperActivity  implements OnMapReadyCallback {


    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtSubTitulo;
    @ViewById public TextView txtTituloEvento;
    @ViewById public TextView txtData;
    @ViewById public TextView txtValorEvento;
    @ViewById public TextView txtNomeLocal;
    @ViewById public TextView txtEnderecoLocal;

    @ViewById public ProgressBar progressImgChef;
    @ViewById public RoundedImageView imgChef;
    @ViewById public TextView txtNomeChef;

    @ViewById public LinearLayout layoutPagarAgora;
    @ViewById public LinearLayout layoutAvalie;
    @ViewById public LinearLayout layoutEventoAvaliado;
    @ViewById public LinearLayout layoutAguardandoPagamento;
    @ViewById public LinearLayout layoutPagamentoEfetuado;
    @ViewById public LinearLayout layoutVerConvites;
    @ViewById public LinearLayout layoutAddConvites;
    @ViewById public LinearLayout layoutAguarde;
    @ViewById public CardView layoutConvites;
    @ViewById public RatingBar ratingAvaliacaoEvento;

    @Extra("pedidoId")
    public String pedidoId;

    private GoogleMap mMap;
    private Resumo resumo;
    private Pedido pedido;


    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        layoutAguarde.setVisibility(View.VISIBLE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        layoutAguardandoPagamento.setVisibility(View.GONE);
        layoutAvalie.setVisibility(View.GONE);
        layoutEventoAvaliado.setVisibility(View.GONE);
        layoutPagarAgora.setVisibility(View.GONE);
        layoutPagamentoEfetuado.setVisibility(View.GONE);
        layoutVerConvites.setVisibility(View.GONE);
        layoutConvites.setVisibility(View.GONE);

        doInBackground(getTransacaoDetalhesEvento(pedidoId), false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private Transacao getTransacaoDetalhesEvento(final String eventoId) {
        return new Transacao() {

            @Override
            public void execute() throws Exception {
                superService.detalhesPedido(getBaseContext(), superActivity, superActivity, pedidoId);
            }

            @Override
            public void onSuccess(Response response) {
                populaTela(response.getResumo());
                layoutAguarde.setVisibility(View.GONE);
            }

            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro, new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        };
    }

    private void populaTela(Resumo resumo) {
        this.resumo = resumo;

        this.pedido = resumo.getPedido();
        Evento evento = pedido.getEvento();
        Chef chef = evento.getChef();

        Passo1 passo1 = evento.getPasso1();
        Passo2 passo2 = evento.getPasso2();
        Passo3 passo3 = evento.getPasso3();

        if(mMap != null){
            LatLng eventoLatLong = new LatLng(passo1.getLatitude(), passo1.getLongitude());
            MarkerOptions marker = new MarkerOptions().position(eventoLatLong).title(passo1.getNome() + " - " + passo2.getTitulo());
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(eventoLatLong));

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            CameraUpdate center = CameraUpdateFactory.newLatLng(eventoLatLong);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }

        setTextString(txtTitulo, passo2.getTitulo());
        setTextString(txtSubTitulo, passo3.getDataPorExtenso());

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

        setTextString(txtData, passo3.getDataPorExtenso());
        setTextString(txtNomeLocal, passo1.getNome());
        setTextString(txtEnderecoLocal, passo1.getEnderecoCompleto(pedido.getStatus() == StatusPedido.PAGO));

        setTextString(txtTituloEvento, passo2.getTitulo());
        txtTituloEvento.setPaintFlags(txtTituloEvento.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        setTextString(txtValorEvento, passo3.getValorFormatado(true));
        setTextString(txtNomeChef, chef.getNome());
        ratingAvaliacaoEvento.setRating((float) pedido.getEvento().getAvaliacaoMedia());

        switch (pedido.getStatus()){
            case AGUARDANDO_PAGAMENTO:
                layoutAguardandoPagamento.setVisibility(View.VISIBLE);
                break;

            case PAGO:
                txtData.setPaintFlags(txtData.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                layoutPagamentoEfetuado.setVisibility(View.VISIBLE);
                layoutConvites.setVisibility(View.VISIBLE);
                addIngressos(pedido);
//                ExpandableListAdapter adapterConvites = getAdapterConvites(pedido);
//                listaExpandidaConvites.setAdapter(adapterConvites);

                break;
            case REALIZADO:
                if(pedido.getEvento().getAvaliacaoMedia() <= 0){
                    layoutAvalie.setVisibility(View.VISIBLE);
                }else{
                    layoutEventoAvaliado.setVisibility(View.VISIBLE);
                }
                break;

            case RESERVADO:
                layoutPagarAgora.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void addIngressos(Pedido pedido) {
        HashMap<String, List<QRCode>> hash = getIngressos(pedido);

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());

        Set<String> strings = hash.keySet();
        for (String titulo: strings) {
            View header = inflater.inflate(R.layout.titulo_lista_expandida, null);
            TextView txtTituloConvite = (TextView) header.findViewById(R.id.txtTituloConvite);
            txtTituloConvite.setText(titulo);
            layoutAddConvites.addView(header, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            List<QRCode> list = hash.get(titulo);
            for (QRCode qrcode : list) {
                if(qrcode != null) {
                    View convertView = inflater.inflate(R.layout.ingresso_item, null);

                    TextView txtCodigoConvite = (TextView) convertView.findViewById(R.id.txtCodigoConvite);
                    TextView txtNomeConvite = (TextView) convertView.findViewById(R.id.txtNomeConvite);
                    ImageView imgQRCodeConvite = (ImageView) convertView.findViewById(R.id.imgQRCodeConvite);
                    final ProgressBar progressImgQRCode = (ProgressBar) convertView.findViewById(R.id.progressImgQRCode);

                    imgQRCodeConvite.getLayoutParams().height = 800;
                    imgQRCodeConvite.getLayoutParams().width = 800;

                    txtCodigoConvite.setText("#" + qrcode.getText());
                    if (StringUtils.isNotEmpty(qrcode.getUrl())) {
                        Picasso.with(getBaseContext()).load(qrcode.getUrl()).resize(800, 800).centerCrop().into(imgQRCodeConvite, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressImgQRCode.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressImgQRCode.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        progressImgQRCode.setVisibility(View.GONE);
                    }

                    layoutAddConvites.addView(convertView);
                }
            }

        }

    }

    public HashMap<String, List<QRCode>> getIngressos(Pedido pedido){
        List<String> listDataHeader = new ArrayList<String>();
        HashMap<String, List<QRCode>> listDataChild = new HashMap<String, List<QRCode>>();

        List<Ingresso> ingressos = pedido.getIngressos();
        for (Ingresso i : ingressos) {
            List<Convite> convites = i.getConvites();

            for (Convite c : convites) {
                listDataHeader.add(c.getTitulo());

                List<QRCode> listQRCode = new ArrayList<QRCode>();
                listQRCode.add(c.getQrcode());
                listDataChild.put(c.getTitulo(), listQRCode);
            }

        }

        return listDataChild;
    }

    private ExpandableListAdapter getAdapterConvites(Pedido pedido) {

        List<String> listDataHeader = new ArrayList<String>();
        HashMap<String, List<QRCode>> listDataChild = new HashMap<String, List<QRCode>>();

        List<Ingresso> ingressos = pedido.getIngressos();
        for (Ingresso i : ingressos) {
            List<Convite> convites = i.getConvites();

            for (Convite c : convites) {
                listDataHeader.add(c.getTitulo());

                List<QRCode> listQRCode = new ArrayList<QRCode>();
                listQRCode.add(c.getQrcode());
                listDataChild.put(c.getTitulo(), listQRCode);
            }

        }


        return new ExpandableListAdapter(this, listDataHeader, listDataChild);
    }

    @Click({R.id.btnDetalhes, R.id.txtTituloEvento})
    public void onClickDetalhesEvento(View v){
        Bundle param = new Bundle();
        param.putSerializable("eventoId", resumo.getPedido().getEvento().getId());
        SuperUtil.showTop(getBaseContext(), DetalheEventoActivity_.class, param);
    }

    @Click(R.id.btnPagarAgora)
    public void onClickPagarAgora(View v){
        Bundle param = new Bundle();
        param.putSerializable("urlPagamento", resumo.getUrl());
        SuperUtil.showTop(getBaseContext(), PagamentoActivity_.class, param);
    }

    @Click(R.id.btnVerConvites)
    public void onClickVerConvites(View v){
        Bundle param = new Bundle();
        param.putSerializable("pedido", pedido);
        SuperUtil.showTop(getBaseContext(), AlertConvitesActivity_.class, param);
    }

    @Click(R.id.btnAvalie)
    public void onClickAvaliar(View v){
        Bundle param = new Bundle();
        param.putSerializable("pedido", pedido);
        SuperUtil.showTop(getBaseContext(), AlertConvitesActivity_.class, param);
    }

    @Click(R.id.btnMaisInfoChef)
    public void onClickMaisInfoChef(View v){
//        Bundle params = new Bundle();
//        params.putSerializable("detalhes", detalhes);
//        SuperUtil.show(getBaseContext(), AlertMaisInfoChefActivity_.class, params);
    }

    @Click(R.id.addEventoCalendario)
    public void onClickAddEventoCalendar(View v){

       SuperUtils.addEventoCalendario(this, pedido);
    }


    @Override
    public void onClickVoltar(View v) {
        finish();
    }
}
