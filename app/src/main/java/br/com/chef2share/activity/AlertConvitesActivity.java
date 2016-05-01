package br.com.chef2share.activity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

@EActivity(R.layout.alert_convites)
public class AlertConvitesActivity extends SuperActivity {


    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtTituloEvento;
    @ViewById public TextView txtNomeChef;
    @ViewById public TextView txtData;
    @ViewById public TextView txtValorEvento;
    @ViewById public TextView txtNomeLocal;
    @ViewById public TextView txtEnderecoLocal;
    @ViewById public ExpandableListView listaExpandidaConvites;

    @Extra("pedido")
    public Pedido pedido;


    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.GONE);

        Evento evento = pedido.getEvento();
        Chef chef = evento.getChef();

        Passo1 passo1 = evento.getPasso1();
        Passo2 passo2 = evento.getPasso2();
        Passo3 passo3 = evento.getPasso3();


        setTextString(txtData, passo3.getDataPorExtenso());
        setTextString(txtNomeLocal, passo1.getNome());
        setTextString(txtEnderecoLocal, passo1.getEnderecoCompleto(pedido.getStatus() == StatusPedido.PAGO));
        setTextString(txtTituloEvento, passo2.getTitulo());
        setTextString(txtValorEvento, passo3.getValorFormatado(true));
        setTextString(txtNomeChef, chef.getNome());

        ExpandableListAdapter adapterConvites = getAdapterConvites(pedido);
        listaExpandidaConvites.setAdapter(adapterConvites);
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
}
