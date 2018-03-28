package com.txh.im.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.AllConstant;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.AppShare;
import com.txh.im.bean.Basebean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/6/26.
 * APP分享
 */

public class WebAppShareAcitivty extends BasicActivity {
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
    private String shareMsg;//分享的短信文本
    private String sharePic;//分享的图片
    private String shareTitle;//分享的标题
    private String shareUrl;//分享链接
    private String shareDesc;//分享描述
    private UMImage umImage;
    private UMWeb umweb;
    private ShareAction shareAction;
    private UMShareListener mShareListener;

    @Override
    protected void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.umeng_blue));
//        }
        setContentView(R.layout.activity_promotion_webview2);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("下载天下货APP");
        llHeadRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        WebSettings settings = brideWebview.getSettings();
        tvHeadRight.setText("分享");
        Intent intent = getIntent();
        String app_share = intent.getStringExtra("app_share");
        settings.setJavaScriptEnabled(true);
        //必须设置否则不显示
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBlockNetworkImage(false);
        brideWebview.loadUrl(app_share);
        getRequestNet();
        mShareListener = new CustomShareListener(this);
    }

    //获取分享相关的内容
    public void getRequestNet() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ActionVersion", AllConstant.ActionVersionFour);
        hashMap.put("IsActionTest", AllConstant.IsActionTest);
        new GetNetUtil(hashMap, Api.GetInfo_GetAppShareInfo, this) {
            @Override
            public void successHandle(String base) {
                if (isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<AppShare> basebean = gson.fromJson(base, new TypeToken<Basebean<AppShare>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    AppShare obj = basebean.getObj();
                    if (obj != null) {
                        AppShare.AppShareInfoBean appShareInfo = obj.getAppShareInfo();
                        shareMsg = appShareInfo.getShareMsg();
                        sharePic = appShareInfo.getSharePic();
                        shareTitle = appShareInfo.getShareTitle();
                        shareUrl = appShareInfo.getShareUrl();
                        shareDesc = appShareInfo.getShareDesc();
                        //网络图片
//                        umImage = new UMImage(WebAppShareAcitivty.this, shareUrl);
//                        UMImage thumb = new UMImage(this, R.drawable.thumb);
//                        umImage.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//                        umImage.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
//                        umImage.setThumb(thumb);
//                        umweb = new UMWeb(shareUrl);
//                        umweb.setTitle(shareTitle);
//                        umweb.setDescription(shareDesc);
                        shareAction = new ShareAction(WebAppShareAcitivty.this)
                                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SMS)
                                .setShareboardclickCallback(new ShareBoardlistener() {
                                    @Override
                                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                        UMWeb web = new UMWeb(shareUrl);
                                        web.setTitle(shareTitle);
                                        web.setDescription(shareDesc);
                                        web.setThumb(new UMImage(WebAppShareAcitivty.this, sharePic));
//                                        UMWeb web = new UMWeb("http://bbs.umeng.com/");
//                                        web.setTitle("来自分享面板标题");
//                                        web.setDescription("来自分享面板内容");
//                                        web.setThumb(new UMImage(WebAppShareAcitivty.this, R.drawable.add_friend));
                                        switch (share_media.name()) {
                                            case "WEIXIN":
                                                new ShareAction(WebAppShareAcitivty.this)
                                                        .withMedia(web)
                                                        .setPlatform(share_media)
                                                        .setCallback(mShareListener)
                                                        .share();
                                                break;
                                            case "WEIXIN_CIRCLE":
                                                new ShareAction(WebAppShareAcitivty.this)
                                                        .withMedia(web)
                                                        .setPlatform(share_media)
                                                        .setCallback(mShareListener)
                                                        .share();
                                                break;
                                            case "SMS":
                                                new ShareAction(WebAppShareAcitivty.this)
                                                        .withText(shareMsg)
                                                        .setPlatform(share_media)
                                                        .setCallback(mShareListener)
                                                        .share();
                                                break;
                                        }
                                    }
                                });
                    }
                }
            }
        };
    }

    @OnClick({R.id.ll_head_back, R.id.ll_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;
            case R.id.ll_head_right:
                if (shareAction == null) {
                    return;
                }
                shareAction.open(new ShareBoardConfig().setIndicatorVisibility(false));
                break;

        }
    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<WebAppShareAcitivty> mActivity;

        private CustomShareListener(WebAppShareAcitivty activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform != SHARE_MEDIA.SMS) {
                ToastUtils.toast(mActivity.get(), "分享成功");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Log.i("jiajia",t.getMessage() + "");
            if (platform != SHARE_MEDIA.SMS) {
                if (!UMShareAPI.get(mActivity.get()).isInstall(mActivity.get(),platform)) {
                    ToastUtils.toast(mActivity.get(), "您尚未安装微信");
                } else {
                    ToastUtils.toast(mActivity.get(), "分享失败");
                }
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (platform != SHARE_MEDIA.SMS)
                ToastUtils.toast(mActivity.get(), "分享取消");
        }
    }

    //授权代码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        shareAction.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
