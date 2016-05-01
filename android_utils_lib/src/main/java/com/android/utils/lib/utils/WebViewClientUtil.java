package com.android.utils.lib.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewClientUtil extends WebViewClient {
	private View progress;

	public WebViewClientUtil(View progress) {
		super();
		this.progress = progress;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		if (progress != null) {
			progress.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		progress.setVisibility(View.GONE);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		progress.setVisibility(View.GONE);
	}

}
