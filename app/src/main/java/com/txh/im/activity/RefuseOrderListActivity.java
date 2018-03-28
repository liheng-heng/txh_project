package com.txh.im.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liheng on 2017/5/18.
 */

public class RefuseOrderListActivity extends BasicActivity {

    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.ll_title_layout)
    LinearLayout llTitleLayout;
    @Bind(R.id.edittext)
    EditText edittext;
    @Bind(R.id.tv_ok)
    TextView tvOk;
    @Bind(R.id.ll_ok_layout)
    LinearLayout llOkLayout;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_refuse_orderlist);
    }

    @Override
    protected void initTitle() {


    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_title_layout, R.id.ll_ok_layout})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.ll_title_layout:
                finish();
                break;

            case R.id.ll_ok_layout:
                finish();
                ToastUtils.toast(this, "确定");
                break;

        }
    }
}
