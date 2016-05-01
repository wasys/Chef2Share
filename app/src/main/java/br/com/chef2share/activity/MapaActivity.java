package br.com.chef2share.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.utils.lib.utils.ColorUtils;
import com.android.utils.lib.utils.StringUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.EventoRecyclerViewAdapter;
import br.com.chef2share.adapter.ViewPagerAdapter;
import br.com.chef2share.domain.Busca;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.request.BuscaFiltro;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.fragment.FragmentViewPagerEventoMapa;
import br.com.chef2share.fragment.FragmentViewPagerFotosDetalheEvento;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.activity_mapa)
public class MapaActivity extends SuperActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @ViewById public TextView txtTitulo;
    @ViewById public Button btnRefazerBuscaRegiao;
    @ViewById public ViewPager viewPagerEventos;
    private final int ZOOM_MAPA = 13;

    /**
     * Auxiliar para saber qual marker esta referenciando qual evento
     */
    private Map<Marker, Evento> haspMapMarker = new HashMap<Marker, Evento>();

    @Extra("buscaFiltro")
    public BuscaFiltro buscaFiltro;

    @Extra("busca")
    public Busca busca;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        setTextString(txtTitulo, getResources().getString(R.string.titulo_buscar_eventos_mapa));

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        viewPagerEventos.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getBaseContext()) / 2;
        viewPagerEventos.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getFragmentsEventos()));
        viewPagerEventos.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Evento evento = busca.getPublicados().get(position);
                Set<Marker> set = haspMapMarker.keySet();
                for (Marker marker : set) {
                    if (haspMapMarker.get(marker).equals(evento)) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM_MAPA));
                        marker.showInfoWindow();
                    }else{
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapa_verde));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                viewPagerEventos.setCurrentItem(busca.getPublicados().indexOf(haspMapMarker.get(marker)));
                marker.showInfoWindow();
                return true;
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
//                buscaFiltro.setLatitude(cameraPosition.target.latitude);
//                buscaFiltro.setLongitude(cameraPosition.target.longitude);
                btnRefazerBuscaRegiao.setVisibility(View.VISIBLE);
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                View rowView = inflater.inflate(R.layout.icon_map_marker, null);
                final ImageView imgPin = (ImageView) rowView.findViewById(R.id.imgPin);
                final RoundedImageView imgChef = (RoundedImageView) rowView.findViewById(R.id.imgChef);
                final ProgressBar progressImgChef = (ProgressBar) rowView.findViewById(R.id.progressImgChef);
                final TextView txtNomeChef = (TextView) rowView.findViewById(R.id.txtNomeChef);
                final RatingBar ratingAvaliacaoChef = (RatingBar) rowView.findViewById(R.id.ratingAvaliacaoChef);


                Evento evento = haspMapMarker.get(marker);
                Chef chef = evento.getChef();

                setTextString(txtNomeChef, chef.getNome());
                ratingAvaliacaoChef.setRating((float) chef.getAvaliacaoMedia());

                String urlImgChef = SuperCloudinery.getUrlImgPessoa(getBaseContext(), chef.getImagem());
                if (StringUtils.isNotEmpty(urlImgChef)) {
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
                } else {
                    imgChef.setImageResource(R.drawable.userpic);
                    progressImgChef.setVisibility(View.GONE);
                }

                return rowView;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        Marker markerFirst = null;

        for (Evento e: busca.getPublicados()) {
            LatLng latLong = new LatLng(e.getPasso1().getLatitude(), e.getPasso1().getLongitude());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLong)
                    .title(e.getPasso2().getTitulo())
                    .snippet(e.getChef().getNome())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapa_verde)));

//            criaPin(marker, e, R.drawable.pin_cinza);

            //cria um hashmap de marker para evento para saber onde esta o que
            haspMapMarker.put(marker, e);

            if(markerFirst == null){
                markerFirst = marker;
            }
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerFirst.getPosition(), ZOOM_MAPA));
        markerFirst.showInfoWindow();
    }

    @Background
    public void criaPin(Marker marker, Evento evento, int drawableResource) {

        Bitmap bmpChef = null;
        Bitmap bmpBackground = BitmapFactory.decodeResource(getResources(), drawableResource);

        Chef chef = evento.getChef();
        String urlImgChef = SuperCloudinery.getUrlImgPessoa(getBaseContext(), chef.getImagem());
        if (StringUtils.isNotEmpty(urlImgChef)) {
            try {
                bmpChef = Picasso.with(getBaseContext()).load(urlImgChef).get();
            }catch(Exception ex){
            }
        }

        if (bmpChef == null) {
            bmpChef = BitmapFactory.decodeResource(getResources(), R.drawable.userpic);
        }
        Bitmap bmpOverlay = overlay(bmpBackground, bmpChef);
        atualizaMarker(marker, bmpOverlay);
    }

    @UiThread
    public void atualizaMarker(Marker marker, Bitmap bmpOverlay) {
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(bmpOverlay));
    }

    @Click(R.id.btnRefazerBuscaRegiao)
    public void onClickRefazerBusca(View v){
        doInBackground(getTransacaoBuscaFiltro(buscaFiltro), true);

        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();

        LatLngBounds latLngBounds = visibleRegion.latLngBounds;
        if(latLngBounds != null) {
            buscaFiltro.setLatitudeNorte(latLngBounds.northeast.latitude);
            buscaFiltro.setLongitudeNorte(latLngBounds.northeast.longitude);
            buscaFiltro.setLatitudeSul(latLngBounds.southwest.latitude);
            buscaFiltro.setLongitudeSul(latLngBounds.southwest.longitude);

            Marker markerNorte = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(buscaFiltro.getLatitudeNorte(), buscaFiltro.getLongitudeNorte()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapa_verde)));

            Marker markerSul = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(buscaFiltro.getLatitudeSul(), buscaFiltro.getLongitudeSul()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapa_verde)));

            SuperUtil.toast(getBaseContext(), "Norte: " + buscaFiltro.getLatitudeNorte() + "/" + buscaFiltro.getLatitudeNorte()+ "/n" + "Sul: " + buscaFiltro.getLatitudeSul()+ "/" + buscaFiltro.getLongitudeSul());
        }
    }

    private List<Fragment> getFragmentsEventos() {
        List<Fragment> list = new ArrayList<Fragment>();
        List<Evento> eventos = busca.getPublicados();
        if(eventos != null && eventos.size() > 0){
            for (Evento e : eventos) {
                list.add(FragmentViewPagerEventoMapa.newInstance(e));
            }
        }
        return list;
    }

    @Override
    public void onClickVoltar(View v) {
        finish();
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
                busca = response.getBusca();

                init();
                onMapReady(mMap);
            }

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(buscaFiltro);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.EVENTO_BUSCA, json);
            }
        };
    }

    private Bitmap overlay(Bitmap bmpBackground, Bitmap bmpPrincipal) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmpBackground.getWidth(), bmpBackground.getHeight(), bmpBackground.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmpPrincipal, new Matrix(), null);
        canvas.drawBitmap(bmpBackground, new Matrix(), null);

        Paint paint = new Paint();
        paint.setColor(Color.GRAY);

        canvas.drawCircle(bmpBackground.getWidth(),  bmpBackground.getHeight(), 10, paint);
        return bmOverlay;
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(120, 120, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
}