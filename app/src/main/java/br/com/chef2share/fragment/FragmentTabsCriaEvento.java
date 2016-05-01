package br.com.chef2share.fragment;

import android.view.View;
import android.widget.Button;

import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.BuscaEventosActivity_;
import br.com.chef2share.activity.HomeActivity_;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.service.UsuarioService;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.view.RoundedImageView;

/**
 *
 */
@EFragment(R.layout.fragment_tabs_cria_evento)
public class FragmentTabsCriaEvento extends FragmentTabs {

    @Click(R.id.btnVoltarHeader)
    public void onClickVoltar(View v){
        SuperUtil.toast(getContext(), "Voltar...");
    }
}
