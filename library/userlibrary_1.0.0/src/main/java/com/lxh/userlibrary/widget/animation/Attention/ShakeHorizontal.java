package com.lxh.userlibrary.widget.animation.Attention;

import android.view.View;
import android.view.animation.CycleInterpolator;

import com.lxh.userlibrary.widget.animation.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/10/9 09:50
 * Des：
 */
public class ShakeHorizontal extends BaseAnimatorSet {
	public ShakeHorizontal() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -10, 10);
		animator.setInterpolator(new CycleInterpolator(5));
		animatorSet.playTogether(animator);

		/**
		 * <pre>
		 *  另一种shake实现
		 * ObjectAnimator.ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
		 * </pre>
		 */
	}
}
