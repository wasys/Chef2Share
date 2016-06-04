package br.com.chef2share.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.request.UsuarioCadastro;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.domain.service.UsuarioService;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;

@EActivity(R.layout.activity_recuperar_senha)
public class RecuperarSenhaActivity extends SuperActivity  {

    @ViewById public EditText txtEmail;

    @Override
    public void init() {
        super.init();
    }

    @Click(R.id.btnEnviarSenha)
    public void onClickEnviarSenha(View v){

        String email = getTextString(txtEmail);

        if(StringUtils.isEmpty(email)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_email);
            return;
        }
        UsuarioCadastro usuarioCadastro = new UsuarioCadastro();
        usuarioCadastro.setEmail(email);

        doInBackground(getTransacaoRecuperarSenha(usuarioCadastro), false);
    }

    @Click(R.id.txtLogin)
    public void onClickLogin(View v){
        finish();
    }


    public Transacao getTransacaoRecuperarSenha(final UsuarioCadastro usuarioCadastro) {

        return new Transacao() {

            @Override
            public void execute() throws Exception {

                JSONObject json = SuperGson.toJSONObject(usuarioCadastro);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.USUARIO_RECUPERAR_SENHA, json);
            }

            @Override
            public void onSuccess(Response response) {
                AppUtil.alertDialog(RecuperarSenhaActivity.this, response.getMensagemCompleta(), new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }

            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }
        };
    }
}
