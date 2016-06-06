package br.com.chef2share.infra;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.FileUtils;
import com.android.utils.lib.utils.IOUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.SuperActivity;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.service.UsuarioService;

/**
 * Created by Jonas on 20/11/2015.
 */
public class Chef2ShareJSONRequest extends JsonObjectRequest {

    private Context context;
    private Usuario usuario;

    private final int TIMEOUT = 1000 * 60;

    public Chef2ShareJSONRequest(Context context, int method, String url, JSONObject jsonRequest, com.android.volley.Response.Listener<JSONObject> responseListener, com.android.volley.Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, responseListener, errorListener);

        SuperUtil.log("####URL " + url);

        this.context = context;
        this.usuario = UsuarioService.getUsuario(context);

        setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        HashMap<String, String> headers = new HashMap<String, String>();

        if(usuario != null) {
            headers.put("userID", usuario.getId());
        }

        headers.put("deviceIMEI", AndroidUtils.getIMEI(context));
        headers.put("deviceSO", "Android");
        headers.put("deviceSOVersion", String.valueOf(AndroidUtils.getVersaoOS()));
        headers.put("deviceWidth", String.valueOf(AndroidUtils.getWidthPixels(context)));
        headers.put("deviceHeight", String.valueOf(AndroidUtils.getHeightPixels(context)));
        headers.put("deviceModel", Build.MODEL);
        headers.put("deviceAppVersion", AndroidUtils.getAppVersionName(context));
        headers.put("Content-Type", "application/json");

        if(headers != null) {
            SuperUtil.log("####HEADERS " + headers);
        }

        return headers;
    }

    @Override
    public byte[] getBody() {
        byte[] body = super.getBody();

        if(body != null) {
            String s = new String(body);
            SuperUtil.log("####REQUEST " + s);
        }

        return body;
    }

    @Override
    public String getBodyContentType() {
        return "raw";
    }
}
