package br.com.chef2share.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.utils.lib.infra.AppUtil;
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
import java.util.List;
import java.util.zip.Inflater;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.ViewPagerAdapter;
import br.com.chef2share.domain.Avaliacao;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Detalhes;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.OutrasDatas;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.TipoEvento;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.fragment.FragmentViewPagerComentariosDetalheEvento;
import br.com.chef2share.fragment.FragmentViewPagerFotosDetalheEvento;
import br.com.chef2share.fragment.FragmentViewPagerOutrosEventosChef;
import br.com.chef2share.fragment.FragmentViewPagerParticipanteEvento;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.CirclePageIndicator;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.activity_detalhes_evento)
public class DetalheEventoActivity extends SuperActivity  implements OnMapReadyCallback {


    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtSubTitulo;
    @ViewById public TextView txtTituloEvento;
    @ViewById public TextView txtValorEvento;
    @ViewById public TextView txtVagasDisponiveis;
    @ViewById public TextView txtQuando;
    @ViewById public TextView txtValorPromocionalIndividual;
    @ViewById public TextView txtValorPromocionalIndividualValidade;
    @ViewById public TextView txtValorPromocionalColetivo;
    @ViewById public TextView txtValorPromocionalQtdeColetivo;
    @ViewById public TextView txtInfoMenu;
    @ViewById public TextView txtInfoBebidas;
    @ViewById public TextView txtInfoChef;
    @ViewById public TextView txtOnde;
    @ViewById public TextView txtOndeEndereco;
    @ViewById public TextView txtConvidadosBarraDisponivel;
    @ViewById public TextView txtQtdeTotalAvaliacoes;
    @ViewById public TextView txtNomeChefOutrosEventos;
    @ViewById public RatingBar ratingAvaliacaoAtendimento;
    @ViewById public RatingBar ratingAvaliacaoComida;
    @ViewById public RatingBar ratingAvaliacaoLocal;

    @ViewById public ProgressBar progressImgChef;
    @ViewById public RatingBar ratingAvaliacaoChef;
    @ViewById public RoundedImageView imgChef;
    @ViewById public TextView txtNomeChef;
    @ViewById public Button btnComprar;
    @ViewById public Button btnAgendeEventoExclusivo;

    @ViewById public ViewPager viewPagerFotosEvento;
    @ViewById public ViewPager viewPagerFotosLocal;
    @ViewById public ViewPager viewPagerParticipantes;
    @ViewById public ViewPager viewPagerOutrosEventosChef;
    @ViewById public ViewPager viewPagerComentarios;
    @ViewById public LinearLayout layoutAguarde;
    @ViewById public LinearLayout layoutPromocional;
    @ViewById public LinearLayout layoutOutrosEventosChef;
    @ViewById public LinearLayout layoutEventoRealizado;
    @ViewById public LinearLayout layoutEventoFuturo;
    @ViewById public LinearLayout layoutBebidas;
    @ViewById public LinearLayout layoutOutrasDatas;
    @ViewById public LinearLayout layoutConvidados;
    @ViewById public LinearLayout layoutAddOutrasDatas;
    @ViewById public CirclePageIndicator pageIndicatorEventos;
    @ViewById public CirclePageIndicator pageIndicatorFotosLocal;
    @ViewById public CirclePageIndicator pageIndicatorParticipantes;
    @ViewById public CirclePageIndicator pageIndicatorComentarios;
    @ViewById public CirclePageIndicator pageIndicatorOutrosEventosChef;

    @Extra("eventoId")
    public String eventoId;

    private GoogleMap mMap;
    private Detalhes detalhes;


    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        layoutAguarde.setVisibility(View.VISIBLE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        viewPagerFotosEvento.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getBaseContext());
        viewPagerComentarios.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getBaseContext());
        viewPagerFotosLocal.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getBaseContext());
        viewPagerOutrosEventosChef.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getBaseContext());
