package br.com.chef2share.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import br.com.chef2share.R;
import br.com.chef2share.domain.Imagem;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.listener.OnClickFotoLocal;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;

/**
 *
 */
public class FragmentViewPagerCriaEventoFotosLocal extends SuperFragment {

    private OnClickFotoLocal onClickFotoLocal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_pager_item_cria_evento_fotos_local, container, false);

        ImageView img = (ImageView) v.findViewById(R.id.img);
        img.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getContext());

        ImageView imgDelete = (ImageView) v.findViewById(R.id.imgDelete);
        ImageView imgAdicionarImagem = (ImageView) v.findViewById(R.id.imgAdicionarImagem);
        final ProgressBar progress = (ProgressBar) v.findViewById(R.id.progress);
        final TextView txtNome = (TextView) v.findViewById(R.id.txtNome);

        final ImagemPasso imagemPasso = (ImagemPasso) getArguments().getSerializable("imagemPasso");

        txtNome.setText(imagemPasso.getTemp());

        imgAdicionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFotoLocal.onClickAddFoto();
            }
        });

        if(imagemPasso.getImagem() != null) {

            String url = SuperCloudinery.getUrl(getContext(), imagemPasso.getImagem(), SuperUtils.getWidthImagemFundo(getContext()), SuperUtils.getHeightImagemFundo(getContext()));
            if (StringUtils.isNotEmpty(url)) {
                Picasso.with(getContext()).load(url).resize(SuperUtils.getWidthImagemFundo(getContext()), SuperUtils.getHeightImagemFundo(getContext())).centerCrop().into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progress.setVisibility(View.GONE);
                    }
                });
            } else {
                img.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.GONE);
            }
        }else{
            Picasso.with(getContext()).load(imagemPasso.getTempFile()).resize(SuperUtils.getWidthImagemFundo(getContext()), SuperUtils.getHeightImagemFundo(getContext())).centerCrop().into(img, new Callback() {
                @Override
                public void onSuccess() {
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progress.setVisibility(View.GONE);
                }
            });
        }

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFotoLocal.onClickDelete(imagemPasso);
            }
        });

        return v;

    }

    public OnClickFotoLocal getOnClickFotoLocal() {
        return onClickFotoLocal;
    }

    public void setOnClickFotoLocal(OnClickFotoLocal onClickFotoLocal) {
        this.onClickFotoLocal = onClickFotoLocal;
    }

    public static final FragmentViewPagerCriaEventoFotosLocal newInstance(ImagemPasso imagemPasso, OnClickFotoLocal onClickFotoLocal) {
        FragmentViewPagerCriaEventoFotosLocal f = new FragmentViewPagerCriaEventoFotosLocal();
        f.setOnClickFotoLocal(onClickFotoLocal);
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("imagemPasso", imagemPasso);
        f.setArguments(bdl);
        return f;

    }
}
