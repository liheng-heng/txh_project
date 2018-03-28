package com.txh.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.UIUtils;

import java.util.ArrayList;

/**
 * liheng
 * <p>
 * 20174.23
 */
public class GuidepageActivity extends Activity {


    private ViewPager mviewPager;
    /**
     * 装分页现实的View(导航页面)的数组
     */
    public ArrayList<View> pageViews;
    private View imageView;
    /**
     * 装小圆点的View的数组
     */
    private View[] imageViews;
    /**
     * 装N个导航页面的linearlayout
     */
    private ViewGroup viewPics;

    /**
     * 装圆点数组的linearlayout
     */
    private LinearLayout viewPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = GuidepageActivity.this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);

        LayoutInflater mInflater = getLayoutInflater();
        // 向pageviews数组中加载各个布局，若多个导航页，从此处添加
        pageViews = new ArrayList<View>();
        pageViews.add(mInflater.inflate(R.layout.guide1, null));
        pageViews.add(mInflater.inflate(R.layout.guide2, null));
        pageViews.add(mInflater.inflate(R.layout.guide3, null));
        // 实例化
        // 创建装小圆点的imageviews，大小是页面的多少
        imageViews = new View[pageViews.size()];
        // 从指定的xml文件加载视图
        viewPics = (ViewGroup) mInflater.inflate(R.layout.guipagemain, null);
        // 实例化小圆点和viewpager(图片）的linearlayout
        viewPoints = (LinearLayout) viewPics.findViewById(R.id.viewGroup);
        mviewPager = (ViewPager) viewPics.findViewById(R.id.guidePages);
        // 添加小圆点
        int a = UIUtils.px2dip(this, 50);
        for (int i = 0; i < pageViews.size(); i++) {
            imageView = new View(GuidepageActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(a, a);
            params.setMargins(20, 0, 20, 0);
            imageView.setLayoutParams(params);
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.dot_normal2);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.dot_normal);
            } // 选中时显示黑棋，未选中显示白棋
            viewPoints.addView(imageViews[i]);
        }
        setContentView(viewPics);
        mviewPager.setAdapter(new GuidePageAdapter());
        mviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageSelected(int arg0) {
                for (int i = 0; i < imageViews.length; i++) {
                    imageViews[arg0].setBackgroundResource(R.drawable.dot_normal2);
                    if (i != arg0) {
                        imageViews[i].setBackgroundResource(R.drawable.dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    class GuidePageAdapter extends PagerAdapter {
        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView(pageViews.get(position));
        }

        @Override
        public void finishUpdate(View container) {
            // TODO Auto-generated method stub
            // super.finishUpdate(container);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pageViews.size();
        }

        @Override
        public Object instantiateItem(View container, int position) {
            // TODO Auto-generated method stub
            ((ViewPager) container).addView(pageViews.get(position));
            if (position == pageViews.size() - 1) {
                TextView btnButton = (TextView) findViewById(R.id.b1);
                btnButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        SPUtil.putAndApply(GuidepageActivity.this, "once", true);
                        Intent intent = new Intent(GuidepageActivity.this, LoginActivity_new.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            return pageViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalApplication.getInstance().removeActivity(this);
    }
}
