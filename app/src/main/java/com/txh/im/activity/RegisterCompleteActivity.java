package com.txh.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.utils.PicassoUtils;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.UIUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 注册完成，自动登录至主界面，或者进入登录界面进行用户名和密码的登录
 */
public class RegisterCompleteActivity extends BasicActivity implements View.OnClickListener {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.iv_code)
    ImageView ivCode;
    @Bind(R.id.tv_five_second)
    TextView tvFiveSecond;
    @Bind(R.id.btn_next)
    Button btnNext;
    private String choose_str = "";
    int val = UIUtils.dip2px(150);
    private int recLen = 0;
    Timer timer = new Timer();
    private String UserQRUrl = "";
    private String WaitTimeForAutoLogin = "";
    private String Result = "";
    private String UserToken = "";

    @Override
    protected void initTitle() {
        headTitle.setText("注册");
        llHeadBack.setVisibility(View.GONE);
        btnNext.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register_complete);
        choose_str = getIntent().getStringExtra("choose_str");
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
        UserQRUrl = intent.getStringExtra("UserQRUrl");
        WaitTimeForAutoLogin = intent.getStringExtra("WaitTimeForAutoLogin");
        Result = intent.getStringExtra("Result");
        UserToken = intent.getStringExtra("UserToken");
        setView();
    }

    private void setView() {
        if (!TextUtil.isEmpty(UserQRUrl)) {
            PicassoUtils.getDefault().CommonUrl(RegisterCompleteActivity.this, UserQRUrl, val, val, R.drawable.wode, ivCode);
        }

        if (!TextUtil.isEmpty(WaitTimeForAutoLogin)) {
            recLen = Integer.parseInt(WaitTimeForAutoLogin);
            startTime();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                timer.cancel();
                Intent intent = new Intent(RegisterCompleteActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    private void startTime() {
        timer.schedule(task, 1000, 1000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    recLen--;
                    if (recLen <= 1) {
                        timer.cancel();
                        Intent intent = new Intent(RegisterCompleteActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    tvFiveSecond.setText(recLen + " 秒后自动登录！");
                }
            });
        }
    };

}
