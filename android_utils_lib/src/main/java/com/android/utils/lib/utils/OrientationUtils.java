package com.android.utils.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

/**
 * Utils para trabalhar com a orientation
 * 
 * http://developer.motorola.com/docstools/library/Handle_Screen_Rotation_Part_1/
 * http://developer.motorola.com/docstools/library/Handle_Screen_Rotation_Part_2/
 * 
 * @author ricardo.lecheta.ext
 *
 */
public class OrientationUtils {

	public static int getOrientation(Context context) {
		int orient = context.getResources().getConfiguration().orientation;
		return orient;
	}
	
	public static boolean isPortrait(Context context) {
		int orient = context.getResources().getConfiguration().orientation;
		boolean portrait = orient == Configuration.ORIENTATION_PORTRAIT;
		return portrait;
	}
	
	public static boolean isLandscape(Context context) {
		int orient = context.getResources().getConfiguration().orientation;
		boolean portrait = orient == Configuration.ORIENTATION_LANDSCAPE;
		return portrait;
	}

	public static void setRequestOrientationPortrait(Activity context) {
		context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public static void setRequestOrientationLandscape(Activity context) {
		context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
}
