package com.txh.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.utils.TextUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForgetCompleteActivity extends BasicActivity implements View.OnClickListener {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_five_second)
    TextView tvFiveSecond;
    @Bind(R.id.btn_next)
    Button btnNext;

    private int recLen = 0;
    Timer timer = new Timer();
    private String WaitTimeForAutoLogin = "";
    private String chooseview = "";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_forget_complete);
        Intent intent = this.getIntent();
        WaitTimeForAutoLogin = intent.getStringExtra("WaitTimeForAutoLogin");
        chooseview = intent.getStringExtra("chooseview");
    }

    @Override
    protected void initTitle() {
        llHeadBack.setVisibility(View.GONE);

        if (chooseview.equals("reset")) {
            headTitle.setText("重置密码");
        }

        if (chooseview.equals("forget")) {
            headTitle.setText("忘记密码");
        }


    }

    @Override
    public void initData() {

        btnNext.setOnClickListener(this);
        // timeTask
        if (!TextUtil.isEmpty(WaitTimeForAutoLogin)) {
            recLen = Integer.parseInt(WaitTimeForAutoLogin);
            timer.schedule(task, 1000, 1000);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                timer.cancel();
                GlobalApplication.getInstance().clearUserData(ForgetCompleteActivity.this);
                Intent intent = new Intent(ForgetCompleteActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                // UI thread
                @Override
                public void run() {
                    recLen--;
                    if (recLen <= 1) {
                        timer.cancel();
                        Intent intent = new Intent(ForgetCompleteActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    tvFiveSecond.setText(recLen + " 秒后自动登录！");
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
