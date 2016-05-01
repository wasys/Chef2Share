package com.android.utils.lib.network;

import android.content.Context;

import com.android.utils.lib.utils.StringUtils;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.InputStream;
import java.security.KeyStore;

/**
 * 
 * @author Jonas Baggio
 * @create 26/10/2012
 */
public class HttpsClient extends DefaultHttpClient {

	final Context context;
	final int rawCertificado;
	final String senhaCertificado;

	public HttpsClient(Context context, String senhaCertificado, int rawCertificado) {
		this.context = context;
		if(StringUtils.isEmpty(senhaCertificado)){
			throw new IllegalArgumentException("Senha do certificado não informada.");
		}
		
		if(rawCertificado == 0){
			throw new IllegalArgumentException("res/raw/.bks não informada.");
		}
		this.senhaCertificado = senhaCertificado;
		this.rawCertificado = rawCertificado;
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		try {
			String defaultType = KeyStore.getDefaultType();
			KeyStore trustedStore = KeyStore.getInstance(defaultType);
			InputStream certificateStream = context.getResources().openRawResource(rawCertificado);
			trustedStore.load(certificateStream, senhaCertificado.toCharArray());
			certificateStream.close();

			SSLSocketFactory sslSocketFactory = new SSLSocketFactory(trustedStore);
			sslSocketFactory.setHostnameVerifier((X509HostnameVerifier) SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
			registry.register(new Scheme("https", sslSocketFactory, 8443));

			return new SingleClientConnManager(getParams(), registry);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
