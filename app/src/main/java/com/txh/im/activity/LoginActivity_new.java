package com.txh.im.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
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
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.easeui.DemoHelper;
import com.txh.im.easeui.PermissionsManager;
import com.txh.im.easeui.PermissionsResultAction;
import com.txh.im.easeui.db.DemoDBManager;
import com.txh.im.presenter.HuanxinLoginPresenter;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.PicassoUtils;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ThreadUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.utils.Utils;
import com.txh.im.view.HuanxinLoginView;
import com.txh.im.widget.CircleImageView;
import com.txh.im.widget.CountDownButtonHelper;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 新版登录界面
 * 只能通过短信验证码登录
 */
public class LoginActivity_new extends BasicActivity implements HuanxinLoginView {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_check_desc)
    TextView tvCheckDesc;
    @Bind(R.id.ll_head_layout)
    LinearLayout llHeadLayout;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_new_pwd)
    TextView tvNewPwd;
    @Bind(R.id.rl_new_pwd)
    RelativeLayout rlNewPwd;
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
    @Bind(R.id.iv_select)
    ImageView ivSelect;
    @Bind(R.id.tv_disclaimer)
    TextView tvDisclaimer;
    @Bind(R.id.ll_ivselect_layout)
    LinearLayout llIvselectLayout;
    @Bind(R.id.touxiang)
    CircleImageView touxiang;

    private String phone_str;
    private String code_num = "";
    private ProgressDialog mProgressDialog;
    private boolean isEdit_phone = false;
    private boolean isEdit_code = false;
    private boolean isImageViewSelect = true;
    private HuanxinLoginPresenter loginPresenter;
    int val = UIUtils.dip2px(150);
    private String appLogo = "";
    private String appName = "";
    private CountDownButtonHelper btnHelper;

    private CryptonyBean cryptonyBean;

    @Override
    protected void beforeSetContentView() {
        super.beforeSetContentView();
        String userToken = GlobalApplication.getInstance().getPerson(LoginActivity_new.this).getUserToken();
        if (!TextUtil.isEmpty(userToken) && !userToken.equals("N")) {
            Intent intent = new Intent(LoginActivity_new.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login_new2);
    }

    @Override
    public void initData() {
        //loginPresenter = new HuanxinLoginPresebterImpl(this);
        etPhone.addTextChangedListener(mTextWatcher1);
        etCode.addTextChangedListener(mTextWatcher2);
        btnNext.setEnabled(false);
        btnHelper = new CountDownButtonHelper(LoginActivity_new.this, tvGetcode,
                getResources().getColor(R.color.white), getResources().getColor(R.color.blue), "重新发送", 59, 1, true);
        if (hasReadPhoneStatePermission()) {
            HttpViewData();
        } else {
            applyPhonePersion();
        }
        requestPermissions();

    }

    /**
     * 获取登录界面相关数据
     */
    private void HttpViewData() {

        new GetNetUtil(null, Api.GetInfo_GetAppDefaultData, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy) {
                    return;
                }
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(basebean, HttpBean.class);
                if (httpBean.getStatus().equals("200")) {
                    CryptonyBean cryptonyBean = httpBean.getObj();
                    updateLoginView(cryptonyBean);
                } else {
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    tvCheckDesc.setText(httpBean.getMsg());
                }
            }
        };
    }

    private void updateLoginView(CryptonyBean cryptonyBean) {
        appLogo = cryptonyBean.getAPPLogo();
        appName = cryptonyBean.getAPPName();

        if (!TextUtil.isEmpty(appLogo)) {
            PicassoUtils.getDefault().CommonUrl(LoginActivity_new.this, appLogo, val, val, R.drawable.default_good, touxiang);
        }

        if (!TextUtil.isEmpty(appName)) {
            tvTitle.setText(appName);
        }
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

            if (temp.length() >= 1) {

                if (!btnHelper.isTimeInIntter()) {
                    tvGetcode.setBackground(getResources().getDrawable(R.drawable.bg_write_title_left_c));
                    tvGetcode.setClickable(true);
                    tvGetcode.setTextColor(getResources().getColor(R.color.blue));
                }
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
                isEdit_code = true;
            } else {
                isEdit_code = false;
            }

            if (isEdit_phone && isEdit_code && isImageViewSelect) {
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
        String trim = etCode.getText().toString().trim();
        if (!TextUtil.isEmpty(trim)) {
            etCode.setText("");
            btnHelper.finish("获取验证码");
        }
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
        //jiajia,4.7,防止activity已不存在还会打开dialog
        if (!this.isFinishing()) {
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
        if (null != cryptonyBean) {
            GlobalApplication.getInstance().savePerson(cryptonyBean, LoginActivity_new.this);
        }
        Intent i = new Intent(LoginActivity_new.this, MainActivity.class);
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

    private void login(final String usename, final String pwd) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
                // close it before login to make sure DemoDB not overlap
                DemoDBManager.getInstance().closeDB();

                // reset current user name before login
                DemoHelper.getInstance().setCurrentUserName(usename);
                EMClient.getInstance().login(usename, pwd, new EMCallBack() {

                    @Override
                    public void onSuccess() {

                        // ** manually load all local groups and conversation
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // get user's info (this should be get from App's server or 3rd party service)
                        DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }
//        loginPresenter.login(usename, pwd);

    @OnClick({R.id.tv_getcode, R.id.btn_next, R.id.ll_ivselect_layout, R.id.tv_disclaimer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode:
                phone_str = etPhone.getText().toString().trim();
                if (TextUtil.isEmpty(phone_str)) {
                    tvCheckDesc.setText("请核实输入手机号！");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }

                if (!Utils.isMobileNO_new(phone_str)) {
                    tvCheckDesc.setText("请核实输入手机号！");
                    tvCheckDesc.setVisibility(View.VISIBLE);
                    return;
                }
                tvCheckDesc.setVisibility(View.GONE);
                HttpMessageCode(phone_str);
                break;

            case R.id.btn_next:
                /**
                 * 先验证验证码的正确与否----正确之后--跳转主界面
                 */
                Login();
                break;

            case R.id.ll_ivselect_layout:
                if (isImageViewSelect) {
                    ivSelect.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_not_selected));
                    isImageViewSelect = false;
                } else {
                    ivSelect.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_select));
                    isImageViewSelect = true;
                }

                if (isEdit_phone && isEdit_code && isImageViewSelect) {
                    //登录按钮可点击
                    btnNext.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
                    btnNext.setEnabled(true);
                } else {
                    //登录按钮不可点击
                    btnNext.setBackground(getResources().getDrawable(R.drawable.shape_radius5_gray));
                    btnNext.setEnabled(false);
                }
                break;

            case R.id.tv_disclaimer:
                startActivity(new Intent(LoginActivity_new.this, DisclaimerActivity.class));
                break;
        }
    }

    /**
     * 通过短信验证码登登录
     */
    private void Login() {
        phone_str = etPhone.getText().toString().toString();
        code_num = etCode.getText().toString().trim();
        if (TextUtil.isEmpty(phone_str)) {
            tvCheckDesc.setVisibility(View.VISIBLE);
            tvCheckDesc.setText(R.string.warm_phonenum);
            return;
        }

        if (TextUtil.isEmpty(code_num)) {
            tvCheckDesc.setVisibility(View.VISIBLE);
            tvCheckDesc.setText(R.string.warm_code);
            return;
        }

        if (!Utils.isMobileNO_new(phone_str)) {
            tvCheckDesc.setVisibility(View.VISIBLE);
            tvCheckDesc.setText(R.string.warm_phonenum);
            return;
        }
        tvCheckDesc.setVisibility(View.GONE);
        boolean networkAvailable = Utils.isNetworkAvailable(this);
        if (networkAvailable) {
            showProgress("正在登录");
            HashMap<String, String> map = new HashMap<>();
            map.put("Phone", phone_str);
            map.put("MsgCode", code_num);
            new GetNetUtil(map, Api.GetInfo_PhoneMsgCodeLogin, this) {

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
                        cryptonyBean = httpBean.getObj();
                        String shopInfoIsOK = httpBean.getObj().getShopInfoIsOK();
                        if (!TextUtil.isEmpty(shopInfoIsOK)) {
                            SPUtil.putAndApply(LoginActivity_new.this, "shopInfoIsOK", shopInfoIsOK);
                        }
//                        登陆环信
                        GlobalApplication.HuanxinUserId = cryptonyBean.getUserId();
                        GlobalApplication.HuanxinPwd = cryptonyBean.getHXIMPwd();
                        //Log.i("jiajiaim", cryptonyBean.getUserId());
                        login(GlobalApplication.HuanxinUserId, GlobalApplication.HuanxinPwd);
                        //登陆主界面
                        if (null != cryptonyBean) {
                            GlobalApplication.getInstance().savePerson(cryptonyBean, LoginActivity_new.this);
                        }
                        Intent i = new Intent(LoginActivity_new.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        tvCheckDesc.setVisibility(View.VISIBLE);
                        tvCheckDesc.setText(httpBean.getMsg());
                        closeProgress();
                    }
                }
            };
        } else {
            tvCheckDesc.setVisibility(View.VISIBLE);
            tvCheckDesc.setText(R.string.warm_net);
        }
    }


    /**
     * 获取短信验证码
     */
    private void HttpMessageCode(String phone_str) {
        showProgress("验证码发送中");
        HashMap<String, String> map = new HashMap();
        map.put("OpType", "PhoneMsgCodeLogin");
        map.put("Phone", phone_str);

        new GetNetUtil(map, Api.GetInfo_SendMsgCodeByType, LoginActivity_new.this) {

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

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }
}