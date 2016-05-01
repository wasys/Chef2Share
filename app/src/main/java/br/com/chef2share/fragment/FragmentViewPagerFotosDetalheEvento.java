package br.com.chef2share.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.chef2share.R;
import br.com.chef2share.domain.Imagem;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;

/**
 *
 */
public class FragmentViewPagerFotosDetalheEvento extends SuperFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_pager_item_fotos_local_evento, container, false);

        ImageView img = (ImageView) v.findViewById(R.id.img);
        img.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getContext());
        final ProgressBar progress = (ProgressBar) v.findViewById(R.id.progress);



        Imagem imagem = (Imagem) getArguments().getSerializable("imagem");
        String url = SuperCloudinery.getUrl(getContext(), imagem, SuperUtils.getWidthImagemFundo(getContext()), SuperUtils.getHeightImagemFundo(getContext()));
        if(StringUtils.isNotEmpty(url)) {
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
        }else{
            img.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.GONE);
        }

        return v;

    }

    public static final FragmentViewPagerFotosDetalheEvento newInstance(Imagem imagem) {
        FragmentViewPagerFotosDetalheEvento f = new FragmentViewPagerFotosDetalheEvento();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("imagem", imagem);
        f.setArguments(bdl);
        return f;

    }
}
