package com.guduodemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.guduodemo.R;

/**
 * 长时间录音效果
 * Created by gqiu on 2017/12/4.
 */

public class LongRecordView extends View {
    private TextPaint mBackgroundPaint, mProgresPaint;
    private Paint mUnitPaint;
    private int mViewW, mViewH;
    private int mPadding;

    public LongRecordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LongRecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBackgroundPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(ContextCompat.getColor(context, R.color.black50));
        mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);


        mProgresPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mProgresPaint.setColor(ContextCompat.getColor(context, R.color.color_8cdf85));
        mProgresPaint.setStyle(Paint.Style.STROKE);
        mProgresPaint.setStrokeCap(Paint.Cap.ROUND);

        mPadding = dp2px(context, 2);
    }

    public int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mViewW = getMeasuredWidth();
        mViewH = getMeasuredHeight();

        mBackgroundPaint.setStrokeWidth(mViewW);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(0, 0, mViewW, mViewH, mBackgroundPaint);
        canvas.drawRect(mPadding, mPadding, mViewW / 2 - mPadding, mViewH - mPadding, mProgresPaint);
    }
}
