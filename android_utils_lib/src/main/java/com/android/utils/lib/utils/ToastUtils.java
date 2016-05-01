package com.android.utils.lib.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ToastUtils {

	public static void toastLong(Context context, String text) {
		Toast t = Toast.makeText(context, text, Toast.LENGTH_LONG);
		t.show();
	}
	
	public static void toast(Context context, String text) {
		Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		t.show();
	}
	
	public static void toast(Context context, View view) {
		Toast t = new Toast(context);
		t.setView(view);
		t.show();
	}
	
	public static void  toast(Context context, ImageView view) {
		Toast t = new Toast(context);
		t.setView(view);
		t.show();
	}
}
