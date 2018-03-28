package com.txh.im.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.GetAskListBean;
import com.txh.im.widget.CircleImageView;

import butterknife.Bind;
import butterknife.OnClick;

public class FriendsAskOverdueAcitivity extends BasicActivity {
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.civ_avatar)
    CircleImageView civAvatar;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_txh_code)
    TextView tvTxhCode;
    @Bind(R.id.ll_user_list)
    LinearLayout llUserList;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_friends_ask_overdue_acitivity);
    }


    @Override
    protected void initTitle() {
        headTitle.setText("详细资料");
    }


    @Override
    public void initData() {
        Intent intent = getIntent();
        String friendask = intent.getStringExtra("friendask");
        Gson g = new Gson();
        GetAskListBean getAskListBean = g.fromJson(friendask, GetAskListBean.class);
        llUserList.setVisibility(View.VISIBLE);
        Glide.with(this).load(getAskListBean.getImagesHead()).into(civAvatar);
        tvUserName.setText(getAskListBean.getUserName());
        tvTxhCode.setText(getAskListBean.getTXHCode());
    }


    @OnClick(R.id.ll_head_back)
    public void onClick(View view){
        finish();
    }

}
