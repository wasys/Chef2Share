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
import br.com.chef2share.infra.TelefoneMaskEditTextChangeListener;
import br.com.chef2share.infra.Transacao;

@EActivity(R.layout.activity_novo_cadastro)
public class NovoCadastroActivity extends SuperActivity  {

    @ViewById public EditText txtNome;
    @ViewById public EditText txtEmail;
    @ViewById public EditText txtSenha;
    @ViewById public EditText txtNumeroCelular;

    @Override
    public void init() {
        super.init();

        txtNumeroCelular.addTextChangedListener(new TelefoneMaskEditTextChangeListener(txtNumeroCelular));

    }

    @Click(R.id.btnCadastrar)
    public void onClickCadastrar(View v){

        String nome = getTextString(txtNome);
        String email = getTextString(txtEmail);
        String senha = getTextString(txtSenha);
        String numeroCelular = getTextString(txtNumeroCelular);

        if(StringUtils.isEmpty(nome)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_nome);
            return;
        }

        if(StringUtils.isEmpty(email)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_email);
            return;
        }

        if(StringUtils.isEmpty(senha)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_senha);
            return;
        }

        if(StringUtils.isEmpty(numeroCelular)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_numero_celular);
            return;
        }

        UsuarioCadastro usuarioCadastro = new UsuarioCadastro();
        usuarioCadastro.setEmail(email);
        usuarioCadastro.setNome(nome);
        usuarioCadastro.setSenha(senha);
        usuarioCadastro.setNumeroCelular(numeroCelular);

        doInBackground(getTransacaoNovoCadastro(usuarioCadastro), false);
    }

    @Click(R.id.txtCadastroGoogle)
    public void onClickCadastroGooglePlus(View v){
        AppUtil.toast(getBaseContext(), "Login Google+");

        Intent returnIntent = new Intent();
        returnIntent.putExtra("loginRedeSocial", R.string.cadastre_se_com_google);
        setResult(EntrarActivity.NOVO_CADASTRO_RESULT, returnIntent);
        finish();
    }

    @Click(R.id.txtCadastroFacebook)
    public void onClickCadastroFacebook(View v){
        AppUtil.toast(getBaseContext(), "Login Facebook");

        Intent returnIntent = new Intent();
        returnIntent.putExtra("loginRedeSocial", R.string.cadastre_se_com_facebook);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Click(R.id.txtLogin)
    public void onClickLogin(View v){
        finish();
    }


    public Transacao getTransacaoNovoCadastro(final UsuarioCadastro usuarioCadastro) {

        return new Transacao() {

            @Override
            public void execute() throws Exception {

                JSONObject json = SuperGson.toJSONObject(usuarioCadastro);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.USUARIO_CADASTRO, json);
            }

            @Override
            public void onSuccess(Response response) {
                UsuarioService.saveOrUpdate(getBaseContext(), response.getUsuario());
                SuperUtil.show(getBaseContext(), HomeActivity_.class);
                finish();
            }

            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }
        };
    }
}
