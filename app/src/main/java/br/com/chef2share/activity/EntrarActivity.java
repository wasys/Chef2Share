package br.com.chef2share.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.StringUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Arrays;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.request.Device;
import br.com.chef2share.domain.request.Login;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.request.LoginFacebook;
import br.com.chef2share.domain.request.LoginGoogle;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.domain.service.UsuarioService;
import br.com.chef2share.infra.SuperGson;
import br.com.chef2share.infra.Transacao;

@EActivity(R.layout.activity_entrar)
public class EntrarActivity extends SuperActivity {

    @ViewById public SignInButton btnLogarGooglePlus;
    @ViewById public LoginButton btnLogarFacebook;
    @ViewById public EditText txtEmail;
    @ViewById public EditText txtSenha;


    private CallbackManager callbackManager;
    //private ProfileTracker profileTracker;
    private LoginResult loginResult;

    public static final int NOVO_CADASTRO_RESULT = 8000;

    private static final int LOGIN_GOOGLE_RESULT = 9001;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(
//                    Profile oldProfile,
//                    Profile currentProfile) {
//
//            }
//        };

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void init() {
        super.init();
        btnLogarGooglePlus.setScopes(gso.getScopeArray());
    }

    @Override
    protected void onStart() {
        super.onStart();
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        profileTracker.stopTracking();
    }

    @Click(R.id.btnLogarFacebook)
    public void onClickLoginFacebook(View v){
        logInfo("Login Facebook");

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));

        btnLogarFacebook.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        doInBackground(getTransacaoLoginFacebook(loginResult));
                    }

                    @Override
                    public void onCancel() {
                        SuperUtil.toast(getBaseContext(), "onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        SuperUtil.toast(getBaseContext(), "onError");
                    }
                });

    }

    @Click(R.id.btnLogarGooglePlus)
    public void onClickLoginGoogle(View v) {
        logInfo("Login Google+");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, LOGIN_GOOGLE_RESULT);
    }

    @Click(R.id.btnEntrar)
    public void onClickEntrar(View v){

        String email = getTextString(txtEmail);
        String senha = getTextString(txtSenha);

        if(StringUtils.isEmpty(email)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_email);
            return;
        }

        if(StringUtils.isEmpty(senha)){
            SuperUtil.alertDialog(this, R.string.msg_validacao_senha);
            return;
        }

        Login login = new Login();
        login.setEmail(email);
        login.setSenha(senha);

        doInBackground(getTransacaoLogin(login), true, R.string.infra_msg_aguarde, false);
    }

    private Transacao getTransacaoLogin(final Login login) {

        return new Transacao() {

            @Override
            public void execute() throws Exception {
                JSONObject json = SuperGson.toJSONObject(login);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.LOGIN, json);
            }

            @Override
            public void onSuccess(Response response) {
                Usuario usuario = response.getUsuario();
                UsuarioService.saveOrUpdate(getBaseContext(), usuario);
                SuperUtil.show(getBaseContext(), HomeActivity_.class);
                finish();
            }

            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(activity, msgErro);
            }
        };
    }

    @Click(R.id.txtCadastreSe)
    public void onClickCadastreSe(View v){

        Intent i = new Intent(this, NovoCadastroActivity_.class);
        startActivityForResult(i, NOVO_CADASTRO_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case LOGIN_GOOGLE_RESULT:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;

            case EntrarActivity.NOVO_CADASTRO_RESULT:
                if(data != null) {
                    int loginRedeSocial = data.getIntExtra("loginRedeSocial", 0);
                    switch (loginRedeSocial) {
                        case R.string.cadastre_se_com_facebook:
                            onClickLoginFacebook(btnLogarFacebook);
                            break;

                        case R.string.cadastre_se_com_google:
                            onClickLoginGoogle(btnLogarGooglePlus);
                            break;
                    }
                }
                break;

            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);

                break;
        }
    }

    private void handleSignInResult(final GoogleSignInResult result) {
        doInBackground(getTransacaoLoginGoogle(result), true, R.string.infra_msg_aguarde, false);
    }

    public Transacao getTransacaoLoginGoogle(final GoogleSignInResult result) {

        return new Transacao() {

            @Override
            public void execute() throws Exception {
                if (result.isSuccess()) {

                    GoogleSignInAccount acct = result.getSignInAccount();

                    LoginGoogle loginGoogle = new LoginGoogle();
                    loginGoogle.setAccessToken(acct.getIdToken());
                    loginGoogle.setUserID(acct.getId());

                    Device device = new Device();
                    device.setSender(SuperService.getPushID(getBaseContext()));
                    device.setSo("ANDROID");
                    loginGoogle.setDevice(device);

                    JSONObject json = SuperGson.toJSONObject(loginGoogle);
                    superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.LOGIN_GOOGLE, json);

                    /*
                    Usuario usuario = new Usuario();
                    usuario.setEmail(acct.getEmail());
                    usuario.setGoogleId(acct.getId());
                    usuario.setFoto(acct.getPhotoUrl().toString());
                    usuario.setNome(acct.getDisplayName());
                    usuario.setAccessToken(acct.getIdToken());
                    usuario.setPushId(SuperService.getPushID(getBaseContext()));
                    */
                }
            }

            @Override
            public void onSuccess(Response response) {
                Usuario usuario = response.getUsuario();
                usuario.setFacebookId(result.getSignInAccount().getId());
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

    public Transacao getTransacaoLoginFacebook(final LoginResult loginResult) {

        return new Transacao() {

            @Override
            public void execute() throws Exception {

                LoginFacebook loginFacebook = new LoginFacebook();
                loginFacebook.setAccessToken(loginResult.getAccessToken().getToken());
                loginFacebook.setUserID(loginResult.getAccessToken().getUserId());

                Device device = new Device();
                device.setSender(SuperService.getPushID(getBaseContext()));
                device.setSo("ANDROID");
                loginFacebook.setDevice(device);

                JSONObject json = SuperGson.toJSONObject(loginFacebook);
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.LOGIN_FACEBOOK, json);
            }

            @Override
            public void onSuccess(Response response) {
                Usuario usuario = response.getUsuario();
                usuario.setFacebookId(loginResult.getAccessToken().getUserId());
                UsuarioService.saveOrUpdate(getBaseContext(), usuario);
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
