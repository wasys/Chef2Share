package br.com.chef2share.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.StringUtils;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.HomeRecyclerViewAdapter;
import br.com.chef2share.adapter.MenuPerfilAdapter;
import br.com.chef2share.domain.EventoHome;
import br.com.chef2share.domain.Mensagem;
import br.com.chef2share.domain.MenuPerfil;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.domain.service.UsuarioService;
import br.com.chef2share.fragment.FragmentTabs;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.RoundedImageView;

@EActivity(R.layout.activity_home)
public class HomeActivity extends SuperActivity {

    @ViewById public DrawerLayout navDrawer;
    @ViewById public LinearLayout layoutDrawer;
    @FragmentById public FragmentTabs fragmentHeader;
    @ViewById public RecyclerView recyclerViewHome;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @Override
    public void init() {
        super.init();

        recyclerViewHome.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerViewHome.setLayoutManager(layoutManager);
        doInBackground(getTransacaoHome(), true);
    }

    public List<MenuPerfil> getMenuList() {
        return MenuPerfil.getList();
    }


    @Click(R.id.btnMeuPerfil)
    public void onClickMeuPerfil(View v){
        navDrawer.openDrawer(layoutDrawer);
    }

    @ItemClick
    public void recyclerViewEventosItemClicked(EventoHome eventoHome){
        SuperUtil.toast(this, eventoHome.getTitulo());
    }


    public Transacao getTransacaoHome(){
        return new Transacao() {

            @Override
            public void execute() throws Exception {
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.HOME);
            }

            @Override
            public void onSuccess(Response response) {
                recyclerViewHome.setAdapter(new HomeRecyclerViewAdapter(getBaseContext(), response.getHome()));
                doInBackground(transacaoLoadMensagens(), false);
            }

            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }

        };
    }

    private Transacao transacaoLoadMensagens() {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
            }

            @Override
            public void onSuccess(Response response) {

                if(response.getResult() == null || response.getResult().getMensagens() == null || response.getResult().getMensagens().size() <= 0) {
                    return;
                }

                List<Mensagem> list = response.getResult().getMensagens();

                int qtdeNovasMsgs = 0;

                for(Mensagem msg : list){
                    if(!msg.isLida()){
                        qtdeNovasMsgs++;
                    }
                }

                fragmentHeader.updateQtdeNovasMensagens(qtdeNovasMsgs);
            }

            @Override
            public void execute() throws Exception {
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.CONSULTA_MENSAGEM, null);
            }
        };
    }
}
