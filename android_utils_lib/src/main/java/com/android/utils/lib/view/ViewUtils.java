package com.android.utils.lib.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * @author Ricardo Lecheta
 * 
 */
public class ViewUtils {

	private static final String TAG = "ViewUtils";

	public static void unbindDrawables(View view) {
		try {
			if (view != null) {
				Drawable background = view.getBackground();
				if (background != null) {
					background.setCallback(null);
					view.setBackgroundDrawable(null);
				}
				if (view instanceof ViewGroup) {
					ViewGroup viewGroup = (ViewGroup) view;
					int childCount = viewGroup.getChildCount();
					for (int i = 0; i < childCount; i++) {
						View childAt = viewGroup.getChildAt(i);
						unbindDrawables(childAt);
					}
					boolean valid = !(viewGroup instanceof AdapterView<?>);
					if (valid) {
						viewGroup.removeAllViews();
					}
				}
			}
		} catch (UnsupportedOperationException e) {
			Log.e(TAG, "UnsupportedOperationException : unbindDrawables: " + e.getMessage());
			// alguns ViewGroups n�o suportam o metodo removeAllViews
		} catch (Exception e) {
			Log.e(TAG, "unbindDrawables: " + e.getMessage(), e);
			// alguns ViewGroups n�o suportam o metodo removeAllViews
		}
	}
	
	public static void setClickSize(View view) {
		if (view != null) {
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String message = v.getWidth() + "/" + v.getHeight();
					Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	public static int getPosition(View v) {
		ViewGroup parent = (ViewGroup) v.getParent();
		if(parent != null) {
			int childCount = parent.getChildCount();
			for (int i = 0; i < childCount; i++) {
				View v2 = parent.getChildAt(i);
				if(v2.equals(v)) {
					return i;
				}
			}
		}
		return 0;
	}

	/**
	 * Draw the view on a Bitmap
	 * 
	 * @param v
	 * @return
	 */
	public static Bitmap screenShot(View v) {
		Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		v.draw(c);
		return bitmap;
	}
	
	public static void setLayoutParamsMatchParent(View view) {
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
}