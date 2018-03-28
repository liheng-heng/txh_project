package com.txh.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.SearchGoodsAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.SearchGoodsBean;
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
 * 2017.4.1--liheng
 * 商品搜索
 */

public class SearchGoodsActivity extends BasicActivity {
    @Bind(R.id.search_back)
    LinearLayout searchBack;
    @Bind(R.id.sv)
    SearchView sv;
    @Bind(R.id.iv_clear_search)
    ImageView ivClearSearch;
    @Bind(R.id.function)
    TextView function;
    @Bind(R.id.ll_nodata)
    LinearLayout llNodata;
    @Bind(R.id.myrecycleview)
    RecyclerView my_merchant_rv;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.fl_goods)
    FrameLayout flGoods;
    @Bind(R.id.rl_class_brand_layout)
    RelativeLayout llLayout;

    private SearchGoodsAdapter adapter;
    private List<SearchGoodsBean.ItemListBean> list = new ArrayList<>();
    private String searchData = "";
    private String shopId;
    private int page = 1;
    public boolean updateRightLists = false;

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.activity_search_goods);
        shopId = getIntent().getStringExtra("shopId");
    }

    @Override
    protected void initTitle() {
        sv.setmTextView("商品名称");
        my_merchant_rv.setLayoutManager(new LinearLayoutManager(SearchGoodsActivity.this, LinearLayoutManager.VERTICAL, false));
        my_merchant_rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new SearchGoodsAdapter(SearchGoodsActivity.this, list, 1, shopId, tvGoodsNum);
        my_merchant_rv.setAdapter(adapter);
        my_merchant_rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    //获取第一个完全显示的条目
                    int firstVisibleItem = manager.findFirstCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部，并且是向下滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        page++;
                        HttpData(page, 2);
                    }

                    if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (recyclerView.isComputingLayout() == false)) {
                        updateRightLists = true;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dy > 0) {
                    //大于0表示，正在向下滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0 表示停止或向上滚动
                    isSlidingToLast = false;
                }
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
            }
        });
    }

    @Override
    public void initData() {
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

        sv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                sv.setCursorVisible(true);
                return false;
            }
        });
    }

    /**
     * 获取搜索内容并展示
     *
     * @param page
     * @param type
     */
    private void HttpData(int page, final int type) {

        HashMap<String, String> hashmap = new HashMap<>();
        searchData = sv.getText().toString().trim().replace(" ", "");
        hashmap.put("ShopId", shopId);
        hashmap.put("PageSize", "0");
        hashmap.put("PageIndex", page + "");
        hashmap.put("KeyWord", searchData);
        hashmap.put("ShopItemCount", "1");
        hashmap.put("IsReturnCartNum", "1");
        hashmap.put("IsReplaceOrder", "0");
        hashmap.put("ActionVersion", AllConstant.ActionVersion);
        new GetNetUtil(hashmap, Api.Mall_SearchItemPageList, SearchGoodsActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<SearchGoodsBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<SearchGoodsBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {
                    List<SearchGoodsBean.ItemListBean> itemList = bean.getObj().getItemList();
                    switch (type) {
                        case 1:
                            if (itemList.size() == 0) {
                                llNodata.setVisibility(View.VISIBLE);
                                my_merchant_rv.setVisibility(View.GONE);
                            } else {
                                llNodata.setVisibility(View.GONE);
                                my_merchant_rv.setVisibility(View.VISIBLE);
                                adapter.RereshData(itemList);
                            }
                            break;

                        case 2:
                            if (itemList.size() == 0) {
                            } else {
                                adapter.loadData(itemList);
                            }
                            break;
                    }

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

                } else {
                    ToastUtils.showToast(SearchGoodsActivity.this, bean.getMsg());
                }
            }
        };
    }

    @OnClick({R.id.search_back, R.id.iv_clear_search, R.id.function, R.id.fl_goods})
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
                tvGoodsNum.setVisibility(View.INVISIBLE);
                break;

            case R.id.function:
                sv.clearFocus();
                searchData = sv.getText().toString().trim();
                if (TextUtil.isEmpty(searchData)) {
                    ToastUtils.showToast(SearchGoodsActivity.this, R.string.no_search_str);
                    return;
                }
                HttpData(1, 1);
                sv.setCursorVisible(false);
                hideKeyBoard();
                break;

            case R.id.fl_goods:
                startActivity(new Intent(SearchGoodsActivity.this, ShopCarAcitivity.class));
                break;

        }
    }

    public boolean isUpdateRightLists() {
        return updateRightLists;
    }

    public void setUpdateRightLists(boolean updateRightLists) {
        this.updateRightLists = updateRightLists;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}


