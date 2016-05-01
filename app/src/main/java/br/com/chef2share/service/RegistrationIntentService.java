package br.com.chef2share.service;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.service.UsuarioService;

public class RegistrationIntentService extends IntentService {


    public RegistrationIntentService() {
        super(SuperUtil.TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            SuperUtil.log("GCM Registration Token: " + token);

            saveRegistrationToken(token);

        } catch (Exception e) {
            saveRegistrationToken(null);
        }
    }

    private void saveRegistrationToken(String token) {
        Usuario usuario = UsuarioService.getUsuario(getBaseContext());
        if(usuario == null){
            usuario = new Usuario();
        }
        usuario.setPushId(token);
        UsuarioService.saveOrUpdate(getBaseContext(), usuario);
    }
}
