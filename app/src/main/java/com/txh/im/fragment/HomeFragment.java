package com.txh.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gxz.library.SwapRecyclerView;
import com.gxz.library.SwipeMenuBuilder;
import com.gxz.library.bean.SwipeMenu;
import com.gxz.library.bean.SwipeMenuItem;
import com.gxz.library.view.SwipeMenuView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.MainActivity;
import com.txh.im.activity.QRCodeFriendsAcitivty;
import com.txh.im.activity.SearchAddedFriendsAcitivty;
import com.txh.im.activity.SearchUserFristActivity;
import com.txh.im.adapter.EMMessageListenerAdapter;
import com.txh.im.adapter.HomeListAdapter;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.GetAskNotReadCountBean;
import com.txh.im.bean.HomeBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.MainButtonBean;
import com.txh.im.dao.SearchHistoryDao;
import com.txh.im.presenter.ConversationPresenter;
import com.txh.im.presenter.impl.ConversationPresenterImpl;
import com.txh.im.utils.DateUtils;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ThreadUtils;
import com.txh.im.utils.ToastUtils;
import com.txh.im.view.ConversationView;
import com.txh.im.widget.CustomPopWindow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jiajia on 2016/12/29.
 */

public class HomeFragment extends BaseFragment implements ConversationView, SwipeMenuBuilder {
    @Bind(R.id.ll_head_back)
    LinearLayout back;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_right)
    ImageView headRight;
    @Bind(R.id.rc_home)
    SwapRecyclerView rcHome;
    @Bind(R.id.srl)
    PullToRefreshLayout srl;
    private CustomPopWindow mCustomPopWindow;
    private View contentView;
    private CustomPopWindow.PopupWindowBuilder popupWindowBuilder;
    private Intent intent;
    private ConversationPresenter conversationPresenter;
    private HomeListAdapter homeListAdapter;
    private List<EMConversation> conversations;
    private List<HomeSingleListBean> history;
    private int allUnreadMessagenumber;
    private MainActivity activity;
    private List<MainButtonBean> listBean;
    private int pos;
    private Handler mHandler = new Handler();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        listBean = activity.getListBean();
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_home, null);
        contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_home, null);
        //创建并显示popWindow
        popupWindowBuilder = new CustomPopWindow.PopupWindowBuilder(mContext);
        intent = new Intent();
        return view;
    }

    @Override
    protected void initTitle() {
        back.setVisibility(View.GONE);
        headRight.setVisibility(View.VISIBLE);
        if (listBean == null)
            return;
        for (int i = 0; i < listBean.size(); i++) {
            if (listBean.get(i).getMenuCode().equals("FirstPage")) {
                headTitle.setText(listBean.get(i).getTitle());
            }
        }
    }

    @Override
    protected void initData() {
        conversationPresenter = new ConversationPresenterImpl(this);
        rcHome.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rcHome.setItemAnimator(new DefaultItemAnimator());
        conversations = conversationPresenter.getConversations();
        //        查询数据库
        hasHistory();
        conversationPresenter.loadAllConversations();
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
        initRefrsh();
        listClick();
        rcHome.setOnSwipeListener(new SwapRecyclerView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {
                pos = position;
            }
        });
        getUnreadCountAskMessage();
        Log.i("jiajia66", GlobalApplication.getInstance().getPerson(mContext).getUserId());
    }

    private void initRefrsh() {

        srl.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }

    // 处理adapter的点击事件


    private void listClick() {
        homeListAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                conversationPresenter.loadAllConversations();
                homeListAdapter.onClickContent(position);
            }
        });
    }

    private SwipeMenuView.OnMenuItemClickListener mOnSwipeItemClickListener = new SwipeMenuView.OnMenuItemClickListener() {

        @Override
        public void onMenuItemClick(final int pos, SwipeMenu menu, int index) {
            rcHome.smoothCloseMenu(pos);
            homeListAdapter.removeItem(pos);
        }
    };

    //构造SwipeMenuView
    @Override
    public SwipeMenuView create() {

        SwipeMenu menu = new SwipeMenu(mContext);

        SwipeMenuItem item = new SwipeMenuItem(mContext);
        item.setTitle("删除")
                .setTitleColor(Color.WHITE)
                .setTitleSize(16)
                .setWidth(160)
                .setBackground(getResources().getDrawable(R.color.red3));
        menu.addMenuItem(item);

        SwipeMenuView menuView = new SwipeMenuView(menu);

        menuView.setOnMenuItemClickListener(mOnSwipeItemClickListener);

        return menuView;
    }

    private void hasHistory() {
        history = SearchHistoryDao.getInstance().getHistory();
        if (history.size() > 0) {
            if (!history.get(0).getMySelfUserId().equals(GlobalApplication.getInstance().getPerson(mContext).getUserId())) {
                SearchHistoryDao.getInstance().clear();
                history.clear();
            }
        }
        homeListAdapter = new HomeListAdapter(mContext, history, this);
        rcHome.setAdapter(homeListAdapter);
    }

    private void getNetRequest() {
        StringBuffer sb = new StringBuffer();
        final int size = conversations.size();
        if (size == 0) {
            return;
        }
        for (int i = 0; i < size; i++) {
            sb.append(conversations.get(i).conversationId());
            if (i < size - 1) {
                sb.append(",");
            }
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("Ids", sb.toString());
        new GetNetUtil(map, Api.TX_App_SNS_IndexMessage, mContext) {
            @Override
            public void successHandle(String base) {
                if (HomeFragment.this.isDestroy) {
                    return;
                }
//                BaseListBean<HomeSingleListBean> basebean = gson.fromJson(base,BaseListBean<HomeSingleListBean>[]);
                Gson gson = new Gson();
                BaseListBean<HomeBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<HomeBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    SearchHistoryDao.getInstance().clear();
                    allUnreadMessagenumber = 0;
                    List<HomeSingleListBean> list = new ArrayList<>();
                    list.clear();
                    List<HomeBean> obj = basebean.getObj();
                    if (obj == null || obj.size() == 0) {
                        ToastUtils.showToast(mContext, basebean.getMsg());
                        return;
                    }
                    for (int i = 0; i < obj.size(); i++) {
                        HomeBean homeBean = obj.get(i);
                        HomeSingleListBean ho = new HomeSingleListBean();
                        try {
                            ho.setUserId(homeBean.getUserId());
                            ho.setUserName(homeBean.getUserName());
                            ho.setImagesHead(homeBean.getImagesHead());
                            ho.setIsShop(homeBean.getIsShop());
                            ho.setIcon(homeBean.getIcon());
                            ho.setShops(gson.toJson(homeBean.getShops()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        list.add(ho);
                    }
                    cc:
                    for (int i = conversations.size() - 1; i >= 0; i--) {
                        for (int j = list.size() - 1; j >= 0; j--) {
                            if (conversations.get(i).conversationId().equals(list.get(j).getUserId())) {
                                HomeSingleListBean homeSingleListBean = list.get(j);
                                EMConversation emConversation = conversations.get(i);
                                EMMessage lastMessage = emConversation.getLastMessage();
//                                if (lastMessage == null || lastMessage.getBody() == null) {
//                                    return;
//                                }
                                try {
                                    homeSingleListBean.setMySelfUserId(GlobalApplication.getInstance().getPerson(mContext).getUserId());
                                    if (lastMessage.getBody() instanceof EMTextMessageBody) {
                                        homeSingleListBean.setLastMessage(((EMTextMessageBody) lastMessage.getBody()).getMessage());
                                    } else if (lastMessage.getBody() instanceof EMImageMessageBody) {
                                        homeSingleListBean.setLastMessage("[图片]");
                                    } else if (lastMessage.getBody() instanceof EMVoiceMessageBody) {
                                        homeSingleListBean.setLastMessage("[语音]");
                                    } else {
                                        homeSingleListBean.setLastMessage("非文本消息");
                                    }
                                    int unreadMsgCount = emConversation.getUnreadMsgCount();
                                    allUnreadMessagenumber += unreadMsgCount;
                                    if (unreadMsgCount > 0 && unreadMsgCount <= 99) {
                                        homeSingleListBean.setUnreadMessageCount(unreadMsgCount + "");
                                    } else if (unreadMsgCount > 99) {
                                        homeSingleListBean.setUnreadMessageCount("99+");
                                    }
                                    homeSingleListBean.setLastUpdateTime(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
                                    SearchHistoryDao.getInstance().addHistory(homeSingleListBean);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                continue cc;
                            }
                        }
                    }
                    activity.getNum(allUnreadMessagenumber, "FirstPage");
                    history.clear();
                    history.addAll(SearchHistoryDao.getInstance().getHistory());
                    homeListAdapter.setList(history, mContext);
                    homeListAdapter.notifyDataSetChanged();
//                    hasHistory();
                } else {
//                    toast(basebean.getMsg());
                }
            }
        };
    }

    @OnClick({R.id.head_right, R.id.rl_sv})
    public void onClick(View view) {
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
                        if (hasCameraPermission()) {
                            intent.setClass(mContext, QRCodeFriendsAcitivty.class);
                            startActivity(intent);
                        } else {
                            applyCameraPersion();
                        }
                        break;
                    case R.id.rl_add_friends:
                        intent.setClass(mContext, SearchUserFristActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        contentView.findViewById(R.id.rl_home_sacn).setOnClickListener(listener);
        contentView.findViewById(R.id.rl_add_friends).setOnClickListener(listener);
    }

    /**
     * 申请权限回调
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CAMERA_STATE:
//                if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
//                    intent.setClass(mContext, QRCodeFriendsAcitivty.class);
//                    startActivity(intent);
//                } else {
//                    toast("未获取到权限");
//                }
//                break;
//        }
//    }

    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {

        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(mContext, "收到消息", Toast.LENGTH_SHORT).show();
                    conversationPresenter.loadAllConversations();
                }
            });
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            super.onMessageChanged(message, change);
            conversationPresenter.loadAllConversations();
//            getNetRequest();
        }
    };

    @Override
    public void onAllConversationsLoaded() {
        if (conversations.size() > 0) {
            getNetRequest();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (conversationPresenter != null) {
            conversationPresenter.loadAllConversations();
            getNetRequest();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListenerAdapter);
    }

    private void getUnreadCountAskMessage() {
        new GetNetUtil(null, Api.TX_App_SNS_GetAskNotReadCount, mContext) {
            @Override
            public void successHandle(String base) {
                if (HomeFragment.this.isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                GetAskNotReadCountBean basebean = gson.fromJson(base, GetAskNotReadCountBean.class);
                int obj = basebean.getObj();
                if (basebean.getStatus().equals("200")) {
                    if (obj > 0) {
                        activity.getNum(obj, "FriendList");
                    }
                }
            }
        };
    }
}