package br.com.chef2share.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.AlertMaisInfoComentarioActivity;
import br.com.chef2share.activity.AlertMaisInfoComentarioActivity_;
import br.com.chef2share.domain.Avaliacao;
import br.com.chef2share.infra.SuperCloudinery;

/**
 *
 */
public class FragmentViewPagerComentariosDetalheEvento extends SuperFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_pager_item_comentario_evento, container, false);

        ImageView img = (ImageView) v.findViewById(R.id.imgUsuario);
        TextView txtNomeUsuario = (TextView) v.findViewById(R.id.txtNomeUsuario);
        final ProgressBar progressImgUsuario = (ProgressBar) v.findViewById(R.id.progressImgUsuario);
        TextView txtDataComentario = (TextView) v.findViewById(R.id.txtDataComentario);
        TextView txtEventoComentario = (TextView) v.findViewById(R.id.txtEventoComentario);
        TextView txtComentario = (TextView) v.findViewById(R.id.txtComentario);
        TextView txtMaisInfoComentarios = (TextView) v.findViewById(R.id.txtMaisInfoComentarios);

        final Avaliacao avaliacao = (Avaliacao) getArguments().getSerializable("avaliacao");

        txtMaisInfoComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bdl = new Bundle(1);
                bdl.putSerializable("avaliacao", avaliacao);
                SuperUtil.show(getContext(), AlertMaisInfoComentarioActivity_.class, bdl);
            }
        });

        txtNomeUsuario.setText(avaliacao.getNome());
        if(StringUtils.isNotEmpty(avaliacao.getComentario())) {
            txtComentario.setText("\"" + avaliacao.getComentario() + "\"");
        }else{
            txtComentario.setText("");
        }
        txtEventoComentario.setText(avaliacao.getTitulo());
        txtDataComentario.setText(avaliacao.getDataFormatada());


        String urlImgChef = SuperCloudinery.getUrlImgPessoa(getContext(), avaliacao.getImagem());

        if(StringUtils.isNotEmpty(urlImgChef)) {
            Picasso.with(getContext()).load(urlImgChef).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(img, new Callback() {
                @Override
                public void onSuccess() {
                    progressImgUsuario.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressImgUsuario.setVisibility(View.GONE);
                }
            });
        }else{
            img.setImageResource(R.drawable.userpic);
            progressImgUsuario.setVisibility(View.GONE);
        }

        return v;

    }

    public static final FragmentViewPagerComentariosDetalheEvento newInstance(Avaliacao avaliacao) {
        FragmentViewPagerComentariosDetalheEvento f = new FragmentViewPagerComentariosDetalheEvento();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("avaliacao", avaliacao);
        f.setArguments(bdl);
        return f;

    }
}
