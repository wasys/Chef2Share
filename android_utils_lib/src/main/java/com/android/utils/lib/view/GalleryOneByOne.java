package com.android.utils.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Gallery para trocar de View 1-by-1
 * 
 * @author rlecheta
 * 
 */
public class GalleryOneByOne extends Gallery {

	public GalleryOneByOne(Context context) {
		super(context);
	}

	public GalleryOneByOne(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}
}