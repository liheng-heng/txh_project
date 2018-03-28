package com.txh.im.activity;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.utils.Utils;

import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/6/8.
 * 关于我们
 */

public class WebAboutUsAcitivty extends BasicActivity {
    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
    @Bind(R.id.tv_close)
    TextView tvClose;
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.rl_head_title)
    RelativeLayout rlHeadTitle;
    @Bind(R.id.tv_nodata)
    TextView tvNodata;
    @Bind(R.id.bride_webview)
    WebView brideWebview;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_promotion_webview2);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("关于我们");
    }

    @Override
    public void initData() {
        WebSettings settings = brideWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        //必须设置否则不显示
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBlockNetworkImage(false);
        brideWebview.loadUrl(Api.head_html + "/aboutUs.html" + "?items=" + jsonBase64());
    }

    public String jsonBase64() {
        CryptonyBean person = GlobalApplication.getInstance().getPerson(this);
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append('"').append("Version").append('"').append(":").append('"').append("1.0").append('"');
        sb.append(",").append('"').append("CertType").append('"').append(":").append('"').append("MD5").append('"');
        sb.append(",").append('"').append("Certification").append('"').append(":").append('"').append(UUID.randomUUID().toString() + "werwerzxsd23234sedwe543453sddsd232434543").append('"');
        sb.append(",").append('"').append("DeviceToken").append('"').append(":").append('"').append(getMyUUID(WebAboutUsAcitivty.this)).append('"');
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

    @OnClick({R.id.ll_head_back, R.id.ll_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;
            case R.id.ll_head_right:
                break;
        }
    }
}
