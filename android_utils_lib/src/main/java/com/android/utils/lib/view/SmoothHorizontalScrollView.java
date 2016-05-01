package com.android.utils.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * HScroll com o "SmoothScrolling" ligado
 * 
 * Faz scroll 1 por 1.
 * 
 * Listener para avisar o PageView qual a view atual
 * 
 * @author rlecheta
 *
 */
public class SmoothHorizontalScrollView extends HorizontalScrollView {
	private static final String TAG = SmoothHorizontalScrollView.class.getName();

	private static final int SWIPE_MIN_DISTANCE = 5;
	private static final int SWIPE_THRESHOLD_VELOCITY = 2000;

	private GestureDetector mGestureDetector;
	private int idxCurrentPage = 0;

	private OnPageChangeListener listener;

	private int boxWidth;
	
	/**
	 * Listener para callback quando trocar a page
	 */
	public static interface OnPageChangeListener {
		void onPageChanged(int page);
	}

	public SmoothHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SmoothHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SmoothHorizontalScrollView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(final Context context) {

		// 1 por 1
		setSmoothScrollingEnabled(true);
		
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Captura o gesto
				if (mGestureDetector.onTouchEvent(event)) {
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {

					ajustaScroll(context);
					return true;
				} else {
					return false;
				}
			}
		});
		mGestureDetector = new GestureDetector(new MyGestureDetector());
	}
	
	private void ajustaScroll(final Context context) {
		if(boxWidth == 0) {
			throw new RuntimeException("Please set box width");
//			boxWidth = (int) DimenUtils.getDimen(context,box_width);
		}
		int scrollX = getScrollX();

		idxCurrentPage = ((scrollX + (boxWidth / 2)) / boxWidth);

		if(listener != null) {
			listener.onPageChanged(idxCurrentPage);
		}

		int scrollTo = (idxCurrentPage * boxWidth);
		
		smoothScrollTo(scrollTo, 0);
	}
	
	public void setPage(Context context, int page) {
		if(boxWidth == 0) {
			throw new RuntimeException("Please set box width");
		}
		idxCurrentPage = page;
		if(listener != null) {
			listener.onPageChanged(idxCurrentPage);
		}
		int scrollTo = (idxCurrentPage * boxWidth);
		scrollTo(scrollTo, 0);
	}

	class MyGestureDetector extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(e1 == null || e2 == null) {
				return super.onFling(e1, e2, velocityX, velocityY); 
			}

			try {
				// Direita para Esquerda
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (listener != null) {
						listener.onPageChanged(idxCurrentPage);
					}
				}
				// Esquerda para Direita
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (listener != null) {
						listener.onPageChanged(idxCurrentPage);
					}
				}
			} catch (Exception e) {
				Log.e(TAG,e.getMessage(), e);
			}
			return false;
		}
	}

	public void setListener(OnPageChangeListener listener) {
		this.listener = listener;
	}
	
	public void setBoxWidth(int boxWidth) {
		this.boxWidth = boxWidth;
	}
}