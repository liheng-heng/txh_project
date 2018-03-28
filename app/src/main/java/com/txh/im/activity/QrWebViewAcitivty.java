package com.txh.im.activity;

import android.content.Intent;
import android.webkit.WebView;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.base.BasicActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/5/19.
 */

public class QrWebViewAcitivty extends BasicActivity {
    @Bind(R.id.wv)
    WebView wv;
    @Bind(R.id.head_title)
    TextView headTitle;

    @Override
    protected void initView() {
        setContentView(R.layout.head_webview);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("天下货APP下载");
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String app_qr = intent.getStringExtra("app_qr");
        //设置WebView属性，能够执行Javascript脚本
        wv.getSettings().setJavaScriptEnabled(true);
//        double i = Math.random();
        wv.loadUrl(app_qr + "?num=" + System.currentTimeMillis());
    }

    @OnClick(R.id.ll_head_back)
    public void onClick() {
        finish();
    }
}
