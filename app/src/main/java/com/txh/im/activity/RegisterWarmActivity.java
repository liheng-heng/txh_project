package com.txh.im.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.utils.DialogHelp;
import com.txh.im.utils.PicassoUtils;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.UIUtils;

import butterknife.Bind;

public class RegisterWarmActivity extends BasicActivity implements View.OnClickListener {

    @Bind(R.id.tv_head_back)
    TextView cancel;
    @Bind(R.id.ll_head_back)
    LinearLayout back;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.tv_new_pwd)
    TextView tvPhone;
    @Bind(R.id.rl_class_brand_layout)
    LinearLayout llLayout;
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.btn_1)
    Button btn1;
    @Bind(R.id.btn_2)
    Button btn2;
    @Bind(R.id.activity_forget_password)
    RelativeLayout activityForgetPassword;

    private String phone = "";
    private String imagesHead = "";
    int val = UIUtils.dip2px(120);

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register_warm);
    }

    @Override
    protected void initTitle() {
        cancel.setVisibility(View.INVISIBLE);
        headTitle.setText("注册");
    }

    @Override
    public void initData() {
        phone = getIntent().getStringExtra("phone");
        imagesHead = getIntent().getStringExtra("imagesHead");
        Log.i("------->", phone);
        back.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        tvPhone.setText(phone);
        tvDesc.setText("手机号" + phone + "已经被以上用户使用，请确认该账户为本人所有");
        if (!TextUtil.isEmpty(imagesHead)) {
            PicassoUtils.getDefault().CommonUrl(RegisterWarmActivity.this, imagesHead, val, val, R.drawable.wode, ivHead);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;

            case R.id.btn_1:
                //立即登录
                Intent intent = new Intent(RegisterWarmActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_2:
                //换个手机号重新注册
//                Intent intent2 = new Intent(RegisterWarmActivity.this, HttpPhoneCodeActivity.class);
//                intent2.putExtra("showview", "rigister");
//                startActivity(intent2);
                finish();
                break;

        }
    }

    private void showCancleDialog(String message) {
        android.support.v7.app.AlertDialog.Builder a = DialogHelp.getConfirmDialog(RegisterWarmActivity.this, message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        a.show();
    }

}
