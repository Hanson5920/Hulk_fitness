package com.gz.hkjs.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gz.hkjs.app.R;

/**
 * Company: chuangxinmengxiang-chongqingwapushidai
 * User: HongBin(Hongbin1011@gmail.com)
 * Date: 2017-04-14
 * Time: 14:44
 */
public class StepViewHorizontal extends View{

    private Paint bgPaint;
    private Paint proPaint;
    private Paint focusPaint;
    private float bgRadius;
    private float proRadius;
    private float startX;
    private float stopX;
    private float bgCenterY;
    private int lineBgWidth;
    private int bgColor;
    private int lineProWidth;
    private int proColor;
    private int textPadding;
    private int timePadding;
    private int maxStep;
    private int textSize;
    private int proStep;
    private int interval;
    private String[] titles = {"基础", "进阶", "强化", "突破", "极限"};
    private OnStepClickListener mStepClickListener;
    private boolean isChecked = false;

    public StepViewHorizontal(Context context) {
        this(context, null);
    }

    public StepViewHorizontal(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepViewHorizontal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StepViewHorizontal);
        bgRadius = ta.getDimension(R.styleable.StepViewHorizontal_h_bg_radius, 10);
        proRadius = ta.getDimension(R.styleable.StepViewHorizontal_h_pro_radius, 8);
        lineBgWidth = (int) ta.getDimension(R.styleable.StepViewHorizontal_h_bg_width, 3f);
        bgColor = ta.getColor(R.styleable.StepViewHorizontal_h_bg_color, Color.parseColor("#cdcbcc"));
        lineProWidth = (int) ta.getDimension(R.styleable.StepViewHorizontal_h_pro_width, 2f);
        proColor = ta.getColor(R.styleable.StepViewHorizontal_h_pro_color, Color.parseColor("#FFFFFF"));
        textPadding = (int) ta.getDimension(R.styleable.StepViewHorizontal_h_text_padding, 50);
        timePadding = (int) ta.getDimension(R.styleable.StepViewHorizontal_h_time_padding, 30);
        maxStep = ta.getInt(R.styleable.StepViewHorizontal_h_max_step, 5);
        textSize = (int) ta.getDimension(R.styleable.StepViewHorizontal_h_textsize, 20);
        proStep = ta.getInt(R.styleable.StepViewHorizontal_h_pro_step, 1);
        ta.recycle();
        init();
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                int i = getWidth()/maxStep;
                int step = 0;
                for (int k = 0; k<maxStep;k++){
                    if (x>k*i && x <i*(k+1)){
                        step = k;
                        break;
                    }
                }
                if (mStepClickListener!=null && step!= proStep && isChecked)//progress 变化才通知重绘，避免过度绘制
                    mStepClickListener.mOnStepClickListener(v,step);
                return true;
            }
        });
    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgColor);
        bgPaint.setStrokeWidth(lineBgWidth);
        bgPaint.setTextSize(textSize);
        bgPaint.setTextAlign(Paint.Align.CENTER);

        proPaint = new Paint();
        proPaint.setAntiAlias(true);
        proPaint.setStyle(Paint.Style.FILL);
        proPaint.setColor(proColor);
        proPaint.setStrokeWidth(lineProWidth);
        proPaint.setTextSize(textSize);
        proPaint.setTextAlign(Paint.Align.CENTER);

        focusPaint = new Paint();
        focusPaint.setAntiAlias(true);
        focusPaint.setStyle(Paint.Style.FILL);
        focusPaint.setColor(Color.RED);
        focusPaint.setStrokeWidth(lineProWidth);
        focusPaint.setTextSize(textSize);
        focusPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int bgWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            bgWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        } else
            bgWidth = Util.dip2px(getContext(), 311);

        int bgHeight;
        if (heightMode == MeasureSpec.EXACTLY) {
            bgHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        } else
            bgHeight = Util.dip2px(getContext(), 150);
        float left = getPaddingLeft() + bgRadius;
        stopX = bgWidth - bgRadius;
        startX = left;
        bgCenterY = bgHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        interval = (int) ((stopX - startX) / (maxStep - 1));
        drawBg(canvas);
        drawText(canvas);
        drawProgress(canvas);

    }

    private void drawBg(Canvas canvas){
        /* 设置渐变色 这个正方形的颜色是改变的 */
        Shader mShader = new LinearGradient(startX, bgCenterY, stopX, bgCenterY,
                new int[] { Color.parseColor("#ff9966"), Color.parseColor("#ff9966"),  Color.parseColor("#cc6600"),  Color.parseColor("#cc6600")}, null, Shader.TileMode.CLAMP); // TODO 一个材质,打造出一个线性梯度沿著一条线。
        bgPaint.setShader(mShader);
        canvas.drawLine(startX, bgCenterY, stopX, bgCenterY, bgPaint);
        for (int i = 0; i < maxStep; i++) {
            canvas.drawCircle(startX + (i * interval), bgCenterY, bgRadius, bgPaint);
        }
    }
    private void drawProgress(Canvas canvas){
        proPaint.setShadowLayer(20, 0, 0, Color.parseColor("#ff3399"));//TODO 设置目标进度外圈渐变色
        setLayerType(LAYER_TYPE_SOFTWARE, proPaint);

        canvas.drawCircle(startX + (proStep * interval), bgCenterY, proRadius*2, proPaint);
        proPaint.setShadowLayer(0, 0, 0, Color.parseColor("#ff3399"));//取消设置目标进度外圈渐变色 半径设为0
        Rect rect = new Rect();
        focusPaint.getTextBounds(titles[proStep],0,titles[proStep].length(),rect);
        int width = rect.width();//文本的宽度
        int height = rect.height();//文本的高度

        canvas.drawRoundRect((float) (startX + (proStep * interval)-width/1.2),bgCenterY-height*2-Util.dip2px(getContext(),textPadding),(float) (startX + (proStep * interval)+width/1.2),(float) (bgCenterY-Util.dip2px(getContext(),textPadding)),10,10,focusPaint);

        Path path = new Path();
        path.moveTo(startX+ (proStep * interval),(float) (bgCenterY-Util.dip2px(getContext(),(float) (textPadding*0.8))));
        path.lineTo(startX+ (proStep* interval)-width/4,bgCenterY-Util.dip2px(getContext(),textPadding));
        path.lineTo(startX+ (proStep * interval)+width/4,bgCenterY-Util.dip2px(getContext(),textPadding));
        path.close();
        canvas.drawPath(path,focusPaint);

        canvas.drawText(titles[proStep], startX + (proStep * interval),bgCenterY - Util.dip2px(getContext(),textPadding+height/4), proPaint);
    }

    private void drawText(Canvas canvas){
        Rect rect;
        Path path;
        for (int i = 0; i < maxStep; i++) {
            //画图片，就是贴图
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.home_ico_580);
//            canvas.drawBitmap(bitmap,startX+(i*interval),bgCenterY-textPadding,proPaint);
            rect = new Rect();
            proPaint.getTextBounds(titles[i],0,titles[i].length(),rect);
            int width = rect.width();//文本的宽度
            int height = rect.height();//文本的高度

            bgPaint.setShader(null);
            bgPaint.setColor(bgColor);
            canvas.drawRoundRect((float) (startX + (i * interval)-width/1.2),bgCenterY-height*2-Util.dip2px(getContext(),textPadding),(float) (startX + (i * interval)+width/1.2),(float) (bgCenterY-Util.dip2px(getContext(),textPadding)),10,10,bgPaint);

            path = new Path();

            path.moveTo(startX+ (i * interval),(float) (bgCenterY-Util.dip2px(getContext(),(float) (textPadding*0.8))));
            path.lineTo(startX+ (i * interval)-width/4,bgCenterY-Util.dip2px(getContext(),textPadding));
            path.lineTo(startX+ (i * interval)+width/4,bgCenterY-Util.dip2px(getContext(),textPadding));
            path.close();
            canvas.drawPath(path,bgPaint);

            if (i < proStep) {
                if (null != titles && i<titles.length)
                    canvas.drawText(titles[i], startX + (i * interval), bgCenterY - Util.dip2px(getContext(),textPadding+height/4), proPaint);

            } else {
                if (null != titles && i<titles.length) {
                    String title = titles[i];
                    if (null == title) continue;
                    canvas.drawText(title, startX + (i * interval), bgCenterY - Util.dip2px(getContext(),textPadding+height/4), proPaint);
                }
            }

        }

    }



    /**
     * 进度设置
     * @param progress 已完成到哪部
     * @param maxStep  总步骤
     * @param titles   步骤名称
     */
    public void setProgress(int progress, int maxStep, String[] titles) {
        proStep = progress;
        this.maxStep = maxStep;
        this.titles = titles;
        invalidate();
    }


    /**
     * 设置是否可以手动控制进度
     * @param checked
     */
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public OnStepClickListener getStepClickListener() {
        return mStepClickListener;
    }

    public void setStepClickListener(OnStepClickListener stepClickListener) {
        mStepClickListener = stepClickListener;
    }

    /**
     * 对view的监听事件
     */
    public interface  OnStepClickListener{
        void mOnStepClickListener(View v, int step);
    }

}
