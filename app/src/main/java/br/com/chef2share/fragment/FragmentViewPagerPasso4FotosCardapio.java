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
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.listener.OnClickFotoCardapio;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;

/**
 *
 */
public class FragmentViewPagerPasso4FotosCardapio extends SuperFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_pager_item_cria_evento_fotos_cardapio, container, false);

        ImageView img = (ImageView) v.findViewById(R.id.img);
        ImageView imgDelete = (ImageView) v.findViewById(R.id.imgDelete);
        img.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getContext());

        LinearLayout layoutActions = (LinearLayout) v.findViewById(R.id.layoutActions);
        layoutActions.setVisibility(View.GONE);
        imgDelete.setVisibility(View.GONE);
        final ProgressBar progress = (ProgressBar) v.findViewById(R.id.progress);

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


        return v;

    }


    public static final FragmentViewPagerPasso4FotosCardapio newInstance(ImagemPasso imagemPasso) {
        FragmentViewPagerPasso4FotosCardapio f = new FragmentViewPagerPasso4FotosCardapio();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("imagemPasso", imagemPasso);
        f.setArguments(bdl);
        return f;

    }
}
