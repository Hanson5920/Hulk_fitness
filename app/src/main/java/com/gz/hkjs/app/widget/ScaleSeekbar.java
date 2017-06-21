package com.gz.hkjs.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import java.util.ArrayList;

/**
 * Created by husong on 2017/4/19.
 */

public class ScaleSeekbar extends SeekBar {

    private ArrayList<Scale> scales = new ArrayList<>();
    private ArrayList<Rect> scalesRect = new ArrayList<>();

    private float positionPerSecond = 0;
    private long videoDuration;
    private Paint scalePaint;


    public ScaleSeekbar(Context context) {
        super(context);
        init();
    }

    public ScaleSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setVideoDuration(long duration) {
        this.videoDuration = duration;
    }

    private void init(){
        scalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scalePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scalePaint.setColor(Color.WHITE);
//        scalePaint.setStrokeWidth(bookmarkWidth);
        setPadding(0, getPaddingTop(), 0, getPaddingBottom());
    }

    public void addScale(long millisec){
        scales.add(new Scale(millisec));
        scalesRect.add(new Rect(0,0,0,0));

        invalidate();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        if(positionPerSecond == 0 && videoDuration != 0) {
            positionPerSecond = (float)getWidth() / (float)videoDuration;
        }

        for(int i = 0; i < scales.size(); i ++) {
            Rect rect = scalesRect.get(i);
            if(rect.left == 0 && rect.right == 0 && positionPerSecond != 0) {
                rect.top = getPaddingTop();
                rect.bottom = getHeight();
                rect.left = (int)(positionPerSecond * scales.get(i).getTime_milsec());
                rect.right = rect.left + 5;
            }
            canvas.drawRect(rect, scalePaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false; //禁止拖动
    }

    class Scale{
        private long time_milsec;

        public Scale(long time){
            this.time_milsec = time;
        }

        public long getTime_milsec(){
            return time_milsec;
        }
    }

}
