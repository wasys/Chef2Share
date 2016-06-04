package br.com.chef2share.activity;

import com.android.utils.lib.exception.ExceptionLogReport;
import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.StringUtils;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import br.com.chef2share.R;
import br.com.chef2share.domain.Usuario;
import br.com.chef2share.domain.service.UsuarioService;
import br.com.chef2share.infra.SuperExceptionHandler;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends SuperActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new SuperExceptionHandler(this));
//        toast("v" + AndroidUtils.getAppVersionName(getBaseContext()));
    }

    @Override
    public void init() {
        super.init();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Usuario usuario = UsuarioService.getUsuario(getBaseContext());

                if (usuario == null) {
                    AppUtil.show(SplashActivity.this, EntrarActivity_.class);

                } else {
//                    AppUtil.show(SplashActivity.this, TesteActivity_.class);
                    AppUtil.show(SplashActivity.this, HomeActivity_.class);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
