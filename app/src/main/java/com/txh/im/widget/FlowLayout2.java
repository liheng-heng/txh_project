package com.txh.im.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.txh.im.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by liheng on 2017/5/23.
 */

public class FlowLayout2 extends ViewGroup {

    int viewWidth, viewHeight;//控件宽和高
    ArrayList<RowViews> rows = new ArrayList<FlowLayout2.RowViews>();//记录行信息，一个数据一行
    int viewSpace = 15; //间隔

    public FlowLayout2(Context context) {
        super(context, null);

    }

    public FlowLayout2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlowLayout2(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    //		测量模式：
    //		EXACTLY：表示设置了精确的值，一般当childView设置其宽、高为精确值、match_parent时，ViewGroup会将其设置为EXACTLY；
    //		AT_MOST：表示子布局被限制在一个最大值内，一般当childView设置其宽、高为wrap_content时，ViewGroup会将其设置为AT_MOST；
    //		UNSPECIFIED：表示子布局想要多大就多大，一般出现在AadapterView的item的heightMode中、ScrollView的childView的heightMode中；此种模式比较少见。

    //			onMeasure设置自己的宽和高

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        viewWidth = DisplayUtil.getMySize(DisplayUtil.getWidth(getContext()), widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录子view设置高
         */
        int height = viewSpace; //统计高度
        int width = viewSpace;//统计一行的宽度，用来判断是否还换行
        int oldwidth = 0;//记录还没有尝试添加下一个控件时的宽度
        int maxRowHeight = 0; //当前行的最大行高
        int oldMaxRowHeight = 0;//记录还没有尝试添加下一个控件时的当前行的最大行
        int childCount = getChildCount();
        RowViews rowViews = new RowViews(); //一行的view数据

        for (int i = 0; i < childCount; i++) {
            oldwidth = width;
            oldMaxRowHeight = maxRowHeight;
            View childView = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            int h = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin + viewSpace;//控件的占用高度
            maxRowHeight = Math.max(maxRowHeight, h);
            width += childView.getMeasuredWidth() + params.rightMargin + params.leftMargin + viewSpace;
            if (width > viewWidth) {
                //判断是否一个控件就超过了父控件的大小
                if (oldwidth != viewSpace) {
                    i = i - 1;//回退
                    maxRowHeight = oldMaxRowHeight;
                } else {
                    rowViews.addView(new ViewInfo(childView, viewSpace));
                }
                width = viewSpace;

                rowViews.setStartLocationY(height);
                height += maxRowHeight;
                rowViews.setMaxHeight(maxRowHeight);//设置最大的高度
                rows.add(rowViews);//加到1行
                rowViews = new RowViews();//重置

                maxRowHeight = 0;
                //刚好是最后一个因素充满
                if (i == childCount - 1) {
                    rowViews = null;
                    break;
                } else {
                    continue;
                }
            } else {
                rowViews.addView(new ViewInfo(childView, oldwidth));
                //当最后一个因素了，而没有充满一行，添加为一行
                if (i == childCount - 1) {
                    rowViews.setStartLocationY(height);
                    height += maxRowHeight;
                    rowViews.setMaxHeight(maxRowHeight);//设置最大的高度
                    rows.add(rowViews);//加到1行
                    rowViews = null;
                }
            }
        }

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension(viewWidth, viewHeight = (heightMode == MeasureSpec.EXACTLY) ? viewHeight
                : height);
    }


    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    //对组件的位置进行设置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            for (int i = 0; i < rows.size(); i++) {
                RowViews rowViews = rows.get(i);
                Vector<ViewInfo> views = rowViews.getViews();
                for (int j = 0; j < views.size(); j++) {
                    ViewInfo viewInfo = views.get(j);
                    viewInfo.view.layout(viewInfo.startX,
                            rowViews.startLocationY,
                            viewInfo.startX + viewInfo.view.getMeasuredWidth(),
                            rowViews.startLocationY + viewInfo.view.getMeasuredHeight());
                }
            }
        }
    }


    /**
     * 一行的数据
     *
     * @作者 廖兴文
     * @时间 2017-2-21
     */
    class RowViews {
        Vector<ViewInfo> views = new Vector<ViewInfo>();
        int maxHeight; //当前行的最大高度
        int startLocationY;//开始的位置

        public int getStartLocationY() {
            return startLocationY;
        }

        public void setStartLocationY(int startLocationY) {
            this.startLocationY = startLocationY;
        }

        public Vector<ViewInfo> getViews() {
            return views;
        }

        public void addView(ViewInfo view) {
            this.views.add(view);
        }

        public void removeView(View view) {
            this.views.remove(view);
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public void setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
        }

    }

    class ViewInfo {
        View view;
        int startX;

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public ViewInfo() {
        }

        public ViewInfo(View view, int startX) {
            this.view = view;
            this.startX = startX;
        }
    }

}