package com.lxh.userlibrary.widget.animation.BounceEnter;

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
public class BounceLeftEnter extends BaseAnimatorSet {
	public BounceLeftEnter() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {

		DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
		animatorSet.playTogether(ObjectAnimator.ofFloat(view, "alpha", 0, 1, 1, 1),//
				ObjectAnimator.ofFloat(view, "translationX", -250 * dm.density, 30, -10, 0));
	}
}
