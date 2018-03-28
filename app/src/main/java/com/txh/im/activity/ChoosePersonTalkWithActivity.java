package com.txh.im.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.adapter.ChoosePersonTalkwithAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.ChoosePersonTalkBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liheng on 2017/3/29.
 * <p>
 * 选择可以聊天的朋友---弹窗效果
 */

public class ChoosePersonTalkWithActivity extends BasicActivity {

    @Bind(R.id.ll_listview)
    ListView llListview;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;

    List<ChoosePersonTalkBean.UserListBean> list = new ArrayList<>();

    private ChoosePersonTalkwithAdapter adapter;
    private String shopId;
    private ChoosePersonTalkBean choosePersonTalkBean;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_choose_person_talkwith);
        choosePersonTalkBean = (ChoosePersonTalkBean) getIntent().getSerializableExtra("choosePersonTalkBean");
    }

    @Override
    protected void initTitle() {
        adapter = new ChoosePersonTalkwithAdapter(ChoosePersonTalkWithActivity.this, list, R.layout.item_choose_person_talk);
        llListview.setAdapter(adapter);
    }

    @Override
    public void initData() {
        if (choosePersonTalkBean != null) {
            List<ChoosePersonTalkBean.UserListBean> userList = choosePersonTalkBean.getUserList();
            adapter.setRefrece(userList);
            if (null != choosePersonTalkBean.getShopInfo()) {
                tvShopname.setText(choosePersonTalkBean.getShopInfo().getShopName());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}


