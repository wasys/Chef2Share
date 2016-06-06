package br.com.chef2share.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;

@EActivity(R.layout.activity_funcionalidades)
public class SolicitarEventoActivity extends SuperActivity  {


    @ViewById public TextView txtTitulo;
    @ViewById public WebView webView;


    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        setTextString(txtTitulo, getResources().getString(R.string.titulo_solicitar_evento));

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(superService.getURLSolicitarEvento(getBaseContext()));
    }

    @Override
    public void onClickVoltar(View v) {
        finish();
    }
}
