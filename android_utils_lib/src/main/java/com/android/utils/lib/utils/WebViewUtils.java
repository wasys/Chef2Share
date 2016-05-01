package com.android.utils.lib.utils;

import android.app.Activity;
import android.webkit.WebView;

import com.android.utils.lib.network.Http;

import java.io.IOException;
import java.util.Map;

public class WebViewUtils {

	private static final String ISO_8859_1 = "ISO-8859-1";

	public static void loadHtml(WebView web, Activity context, int raw) throws IOException {
		loadHtml(web, context, raw, ISO_8859_1);
	}
	
	public static void loadHtml(WebView web, Activity context, int raw, String charset) throws IOException {
		String htmldata = FileUtils.getFileString(context, raw);
		web.loadDataWithBaseURL("", htmldata, "text/html",charset, "");
	}

	public static void loadHtml(WebView web, String html) {
		loadHtml(web, html, ISO_8859_1);
	}

	public static void loadHtml(WebView web,String html, String charset) {
		web.loadDataWithBaseURL("", html, "text/html",charset, "");
	}

	public static void postData(WebView web, Activity context, String html, Map<String, String> params) throws IOException {
		postData(web, context, html, ISO_8859_1, params);
	}

	public static void postData(WebView web, Activity context, String url, String charset, Map<String, String> params) throws IOException {

		String queryString = Http.getQueryString(params, charset);
		byte[] bytes = params != null ? queryString.getBytes(charset) : null;

//		bytes = EncodingUtils.getBytes(queryString, "BASE64");

		web.postUrl(url,bytes);
	}
	
	public static void execJs(WebView webview, String js) {
		webview.loadUrl("javascript:"+js+";");
	}
}
