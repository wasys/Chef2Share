package com.android.utils.lib.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;

/**
 * http://developerlife.com/tutorials/?p=343
 * 
 * http://developer.android.com/reference/android/widget/ViewFlipper.html
 * 
 * @author ricardo
 *
 */
public class AnimUtils {
	/**
	 * Anim para aparecer
	 * 
	 * @param viewgroup
	 * @param time
	 */
	public static void alphaFadeIn(View view, long time) {
		AlphaAnimation a = new AlphaAnimation(0F, 1F);
		a.setDuration(time);
		view.startAnimation(a);
	}
	
	/**
	 * Anim para desaparece
	 * 
	 * @param viewgroup
	 * @param time
	 */
	public static void alphaFadeOut(View view, long time) {
		AlphaAnimation a = new AlphaAnimation(1F, 0F);
		a.setDuration(time);
		view.startAnimation(a);
	}

	public static void animateScale(ViewGroup viewgroup, long time) {
		AnimationSet animationset = new AnimationSet(true);
		ScaleAnimation scaleanimation = new ScaleAnimation(0F, 1F, 0F, 1F);
		scaleanimation.setDuration(time);
		animationset.addAnimation(scaleanimation);
		LayoutAnimationController layoutanimationcontroller = new LayoutAnimationController(
				animationset, 0.25F);
		viewgroup.setLayoutAnimation(layoutanimationcontroller);
	}

	public static void animateView(Context context, View view, int resAnimId, long duration) {
		Animation a = AnimationUtils.loadAnimation(context, resAnimId);
		a.setDuration(duration);
		view.startAnimation(a);
	}
}
