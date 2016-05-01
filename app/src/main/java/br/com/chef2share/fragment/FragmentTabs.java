package br.com.chef2share.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.ImageUtils;
import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.BuscaEventosActivity_;
import br.com.chef2share.activity.HomeActivity_;
import br.com.chef2share.activity.MensagemActivity_;
import br.com.chef2share.domain.Mensagem;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.domain.service.UsuarioService;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

/**
 *
 */
@EFragment(R.layout.fragment_tabs)
public class FragmentTabs extends SuperFragment {

    @ViewById public Button btnEntrar;
    @ViewById public RoundedImageView btnMeuPerfil;
    @ViewById public TextView txtIndicadorNovasMsgs;

    @Override
    public void init() {

        Usuario usuario = UsuarioService.getUsuario(getActivity());
        String urlMeuPerfil = SuperCloudinery.getUrlImgPessoa(getContext(), usuario.getImagem());
        if(StringUtils.isNotEmpty(urlMeuPerfil)) {
            Picasso.with(getContext()).load(urlMeuPerfil).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(btnMeuPerfil);

        }else{
            btnMeuPerfil.setImageResource(R.drawable.userpic);
        }

        updateQtdeNovasMensagens(SuperApplication.getSuperCache().getQtdeNovasMsgs());
    }

    @Override
    public void onResume() {
        super.onResume();

        if(txtIndicadorNovasMsgs == null){
            txtIndicadorNovasMsgs = (TextView) getActivity().findViewById(R.id.txtIndicadorNovasMsgs);
        }

        if(txtIndicadorNovasMsgs != null) {
            txtIndicadorNovasMsgs.setVisibility(View.GONE);
        }
    }

    @Click(R.id.btnHome)
    public void onClickHome(View v){
        AppUtil.showTop(getContext(), HomeActivity_.class);
    }

    @Click(R.id.btnPesquisar)
    public void onClickPesquisar(View v){
        SuperUtil.show(getActivity(), BuscaEventosActivity_.class);
    }

    @Click(R.id.btnMeuPerfil)
    public void onClickMeuPerfil(View v){

    }

    @Click(R.id.btnMensagens)
    public void onClickMensagens(View v){
        SuperUtil.show(getContext(), MensagemActivity_.class);
    }

    public void updateQtdeNovasMensagens(int qtdeNovasMsgs) {
        if(qtdeNovasMsgs  > 0 && txtIndicadorNovasMsgs != null){
            txtIndicadorNovasMsgs.setText(String.valueOf(qtdeNovasMsgs));
            txtIndicadorNovasMsgs.setVisibility(View.VISIBLE);
            SuperApplication.getSuperCache().setQtdeNovasMsgs(qtdeNovasMsgs);
        }
    }
}
