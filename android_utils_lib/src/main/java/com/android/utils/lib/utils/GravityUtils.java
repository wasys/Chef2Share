package com.android.utils.lib.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Ricardo Lecheta
 *
 */
public class GravityUtils {

	/**
	 * @param gravity Gravity.LEFT
	 */
	public static void setGravity(View view , int gravity) {
		if(view instanceof LinearLayout) {
			((LinearLayout)view).setGravity(gravity);	
		} else if(view instanceof TextView) {
			((TextView)view).setGravity(gravity);	
		}
	}
}
