package br.com.chef2share.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.chef2share.R;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.infra.SuperCloudinery;

/**
 *
 */
public class FragmentViewPagerParticipanteEvento extends SuperFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_pager_item_participante_evento, container, false);

        LinearLayout layoutParticipante1 = (LinearLayout) v.findViewById(R.id.layoutParticipante1);
        ImageView imgConvidado1 = (ImageView) v.findViewById(R.id.imgConvidado1);
        final ProgressBar progressImgConvidado1 = (ProgressBar) v.findViewById(R.id.progressImgConvidado1);
        TextView txtNomeConvidado1 = (TextView) v.findViewById(R.id.txtNomeConvidado1);

        Usuario participante1= (Usuario) getArguments().getSerializable("participante1");
        setInfoParticipante(layoutParticipante1, imgConvidado1, progressImgConvidado1, txtNomeConvidado1, participante1);


        LinearLayout layoutParticipante2 = (LinearLayout) v.findViewById(R.id.layoutParticipante2);
        ImageView imgConvidado2 = (ImageView) v.findViewById(R.id.imgConvidado2);
        final ProgressBar progressImgConvidado2 = (ProgressBar) v.findViewById(R.id.progressImgConvidado2);
        TextView txtNomeConvidado2 = (TextView) v.findViewById(R.id.txtNomeConvidado2);

        Usuario participante2= (Usuario) getArguments().getSerializable("participante2");
        setInfoParticipante(layoutParticipante2, imgConvidado2, progressImgConvidado2, txtNomeConvidado2, participante2);

        return v;

    }

    private void setInfoParticipante(LinearLayout layoutParticipante, ImageView imgConvidado, final ProgressBar progressImgConvidado, TextView txtNomeConvidado, Usuario participante) {

        if(participante == null){
            layoutParticipante.setVisibility(View.GONE);
            return;
        }

        layoutParticipante.setVisibility(View.VISIBLE);

        txtNomeConvidado.setText(participante.getNome());
        String url = SuperCloudinery.getUrlImgPessoa(getContext(), participante.getImagem());
        if(StringUtils.isNotEmpty(url)) {
            Picasso.with(getContext()).load(url).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(imgConvidado, new Callback() {
                @Override
                public void onSuccess() {
                    progressImgConvidado.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressImgConvidado.setVisibility(View.GONE);
                }
            });

        }else{
            imgConvidado.setImageResource(R.drawable.userpic);
            progressImgConvidado.setVisibility(View.GONE);
        }
    }

    public static final FragmentViewPagerParticipanteEvento newInstance(Usuario participante1, Usuario participante2) {
        FragmentViewPagerParticipanteEvento f = new FragmentViewPagerParticipanteEvento();
        Bundle bdl = new Bundle(2);
        bdl.putSerializable("participante1", participante1);
        bdl.putSerializable("participante2", participante2);
        f.setArguments(bdl);
        return f;

    }
}
