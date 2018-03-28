package com.txh.im.activity;

import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.base.BasicActivity;

/**
 * Created by liheng on 2017/5/6.
 */
public class ActiveRuleActivity extends BasicActivity {

    private RelativeLayout rl_close_layout;
    private String web_str;
    private TextView tv_textview;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_active_rule);
        rl_close_layout = (RelativeLayout) findViewById(R.id.rl_close_layout);
        tv_textview = (TextView) findViewById(R.id.tv_textview);
        web_str = getIntent().getStringExtra("web_str");

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
        tv_textview.setText(Html.fromHtml(web_str));
    }
}








