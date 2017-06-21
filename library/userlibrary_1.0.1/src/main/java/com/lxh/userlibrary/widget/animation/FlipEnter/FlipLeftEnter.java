package com.lxh.userlibrary.widget.animation.FlipEnter;

import android.util.DisplayMetrics;
import android.view.View;

import com.lxh.userlibrary.widget.animation.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/10/9 09:50
 * Des：
 */
public class FlipLeftEnter extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();

		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "rotationY", 90, 0),//
				ObjectAnimator.ofFloat(view, "translationX", -200 * dm.density, 0), //
				ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1));
	}
}
