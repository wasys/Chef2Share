package br.com.chef2share.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.listener.OnClickFotoCardapio;
import br.com.chef2share.domain.listener.OnClickFotoLocal;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.view.LinearLayoutAnimator;

/**
 *
 */
public class FragmentViewPagerCriaEventoFotosCardapio extends SuperFragment {

    private OnClickFotoCardapio onClickFotoCardapio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_pager_item_cria_evento_fotos_cardapio, container, false);

        ImageView img = (ImageView) v.findViewById(R.id.img);
        img.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getContext());

        ImageView imgDelete = (ImageView) v.findViewById(R.id.imgDelete);
        ImageView imgAdicionarImagem = (ImageView) v.findViewById(R.id.imgAdicionarImagem);
        CheckBox checkBoxFotoPrincipal = (CheckBox) v.findViewById(R.id.checkBoxFotoPrincipal);
        CheckBox checkBoxFotoDivulgacao = (CheckBox) v.findViewById(R.id.checkBoxFotoDivulgacao);
        final ProgressBar progress = (ProgressBar) v.findViewById(R.id.progress);
        final LinearLayout layoutActions = (LinearLayout) v.findViewById(R.id.layoutActions);

        final ImagemPasso imagemPasso = (ImagemPasso) getArguments().getSerializable("imagemPasso");

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

        imgAdicionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFotoCardapio.onClickAddFoto();
            }
        });
        checkBoxFotoDivulgacao.setChecked(imagemPasso.isDivulgacao());
        checkBoxFotoPrincipal.setChecked(imagemPasso.isPrincipal());

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFotoCardapio.onClickDelete(imagemPasso);
            }
        });

        checkBoxFotoDivulgacao.setOnCheckedChangeListener(onCheckFotoDivulgacao(imagemPasso));
        checkBoxFotoPrincipal.setOnCheckedChangeListener(onCheckFotoPrincipal(imagemPasso));

        return v;

    }

    private CompoundButton.OnCheckedChangeListener onCheckFotoDivulgacao(final ImagemPasso imagemPasso) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                onClickFotoCardapio.onClickFotoDivulgacao(imagemPasso, isChecked);
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener onCheckFotoPrincipal(final ImagemPasso imagemPasso) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onClickFotoCardapio.onClickFotoPrincipal(imagemPasso, isChecked);
            }
        };
    }

    public OnClickFotoCardapio getOnClickFotoLocal() {
        return onClickFotoCardapio;
    }

    public void setOnClickFotoCardapio(OnClickFotoCardapio onClickFotoCardapio) {
        this.onClickFotoCardapio = onClickFotoCardapio;
    }

    public static final FragmentViewPagerCriaEventoFotosCardapio newInstance(ImagemPasso imagemPasso, OnClickFotoCardapio onClickFotoCardapio) {
        FragmentViewPagerCriaEventoFotosCardapio f = new FragmentViewPagerCriaEventoFotosCardapio();
        f.setOnClickFotoCardapio(onClickFotoCardapio);
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("imagemPasso", imagemPasso);
        f.setArguments(bdl);
        return f;

    }
}
