package com.android.utils.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;

public class WiFiUtils {

	public static void turnOn(Activity context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(true);
	}
}
