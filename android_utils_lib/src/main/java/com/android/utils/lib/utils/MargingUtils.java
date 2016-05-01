package com.android.utils.lib.utils;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

/**
 * @author Ricardo Lecheta
 *
 */
public class MargingUtils {

	public static void setMargin(View view, int margin) {
		MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
		if(layoutParams != null) {
			layoutParams.setMargins(margin, margin, margin, margin);
		}
	}
	
	public static void setMargin(View view, int left, int top, int right, int bottom) {
		MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
		if(layoutParams != null) {
			layoutParams.setMargins(left, top, right, bottom);
		}
	}

	public static void setMarginLeft(View view, int left) {
		MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
		if(layoutParams != null) {
			int top = layoutParams.topMargin;
			int right = layoutParams.rightMargin;
			int bottom = layoutParams.bottomMargin;
			layoutParams.setMargins(left, top, right, bottom);
		}
	}

	public static void setMarginRoight(View view, int right) {
		MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
		if(layoutParams != null) {
			int left = layoutParams.leftMargin;
			int top = layoutParams.topMargin;
			int bottom = layoutParams.bottomMargin;
			layoutParams.setMargins(left, top, right, bottom);
		}
	}

	public static void setMarginTop(View view, int top) {
		MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
		if(layoutParams != null) {
			int left = layoutParams.leftMargin;
			int right = layoutParams.rightMargin;
			int bottom = layoutParams.bottomMargin;
			layoutParams.setMargins(left, top, right, bottom);
		}
	}

	public static void setMarginBottom(View view, int bottom) {
		MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
		if(layoutParams != null) {
			int left = layoutParams.leftMargin;
			int top = layoutParams.topMargin;
			int right = layoutParams.rightMargin;
			layoutParams.setMargins(left, top, right, bottom);
		}
	}
}