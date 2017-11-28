package com.guduodemo.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.guduodemo.R;


/**
 * 涟漪效果
 * Created by gqiu on 2017/10/18.
 */

public class MainRippleView extends View implements Animator.AnimatorListener {

    private Paint mPaint;
    private int mRadius1, mRadius2, mRadius3;
    private boolean isUpdate;
    private int mCenterX, mCenterY;
    private int mAlpha1, mAlpha2, mAlpha3;
    private AnimatorSet animatorSet;
    private boolean mStopDraw1, mStopDraw2, mStopDraw3;
    private int mViewH;

    public MainRippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainRippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_3f51b5));
        mPaint.setStrokeWidth(dp2px(2));
        mPaint.setStyle(Paint.Style.FILL);
        mStopDraw1 = true;
        mStopDraw2 = true;
        mStopDraw3 = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;
        mViewH = getMeasuredHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean next = false;
        if (isUpdate || !mStopDraw1) {
            mPaint.setAlpha(mAlpha1);
            canvas.drawCircle(mCenterX, mCenterY, mRadius1, mPaint);
            next = true;
        }

        if (isUpdate || !mStopDraw2) {
            mPaint.setAlpha(mAlpha2);
            canvas.drawCircle(mCenterX, mCenterY, mRadius2, mPaint);
            next = true;
        }

        if (isUpdate || !mStopDraw3) {
            mPaint.setAlpha(mAlpha3);
            canvas.drawCircle(mCenterX, mCenterY, mRadius3, mPaint);
            next = true;
        }

        if (next) {
            postInvalidate();
        } else {
            //取消动画
            if (animatorSet != null && animatorSet.isRunning()) {
                animatorSet.cancel();
            }
        }
    }


    public void start() {
        mStopDraw1 = false;
        mStopDraw2 = false;
        mStopDraw3 = false;

        int start =  dp2px(30);


        animatorSet = new AnimatorSet();
        ValueAnimator animator1 = ValueAnimator.ofInt(start, mViewH / 2);
        animator1.setDuration(3000);
        animator1.setRepeatCount(ValueAnimator.INFINITE);
        animator1.setRepeatMode(ValueAnimator.RESTART);

        ValueAnimator animator2 = ValueAnimator.ofInt(start, mViewH / 2);
        animator2.setDuration(3000);
        animator2.setStartDelay(400);
        animator2.setRepeatCount(ValueAnimator.INFINITE);
        animator2.setRepeatMode(ValueAnimator.RESTART);

        ValueAnimator animator3 = ValueAnimator.ofInt(start, mViewH / 2);
        animator3.setDuration(3000);
        animator3.setStartDelay(800);
        animator3.setRepeatCount(ValueAnimator.INFINITE);
        animator3.setRepeatMode(ValueAnimator.RESTART);

        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRadius1 = (int) valueAnimator.getAnimatedValue();
                mAlpha1 = (int) ((1 - (float) mRadius1 / mCenterY) * 255 * 0.8f);
            }
        });
        addUpdateListener(animator1, 1);

        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRadius2 = (int) valueAnimator.getAnimatedValue();
                mAlpha2 = (int) ((1 - (float) mRadius2 / mCenterY) * 255 * 0.8f);
            }
        });
        addUpdateListener(animator2, 2);


        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRadius3 = (int) valueAnimator.getAnimatedValue();
                mAlpha3 = (int) ((1 - (float) mRadius3 / mCenterY) * 255 * 0.8f);
            }
        });
        addUpdateListener(animator3, 3);

        animatorSet.playTogether(animator1, animator2, animator3);
        animatorSet.addListener(this);

        animatorSet.start();
    }


    private void addUpdateListener(ValueAnimator animator, final int index) {

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                animator.setStartDelay(0);
                if (!isUpdate) {
                    switch (index) {
                        case 1:
                            mStopDraw1 = true;
                            break;
                        case 2:
                            mStopDraw2 = true;
                            break;
                        case 3:
                            mStopDraw3 = true;
                            break;
                    }
                }
            }
        });
    }

    public void stop() {
        isUpdate = false;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        isUpdate = true;
        postInvalidate();
    }

    @Override
    public void onAnimationEnd(Animator animator) {
    }

    @Override
    public void onAnimationCancel(Animator animator) {
    }

    @Override
    public void onAnimationRepeat(Animator animator) {
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
