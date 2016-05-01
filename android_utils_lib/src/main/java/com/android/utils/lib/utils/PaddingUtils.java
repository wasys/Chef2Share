package com.android.utils.lib.utils;

import android.view.View;

/**
 * @author Ricardo Lecheta
 *
 */
public class PaddingUtils {

	public static void setPadding(View view, int padding) {
		view.setPadding(padding, padding, padding, padding);
	}
	
	public static void setPadding(View view, int left, int top, int right, int bottom) {
		view.setPadding(left, top, right, bottom);
	}

	public static void setPaddingLeft(View view, int left) {
		int top = view.getPaddingTop();
		int right = view.getPaddingRight();
		int bottom = view.getPaddingBottom();
		view.setPadding(left, top, right, bottom);
	}

	public static void setPaddingRoight(View view, int right) {
		int left = view.getPaddingLeft();
		int top = view.getPaddingTop();
		int bottom = view.getPaddingBottom();
		view.setPadding(left, top, right, bottom);
	}

	public static void setPaddingTop(View view, int top) {
		int left = view.getPaddingLeft();
		int right = view.getPaddingRight();
		int bottom = view.getPaddingBottom();
		view.setPadding(left, top, right, bottom);
	}

	public static void setPaddingBottom(View view, int bottom) {
		int left = view.getPaddingLeft();
		int top = view.getPaddingTop();
		int right = view.getPaddingRight();
		view.setPadding(left, top, right, bottom);
	}
}
