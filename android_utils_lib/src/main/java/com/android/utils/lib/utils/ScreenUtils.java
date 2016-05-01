package com.android.utils.lib.utils;

import android.content.Context;

public class ScreenUtils {

	public static String getSize(Context context) {
		String s = AndroidUtils.getWidthPixels(context)+"/"+AndroidUtils.getHeightPixels(context);
		return s;
	}
}
