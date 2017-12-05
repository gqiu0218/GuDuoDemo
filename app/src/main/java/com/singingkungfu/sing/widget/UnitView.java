package com.singingkungfu.sing.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.singingkungfu.sing.R;

/**
 * Created by gqiu On 2017/12/5.
 */

public class UnitView extends View {
    private Paint mPaint;
    private TextPaint mUnitPaint;
    private int mMinSize, mMaxSize;
    private static final int TOTAL = 30;
    private static final int TXT_SIZE = 14;

    private int mTxtSize;


    public UnitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UnitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ContextCompat.getColor(context, R.color.white50));

        mUnitPaint = new TextPaint();
        mUnitPaint.setColor(Color.WHITE);
        mTxtSize = sp2px(context, TXT_SIZE);
        mUnitPaint.setTextSize(mTxtSize);


        mMinSize = dp2px(context, 1);
        mMaxSize = dp2px(context, 1.5f);
    }


    public int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int centerY = height / 2;

        int minH = (int) (height * 0.3f);
        int maxH = (int) (height * 0.45f);
        int cellW = width / TOTAL;

        int index = 1;
        while (index < TOTAL) {
            int startX = index * cellW;

            switch (index) {
                case 3:
                case 8:
                case 13:
                case 18:
                case 23:
                case 28:
                    mPaint.setStrokeWidth(mMaxSize);
                    canvas.drawLine(startX, centerY - maxH / 2, startX, centerY + maxH / 2, mPaint);
                    break;
                case 5:
                case 10:
                case 15:
                case 20:
                case 25:
                    String txt = index + "s";
                    canvas.drawText(txt, startX, centerY + mTxtSize / 3, mUnitPaint);
                default:
                    mPaint.setStrokeWidth(mMinSize);
                    canvas.drawLine(startX, centerY - minH / 2, startX, centerY + minH / 2, mPaint);
                    break;
            }
            index++;
        }
    }

}
