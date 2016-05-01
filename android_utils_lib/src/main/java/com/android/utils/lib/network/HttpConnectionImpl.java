package com.android.utils.lib.network;

import android.util.Log;

import com.android.utils.lib.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Classe que encapsula as requests HTTP utilizando a "HttpURLConnection"
 * 
 * @author ricardo
 * 
 */
public class HttpConnectionImpl extends Http {
	/**
	 * 30 segundos
	 */
	public static int TIMEOUT = 30000;
	/**
	 * UTF-8
	 * ISO-8859-1
	 */
	public static String CHARSET = "UTF-8";

	@Override
	public String doGet(String url, Map<String,String> params) throws IOException {
		return doGet(url,params,CHARSET);
	}

	@Override
	public String doGet(String url, Map<String,String> params, String charset) throws IOException {
		url = getURLWithQueryString(url, params, charset);
		String s = doGet(url,charset);
		return s;
	}
	
	@Override
	public final String doGet(String url) throws IOException {
		return doGet(url,CHARSET);
	}
	
	@Override
	public final String doGet(String url, String charset) throws IOException {
		if(LOG_ON) {
			Log.v(TAG, "----------------------------------------");
			Log.d(TAG, "Http.doGet: " + url);
		}

		// Cria a URL
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setReadTimeout(TIMEOUT);

		// Configura a requisi��o para get
		// connection.setRequestProperty("Request-Method","GET");
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setDoOutput(false);
		conn.connect();
		
		InputStream in = conn.getInputStream();

		// String arquivo = readBufferedString(sb, in);
		String s = IOUtils.toString(in,charset);

		if(LOG_ON) {
			Log.v(TAG,"[" + conn.getResponseCode() + " - "+conn.getResponseMessage()+"]");
			Log.v(TAG,"[" + s + "]");
			Log.v(TAG, "----------------------------------------");
		}
		
		try {
			conn.disconnect();
		} catch (Exception e) {
			Log.e(TAG, "HttpConnectionImpl conn.disconnect(): " + e.getMessage());
		}
		
		try {
			if(in != null) {
				in.close();
			}
		} catch (Exception e) {
			Log.e(TAG, "HttpConnectionImpl in.close(): " + e.getMessage());
		}

		return s;
	}

	@Override
	public final byte[] downloadImagem(String url) throws IOException {
		if(LOG_ON) {
			Log.d(TAG, "Http.downloadImagem: " + url);
		}

		// Cria a URL
		URL u = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setReadTimeout(TIMEOUT);

		// Configura a requisi��o para get
		conn.setRequestProperty("Request-Method", "GET");
		conn.setDoInput(true);
		conn.setDoOutput(false);

		conn.connect();

		InputStream in = conn.getInputStream();

		// String arquivo = readBufferedString(sb, in);
		byte[] bytes = IOUtils.toByteArray(in);

		if(LOG_ON) {
			Log.d(TAG, "imagem retornada com: " + bytes.length + " bytes");
		}

		conn.disconnect();

		return bytes;
	}
	
	@Override
	public String doPost(String url, Map<String,String> params) throws IOException {
		return doPost(url, params, CHARSET, CHARSET);
	}
	
	@Override
	public String doPost(String url, Map<String,String> params, String charsetEnvio, String charsetLeitura) throws IOException {
		String queryString = Http.getQueryString(params, charsetEnvio);
		String texto = doPost(url, queryString, charsetEnvio);
		return texto;
	}

	// Faz um requsi��o POST na URL informada e retorna o texto
	// Os par�metros s�o enviados ao servidor
	@Override
	public String doPost(String url, String params, String charset) throws IOException {
		if(LOG_ON){
			Log.d(TAG, "Http.doPost: " + url + "?" + params);
		}
		URL u = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setReadTimeout(TIMEOUT);
		
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);

		conn.connect();

		if(params != null) {
			OutputStream out = conn.getOutputStream();
			byte[] bytes = params.getBytes(charset);
			out.write(bytes);
			out.flush();
			out.close();
		}

		InputStream in = conn.getInputStream();

		// le o texto
		String s = IOUtils.toString(in,charset);
		
		if(LOG_ON) {
			Log.v(TAG,"[" + conn.getResponseCode() + " - "+conn.getResponseMessage()+"]");
			Log.v(TAG,"[" + s + "]");
			Log.v(TAG, "----------------------------------------");
		}

		try {
			conn.disconnect();
		} catch (Exception e) {
			Log.e(TAG, "HttpConnectionImpl conn.disconnect(): " + e.getMessage());
		}

		try {
			if(in != null) {
				in.close();
			}
		} catch (Exception e) {
			Log.e(TAG, "HttpConnectionImpl in.close(): " + e.getMessage());
		}

		return s;
	}
	
	@Override
	public String doPost(String url, byte[] bytes, String charset) throws IOException {
		if(LOG_ON){
			Log.d(TAG, "Http.doPost: " + url + "?" + bytes);
		}
		URL u = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setReadTimeout(TIMEOUT);
		
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);

		conn.connect();

		if(bytes != null && bytes.length > 0) {
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
		}

		InputStream in = conn.getInputStream();

		// le o texto
		String s = IOUtils.toString(in,charset);
		
		if(LOG_ON) {
			Log.v(TAG,"[" + conn.getResponseCode() + " - "+conn.getResponseMessage()+"]");
			Log.v(TAG,"[" + s + "]");
			Log.v(TAG, "----------------------------------------");
		}

		try {
			conn.disconnect();
		} catch (Exception e) {
			Log.e(TAG, "HttpConnectionImpl conn.disconnect(): " + e.getMessage());
		}

		try {
			if(in != null) {
				in.close();
			}
		} catch (Exception e) {
			Log.e(TAG, "HttpConnectionImpl in.close(): " + e.getMessage());
		}

		return s;
	}
}