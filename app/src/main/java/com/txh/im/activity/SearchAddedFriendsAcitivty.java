package com.txh.im.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.FriendsAddExpandableListview;
import com.txh.im.adapter.SearchHistoryListAdapter;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.FriendsShopBean3;
import com.txh.im.bean.SearchHistoryBean;
import com.txh.im.dao.SearchHistoryStringDao;
import com.txh.im.listener.HistoryStringListener;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.Utils;
import com.txh.im.widget.SearchView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 对于历史纪录需要判断是不是和上次是同一个人，如果不是就清楚本地聊天记录
 */
public class SearchAddedFriendsAcitivty extends BasicActivity {

    @Bind(R.id.search_back)
    LinearLayout searchBack;
    @Bind(R.id.sv)
    SearchView sv;
    @Bind(R.id.iv_clear_search)
    ImageView ivClearSearch;
    @Bind(R.id.function)
    TextView function;
    @Bind(R.id.ll_search_head)
    LinearLayout llSearchHead;
    @Bind(R.id.ll_first_search)
    LinearLayout llFirstSearch;
    @Bind(R.id.elv)
    ExpandableListView elv;
    @Bind(R.id.activity_search_added_friends_acitivty)
    LinearLayout activitySearchAddedFriendsAcitivty;
    @Bind(R.id.tv_msg)
    TextView tvMsg;
    @Bind(R.id.rc_history)
    RecyclerView rcHistory;
    @Bind(R.id.btn_clear)
    Button btnClear;
    @Bind(R.id.imageView)
    ImageView imageView;
    private List<SearchHistoryBean> history;
    private HistoryStringListener historyStringListener;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_added_friends_acitivty);
    }

    @Override
    protected void initTitle() {
        sv.setmTextView("搜索");
    }

    @Override
    public void initData() {
        rcHistory.setLayoutManager(new LinearLayoutManager(this));
        historyStringListener = new HistoryStringListener() {
            @Override
            public void refreshPriorityUI(String username) {
                sv.setText(username);
            }
        };
        //        查询数据库
        hasHistory();
        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                sv.setCursorVisible(true);
                return false;
            }
        });
        sv.addTextChangedListener(new TextWatcher() {

            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                hasHistory();
                llFirstSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                temp = charSequence;
                if (!charSequence.toString().trim().equals("")) {
                    Log.i("=================>>>>", "不为空");
                    ivClearSearch.setVisibility(View.VISIBLE);
                    httpSearch(charSequence.toString().trim());
                    tvMsg.setVisibility(View.GONE);
                    rcHistory.setVisibility(View.GONE);
                    btnClear.setVisibility(View.GONE);
                } else {
                    Log.i("=================>>>>", "为空");
                    elv.setVisibility(View.GONE);
                    ivClearSearch.setVisibility(View.GONE);
                    hasHistory();
                }
                llFirstSearch.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (temp.length() > 0) {
                } else {
                    elv.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.iv_clear_search, R.id.function, R.id.search_back, R.id.btn_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear_search:
                tvMsg.setVisibility(View.GONE);
//                elv.setVisibility(View.GONE);
                sv.setText("");
                break;
            case R.id.function:
                sv.clearFocus();
                llFirstSearch.setVisibility(View.GONE);
                if (sv.getText().toString().trim().equals("")) {
                    rcHistory.setVisibility(View.GONE);
                    btnClear.setVisibility(View.GONE);
                    tvMsg.setVisibility(View.VISIBLE);
                    tvMsg.setText("请输入搜索条件!");
                    return;
                }
                SearchHistoryStringDao.getInstance().addHistory(new SearchHistoryBean(null, sv.getText().toString().trim(), GlobalApplication.getInstance().getPerson(SearchAddedFriendsAcitivty.this).getUserId()));
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                sv.setCursorVisible(false);
                String s = sv.getText().toString().trim();
                if (Utils.isNetworkAvailable(this)) {
                    httpSearch(s);
                } else {
                    toast(R.string.warm_net);
                }
                break;
            case R.id.search_back:
                finish();
                break;
            case R.id.btn_clear:
                SearchHistoryStringDao.getInstance().clear();
                rcHistory.setVisibility(View.GONE);
                btnClear.setVisibility(View.GONE);
                tvMsg.setVisibility(View.VISIBLE);
                tvMsg.setText("暂无历史搜索记录");
                break;
        }
    }

    private void httpSearch(String s) {
        HashMap<String, String> map = new HashMap<>();
        map.put("UserName", s);
        map.put("PageSize", "10");
        map.put("PageNo", "1");
        new GetNetUtil(map, Api.TX_App_SNS_SearchFriend, this) {
            @Override
            public void successHandle(String base) {
                if (isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                BaseListBean<FriendsShopBean3> basebean = gson.fromJson(base, new TypeToken<BaseListBean<FriendsShopBean3>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    List<FriendsShopBean3> list = basebean.getObj();
                    if (list.size() == 0) {
                        tvMsg.setText("该好友不存在");
                        elv.setVisibility(View.GONE);
                        llFirstSearch.setVisibility(View.GONE);
                    } else {
                        elv.setVisibility(View.VISIBLE);
                        tvMsg.setVisibility(View.GONE);
                        llFirstSearch.setVisibility(View.GONE);
                        FriendsAddExpandableListview friendsExpandableListview = new FriendsAddExpandableListview(SearchAddedFriendsAcitivty.this, list);
                        elv.setAdapter(friendsExpandableListview);
                        for (int i = 0; i < list.size(); i++) {
                            elv.expandGroup(i);
                        }
                    }
                } else {
                    if (basebean.getMsg() != null) {
                        tvMsg.setText(basebean.getMsg());
                        tvMsg.setVisibility(View.VISIBLE);
                    }
                    elv.setVisibility(View.GONE);
                    llFirstSearch.setVisibility(View.GONE);
                }
            }
        };
    }

    private void hasHistory() {
        history = SearchHistoryStringDao.getInstance().getHistory();
        if (history != null && history.size() > 0) {
            if (!history.get(0).getLocatinUsername().equals(GlobalApplication.getInstance().getPerson(this).getUserId())) {
                SearchHistoryStringDao.getInstance().clear();
                history.clear();
            }
            if (history != null && history.size() > 5) {
                SearchHistoryStringDao.getInstance().delete(history.get(history.size() - 1));
            }
            history.clear();
            history = SearchHistoryStringDao.getInstance().getHistory();
            rcHistory.setVisibility(View.VISIBLE);
            tvMsg.setVisibility(View.GONE);
            SearchHistoryListAdapter historyListAdapter = new SearchHistoryListAdapter(this, history, historyStringListener);
            rcHistory.setAdapter(historyListAdapter);
            btnClear.setVisibility(View.VISIBLE);
            llFirstSearch.setVisibility(View.GONE);
        } else {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText("暂无历史搜索记录");
            llFirstSearch.setVisibility(View.VISIBLE);
        }
    }
}












