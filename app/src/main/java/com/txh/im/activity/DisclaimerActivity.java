package com.txh.im.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.DisclaimerBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;

import java.util.HashMap;

/**
 * Created by liheng on 2017/4/26.
 */

public class DisclaimerActivity extends BasicActivity {

    private WebView webview;
    private TextView headTitle;
    private LinearLayout llHeadBack;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_disclaimer);
        webview = (WebView) findViewById(R.id.webview);
        headTitle = (TextView) findViewById(R.id.head_title);
        llHeadBack = (LinearLayout) findViewById(R.id.ll_head_back);
        llHeadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initTitle() {
        headTitle.setText("服务声明");
        // 设置默认缩放
        webview.setInitialScale(50);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);
    }

    @Override
    public void initData() {
        HttpData();
    }

    private void HttpData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("AgreementType", "1");
        new GetNetUtil(hashMap, Api.GetInfo_GetAgreementByType, DisclaimerActivity.this) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy){
                    return;
                }
                Gson gson = new Gson();
                Basebean<DisclaimerBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<DisclaimerBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    updateView(bean.getObj().getAgreementContent());
                } else {
                    ToastUtils.showToast(DisclaimerActivity.this, bean.getMsg());
                }
            }
        };
    }

    private void updateView(String webview_str) {
        final StringBuffer temp = new StringBuffer();
        temp.append("<meta name='viewport' content='width=320' />").append(webview_str);
        webview.loadDataWithBaseURL(null, temp + "<br/>", "text/html", "UTF-8", null);
        webview.getSettings().setDomStorageEnabled(true);
    }
}
