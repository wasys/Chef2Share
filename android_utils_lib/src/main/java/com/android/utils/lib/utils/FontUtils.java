package com.android.utils.lib.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontUtils {

	/**
	 * @param text
	 * @param fontPath - "fonts/MyriadPro-Bold.otf"
	 */
	public static void setFont(TextView t, String fontPath) {
		if(t == null || StringUtils.isEmpty(fontPath)) {
			return;
		}
		AssetManager assets = t.getContext().getAssets();
		Typeface face = Typeface.createFromAsset(assets,fontPath);
		if(face != null && t != null) {
			t.setTypeface(face);
		}
	}
}
