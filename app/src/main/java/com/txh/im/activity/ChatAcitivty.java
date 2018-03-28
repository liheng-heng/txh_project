package com.txh.im.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.NetUtils;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.UnitBean;
import com.txh.im.easeui.ChatFragment;
import com.txh.im.easeui.Constant;
import com.txh.im.easeui.DemoHelper;
import com.txh.im.easeui.EaseBasicAcitivty;
import com.txh.im.easeui.EaseConstant;
import com.txh.im.easeui.PermissionsManager;
import com.txh.im.easeui.PermissionsResultAction;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ThreadUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/3/23.聊天
 */

public class ChatAcitivty extends EaseBasicAcitivty {
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    private HomeSingleListBean homeSingleListBean;
    private String card_detail;
    private String home_fragment;
    private String UnitId;
    //demo
    public static ChatAcitivty activityInstance;
    private ChatFragment chatFragment;
    private String isOrder;//是否显示代下单 0否，1是
    String userId;//聊天对方的ID
    private String userName;//对方的昵称
    private String userAvatar;//对方的头像


    @Override
    protected void initView() {
        setContentView(R.layout.em_activity_chat);
    }

    @Override
    protected void initTitle() {
        Intent intent = getIntent();
        Gson g = new Gson();
        String homeSingleBean = intent.getStringExtra("homeSingleListBean");
        card_detail = intent.getStringExtra("card_detail");
        home_fragment = intent.getStringExtra("home_fragment");
        userId = intent.getStringExtra(Constant.EXTRA_USER_ID);
//        userName = intent.getStringExtra(Constant.EXTRA_USER_NAME);
//        userAvatar = intent.getStringExtra(Constant.EXTRA_USER_AVATAR);

        if (homeSingleBean != null) {
            homeSingleListBean = g.fromJson(homeSingleBean, HomeSingleListBean.class);
            if (homeSingleListBean.getMarkName() != null && !homeSingleListBean.getMarkName().equals("")) {
                headTitle.setText(homeSingleListBean.getMarkName());
            } else {
                headTitle.setText(homeSingleListBean.getUserName());
            }
            userId = homeSingleListBean.getUserId();
            userName = homeSingleListBean.getUserName();
            userAvatar = homeSingleListBean.getImagesHead();
//            UserDao dao = new UserDao(this);
            EaseUser easeUser = new EaseUser(userId);
            easeUser.setAvatar(userAvatar);
            easeUser.setNick(userName);
//            dao.saveContact(easeUser);
            // 通知listeners联系人同步完毕
            DemoHelper.getInstance().saveContact(easeUser);
            DemoHelper.getInstance().getModel().setContactSynced(true);
            DemoHelper.getInstance().notifyContactsSyncListener(true);
//            EaseUser user = EaseUserUtils.getUserInfo(userId);
//            user.setAvatar(userAvatar);
//            user.setNick(userName);
        } else {
            if (EaseUserUtils.getUserInfo(userId) != null) {
                EaseUser user = EaseUserUtils.getUserInfo(userId);
                if (user != null) {
                    userAvatar = user.getAvatar();
                    userName = user.getNick();
                }
            }
        }
        if (EaseUserUtils.getUserInfo(userId) != null) {
            EaseUser user = EaseUserUtils.getUserInfo(userId);
            if (user != null) {
                userAvatar = user.getAvatar();
                userName = user.getNick();
            }
        }
        headTitle.setText(userName);
        getUnit();//获取单位id，判断是否会显示代下单
    }

