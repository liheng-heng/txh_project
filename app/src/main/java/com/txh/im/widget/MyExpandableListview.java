package com.txh.im.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by jiajia on 2017/4/11.
 */

public class MyExpandableListview extends ExpandableListView{
    public MyExpandableListview(Context context) {
        super(context);
    }

    public MyExpandableListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyExpandableListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
