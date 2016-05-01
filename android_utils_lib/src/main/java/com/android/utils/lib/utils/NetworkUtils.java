package com.android.utils.lib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Classe utilit�ria para recuperar informa��es referente a rede em que o device
 * esta
 * 
 * @author Jonas Baggio
 * @create 28/12/2011
 */
public class NetworkUtils {

	private final static String TAG = "network_utils";

	/**
	 * Retorna o endere�o de IP em que o dispositivo esta, independente do tipo
	 * de conex�o(wifi, 3g) <uses-permission
	 * android:name="android.permission.INTERNET" />
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getIPAddress() {
		try {
			for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = (NetworkInterface) en.nextElement();
				for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String ipaddress = inetAddress.getHostAddress().toString();
						Log.i(TAG, "IP: " + ipaddress);
						return ipaddress;
					}
				}
			}
		} catch (SocketException ex) {
			Log.i(TAG, "Erro ao obter endere�o IP. " + ex.toString());
		}
		return null;
	}

	public static String getMacAdress(Context context) {
		if (isNetworkAvailable(context)) {
			WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInf = wifiMan.getConnectionInfo();
			return wifiInf.getMacAddress();
		}
		return null;
	}

	public static boolean isNetworkAvailable(Context context) {
		// Context context = getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
