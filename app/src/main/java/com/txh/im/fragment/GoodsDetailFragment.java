package com.txh.im.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.txh.im.R;
import com.txh.im.activity.GoodsDetailActivity;
import com.txh.im.bean.GoodsDetailBean;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * 商品详情界面--加载webview
 */
public class GoodsDetailFragment extends BasicFragment {

    private GoodsDetailActivity activity;
    private View rootView;
    private WebView webview;

    @Override
    protected View initView(LayoutInflater inflater) {
        rootView = inflater.inflate(R.layout.fragment_goods_detail, null);
        webview = (WebView) rootView.findViewById(R.id.webview);
        return rootView;
    }

    @Subscribe
    @Override
    protected void initTitle() {
        super.initTitle();
        EventBus.getDefault().register(this);
        // 设置默认缩放
        webview.setInitialScale(50);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);
    }

    @Override
    protected void initData() {

    }

    @Subscribe
    public void onEventMainThread(String event) {
        if (event != null) {
            updateView(event);
        }
    }

    private void updateView(String webview_str) {
        final StringBuffer temp = new StringBuffer();
//        temp.append("<meta name='viewport' content='width=320' />").append("<style> *{margin:0} </style>").append(webview_str);
        temp.append("<meta name='viewport' content='width=320' />").append("<div style='display:none'><style>*{padding:0;margin:0;display:block}</style></div>").append(webview_str);
        webview.loadDataWithBaseURL(null, temp + "<br/>", "text/html", "UTF-8", null);
        webview.getSettings().setDomStorageEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (GoodsDetailActivity) context;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

}