package br.com.chef2share.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.FileUtils;
import com.android.utils.lib.utils.IOUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.google.gson.Gson;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.domain.Imagem;
import br.com.chef2share.domain.Response;
import br.com.chef2share.infra.SuperCloudinery;

/**
 * Created by Jonas on 18/11/2015.
 */
@EActivity(R.layout.teste)
public class TesteActivity extends SuperActivity {

    @ViewById
    public WebView webView;

    boolean loadingFinished = true;
    boolean redirect = false;

    @Override
    public void init() {
        super.init();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://www.google.com.br");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {

                SuperUtil.log(activity, "shouldOverrideUrlLoading: " + urlNewString);

                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                webView.loadUrl(urlNewString);
                return true;
            }

            public void onPageStarted(WebView view, String url) {
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE


                SuperUtil.log(activity, "onPageStarted: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                SuperUtil.log(activity, "onPageFinished: " + url);

                if (!redirect) {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect) {
                    //HIDE LOADING IT HAS FINISHED
                } else {
                    redirect = false;
                }

            }
        });

    }


}
