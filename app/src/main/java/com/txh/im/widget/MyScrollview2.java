package com.txh.im.widget;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by liheng on 2017/7/3.
 */
import android.content.Context;

import org.json.JSONObject;

public class MyScrollview2 extends ScrollView {
    private int downX;
    private int downY;
    private int mTouchSlop;

    public MyScrollview2(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollview2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollview2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = (View) getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        d -= (getHeight() + getScrollY());
        if (d == 0) {
//            int allorjigou = InstanceBean.getInstanceBean().getAllorjigou();
//            if (allorjigou == 1) {
//                JSONObject eventObject = new JSONObject();
//                ZhugeSDK.getInstance().onEvent(getContext(), "投资人资料_全部浏览", eventObject);
//            } else if (allorjigou == 2) {
//                JSONObject eventObject = new JSONObject();
//                ZhugeSDK.getInstance().onEvent(getContext(), "投资人机构资料_全部浏览", eventObject);
//            }

        } else
            super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
}
