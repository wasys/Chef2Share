package br.com.chef2share.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.BuscaEventosActivity_;
import br.com.chef2share.activity.DetalheEventoActivity_;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.EventoHome;
import br.com.chef2share.domain.Home;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.view.RoundedImageView;

/**
 * Created by Jonas on 21/11/2015.
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BANNER = 0;
    private static final int EVENTO = 1;

    private Context context;
    private Home home;

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeRecyclerViewAdapter(Context context, Home home) {
        this.context = context;
        this.home = home;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            //só o primeiro será BANNER
            case 0: return BANNER;

            //o resto é EVENTO
            default: return EVENTO;
        }
    }

    public static class ViewHolderEvento extends RecyclerView.ViewHolder {
        private EventoHome eventoHome;
        public ImageView imgBackground;
        public TextView txtTituloEvento;
        public LinearLayout layoutProgressImgBackgroundEvento;
        public RoundedImageView imgChef;
        public TextView txtNomeChef;
        public RatingBar ratingAvaliacaoChef;
        public ProgressBar progressImgChef;

        public ViewHolderEvento(View view) {
            super(view);

            imgBackground = (ImageView) view.findViewById(R.id.imgBackground);
            txtTituloEvento = (TextView) view.findViewById(R.id.txtTituloEvento);
            layoutProgressImgBackgroundEvento = (LinearLayout) view.findViewById(R.id.layoutProgressImgBackgroundEvento);
            imgChef = (RoundedImageView) view.findViewById(R.id.imgChef);
            txtNomeChef = (TextView) view.findViewById(R.id.txtNomeChef);
            ratingAvaliacaoChef = (RatingBar) view.findViewById(R.id.ratingAvaliacaoChef);
            progressImgChef = (ProgressBar) view.findViewById(R.id.progressImgChef);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle params = new Bundle();
                    params.putString("eventoId", getEventoHome().getId());
                    SuperUtil.show(v.getContext(), DetalheEventoActivity_.class, params);
                }
            });
        }

        public void setEventoHome(final EventoHome eventoHome) {
            this.eventoHome = eventoHome;
        }

        public EventoHome getEventoHome() {
            return eventoHome;
        }
    }

    public static class ViewHolderBanner extends RecyclerView.ViewHolder {
        public ImageView imgBackground;
        public TextView txtTituloBanner;
        public ProgressBar progressImgBackgroundBanner;
        public TextView txtSubTituloBanner;
        public Button btnProcurarEventos;

        public ViewHolderBanner(View view) {
            super(view);

            imgBackground = (ImageView) view.findViewById(R.id.imgBackground);
            txtTituloBanner = (TextView) view.findViewById(R.id.txtTituloBanner);
            txtSubTituloBanner = (TextView) view.findViewById(R.id.txtSubTituloBanner);
            btnProcurarEventos = (Button) view.findViewById(R.id.btnProcurarEventos);
            progressImgBackgroundBanner = (ProgressBar) view.findViewById(R.id.progressImgBackgroundBanner);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case BANNER: {
                View row = inflater.inflate(R.layout.home_list_item_banner, parent, false);
                return new ViewHolderBanner(row);
            }

            case EVENTO: {
                View row = inflater.inflate(R.layout.home_list_item_evento, parent, false);
                return new ViewHolderEvento(row);
            }
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolderBanner){
            final ViewHolderBanner viewHolderBanner = (ViewHolderBanner) holder;

            viewHolderBanner.btnProcurarEventos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SuperUtil.show(v.getContext(), BuscaEventosActivity_.class);
                }
            });

            String urlBanner = home.getBanner();
            Picasso.with(context).load(urlBanner).resize(SuperUtils.getWidthImagemFundo(context), SuperUtils.getHeightImagemFundo(context)).centerCrop().into(viewHolderBanner.imgBackground, new Callback() {
                @Override
                public void onSuccess() {
                    viewHolderBanner.progressImgBackgroundBanner.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    viewHolderBanner.progressImgBackgroundBanner.setVisibility(View.GONE);
                }
            });


            try {
                List<String> titulos = home.getTitulos();
                viewHolderBanner.txtTituloBanner.setText(titulos.get(0));
                viewHolderBanner.txtSubTituloBanner.setText(titulos.get(1));
            }catch(Exception e){
                viewHolderBanner.txtTituloBanner.setText(R.string.app_name);
                viewHolderBanner.txtSubTituloBanner.setText("");
            }


        }else if(holder instanceof ViewHolderEvento){

            final ViewHolderEvento viewHolderEvento = (ViewHolderEvento) holder;

            EventoHome evento = (EventoHome) home.getEventosHome().get(position - 1);
            viewHolderEvento.setEventoHome(evento);
            viewHolderEvento.txtTituloEvento.setText(evento.getTitulo());

            String url = SuperCloudinery.getUrl(context, evento.getImagem(), SuperUtils.getWidthImagemFundo(context), SuperUtils.getHeightImagemFundo(context));
            Picasso.with(context).load(url).into(viewHolderEvento.imgBackground);

            Chef chef = evento.getChef();
            viewHolderEvento.txtNomeChef.setText(chef.getNome());
            viewHolderEvento.ratingAvaliacaoChef.setRating((float) chef.getAvaliacaoMedia());

            String urlImgChef = SuperCloudinery.getUrlImgPessoa(context, chef.getImagem());

            if(StringUtils.isNotEmpty(urlImgChef)) {
//                imageLoader.displayImage(urlImgChef, viewHolderEvento.imgChef, null, new SuperImageLoader(context, viewHolderEvento.progressImgChef, R.drawable.userpic));
                Picasso.with(context).load(urlImgChef).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(viewHolderEvento.imgChef, new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolderEvento.progressImgChef.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        viewHolderEvento.progressImgChef.setVisibility(View.GONE);
                    }
                });
            }else{
                viewHolderEvento.imgChef.setImageResource(R.drawable.userpic);
                viewHolderEvento.progressImgChef.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        List<EventoHome> eventos = home.getEventosHome();
        if(eventos != null){
            return eventos.size() + 1;
        }

        return 1;
    }
}