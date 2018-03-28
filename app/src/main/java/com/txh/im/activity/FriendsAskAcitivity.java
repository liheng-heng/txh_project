package com.txh.im.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.GetAskListBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FriendsAskAcitivity extends BasicActivity {

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
    private GetAskListBean getAskListBean;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_friends_ask_acitivity);
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
        getAskListBean = g.fromJson(friendask, GetAskListBean.class);
        llUserList.setVisibility(View.VISIBLE);
        Glide.with(this).load(getAskListBean.getImagesHead()).into(civAvatar);
        tvUserName.setText(getAskListBean.getUserName());
        tvTxhCode.setText(getAskListBean.getTXHCode());
    }

    @OnClick({R.id.ll_head_back, R.id.btn_friends})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                showProgress("正在加载");
                toNewFriendsListAcitivty();
                break;
            case R.id.btn_friends:
                showProgress("正在加载");
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("FId", getAskListBean.getFId());
                new GetNetUtil(hashmap, Api.TX_App_SNS_TakeFriend, this) {
                    @Override
                    public void successHandle(String base) {
                        if (isDestroy){
                            return;
                        }
                        Gson gson = new Gson();
                        HttpBean basebean = gson.fromJson(base, HttpBean.class);
                        if (basebean.getStatus().equals("200")) {
                            toNewFriendsListAcitivty();
                            ToastUtils.toast(FriendsAskAcitivity.this, basebean.getMsg());
                        } else {
                            hideProgress();
                            ToastUtils.toast(FriendsAskAcitivity.this, basebean.getMsg());
                        }
                    }
                };
                break;
        }
    }

    private void toNewFriendsListAcitivty() {
        new GetNetUtil(null, Api.TX_App_SNS_GetAskList, this) {
            @Override
            public void successHandle(String base) {
                if (isDestroy){
                    return;
                }
                hideProgress();
                Gson gson = new Gson();
                BaseListBean<GetAskListBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<GetAskListBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    List<GetAskListBean> obj = basebean.getObj();
                    if (obj == null || obj.size() == 0) {
                        toast(basebean.getMsg());
                        return;
                    }
                    Intent intent = new Intent(FriendsAskAcitivity.this, NewFriendsAcitivty.class);
                    intent.putExtra("asklist", gson.toJson(obj));
                    startActivity(intent);
                    finish();
                } else {
                    toast(basebean.getMsg());
                }
            }
        };
    }
}
