package com.txh.im.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.RecommendsFriendsToOthersAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.FriendsRolesBean;
import com.txh.im.bean.FristWordBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.utils.GetNetUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class RecommendFriendsToOthersAcitivty extends BasicActivity {
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.rc)
    RecyclerView rc;
    private String card_detail;
    private List<FristWordBean> list;
    private Gson gson;
    private HomeSingleListBean homeSingleListBean;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_recommend_friends_to_others_acitivty);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("推荐好友");
        llHeadRight.setVisibility(View.VISIBLE);
        tvHeadRight.setText("发送");
    }

    @Override
    public void initData() {
        rc.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        gson = new Gson();
        card_detail = intent.getStringExtra("card_detail");
        homeSingleListBean = gson.fromJson(card_detail, HomeSingleListBean.class);
        getPushFriend();
    }

    private void getPushFriend() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("FriendUserId", homeSingleListBean.getUserId());
        new GetNetUtil(hashMap, Api.TX_App_SNS_PushFriend, this) {
            @Override
            public void successHandle(String base) {
                if (isDestroy) {
                    return;
                }
                BaseListBean<FristWordBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<FristWordBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    list = basebean.getObj();
                    if (list != null && list.size() > 0) {
                        tv.setVisibility(View.GONE);
                        RecommendsFriendsToOthersAdapter recommendsFriendsToOthersAdapter =
                                new RecommendsFriendsToOthersAdapter(RecommendFriendsToOthersAcitivty.this, list);
                        rc.setAdapter(recommendsFriendsToOthersAdapter);
                    } else {
                        if (basebean.getMsg() != null) {
                            tv.setVisibility(View.VISIBLE);
                            tv.setText(basebean.getMsg());
                        }
                    }
                } else {
                    if (basebean.getMsg() != null) {
                        tv.setVisibility(View.VISIBLE);
                        tv.setText(basebean.getMsg());
                    }
                }
            }
        };
    }

    @OnClick({R.id.ll_head_right, R.id.ll_head_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_right:
                toChatAcitivty();
                break;
            case R.id.ll_head_back:
                finish();
                break;
        }
    }

    private void toChatAcitivty() {
        HomeSingleListBean homeSingleListBean = toChat();
        if (homeSingleListBean == null) {
            toast("请选择推荐的好友!");
            return;
        }
        Intent intent = new Intent();
        Gson gson = new Gson();
        intent.setClass(this, ChatAcitivty.class);
        intent.putExtra("card_detail", card_detail);
        intent.putExtra("homeSingleListBean", gson.toJson(homeSingleListBean));
        intent.putExtra("home_fragment", "home_fragment");
//        this.setResult(Activity.RESULT_OK);
//        startActivityForResult(intent, 12);
        startActivity(intent);
        finish();
    }

    private HomeSingleListBean toChat() {
        HomeSingleListBean homeSingleListBean = new HomeSingleListBean();
        for (int a = 0; a < list.size(); a++) {
            for (int b = 0; b < list.get(a).getList().size(); b++) {
                for (int c = 0; c < list.get(a).getList().get(b).getItem().size(); c++) {
                    FriendsRolesBean friendsRolesBean = list.get(a).getList().get(b).getItem().get(c);
                    if (friendsRolesBean.getIsChecked() == 1) {
                        homeSingleListBean.setImagesHead(friendsRolesBean.getImagesHead());
                        homeSingleListBean.setUserName(friendsRolesBean.getUserName());
                        homeSingleListBean.setUserId(friendsRolesBean.getUserId());
                    }
                }
            }
        }
        if (homeSingleListBean.getUserId() == null) {
            return null;
        }
        return homeSingleListBean;
    }
}
