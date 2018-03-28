package com.txh.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.util.NetUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.MainActivity;
import com.txh.im.activity.NewFriendsAcitivty;
import com.txh.im.activity.QRCodeFriendsAcitivty;
import com.txh.im.activity.SearchAddedFriendsAcitivty;
import com.txh.im.activity.SearchUserFristActivity;
import com.txh.im.adapter.HailFellowAdapter;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.GetAskListBean;
import com.txh.im.bean.GetAskNotReadCountBean;
import com.txh.im.bean.HailFellowBean;
import com.txh.im.bean.MainButtonBean;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.widget.CustomPopWindow;
import com.txh.im.widget.FullyLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/1/4.
 * 朋友
 */

public class HailFellowFragment extends BasicFragment {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_search_content)
    TextView tvSearchContent;
    @Bind(R.id.rc)
    RecyclerView rc;
    @Bind(R.id.head_right)
    ImageView headRight;
    @Bind(R.id.tv_unread_count)
    TextView tvUnreadCount;
    @Bind(R.id.srl)
    PullToRefreshLayout srl;
    private MainActivity activity;
    private List<MainButtonBean> listBean;
    private CustomPopWindow mCustomPopWindow;
    private View contentView;
    private CustomPopWindow.PopupWindowBuilder popupWindowBuilder;
    private int mObj;
    private List<HailFellowBean> list;
    private HailFellowAdapter hailFellowAdapter;
    private boolean refershCount;
    private boolean refreshList;
    private Gson gson;
    private List<GetAskListBean> obj;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        listBean = activity.getListBean();
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_hail_fellow, null);
        contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_home, null);
        popupWindowBuilder = new CustomPopWindow.PopupWindowBuilder(mContext);
        return view;
    }

    @Override
    protected void initTitle() {
        //注册EventBus
        EventBus.getDefault().register(this);
        llHeadBack.setVisibility(View.GONE);

        if (listBean != null && listBean.size() > 0) {
            for (int i = 0; i < listBean.size(); i++) {
                if (listBean.get(i).getMenuCode().equals("FriendList")) {
                    headTitle.setText(listBean.get(i).getTitle());
                }
            }
        }
    }

    @Subscribe
    public void onEventMainThread(FirstEventBus event) {
        if (event != null) {
            if (event.getMsg().equals("FirstEventBus")) {
                hasHistory();
                getUnreadCountAskMessage(0);
                getNetRequest(0);
                toNewFriendsList();
            } else if (event.getMsg().equals("UserRemarkAcitivity")) {
                getNetRequest(0);//备注之后刷新
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            getUnreadCountAskMessage(0);
        }
    }

    @Override
    protected void initData() {
        refershCount = false;
        refreshList = false;
        gson = new Gson();
        headRight.setVisibility(View.VISIBLE);
        srl.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                // 结束刷新
                if (NetUtils.hasNetwork(mContext)) {
                    getUnreadCountAskMessage(1);
                    getNetRequest(1);
                    //toNewFriendsList();//请求新的朋友
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            srl.finishRefresh();
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
                        srl.finishLoadMore();
                    }
                }, 1);
            }
        });
        srl.setCanLoadMore(false);//禁止加载更多
        rc.setItemAnimator(new DefaultItemAnimator());
        rc.setLayoutManager(new FullyLinearLayoutManager(mContext));
        hasHistory();
        // getUnreadCountAskMessage(0);
        getNetRequest(0);
        toNewFriendsList();
        mObj = activity.getUnReadFriends();
        if (mObj > 0) {
            tvUnreadCount.setVisibility(View.VISIBLE);
        } else {
            tvUnreadCount.setVisibility(View.GONE);
        }
    }

    private void hasHistory() {
        String friends_list = (String) SPUtil.get(mContext, "friends_list", "friends_list");
        if (friends_list != null && !friends_list.equals("friends_list")) {
            List<HailFellowBean> basebean = gson.fromJson(friends_list, new TypeToken<ArrayList<HailFellowBean>>() {
            }.getType());
            if (list != null && list.size() > 0) {
                list.clear();
                list.addAll(basebean);
            } else {
                list = basebean;
            }
            if (list != null && list.size() > 0) {
                if (hailFellowAdapter == null) {
                    hailFellowAdapter = new HailFellowAdapter(mContext, list);
                    if (rc != null) {
                        rc.setAdapter(hailFellowAdapter);
                    }
                } else {
                    hailFellowAdapter.refrsh(list);
                    hailFellowAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @OnClick({R.id.rl_sv, R.id.ll_new_friends, R.id.head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sv:
                Intent intent = new Intent();
                intent.setClass(mContext, SearchAddedFriendsAcitivty.class);
                startActivity(intent);
                break;
            
            case R.id.ll_new_friends:
                if (mObj > 0) {
                    mObj = 0;
                    tvUnreadCount.setVisibility(View.GONE);
                    activity.getNum(mObj, "FriendList");
                }
                SetAskReaded();
                Intent intent1 = new Intent(mContext, NewFriendsAcitivty.class);
                intent1.putExtra("asklist", gson.toJson(obj));
                startActivity(intent1);
                break;

            case R.id.head_right:
                //处理popWindow 显示内容
                handleLogic(contentView);
                mCustomPopWindow = popupWindowBuilder
                        .setView(contentView)
                        .create()
                        .showAsDropDown(headRight, -210, -12);
                break;
        }
    }

    //请求新的朋友列表
    private void toNewFriendsList() {
        new GetNetUtil(null, Api.TX_App_SNS_GetAskList, mContext) {
            @Override
            public void successHandle(String base) {
                Gson gson = new Gson();
                BaseListBean<GetAskListBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<GetAskListBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    obj = basebean.getObj();
                } else {
                    // toast(basebean.getMsg());
                }
            }
        };
    }

    private void SetAskReaded() {
        new GetNetUtil(null, Api.TX_App_SNS_SetAskReaded, mContext) {
            @Override
            public void successHandle(String base) {
                Gson gson = new Gson();
                BaseListBean<GetAskListBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<GetAskListBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    mObj = 0;
                    activity.getNum(mObj, "FriendList");
                } else {
                    //toast(basebean.getMsg());
                }
            }
        };
    }

    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                Intent intent = new Intent();
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

    public void getNetRequest(final int i) {
        //                    list_brand.addAll(list_brand);
        new GetNetUtil(null, Api.TX_App_SNS_GetFriendList, mContext) {
            @Override
            public void successHandle(String base) {
                if (HailFellowFragment.this.isDestroy) {
                    return;
                }
                if (i == 1) {
                    refreshList = true;
                }
                if (refreshList && refershCount) {
                    srl.finishRefresh();
                    refreshList = false;
                    refershCount = false;
                }
                BaseListBean<HailFellowBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<HailFellowBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    SPUtil.remove(mContext, "friends_list");
                    SPUtil.putAndApply(mContext, "friends_list", gson.toJson(basebean.getObj()));
                    if (list != null && list.size() > 0) {
                        list.clear();
                        list.addAll(basebean.getObj());
                    } else {
                        list = basebean.getObj();
                    }
                    if (hailFellowAdapter == null) {
                        hailFellowAdapter = new HailFellowAdapter(mContext, list);
                        if (rc != null) {
                            rc.setAdapter(hailFellowAdapter);
                        }
                    } else {
                        hailFellowAdapter.refrsh(list);
                        hailFellowAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (i == 1) {
                        srl.finishRefresh();
                    }
                }
            }
        };

    }

    private void getUnreadCountAskMessage(final int i) {
        new GetNetUtil(null, Api.TX_App_SNS_GetAskNotReadCount, GlobalApplication.getInstance().getApplicationContext()) {
            @Override
            public void successHandle(String base) {
                if (HailFellowFragment.this.isDestroy) {
                    return;
                }
                if (i == 1) {
                    refershCount = true;
                }
                if (refreshList && refershCount) {
                    srl.finishRefresh();
                    refreshList = false;
                    refershCount = false;
                }
                Gson gson = new Gson();
                GetAskNotReadCountBean basebean = gson.fromJson(base, GetAskNotReadCountBean.class);
                mObj = basebean.getObj();
                if (basebean.getStatus().equals("200")) {
                    if (mObj > 0) {
                        tvUnreadCount.setVisibility(View.VISIBLE);
                        if (mObj < 99) {
                            tvUnreadCount.setText(mObj + "");
                        } else if (mObj > 99) {
                            tvUnreadCount.setText("99+");
                        }
                        activity.getNum(mObj, "FriendList");
                    } else {
                        mObj = 0;
                        if (tvUnreadCount != null) {
                            tvUnreadCount.setVisibility(View.GONE);
                        }
                    }
                } else {
//                    toast(basebean.getMsg());
                    if (i == 1) {
                        srl.finishRefresh();
                    }
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
}