package br.com.chef2share.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.StringUtils;
import com.facebook.login.LoginManager;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.MenuPerfil;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.service.UsuarioService;
import br.com.chef2share.fragment.FragmentListaPassos;
import br.com.chef2share.fragment.FragmentListaPassos_;
import br.com.chef2share.infra.FragmentUtil;

@EActivity
public class SuperActivityMainMenu extends SuperActivity {

    @ViewById public DrawerLayout navDrawer;
    @ViewById public LinearLayout layoutDrawer;


    @Click(R.id.btnMeuPerfil)
    public void onClickMeuPerfil(View v){
        navDrawer.openDrawer(layoutDrawer);
    }

    @Override
    public void onClickVoltar(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
