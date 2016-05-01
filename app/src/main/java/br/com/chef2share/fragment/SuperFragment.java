package br.com.chef2share.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.utils.lib.exception.DomainException;
import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.StringUtils;
import com.android.volley.VolleyError;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.SuperActivity;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;

/**
 *
 */
@EFragment
public class SuperFragment extends Fragment implements DialogInterface.OnCancelListener, com.android.volley.Response.Listener<JSONObject>, com.android.volley.Response.ErrorListener {

    private boolean running;
    private ProgressDialog progress;
    private Transacao transacao;
    private View aguarde;
    private View conteudo;

    @Bean
    public SuperService superService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public static SuperFragment newInstance(int someInt) {
        SuperFragment myFragment = new SuperFragment();
        Bundle args = new Bundle();
        args.putInt("fragmentId", someInt);
        myFragment.setArguments(args);
        return myFragment;
    }

    @AfterViews
    public void init() {
    }

    protected SuperActivity getSuperActivity() {
        return (SuperActivity) getActivity();
    }
    protected SuperFragment getSuperFragment() {
        return this;
    }

    @Background
    public void doInBackground(Transacao transacao){
        doInBackground(transacao, true, R.string.infra_msg_aguarde, true);
    }

    @Background
    public void doInBackground(Transacao transacao, View aguarde, View conteudo){
        doInBackground(transacao, true, R.string.infra_msg_aguarde, false, aguarde, conteudo);
    }

    @Background
    public void doInBackground(Transacao transacao, boolean showAguarde){
        doInBackground(transacao, showAguarde, R.string.infra_msg_aguarde, false);
    }

    @Background
    public void doInBackground(Transacao transacao, boolean showAguarde, int msg, boolean permiteCancelar){
        doInBackground(transacao, showAguarde, msg, permiteCancelar, null, null);
    }

    @Background
    public void doInBackground(Transacao transacao, boolean showAguarde, int msg, boolean permiteCancelar, View aguarde, View conteudo){

        hideKeyboard();

        if(!AndroidUtils.isNetworkAvailable(getContext())){
            showError(R.string.infra_msg_network_indisponivel);
            return;
        }

        try {

            this.transacao = transacao;
            this.aguarde = aguarde;
            this.conteudo = conteudo;

            if(showAguarde) {
                showAlerta(msg, permiteCancelar, aguarde, conteudo);
            }

            running = true;
            if(running) {
                transacao.execute();
            }

        }catch(SQLException e){
            AppUtil.logError(e.getMessage(), e);
            if(running) {
                showError(R.string.infra_msg_erro_sql);
            }

        }catch(ConnectTimeoutException e){
            AppUtil.logError(e.getMessage(), e);
            if(running) {
                showError(R.string.infra_msg_erro_timeout);
            }

        }catch(IOException e){
            AppUtil.logError(e.getMessage(), e);
            if(running) {
                showError(R.string.infra_msg_erro_io);
            }

        }catch (DomainException e) {
            AppUtil.logError(getSuperActivity(), e.getMessage(), e);
            if(running) {
                showError(e.getMessage());
            }

        } catch (Throwable e) {
            AppUtil.logError(getSuperActivity(), e.getMessage(), e);
            if(running) {
                showError(R.string.infra_msg_erro);
            }
        }
    }

    @UiThread
    public void hideKeyboard() {
        if(getSuperActivity().getCurrentFocus() != null) {
            AndroidUtils.closeVirtualKeyboard(getContext(), getSuperActivity().getCurrentFocus());
        }
    }

    @UiThread
    public void showAlerta(int msg, boolean permiteCancelar, View aguarde, View conteudo) {
        progress = ProgressDialog.show(getSuperActivity(), null, getString(msg), false, permiteCancelar, this);
        getSuperActivity().setProgressBarIndeterminateVisibility(true);

        if(aguarde != null){
            aguarde.setVisibility(View.VISIBLE);
        }

        if(conteudo != null){
            conteudo.setVisibility(View.GONE);
        }
    }

    @UiThread
    public void showError(String error) {
        AppUtil.alertDialogSemIcon(getSuperActivity(), error);
    }

    @UiThread
    public void showError(int error) {
        hideProgress(aguarde, conteudo);
        AppUtil.alertDialogSemIcon(getSuperActivity(), error);
    }

    @UiThread
    public void hideProgress(View aguarde, View conteudo) {
        try {

            if(progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            if(aguarde != null){
                aguarde.setVisibility(View.GONE);
            }

            if(conteudo != null){
                conteudo.setVisibility(View.VISIBLE);
            }

            getSuperActivity().setProgressBarIndeterminateVisibility(false);

        } catch (Exception e) {

        } finally {
            progress = null;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        running = false;
        hideProgress(aguarde, conteudo);
    }

    @Override
    public void onResponse(JSONObject json) {
        try {

            SuperUtil.log(json == null ? "Retorno null" : json.toString());

            if (json == null) {
                onError(getString(R.string.infra_msg_retorno_vazio_servidor));
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
                    onError(getString(R.string.infra_msg_retorno_vazio_servidor, String.valueOf(response.getCode())));
                }
                return;
            }

            onSuccess(response);

        }catch(Exception e){
            SuperUtil.logError(e.getMessage(), e);
            onError(getString(R.string.infra_msg_retorno_invalido_servidor));
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        SuperUtil.logError(error.getMessage(), error);
        hideProgress(aguarde, conteudo);
        transacao.onError(getString(R.string.infra_msg_retorno_invalido_servidor));
    }

    @UiThread
    public void onError(String msg) {
        transacao.onError(msg);
        hideProgress(aguarde, conteudo);
    }

    @UiThread
    public void onSuccess(Response response) {
        transacao.onSuccess(response);
        hideProgress(aguarde, conteudo);
    }
}
