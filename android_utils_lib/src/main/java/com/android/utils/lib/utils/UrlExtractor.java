package com.android.utils.lib.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Recebe uma URL String, e retorna a URL + um HashMap com todos os seus parametros
 * 
 * Util se precisar usar uma URL pronta, e alterar algum parametro
 * 
 * @author ricardo
 *
 */
public class UrlExtractor {

	private String url;
	private String urlOriginal;
	private Map<String,String> params = new LinkedHashMap<String, String>();

	public UrlExtractor(String url) {
		this.urlOriginal = url;
		build(url);
	}

	private void build(String url) {
		StringTokenizer st = new StringTokenizer(url);
		String surl = st.nextToken("?");
		this.url = surl;
		String s = null;
		try {
			// Para cada param
			while((s=st.nextToken("&"))!= null) {
				if(s.contains("=")) {
					if(s.startsWith("?")) {
						// Remove o '?' da URL
						s = s.substring(1); 
					}
					// Params
					String[] array = s.split("=");
					String param = array[0];
					String value = array[1];
					params.put(param, value);
				}
			}
		} catch (NoSuchElementException e) {
			// Fim
		}
	}
	
	public String getUrl() {
		return url;
	}
	
	public Map<String, String> getParams() {
		return params;
	}

	public String getUrlOriginal() {
		return urlOriginal;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
