package com.txh.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.Utils;

import java.util.UUID;

/**
 * Created by liheng on 2017/5/23.
 * 促销--和h5交互
 */

public class CouponWebviewActivity extends BasicActivity implements View.OnClickListener {

    BridgeWebView bride_webview;
    private ImageView iv_head_back;
    private TextView tv_head_back, head_title, tv_head_right, tv_nodata, tv_close;
    private LinearLayout ll_head_back, ll_head_right, ll_error_layout, ll_error_no_layout;
    private String uri;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_coupon_webview);
        uri = getIntent().getStringExtra("uri");
        iv_head_back = (ImageView) findViewById(R.id.iv_head_back);
        tv_head_back = (TextView) findViewById(R.id.tv_head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        tv_close = (TextView) findViewById(R.id.tv_close);
        bride_webview = (BridgeWebView) findViewById(R.id.bride_webview);
        ll_head_back = (LinearLayout) findViewById(R.id.ll_head_back);
        ll_head_right = (LinearLayout) findViewById(R.id.ll_head_right);
        ll_error_layout = (LinearLayout) findViewById(R.id.ll_error_layout);
        ll_error_no_layout = (LinearLayout) findViewById(R.id.ll_error_no_layout);
        ll_head_back.setOnClickListener(this);
        ll_head_right.setOnClickListener(this);
        tv_close.setOnClickListener(this);
        ll_error_no_layout.setOnClickListener(this);

        bride_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        bride_webview.setWebChromeClient(new WebChromeClient());
        bride_webview.getSettings().setLoadWithOverviewMode(true);
        bride_webview.getSettings().setUseWideViewPort(true);
        bride_webview.getSettings().setSupportZoom(true);
        bride_webview.getSettings().setJavaScriptEnabled(true);
        //必须设置否则不显示
        bride_webview.getSettings().setDomStorageEnabled(true);
        bride_webview.getSettings().setDefaultTextEncodingName("UTF-8");
        bride_webview.getSettings().setBlockNetworkImage(false);
        head_title.setText("优惠券");
    }

    @Override
    protected void initTitle() {
        if (Utils.isNetworkAvailable(this)) {
            ll_error_layout.setVisibility(View.GONE);
            ll_error_no_layout.setVisibility(View.GONE);
            bride_webview.loadUrl(uri + "?items=" + jsonBase64());
        } else {
            ll_error_layout.setVisibility(View.VISIBLE);
            ll_error_no_layout.setVisibility(View.VISIBLE);
        }

        String items_str = uri + "?items=" + jsonBase64();

        Log.i("------",items_str);

    }

    @Override
    public void initData() {
//        js调用原生
        bride_webview.registerHandler("activeHandler", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                updateView(data);
                function.onCallBack(data + "回调---返回");
            }
        });
    }

    //    @李恒  去商城首页  goMall       去分类页  goCategory
    private void updateView(String data) {
        Log.i("=================>>>>", "优惠券--------------" + data);
        Gson gson = new Gson();
        Method me = gson.fromJson(data, Method.class);

        if (me.getMethod().equals("getTitle")) {
            head_title.setText(me.getTitle());
        } else if (me.getMethod().equals("goLogin")) {
            /**
             * 执行退出操作
             */
            GlobalApplication.getInstance().clearUserData(CouponWebviewActivity.this);
            SPUtil.remove(CouponWebviewActivity.this, "sp_personalCenterBean");
            SPUtil.remove(CouponWebviewActivity.this, "sp_MyInfoBean");
            SPUtil.remove(CouponWebviewActivity.this, "sp_shoppingTrolleyBean");
            EMClient.getInstance().logout(true);
            startActivity(new Intent(CouponWebviewActivity.this, LoginActivity_new.class));

        } else if (me.getMethod().equals("goQuotePriceManager")) {
            startActivity(new Intent(CouponWebviewActivity.this, QuoteAcitivty.class));

        } else if (me.getMethod().equals("goMall")) {
            Intent i = new Intent(CouponWebviewActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        } else if (me.getMethod().equals("goCategory")) {
            String data1 = me.getData();
            Data data2 = gson.fromJson(data1, Data.class);
            Intent intent = new Intent(CouponWebviewActivity.this, ShopClassifyActivity.class);
            intent.putExtra("shopId", data2.getShopId());
            startActivity(intent);
            finish();

        } else {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;

            case R.id.ll_head_right:
                if (Utils.isNetworkAvailable(this)) {
                    ll_error_layout.setVisibility(View.GONE);
                    ll_error_no_layout.setVisibility(View.GONE);
                    //刷新
                    bride_webview.loadUrl("javascript:reload ('" + "" + "')");
                } else {
                    ll_error_layout.setVisibility(View.VISIBLE);
                    ll_error_no_layout.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.tv_close:
                finish();
                break;

            case R.id.ll_error_no_layout:
                break;
        }
    }

    public String jsonBase64() {
        CryptonyBean person = GlobalApplication.getInstance().getPerson(CouponWebviewActivity.this);
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append('"').append("Version").append('"').append(":").append('"').append("1.0").append('"');
        sb.append(",").append('"').append("CertType").append('"').append(":").append('"').append("MD5").append('"');
        sb.append(",").append('"').append("Certification").append('"').append(":").append('"').append(UUID.randomUUID().toString() + "werwerzxsd23234sedwe543453sddsd232434543").append('"');
        sb.append(",").append('"').append("DeviceToken").append('"').append(":").append('"').append(getMyUUID(CouponWebviewActivity.this)).append('"');
        sb.append(",").append('"').append("Timestamp").append('"').append(":").append('"').append(Utils.getCurrentTime()).append('"');
        sb.append(",").append('"').append("UserId").append('"').append(":").append('"').append(person.getUserId()).append('"');
        sb.append(",").append('"').append("ToKen").append('"').append(":").append('"').append(person.getUserToken()).append('"');
        sb.append(",").append('"').append("Plat").append('"').append(":").append('"').append("Android").append('"');
        sb.append(",").append('"').append("OSInformation").append('"').append(":").append('"').append(Build.VERSION.RELEASE + "-" + Build.MODEL + "-" + Build.MANUFACTURER).append('"');
        sb.append(",").append('"').append("Channle").append('"').append(":").append('"').append(GlobalApplication.getVersionName() + "").append('"');
        sb.append(",").append('"').append("UnitType").append('"').append(":").append('"').append(person.getUnitType()).append('"');
        sb.append(",").append('"').append("UnitId").append('"').append(":").append('"').append(person.getUnitId()).append('"');
        sb.append(",").append('"').append("RGuid").append('"').append(":").append('"').append(UUID.randomUUID().toString()).append('"');
        sb.append(",").append('"').append("UIServiceId").append('"').append(":").append('"').append("IM" + GlobalApplication.getVersionName()).append('"');
        sb.append(",").append('"').append("IsTest").append('"').append(":").append('"').append("1").append('"');
        sb.append(",").append('"').append("UIUserId").append('"').append(":").append('"').append(person.getUserId()).append('"');
        sb.append("}");
        return Base64.encodeToString(sb.toString().getBytes(), Base64.NO_WRAP);
    }

    public static String getMyUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    class Method {
        private String title;
        private String level;
        private String method;
        private String data;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

    }

    class Data {
        private String ShopId;

        public String getShopId() {
            return ShopId;
        }

        public void setShopId(String shopId) {
            ShopId = shopId;
        }
    }

    /**
     * 为防止页面卡死，不处理手机自带的返回功能
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (!TextUtil.isEmpty(level)) {
//                if (level.equals("1")) {
//                    finish();
//                }
//            }
//            bride_webview.loadUrl("javascript:goBack ('" + "" + "')");
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onNetChange(int netMobile) {
        // TODO Auto-generated method stub
        //在这个判断，根据需要做处理
        if (netMobile == -1) {
            ll_error_layout.setVisibility(View.VISIBLE);
            ll_error_no_layout.setVisibility(View.VISIBLE);
        } else {
            ll_error_layout.setVisibility(View.GONE);
            ll_error_no_layout.setVisibility(View.GONE);
        }
    }

}






