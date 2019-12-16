package com.allenliu.sidebar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Allen Liu on 2019/12/12.
 */
public class SideBar extends android.support.v7.widget.AppCompatTextView {
    private String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint textPaint;
    private Paint bigTextPaint;

    private ISideBarSelectCallBack callBack;
    private float eventY;
    private float w;
    private float sideTextWidth;
    /**
     * 是否重新测量宽高
     */
    private boolean isTouching = false;
    private float itemH;

    /**
     * 振幅
     */
    private float A = dp(100);
    /**
     * 波峰与bigText之间的距离
     */
    private int gapBetweenText = dp(50);

    /**
     * 开口数量
     */
    private int openCount = 13;
    /**
     * 字体缩放，基于textSize
     */
    private float fontScale = 1;
    private float bigTextSize;

    public SideBar(Context context) {
        super(context);
        init(null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * set MaxFontScale
     *
     * @param fontScale
     * @return
     */
    public SideBar setFontScale(float fontScale) {
        this.fontScale = fontScale;
        return this;
    }

    public void setDataResource(String[] data) {
        letters = data;
        invalidate();
    }

    public void setOnStrSelectCallBack(ISideBarSelectCallBack callBack) {
        this.callBack = callBack;
    }

    public SideBar setBigTextSize(float bigTextSize) {
        this.bigTextSize = bigTextSize;
        bigTextPaint.setTextSize(bigTextSize);
//        invalidate();
        return this;
    }

    public SideBar setA(float a) {
        A = a;
//        invalidate();
        return this;
    }

    public SideBar setGapBetweenText(int gapBetweenText) {
        this.gapBetweenText = gapBetweenText;
//        invalidate();
        return this;
    }

    public SideBar setOpenCount(int openCount) {
        this.openCount = openCount;
//        invalidate();
        return this;
    }

    private void caculateAW(int height) {
        itemH = height * 1.0f / letters.length;
        /**
         * 开口宽度
         */
        float opendWidth = itemH * openCount;
        //角速度 2PI/t 周期
        w = (float) (Math.PI * 2.0f / (opendWidth * 2));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        caculateAW(MeasureSpec.getSize(heightMeasureSpec));
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            viewWidth = !isTouching ? (int) (sideTextWidth + getPaddingLeft() + getPaddingRight()) : (int) (A + gapBetweenText + getBigTextWidth() + getPaddingLeft() + getPaddingRight());
        }
//        CLog.e("width:" + viewWidth + "height:" + MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(viewWidth, MeasureSpec.getSize(heightMeasureSpec));
    }

    private void init(AttributeSet attrs) {
        //  setPadding(dp(10), 0, dp(10), 0);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SideBar);
            A = typedArray.getInteger(R.styleable.SideBar_A, dp(100));
            fontScale = typedArray.getFloat(R.styleable.SideBar_fontScale, 1);
            bigTextSize = typedArray.getFloat(R.styleable.SideBar_bigTextSize, getTextSize() * 3);
            gapBetweenText = typedArray.getInteger(R.styleable.SideBar_gapBetweenText, dp(50));
            openCount = typedArray.getInteger(R.styleable.SideBar_openCount, 13);
        } else {
            bigTextSize = getTextSize() * 3;
        }
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getCurrentTextColor());
        textPaint.setTextSize(getTextSize());
        textPaint.setTextAlign(Paint.Align.CENTER);

        bigTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigTextPaint.setColor(getCurrentTextColor());
        bigTextPaint.setTextSize(bigTextSize);
        bigTextPaint.setTextAlign(Paint.Align.CENTER);


        float sideTextHeight = textPaint.getFontMetrics().descent - textPaint.getFontMetrics().ascent;
        sideTextWidth = textPaint.measureText("W");
    }


    private int dp(int v) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * scale + 0.5f);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int startTouchX = (int) (getMeasuredWidth() - A);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (event.getX() > startTouchX) {
                    eventY = event.getY();
                    if (!isTouching) {
                        isTouching = true;
                        requestLayout();
                    } else {
                        invalidate();
                    }

                } else {
                    if (isTouching) {
                        resetDefault();
                    }
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                resetDefault();
                return true;

        }
        return super.onTouchEvent(event);
    }

    private void resetDefault() {
        isTouching = false;
        eventY = 0;
        requestLayout();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int singleSideCount = openCount / 2;
        int index = isTouching && eventY >= 0 && eventY <= getMeasuredHeight() ? (int) Math.floor((eventY / itemH)) : -(singleSideCount + 1);
//        index=Math.min(letters.length,index);
//        CLog.e("index:" + index + "eventY:" + eventY);
        float sideX = sideTextWidth / 2 + getPaddingRight();
        for (int i = 0; i < letters.length; i++) {
            //rest textsize
            textPaint.setTextSize(getTextSize());
            int y = (int) (itemH * (i + 1));
            int x;
            if (Math.abs(i - index) > singleSideCount) {
                x = (int) (getMeasuredWidth() - sideX);
            } else {
                float percent = eventY / itemH;
                int t = (int) (i * itemH - eventY);
                double v = A * Math.sin(w * t + Math.PI / 2);

//                //如果算出来小于字体宽度 就取字体宽度
                v = Math.max(v, sideX);
                x = (int) (getMeasuredWidth() - v);
                //根据delta缩放字体
                if (v == sideX) {
                    textPaint.setTextSize(getTextSize());
                } else {
                    float delta = (Math.abs((i - percent)) / singleSideCount);
                    float textSize = getTextSize() + (1 - delta) * getTextSize() * fontScale;
//                    textSize=Math.max(textSize,getTextSize());
                    textPaint.setTextSize(textSize);
                }

            }
            canvas.drawText(letters[i], x, y, textPaint);

        }
        if (index != -(singleSideCount + 1)) {
            canvas.drawText(letters[index], getPaddingLeft() + getBigTextWidth() / 2, (int) (itemH * (index + 1)), bigTextPaint);
            if (callBack != null) {
                callBack.onSelectStr(index, letters[index]);

            }
        }

    }

    private float getBigTextWidth() {
        return bigTextPaint.measureText("W");
    }
}