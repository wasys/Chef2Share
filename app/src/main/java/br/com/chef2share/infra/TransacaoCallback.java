package br.com.chef2share.infra;

import android.content.Context;

import com.android.utils.lib.utils.StringUtils;
import com.android.volley.VolleyError;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.json.JSONObject;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.service.SuperService;

/**
 * Created by Jonas on 20/11/2015.
 */

@EBean
public class TransacaoCallback implements com.android.volley.Response.Listener<JSONObject>, com.android.volley.Response.ErrorListener{

    @RootContext
    public Context context;

    private Transacao transacao;

    public void setTransacao(Transacao transacao){
        this.transacao = transacao;
    }


    @Override
    public void onResponse(JSONObject json) {
        try {

            SuperUtil.log(json == null ? "Retorno null" : json.toString());

            if (json == null) {
                onError(context.getString(R.string.infra_msg_retorno_vazio_servidor));
                return;
            }


            Response response = SuperGson.fromJson(json.toString(), Response.class);
            if(response.getStatus() == Response.Status.FAILURE){
                /**
                 * se o servidor mandou mensagem mostra pro usuario
                 */
                if(StringUtils.isNotEmpty(response.getMensagemCompleta())){
                    onError(response.getMensagemCompleta());
                }else {
                    onError(context.getString(R.string.infra_msg_retorno_vazio_servidor, String.valueOf(response.getCode())));
                }
                return;
            }

            onSuccess(response);

        }catch(Exception e){
            SuperUtil.logError(e.getMessage(), e);
            onError(context.getString(R.string.infra_msg_retorno_invalido_servidor));
        }
    }

    @UiThread
    public void onError(String msg) {
        transacao.onError(msg);
    }

    @UiThread
    public void onSuccess(Response response) {
        transacao.onSuccess(response);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        SuperUtil.logError(error.getMessage(), error);
        transacao.onError(context.getString(R.string.infra_msg_retorno_invalido_servidor));
    }
}
