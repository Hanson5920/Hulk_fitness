package com.jaydenxiao.common.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaydenxiao.common.R;

/**
 * 查看更多TextView
 */
public class MoreTextView extends LinearLayout {
    protected TextView contentTextView;
    protected ImageView expandImageView;

    protected int textColor;
    protected float textSize;
    protected int maxLine;
    protected String text;

    public int defaultTextColor = Color.BLACK;
    public int defaultTextSize = 12;
    public int defaultLine = 3;
    int durationMillis = 200;//动画时间 ms
    private int addLine = 1;//有的字体变大后，高度变化，而取到的高度还是原来较小的，这里的作用是增加一行

    public MoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithAttrs(context, attrs);
        initView();
        bindTextView(textColor, textSize, maxLine, text);
        bindListener();
    }

    protected void initWithAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MoreTextStyle);
        textColor = a.getColor(R.styleable.MoreTextStyle_textColor,
                defaultTextColor);
        textSize = a.getDimensionPixelSize(R.styleable.MoreTextStyle_textSize, defaultTextSize);
        maxLine = a.getInt(R.styleable.MoreTextStyle_maxLine, defaultLine);
        text = a.getString(R.styleable.MoreTextStyle_text);
        a.recycle();
    }

    protected void initView() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        contentTextView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addView(contentTextView, layoutParams);

        expandImageView = new ImageView(getContext());
        int padding = dip2px(getContext(), 10);
        expandImageView.setImageResource(R.drawable.ic_expand);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, padding, 0, padding);
        addView(expandImageView, llp);
    }

    protected void bindTextView(int color, float size, final int line, String text) {
        contentTextView.setLineSpacing(0,1.2f);
        contentTextView.setTextColor(color);
        contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        contentTextView.setText(text);
        contentTextView.setHeight(contentTextView.getLineHeight() * line);
        expandImageView.setVisibility(contentTextView.getLineCount() > line ? View.VISIBLE : View.GONE);
    }

    protected void bindListener() {
        setOnClickListener(new View.OnClickListener() {
            boolean isExpand;

            @Override
            public void onClick(View v) {
                if (contentTextView.getLineCount() < maxLine) {
                    return;
                }
                isExpand = !isExpand;
                contentTextView.clearAnimation();
                final int deltaValue;
                final int startValue = contentTextView.getHeight();
                if (isExpand) {
                    deltaValue = contentTextView.getLineHeight() * (contentTextView.getLineCount() + addLine) - startValue;
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    expandImageView.startAnimation(animation);
                } else {
                    deltaValue = contentTextView.getLineHeight() * maxLine - startValue;
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    expandImageView.startAnimation(animation);
                }
                Animation animation = new Animation() {
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        contentTextView.setHeight((int) (startValue + deltaValue * interpolatedTime));
                    }

                };
                animation.setDuration(durationMillis);
                contentTextView.startAnimation(animation);
            }
        });
    }


    public void setText(CharSequence charSequence) {
        bindTextView(textColor, textSize, maxLine, charSequence.toString());
        contentTextView.setText(charSequence);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
