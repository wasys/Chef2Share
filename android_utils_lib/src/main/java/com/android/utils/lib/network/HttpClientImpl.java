package com.android.utils.lib.network;

import android.content.Context;
import android.util.Log;

import com.android.utils.lib.utils.IOUtils;
import com.android.utils.lib.utils.StringUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exemplo do HttpClient
 * 
 * Exemplos HttpClient: http://hc.apache.org/httpcomponents-client/examples.html
 * 
 * http://svn.apache.org/repos/asf/httpcomponents/httpclient/trunk/module-client
 * /src/examples/org/apache/http/examples/client/ClientConnectionRelease.java
 * 
 * @author ricardo
 * 
 */
public class HttpClientImpl extends Http {
	/**
	 * 30 segundos
	 */
	public static int TIMEOUT_CONNECTION = 10000;
	public static int TIMEOUT_WAITING_DATA = 50000;

	public static String CHARSET = HTTP.UTF_8;
	private String userAgent;
	public static boolean PRINT_HEADERS = false;
	private boolean SAVE_COOKIE_SESSION = false;
	private Context context;
	
	@Override
	public String doGet(String url) throws IOException {
		String s = doGet(url,null, CHARSET);
		return s;
	}
	
	@Override
	public String doGet(String url, String charset) throws IOException {
		String s = doGet(url,null, charset);
		return s;
	}

	@Override
	public final String doGet(String url, Map<String,String> params) throws IOException {
		return doGet(url,params,CHARSET);
	}
	
	@Override
	public final String doGet(String url, Map<String,String> params, String charset) throws IOException {

		try {
			HttpClient httpclient = getHttpClient();
			HttpGet httpget = new HttpGet(url);
			
			String queryString = Http.getQueryString(params, charset);
			if(StringUtils.isNotEmpty(queryString)){
				url += "?" + queryString;
			}
			
			if(LOG_ON) {
				Log.v(TAG, "----------------------------------------");
				Log.d(TAG, "doGet: " + url);
			}

			// Timeout
			httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, TIMEOUT_CONNECTION);
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, TIMEOUT_WAITING_DATA);

			SyncBasicHttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
			if(context != null && SAVE_COOKIE_SESSION) {
				PersistentCookieStore cookieStore = new PersistentCookieStore(context);
				httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			}

			HttpResponse response = httpclient.execute(httpget, httpContext);

			if(LOG_ON){
				Log.d(TAG, String.valueOf(response.getStatusLine()));
			}

			HttpEntity entity = response.getEntity();

			if (entity != null) {

				printHeaders(response);

				String texto = EntityUtils.toString(entity, charset);
				
//				InputStream in = entity.getContent();
//				String texto = toString(in,charset);

				if(LOG_ON) {
					Log.v(TAG,"[" + texto + "]");
					Log.v(TAG, "----------------------------------------");
				}
				return texto;
			}

