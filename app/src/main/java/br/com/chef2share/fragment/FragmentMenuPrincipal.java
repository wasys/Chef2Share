package br.com.chef2share.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.StringUtils;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperCache;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.AjudaActivity_;
import br.com.chef2share.activity.BuscaEventosActivity_;
import br.com.chef2share.activity.CriarEventoActivity_;
import br.com.chef2share.activity.DadosCadastraisActivity;
import br.com.chef2share.activity.DadosCadastraisActivity_;
import br.com.chef2share.activity.EventosQueOferecoActivity_;
import br.com.chef2share.activity.EventosQueVouActivity_;
import br.com.chef2share.activity.HomeActivity_;
import br.com.chef2share.activity.MensagemActivity;
import br.com.chef2share.activity.MensagemActivity_;
import br.com.chef2share.activity.PerfilChefActivity_;
import br.com.chef2share.activity.SplashActivity_;
import br.com.chef2share.adapter.MenuPerfilAdapter;
import br.com.chef2share.domain.EventoHome;
import br.com.chef2share.domain.MenuPerfil;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.service.UsuarioService;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.view.RoundedImageView;

/**
 *
 */
@EFragment(R.layout.fragment_menu_principal)
public class FragmentMenuPrincipal extends SuperFragment {

    @ViewById public TextView tNomeUsuario;
    @ViewById public ListView listViewMenu;
    @ViewById public RoundedImageView imgMeuPerfilFoto;
    @ViewById public Button btnCriarGerenciarEvento;
    @ViewById public Button btnBuscarEComprarEvento;

    private MenuPerfilAdapter adapter;

    @AnimationRes
    public Animation fadeIn;

    @Override
    public void init() {
        super.init();

        btnBuscarEComprarEvento.setVisibility(View.GONE);
        btnCriarGerenciarEvento.setVisibility(View.VISIBLE);

        Usuario usuario = UsuarioService.getUsuario(getContext());
        String urlImgChef = SuperCloudinery.getUrlImgPessoa(getContext(), usuario.getImagem());
        if(StringUtils.isNotEmpty(urlImgChef)) {
            Picasso.with(getContext()).load(urlImgChef).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(imgMeuPerfilFoto);

        }else{
            imgMeuPerfilFoto.setImageResource(R.drawable.userpic);
        }

        switch (SuperApplication.getInstance().getSuperCache().getTipoAcesso()){
            case CHEF:
                adapter = new MenuPerfilAdapter(getContext(), MenuPerfil.getListChef());
                break;
            case USUARIO:
                adapter = new MenuPerfilAdapter(getContext(), MenuPerfil.getList());
                break;
        }

        listViewMenu.setAdapter(adapter);
        tNomeUsuario.setText(usuario.getNome());
    }

    @Click(R.id.btnBuscarEComprarEvento)
    public void onClickBuscarEComprarEvento(View v){
        SuperApplication.getInstance().getSuperCache().setTipoAcesso(SuperCache.TipoAcesso.USUARIO);
        adapter.refresh(MenuPerfil.getList());
        btnBuscarEComprarEvento.setVisibility(View.GONE);
        btnCriarGerenciarEvento.setVisibility(View.VISIBLE);
    }

    @Click(R.id.btnCriarGerenciarEvento)
    public void onClickCriarGerenciarEvento(View v){
        SuperApplication.getInstance().getSuperCache().setTipoAcesso(SuperCache.TipoAcesso.CHEF);
        adapter.refresh(MenuPerfil.getListChef());
        btnBuscarEComprarEvento.setVisibility(View.VISIBLE);
        btnCriarGerenciarEvento.setVisibility(View.GONE);
    }

    @ItemClick
    public void listViewMenuItemClicked(MenuPerfil menuPerfil){
        switch (menuPerfil.getLabel()){
            case R.string.menu_cadastro:
                AppUtil.show(getContext(), DadosCadastraisActivity_.class);
                break;

            case R.string.menu_perfil_chef:
                AppUtil.show(getContext(), PerfilChefActivity_.class);
                break;

            case R.string.menu_criar_um_evento:
                //zera o que tinha em memória do evento
                SuperApplication.getSuperCache().setEvento(null);
                SuperApplication.getSuperCache().setCadastro(null);
                AppUtil.show(getContext(), CriarEventoActivity_.class);
                break;

            case R.string.menu_eventos_que_ofereco:
                SuperUtil.show(getContext(), EventosQueOferecoActivity_.class);
                break;

            case R.string.menu_perfil_ajuda:
                AppUtil.show(getContext(), AjudaActivity_.class);
                break;

            case R.string.menu_perfil_buscar_eventos:
                SuperUtil.show(getContext(), BuscaEventosActivity_.class);
                break;

            case R.string.menu_perfil_convide_chef:
                break;

            case R.string.menu_perfil_eventos_que_vou:
                SuperUtil.show(getContext(), EventosQueVouActivity_.class);
                break;

            case R.string.menu_perfil_mensagens:
                SuperUtil.show(getContext(), MensagemActivity_.class);
                break;

            case R.string.menu_perfil_sair:

                Usuario usuario = UsuarioService.getUsuario(getContext());

                if(StringUtils.isNotEmpty(usuario.getFacebookId())){
                    //desloga do face
                    LoginManager.getInstance().logOut();


                }else if(StringUtils.isNotEmpty(usuario.getGoogleId())){
                    //nada especial

                }

                UsuarioService.saveOrUpdate(getContext(), null);
                SuperUtil.showTop(getContext(), SplashActivity_.class);
                getActivity().finish();
                break;
        }
    }
}
