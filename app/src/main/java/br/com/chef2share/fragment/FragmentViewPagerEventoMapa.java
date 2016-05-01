package br.com.chef2share.fragment;

import android.graphics.Paint;
import android.os.Bundle;
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
public class FragmentViewPagerEventoMapa extends SuperFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_pager_item_evento_mapa, container, false);

        ImageView imgBackground = (ImageView) v.findViewById(R.id.imgBackground);
        imgBackground.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getContext()) / 2;

        TextView txtTituloEvento = (TextView) v.findViewById(R.id.txtTituloEvento);
        txtTituloEvento.setPaintFlags(txtTituloEvento.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        TextView txtDataEvento = (TextView) v.findViewById(R.id.txtDataEvento);
        TextView txtValorEvento = (TextView) v.findViewById(R.id.txtValorEvento);
        final ProgressBar progressImgBackgroundEvento = (ProgressBar) v.findViewById(R.id.progressImgBackgroundEvento);


        final Evento evento = (Evento) getArguments().getSerializable("evento");


        imgBackground.setOnClickListener(onClickDetalhesEvento(evento));
        txtTituloEvento.setOnClickListener(onClickDetalhesEvento(evento));

        txtTituloEvento.setText(evento.getPasso2().getTitulo());
        txtValorEvento.setText(evento.getPasso3().getValorFormatado(true));
        txtDataEvento.setText(evento.getPasso3().getDataPorExtenso());

        String url = SuperCloudinery.getUrl(getContext(), evento.getPasso2().getImagemPrincipal(), SuperUtils.getWidthImagemFundo(getContext()), SuperUtils.getHeightImagemFundo(getContext()) / 2);
        if(StringUtils.isNotEmpty(url)) {
            Picasso.with(getContext()).load(url).into(imgBackground, new Callback() {
                @Override
                public void onSuccess() {
                    progressImgBackgroundEvento.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressImgBackgroundEvento.setVisibility(View.GONE);
                }
            });
        }


        return v;

    }

    private View.OnClickListener onClickDetalhesEvento(final Evento evento) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("eventoId", evento.getId());
                SuperUtil.show(v.getContext(), DetalheEventoActivity_.class, params);
            }
        };
    }

    public static final FragmentViewPagerEventoMapa newInstance(Evento evento) {
        FragmentViewPagerEventoMapa f = new FragmentViewPagerEventoMapa();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("evento", evento);
        f.setArguments(bdl);
        return f;

    }
}
