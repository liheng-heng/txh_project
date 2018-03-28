package com.txh.im.photo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.crm.app.R;
//import com.crm.app.base.BaseActivity;
//import com.crm.app.utils.PicassoUtils;
//import com.crm.app.utils.TextUtil;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.utils.PicassoUtils;
import com.txh.im.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 图片详情查看器
 *
 * @author Administrator
 */
public class ImageZoomActivity extends BasicActivity implements View.OnClickListener {

    private ViewPager zoom_vp;
    private MyPageAdapter adapter;
    private TextView cancel, head_title;
    private LinearLayout ll_head_back;
    private int currentPosition;
    private List<ImageItem> mDataList = new ArrayList<ImageItem>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_imagezoom);
    }

    @Override
    protected void initTitle() {
        //得到图片当前的位置（）
        currentPosition = getIntent().getIntExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, 0);
        mDataList = (List<ImageItem>) getIntent().getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
        zoom_vp = (ViewPager) findViewById(R.id.zoom_vp);
        zoom_vp.setOnPageChangeListener(pageChangeListener);

        cancel = (TextView) findViewById(R.id.cancel);
        head_title = (TextView) findViewById(R.id.head_title);
        ll_head_back = (LinearLayout) findViewById(R.id.ll_head_back);
        ll_head_back.setOnClickListener(this);
        cancel.setOnClickListener(this);

        adapter = new MyPageAdapter(mDataList);
        zoom_vp.setAdapter(adapter);
        zoom_vp.setCurrentItem(currentPosition);
    }

    @Override
    public void initData() {
        head_title.setText(getIntent().getIntExtra("photo_title", R.string.see_photo));
    }



    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            currentPosition = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;

            case R.id.ll_head_back:
                finish();
                break;
        }
    }

    class MyPageAdapter extends PagerAdapter {
        private List<ImageItem> dataList = new ArrayList<>();
        private ArrayList<ImageView> mViews = new ArrayList<>();

        public MyPageAdapter(List<ImageItem> dataList) {
//            Log.v("----------------------????????", dataList.toString());
            this.dataList = dataList;
            int size = dataList.size();

            for (int i = 0; i != size; i++) {
                ImageItem item = dataList.get(i);
                ImageView iv = new ImageView(ImageZoomActivity.this);

             /*   if (!TextUtil.isEmpty(dataList.get(i).getThumbnailPath())) {
                    *//**  1 *//*
                    PicassoUtils.getDefault().CommonSDCardUrl(ImageZoomActivity.this, item.getSourcePath(), R.drawable.order_master_chart2_gray, iv);
                    Log.v("----------------------????????--", "dataList.get(i).getThumbnailPath()----" + dataList.get(i).getThumbnailPath());

                } else {
                    *//**  2 *//*
                    PicassoUtils.getDefault().CommonUrl(ImageZoomActivity.this, item.getSourcePath(), R.drawable.icon, iv);

                    Log.v("----------------------????????--", "item.getSourcePath()----" + item.getSourcePath());
                    Log.v("----------------------????????--", "item.getOss_path()----" + item.getOss_path());

//                    PicassoUtils.getDefault().CommonUrl(ImageZoomActivity.this, item.getOss_path(), R.drawable.icon, iv);

                }*/

                if (!TextUtil.isEmpty(dataList.get(i).getThumbnailPath())) {
                    /**  1 */
                    PicassoUtils.getDefault().CommonSDCardUrl(ImageZoomActivity.this, item.getSourcePath(), R.drawable.order_master_chart2_gray, iv);
                    Log.v("-????????--", "11-dataList.get(i).getThumbnailPath()----" + dataList.get(i).getThumbnailPath());

                } else if (!TextUtil.isEmpty(item.getOss_path())) {
                    /**  2 */
                    PicassoUtils.getDefault().CommonUrl(ImageZoomActivity.this, item.getOss_path(), R.drawable.order_master_chart2_gray, iv);

                    Log.v("-????????--", "22-item.getSourcePath()----1--" + item.getSourcePath());
                    Log.v("????????--", "22-item.getOss_path()----1--" + item.getOss_path());

                } else {
                    PicassoUtils.getDefault().CommonSDCardUrl(ImageZoomActivity.this, item.getSourcePath(), R.drawable.order_master_chart2_gray, iv);

                    Log.v("-????????--", "33-item.getSourcePath()----2--" + item.getSourcePath());
                    Log.v("-????????--", "33-item.getOss_path()----2--" + item.getOss_path());

                }

                iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                iv.setBackgroundColor(Color.BLACK);
                mViews.add(iv);
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public Object instantiateItem(View arg0, int arg1) {
            ImageView iv = mViews.get(arg1);
            ((ViewPager) arg0).addView(iv);
            return iv;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            if (mViews.size() >= arg1 + 1) {
                ((ViewPager) arg0).removeView(mViews.get(arg1));
            }
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        public void removeView(int position) {
            if (position + 1 <= mViews.size()) {
                mViews.remove(position);
            }
        }

    }
}