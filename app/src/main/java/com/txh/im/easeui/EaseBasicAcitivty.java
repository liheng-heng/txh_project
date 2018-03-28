package com.txh.im.easeui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.txh.im.android.GlobalApplication;

import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/5/25.
 */
@SuppressLint("Registered")
public abstract class EaseBasicAcitivty extends EaseBaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        GlobalApplication.getInstance().addActivity(this);
        initView();
        ButterKnife.bind(this);
        initTitle();
        initData();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    protected abstract void initView();

    protected abstract void initTitle();

    public abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        GlobalApplication.getInstance().removeActivity(this);
    }
}
