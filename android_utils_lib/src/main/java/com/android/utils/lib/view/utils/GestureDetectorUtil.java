package com.android.utils.lib.view.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author Ricardo Lecheta
 *
 */
public class GestureDetectorUtil {
	public interface FlingGestureDetectorListener {
		public static final int LEFT_TO_RIGHT = 0;
		public static final int RIGHT_TO_LEFT = 1;
		void swipe(int direction);
	}

	private static final int DEFAULT_SWIPE_MIN_DISTANCE = 120;
	private static final int DEFAULT_SWIPE_MAX_OFF_PATH = 250;
	private static final int DEFAULT_SWIPE_THRESHOLD_VELOCITY = 200;

	public static void addFlingGestureDetectorListener(View view, FlingGestureDetectorListener listener) {
		addFlingGestureDetectorListener(view, DEFAULT_SWIPE_MIN_DISTANCE,DEFAULT_SWIPE_MAX_OFF_PATH,DEFAULT_SWIPE_THRESHOLD_VELOCITY,listener);
	}
	
	public static void addFlingGestureDetectorListener(View view, int swipeMinDistance, int swipeMaxOfPath,int swipeThreasholdVelocity,FlingGestureDetectorListener listener) {
		final GestureDetector gestureDetector = new GestureDetector(view.getContext(),new FlingGestureDetector(view.getContext(), swipeMaxOfPath,  swipeMinDistance,  swipeThreasholdVelocity,listener));
		OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        view.setOnTouchListener(gestureListener);
	}

	static class FlingGestureDetector extends SimpleOnGestureListener {
		private float swipeMaxOfPath = DEFAULT_SWIPE_MIN_DISTANCE;
		private float swipeMinDistance = DEFAULT_SWIPE_MAX_OFF_PATH;
		private float swipeThreasholdVelocity = DEFAULT_SWIPE_THRESHOLD_VELOCITY;
		private final FlingGestureDetectorListener listener;

		private FlingGestureDetector(Context context, int swipeMaxOfPath, int swipeMinDistance, int swipeThreasholdVelocity,final FlingGestureDetectorListener listener) {
			this.swipeMaxOfPath = swipeMaxOfPath;
			this.swipeMinDistance = swipeMinDistance;
			this.swipeThreasholdVelocity = swipeThreasholdVelocity;
			this.listener = listener;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > swipeMaxOfPath) {
					return false;
				}
				// right to left swipe
				if (e1.getX() - e2.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThreasholdVelocity) {
//					Toast.makeText(context, "Left Swipe", Toast.LENGTH_SHORT).show();
					if(listener != null) {
						listener.swipe(FlingGestureDetectorListener.RIGHT_TO_LEFT);
					}
				} else if (e2.getX() - e1.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThreasholdVelocity) {
//					Toast.makeText(context, "Right Swipe", Toast.LENGTH_SHORT).show();
					if(listener != null) {
						listener.swipe(FlingGestureDetectorListener.LEFT_TO_RIGHT);
					}
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}

}
