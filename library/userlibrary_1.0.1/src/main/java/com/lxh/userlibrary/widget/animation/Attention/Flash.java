package com.lxh.userlibrary.widget.animation.Attention;

import android.view.View;

import com.lxh.userlibrary.widget.animation.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/10/9 09:50
 * Des：
 */
public class Flash extends BaseAnimatorSet {
	public Flash() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0, 1, 0, 1));
	}
}
