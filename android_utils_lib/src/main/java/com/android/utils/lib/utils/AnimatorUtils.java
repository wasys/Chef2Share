package com.android.utils.lib.utils;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * 
 * @author ricardo
 * 
 */
public class AnimatorUtils {
	public static ObjectAnimator fadeIn(View view, long duration) {
		ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 1);
		anim.setDuration(duration);
		anim.start();
		return anim;
	}

	public static ObjectAnimator fadeOut(View view, long duration) {
		ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0);
		anim.setDuration(duration);
		anim.start();
		return anim;
	}

	public static ObjectAnimator moveX(View view, float newX, long duration) {
		ObjectAnimator anim = ObjectAnimator.ofFloat(view, "x", newX);
		anim.setDuration(duration);
		anim.start();
		return anim;
	}
	
	public static ObjectAnimator moveY(View view, float newY, long duration) {
		ObjectAnimator anim = ObjectAnimator.ofFloat(view, "y", newY);
		anim.setDuration(duration);
		anim.start();
		return anim;
	}
	
	public static ObjectAnimator height(View view, float newH, long duration) {
		ObjectAnimator anim = ObjectAnimator.ofFloat(view, "height", newH);
		anim.setDuration(duration);
		anim.start();
		return anim;
	}
}
