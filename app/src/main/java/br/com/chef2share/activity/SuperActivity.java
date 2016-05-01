package br.com.chef2share.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.utils.lib.exception.DomainException;
import com.android.utils.lib.exception.ExceptionLogReport;
import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.StringUtils;
import com.android.volley.VolleyError;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.SQLException;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;

@EActivity
public class SuperActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, com.android.volley.Response.Listener<JSONObject>, com.android.volley.Response.ErrorListener {

    @ViewById public ImageButton btnVoltarHeader;

    private boolean running;
    private ProgressDialog progress;
    private Transacao transacao;
    private View aguarde;
    private View conteudo;


    @Bean
    public SuperService superService;

    public final FragmentActivity activity = this;
    public final SuperActivity superActivity = this;


    @AfterViews
    public void init() {
        setVisibilityBotaoVoltar(View.GONE);
    }

    protected void setVisibilityBotaoVoltar(int estado){
        if(btnVoltarHeader != null){
            btnVoltarHeader.setVisibility(estado);
        }
    }

    protected void toast(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected void logInfo(String msg){
        AppUtil.log(this, msg);
    }

    protected void logError(Throwable e, String msg){
        AppUtil.logError(msg, e);
    }

    @Click({R.id.btnVoltarHeader, R.id.layoutVoltar})
    public void onClickVoltar(View v){
        AppUtil.toast(getBaseContext(), "Voltar");
    }

    protected String getTextString(View view){
        String text = "";
        if(view instanceof EditText){
            EditText editText = (EditText) view;
            if(editText != null && editText.getText() != null){
                text = editText.getText().toString();
            }
        }

        if(view instanceof AutoCompleteTextView){
            AutoCompleteTextView editText = (AutoCompleteTextView) view;
            if(editText != null && editText.getText() != null){
                text = editText.getText().toString();
            }
        }

        if(view instanceof TextView){
            TextView textView = (TextView) view;
            if(textView != null && textView.getText() != null){
                text = textView.getText().toString();
            }
        }

        if(view instanceof Button){
            Button button = (Button) view;
            if(button != null && button.getText() != null){
                text = button.getText().toString();
            }
        }

        return text;
    }

    protected void setTextString(View view, String valor){
        if(valor == null) {
            return;
        }
        if(view instanceof EditText){
            EditText editText = (EditText) view;
            editText.setText(valor);
            return;
        }

        if(view instanceof TextView){
            TextView textView = (TextView) view;
            textView.setText(valor);
            return;
        }

        if(view instanceof Button){
            Button button = (Button) view;
            button.setText(valor);
            return;
        }
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

        if(!AndroidUtils.isNetworkAvailable(getBaseContext())){
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
            AppUtil.logError(this, e.getMessage(), e);
            if(running) {
                showError(e.getMessage());
            }

        } catch (Throwable e) {
            AppUtil.logError(this, e.getMessage(), e);
            if(running) {
                showError(R.string.infra_msg_erro);
            }
        }
    }

    @UiThread
    public void hideKeyboard() {
        if(getCurrentFocus() != null) {
            AndroidUtils.closeVirtualKeyboard(getBaseContext(), getCurrentFocus());
        }
    }

    @UiThread
    public void showAlerta(int msg, boolean permiteCancelar, View aguarde, View conteudo) {
        progress = ProgressDialog.show(this, null, getString(msg), false, permiteCancelar, this);
        setProgressBarIndeterminateVisibility(true);

        if(aguarde != null){
            aguarde.setVisibility(View.VISIBLE);
        }

        if(conteudo != null){
            conteudo.setVisibility(View.GONE);
        }
    }

    @UiThread
    public void showError(String error) {
        AppUtil.alertDialogSemIcon(this, error);
    }

    @UiThread
    public void showError(int error) {
        hideProgress(aguarde, conteudo);
        AppUtil.alertDialogSemIcon(this, error);
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

            setProgressBarIndeterminateVisibility(false);

        } catch (Exception e) {

        } finally {
            progress = null;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        hideProgress(aguarde, conteudo);
        running = false;
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
        hideProgress(aguarde, conteudo);
        transacao.onError(msg);
    }

    @UiThread
    public void onSuccess(Response response) {
        hideProgress(aguarde, conteudo);
        transacao.onSuccess(response);
    }
}
