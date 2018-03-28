package com.txh.im.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.NetUtils;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.EMMessageListenerAdapter;
import com.txh.im.adapter.WgoodsFragmentPagerAdapter;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.ChangeStatusBean;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.MainButtonBean;
import com.txh.im.easeui.DemoHelper;
import com.txh.im.easeui.DemoModel;
import com.txh.im.easeui.db.InviteMessgeDao;
import com.txh.im.iconnum.ShortcutBadger;
import com.txh.im.listener.UnreadMessageListener;
import com.txh.im.utils.ExampleUtil;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ThreadUtils;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UpdateManager;
import com.txh.im.utils.Utils;
import com.txh.im.widget.ImageWordView;
import com.txh.im.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import butterknife.Bind;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends BasicActivity implements UnreadMessageListener {

    @Bind(R.id.nvp_fg)
    NoScrollViewPager nvpFg;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    private WgoodsFragmentPagerAdapter mWgoodsFragmentPagerAdapter;
    private int unReadNumHome;
    private List<MainButtonBean> listBean;
    private String mMyinfo;
    private String home_fragment;
    private boolean finishChangeUnittype = false;
    private String unitType;//商家类型(1买家，2卖家)
    private Gson gson;
    private int unReadFriends;
    private DemoModel model;

    @Override
    protected void beforeSetContentView() {
        super.beforeSetContentView();
    }

    @Override
    protected void initTitle() {
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
        EMClient.getInstance().addConnectionListener(emConnectionListener);
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        model = DemoHelper.getInstance().getModel();
    }

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
                                //ToastUtils.toast(MainActivity.this, "账号已经被移除");
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
                    } else if (errorCode == EMError.NETWORK_ERROR) {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //ToastUtils.toast(MainActivity.this, "连接不到聊天服务器");
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
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //ToastUtils.toast(MainActivity.this, "连接不到聊天服务器");
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
                            if (NetUtils.hasNetwork(MainActivity.this)) {
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

    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {

        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            for (EMMessage message : list) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
//            initUnreadMeaageNum();
            setDeskIconNum();
            getUnreadMessage();
            refreshUIWithMessage();
            mWgoodsFragmentPagerAdapter.getConversationListFragment().getNetRequest(0);
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            super.onMessageRead(messages);
            refreshUIWithMessage();
//            setDeskIconNum();
//            getUnreadMessage();
//            initUnreadMeaageNum();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            super.onMessageChanged(message, change);
//            initUnreadMeaageNum();
            refreshUIWithMessage();
//            getUnreadMessage();
            mWgoodsFragmentPagerAdapter.getConversationListFragment().getNetRequest(0);
        }
    };

    private void initUnreadMeaageNum() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                final Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        getUnreadMessage(allConversations);
                    }
                });
            }
        });
    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
