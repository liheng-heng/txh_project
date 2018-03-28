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
import com.txh.im.datepicker.DatePickDialog;
import com.txh.im.datepicker.bean.DateType;
import com.txh.im.datetime.TimeSelector;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by liheng on 2017/5/23.
 * 促销--和h5交互
 */

public class PromotionWebviewActivity extends BasicActivity implements View.OnClickListener {

    BridgeWebView bride_webview;
    private ImageView iv_head_back;
    private TextView tv_head_back, head_title, tv_head_right, tv_nodata, tv_close;
    private LinearLayout ll_head_back, ll_head_right, ll_error_layout, ll_error_no_layout;
    private String level;
    private String pushTime;
    private String startTime;
    private String endTime;
    private String currentTime;
    private String uri;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_promotion_webview);
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

        head_title.setText("商城管理");
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
        getCurrentTime();
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        currentTime = formatter.format(curDate);
        return currentTime;

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

    private void updateView(String data) {
        Gson gson = new Gson();
        Method me = gson.fromJson(data, Method.class);
        if (me.getMethod().equals("getTitle")) {
            level = me.getLevel();
            head_title.setText(me.getTitle());
        } else if (me.getMethod().equals("goLogin")) {
            /**
             * 执行退出操作
             */
            GlobalApplication.getInstance().clearUserData(PromotionWebviewActivity.this);
            SPUtil.remove(PromotionWebviewActivity.this, "sp_personalCenterBean");
            SPUtil.remove(PromotionWebviewActivity.this, "sp_MyInfoBean");
            SPUtil.remove(PromotionWebviewActivity.this, "sp_shoppingTrolleyBean");
            EMClient.getInstance().logout(true);
            startActivity(new Intent(PromotionWebviewActivity.this, LoginActivity_new.class));
        } else if (me.getMethod().equals("goQuotePriceManager")) {
            startActivity(new Intent(PromotionWebviewActivity.this, QuoteAcitivty.class));
        } else {
            String data1 = me.getData();
            Data data2 = gson.fromJson(data1, Data.class);
            String type = data2.getType();
            pushTime = data2.getPush();
            startTime = data2.getStart();
            endTime = data2.getEnd();

            Log.i("=========>>>>>", "pushTime-----" + pushTime);
            Log.i("=========>>>>>", "startTime-----" + startTime);
            Log.i("=========>>>>>", "endTime-----" + endTime);

            showDatePickDialog(DateType.TYPE_YMDHM, type);

            //1=推送时间，2=开始时间，3= 结束时间
//            if (type.equals("1")) {
//                TimeSelector timeSelector = new TimeSelector(PromotionWebviewActivity.this, new TimeSelector.ResultHandler() {
//                    @Override
//                    public void handle(String time) {
//                        if (compareTime(time, startTime)) {
//                            bride_webview.loadUrl("javascript:setTimeHandler('" + StringToJson(time, "1") + "')");
//                        } else {
//                            ToastUtils.toast(PromotionWebviewActivity.this, "推送时间必须小于开始时间");
//                        }
//                    }
//                }, getCurrentTime(), "2050-12-31 00:00");
//                timeSelector.setIsLoop(false);
//                timeSelector.show();
//            } else if (type.equals("2")) {
//                TimeSelector timeSelector = new TimeSelector(PromotionWebviewActivity.this, new TimeSelector.ResultHandler() {
//                    @Override
//                    public void handle(String time) {
//                        if (TextUtil.isEmpty(endTime) && TextUtil.isEmpty(pushTime)) {
//                            bride_webview.loadUrl("javascript:setTimeHandler('" + StringToJson(time, "2") + "')");
//                        } else {
//                            if (TextUtil.isEmpty(pushTime)) {
//                                //结束时间对比开始时间
//                                if (compareTime(time, endTime)) {
//                                    bride_webview.loadUrl("javascript:setTimeHandler('" + StringToJson(time, "2") + "')");
//                                } else {
//                                    ToastUtils.toast(PromotionWebviewActivity.this, "开始时间必须小于结束时间");
//                                }
//                            } else {
//                                if (compareTime(time, endTime)) {
//                                    if (compareTime(pushTime, time)) {
//                                        bride_webview.loadUrl("javascript:setTimeHandler('" + StringToJson(time, "2") + "')");
//                                    } else {
//                                        ToastUtils.toast(PromotionWebviewActivity.this, "推送时间必须小于开始时间");
//                                    }
//                                } else {
//                                    ToastUtils.toast(PromotionWebviewActivity.this, "开始时间必须小于结束时间");
//                                }
//                            }
//                        }
//                    }
//                }, getCurrentTime(), "2050-12-31 00:00");
//                timeSelector.setIsLoop(false);
//                timeSelector.show();
//            } else if (type.equals("3")) {
//                TimeSelector timeSelector = new TimeSelector(PromotionWebviewActivity.this, new TimeSelector.ResultHandler() {
//                    @Override
//                    public void handle(String time) {
//                        if (compareTime(startTime, time)) {
//                            bride_webview.loadUrl("javascript:setTimeHandler('" + StringToJson(time, "3") + "')");
//                        } else {
//                            ToastUtils.toast(PromotionWebviewActivity.this, "开始时间必须小于结束时间");
//                        }
//                    }
//                }, getCurrentTime(), "2050-12-31 00:00");
//                timeSelector.setIsLoop(false);
//                timeSelector.show();
//
//            } else {
//            }
        }
    }


    private void showDatePickDialog(DateType type, final String timeType_str) {
        final DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(500);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(null);
        dialog.show();
        dialog.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("============", "确定确定确定确定----" + dialog.getMessge());
                //确定----2017-06-25 15:21
                dialog.dismiss();
                updateH5Data(dialog.getMessge(), timeType_str);

            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("============", "取消取消取消取消取消取消取消取消");
                dialog.dismiss();
            }
        });
    }

    private void updateH5Data(String timeString, String type) {
        //1=推送时间，2=开始时间，3= 结束时间
        if (type.equals("1")) {
            if (compareTime(getCurrentTime(), timeString)) {
                if (compareTime(timeString, startTime)) {
                    bride_webview.loadUrl("javascript:setTimeHandler('" + StringToJson(timeString, "1") + "')");
                } else {
                    ToastUtils.toast(PromotionWebviewActivity.this, "推送时间必须小于开始时间");
                }
            } else {
                ToastUtils.toast(PromotionWebviewActivity.this, "推送时间必须大于当前时间");
            }
        } else if (type.equals("2")) {
            if (compareTime(getCurrentTime(), timeString)) {
                if (TextUtil.isEmpty(endTime)) {
                    bride_webview.loadUrl("javascript:setTimeHandler('" + StringToJson(timeString, "2") + "')");
                } else {
                    if (compareTime(timeString, endTime)) {
                        bride_webview.loadUrl("javascript:setTimeHandler('" + StringToJson(timeString, "2") + "')");
                    } else {
                        ToastUtils.toast(PromotionWebviewActivity.this, "开始时间必须小于结束时间");
                    }
                }
            } else {
                ToastUtils.toast(PromotionWebviewActivity.this, "开始时间必须大于当前时间");
            }
        } else if (type.equals("3")) {
            if (compareTime(getCurrentTime(), timeString)) {
                if (TextUtil.isEmpty(startTime)) {
                    bride_webview.loadUrl("javascript:setTimeHandler('" + StringToJson(timeString, "3") + "')");
                } else {
                    if (compareTime(startTime, timeString)) {
                        bride_webview.loadUrl("javascript:setTimeHandler('" + StringToJson(timeString, "3") + "')");
                    } else {
                        ToastUtils.toast(PromotionWebviewActivity.this, "结束时间必须大于开始时间");
                    }
                }
            } else {
                ToastUtils.toast(PromotionWebviewActivity.this, "结束时间必须大于当前时间");
            }
        } else {
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                if (Utils.isNetworkAvailable(this)) {
                    if (!TextUtil.isEmpty(level)) {
                        if (level.equals("1")) {
                            finish();
                        }
                    }
                    bride_webview.loadUrl("javascript:goBack ('" + "" + "')");
                } else {
                    finish();
                }
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

    public String StringToJson(String time, String type) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append('"').append("type").append('"').append(":").append('"').append(type).append('"');
        sb.append(",").append('"').append("time").append('"').append(":").append('"').append(time).append('"');
        sb.append("}");

        return sb.toString();
    }

    public String jsonBase64() {
        CryptonyBean person = GlobalApplication.getInstance().getPerson(PromotionWebviewActivity.this);
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append('"').append("Version").append('"').append(":").append('"').append("1.0").append('"');
        sb.append(",").append('"').append("CertType").append('"').append(":").append('"').append("MD5").append('"');
        sb.append(",").append('"').append("Certification").append('"').append(":").append('"').append(UUID.randomUUID().toString() + "werwerzxsd23234sedwe543453sddsd232434543").append('"');
        sb.append(",").append('"').append("DeviceToken").append('"').append(":").append('"').append(getMyUUID(PromotionWebviewActivity.this)).append('"');
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

        private String type;
        private String end;
        private String push;
        private String start;

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getPush() {
            return push;
        }

        public void setPush(String push) {
            this.push = push;
        }
    }

    /**
     * 选择开始时间，此时有结束时间，两者对比
     * true: 开始时间在结束时间之前
     * false: 开始时间在结束时间之后
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public Boolean compareTime(String beginTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date begin_T = sdf.parse(beginTime);
            Date end_T = sdf.parse(endTime);

            if (begin_T.before(end_T)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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






