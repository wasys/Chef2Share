package br.com.chef2share.domain;

import java.util.ArrayList;
import java.util.List;

import br.com.chef2share.R;

/**
 * Created by Jonas on 17/11/2015.
 */
public class MenuPerfil {

    private int icone;
    private int label;

    public MenuPerfil(int icone, int label){
        this.icone = icone;
        this.label = label;
    }

    public MenuPerfil(){}

    public static List<MenuPerfil> getList(){
        List<MenuPerfil> list = new ArrayList<MenuPerfil>();
        list.add(new MenuPerfil(R.drawable.ic_perfil_icon, R.string.menu_cadastro));
        list.add(new MenuPerfil(R.drawable.ic_pesquisar_icon, R.string.menu_perfil_buscar_eventos));
        list.add(new MenuPerfil(R.drawable.ic_icon_convide_um_chef, R.string.menu_perfil_convide_chef));
        list.add(new MenuPerfil(R.drawable.ic_eventos_icon, R.string.menu_perfil_eventos_que_vou));
        list.add(new MenuPerfil(R.drawable.ic_mensagem_icon, R.string.menu_perfil_mensagens));
        list.add(new MenuPerfil(R.drawable.ic_ajuda_icon, R.string.menu_perfil_ajuda));
        list.add(new MenuPerfil(R.drawable.ic_sair_icon, R.string.menu_perfil_sair));
        return list;
    }
    public static List<MenuPerfil> getListChef(){
        List<MenuPerfil> list = new ArrayList<MenuPerfil>();
        list.add(new MenuPerfil(R.drawable.ic_icon_perfil_chef, R.string.menu_perfil_chef));
        list.add(new MenuPerfil(R.drawable.ic_icon_criar_evento, R.string.menu_criar_um_evento));
        list.add(new MenuPerfil(R.drawable.ic_icon_eventos_ofereco, R.string.menu_eventos_que_ofereco));
        list.add(new MenuPerfil(R.drawable.ic_mensagem_icon, R.string.menu_perfil_mensagens));
        list.add(new MenuPerfil(R.drawable.ic_ajuda_icon, R.string.menu_perfil_ajuda));
        list.add(new MenuPerfil(R.drawable.ic_sair_icon, R.string.menu_perfil_sair));
        return list;
    }

    public int getIcone() {
        return icone;
    }

    public void setIcone(int icone) {
        this.icone = icone;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