//                updateUnreadLabel();
                if (mWgoodsFragmentPagerAdapter.getConversationListFragment() != null) {
                    mWgoodsFragmentPagerAdapter.getConversationListFragment().refresh();
                }
            }
        });
    }

    private InviteMessgeDao inviteMessgeDao;

    public int getUnreadAddressCountTotal() {
        int unreadMsgCountTotal = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMessageCount();
        return unreadMsgCountTotal;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUnreadMessage();
        setDeskIconNum();//角标显示
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        if (has_write_external_storage_Permission()) {
            Log.i("------>>", "有权限");
        } else {
            apply_write_external_storage_Permission();
            Log.i("------>>", "没有权限");
        }
        inviteMessgeDao = new InviteMessgeDao(this);
//        setDeskIconNum();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mMyinfo = intent.getStringExtra("myinfo");
        home_fragment = intent.getStringExtra("home_fragment");
        gson = new Gson();
        // HttpData();
        if (home_fragment != null && home_fragment.equals("home_fragment")) {
            String tab_bottom = (String) SPUtil.get(MainActivity.this, "tab_bottom", "tab_bottom");
            if (tab_bottom != null && !tab_bottom.equals("tab_bottom")) {
                List<MainButtonBean> bean = gson.fromJson(tab_bottom, new TypeToken<ArrayList<MainButtonBean>>() {
                }.getType());
                if (bean != null && bean.size() > 0) {
                    if (listBean != null && listBean.size() > 0) {
                        listBean.clear();
                        listBean.addAll(bean);
                    } else {
                        listBean = bean;
                    }
                    initEvents();
                }
            }
            return;
        }
        //httpChangeDate();
        unitType = GlobalApplication.getInstance().getPerson(MainActivity.this).getUnitType();
        finishChangeUnittype = (boolean) SPUtil.get(MainActivity.this, "finishChangeUnittype", true);
        if (unitType.equals("2") && !finishChangeUnittype) {
            HttpChangeStatus();
            Log.i("----->", "编辑资料时进程被干掉");
            return;
        }
//        else {
//            httpChangeDate();
//        }
        httpChangeDate();
        /**
         *版本更新
         */
        checkUpdate();
        /**
         * 	设置极光推送的别名
         */
        setAlias();

        setTag();


    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    break;
                case 6002:
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                    }
                    break;
                default:
            }
        }
    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    break;
                case 6002:
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                    }
                    break;
                default:
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                case MSG_SET_TAGS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;
                default:
            }
        }
    };

    private void setAlias() {
        String alias = Utils.GetPhoneNum(MainActivity.this);
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private void setTag() {
        String alias = Utils.GetPhoneNum(MainActivity.this);
        Set<String> tagSet = new LinkedHashSet<String>();
        tagSet.add(alias);
        tagSet.add(GlobalApplication.getInstance().getPerson(MainActivity.this).getUserId());
        tagSet.add(alias);
        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }

    private void getUnreadMessage() {
//        List<EMConversation> list = new ArrayList<>();
//        unReadNumHome = 0;
//        list.addAll(allConversations.values());
//        if (list != null && list.size() > 0) {
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i) != null) {
//                    unReadNumHome += list.get(i).getUnreadMsgCount();
//                }
//            }
//        }
        runOnUiThread(new Runnable() {
            public void run() {
                if (listBean == null || listBean.size() == 0) {
                    return;
                }
                for (int i = 0; i < listBean.size(); i++) {
                    if (listBean.get(i).getMenuCode().equals("FirstPage")) {
                        ImageWordView customView = (ImageWordView) tablayout.getTabAt(i).getCustomView();
                        if (customView != null) {
                            customView.setNum(getUnreadAddressCountTotal());
                        }
                    }
                }

            }
        });
    }

    private void setDeskIconNum() {
        int badgeCount = 0;
        if (!EasyUtils.isAppRunningForeground(this)) {
            //在后台运行
            badgeCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
            if (GlobalApplication.isOpen == 0) {//打开
                model.setSettingMsgNotification(false);//提示音的关闭
                model.setSettingMsgSound(false);
            }
        } else {
            if (GlobalApplication.isOpen == 0) {//打开
                model.setSettingMsgNotification(true);
                model.setSettingMsgSound(true);
            } else {
                model.setSettingMsgNotification(false);//提示音的打开
                model.setSettingMsgSound(false);
            }
            badgeCount = 0;
        }
        ShortcutBadger.applyCount(MainActivity.this, badgeCount);
//        startService(
//                new Intent(MainActivity.this, BadgeIntentService.class).putExtra("badgeCount", badgeCount)
//        );
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatAcitivty.activityInstance != null && ChatAcitivty.activityInstance.userId != null &&
                            username.equals(ChatAcitivty.activityInstance.userId)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatAcitivty.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatAcitivty.activityInstance.finish();
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onFriendRequestAccepted(String s) {

        }

        @Override
        public void onFriendRequestDeclined(String s) {

        }
    }

    private void checkUpdate() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new UpdateManager(MainActivity.this, false).checkUpdate();
            }
        }, 2000);
    }

    /**
     * 切换身份
     */
    private void HttpChangeStatus() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ActionVersion", "3.0");
        new GetNetUtil(hashMap, Api.GetInfo_ChangeUnitType, MainActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                Log.i("----------->", "身份切换-----" + basebean);
                Gson gson = new Gson();
                Basebean<ChangeStatusBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ChangeStatusBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    String unitId = bean.getObj().getUserInfo().getUnitId();
                    String unitType = bean.getObj().getUserInfo().getUnitType();
                    CryptonyBean cryptonyBean = new CryptonyBean();
                    if (!TextUtil.isEmpty(unitId)) {
                        cryptonyBean.setUnitId(unitId);
                    }
                    if (!TextUtil.isEmpty(unitType)) {
                        cryptonyBean.setUnitType(unitType);
                    }
                    if (!TextUtil.isEmpty(bean.getObj().getUserInfo().getShopInfoIsOK())) {
                        SPUtil.putAndApply(MainActivity.this, "shopInfoIsOK", bean.getObj().getUserInfo().getShopInfoIsOK());
                    }
                    GlobalApplication.getInstance().savePerson(cryptonyBean, MainActivity.this);

                    Log.i("-------->>>", "主界面切换身份--调用------" + "unitType----" + unitType +
                            "-----finishChangeUnittype-----" + finishChangeUnittype);
                    /*切换成功身份成功*/
                    SPUtil.remove(MainActivity.this, "tab_bottom");
                    SPUtil.putAndApply(MainActivity.this, "tab_bottom", gson.toJson(bean.getObj().getMenuList()));
                    if (listBean != null && listBean.size() > 0) {
                        listBean.clear();
                        listBean.addAll(bean.getObj().getMenuList());
                        tablayout.removeAllTabs();
                        tablayout.setupWithViewPager(nvpFg);
                        mWgoodsFragmentPagerAdapter = null;
                    } else {
                        listBean = bean.getObj().getMenuList();
                    }
                    initEvents();
//                    httpChangeDate();

                } else {
                    ToastUtils.showToast(MainActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取APP底部菜单数据（有用户登录信息验证）
     */
    private void HttpData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ActionVersion", "3.0");
        new GetNetUtil(hashMap, Api.GetInfo_GetButtomMenuData, MainActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                Gson gson = new Gson();
                BaseListBean<MainButtonBean> bean = gson.fromJson(basebean, new TypeToken<BaseListBean<MainButtonBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    listBean = bean.getObj();
                    initEvents();
                } else {
                    ToastUtils.showToast(MainActivity.this, bean.getMsg());
                }
            }
        };
    }

    private void initEvents() {
        tablayout.setTabTextColors(Color.parseColor(listBean.get(0).getDefaultColor()),
                Color.parseColor(listBean.get(0).getSelectedColor()));
        mWgoodsFragmentPagerAdapter = new WgoodsFragmentPagerAdapter(getSupportFragmentManager(), this, tablayout, listBean, unReadNumHome);
        nvpFg.setAdapter(mWgoodsFragmentPagerAdapter);
        nvpFg.setNoScroll(true);
        tablayout.setupWithViewPager(nvpFg);
        for (int i = 0; i < tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = tablayout.getTabAt(i);
            if (mMyinfo != null && mMyinfo.equals("myinfo")) {
                if (listBean.get(i).getMenuCode().equals("MyInfo")) {
                    tablayout.getTabAt(i).select();
                    mWgoodsFragmentPagerAdapter.getTabView(i).setSelected(true);
                }
            }
            if (home_fragment != null && home_fragment.equals("home_fragment")) {
                if (listBean.get(i).getMenuCode().equals("FirstPage")) {
                    tablayout.getTabAt(i).select();
                    mWgoodsFragmentPagerAdapter.getTabView(i).setSelected(true);
                }
            }
            tab.setCustomView(mWgoodsFragmentPagerAdapter.getTabView(i));
            getUnreadMessage();
        }
    }

    // 点击两次返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GlobalApplication.getInstance().exitWithDoubleClick();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = alpha;
        this.getWindow().setAttributes(lp);
    }

    @Override
    public void getNum(int num, String fromFriends) {
        for (int i = 0; i < listBean.size(); i++) {
            if (listBean.get(i).getMenuCode().equals(fromFriends)) {
                ImageWordView customView = (ImageWordView) tablayout.getTabAt(i).getCustomView();
                customView.setNum(num);
            }
        }
        if (fromFriends.equals("FriendList")) {
            this.unReadFriends = num;
        }
    }

    public int getUnReadFriends() {
        return unReadFriends;
    }

    public void setUnReadFriends(int unReadFriends) {
        this.unReadFriends = unReadFriends;
    }

    public List<MainButtonBean> getListBean() {
        return listBean;
    }

    /**
     * 获取APP底部菜单数据（有用户登录信息验证）
     */

    public void httpChangeDate() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ActionVersion", "3.0");
        new GetNetUtil(hashMap, Api.GetInfo_GetButtomMenuData, MainActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                BaseListBean<MainButtonBean> bean = gson.fromJson(basebean, new TypeToken<BaseListBean<MainButtonBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    SPUtil.remove(MainActivity.this, "tab_bottom");
                    SPUtil.putAndApply(MainActivity.this, "tab_bottom", gson.toJson(bean.getObj()));
                    if (listBean != null && listBean.size() > 0) {
                        listBean.clear();
                        listBean.addAll(bean.getObj());
                        tablayout.removeAllTabs();
//                        tablayout.setupWithViewPager(nvpFg);
                        mWgoodsFragmentPagerAdapter = null;
                    } else {
                        listBean = bean.getObj();
                    }
                    initEvents();
                } else {
                    ToastUtils.showToast(MainActivity.this, bean.getMsg());
                }
            }
        };
    }

    protected boolean has_write_external_storage_Permission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void apply_write_external_storage_Permission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListenerAdapter);
        EMClient.getInstance().removeConnectionListener(emConnectionListener);
    }

    @Override
    public void onNetChange(int netMobile) {
        // TODO Auto-generated method stub
        if (netMobile == -1) {
            Log.i("=====", "网络断开");
        } else {
            Log.i("=====", "网络连接");
        }
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

}