    @Override
    public void initData() {
        //new出EaseChatFragment或其子类的实例
        chatFragment = new ChatFragment();
        //传入参数
        Bundle args = new Bundle();
        isOrder = "0";
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, userId);
        args.putString(Constant.CARD_DETAIL, card_detail);
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        requestPermissions();
        EMClient.getInstance().addConnectionListener(emConnectionListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        EMClient.getInstance().removeConnectionListener(emConnectionListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (userId.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    public String getToChatUsername() {
        return userId;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    /**
     * 跳转Activity
     *
     * @param activityClass
     */
    public void jumpActivity(Class<? extends Activity> activityClass, String name, String value) {
        Intent intent = new Intent(ChatAcitivty.this, activityClass);
        intent.putExtra(name, value);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @OnClick({R.id.ll_head_back, R.id.ll_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                if (home_fragment != null) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("home_fragment", "home_fragment");
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.ll_head_right:
                if (GlobalApplication.getInstance().getPerson(ChatAcitivty.this).getUnitType().equals("2") && isOrder.equals("1")) {
                    //如果对方为买家，自己为卖家
                    jumpActivity(GoodsPlaceOrderAcitivty.class, "userId", userId);
                    return;
                }
                jumpActivity(ShopClassifyActivity.class, "shopId", UnitId);
                break;
        }
    }

    //获取单位id
    public void getUnit() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("ToUserId", userId);
        new GetNetUtil(map, Api.TX_App_SNS_GetShopUnitId, this) {
            @Override
            public void successHandle(String basebean) {
                if (ChatAcitivty.this.isFinishing()) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<UnitBean> base = gson.fromJson(basebean, new TypeToken<Basebean<UnitBean>>() {
                }.getType());
                if (base.getStatus().equals("200")) {
                    UnitBean obj = base.getObj();
                    if (obj != null) {
                        if (obj.getUnitId() != null && !obj.getUnitId().equals("")) {
                            llHeadRight.setVisibility(View.VISIBLE);
                            tvHeadRight.setText("进店");
                            UnitId = base.getObj().getUnitId();
                        } else {
                            UnitId = "";
                        }
                        if (obj.getIsOrder() != null && !obj.getIsOrder().equals("")) {
                            if (obj.getIsOrder().equals("1")) {
                                if (GlobalApplication.getInstance().getPerson(ChatAcitivty.this).getUnitType().equals("2")) {
                                    //如果对方为买家，自己为卖家
                                    llHeadRight.setVisibility(View.VISIBLE);
                                    tvHeadRight.setText("代下单");
                                    isOrder = "1";
                                    chatFragment.registerPriceGrouping();
                                }
                            }
                        }
                    }
                } else {
                    return;
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

//    public HomeSingleListBean getHomeSingleListBean() {
//        return homeSingleListBean;
//    }

    //环信掉线重连
    private EMConnectionListener emConnectionListener = new EMConnectionListener() {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int errorCode) {
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    if (errorCode == EMError.USER_REMOVED) {
                        //显示账号已经被移除
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //连接不到聊天服务器
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        ToastUtils.toast(ChatAcitivty.this, "连接不到聊天服务器");
                                        EMClient.getInstance().login(GlobalApplication.HuanxinUserId,
                                                GlobalApplication.HuanxinPwd, new EMCallBack() {
                                                    @Override
                                                    public void onSuccess() {
                                                        ThreadUtils.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                EMClient.getInstance().groupManager().loadAllGroups();
                                                                EMClient.getInstance().chatManager().loadAllConversations();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onError(final int code, String error) {
                                                        ThreadUtils.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onProgress(int progress, String status) {

                                                    }
                                                });
                                    }
                                });
                            }
                        });
                    } else if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        //显示账号在其他设备登陆
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //ToastUtils.toast(MainActivity.this, "账号在其他设备登陆");
                            }
                        });
                    } else {
                        if (NetUtils.hasNetwork(ChatAcitivty.this)) {
                            //连接不到聊天服务器
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    ToastUtils.toast(ChatAcitivty.this, "连接不到聊天服务器");
                                    EMClient.getInstance().login(GlobalApplication.HuanxinUserId,
                                            GlobalApplication.HuanxinPwd, new EMCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    ThreadUtils.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            EMClient.getInstance().groupManager().loadAllGroups();
                                                            EMClient.getInstance().chatManager().loadAllConversations();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onError(final int code, String error) {
                                                    ThreadUtils.runOnUiThread(new Runnable() {
                                                        public void run() {
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onProgress(int progress, String status) {

                                                }
                                            });
                                }
                            });
                        } else {
                            //当前网络不可用，请检查网络连接
                            if (NetUtils.hasNetwork(ChatAcitivty.this)) {
                                //连接不到聊天服务器
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //ToastUtils.toast(MainActivity.this, "请检查网络连接");
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    };

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    public String getIsOrder() {
        return isOrder;
    }

}