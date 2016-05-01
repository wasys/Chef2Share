package br.com.chef2share.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Pedido;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.Resumo;
import br.com.chef2share.enums.StatusPedido;

@EActivity(R.layout.activity_pagamento)
public class PagamentoActivity extends SuperActivity  {


    private static final String[] DOMINIO_CALLBACK_ESPERADO = new String[]{"chef2share", "c2shmg"};
    @ViewById public TextView txtTitulo;
    @ViewById public WebView webView;
    @ViewById public ProgressBar progress;

    @Extra("resumo")
    public Resumo resumo;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        setTextString(txtTitulo, getResources().getString(R.string.titulo_pagamento));


        switch (resumo.getPedido().getStatus()){
            case PAGO:
                Bundle param = new Bundle();
                param.putSerializable("pedidoId", resumo.getPedido().getId());
                SuperUtil.show(getBaseContext(), AlertResultadoCompraActivity_.class, param);
                finish();
                break;

            case DISPONIVEL:

                break;

            case RESERVADO:

                break;

            case REALIZADO:

                break;

            case AGUARDANDO_PAGAMENTO:

                break;

        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(resumo.getUrl());

    }

    private WebViewClient webViewClient = new WebViewClient() {

        boolean loadingFinished = true;
        boolean redirect = false;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {

            SuperUtil.log(activity, "shouldOverrideUrlLoading: " + urlNewString);

            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;

            for (String url : DOMINIO_CALLBACK_ESPERADO) {
                /**
                 * A página de callback deve ser alguma página do chef2share
                 */
                if (StringUtils.contains(urlNewString, url)) {
                    Bundle param = new Bundle();
                    param.putSerializable("pedidoId", resumo.getPedido().getId());
                    SuperUtil.showTop(activity, AlertResultadoCompraActivity_.class, param);
                    finish();
                    return false;
                }
            }

            webView.loadUrl(urlNewString);



            return true;
        }

        public void onPageStarted(WebView view, String url) {
            loadingFinished = false;
            SuperUtil.log(activity, "onPageStarted: " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            SuperUtil.log(activity, "onPageFinished: " + url);

            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {

                if(progress != null) {
                    progress.setVisibility(View.GONE);
                }

            } else {
                redirect = false;
            }

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if(progress != null) {
                progress.setVisibility(View.GONE);
            }
            SuperUtil.alertDialog(activity, "Ocorreu um erro ao carregar a página de pagamento!", new Runnable() {
                @Override
                public void run() {
                    SuperUtil.showTop(activity, EventosQueVouActivity_.class);
                    finish();
                }
            });
        }
    };


    @Override
    public void onClickVoltar(View v) {
        SuperUtil.showTop(activity, EventosQueVouActivity_.class);
        finish();
    }
}
