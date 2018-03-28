package com.txh.im.activity;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.R;
import com.txh.im.adapter.ActiveAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.ActiveNoticeBean;
import com.txh.im.bean.BaseListBean;
import com.txh.im.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liheng on 2017/5/6.
 */

public class ActiveActivity extends BasicActivity {

    private RelativeLayout rl_close_layout;
    private ListView ll_listview;
    private String basebean_active;
    private ActiveAdapter adapter;
    List<ActiveNoticeBean> list = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_active);
        rl_close_layout = (RelativeLayout) findViewById(R.id.rl_close_layout);
        ll_listview = (ListView) findViewById(R.id.ll_listview);
        basebean_active = getIntent().getStringExtra("basebean_active");
        adapter = new ActiveAdapter(ActiveActivity.this, list, R.layout.item_active);
        ll_listview.setAdapter(adapter);
    }

    @Override
    protected void initTitle() {
        rl_close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

        Gson gson = new Gson();
        BaseListBean<ActiveNoticeBean> bean = gson.fromJson(basebean_active, new TypeToken<BaseListBean<ActiveNoticeBean>>() {
        }.getType());

        if (bean.getStatus().equals("200")) {
            List<ActiveNoticeBean> listbean = bean.getObj();
            adapter.setRefrece(listbean);
        } else {
            ToastUtils.toast(ActiveActivity.this, bean.getMsg());
        }

    }
}








