package com.android.utils.lib.network;

import com.android.utils.lib.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;


/**
 * Simples classe abstrata para definir os m�todos de Http e uma 
 * factory para criar a implementa��o correta
 * 
 * @author ricardo
 * 
 */
public abstract class Http {
	public static boolean LOG_ON = true;
	public static final String TAG = "Http";

	//utiliza UrlConnection
	public static final int NORMAL 	= 1;
	//Utiliza o Jakarta HttpClient
	public static final int JAKARTA = 2;
	public static Http getInstance(int tipo){
		switch (tipo) {
			case NORMAL:
				//UrlConnection
				return new HttpConnectionImpl();
			case JAKARTA:
				//Jakarta Commons HttpClient
				return new HttpClientImpl();
		default:
			return new HttpConnectionImpl();
		}
	}

	public abstract String doGet(String url) throws IOException;
	
	public abstract String doGet(String url, String charset) throws IOException;

	public abstract String doGet(String url, Map<String,String> params) throws IOException;
	
	public abstract String doGet(String url, Map<String,String> params,String charset) throws IOException;
	
	public abstract byte[] downloadImagem(String url) throws IOException;

	public abstract String doPost(String url, Map<String,String> params) throws IOException;
	
	public abstract String doPost(String url, Map<String,String> params, String charsetEnvio, String charsetLeitura) throws IOException;
	public abstract String doPost(String url, byte[] bytes, String charset) throws IOException;

	/**
	 * Retorna a QueryString para 'GET' 
	 * 
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String getQueryString(Map<String,String> params, String charsetToEncode) throws IOException {
		if (params == null || params.size() == 0) {
			return null;
		}
		String urlParams = null;
		for (String chave : params.keySet()) {
			Object objValor = params.get(chave);
			if (objValor != null) {
				String valor = objValor.toString();
				if(charsetToEncode != null) {
					valor = URLEncoder.encode(valor, charsetToEncode);
				}
				urlParams = urlParams == null ? "" : urlParams + "&";
				urlParams += chave + "=" + valor;
			}
		}
		return urlParams;
	
	}

	public abstract String doPost(String url, String params, String charset) throws IOException;

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				bos.write(buf, 0, len);
			}

			byte[] bytes = bos.toByteArray();
			return bytes;
		} finally {
			bos.close();
		}
	}

	// Faz a leitura do texto da InputStream retornada
	public static String toString(InputStream in) throws IOException {
		byte[] bytes = toBytes(in);
		String texto = new String(bytes);
		return texto;
	}

	public static String toString(InputStream in, String charset) throws IOException {
		byte[] bytes = toBytes(in);
		String texto = new String(bytes, charset);
		return texto;
	}

	public static String getURLWithQueryString(String url,Map<String, String> params, String charset) throws IOException {
		String queryString = Http.getQueryString(params, charset);
		if(StringUtils.isNotEmpty(queryString)){
			url += "?" + queryString;
		}
		return url;
	}

}