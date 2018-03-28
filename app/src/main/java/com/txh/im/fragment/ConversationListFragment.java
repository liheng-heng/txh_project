package com.txh.im.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.ChatAcitivty;
import com.txh.im.activity.MainActivity;
import com.txh.im.activity.QRCodeFriendsAcitivty;
import com.txh.im.activity.SearchAddedFriendsAcitivty;
import com.txh.im.activity.SearchUserFristActivity;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.HomeBean;
import com.txh.im.bean.MainButtonBean;
import com.txh.im.easeui.Constant;
import com.txh.im.easeui.DemoHelper;
import com.txh.im.easeui.PermissionsManager;
import com.txh.im.easeui.PermissionsResultAction;
import com.txh.im.easeui.db.InviteMessgeDao;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ThreadUtils;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.CustomPopWindow;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息列表,代替之前的HomeFragment,集成easeui可以用这个列表
 */

public class ConversationListFragment extends EaseConversationListFragment {

    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
    @Bind(R.id.tv_head_back)
    TextView tvHeadBack;
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_right)
    ImageView headRight;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.rl_head_title)
    RelativeLayout rlHeadTitle;
    @Bind(R.id.tv_search_content)
    TextView tvSearchContent;
    @Bind(R.id.rl_sv)
    RelativeLayout rlSv;
    //    private TextView errorText;
    private Context mContext;
    private MainActivity activity;
    private List<MainButtonBean> listBean;
    private List<HomeBean> obj;
    private CustomPopWindow mCustomPopWindow;
    private View contentView;
    private CustomPopWindow.PopupWindowBuilder popupWindowBuilder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        listBean = activity.getListBean();
    }

    @Override
    protected void initView() {
        super.initView();
//        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        View title_search = (LinearLayout) View.inflate(getActivity(), R.layout.title_search, null);
//        errorItemContainer.addView(errorView);
//        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        TitleContainer.addView(title_search);
        ButterKnife.bind(this, title_search);
        initTitle();
    }

    private void initTitle() {
        mContext = getActivity();
        llHeadBack.setVisibility(View.GONE);
        headRight.setVisibility(View.VISIBLE);
        if (listBean == null)
            return;
        for (int i = 0; i < listBean.size(); i++) {
            if (listBean.get(i).getMenuCode().equals("FirstPage")) {
                headTitle.setText(listBean.get(i).getTitle());
            }
        }
        contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_home, null);
        //创建并显示popWindow
        popupWindowBuilder = new CustomPopWindow.PopupWindowBuilder(mContext);
        EMClient.getInstance().addConnectionListener(emConnectionListener);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        //隐藏easeui的头部
        hideTitleBar();
        errorItemContainer.setVisibility(View.GONE);
        // register context menu
        registerForContextMenu(conversationListView);
        //点击事件
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String userId = conversation.conversationId();
                if (userId.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatAcitivty.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        } else {
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, userId);
                    startActivity(intent);
                }
            }
        });
        getNetRequest(0);
        super.setUpView();
        ptorefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                // 结束刷新
                if (NetUtils.hasNetwork(getContext())) {
                    getNetRequest(1);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptorefresh.finishRefresh();
                        }
                    }, 200);//无网的时候给个效果就好了
                }
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        ptorefresh.finishLoadMore();
                    }
                }, 1);
            }
        });
        requestPermissions();
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
//        if (NetUtils.hasNetwork(getActivity())) {
//            errorText.setText(R.string.can_not_connect_chat_server_connection);
//        } else {
//            errorText.setText(R.string.the_current_network);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count,更新未读消息数
//        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }

    public void getNetRequest(final int num) {
        StringBuffer sb = new StringBuffer();
        final int size = conversationList.size();
        if (size == 0) {
            ptorefresh.finishRefresh();
            return;
        }
        for (int i = 0; i < size; i++) {
            sb.append(conversationList.get(i).conversationId());
            if (i < size - 1) {
                sb.append(",");
            }
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("Ids", sb.toString());
        new GetNetUtil(map, Api.TX_App_SNS_IndexMessage, mContext) {
            @Override
            public void errorHandle() {
                super.errorHandle();
                if (num == 1) {
                    ptorefresh.finishRefresh();
                }
            }

            @Override
            public void successHandle(String base) {
                if (!ConversationListFragment.this.isVisible()) {
                    return;
                }
                if (num == 1) {
                    ptorefresh.finishRefresh();
                }
//                BaseListBean<HomeSingleListBean> basebean = gson.fromJson(base,BaseListBean<HomeSingleListBean>[]);
                Gson gson = new Gson();
                BaseListBean<HomeBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<HomeBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    if (obj != null) {
                        obj.clear();
                        obj.addAll(basebean.getObj());
                    } else {
                        obj = basebean.getObj();
                    }
                    if (obj == null || obj.size() == 0) {
                        ToastUtils.showToast(mContext, basebean.getMsg());
                        return;
                    }
//                    Map<String, EaseUser> contactList = DemoHelper.getInstance().getContactList();
//                    contactList.clear();

                    for (int i = 0; i < obj.size(); i++) {
                        HomeBean homeBean = obj.get(i);
                        try {
//                            UserDao dao = new UserDao(mContext);
                            EaseUser easeUser = new EaseUser(homeBean.getUserId());
                            easeUser.setAvatar(homeBean.getImagesHead());
                            easeUser.setNick(homeBean.getUserName());
//                            dao.saveContact(easeUser);
//                            EaseUser user = EaseUI.getInstance().getUserProfileProvider().getUser(homeBean.getUserId());
//                            user.setAvatar(homeBean.getImagesHead());
//                            user.setNick(homeBean.getUserName());
                            // 通知listeners联系人同步完毕
//                            DemoHelper.getInstance().getModel().setContactSynced(true);


//                            DemoHelper.getInstance().notifyContactsSyncListener(true);
                            //保存联系人头像，昵称
                            DemoHelper.getInstance().saveContact(easeUser);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // 通知listeners联系人同步完毕
                    DemoHelper.getInstance().getModel().setContactSynced(true);


                    DemoHelper.getInstance().notifyContactsSyncListener(true);
                    refresh();
                }
            }
        };
    }

    //    @Override
//    protected void refreshDao() {
//        getNetRequest(0);
//        super.refreshDao();
//    }
    @OnClick({R.id.head_right, R.id.rl_sv})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_right:
                //处理popWindow 显示内容
                handleLogic(contentView);
                mCustomPopWindow = popupWindowBuilder
                        .setView(contentView)
                        .create()
                        .showAsDropDown(headRight, -210, -12);
                break;
            case R.id.rl_sv:
                intent = new Intent();
                intent.setClass(mContext, SearchAddedFriendsAcitivty.class);
                startActivity(intent);
                break;
        }
    }

    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.rl_home_sacn:
                        Intent intent1 = new Intent();
                        intent1.setClass(mContext, QRCodeFriendsAcitivty.class);
                        startActivity(intent1);
                        break;
                    case R.id.rl_add_friends:
                        Intent intent = new Intent();
                        intent.setClass(mContext, SearchUserFristActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        contentView.findViewById(R.id.rl_home_sacn).setOnClickListener(listener);
        contentView.findViewById(R.id.rl_add_friends).setOnClickListener(listener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ButterKnife.unbind(this);
        EMClient.getInstance().removeConnectionListener(emConnectionListener);
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(activity, new PermissionsResultAction() {
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
                        if (NetUtils.hasNetwork(mContext)) {
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
                            if (NetUtils.hasNetwork(mContext)) {
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
}