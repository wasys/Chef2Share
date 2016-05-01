package br.com.chef2share.domain.service;

import android.content.Context;

import com.android.utils.lib.utils.StringUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.infra.Prefs;

/**
 * Created by Jonas on 17/11/2015.
 */
public class UsuarioService {

    private static final String USUARIO_KEY = "usuario.logado";

    public static void saveOrUpdate(Context context, Usuario usuario){
        String usuarioGson = null;
        if(usuario != null) {
            Gson gson = new Gson();
            usuarioGson = gson.toJson(usuario);
        }

        SuperUtil.log(usuarioGson);

        Prefs.setString(context, USUARIO_KEY, usuarioGson);
    }

    public static Usuario getUsuario(Context context){
        String usuarioGson = Prefs.getString(context, USUARIO_KEY);
        Usuario usuario = null;
        if(StringUtils.isNotEmpty(usuarioGson)) {
            Gson gson = new Gson();
            usuario = gson.fromJson(usuarioGson, Usuario.class);
        }
        return usuario;
    }

}
