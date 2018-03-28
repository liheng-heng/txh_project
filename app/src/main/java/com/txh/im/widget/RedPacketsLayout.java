package com.txh.im.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.txh.im.R;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by my on 2017/6/24.
 */

public class RedPacketsLayout extends RelativeLayout {

    private int dHeight;//红包图片高度
    private int dWidth;//红包图片宽度

    private int mHeight;//view 高度
    private int mWidth;//view宽度

    private LayoutParams mLp;//红包布局参数

    private Drawable[] mDrawables;//红包图片

    private Random mRandom = new Random();

    private Thread mRainThread;
    private LinkedBlockingQueue<View> mCacheQueue = new LinkedBlockingQueue();

    private boolean isStop;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                addPacket();
            }
        }
    };


    public RedPacketsLayout(Context context) {
        super(context);
        init();
    }

    public RedPacketsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RedPacketsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDrawables = new Drawable[4];
        mDrawables[0] = getResources().getDrawable(R.drawable.bag_material);
        mDrawables[1] = getResources().getDrawable(R.drawable.gold_material);
        mDrawables[2] = getResources().getDrawable(R.drawable.gold_material_small);
        mDrawables[3] = getResources().getDrawable(R.drawable.red_packets);

        dHeight = mDrawables[1].getIntrinsicHeight();
        dWidth = mDrawables[1].getIntrinsicWidth();
        mLp = new LayoutParams(dWidth, dHeight);
        mLp.addRule(ALIGN_PARENT_TOP, TRUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private void addPacket() {
        ImageView imageView = null;
        if (mCacheQueue.isEmpty()) {
            imageView = new ImageView(getContext());
            //统一设置图片的宽高
//            imageView.setLayoutParams(mLp);

            //设置图片宽高自适应
            imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setImageDrawable(mDrawables[mRandom.nextInt(mDrawables.length)]);
            imageView.setRotation(mRandom.nextInt(180));
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeView(view);
                }
            });
        } else {
            imageView = (ImageView) mCacheQueue.poll();
        }

        addView(imageView);
        ValueAnimator set = genBezierAnimator(imageView);
        set.start();

    }

    public void startRain() {
        mRainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    try {
                        Thread.sleep(150);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mHandler.sendEmptyMessage(1);
                }
            }
        });
        mRainThread.start();
    }

    public void stopRain() {
        isStop = true;
    }

    private ValueAnimator genBezierAnimator(View target) {
        BezierEvaluator evaluator = new BezierEvaluator(getPoint(1), getPoint(2));//传入中间两个点
        ValueAnimator valueAnimator = ValueAnimator.ofObject(evaluator, new PointF(mRandom.nextInt(mWidth - dWidth), -dHeight),
                new PointF(mRandom.nextInt(mWidth - dWidth), mHeight));//传入开始位置结束位置
        valueAnimator.addUpdateListener(new BezierListener(target));
        valueAnimator.addListener(new AnimatorEndListener(target));
        valueAnimator.setTarget(target);
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return valueAnimator;
    }

    private PointF getPoint(int scale) {
        PointF pointF = new PointF();
        pointF.x = mRandom.nextInt(mWidth - 100);

        pointF.y = mRandom.nextInt((mHeight - 100) * scale / 2);
        return pointF;
    }

    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {
        private View mTarget;

        public BezierListener(View target) {
            this.mTarget = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            PointF pointF = (PointF) valueAnimator.getAnimatedValue();
            mTarget.setX(pointF.x);
            mTarget.setY(pointF.y);
        }
    }

    private class AnimatorEndListener extends AnimatorListenerAdapter {
        private View mTarget;

        public AnimatorEndListener(View view) {
            mTarget = view;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeView(mTarget);

            mCacheQueue.add(mTarget);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStop = true;
    }
}
