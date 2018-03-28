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
import com.txh.im.adapter.GoodsReplaceOrderRightAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.SearchGoodsBean;
import com.txh.im.bean.ShopClassifyRightBean;
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
 * Created by liheng on 2017/6/5.
 * 搜索活动商品
 */

public class SearchActiveGoodsActivity extends BasicActivity {
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

    private String userId;
    private GoodsReplaceOrderRightAdapter right_adapter;
    private List<SearchGoodsBean.ItemListBean> right_list = new ArrayList<>();
    private String searchData = "";
    private int page = 1;
    public boolean updateRightLists = false;

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.activity_search_active_goods);
        userId = getIntent().getStringExtra("userId");
    }

    @Override
    protected void initTitle() {
        sv.setmTextView("商品名称");
        my_merchant_rv.setLayoutManager(new LinearLayoutManager(SearchActiveGoodsActivity.this, LinearLayoutManager.VERTICAL, false));
        my_merchant_rv.setItemAnimator(new DefaultItemAnimator());
        right_adapter = new GoodsReplaceOrderRightAdapter(SearchActiveGoodsActivity.this, right_list, userId);
        my_merchant_rv.setAdapter(right_adapter);

        //底部滑动监听
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


    @OnClick({R.id.search_back, R.id.iv_clear_search, R.id.function, R.id.fl_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;

            case R.id.iv_clear_search:
                sv.setText("");
                if (right_adapter != null) {
                    right_adapter.Clear();
                }
                tvGoodsNum.setVisibility(View.INVISIBLE);
                break;

            case R.id.function:
                sv.clearFocus();
                searchData = sv.getText().toString().trim();
                if (TextUtil.isEmpty(searchData)) {
                    ToastUtils.showToast(SearchActiveGoodsActivity.this, R.string.no_search_str);
                    return;
                }
                HttpData(1, 1);
                sv.setCursorVisible(false);
                hideKeyBoard();
                break;

            case R.id.fl_goods:
                startActivity(new Intent(SearchActiveGoodsActivity.this, ShopCarAcitivity.class));
                break;

        }
    }


    /**
     * 获取右侧数据
     *
     * @param page
     * @param type
     */
    public void HttpData(int page, final int type) {

//        BUserId	        false	int	买家UserId(代下单的时候需要传入买家的UserId)
//        ShopId	        true	int	商户ID（代下单的时候不需要传）
//        PageSize	        false	int	每页数量（如果为0，则有服务器来控制页大小）
//        PageIndex	        true	int	页码（默认请传1）
//        CategoryId	    false	int	商品一级分类ID
//        SubCategoryId	    false	int	商品二级分类ID
//        BrandId	        false	int	品牌ID
//        KeyWord	        false	string	搜索关键字（如店铺名称）
//        IsReturnCartNum	false	int	是否返回购物车中商品数量（0不返回，1返回）
//        IsReturnShelfNum	false	int	是否返回货架中商品数量（0不返回，1返回）
//        IsReplaceOrder	false	int	是否代下单列表（1表示代下单，其它为正常商品列表）
//        ActionVersion	    false	string	不传默认1.0，当前接口版本号（2.0）
//        IsActionTest	    false	string	是否测试：1为静态数据，不传或其他为动态数据

        //refreshtype  0 第一次或者点击条目  1 下拉刷新  2  上滑加载
        //type 1:第一次或点击条目加载  2：上滑加载  3：下拉刷新
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("BUserId", userId);
        hashMap.put("PageSize", "0");
        hashMap.put("PageIndex", page + "");
        hashMap.put("KeyWord", searchData);
        hashMap.put("IsReplaceOrder", "1");
        hashMap.put("ActionVersion", "2.0");
        hashMap.put("IsActionTest", AllConstant.IsActionTest);
        showProgress(getResources().getString(R.string.http_loading_data));
        new GetNetUtil(hashMap, Api.Mall_SearchItemPageList, SearchActiveGoodsActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                hideProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy) {
                    return;
                }
                hideProgress();
                Gson gson = new Gson();
                Basebean<ShopClassifyRightBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopClassifyRightBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    List<SearchGoodsBean.ItemListBean> list = bean.getObj().getItemList();
                    switch (type) {
                        case 1:
                            right_adapter.RereshData(list, "0");
                            if (llNodata != null) {
                                if (list.size() == 0) {
                                    llNodata.setVisibility(View.VISIBLE);
                                } else {
                                    llNodata.setVisibility(View.GONE);
                                }
                            }
                            break;
                        case 2:
                            right_adapter.loadData(list, "0");
                            break;

                        case 3:
                            right_adapter.RereshData(list, "0");
                            break;
                    }
                } else {
                    ToastUtils.showToast(SearchActiveGoodsActivity.this, bean.getMsg());
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
