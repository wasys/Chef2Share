package br.com.chef2share.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.utils.lib.exception.DomainException;
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
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    private static final int GOOGLE_AUTH_UTIL_RESULT = 10000;
    private ConnectionResult mConnectionResult;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(onCallBackLoginGooglePlus())
                .addOnConnectionFailedListener(onConnectionFail()).addApi(Plus.API)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    private GoogleApiClient.OnConnectionFailedListener onConnectionFail() {
        return new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                if (!connectionResult.hasResolution()) {
                    GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), EntrarActivity.this, 0).show();
                    return;
                }

                mConnectionResult = connectionResult;
                resolveSignInError();
            }
        };
    }

    private GoogleApiClient.ConnectionCallbacks onCallBackLoginGooglePlus() {
        return new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {

                    getAccessToken();


                } else {
                    resolveSignInError();
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        };
    }

    /**
     * Recupera o accessToken do login realizado no Google Plus
     */
    @Background
    public void getAccessToken() {

        String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

        try {
            URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo");
            String sAccessToken = GoogleAuthUtil.getToken(
                    this,
                    email + "",
                    "oauth2:"
                            + Plus.SCOPE_PLUS_PROFILE + " "
                            + "https://www.googleapis.com/auth/plus.login" + " "
                            + "https://www.googleapis.com/auth/plus.profile.emails.read");

            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            doInBackground(getTransacaoLoginGoogle(currentPerson, sAccessToken), true, R.string.infra_msg_aguarde, false);

        /**
         * A primeira vez vai lançar uma exceçao de permissão
         */
        } catch (UserRecoverableAuthException e) {
            e.printStackTrace();
            Intent recover = e.getIntent();
            startActivityForResult(recover, GOOGLE_AUTH_UTIL_RESULT);

        } catch (GoogleAuthException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mConnectionResult.startResolutionForResult(this, LOGIN_GOOGLE_RESULT);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, LOGIN_GOOGLE_RESULT);
        mGoogleApiClient.connect();
        resolveSignInError();
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

    @Click(R.id.txtRecuperarSenha)
    public void onClickRecuperarSenha(View v){
        SuperUtil.show(getBaseContext(), RecuperarSenhaActivity_.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case LOGIN_GOOGLE_RESULT:
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
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
            case EntrarActivity.GOOGLE_AUTH_UTIL_RESULT:
                getAccessToken();
                break;

            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);

                break;
        }
    }


    public Transacao getTransacaoLoginGoogle(final Person person, final String sAccessToken) {

        return new Transacao() {

            @Override
            public void execute() throws Exception {


                LoginGoogle loginGoogle = new LoginGoogle();
                loginGoogle.setAccessToken(sAccessToken);
                loginGoogle.setUserID(person.getId());

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

            @Override
            public void onSuccess(Response response) {
                Usuario usuario = response.getUsuario();
                usuario.setFacebookId(person.getId());
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
