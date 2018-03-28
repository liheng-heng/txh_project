package com.txh.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

/**
 * Created by liheng on 2017/5/23.
 * 促销--和h5交互
 */

public class FriendsDataAddWebviewActivity extends BasicActivity implements View.OnClickListener {
    BridgeWebView bride_webview;
    private ImageView iv_head_back;
    private TextView tv_head_back, head_title, tv_head_right, tv_close;
    private LinearLayout ll_head_back, ll_head_right, ll_error_layout, ll_error_no_layout;
    private String intenttype;
    private String ToUnitId;
    private String Distance;
    private String AskId;
    private String Status;
    private String friendId;
    private String FriendUnitId;
    private String userKey = "";
    private String intenttype3_where;//1:好友列表
    private String from;

    @Override
    @JavascriptInterface
    protected void initView() {
        setContentView(R.layout.activity_friend_dataadd_webview);
        intenttype = getIntent().getStringExtra("intenttype");
        ToUnitId = getIntent().getStringExtra("ToUnitId");
        Distance = getIntent().getStringExtra("Distance");
        AskId = getIntent().getStringExtra("AskId");
        Status = getIntent().getStringExtra("Status");
        friendId = getIntent().getStringExtra("friendId");
        FriendUnitId = getIntent().getStringExtra("FriendUnitId");
        userKey = getIntent().getStringExtra("userKey");
        intenttype3_where = getIntent().getStringExtra("intenttype3_where");
        from = getIntent().getStringExtra("from");

        iv_head_back = (ImageView) findViewById(R.id.iv_head_back);
        tv_head_back = (TextView) findViewById(R.id.tv_head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
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

    }

    @Override
    @JavascriptInterface
    protected void initTitle() {
        bride_webview.loadUrl(Api.head_html + getHtml() + "?items=" + jsonBase64(intenttype));

    }

    @Override
    @JavascriptInterface
    public void initData() {
//        js调用原生
        bride_webview.registerHandler("activeHandler", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                updateView(data);
                function.onCallBack(data);
            }
        });
    }

    Intent intent;
    Gson gson = new Gson();

    @JavascriptInterface
    private void updateView(String data) {
        Gson gson = new Gson();
        Method method = gson.fromJson(data, Method.class);

        if (method.getMethod().equals("getTitle")) {
            if (!TextUtil.isEmpty(method.getTitle())) {
                head_title.setText(method.getTitle());
            }
        }

        if (method.getMethod().equals("goChat")) {
            //发送消息
            String data_str = method.getData();
            Data data2 = gson.fromJson(data_str, Data.class);
            String imagesHead = data2.getImagesHead();
            String markName = data2.getMarkName();
            String userId = data2.getUserId();
            String userName = data2.getUserName();
            HomeSingleListBean bean = new HomeSingleListBean();
            bean.setImagesHead(imagesHead);
            bean.setMarkName(markName);
            bean.setUserId(userId);
            bean.setUserName(userName);

            intent = new Intent();
            intent.setClass(this, ChatAcitivty.class);
            intent.putExtra("homeSingleListBean", gson.toJson(bean));
            startActivity(intent);

        }

        if (method.getMethod().equals("recommendFriends")) {
            //推荐好友：recommendFriends
            String data_str = method.getData();
            Data data2 = gson.fromJson(data_str, Data.class);
            String userId = data2.getUserId();
            String userName = data2.getUserName();
            String imagesHead = data2.getImagesHead();
            String unitId = data2.getUnitId();
            String unitName = data2.getUnitName();
            HomeSingleListBean bean = new HomeSingleListBean();
            bean.setUserId(userId);
            bean.setUserName(userName);
            bean.setImagesHead(imagesHead);
            bean.setUnitId(unitId);
            bean.setUnitName(unitName);
            intent = new Intent(this, RecommendFriendsToOthersAcitivty.class);
            intent.putExtra("card_detail", gson.toJson(bean));
            startActivity(intent);
        }

        if (method.getMethod().equals("remarksFriend")) {
            //备注 remarksFriend;
            String data_str = method.getData();
            Data data2 = gson.fromJson(data_str, Data.class);
            intent = new Intent(this, UserRemarkAcitivity_new.class);
            intent.putExtra("FriendId", data2.getUserId());
            intent.putExtra("FriendUnitId", data2.getFriendUnitId());
            intent.putExtra("markname", data2.getMarkName());
            startActivityForResult(intent, 1);
        }

        if (method.getMethod().equals("popupImg")) {
            //查看大图  popupImg
            String data_str = method.getData();
            Data data2 = gson.fromJson(data_str, Data.class);
            String imagesHead = data2.getImagesHead();
            if (!imagesHead.equals("")) {
                OSCPhotosActivity.showImagePreview(this, imagesHead);
            }
        }
        if (method.getMethod().equals("goBack")) {
            EventBus.getDefault().post(new FirstEventBus("NewFriendsAcitivty"));//刷新NewFriendsAcitivty
            finish();
        }
    }

    private String getHtml() {
        if (intenttype.equals("1")) {
            return "/nearbyDetails.html";
        } else if (intenttype.equals("2")) {
            return "/applyDetails.html";
        } else {
            return "/userDetails.html";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                //备注回调
                bride_webview.loadUrl("javascript:rePost ('" + "" + "')");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                EventBus.getDefault().post(new FirstEventBus("UserRemarkAcitivity"));
                finish();
                break;

            case R.id.ll_head_right:
                break;

            case R.id.tv_close:
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

    public String jsonBase64(String intenttype) {
        CryptonyBean person = GlobalApplication.getInstance().getPerson(FriendsDataAddWebviewActivity.this);
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append('"').append("Version").append('"').append(":").append('"').append("1.0").append('"');
        sb.append(",").append('"').append("CertType").append('"').append(":").append('"').append("MD5").append('"');
        sb.append(",").append('"').append("Certification").append('"').append(":").append('"').append(UUID.randomUUID().toString() + "werwerzxsd23234sedwe543453sddsd232434543").append('"');
        sb.append(",").append('"').append("DeviceToken").append('"').append(":").append('"').append(getMyUUID(FriendsDataAddWebviewActivity.this)).append('"');
        sb.append(",").append('"').append("Timestamp").append('"').append(":").append('"').append(Utils.getCurrentTime()).append('"');
        sb.append(",").append('"').append("UserId").append('"').append(":").append('"').append(person.getUserId()).append('"');
        sb.append(",").append('"').append("ToKen").append('"').append(":").append('"').append(person.getUserToken()).append('"');
        if (intenttype.equals("1")) {
            sb.append(",").append('"').append("ToUnitId").append('"').append(":").append('"').append(ToUnitId).append('"');
            sb.append(",").append('"').append("Distance").append('"').append(":").append('"').append(Distance).append('"');
        } else if (intenttype.equals("2")) {
            sb.append(",").append('"').append("AskId").append('"').append(":").append('"').append(AskId).append('"');
            sb.append(",").append('"').append("Status").append('"').append(":").append('"').append(Status).append('"');
        } else if (intenttype.equals("3")) {
            sb.append(",").append('"').append("friendId").append('"').append(":").append('"').append(friendId).append('"');

            if (TextUtil.isEmpty(FriendUnitId)) {
                sb.append(",").append('"').append("FriendUnitId").append('"').append(":").append('"').append("").append('"');
            } else {
                sb.append(",").append('"').append("FriendUnitId").append('"').append(":").append('"').append(FriendUnitId).append('"');
            }
            if (!TextUtil.isEmpty(userKey)) {
                sb.append(",").append('"').append("userKey").append('"').append(":").append('"').append(userKey).append('"');
            }
            sb.append(",").append('"').append("type").append('"').append(":").append('"').append(intenttype3_where).append('"');
        } else {
        }

        sb.append(",").append('"').append("Plat").append('"').append(":").append('"').append("Android").append('"');
        sb.append(",").append('"').append("OSInformation").append('"').append(":").append('"').append(Build.VERSION.RELEASE + "-" + Build.MODEL + "-" + Build.MANUFACTURER).append('"');
        sb.append(",").append('"').append("Channle").append('"').append(":").append('"').append(GlobalApplication.getVersionName() + "").append('"');
        sb.append(",").append('"').append("UnitType").append('"').append(":").append('"').append(person.getUnitType()).append('"');
        sb.append(",").append('"').append("UnitId").append('"').append(":").append('"').append(person.getUnitId()).append('"');
        sb.append(",").append('"').append("RGuid").append('"').append(":").append('"').append(UUID.randomUUID().toString()).append('"');
        sb.append(",").append('"').append("UIServiceId").append('"').append(":").append('"').append("IM" + GlobalApplication.getVersionName()).append('"');
        sb.append(",").append('"').append("IsTest").append('"').append(":").append('"').append("1").append('"');
        sb.append(",").append('"').append("UIUserId").append('"').append(":").append('"').append(person.getUserId()).append('"');
        if (from != null && from.equals("friendlist")) {//只有朋友列表进需要添加
            sb.append(",").append('"').append("from").append('"').append(":").append('"').append("friendlist").append('"');
        }
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


    @Override
    public void onNetChange(int netMobile) {
        // TODO Auto-generated method stub
        if (netMobile == -1) {
            ll_error_layout.setVisibility(View.VISIBLE);
            ll_error_no_layout.setVisibility(View.VISIBLE);
        } else {
            ll_error_layout.setVisibility(View.GONE);
            ll_error_no_layout.setVisibility(View.GONE);
        }
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
        private String ImagesHead;
        private String UserId;
        private String MarkName;
        private String UserName;
        private String UnitId;
        private String UnitName;
        private String FriendUnitId;

        public String getFriendUnitId() {
            return FriendUnitId;
        }

        public void setFriendUnitId(String friendUnitId) {
            FriendUnitId = friendUnitId;
        }

        public String getUnitId() {
            return UnitId;
        }

        public void setUnitId(String unitId) {
            UnitId = unitId;
        }

        public String getUnitName() {
            return UnitName;
        }

        public void setUnitName(String unitName) {
            UnitName = unitName;
        }

        public String getImagesHead() {
            return ImagesHead;
        }

        public void setImagesHead(String imagesHead) {
            ImagesHead = imagesHead;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public String getMarkName() {
            return MarkName;
        }

        public void setMarkName(String markName) {
            MarkName = markName;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }
    }

}






