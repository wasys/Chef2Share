package com.android.utils.lib.utils;

import android.content.Context;
import android.content.res.Resources;

public class DimenUtils {

	/**
	 * Retorna o tamanho em pixels de uma dimens�o, seja em "dp" ou "sp"
	 * 
	 * Definido no XML como uma dimensao em dip
	 * 
	 * <resources>
	 * 	<dimen name="ticker_size">14sp</dimen>
	 * </resources>
	 * 
	 * @param context
	 * @param resDimen
	 * @return
	 */
	public static float getDimen(Context context, int resDimen) {
		Resources res = context.getResources();
		float d = res.getDimension(resDimen);
		return d;
	}
}
