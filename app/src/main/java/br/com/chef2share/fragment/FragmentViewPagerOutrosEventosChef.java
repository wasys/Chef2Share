package br.com.chef2share.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.DetalheEventoActivity_;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.view.RoundedImageView;

/**
 *
 */
public class FragmentViewPagerOutrosEventosChef extends SuperFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_list_item_evento, container, false);

        ImageView imgBackground = (ImageView) v.findViewById(R.id.imgBackground);
        imgBackground.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getContext());

        TextView txtTituloEvento = (TextView) v.findViewById(R.id.txtTituloEvento);
        LinearLayout layoutProgressImgBackgroundEvento = (LinearLayout) v.findViewById(R.id.layoutProgressImgBackgroundEvento);
        RoundedImageView imgChef = (RoundedImageView) v.findViewById(R.id.imgChef);
        TextView txtNomeChef = (TextView) v.findViewById(R.id.txtNomeChef);
        RatingBar ratingAvaliacaoChef = (RatingBar) v.findViewById(R.id.ratingAvaliacaoChef);
        final ProgressBar progressImgChef = (ProgressBar) v.findViewById(R.id.progressImgChef);


        Chef chef = (Chef) getArguments().getSerializable("chef");
        final Evento evento = (Evento) getArguments().getSerializable("evento");

        txtTituloEvento.setText(evento.getTitulo());

        String url = SuperCloudinery.getUrl(getContext(), evento.getImagem(), SuperUtils.getWidthImagemFundo(getContext()), SuperUtils.getHeightImagemFundo(getContext()));
        if(StringUtils.isNotEmpty(url)) {
            Picasso.with(getContext()).load(url).into(imgBackground);
        }


        txtNomeChef.setText(chef.getNome());
        ratingAvaliacaoChef.setRating((float) chef.getAvaliacaoMedia());

        String urlImgChef = SuperCloudinery.getUrlImgPessoa(getContext(), chef.getImagem());

        if(StringUtils.isNotEmpty(urlImgChef)) {
            Picasso.with(getContext()).load(urlImgChef).resize(SuperUtils.getWidthImagemFundo(getContext()), SuperUtils.getHeightImagemFundo(getContext())).centerCrop().placeholder(R.drawable.userpic).error(R.drawable.userpic).into(imgChef, new Callback() {
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

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("eventoId", evento.getId());
                SuperUtil.show(v.getContext(), DetalheEventoActivity_.class, params);
            }
        });

        return v;

    }

    public static final FragmentViewPagerOutrosEventosChef newInstance(Evento evento, Chef chef) {
        FragmentViewPagerOutrosEventosChef f = new FragmentViewPagerOutrosEventosChef();
        Bundle bdl = new Bundle(2);
        bdl.putSerializable("evento", evento);
        bdl.putSerializable("chef", chef);
        f.setArguments(bdl);
        return f;

    }
}
