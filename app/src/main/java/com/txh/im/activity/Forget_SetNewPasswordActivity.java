package com.txh.im.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.HttpBean;
import com.txh.im.utils.DialogHelp;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.Utils;

import java.util.HashMap;

import butterknife.Bind;

/**
 * 忘记密码---密码重置界面
 */
public class Forget_SetNewPasswordActivity extends BasicActivity implements View.OnClickListener {
    @Bind(R.id.tv_head_back)
    TextView cancel;
    @Bind(R.id.ll_head_back)
    LinearLayout back;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_old_pwd)
    TextView ivPhone;
    @Bind(R.id.et_pwd1)
    EditText etPassword1;
    @Bind(R.id.iv_eye1)
    ImageView ivEye1;
    @Bind(R.id.rl_old_pwd)
    RelativeLayout rlPwd1;
    @Bind(R.id.tv_new_pwd)
    TextView tvPhone;
    @Bind(R.id.et_phone)
    EditText etPassword2;
    @Bind(R.id.iv_eye2)
    ImageView ivEye2;
    @Bind(R.id.rl_new_pwd)
    RelativeLayout rlPassword2;
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.tv_check_desc)
    TextView tvCheckDesc;

    private String MsgCode = "";
    private String Phone = "";
    private static boolean pstate1 = false;
    private static boolean pstate2 = false;
    private String pwd1 = "";
    private String pwd2 = "";
    private boolean isEdit_pwd1 = false;
    private boolean isEdit_pwd2 = false;
    private String pwdstrnokong1 = "";
    private String pwdstrnokong2 = "";


    @Override
    protected void initTitle() {
        cancel.setVisibility(View.INVISIBLE);
        headTitle.setText("忘记密码");
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_set_password);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        MsgCode = intent.getStringExtra("MsgCode");
        Phone = intent.getStringExtra("Phone");
        back.setOnClickListener(this);
        ivEye1.setOnClickListener(this);
        ivEye2.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        etPassword1.addTextChangedListener(mTextWatcher1);
        etPassword2.addTextChangedListener(mTextWatcher2);
        btnNext.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_next:
                pwd1 = etPassword1.getText().toString().trim();
                pwd2 = etPassword2.getText().toString().trim();
                pwdstrnokong1 = pwd1.replaceAll(" ", "");
                pwdstrnokong2 = pwd2.replaceAll(" ", "");

                if (TextUtil.isContainCH_OR_SC(pwdstrnokong1)) {
                    tvCheckDesc.setText("密码格式不正确，请检查！");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                if (pwd1.length() < 6) {
                    tvCheckDesc.setText("密码不得少于6位");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                if (pwd1.length() > 20) {
                    tvCheckDesc.setText("密码不得超过20位");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                if (!pwdstrnokong1.equals(pwdstrnokong2)) {
                    tvCheckDesc.setText("两次输入密码不一致！");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                tvCheckDesc.setVisibility(View.GONE);
                HttpResetPassword();

                break;

            case R.id.ll_head_back:
                finish();
                break;
            case R.id.iv_eye1:
                if (pstate1) {
                    // 隐藏密码 修改图片背景
                    etPassword1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pstate1 = false;
                    ivEye1.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshi));
                } else {
                    // 显示密码
                    etPassword1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pstate1 = true;
                    ivEye1.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshitubiao));
                }
                if (etPassword1.getText().toString().trim().length() != 0) {
                    etPassword1.setSelection(etPassword1.getText().toString().trim().length());
                }
                break;

            case R.id.iv_eye2:
                if (pstate2) {
                    // 隐藏密码 修改图片背景
                    etPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pstate2 = false;
                    ivEye2.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshi));

                } else {
                    // 显示密码
                    etPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pstate2 = true;
                    ivEye2.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshitubiao));

                }
                if (etPassword2.getText().toString().trim().length() != 0) {
                    etPassword2.setSelection(etPassword2.getText().toString().trim().length());
                }
                break;
        }
    }

    private void HttpResetPassword() {
        showProgress("加载中...");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ResetType", "NoLoginForgetLoginPwd");
        hashMap.put("Phone", Phone);
        hashMap.put("MsgCode", MsgCode);
        hashMap.put("Pwd", Utils.str2MD5(pwdstrnokong1));

        new GetNetUtil(hashMap, Api.GetInfo_SetLoginPwdByMsgCode, Forget_SetNewPasswordActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy){
                    return;
                }
                closeProgress();
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(basebean, HttpBean.class);
                if (httpBean.getStatus().equals("200")) {
                    Intent intent = new Intent(Forget_SetNewPasswordActivity.this, ForgetCompleteActivity.class);
                    intent.putExtra("chooseview", "forget");
                    intent.putExtra("WaitTimeForAutoLogin", httpBean.getObj().getWaitTimeForAutoLogin());
                    startActivity(intent);
                    tvCheckDesc.setVisibility(View.GONE);
                    finish();
                } else {
                    tvCheckDesc.setText(httpBean.getMsg());
                    tvCheckDesc.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    TextWatcher mTextWatcher1 = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (temp.length() > 0) {
                isEdit_pwd1 = true;
            } else {
                isEdit_pwd1 = false;
            }

            if (isEdit_pwd1 && isEdit_pwd2) {
                //登录按钮可点击
                btnNext.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
                btnNext.setEnabled(true);
            } else {
                //登录按钮不可点击
                btnNext.setBackground(getResources().getDrawable(R.drawable.shape_radius5_gray));
                btnNext.setEnabled(false);
            }

        }
    };


    TextWatcher mTextWatcher2 = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (temp.length() > 0) {
                isEdit_pwd2 = true;
            } else {
                isEdit_pwd2 = false;
            }

            if (isEdit_pwd1 && isEdit_pwd2) {
                //登录按钮可点击
                btnNext.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
                btnNext.setEnabled(true);
            } else {
                //登录按钮不可点击
                btnNext.setBackground(getResources().getDrawable(R.drawable.shape_radius5_gray));
                btnNext.setEnabled(false);
            }

        }
    };

    private void showCancleDialog(String message) {
        AlertDialog.Builder a = DialogHelp.getConfirmDialog(Forget_SetNewPasswordActivity.this, message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        a.show();
    }

    private ProgressDialog mProgressDialog;

    protected void showProgress(String showstr) {
        mProgressDialog = ProgressDialog.show(this, "", showstr);
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
