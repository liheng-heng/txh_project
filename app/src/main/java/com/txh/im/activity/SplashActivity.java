package com.txh.im.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.SplashBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.utils.Utils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liheng on 2017/2/21.
 */
public class SplashActivity extends Activity {
    @Bind(R.id.tv_skip_time)
    TextView tvSkipTime;
    @Bind(R.id.ll_skip_layout)
    LinearLayout llSkipLayout;
    @Bind(R.id.rl_webview_skip_layout)
    RelativeLayout rlWebviewSkipLayout;
    private boolean had_in = false;
    private WebView webview;
    private static final int REQUEST_READ_PHONE_STATE = 1;

    /**
     * 自己计时器---1
     */
    private int recLen_1 = 3;
    Timer timer_1 = new Timer();

    /**
     * 加载网络数据---2
     */
    private int recLen_2 = 0;
    Timer timer_2 = new Timer();

    /**
     * 默认---3
     */
    private int recLen_3 = 3;
    Timer timer_3 = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = UIUtils.inflate(this, R.layout.activity_splash_new);
        setContentView(view);
        ButterKnife.bind(this);
        webview = (WebView) findViewById(R.id.webview);
        // 设置默认缩放
        webview.setInitialScale(50);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);
        GlobalApplication.getInstance().addActivity(this);
        had_in = (Boolean) SPUtil.get(SplashActivity.this, "once", false);

        if (hasReadPhoneStatePermission()) {
            if (had_in) {
                HttpData();
                Log.i("---->>>>", "had_in + 有权限");
            } else {
                /**
                 * 定义自己的计时器
                 */
                startTime_1();
                tvSkipTime.setVisibility(View.GONE);
                webview.setVisibility(View.GONE);
                Log.i("---->>>>", "第一次 + 有权限");
            }
        } else {
            applyPhonePersion();
            Log.i("---->>>>", "没权限权限");
            startTime_3();
        }

        llSkipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectTo();
                timer_2.cancel();
            }
        });

    }

    /**
     * 第一次自己定义计时器
     */
    private void startTime_1() {
        timer_1.schedule(task_1, 1000, 1000);
    }

    TimerTask task_1 = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    recLen_1--;
                    if (recLen_1 <= 1) {
                        timer_1.cancel();
                        redirectTo();
                    }
                }
            });
        }
    };


    /**
     * 网络加载计时器
     */
    private void startTime_2() {
        timer_2.schedule(task_2, 1000, 1000);
    }

    TimerTask task_2 = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    tvSkipTime.setVisibility(View.VISIBLE);

                    if (recLen_2 <= 1) {
                        timer_2.cancel();
                        redirectTo();
                    }
                    tvSkipTime.setText("跳过 " + recLen_2);
                    recLen_2--;
                }
            });
        }
    };

    /**
     * 没有权限进行页面跳转
     */
    private void startTime_3() {
        timer_3.schedule(task_3, 1000, 1000);
    }

    TimerTask task_3 = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    recLen_3--;
                    if (recLen_3 <= 1) {
                        timer_3.cancel();
                        redirectTo();
                    }
                }
            });
        }
    };


    /**
     * 请求启动页数据以及倒计时时间
     */
    private void HttpData() {

        if (Utils.isNetworkAvailable(SplashActivity.this)) {
            HashMap<String, String> hashMap = new HashMap<>();
            new GetNetUtil(hashMap, Api.AppSys_GetStartPageInfo, SplashActivity.this) {
                @Override
                public void errorHandle() {
                    super.errorHandle();
                }

                @Override
                public void successHandle(String basebean) {
                    if (SplashActivity.this.isFinishing()){
                        return;
                    }
                    Log.i("---->>", "basebean----" + basebean);
                    Gson gson = new Gson();
                    Basebean<SplashBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<SplashBean>>() {
                    }.getType());
                    if (bean.getStatus().equals("200")) {
                        webview.setVisibility(View.VISIBLE);
                        if (!TextUtil.isEmpty(bean.getObj().getObjUrl())) {
                            webview.loadUrl(bean.getObj().getObjUrl());
                        }
                        if (!TextUtil.isEmpty(bean.getObj().getWaitSecond())) {
                            if (bean.getObj().getWaitSecond().equals("0")) {
                                redirectTo();
                            } else {
                                recLen_2 = Integer.parseInt(bean.getObj().getWaitSecond());
                                startTime_2();
                            }
                        }
                    } else {
                        ToastUtils.showToast(SplashActivity.this, bean.getMsg());
                    }
                }
            };
        } else {
            ToastUtils.toast(SplashActivity.this, "您的网络开小差啦");
        }
    }

    private void redirectTo() {
        if (had_in) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity_new.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, GuidepageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void applyPhonePersion() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
    }

    protected boolean hasReadPhoneStatePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PermissionChecker.PERMISSION_GRANTED;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalApplication.getInstance().removeActivity(this);
    }
}

