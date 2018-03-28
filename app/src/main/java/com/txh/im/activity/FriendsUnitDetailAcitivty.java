package com.txh.im.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.FriendsUnitDetailBean;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/4/18.
 */

public class FriendsUnitDetailAcitivty extends BasicActivity {
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_shop_name)
    TextView tvShopName;
    @Bind(R.id.tv_shop_address)
    TextView tvShopAddress;
    @Bind(R.id.tv_shop_type)
    TextView tvShopType;
    @Bind(R.id.tv_remarks)
    TextView tvRemarks;
    @Bind(R.id.ll_address)
    LinearLayout llAddess;
    private Gson gson;
    private FriendsUnitDetailBean friendsUnitDetailBean;
    private String friendUnitId;
    private String friendsunit;
    private String friendUnitType;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_friends_unit_detail);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("详细资料");
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        friendsunit = intent.getStringExtra("friendsunit");
        friendUnitId = intent.getStringExtra("FriendUnitId");
        friendUnitType = intent.getStringExtra("FriendUnitType");
        gson = new Gson();
        friendsUnitDetailBean = gson.fromJson(friendsunit, FriendsUnitDetailBean.class);
        if (friendsUnitDetailBean.getUnitName() == null) {
            friendsUnitDetailBean.setUnitName("");
        }
        if (friendsUnitDetailBean.getAddress() == null) {
            friendsUnitDetailBean.setAddress("");
        }
        if (friendsUnitDetailBean.getProvinceName() == null) {
            friendsUnitDetailBean.setProvinceName("");
        }
        if (friendsUnitDetailBean.getCityName() == null) {
            friendsUnitDetailBean.setCityName("");
        }
        if (friendsUnitDetailBean.getCountyName() == null) {
            friendsUnitDetailBean.setCountyName("");
        }
        if (friendsUnitDetailBean.getZoneName() == null) {
            friendsUnitDetailBean.setZoneName("");
        }
        if (friendUnitType.equals("2")) {
            tvShopType.setText("供应商");
        } else if (friendUnitType.equals("1")) {
            tvShopType.setText("店铺");
            llAddess.setVisibility(View.GONE);
        }
        if (friendsUnitDetailBean.getMarkName() != null && !friendsUnitDetailBean.getMarkName().equals("")) {
            tvShopName.setText(friendsUnitDetailBean.getMarkName() + "(" + friendsUnitDetailBean.getUnitName() + ")");
        } else {
            tvShopName.setText(friendsUnitDetailBean.getUnitName());
        }
        tvShopAddress.setText(friendsUnitDetailBean.getProvinceName() +
                friendsUnitDetailBean.getCityName() +
                friendsUnitDetailBean.getZoneName() +
                friendsUnitDetailBean.getCountyName() +
                friendsUnitDetailBean.getAddress());
    }

    @OnClick({R.id.ll_head_back, R.id.ll_remark})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;
            case R.id.ll_remark:
                Intent intent = new Intent(this, ShopRemarkAcitivity.class);
                intent.putExtra("FriendUnitId", friendUnitId);
                intent.putExtra("FriendUnitType", friendUnitType);
                intent.putExtra("unitname", friendsUnitDetailBean.getMarkName());
                startActivity(intent);
                finish();
                break;
        }
    }
}
