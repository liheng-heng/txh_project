package com.txh.im.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gxz.library.SwapRecyclerView;
import com.gxz.library.SwipeMenuBuilder;
import com.gxz.library.bean.SwipeMenu;
import com.gxz.library.bean.SwipeMenuItem;
import com.gxz.library.view.SwipeMenuView;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.NewFriendsAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.GetAskListBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.widget.CustomPopWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by jiajia on 2017/4/5.
 */

public class NewFriendsAcitivty extends BasicActivity implements SwipeMenuBuilder {

    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_right)
    ImageView headRight;
    @Bind(R.id.tv_search_content)
    TextView tvSearchContent;
    @Bind(R.id.rc_home)
    public SwapRecyclerView rcHome;
    private Gson gson;
    private NewFriendsAdapter newFriendsAdapter;
    private CustomPopWindow mCustomPopWindow;
    private View contentView;
    private CustomPopWindow.PopupWindowBuilder popupWindowBuilder;
    private List<GetAskListBean> list;
    private int pos;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_new_friends);
        contentView = LayoutInflater.from(this).inflate(R.layout.pop_home, null);
        popupWindowBuilder = new CustomPopWindow.PopupWindowBuilder(this);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("新的朋友");
        headRight.setVisibility(View.VISIBLE);
        tvSearchContent.setText("手机号");
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        rcHome.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        rcHome.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rcHome.setItemAnimator(new DefaultItemAnimator());
        Intent intent = getIntent();
        String asklist = intent.getStringExtra("asklist");
        gson = new Gson();
        list = gson.fromJson(asklist, new TypeToken<List<GetAskListBean>>() {
        }.getType());
        toNewFriendsList();
        if (list == null || list.size() == 0) {
            return;
        }
        newFriendsAdapter = new NewFriendsAdapter(this, list);
        rcHome.setAdapter(newFriendsAdapter);
        rcHome.setOnSwipeListener(new SwapRecyclerView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {
                pos = position;
            }
        });
        newFriendsAdapter.setOnItemClickListener(new NewFriendsAdapter.OnItemClickListener<GetAskListBean>() {

            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, GetAskListBean getAskListBean, int position) {

                if (getAskListBean.getStatus() == 1) {
                    //已经是好友了
                    Intent intent = new Intent();
                    intent.setClass(NewFriendsAcitivty.this, FriendsDataAddWebviewActivity.class);
                    intent.putExtra("friendId", getAskListBean.getFromUserId());
                    intent.putExtra("FriendUnitId", getAskListBean.getFromUnitId());
                    intent.putExtra("intenttype", "3");
                    intent.putExtra("intenttype3_where", "1");
                    startActivity(intent);
                    Log.v("=========>>", "4-好友请求--已经添加");
                } else {
                    //非好友
                    Intent intent = new Intent();
                    intent.setClass(NewFriendsAcitivty.this, FriendsDataAddWebviewActivity.class);
                    intent.putExtra("AskId", getAskListBean.getFId());
                    intent.putExtra("Status", getAskListBean.getStatus() + "");
                    intent.putExtra("intenttype", "2");
                    startActivity(intent);
                    Log.v("=========>>", "好友请求---非好友跳转");
                }

//                Intent intent = new Intent();
//                switch (getAskListBean.getStatus()) {
//                    case -1://"已过期"
//                        intent.setClass(NewFriendsAcitivty.this, FriendsAskOverdueAcitivity.class);
//                        intent.putExtra("friendask", gson.toJson(getAskListBean));
//                        startActivity(intent);
//                        break;
//
//                    case 0://接受
//                        intent.setClass(NewFriendsAcitivty.this, FriendsAskAcitivity.class);
//                        intent.putExtra("friendask", gson.toJson(getAskListBean));
//                        startActivity(intent);
//                        finish();
//                        break;
//
//                    case 1://已添加
//                        HashMap<String, String> map = new HashMap<>();
//                        map.put("FriendId", getAskListBean.getFromUserId());
//                        map.put("FriendUnitId", getAskListBean.getFromUnitId());
//                        new GetNetUtil(map, Api.TX_App_SNS_GetFriendDetail, NewFriendsAcitivty.this) {
//                            @Override
//                            public void successHandle(String base) {
//                                if (isDestroy) {
//                                    return;
//                                }
//                                Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
//                                }.getType());
//                                if (basebean.getStatus().equals("200")) {
//                                    Intent intent = new Intent(NewFriendsAcitivty.this, FriendsDataAddActivity.class);
//                                    HomeSingleListBean obj = basebean.getObj();
//                                    obj.setIsFriend("1");
//                                    intent.putExtra("homeSingleListBean", gson.toJson(obj));
//                                    startActivity(intent);
//                                } else {
//                                    toast(basebean.getMsg());
//                                }
//                            }
//                        };
//                        break;
//                }

            }
        });
    }

    @OnClick({R.id.head_right, R.id.rl_sv, R.id.ll_head_back})
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
                Intent intent = new Intent();
                intent.setClass(NewFriendsAcitivty.this, SearchUserFristActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_head_back:
                EventBus.getDefault().post(
                        new FirstEventBus("FirstEventBus"));
                finish();
                break;

        }
    }

    //请求新的朋友列表
    private void toNewFriendsList() {
        new GetNetUtil(null, Api.TX_App_SNS_GetAskList, this) {
            @Override
            public void successHandle(String base) {
                if (isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                BaseListBean<GetAskListBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<GetAskListBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    if (basebean.getObj() == null || basebean.getObj().size() == 0) {
                        return;
                    }
                    if (list != null && list.size() > 0) {
                        list.clear();
                        list.addAll(basebean.getObj());
                        newFriendsAdapter.refresh(list);
                        newFriendsAdapter.notifyDataSetChanged();
                    } else {
                        newFriendsAdapter = new NewFriendsAdapter(NewFriendsAcitivty.this, basebean.getObj());
                        rcHome.setAdapter(newFriendsAdapter);
                    }
                } else {
                    // toast(basebean.getMsg());
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
                            intent.setClass(NewFriendsAcitivty.this, QRCodeFriendsAcitivty.class);
                            startActivity(intent);
                        } else {
                            applyCameraPersion();
                        }
                        break;
                    case R.id.rl_add_friends:
                        intent.setClass(NewFriendsAcitivty.this, SearchUserFristActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        contentView.findViewById(R.id.rl_home_sacn).setOnClickListener(listener);
        contentView.findViewById(R.id.rl_add_friends).setOnClickListener(listener);
    }

    private SwipeMenuView.OnMenuItemClickListener mOnSwipeItemClickListener = new SwipeMenuView.OnMenuItemClickListener() {

        @Override
        public void onMenuItemClick(final int pos, SwipeMenu menu, int index) {
            showProgress("正在加载");
            HashMap<String, String> map = new HashMap<>();
            map.put("FId", list.get(pos).getFId());
            new GetNetUtil(map, Api.TX_App_SNS_DeleteAsk, NewFriendsAcitivty.this) {
                @Override
                public void successHandle(String base) {
                    if (NewFriendsAcitivty.this.isFinishing()) {
                        return;
                    }
                    hideProgress();
                    HttpBean basebean = gson.fromJson(base, HttpBean.class);
                    if (basebean.getStatus().equals("200")) {
                        rcHome.smoothCloseMenu(pos);
                        list.remove(pos);
                        newFriendsAdapter.remove(pos);
                    } else {
                        toast(basebean.getMsg());
                    }
                }
            };
        }
    };

    //构造SwipeMenuView
    @Override
    public SwipeMenuView create() {

        SwipeMenu menu = new SwipeMenu(this);

        SwipeMenuItem item = new SwipeMenuItem(this);
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

    @Subscribe
    public void onEventMainThread(FirstEventBus event) {
        if (event != null && event.getMsg().equals("NewFriendsAcitivty")) {
            toNewFriendsList();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
