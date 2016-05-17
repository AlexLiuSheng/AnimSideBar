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
 * Created by Allen Liu on 2016/5/12.
 */
public class SideBar extends TextView {
    private String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint textPaint;
    private Paint bigTextPaint;
    private Paint scaleTextPaint;
    private Canvas canvas;
    private int itemH;
    private int w;
    private int h;
    /**
     * 普通情况下字体大小
     */
    float singleTextH;
    /**
     * 缩放离原始的宽度
     */
    private float scaleWidth = dp(100);
    /**
     * 滑动的Y
     */
    private float eventY = 0;
    /**
     * 缩放的倍数
     */
    private int scaleTime = 1;
    /**
     * 缩放个数item，即开口大小
     */
    private int scaleItemCount = 6;
    private ISideBarSelectCallBack callBack;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SideBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    public void setDataResource(String[] data){
        letters=data;
        invalidate();
    }
   public void setOnStrSelectCallBack(ISideBarSelectCallBack callBack){
       this.callBack=callBack;
   }
    /**
     * 设置字体缩放比例
     * @param scale
     */
    public void setScaleTime(int scale){
        scaleTime=scale;
        invalidate();
    }

    /**
     * 设置缩放字体的个数，即开口大小
     * @param scaleItemCount
     */
    public  void setScaleItemCount(int scaleItemCount){
        this.scaleItemCount=scaleItemCount;
        invalidate();
    }

    private void init(AttributeSet attrs) {
      //  setPadding(dp(10), 0, dp(10), 0);
        if(attrs!=null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SideBar);
           scaleTime= typedArray.getInteger(R.styleable.SideBar_scaleTime,1);
            scaleItemCount=typedArray.getInteger(R.styleable.SideBar_scaleItemCount,6);
        }
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getCurrentTextColor());
        textPaint.setTextSize(getTextSize());
        textPaint.setTextAlign(Paint.Align.CENTER);
        bigTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigTextPaint.setColor(getCurrentTextColor());
        bigTextPaint.setTextSize(getTextSize()+getTextSize() * (scaleTime+2) );
        bigTextPaint.setTextAlign(Paint.Align.CENTER);
        scaleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scaleTextPaint.setColor(getCurrentTextColor());
        scaleTextPaint.setTextSize(getTextSize() * (scaleTime + 1));
        scaleTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private int dp(int px) {
        return DensityUtil.dip2px(getContext(), px);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(event.getX()>(w-getPaddingRight()-singleTextH-10)) {
                    eventY = event.getY();
                    invalidate();
                    return true;
                }else{
                    eventY = 0;
                    invalidate();
                    break;
                }
            case MotionEvent.ACTION_CANCEL:
                eventY = 0;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if(event.getX()>(w-getPaddingRight()-singleTextH-10)) {
                    eventY = 0;
                    invalidate();
                    return true;
                }else
                    break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        DrawView(eventY);
    }

    private void DrawView(float y) {
        int currentSelectIndex = -1;
        if (y != 0) {
            for (int i = 0; i < letters.length; i++) {
                float currentItemY = itemH * i;
                float nextItemY = itemH * (i + 1);
                if (y >= currentItemY && y < nextItemY) {
                    currentSelectIndex = i;
                    if(callBack!=null){
                        callBack.onSelectStr(currentSelectIndex,letters[i]);
                    }
                    //画大的字母
                    Paint.FontMetrics fontMetrics = bigTextPaint.getFontMetrics();
                    float bigTextSize = fontMetrics.descent - fontMetrics.ascent;
                    canvas.drawText(letters[i], w - getPaddingRight() - scaleWidth - bigTextSize, singleTextH + itemH * i, bigTextPaint);
                }
            }
        }
        drawLetters(y, currentSelectIndex);
    }

    private void drawLetters(float y, int index) {
        //第一次进来没有缩放情况，默认画原图
        if (index == -1) {
            w = getMeasuredWidth();
            h = getMeasuredHeight();
            itemH = h / letters.length;
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            singleTextH = fontMetrics.descent - fontMetrics.ascent;
            for (int i = 0; i < letters.length; i++) {
                canvas.drawText(letters[i], w - getPaddingRight(), singleTextH + itemH * i, textPaint);
            }
            //触摸的时候画缩放图
        } else {
            //遍历所有字母
            for (int i = 0; i < letters.length; i++) {
                //要画的字母的起始Y坐标
                float currentItemToDrawY = singleTextH + itemH * i;
                float centerItemToDrawY;
                if (index < i)
                    centerItemToDrawY = singleTextH + itemH * (index + scaleItemCount);
                else
                    centerItemToDrawY = singleTextH + itemH * (index - scaleItemCount);
                float delta = 1 - Math.abs((y - currentItemToDrawY) / (centerItemToDrawY - currentItemToDrawY));
                Log.v("delta", letters[i] + "--->" + delta + "");
                float maxRightX = w - getPaddingRight();
                //如果大于0，表明在y坐标上方
                scaleTextPaint.setTextSize(getTextSize() + getTextSize() * delta);
                float drawX = maxRightX - scaleWidth * delta;
                //超出边界直接花在边界上
                if (drawX > maxRightX)
                    canvas.drawText(letters[i], maxRightX, singleTextH + itemH * i, textPaint);
                else
                    canvas.drawText(letters[i], drawX, singleTextH + itemH * i, scaleTextPaint);

//抛物线实现，没有动画效果，太生硬了
//                    canvas.save();
//                    canvas.translate(w-getPaddingRight(),0);
//                    double y1 = singleTextH + itemH * (index - scaleItemCount);
//                    double y2 = singleTextH + itemH * (index + scaleItemCount);
//                    double topY = y;
//                    double topX = -scaleWidth;
//                    double p = topX / ((topY - y1) * (topY - y2));
//                    for (int j = 1; j <= scaleItemCount; j++) {
//                        double currentY=singleTextH + itemH * i;
//                        canvas.drawText(letters[i], (float) (p * (currentY - y1) * (currentY - y2)), singleTextH + itemH * i, scaleTextPaint);
//                    }
//                    canvas.restore();
                //     }
            }
        }
    }
}
