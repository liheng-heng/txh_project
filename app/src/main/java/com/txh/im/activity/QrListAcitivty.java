package com.txh.im.activity;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.R;
import com.txh.im.adapter.QrListAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.UserInShopBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class QrListAcitivty extends BasicActivity {

    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.lv)
    ListView lv;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_qr_list_acitivty);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("我的二维码");
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        Gson gson = new Gson();
        String qrlist = intent.getStringExtra("qrlist");
        List<UserInShopBean> list = gson.fromJson(qrlist,new TypeToken<ArrayList<UserInShopBean>>(){}.getType());
        QrListAdapter qrAdapter = new QrListAdapter(this,list);
        lv.setAdapter(qrAdapter);
    }

    @OnClick(R.id.ll_head_back)
    public void onClick() {
        finish();
    }
}
