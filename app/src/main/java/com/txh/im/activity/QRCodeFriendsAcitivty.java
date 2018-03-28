package com.txh.im.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.UserInShopBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.QrDialog;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class QRCodeFriendsAcitivty extends BasicActivity {

    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.fl_my_container)
    FrameLayout flMyContainer;
    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
    @Bind(R.id.tv_head_back)
    TextView tvHeadBack;
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_right)
    ImageView headRight;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.rl_head_title)
    RelativeLayout rlHeadTitle;
    @Bind(R.id.activity_qrcode_friends_acitivty)
    FrameLayout activityQrcodeFriendsAcitivty;
    private CaptureFragment captureFragment;
    private CryptonyBean person;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_qrcode_friends_acitivty);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    @Override
    protected void initTitle() {
        tvHeadBack.setVisibility(View.GONE);
        headTitle.setText("二维码/条码");
    }

    @Override
    public void initData() {
        person = GlobalApplication.getInstance().getPerson(this);
    }


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, final String result) {
            if (result.contains("tianxiahuo.cn")) {
                //登陆
                String[] sourceStrArray = result.split("code=");
                HashMap<String, String> map = new HashMap<>();
                map.put("Code", sourceStrArray[1]);
                new GetNetUtil(map, Api.PZ_Center_ScanQRCode, QRCodeFriendsAcitivty.this) {
                    @Override
                    public void successHandle(String base) {
                        if (isDestroy) {
                            return;
                        }
                        Gson gson = new Gson();
                        Basebean basebean = gson.fromJson(base, Basebean.class);
                        if (basebean.getStatus().equals("200")) {
                            QRCodeFriendsAcitivty.this.finish();
                            return;
                        }
                        ToastUtils.toast(QRCodeFriendsAcitivty.this, basebean.getMsg());
                    }
                };
            } else {
                //判断是不是天下货的二维码
                if (result.contains("_")) {
                    String[] sourceStrArray = result.split("_");
                    //已经是好友了
                    Intent intent = new Intent();
                    intent.setClass(QRCodeFriendsAcitivty.this, FriendsDataAddWebviewActivity.class);
                    intent.putExtra("friendId", sourceStrArray[0]);
                    intent.putExtra("FriendUnitId", sourceStrArray[1]);
                    intent.putExtra("userKey", result);
                    intent.putExtra("intenttype", "3");
                    intent.putExtra("intenttype3_where", "2");
                    startActivity(intent);
                    Log.v("=========>>", "1-扫一扫跳转好友详情(已经是好友)");

//                    String[] sourceStrArray = result.split("_");
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("UserKey", sourceStrArray[0]);
//                    map.put("SearchUnitId", sourceStrArray[1]);
//                    map.toString();
//                    new GetNetUtil(map, Api.TX_App_SNS_ScanQRCode, QRCodeFriendsAcitivty.this) {
//                        @Override
//                        public void successHandle(String base) {
//                            if (isDestroy){
//                                return;
//                            }
//                            Gson gson = new Gson();
//                            Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
//                            }.getType());
//                            HomeSingleListBean obj = basebean.getObj();
//                            Intent resultIntent = new Intent();
//                            resultIntent.setClass(QRCodeFriendsAcitivty.this, FriendsDataAddActivity.class);
//                            if (obj == null) {
//                                resultIntent.putExtra(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//                                resultIntent.putExtra(CodeUtils.RESULT_STRING, result);
//                            } else {
//                                resultIntent.putExtra("homeSingleListBean", gson.toJson(obj));
//                            }
//                            startActivity(resultIntent);
//                            QRCodeFriendsAcitivty.this.finish();
//                        }
//                    };

                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.setClass(QRCodeFriendsAcitivty.this, FriendsDataAddActivity.class);
                    resultIntent.putExtra(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                    resultIntent.putExtra(CodeUtils.RESULT_STRING, result);
                    startActivity(resultIntent);
                    QRCodeFriendsAcitivty.this.finish();

                }
            }
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            resultIntent.setClass(QRCodeFriendsAcitivty.this, FriendsDataAddActivity.class);
//            Bundle bundle = new Bundle();
            resultIntent.putExtra(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            resultIntent.putExtra(CodeUtils.RESULT_STRING, "");
//            resultIntent.putExtras(bundle);
//            QRCodeFriendsAcitivty.this.setResult(RESULT_OK, resultIntent);
            startActivity(resultIntent);
            QRCodeFriendsAcitivty.this.finish();
        }
    };

    @OnClick({R.id.ll_head_back, R.id.tv_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;
            case R.id.tv_qr:
                new GetNetUtil(null, Api.TX_App_User_GetUserMapUnitInfo, this) {
                    @Override
                    public void successHandle(String base) {
                        if (isDestroy) {
                            return;
                        }
                        Gson gson = new Gson();
                        BaseListBean<UserInShopBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<UserInShopBean>>() {
                        }.getType());
                        if (basebean.getStatus().equals("200")) {
                            List<UserInShopBean> list = basebean.getObj();
                            if (list != null && list.size() != 0) {
                                if (list.size() == 1) {
                                    UserInShopBean userInShopBean = list.get(0);
                                    new QrDialog(QRCodeFriendsAcitivty.this, userInShopBean).show();
                                } else {
                                    Intent intent = new Intent(QRCodeFriendsAcitivty.this, QrListAcitivty.class);
                                    intent.putExtra("qrlist", gson.toJson(list));
                                    startActivity(intent);
                                }
                            }
                        } else {
                            toast(basebean.getMsg());
                        }
                    }
                };
                break;
        }
    }
}