			return null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw e;
		}
	}

	protected DefaultHttpClient getHttpClient() {
		return new DefaultHttpClient();
	}

	@Override
	public final byte[] downloadImagem(String url) throws IOException {

		try {
			HttpClient httpclient = getHttpClient();
			HttpGet httpget = new HttpGet(url);

			if(LOG_ON){
				Log.d(TAG, "URL: " + httpget.getURI());
			}

			// Timeout
			httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, TIMEOUT_CONNECTION);
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, TIMEOUT_WAITING_DATA);

			HttpResponse response = httpclient.execute(httpget);

			if(LOG_ON){
				Log.v(TAG, "----------------------------------------");
				Log.v(TAG, String.valueOf(response.getStatusLine()));
				Log.v(TAG, "----------------------------------------");
			}

			HttpEntity entity = response.getEntity();

			printHeaders(response);
			
			if (entity != null) {
				byte[] bytes = EntityUtils.toByteArray(entity);
//				InputStream in = entity.getContent();
//				byte[] bytes = toBytes(in);
				return bytes;
			}

			return null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw e;
		}
	}

	private void printHeaders(HttpResponse response) {
		if(PRINT_HEADERS) {
			Log.i("http","--- printKeys ---");
			Header[] headers = response.getAllHeaders();
			if(headers != null) {
				for (int i = 0; i < headers.length; i++) {
					Header h = headers[i];
					Log.i(TAG,"("+h.getName() + "): " + h.getValue());
				}
			}
			Log.i("http","--- printKeys ---");
		}
	}

	@Override
	public final String doPost(String url, Map<String,String> map) throws IOException {
		return doPost(url, map, CHARSET, CHARSET);
	}

	@Override
	public final String doPost(String url, Map<String,String> map, String charsetEnvio, String charsetLeitura) throws IOException {

		try {
			HttpClient httpclient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);

			if(LOG_ON) {
				Log.v(TAG, "----------------------------------------");
				Log.d(TAG, "doPost: " + url);
				Log.d(TAG, "\tparams: " + map);
			}

			if(map != null && map.size() > 0) {
				// cria os parâmetros
				List<NameValuePair> params = getParams(map);
				// Seta os parametros para enviar
				httpPost.setEntity(new UrlEncodedFormEntity(params, charsetEnvio));
			}

			if(StringUtils.isNotEmpty(userAgent)) {
				HttpProtocolParams.setUserAgent(httpclient.getParams(), userAgent);
			}

			// Timeout
			httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, TIMEOUT_CONNECTION);
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, TIMEOUT_WAITING_DATA);

			SyncBasicHttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
			if(context != null && SAVE_COOKIE_SESSION) {
				PersistentCookieStore cookieStore = new PersistentCookieStore(context);
				httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			}

			HttpResponse response = httpclient.execute(httpPost, httpContext);

			if(LOG_ON) {
				Log.d(TAG, String.valueOf(response.getStatusLine()));
			}

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				printHeaders(response);

				InputStream in = entity.getContent();
				String texto = toString(in, charsetLeitura);
				if(LOG_ON){
					Log.v(TAG, "[" + texto + "]");
					Log.v(TAG, "----------------------------------------");
				}
				return texto;
			}

			return null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw e;
		}
	}

	public final String doPost(String url, HttpEntity entity, String charset) throws IOException {

		try {
			HttpClient httpclient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);

			if(LOG_ON) {
				Log.v(TAG, "----------------------------------------");
				Log.d(TAG, "doPost: " + url);
			}

			if(entity != null) {
				// cria os par�metros
				httpPost.setEntity(entity);
			}

			if(StringUtils.isNotEmpty(userAgent)) {
				HttpProtocolParams.setUserAgent(httpclient.getParams(), userAgent);
			}

			// Timeout
			httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, TIMEOUT_CONNECTION);
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, TIMEOUT_WAITING_DATA);

			SyncBasicHttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
			if(context != null && SAVE_COOKIE_SESSION) {
				PersistentCookieStore cookieStore = new PersistentCookieStore(context);
				httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			}

			HttpResponse response = httpclient.execute(httpPost, httpContext);

			if(LOG_ON) {
				Log.d(TAG, String.valueOf(response.getStatusLine()));
			}

			HttpEntity entityResponse = response.getEntity();

			if (entityResponse != null) {
				printHeaders(response);

				InputStream in = entityResponse.getContent();
				String texto = toString(in,charset);
				if(LOG_ON){
					Log.v(TAG, "[" + texto + "]");
					Log.v(TAG, "----------------------------------------");
				}
				return texto;
			}

			return null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw e;
		}
	}
	
	@Override
	public final String doPost(String url, byte[] bytes, String charset) throws IOException {

		try {
			HttpClient httpclient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);

			if(LOG_ON) {
				Log.v(TAG, "----------------------------------------");
				Log.d(TAG, "doPost: " + url);
				Log.d(TAG, "\tparams: " + bytes);
			}

			if(bytes != null && bytes.length > 0) {
				// Seta os parametros para enviar
				httpPost.setEntity(new ByteArrayEntity(bytes));
			}

			if(StringUtils.isNotEmpty(userAgent)) {
				HttpProtocolParams.setUserAgent(httpclient.getParams(), userAgent);
			}

			// Timeout
			httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, TIMEOUT_CONNECTION);
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, TIMEOUT_WAITING_DATA);

			SyncBasicHttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
			if(context != null && SAVE_COOKIE_SESSION) {
				PersistentCookieStore cookieStore = new PersistentCookieStore(context);
				httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			}

			HttpResponse response = httpclient.execute(httpPost, httpContext);

			if(LOG_ON) {
				Log.d(TAG, String.valueOf(response.getStatusLine()));
			}

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				printHeaders(response);

				InputStream in = entity.getContent();
				String texto = toString(in,charset);
				if(LOG_ON){
					Log.v(TAG, "[" + texto + "]");
					Log.v(TAG, "----------------------------------------");
				}
				return texto;
			}

			return null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw e;
		}
	}

	private List<NameValuePair> getParams(Map<String,String> params) throws IOException {
		if (params == null || params.size() == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		
		List<NameValuePair> httpParams = new ArrayList<NameValuePair>();

		for (String name : params.keySet()) {
			Object value = params.get(name);
			httpParams.add(new BasicNameValuePair(name, String.valueOf(value)));
			
			sb.append(name).append("=").append(value).append("&");
		}

		if(LOG_ON){
			Log.v(TAG, "=================================");
			Log.v(TAG, ">>>> Params: " + sb.toString());
			Log.v(TAG, "=================================");
		}
		
		return httpParams;
	}

	@Override
	public String doPost(String url, String params, String charset) throws IOException {
		throw new IOException("Metodo nao implementado");
	}
	
	public String doPost(String url, String nomeParam, String request, String charset) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put(nomeParam, request);
		return doPost(url, params, charset, charset);
	}
	
	public String doPost(String url, String nomeParam, String request, String charsetEnvio, String charsetLeitura) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put(nomeParam, request);
		return doPost(url, params, charsetEnvio, charsetLeitura);
	}
	
	

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * Salva a sessao HTTP
	 * 
	 * @param context
	 * @param flag
	 */
	public void setSaveCookieSession(Context context, boolean flag) {
		this.context = context;
		this.SAVE_COOKIE_SESSION = flag;
	}
	
	public void close() {
		
	}

	/**
	 * Executa uma chamada GET e obtem como resposta um conteudo gzip
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public final byte[] doGetGzip(String url, Map<String,String> map, String charsetEnvio) throws IOException {

		try {
			HttpClient httpclient = getHttpClient();

            String queryString = Http.getQueryString(map, charsetEnvio);
            if(StringUtils.isNotEmpty(queryString)){
                url += "?" + queryString;
            }

            HttpGet httpGet = new HttpGet(url);

            httpGet.setHeader("Accept-Encoding", "gzip");

			if(LOG_ON) {
				Log.v(TAG, "----------------------------------------");
				Log.d(TAG, "doGET: " + url);
				Log.d(TAG, "\tparams: " + map);
			}

			//if(map != null && map.size() > 0) {
				// cria os parametros
				//List<NameValuePair> params = getParams(map);
				// Seta os parametros para enviar
				//httpGet.setEntity(new UrlEncodedFormEntity(params, charsetEnvio));
			//}

			if(StringUtils.isNotEmpty(userAgent)) {
				HttpProtocolParams.setUserAgent(httpclient.getParams(), userAgent);
			}

			// Timeout
			httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 1000 * 60);
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 1000 * 60 * 10);

			SyncBasicHttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
			if(context != null && SAVE_COOKIE_SESSION) {
				PersistentCookieStore cookieStore = new PersistentCookieStore(context);
				httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			}

			HttpResponse response = httpclient.execute(httpGet, httpContext);

			if(LOG_ON) {
				Log.d(TAG, String.valueOf(response.getStatusLine()));
			}

			HttpEntity entity = response.getEntity();

			if (entity != null) {
                printHeaders(response);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(baos);
				//entity.writeTo(bos);

                InputStream in = entity.getContent();
                IOUtils.copy(new BufferedInputStream(in),bos);

                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(baos);
                IOUtils.closeQuietly(bos);

                byte[] buff = baos.toByteArray();
                System.out.println("TAM : " + buff.length);

				return buff;
			}

			return null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw e;
		}
	}

}
