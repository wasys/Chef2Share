package com.android.utils.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

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
public class SmoothVerticalScrollView extends ScrollView {
	private static final String TAG = SmoothVerticalScrollView.class.getName();

	private static final int SWIPE_MIN_DISTANCE = 5;
	private static final int SWIPE_THRESHOLD_VELOCITY = 2000;

	private GestureDetector mGestureDetector;
	private int idxCurrentPage = 0;

	private OnPageChangeListener listener;

	private int boxHeight;
	
	/**
	 * Listener para callback quando trocar a page
	 */
	public static interface OnPageChangeListener {
		void onPageChanged(int page);

		void onTouch(View v, MotionEvent event);
	}

	public SmoothVerticalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SmoothVerticalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SmoothVerticalScrollView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(final Context context) {

		// 1 por 1
		setSmoothScrollingEnabled(true);
		
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				listener.onTouch(v,event);

				// Captura o gesto
				if (mGestureDetector.onTouchEvent(event)) {
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {

					adjustScroll(context);
					return true;
				} else {
					return false;
				}
			}
		});
		mGestureDetector = new GestureDetector(new SmoothGestureDetector());
	}
	
	protected int adjustScroll(final Context context) {
		int boxHeight = getBoxHeight();
		int scrollY = getScrollY();

		idxCurrentPage = ((scrollY + (boxHeight / 2)) / boxHeight);

		if(listener != null) {
			listener.onPageChanged(idxCurrentPage);
		}

		int scrollTo = (idxCurrentPage * boxHeight);

		smoothScrollTo(0, scrollTo);
		
		return idxCurrentPage;
	}
	
	public void setPage(Context context, int page) {
		int boxHeight = getBoxHeight();
		idxCurrentPage = page;
		if(listener != null) {
			listener.onPageChanged(idxCurrentPage);
		}
		int scrollTo = (idxCurrentPage * boxHeight);
		scrollTo(0, scrollTo);
	}

	class SmoothGestureDetector extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(e1 == null || e2 == null) {
				return super.onFling(e1, e2, velocityX, velocityY); 
			}

			try {
				// Direita para Esquerda
				if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (listener != null) {
						listener.onPageChanged(idxCurrentPage);
					}
				}
				// Esquerda para Direita
				else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
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

	public void setBoxHeight(int boxHeight) {
		this.boxHeight = boxHeight;
	}

	public int getBoxHeight() {
		if(boxHeight == 0) {
			View view = getChildCount() > 0 ? getChildAt(0) : null;
			if(view != null && view instanceof ViewGroup) {
				ViewGroup group = (ViewGroup)view;
				View firstView	= group.getChildCount() > 0 ? group.getChildAt(0) : null;;
				if(firstView != null) {
					boxHeight = firstView.getHeight();
				}
			}
		}
		if(boxHeight == 0) {
			throw new RuntimeException("Please set box width");
		}
		return boxHeight;
	}
}