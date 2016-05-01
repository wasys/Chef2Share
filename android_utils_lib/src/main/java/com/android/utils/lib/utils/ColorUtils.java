package com.android.utils.lib.utils;

import android.content.Context;
import android.graphics.Color;

/**
 * Utils para Cores
 * 
 * @author ricardo
 *
 */
public class ColorUtils {

	public static int getColor(Context context, int resColorId) {
		// R.color.azul
		int color = context.getResources().getColor(resColorId);
		return color;
	}
	
	public static int getColorHexa(String hexa) {
		//Color.parseColor("#1F94D2");
		int color = Color.parseColor(hexa);
		return color;
	}
	
	public static int getColorRGB(int r, int g, int b) {
		int color = Color.rgb(r,g,b);
		return color;
	}

	public static int getColorRGB(float rPerc, float gPerc, float bPerc) {
//		[UIColor colorWithRed:0.72 green:0.60 blue:0.31 alpha:1]
		int color = Color.rgb((int)(rPerc*255),(int)(gPerc*255),(int)(bPerc*255));
		return color;
	}

	public static int getColorRGB(float alpha,float rPerc, float gPerc, float bPerc) {
		int color = Color.argb((int)(alpha*255),(int)(rPerc*255),(int)(gPerc*255),(int)(bPerc*255));
		return color;
	}
}
