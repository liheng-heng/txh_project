package com.txh.im.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.HttpBean;
import com.txh.im.utils.DialogHelp;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.Utils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 重设密码
 */
public class PasswordResetActivity extends BasicActivity implements View.OnClickListener {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_check_desc)
    TextView tvCheckDesc;

    @Bind(R.id.et_pwd1)
    EditText etPwd1;
    @Bind(R.id.et_phone)
    EditText etPwd2;
    @Bind(R.id.et_pwd3)
    EditText etPwd3;

    @Bind(R.id.iv_eye1)
    ImageView ivEye1;
    @Bind(R.id.iv_eye2)
    ImageView ivEye2;
    @Bind(R.id.iv_eye3)
    ImageView ivEye3;

    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.btn_next)
    Button btnNext;

    private String pwd1 = "";
    private String pwd2 = "";
    private String pwd3 = "";

    private String pwdstrnokong1 = "";
    private String pwdstrnokong2 = "";
    private String pwdstrnokong3 = "";

    private static boolean pstate1 = false;
    private static boolean pstate2 = false;
    private static boolean pstate3 = false;

    private boolean isEdit_pwd1 = false;
    private boolean isEdit_pwd2 = false;
    private boolean isEdit_pwd3 = false;

    @Override
    protected void initTitle() {
        headTitle.setText("重置密码");
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_password_reset);
    }

    @Override
    public void initData() {
        llHeadBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnNext.setEnabled(false);
        ivEye1.setOnClickListener(this);
        ivEye2.setOnClickListener(this);
        ivEye3.setOnClickListener(this);
        etPwd1.addTextChangedListener(mTextWatcher1);
        etPwd2.addTextChangedListener(mTextWatcher2);
        etPwd3.addTextChangedListener(mTextWatcher3);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_head_back:
                finish();
                break;

            case R.id.btn_next:
                pwd1 = etPwd1.getText().toString().trim();
                pwd2 = etPwd2.getText().toString().trim();
                pwd3 = etPwd3.getText().toString().trim();
                pwdstrnokong1 = pwd1.replaceAll(" ", "");
                pwdstrnokong2 = pwd2.replaceAll(" ", "");
                pwdstrnokong3 = pwd3.replaceAll(" ", "");

                if (TextUtil.isContainCH_OR_SC(pwdstrnokong2)) {
                    tvCheckDesc.setText("密码格式不正确，请检查！");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                if (pwd2.length() < 6) {
                    tvCheckDesc.setText("密码不得少于6位");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                if (pwd2.length() > 20) {
                    tvCheckDesc.setText("密码不得超过20位");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                if (!pwdstrnokong2.equals(pwdstrnokong3)) {
                    tvCheckDesc.setText("两次输入密码不一致！");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                tvCheckDesc.setVisibility(View.GONE);
                HttpResetPassword(pwd1, pwd2);

                break;

            case R.id.iv_eye1:
                if (pstate1) {
                    // 隐藏密码
                    etPwd1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pstate1 = false;
                    ivEye1.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshi));
                } else {
                    // 显示密码
                    etPwd1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pstate1 = true;
                    ivEye1.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshitubiao));
                }
                if (etPwd1.getText().toString().trim().length() != 0) {
                    etPwd1.setSelection(etPwd1.getText().toString().trim().length());
                }

                break;

            case R.id.iv_eye2:

                if (pstate2) {
                    // 隐藏密码 修改图片背景
                    etPwd2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pstate2 = false;
                    ivEye2.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshi));

                } else {
                    // 显示密码
                    etPwd2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pstate2 = true;
                    ivEye2.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshitubiao));

                }
                if (etPwd2.getText().toString().trim().length() != 0) {
                    etPwd2.setSelection(etPwd2.getText().toString().trim().length());
                }

                break;

            case R.id.iv_eye3:

                if (pstate3) {
                    // 隐藏密码
                    etPwd3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pstate3 = false;
                    ivEye3.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshi));
                } else {
                    // 显示密码
                    etPwd3.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pstate3 = true;
                    ivEye3.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshitubiao));
                }

                if (etPwd3.getText().toString().trim().length() != 0) {
                    etPwd3.setSelection(etPwd3.getText().toString().trim().length());
                }

                break;
        }
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

            if (isEdit_pwd1 && isEdit_pwd2 && isEdit_pwd3) {
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

            if (isEdit_pwd1 && isEdit_pwd2 && isEdit_pwd3) {
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


    TextWatcher mTextWatcher3 = new TextWatcher() {
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
                isEdit_pwd3 = true;
            } else {
                isEdit_pwd3 = false;
            }

            if (isEdit_pwd1 && isEdit_pwd2 && isEdit_pwd3) {
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

    private void HttpResetPassword(String old_str, String new_str) {
        showProgress("加载中...");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Pwd", Utils.str2MD5(old_str));
        hashMap.put("NewPwd", Utils.str2MD5(new_str));

        new GetNetUtil(hashMap, Api.GetInfo_ChangeLoginPwdByOld, PasswordResetActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy) {
                    return;
                }
                closeProgress();
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(basebean, HttpBean.class);
                GlobalApplication.getInstance().savePerson(httpBean.getObj(), PasswordResetActivity.this);
                if (httpBean.getStatus().equals("200")) {
                    Intent intent = new Intent(PasswordResetActivity.this, ForgetCompleteActivity.class);
                    intent.putExtra("WaitTimeForAutoLogin", httpBean.getObj().getWaitTimeForAutoLogin());
                    intent.putExtra("chooseview", "reset");
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

    private ProgressDialog mProgressDialog;

    protected void showProgress(String showstr) {
        mProgressDialog = ProgressDialog.show(this, "", showstr);
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showCancleDialog(String message) {
        AlertDialog.Builder a = DialogHelp.getConfirmDialog(PasswordResetActivity.this, message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        a.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
