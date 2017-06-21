package com.lxh.userlibrary.widget;



import com.lxh.userlibrary.widget.animation.Attention.Flash;
import com.lxh.userlibrary.widget.animation.Attention.RubberBand;
import com.lxh.userlibrary.widget.animation.Attention.ShakeHorizontal;
import com.lxh.userlibrary.widget.animation.Attention.ShakeVertical;
import com.lxh.userlibrary.widget.animation.Attention.Swing;
import com.lxh.userlibrary.widget.animation.Attention.Tada;
import com.lxh.userlibrary.widget.animation.BaseAnimatorSet;
import com.lxh.userlibrary.widget.animation.BounceEnter.BounceBottomEnter;
import com.lxh.userlibrary.widget.animation.BounceEnter.BounceEnter;
import com.lxh.userlibrary.widget.animation.BounceEnter.BounceLeftEnter;
import com.lxh.userlibrary.widget.animation.BounceEnter.BounceRightEnter;
import com.lxh.userlibrary.widget.animation.BounceEnter.BounceTopEnter;
import com.lxh.userlibrary.widget.animation.FadeEnter.FadeEnter;
import com.lxh.userlibrary.widget.animation.FadeExit.FadeExit;
import com.lxh.userlibrary.widget.animation.FallEnter.FallEnter;
import com.lxh.userlibrary.widget.animation.FallEnter.FallRotateEnter;
import com.lxh.userlibrary.widget.animation.FlipEnter.FlipBottomEnter;
import com.lxh.userlibrary.widget.animation.FlipEnter.FlipHorizontalEnter;
import com.lxh.userlibrary.widget.animation.FlipEnter.FlipHorizontalSwingEnter;
import com.lxh.userlibrary.widget.animation.FlipEnter.FlipLeftEnter;
import com.lxh.userlibrary.widget.animation.FlipEnter.FlipRightEnter;
import com.lxh.userlibrary.widget.animation.FlipEnter.FlipTopEnter;
import com.lxh.userlibrary.widget.animation.FlipEnter.FlipVerticalEnter;
import com.lxh.userlibrary.widget.animation.FlipEnter.FlipVerticalSwingEnter;
import com.lxh.userlibrary.widget.animation.FlipExit.FlipHorizontalExit;
import com.lxh.userlibrary.widget.animation.FlipExit.FlipVerticalExit;
import com.lxh.userlibrary.widget.animation.Jelly;
import com.lxh.userlibrary.widget.animation.NewsPaperEnter;
import com.lxh.userlibrary.widget.animation.SlideEnter.SlideBottomEnter;
import com.lxh.userlibrary.widget.animation.SlideEnter.SlideLeftEnter;
import com.lxh.userlibrary.widget.animation.SlideEnter.SlideRightEnter;
import com.lxh.userlibrary.widget.animation.SlideEnter.SlideTopEnter;
import com.lxh.userlibrary.widget.animation.SlideExit.SlideBottomExit;
import com.lxh.userlibrary.widget.animation.SlideExit.SlideLeftExit;
import com.lxh.userlibrary.widget.animation.SlideExit.SlideRightExit;
import com.lxh.userlibrary.widget.animation.SlideExit.SlideTopExit;
import com.lxh.userlibrary.widget.animation.ZoomEnter.ZoomInBottomEnter;
import com.lxh.userlibrary.widget.animation.ZoomEnter.ZoomInEnter;
import com.lxh.userlibrary.widget.animation.ZoomEnter.ZoomInLeftEnter;
import com.lxh.userlibrary.widget.animation.ZoomEnter.ZoomInRightEnter;
import com.lxh.userlibrary.widget.animation.ZoomEnter.ZoomInTopEnter;
import com.lxh.userlibrary.widget.animation.ZoomExit.ZoomInExit;
import com.lxh.userlibrary.widget.animation.ZoomExit.ZoomOutBottomExit;
import com.lxh.userlibrary.widget.animation.ZoomExit.ZoomOutExit;
import com.lxh.userlibrary.widget.animation.ZoomExit.ZoomOutLeftExit;
import com.lxh.userlibrary.widget.animation.ZoomExit.ZoomOutRightExit;
import com.lxh.userlibrary.widget.animation.ZoomExit.ZoomOutTopExit;

import java.util.Random;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/10/9 10:07
 * Des：
 */
public class DiaogAnimChoose {

    public static BaseAnimatorSet getShowAnim() {
        final Class<?> showAnim[] = {
                BounceEnter.class,//
                BounceTopEnter.class,//
                BounceBottomEnter.class,//
                BounceLeftEnter.class,//
                BounceRightEnter.class,//
                FlipHorizontalEnter.class,//
                FlipHorizontalSwingEnter.class,//
                FlipVerticalEnter.class,//
                FlipVerticalSwingEnter.class,//
                FlipTopEnter.class,//
                FlipBottomEnter.class,//
                FlipLeftEnter.class,//
                FlipRightEnter.class,//
                FadeEnter.class, //
                FallEnter.class,//
                FallRotateEnter.class,//
                SlideTopEnter.class,//
                SlideBottomEnter.class,//
                SlideLeftEnter.class, //
                SlideRightEnter.class,//
                ZoomInEnter.class,//
                ZoomInTopEnter.class,//
                ZoomInBottomEnter.class,//
                ZoomInLeftEnter.class,//
                ZoomInRightEnter.class,//
                NewsPaperEnter.class,//
                Flash.class,//
                ShakeHorizontal.class,//
                ShakeVertical.class,//
                Jelly.class,//
                RubberBand.class,//
                Swing.class,//
                Tada.class,//
        };

        Random random = new Random();
        int position = random.nextInt(showAnim.length);
//        Log.e("--szx-getShowAnim---", position + "--"+showAnim.length);

        try {
            return (BaseAnimatorSet) showAnim[position].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return new Tada();
        }

    }


    public static BaseAnimatorSet getDissmissAnim() {
        final Class<?> dissmissAnim[] = {
            FlipHorizontalExit.class,//
            FlipVerticalExit.class,//
                FadeExit.class,//
            SlideTopExit.class,//
            SlideBottomExit.class,//
            SlideLeftExit.class, //
            SlideRightExit.class,//
            ZoomOutExit.class,//
            ZoomOutTopExit.class,//
            ZoomOutBottomExit.class,//
            ZoomOutLeftExit.class,//
            ZoomOutRightExit.class,//
            ZoomInExit.class,//
        };

        Random random = new Random();
        int position = random.nextInt(dissmissAnim.length);
//        Log.e("--szx-getDissmiss---", position + "--"+dissmissAnim.length);
        try {
            return (BaseAnimatorSet) dissmissAnim[position].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return new ZoomInExit();
        }
    }
}

