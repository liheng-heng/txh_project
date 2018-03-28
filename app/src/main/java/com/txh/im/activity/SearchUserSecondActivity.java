package com.txh.im.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.FriendsExpandableListview;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.FriendsShopBean2;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.Utils;
import com.txh.im.widget.SearchView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SearchUserSecondActivity extends BasicActivity {

    @Bind(R.id.sv)
    SearchView sv;
    @Bind(R.id.iv_clear_search)
    ImageView ivClearSearch;
    @Bind(R.id.tv_inexistence)
    TextView tvInexistence;
    @Bind(R.id.elv)
    ExpandableListView elv;
    private Gson gson;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_user_second);
    }

    @Override
    protected void initTitle() {
    }

    @Override
    public void initData() {
        gson = new Gson();
        sv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ivClearSearch.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().equals("")) {
                    ivClearSearch.setVisibility(View.VISIBLE);
                } else {
                    ivClearSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                sv.setCursorVisible(true);
                return false;
            }
        });
        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    @OnClick({R.id.search_back, R.id.iv_clear_search, R.id.function})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;

            case R.id.iv_clear_search:
                sv.setText("");
                elv.setVisibility(View.GONE);
                break;

            case R.id.function:
                sv.clearFocus();
                if (sv.getText().toString().trim().equals("")) {
                    tvInexistence.setVisibility(View.VISIBLE);
                    tvInexistence.setText("请输入搜索条件!");
//                    toast("请输入正确的手机号和天下货号");
                    return;
                } else {
                    tvInexistence.setVisibility(View.GONE);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                sv.setCursorVisible(false);
                if (Utils.isNetworkAvailable(this)) {
                    httpSearch();
                } else {
                    toast(R.string.warm_net);
                }
                break;
        }
    }

    private void httpSearch() {
        String s = sv.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("UserNo", s);
        new GetNetUtil(map, Api.TX_App_SNS_Search, this) {
            @Override
            public void successHandle(String base) {
                if (SearchUserSecondActivity.this.isFinishing()) {
                    return;
                }
                BaseListBean<FriendsShopBean2> basebean = gson.fromJson(base, new TypeToken<BaseListBean<FriendsShopBean2>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    List<FriendsShopBean2> list = basebean.getObj();
                    if (list == null || list.size() == 0) {
                        tvInexistence.setVisibility(View.VISIBLE);
                        elv.setVisibility(View.GONE);
                    } else {
                        tvInexistence.setVisibility(View.GONE);
                        elv.setVisibility(View.VISIBLE);
                        FriendsExpandableListview friendsExpandableListview = new FriendsExpandableListview(SearchUserSecondActivity.this, list);
                        elv.setAdapter(friendsExpandableListview);
                        for (int i = 0; i < list.size(); i++) {
                            elv.expandGroup(i);
                        }
                    }
                } else {
                    tvInexistence.setVisibility(View.VISIBLE);
                    elv.setVisibility(View.GONE);
                    tvInexistence.setText(basebean.getMsg());
                }
            }
        };
    }
}
