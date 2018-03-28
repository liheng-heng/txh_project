package com.txh.im.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.presenter.HuanxinLoginPresenter;
import com.txh.im.presenter.impl.HuanxinLoginPresebterImpl;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.PicassoUtils;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.UIUtils;
import com.txh.im.utils.Utils;
import com.txh.im.view.HuanxinLoginView;
import com.txh.im.view.ValidationCode;
import com.txh.im.widget.RoundAngleImageView;

import java.util.HashMap;

import butterknife.Bind;

public class LoginActivity extends BasicActivity implements View.OnClickListener, HuanxinLoginView {

    @Bind(R.id.iv_head)
    RoundAngleImageView ivHead;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_old_pwd)
    ImageView ivPhone;
    @Bind(R.id.et_pwd1)
    EditText etPhone;
    @Bind(R.id.rl_old_pwd)
    RelativeLayout rlPhone;
    @Bind(R.id.tv_new_pwd)
    ImageView ivPasscodeCode;
    @Bind(R.id.et_phone)
    EditText etPassword;
    @Bind(R.id.iv_eye2)
    ImageView ivPasscodeEye;
    @Bind(R.id.rl_new_pwd)
    RelativeLayout rlPassword;
    @Bind(R.id.tv_local_code)
    TextView tvLocalCode;
    @Bind(R.id.et_code)
    EditText etLocalCode;
    @Bind(R.id.tv_getcode)
    ValidationCode etValidationCode;
    @Bind(R.id.ll_local_code)
    LinearLayout rlLocalCode;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.tv_forgetpassword)
    TextView tvForgetpassword;
    @Bind(R.id.tv_rigister)
    TextView tvRigister;
    @Bind(R.id.tv_check_desc)
    TextView tvCheckDesc;

    private String phone_str;
    private String password_str;
    private String localcode_str;
    private ProgressDialog mProgressDialog;
    private static boolean pstate = false; // 密码是否可见
    private boolean isEdit_phone = false;
    private boolean isEdit_password = false;
    private boolean isEdit_local_code = false;
    private HuanxinLoginPresenter loginPresenter;
    int val = UIUtils.dip2px(150);
    private String appLogo = "";
    private String appName = "";
    private static int register_for_result_code = 100;
    private static int forget_for_result_code = 200;

    @Override
    protected void beforeSetContentView() {
        super.beforeSetContentView();
        String userToken = GlobalApplication.getInstance().getPerson(LoginActivity.this).getUserToken();
        if (!TextUtil.isEmpty(userToken) && !userToken.equals("N")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login_new);
    }

    @Override
    public void initData() {
        loginPresenter = new HuanxinLoginPresebterImpl(this);
        tvRigister.setOnClickListener(this);
        tvForgetpassword.setOnClickListener(this);
        ivPasscodeEye.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        etPhone.addTextChangedListener(mTextWatcher1);
        etPassword.addTextChangedListener(mTextWatcher2);
        etLocalCode.addTextChangedListener(mTextWatcher3);
        btnNext.setEnabled(false);
        if (hasReadPhoneStatePermission()) {
            HttpViewData();
        } else {
            applyPhonePersion();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_rigister:
                /**
                 * 注册
                 */
                Intent intent = new Intent(LoginActivity.this, HttpPhoneCodeActivity.class);
                intent.putExtra("showview", "rigister");
                startActivityForResult(intent, register_for_result_code);
                Log.i("----------->", "点击注册");
                break;

            case R.id.tv_forgetpassword:
                /**
                 * 忘记密码
                 */
                Intent intent2 = new Intent(LoginActivity.this, HttpPhoneCodeActivity.class);
                intent2.putExtra("showview", "forgetpassword");
//                startActivity(intent2);
                startActivityForResult(intent2, forget_for_result_code);
                Log.i("----------->", "点击忘记密码");
                break;

            case R.id.iv_eye2:
                if (pstate) {
                    // 隐藏密码 修改图片背景
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pstate = false;
                    ivPasscodeEye.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshi));
                } else {
                    // 显示密码
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pstate = true;
                    ivPasscodeEye.setBackgroundDrawable(getResources().getDrawable(R.drawable.mimaxianshitubiao));
                }

                if (etPassword.getText().toString().trim().length() != 0) {
                    etPassword.setSelection(etPassword.getText().toString().trim().length());
                }
                break;

            case R.id.btn_next:
                Login();
                break;
        }
    }

    /**
     * 获取登录界面相关数据
     */
    private void HttpViewData() {
        showProgress("正在加载");
        new GetNetUtil(null, Api.GetInfo_GetAppDefaultData, this) {

            @Override
            public void errorHandle() {
                closeProgress();
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                Log.i("----------->", basebean);
                if (isDestroy){
                    return;
                }
                closeProgress();
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(basebean, HttpBean.class);

                if (httpBean.getStatus().equals("200")) {
//                    Log.i("----------->", "成功调用");
                    CryptonyBean cryptonyBean = httpBean.getObj();
                    updateLoginView(cryptonyBean);

                } else {
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    tvCheckDesc.setText(httpBean.getMsg());
                    etLocalCode.setText("");
                    etValidationCode.refresh();

                }
            }
        };
    }

    private void updateLoginView(CryptonyBean cryptonyBean) {
        appLogo = cryptonyBean.getAPPLogo();
        appName = cryptonyBean.getAPPName();

        if (!TextUtil.isEmpty(appLogo)) {
            PicassoUtils.getDefault().CommonUrl(LoginActivity.this, appLogo, val, val, R.drawable.wode, ivHead);
        }

        if (!TextUtil.isEmpty(appName)) {
            tvTitle.setText(appName);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                clearViewData();
                break;

            case 200:
                clearViewData();
                break;

        }
    }

    private void clearViewData() {
        /**
         * 清空登录界面的数据
         */
        phone_str = etPhone.getText().toString().trim();
        password_str = etPassword.getText().toString().trim();
        localcode_str = etLocalCode.getText().toString().trim();

        if (!TextUtil.isEmpty(phone_str)) {
            etPhone.setText("");
        }

        if (!TextUtil.isEmpty(password_str)) {
            etPassword.setText("");
        }

        if (!TextUtil.isEmpty(localcode_str)) {
            etLocalCode.setText("");
        }
        etValidationCode.refresh();
        tvCheckDesc.setVisibility(View.GONE);

    }

    private void Login() {
        phone_str = etPhone.getText().toString().toString();
        password_str = etPassword.getText().toString().trim();
        if (!Utils.isMobileNO_new(phone_str)) {
            tvCheckDesc.setVisibility(View.VISIBLE);
            tvCheckDesc.setText(R.string.warm_phonenum);
            etLocalCode.setText("");
            etValidationCode.refresh();
            return;
        }

        if (!etValidationCode.isEqualsIgnoreCase(etLocalCode.getText().toString().trim())) {
            tvCheckDesc.setVisibility(View.VISIBLE);
            tvCheckDesc.setText(R.string.warm_code);
            etLocalCode.setText("");
            etValidationCode.refresh();
            return;
        }

        tvCheckDesc.setVisibility(View.GONE);
        boolean networkAvailable = Utils.isNetworkAvailable(this);
        if (networkAvailable) {
            showProgress("正在登录");
            HashMap<String, String> map = new HashMap<>();
            map.put("Phone", phone_str);
            map.put("Pwd", Utils.str2MD5(password_str));
            new GetNetUtil(map, Api.GetInfo_UserPwdLogin, this) {

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
//                        Log.i("----------->", "成功调用");
                        CryptonyBean cryptonyBean = httpBean.getObj();
                        if (null != cryptonyBean) {
                            GlobalApplication.getInstance().savePerson(cryptonyBean, LoginActivity.this);
                        }
//                        登陆环信
                        Log.i("jiajiaim",cryptonyBean.getUserId());
                        login(cryptonyBean.getUserId(), cryptonyBean.getHXIMPwd());

                    } else {

                        tvCheckDesc.setVisibility(View.VISIBLE);
                        tvCheckDesc.setText(httpBean.getMsg());
                        etLocalCode.setText("");
                        etValidationCode.refresh();

                    }
                }
            };
        } else {
//            ToastUtils.showToast(this, R.string.warm_net);
            tvCheckDesc.setVisibility(View.VISIBLE);
            tvCheckDesc.setText(R.string.warm_net);
            etLocalCode.setText("");
            etValidationCode.refresh();
            closeProgress();
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
                isEdit_phone = true;
            } else {
                isEdit_phone = false;
            }

            if (isEdit_phone && isEdit_password && isEdit_local_code) {
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
                isEdit_password = true;
            } else {
                isEdit_password = false;
            }

            if (isEdit_phone && isEdit_password && isEdit_local_code) {
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
                isEdit_local_code = true;
            } else {
                isEdit_local_code = false;
            }

            if (isEdit_phone && isEdit_password && isEdit_local_code) {
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GlobalApplication.getInstance().exitWithSingleClick();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    protected void showProgress(String showstr) {
        if (!this.isFinishing()){//jiajia,4.7,防止activity已不存在还会打开dialog
            mProgressDialog = ProgressDialog.show(this, "", showstr);
        }
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onLoginSuccess() {
        closeProgress();
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onLoginFail() {
        closeProgress();
        toast("登陆失败");
    }

    @Override
    public void onStartLogin() {

    }

    private void login(String usename, String pwd) {
        loginPresenter.login(usename, pwd);
    }
}