//        viewPagerParticipantes.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getBaseContext());

        doInBackground(getTransacaoDetalhesEvento(eventoId), true, R.string.msg_aguarde_carregando_detalhes_evento, false, layoutAguarde, null);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private Transacao getTransacaoDetalhesEvento(final String eventoId) {
        return new Transacao() {

            @Override
            public void execute() throws Exception {
                superService.detalhesEvento(getBaseContext(), superActivity, superActivity, eventoId);
            }

            @Override
            public void onSuccess(Response response) {
                populaTela(response.getDetalhe());
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

    private void populaTela(Detalhes detalhe) {

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());

        this.detalhes = detalhe;

        Evento evento = detalhe.getEvento();
        Chef chef = evento.getChef();

        Passo1 passo1 = detalhe.getPasso1();
        Passo2 passo2 = detalhe.getPasso2();
        Passo3 passo3 = detalhe.getPasso3();

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
        setTextString(txtTituloEvento, passo2.getTitulo());
        setTextString(txtValorEvento, passo3.getValorFormatado(true));
        setTextString(txtVagasDisponiveis, detalhe.getRestantes() + " vagas");
        setTextString(txtQuando, passo3.getDataPorExtenso());
        txtQuando.setPaintFlags(txtQuando.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        /**
         * se nao tem desconto antecipado, ou tem desnconto antecipado mas já passou a data,
         * OU  nem valor promocional duplo, não mostra o layout promocional.
         */
        if(passo3.isPossuiValorAntecipado() || (passo3.getValorDuplo() != null && passo3.getValorDuplo() > 0)) {
            layoutPromocional.setVisibility(View.VISIBLE);
        }else{
            layoutPromocional.setVisibility(View.GONE);
        }

        if(passo3.isPossuiValorAntecipado()){
            txtValorPromocionalIndividual.setVisibility(View.VISIBLE);
            txtValorPromocionalIndividualValidade.setVisibility(View.VISIBLE);
            setTextString(txtValorPromocionalIndividual, passo3.getValorAntecipadoFormatado());
            setTextString(txtValorPromocionalIndividualValidade, "até dia " + DateUtils.toString(passo3.getDataAntecipadoDate(), "d MMMM"));
        }else{
            txtValorPromocionalIndividual.setVisibility(View.GONE);
            txtValorPromocionalIndividualValidade.setVisibility(View.GONE);
        }

        if((passo3.getValorDuplo() != null && passo3.getValorDuplo() != 0)){
            txtValorPromocionalColetivo.setVisibility(View.VISIBLE);
            txtValorPromocionalQtdeColetivo.setVisibility(View.VISIBLE);
            setTextString(txtValorPromocionalColetivo, passo3.getValorDuploFormatado());
            setTextString(txtValorPromocionalQtdeColetivo, "para 2 convites");

        }else {
            txtValorPromocionalColetivo.setVisibility(View.GONE);
            txtValorPromocionalQtdeColetivo.setVisibility(View.GONE);
        }

        /**
         * Se o evento já aconteceu, nao deixa comprar
         */
        if(passo3.isEventoRealizado()){
            layoutEventoRealizado.setVisibility(View.VISIBLE);
            layoutEventoFuturo.setVisibility(View.GONE);
            layoutConvidados.setVisibility(View.GONE);
            btnAgendeEventoExclusivo.setVisibility(View.VISIBLE);
        }else{
            btnAgendeEventoExclusivo.setVisibility(View.GONE);
            layoutEventoRealizado.setVisibility(View.GONE);
            layoutEventoFuturo.setVisibility(View.VISIBLE);
            layoutConvidados.setVisibility(View.VISIBLE);
        }

        if(StringUtils.isNotEmpty(detalhes.getOutrasDatasPorExtenso())) {
            layoutOutrasDatas.setVisibility(View.VISIBLE);

            for(final OutrasDatas outraData : detalhes.getOutrasDatas()){

                TextView txtOutraData = (TextView) inflater.inflate(R.layout.include_item_outra_data, null);
                txtOutraData.setText(outraData.getDataPorExtenso() + " | ");
//                txtOutraData.setPaintFlags(txtQuando.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txtOutraData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle params = new Bundle();
                        params.putString("eventoId", outraData.getId());
                        SuperUtil.show(v.getContext(), DetalheEventoActivity_.class, params);
                    }
                });

                layoutAddOutrasDatas.addView(txtOutraData);
            }

        }else{
            layoutOutrasDatas.setVisibility(View.GONE);
        }


        setTextString(txtInfoMenu, passo2.getDescricao());

        if(StringUtils.isNotEmpty(passo2.getBebida())){
            layoutBebidas.setVisibility(View.VISIBLE);
            setTextString(txtInfoBebidas, passo2.getBebida());
        }else{
            layoutBebidas.setVisibility(View.GONE);
        }

        setTextString(txtInfoChef, chef.getResumo());
        setTextString(txtOnde, passo1.getNome());
        setTextString(txtOndeEndereco, passo1.getEnderecoCompleto(false));
        setTextString(txtConvidadosBarraDisponivel, detalhe.getQuantidadeConfirmada() + " de " + passo3.getMaximo() + " convidados");
        setTextString(txtQtdeTotalAvaliacoes, chef.getTotalAvaliadores());
        setTextString(txtNomeChefOutrosEventos, chef.getNome());
        ratingAvaliacaoAtendimento.setRating((float) SuperUtil.toDoublePrimitivo(chef.getAvaliacaoAtendimento()));
        ratingAvaliacaoComida.setRating((float) SuperUtil.toDoublePrimitivo(chef.getAvaliacaoComida()));
        ratingAvaliacaoLocal.setRating((float) SuperUtil.toDoublePrimitivo(chef.getAvaliacaoLocal()));

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
            pageIndicatorEventos.setViewPager(viewPagerFotosEvento);
        }else{
            viewPagerFotosEvento.setVisibility(View.GONE);
        }

        List<Fragment> fragmentsFotosLocal = getFragmentsFotoLocal(passo1);
        if (fragmentsFotosLocal != null && fragmentsFotosLocal.size() > 0) {
            viewPagerFotosLocal.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentsFotosLocal));
            viewPagerFotosLocal.setVisibility(View.VISIBLE);
            pageIndicatorFotosLocal.setViewPager(viewPagerFotosLocal);
        }else{
            viewPagerFotosLocal.setVisibility(View.GONE);
        }

        List<Fragment> fragmentsParticipantes = getFragmentsParticipantes(detalhe);
        if (fragmentsParticipantes != null && fragmentsParticipantes.size() > 0 && StringUtils.equalsIgnoreCase(passo3.getTipo(), "PUBLICO")) {
            viewPagerParticipantes.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentsParticipantes));
            viewPagerParticipantes.setVisibility(View.VISIBLE);
            pageIndicatorParticipantes.setViewPager(viewPagerParticipantes);
        }else{
            viewPagerParticipantes.setVisibility(View.GONE);
        }

        List<Fragment> fragmentsOutrosEventosChef = getFragmentsEventosChef(detalhe, chef);
        if (fragmentsOutrosEventosChef != null && fragmentsOutrosEventosChef.size() > 0) {
            viewPagerOutrosEventosChef.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentsOutrosEventosChef));
            layoutOutrosEventosChef.setVisibility(View.VISIBLE);
            pageIndicatorOutrosEventosChef.setViewPager(viewPagerOutrosEventosChef);
        }else{
            layoutOutrosEventosChef.setVisibility(View.GONE);
        }

        List<Fragment> fragmentsComentarios = getFragmentsComentarios(detalhe);
        if (fragmentsComentarios != null && fragmentsComentarios.size() > 0) {
            viewPagerComentarios.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentsComentarios));
            viewPagerComentarios.setVisibility(View.VISIBLE);
            pageIndicatorComentarios.setViewPager(viewPagerComentarios);
        }else{
            viewPagerComentarios.setVisibility(View.GONE);
        }

    }

    private List<Fragment> getFragmentsFotoLocal(Passo1 passo1) {
        List<Fragment> list = new ArrayList<Fragment>();
        List<ImagemPasso> imagensPasso1 = passo1.getImagensPasso1();
        if(imagensPasso1 != null && imagensPasso1.size() > 0){
            for (ImagemPasso i : imagensPasso1) {
                list.add(FragmentViewPagerFotosDetalheEvento.newInstance(i.getImagem()));
            }
        }
        return list;
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

    /**
     * Dois em Dois participantes por view
     * @param detalhes
     * @return
     */
    private List<Fragment> getFragmentsParticipantes(Detalhes detalhes) {
        List<Fragment> list = new ArrayList<Fragment>();
        List<Usuario> participantes = detalhes.getParticipantes();
        if(participantes != null && participantes.size() > 0){

            int qtde = participantes.size();
            int idx = 0;

            while (idx < qtde){
                Usuario participante1 = participantes.get(idx);
                Usuario participante2 = null;
                idx++;

                if(idx < qtde) {
                    participante2 = participantes.get(idx);
                    idx++;
                }

                list.add(FragmentViewPagerParticipanteEvento.newInstance(participante1, participante2));
            }

        }
        return list;
    }

    private List<Fragment> getFragmentsEventosChef(Detalhes detalhes, Chef chef) {
        List<Fragment> list = new ArrayList<Fragment>();
        List<Evento> eventos = detalhes.getOutrosEventos();
        if(eventos != null && eventos.size() > 0){
            for (Evento i : eventos) {
                list.add(FragmentViewPagerOutrosEventosChef.newInstance(i, chef));
            }
        }
        return list;
    }

    private List<Fragment> getFragmentsComentarios(Detalhes detalhes) {
        List<Fragment> list = new ArrayList<Fragment>();
        List<Avaliacao> avaliacoes = detalhes.getAvaliadores();
        if(avaliacoes != null && avaliacoes.size() > 0){
            for (Avaliacao i : avaliacoes) {
                list.add(FragmentViewPagerComentariosDetalheEvento.newInstance(i));
            }
        }
        return list;
    }

    @Click(R.id.btnComprar)
    public void onClickComprar(View v){
        Bundle params = new Bundle();
        params.putSerializable("detalhes", detalhes);
        SuperUtil.show(getBaseContext(), CompraEventoActivity_.class, params);
    }

    @Click(R.id.btnAgendeEventoExclusivo)
    public void onClickAgendarEventoExclusivo(View v){
        AppUtil.show(getBaseContext(), SolicitarEventoActivity_.class);
    }

    @Click(R.id.btnFaleComAnfitriao)
    public void onClickFalarComAnfitriao(View v){
        Chef chef = detalhes.getEvento().getChef();
        Bundle param = new Bundle();
        param.putSerializable("chef", chef);
        SuperUtil.show(getBaseContext(), DetalhesMensagemActivity_.class, param);
    }

    @Click({R.id.btnCompartilhar, R.id.btnShare})
    public void onClickCompartilhar(View v){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = detalhes.getCompartilhe().getFacebook();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Chef2Share - " + detalhes.getPasso2().getTitulo());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Compartilhe com..."));
    }

    @Click(R.id.btnMaisInfoBebidas)
    public void onClickMaisInfoBebidas(View v){
        Bundle params = new Bundle();
        params.putSerializable("detalhes", detalhes);
        SuperUtil.show(getBaseContext(), AlertMaisInfoBebidasActivity_.class, params);

    }

    @Click(R.id.btnMaisInfoChef)
    public void onClickMaisInfoChef(View v){
        Bundle params = new Bundle();
        params.putSerializable("detalhes", detalhes);
        SuperUtil.show(getBaseContext(), AlertMaisInfoChefActivity_.class, params);
    }

    @Click(R.id.btnMaisInfoOnde)
    public void onClickMaisInfoOnde(View v){
        Bundle params = new Bundle();
        params.putSerializable("detalhes", detalhes);
        SuperUtil.show(getBaseContext(), AlertMaisInfoOndeActivity_.class, params);
    }

    @Override
    public void onClickVoltar(View v) {
        finish();
    }
}
