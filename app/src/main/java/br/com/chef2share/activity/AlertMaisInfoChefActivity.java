package br.com.chef2share.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
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
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.ViewPagerAdapter;
import br.com.chef2share.domain.Avaliacao;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Detalhes;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.fragment.FragmentViewPagerComentariosDetalheEvento;
import br.com.chef2share.fragment.FragmentViewPagerFotosDetalheEvento;
import br.com.chef2share.fragment.FragmentViewPagerOutrosEventosChef;
import br.com.chef2share.fragment.FragmentViewPagerParticipanteEvento;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.alert_mais_info_chef)
public class AlertMaisInfoChefActivity extends SuperActivity {

    @ViewById public TextView txtInfoChef;
    @ViewById public ProgressBar progressImgChef;
    @ViewById public RatingBar ratingAvaliacaoChef;
    @ViewById public RoundedImageView imgChef;
    @ViewById public TextView txtNomeChef;

    @Extra("detalhes")
    public Detalhes detalhes;

    @Override
    public void init() {
        super.init();

        Evento evento = detalhes.getEvento();
        Chef chef = evento.getChef();

        Passo1 passo1 = detalhes.getPasso1();
        Passo2 passo2 = detalhes.getPasso2();
        Passo3 passo3 = detalhes.getPasso3();

        setTextString(txtInfoChef, chef.getResumo());
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
    }

    @Click(R.id.btnFecharMaisInfoChef)
    public void onClickFechar(View v){
        finish();
    }
}
