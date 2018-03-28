package com.txh.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.MercantHistoryListAdapter;
import com.txh.im.adapter.ShoppingAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.CartShelfNumBean;
import com.txh.im.bean.GetHistoryMerchantBean;
import com.txh.im.bean.ShopTrolleyBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索配送商
 */
public class SearchMerchantActivity extends BasicActivity {
    @Bind(R.id.search_back)
    LinearLayout searchBack;
    @Bind(R.id.sv)
    SearchView sv;
    @Bind(R.id.iv_clear_search)
    ImageView ivClearSearch;
    @Bind(R.id.function)
    TextView function;
    @Bind(R.id.tv_history_msg)
    TextView tvHistoryMsg;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.view_line)
    View viewLine;
    @Bind(R.id.lv_history)
    ListView lvHistory;
    @Bind(R.id.listview_history_layout)
    LinearLayout listviewHistoryLayout;
    @Bind(R.id.btn_clear)
    Button btnClear;
    @Bind(R.id.ll_history)
    LinearLayout llHistory;
    @Bind(R.id.ll_nodata)
    LinearLayout llNodata;
    @Bind(R.id.myrecycleview)
    RecyclerView my_recyclerview;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.fl_goods)
    FrameLayout flGoods;

    private ShoppingAdapter adapter;
    private List<ShopTrolleyBean.ShopListBean> list;
    private String searchData = "";
    private int page = 1;
    List<GetHistoryMerchantBean.SearchKeyWordHistoryBean> historylist = new ArrayList<>();
    private MercantHistoryListAdapter historyAdapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_merchant);
    }

    @Override
    protected void initTitle() {
        sv.setmTextView(getResources().getString(R.string.merchant_search));
        my_recyclerview.setLayoutManager(new LinearLayoutManager(SearchMerchantActivity.this, LinearLayoutManager.VERTICAL, false));
        my_recyclerview.setItemAnimator(new DefaultItemAnimator());
        adapter = new ShoppingAdapter(SearchMerchantActivity.this, list, 1);
        my_recyclerview.setAdapter(adapter);

        //底部滑动监听
        my_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int firstVisibleItem = manager.findFirstVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        page++;
                        HttpData(page, 2);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
            }
        });
        historyAdapter = new MercantHistoryListAdapter(SearchMerchantActivity.this, historylist, R.layout.item_merchant_history);
        lvHistory.setAdapter(historyAdapter);

    }

    @Override
    public void initData() {
        sv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                sv.setCursorVisible(true);
                return false;
            }
        });

        sv.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().equals("")) {
                    ivClearSearch.setVisibility(View.VISIBLE);
                } else {
                    ivClearSearch.setVisibility(View.GONE);
                }
            }
        });

        /**
         * 请求历史记录数据
         */
        HttpHistoryData(true);
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < 0) {
                    return;
                }
                String keyWord = historylist.get(i).getKeyWord();
                sv.setText(keyWord);
                HttpData(1, 1);
                hideKeyBoard();
            }
        });
    }

    private void HttpHistoryData(final boolean addfootview) {

        llNodata.setVisibility(View.GONE);
        my_recyclerview.setVisibility(View.GONE);
        llHistory.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("SearchType", "1");
        map.put("NeedRecordCount", "0");

        new GetNetUtil(map, Api.Mall_GetUserSearchHistory, SearchMerchantActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy){
                    return;
                }
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                Basebean<GetHistoryMerchantBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<GetHistoryMerchantBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    List<GetHistoryMerchantBean.SearchKeyWordHistoryBean> searchKeyWordHistory = bean.getObj().getSearchKeyWordHistory();
                    if (searchKeyWordHistory.size() == 0) {
                        tvHistoryMsg.setVisibility(View.VISIBLE);
                        btnClear.setVisibility(View.GONE);
                        tvName.setVisibility(View.GONE);
                        viewLine.setVisibility(View.GONE);
                    } else {
                        tvHistoryMsg.setVisibility(View.GONE);
                        listviewHistoryLayout.setVisibility(View.VISIBLE);
                        btnClear.setVisibility(View.VISIBLE);
                        tvName.setVisibility(View.VISIBLE);
                        viewLine.setVisibility(View.VISIBLE);
                        historyAdapter.setRefrece(searchKeyWordHistory);
                    }
                } else {
                    ToastUtils.showToast(SearchMerchantActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 请求搜索数据
     *
     * @param page
     * @param type
     */
    private void HttpData(int page, final int type) {
        llHistory.setVisibility(View.GONE);
        searchData = sv.getText().toString().trim().replace(" ", "");
        if (TextUtil.isEmpty(searchData)) {
            ToastUtils.showToast(SearchMerchantActivity.this, R.string.no_search_str);
            return;
        }
        if (searchData.length() > 100) {
            searchData = searchData.substring(0, 101);
        }

        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("KeyWord", searchData);
        hashmap.put("PageIndex", page + "");
        hashmap.put("ShopItemCount", "0");
        hashmap.put("PageType", "1");
        hashmap.put("IsReturnCartNum", "0");
        hashmap.put("IsReturnShelfNum", "0");
        hashmap.put("ActionVersion", AllConstant.ActionVersion);
        hashmap.put("IsActionTest", AllConstant.IsActionTest);

        new GetNetUtil(hashmap, Api.Mall_SearchDataForMallFPage, SearchMerchantActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
//                hideProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy){
                    return;
                }
//                hideProgress();
                Log.i("----------->", "搜索数据----" + basebean);
                Gson gson = new Gson();
                Basebean<ShopTrolleyBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopTrolleyBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    List<ShopTrolleyBean.ShopListBean> shopList = bean.getObj().getShopList();
                    switch (type) {
                        case 1:
                            if (shopList.size() == 0) {
                                llNodata.setVisibility(View.VISIBLE);
                                my_recyclerview.setVisibility(View.GONE);
                            } else {
                                llNodata.setVisibility(View.GONE);
                                my_recyclerview.setVisibility(View.VISIBLE);
                                adapter.RereshData(shopList);
                            }
                            break;

                        case 2:
                            if (shopList.size() == 0) {
                            } else {
                                adapter.loadData(shopList);
                            }
                            break;
                    }
                } else {
                    tvGoodsNum.setVisibility(View.INVISIBLE);
                    hideProgress();
                    ToastUtils.showToast(SearchMerchantActivity.this, bean.getMsg());
                }
            }
        };
    }

    @OnClick({R.id.search_back, R.id.iv_clear_search, R.id.function, R.id.btn_clear, R.id.fl_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;

            case R.id.iv_clear_search:
                sv.setText("");
                if (adapter != null) {
                    adapter.Clear();
                }
                HttpHistoryData(false);
                break;

            case R.id.function:
                HttpData(1, 1);
                hideKeyBoard();
                break;

            case R.id.btn_clear:
                HttpClearHistoryData();
                break;

            case R.id.fl_goods:
                startActivity(new Intent(SearchMerchantActivity.this, ShopCarAcitivity.class));
                break;

        }
    }

    /**
     * 清楚历史记录
     */
    private void HttpClearHistoryData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("SearchType", "1");
        new GetNetUtil(map, Api.Mall_CleanSearchHistory, SearchMerchantActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy){
                    return;
                }
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                Basebean<GetHistoryMerchantBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<GetHistoryMerchantBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    tvHistoryMsg.setVisibility(View.VISIBLE);
                    listviewHistoryLayout.setVisibility(View.GONE);
                    btnClear.setVisibility(View.GONE);
                } else {
                    ToastUtils.showToast(SearchMerchantActivity.this, bean.getMsg());
                }
            }
        };
    }


    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        new GetNetUtil(hashMap, Api.Mall_GetLatestCartShelfNum, SearchMerchantActivity.this) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy){
                    return;
                }
                Gson gson = new Gson();
                Basebean<CartShelfNumBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<CartShelfNumBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    if (!TextUtil.isEmpty(bean.getObj().getCartItemNum())) {
                        tvGoodsNum.setVisibility(View.VISIBLE);
                        if (Integer.parseInt(bean.getObj().getCartItemNum()) > 99) {
                            tvGoodsNum.setText("99+");
                        } else {
                            if (Integer.parseInt(bean.getObj().getCartItemNum()) == 0) {
                                tvGoodsNum.setVisibility(View.INVISIBLE);
                            } else {
                                tvGoodsNum.setText(bean.getObj().getCartItemNum());
                            }
                        }
                    }
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
