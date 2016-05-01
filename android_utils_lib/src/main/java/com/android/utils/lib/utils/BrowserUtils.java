package com.android.utils.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class BrowserUtils {

	public static void openUrl(Context context, String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}
}
