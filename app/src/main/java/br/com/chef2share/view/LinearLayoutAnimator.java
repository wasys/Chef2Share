package br.com.chef2share.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.LinearLayout;

/**
 * Created by Jonas on 19/11/2015.
 */
public class LinearLayoutAnimator extends LinearLayout{
    private Animation inAnimation;
    private Animation outAnimation;


    public LinearLayoutAnimator(Context context) {
        super(context, null);
    }

    public LinearLayoutAnimator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public void setInAnimation(Animation inAnimation) {
        this.inAnimation = inAnimation;
    }

    public void setOutAnimation(Animation outAnimation) {
        this.outAnimation = outAnimation;
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            if (visibility == VISIBLE) {
                if (inAnimation != null) startAnimation(inAnimation);
            } else if ((visibility == INVISIBLE) || (visibility == GONE)) {
                if (outAnimation != null) startAnimation(outAnimation);
            }
        }

        super.setVisibility(visibility);
    }
}
