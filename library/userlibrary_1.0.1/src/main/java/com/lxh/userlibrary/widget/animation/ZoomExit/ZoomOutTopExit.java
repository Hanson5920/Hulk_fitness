package com.lxh.userlibrary.widget.animation.ZoomExit;

import android.view.View;
import android.view.View.MeasureSpec;

import com.lxh.userlibrary.widget.animation.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/10/9 09:50
 * Des：
 */
public class ZoomOutTopExit extends BaseAnimatorSet {
	public ZoomOutTopExit() {
		duration = 600;
	}

	@Override
	public void setAnimation(View view) {
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int h = view.getMeasuredHeight();

		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 1, 0),//
				ObjectAnimator.ofFloat(view, "scaleX", 1, 0.475f, 0.1f),//
				ObjectAnimator.ofFloat(view, "scaleY", 1, 0.475f, 0.1f),//
				ObjectAnimator.ofFloat(view, "translationY", 0, 60, -h));
	}
}
