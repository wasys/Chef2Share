package com.android.utils.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.android.utils.lib.utils.AndroidUtils;

/**
 * Video que pode ser redimensionado, chamando os m�todos setVideoSize(w,h)
 * 
 * @author Ricardo Lecheta
 * 
 */
public class FullScreenVideoView extends VideoView {
	private int mVideoWidth;
	private int mVideoHeight;

	public FullScreenVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FullScreenVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FullScreenVideoView(Context context) {
		super(context);
	}

	public void setVideoSize(int width, int height) {
		this.mVideoWidth = width;
		this.mVideoHeight = height;

		// not sure whether it is useful or not but safe to do so
		getHolder().setFixedSize(width, height);

		requestLayout();
		invalidate(); // very important, so that onMeasure will be triggered
	}
	
	public void setVideoDefaultSize() {
		setVideoSize(0, 0);
	}
	
	public void setVideoFullScreenSize() {
		int w = AndroidUtils.getWidthPixels(getContext());
		int h = AndroidUtils.getHeightPixels(getContext());
		setVideoSize(w, h);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
		int height = getDefaultSize(mVideoHeight, heightMeasureSpec);

		// must set this at the end
		if(mVideoWidth == 0 && mVideoHeight == 0) {
			setMeasuredDimension(width, height);
		} else {
			setMeasuredDimension(mVideoWidth, mVideoHeight);
		}
	}
}