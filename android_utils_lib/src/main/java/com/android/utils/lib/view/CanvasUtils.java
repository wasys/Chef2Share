package com.android.utils.lib.view;

import android.graphics.Paint;

public class CanvasUtils {

	public static int getTextWidth(String s, Paint paint) {
		int scale = 1;
		int height = Math.round(paint.measureText(s) * scale);
		return height;
	}

	public static int getTextHeight(String s, Paint paint) {
		int scale = 1;
		int height = Math.round(paint.getTextSize() * scale);
		return height;
	}
}