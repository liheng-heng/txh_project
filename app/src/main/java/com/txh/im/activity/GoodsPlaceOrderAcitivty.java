package com.txh.im.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.fragment.ShopCarFragment;
import com.txh.im.fragment.ShopClassifyFragment;
import com.txh.im.segmentcontrol.SegmentControl;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * jiajia,商品代下单
 */

public class GoodsPlaceOrderAcitivty extends BasicActivity {

    @Bind(R.id.segment_control)
    SegmentControl segmentControl;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.iv_head_right)
    ImageView ivHeadRight;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.ll_tv_head_right)
    LinearLayout llTvHeadRight;
    @Bind(R.id.fl)
    FrameLayout fl;
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    private ShopClassifyFragment shopClassifyFragment;
    private ShopCarFragment shopCarFragment;
    private String userId;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_goods_place_order);
    }

    @Override
    protected void initTitle() {
        segmentControl.setText(new String[]{"商品", "代下单"});
        llHeadRight.setVisibility(View.VISIBLE);
        fragmentManager = this.getSupportFragmentManager();
        tvHeadRight.setTextColor(getResources().getColor(R.color.dipatch_bg));
    }

    @Override
    public void initData() {
        litennersegmentControl();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        setTabSelection(0);
    }

    private void litennersegmentControl() {
        segmentControl.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                hideKeyBoard();
                switch (index) {
                    case 0://点击商品
                        llHeadRight.setVisibility(View.VISIBLE);
                        setTabSelection(0);
                        break;
                    case 1://点击代下单
                        setTabSelection(1);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    int statusBarHeight;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        Log.i("------->>>", "GoodsPlaceOrderAcitivty--手机主题statusBarHeight-------" + statusBarHeight);
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    @SuppressLint("NewApi")
    public void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                llHeadRight.setVisibility(View.VISIBLE);
                if (shopClassifyFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    shopClassifyFragment = new ShopClassifyFragment();
                    transaction.add(R.id.fl, shopClassifyFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    shopClassifyFragment.HttpRightData(1, 1, 0, "", false);
                    transaction.show(shopClassifyFragment);
                }
                break;
            case 1:
                tvHeadRight.setText("编辑");
                if (shopCarFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    shopCarFragment = new ShopCarFragment();
                    transaction.add(R.id.fl, shopCarFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    shopCarFragment.getNetRequest();
                    shopCarFragment.initBottom();
                    transaction.show(shopCarFragment);
                }
                break;
        }
        transaction.commit();
    }

    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction) {
        llHeadRight.setVisibility(View.GONE);//隐藏搜索
        llTvHeadRight.setVisibility(View.GONE);//隐藏编辑
        if (shopCarFragment != null) {
            transaction.hide(shopCarFragment);
        }
        if (shopClassifyFragment != null) {
            transaction.hide(shopClassifyFragment);
        }
    }

    @OnClick({R.id.ll_head_back, R.id.ll_head_right, R.id.ll_tv_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                if (shopCarFragment != null && shopCarFragment.isVisible()) {
                    setTabSelection(0);
                    selectIndx(0);
                    return;
                }
                finish();
                break;

            case R.id.ll_head_right://点击搜索
                Intent intent = new Intent(GoodsPlaceOrderAcitivty.this, SearchActiveGoodsActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;

            case R.id.ll_tv_head_right://点击编辑
                String sHeadRight = tvHeadRight.getText().toString();
                if (sHeadRight.equals("编辑")) {//编辑状态被点击
                    tvHeadRight.setText("完成");
                    shopCarFragment.editShopCar();
                } else {//完成状态被点击
                    tvHeadRight.setText("编辑");
                    shopCarFragment.finishShopCar();
                }
                break;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    //设置选中的选项卡
    public void selectIndx(int i) {
        segmentControl.setSelectedIndex(i);
    }

    public LinearLayout getLlHeadRight() {
        return llTvHeadRight;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }
}
