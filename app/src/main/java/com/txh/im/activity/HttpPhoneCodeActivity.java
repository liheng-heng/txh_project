package com.txh.im.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.Utils;
import com.txh.im.widget.CountDownButtonHelper;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 注册和忘记密码
 * 获取手机验证码，并校验手机号和验证码
 */
public class HttpPhoneCodeActivity extends BasicActivity implements View.OnClickListener {

    @Bind(R.id.tv_head_back)
    TextView cancel;
    @Bind(R.id.ll_head_back)
    LinearLayout back;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_new_pwd)
    TextView tvPhone;
    @Bind(R.id.rl_new_pwd)
    RelativeLayout rlPassword2;
    @Bind(R.id.tv_local_code)
    TextView tvLocalCode;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.tv_getcode)
    TextView tvGetcode;
    @Bind(R.id.ll_local_code)
    LinearLayout llLocalCode;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.activity_forget_password)
    RelativeLayout activityForgetPassword;
    @Bind(R.id.tv_check_desc)
    TextView tvCheckDesc;

    private String showview = "";
    private String phone_num = "";
    private String code_num = "";
    private boolean isEdit_phone = false;
    private boolean isEdit_local_code = false;
    private CountDownButtonHelper btnHelper;
    private ProgressDialog mProgressDialog;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_http_phone_code);
        Intent intent = getIntent();
        showview = intent.getStringExtra("showview");
    }

    @Override
    protected void initTitle() {
        cancel.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(showview) && showview.equals("rigister")) {
            headTitle.setText("注册");
        }

        if (!TextUtils.isEmpty(showview) && showview.equals("forgetpassword")) {
            headTitle.setText("忘记密码");
        }
    }

    @Override
    public void initData() {
        btnNext.setEnabled(false);
        tvGetcode.setClickable(false);
        btnNext.setOnClickListener(this);
        back.setOnClickListener(this);
        tvGetcode.setOnClickListener(this);
        etPhone.addTextChangedListener(mTextWatcher1);
        etCode.addTextChangedListener(mTextWatcher2);
        btnHelper = new CountDownButtonHelper(HttpPhoneCodeActivity.this, tvGetcode,
                getResources().getColor(R.color.white), getResources().getColor(R.color.blue), "重新发送", 59, 1, true);

    }

    TextWatcher mTextWatcher1 = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            code_num = etCode.getText().toString().trim();
            if (!TextUtil.isEmpty(code_num)) {
                etCode.setText("");
                btnHelper.finish("获取验证码");
            }

            editStart = etPhone.getSelectionStart();
            editEnd = etPhone.getSelectionEnd();

            if (temp.length() == 11) {
                tvGetcode.setBackground(getResources().getDrawable(R.drawable.bg_write_title_left_c));
                tvGetcode.setClickable(true);
                tvGetcode.setTextColor(getResources().getColor(R.color.blue));
                isEdit_phone = true;

            } else {
                tvGetcode.setBackground(getResources().getDrawable(R.drawable.shape_radius5_gray_2));
                tvGetcode.setClickable(false);
                tvGetcode.setTextColor(getResources().getColor(R.color.white));
                //登录按钮不可点击
                btnNext.setBackground(getResources().getDrawable(R.drawable.shape_radius5_gray));
                btnNext.setEnabled(false);
                isEdit_phone = false;
            }

            if (temp.length() > 11) {
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                etPhone.setText(s);
                etPhone.setSelection(tempSelection);
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
                isEdit_local_code = true;
            } else {
                isEdit_local_code = false;
            }

            if (isEdit_phone && isEdit_local_code) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                setResult(RESULT_OK);
                finish();
                break;

            case R.id.tv_getcode:
                phone_num = etPhone.getText().toString().trim();
                if (!Utils.isMobileNO_new(phone_num)) {
                    tvCheckDesc.setText("请核实输入手机号！");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                tvCheckDesc.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(showview) && showview.equals("rigister")) {
                    /**
                     * 获取验证码--注册
                     */
                    HttpCode_register(phone_num);
                    etCode.setText("");
                }

                if (!TextUtils.isEmpty(showview) && showview.equals("forgetpassword")) {
                    headTitle.setText("忘记密码");
                    /**
                     * 获取验证码--忘记密码
                     */
                    HttpCode_forget(phone_num);
                    etCode.setText("");
                }
                break;

            case R.id.btn_next:
                code_num = etCode.getText().toString().trim();
                phone_num = etPhone.getText().toString().trim();

                if (!Utils.isMobileNO_new(phone_num)) {
                    tvCheckDesc.setText("请核实输入手机号！");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                if (!TextUtils.isEmpty(showview) && showview.equals("rigister")) {
                    /**
                     * 校验验证码，--注册---确定没有问题之后进行页面的跳转
                     */
                    HttpCheck_Register_Code(code_num, phone_num);
                }

                if (!TextUtils.isEmpty(showview) && showview.equals("forgetpassword")) {
                    /**
                     * 校验验证码，--忘记密码---确定没有问题之后进行页面的跳转
                     */
                    HttpCheck_Forget_Code(code_num, phone_num);
                }
                break;
        }
    }

    /**
     * 校验---注册短信验证码
     *
     * @param code_num
     */
    private void HttpCheck_Register_Code(final String code_num, final String phone_num) {
        showProgress("加载中...");
        HashMap<String, String> map = new HashMap();
        map.put("MsgCode", code_num);
        map.put("Phone", phone_num);

        new GetNetUtil(map, Api.GetInfo_CheckMsgCodeForUserRegister, HttpPhoneCodeActivity.this) {

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
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(basebean, HttpBean.class);
                if (httpBean.getStatus().equals("200")) {
                    tvCheckDesc.setVisibility(View.GONE);
                    Log.i("----------->", "接口调用成功");
                    CryptonyBean cryptonyBean = httpBean.getObj();
                    if (null != cryptonyBean && !TextUtil.isEmpty(cryptonyBean.getResult())) {
                        switch (cryptonyBean.getResult()) {
                            case "1":
                                Intent intent = new Intent(HttpPhoneCodeActivity.this, Register_SetPasswordActivity.class);
                                intent.putExtra("MsgCode", code_num);
                                intent.putExtra("Phone", phone_num);
                                startActivity(intent);
                                break;

                            case "2":
                                Intent intent2 = new Intent(HttpPhoneCodeActivity.this, RegisterWarmActivity.class);
                                intent2.putExtra("phone", cryptonyBean.getPhone());
                                intent2.putExtra("imagesHead", cryptonyBean.getImagesHead());
                                startActivity(intent2);
                                break;
                        }
                    }
                } else {
                    tvCheckDesc.setText(httpBean.getMsg());
                    tvCheckDesc.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    /**
     * 校验---忘记密码短信验证码
     *
     * @param code_num
     */
    private void HttpCheck_Forget_Code(final String code_num, final String phone_num) {
        showProgress("加载中...");
        HashMap<String, String> map = new HashMap();
        map.put("ResetType", "NoLoginForgetLoginPwd");
        map.put("MsgCode", code_num);
        map.put("Phone", phone_num);

        new GetNetUtil(map, Api.GetInfo_CheckMsgCodeForSetLoginPwd, HttpPhoneCodeActivity.this) {

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
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(basebean, HttpBean.class);
                if (httpBean.getStatus().equals("200")) {
                    tvCheckDesc.setVisibility(View.GONE);
                    Log.i("----------->", "接口调用成功");
                    Intent intent = new Intent(HttpPhoneCodeActivity.this, Forget_SetNewPasswordActivity.class);
                    intent.putExtra("MsgCode", code_num);
                    intent.putExtra("Phone", phone_num);
                    startActivity(intent);
                } else {
                    tvCheckDesc.setText(httpBean.getMsg());
                    tvCheckDesc.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    /**
     * 获取短信验证码----忘记密码时
     *
     * @param phone_num
     */
    private void HttpCode_forget(String phone_num) {
        showProgress("验证码发送中");
        HashMap<String, String> map = new HashMap();
        map.put("OpType", "NoLoginForgetLoginPwd");
        map.put("Phone", phone_num);

        new GetNetUtil(map, Api.GetInfo_SendMsgCodeByType, HttpPhoneCodeActivity.this) {

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
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(basebean, HttpBean.class);

                if (httpBean.getStatus().equals("200")) {
                    Log.i("----------->", "接口调用成功");
                    tvCheckDesc.setVisibility(View.GONE);
                    btnHelper.start();
                } else {
                    tvCheckDesc.setText(httpBean.getMsg());
                    tvCheckDesc.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    /**
     * 获取短信验证码---注册时
     *
     * @param phone_num
     */

    private void HttpCode_register(String phone_num) {
        showProgress("验证码发送中");
        HashMap<String, String> map = new HashMap();
        map.put("OpType", "UserRegister");
        map.put("Phone", phone_num);

        new GetNetUtil(map, Api.GetInfo_SendMsgCodeByType, HttpPhoneCodeActivity.this) {

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
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(basebean, HttpBean.class);
                if (httpBean.getStatus().equals("200")) {
                    Log.i("----------->", "接口调用成功");
                    tvCheckDesc.setVisibility(View.GONE);
                    btnHelper.start();
                } else {
                    tvCheckDesc.setText(httpBean.getMsg());
                    tvCheckDesc.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    protected void showProgress(String showstr) {
        mProgressDialog = ProgressDialog.show(this, "", showstr);
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String trim = etCode.getText().toString().trim();
        if (!TextUtil.isEmpty(trim)) {
            etCode.setText("");
            btnHelper.finish("获取验证码");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
