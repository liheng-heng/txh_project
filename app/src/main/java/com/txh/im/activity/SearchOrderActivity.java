package com.txh.im.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.MyOrderContentAdapter;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.MyOrderContentBean;
import com.txh.im.bean.MyOrderTitleBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 搜索订单---liheng
 */
public class SearchOrderActivity extends BasicActivity {

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
    @Bind(R.id.myrecycleview)
    RecyclerView content_recycleview;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.rl_no_order)
    RelativeLayout llNodata;

    private MyOrderContentAdapter adapter_content;
    private List<MyOrderContentBean> list_content = new ArrayList<>();
    private String searchData = "";
    private int page = 1;
    private String ShopId;
    List<MyOrderTitleBean.RefuseRmearkListBean> refuseRmearkList = new ArrayList<>();

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        ShopId = getIntent().getStringExtra("ShopId");
        setContentView(R.layout.activity_search_order);
    }

    @Override
    protected void initTitle() {
        sv.setmTextView("输入商品名称/订单编号");
        content_recycleview.setLayoutManager(new LinearLayoutManager(SearchOrderActivity.this, LinearLayoutManager.VERTICAL, false));
        content_recycleview.setItemAnimator(new DefaultItemAnimator());
        adapter_content = new MyOrderContentAdapter(SearchOrderActivity.this, list_content, llNodata, false, "0");
        content_recycleview.setAdapter(adapter_content);

        //底部滑动监听
        content_recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    int firstVisibleItem = manager.findFirstVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部，并且是向下滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        //上滑加载数据
                        page++;
                        HttpEditData(page, 2);
                    }

                    if (firstVisibleItem == 0 && !isSlidingToLast) {
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
        HttpTitleData();
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
    }

    private void HttpTitleData() {
        HashMap<String, String> hashMap = new HashMap<>();
        //ShopId	false	int	商户ID（卖家身份时必传，买家不用传）
        String unitType = GlobalApplication.getInstance().getPerson(SearchOrderActivity.this).getUnitType();
        if (unitType.equals("2")) {
            hashMap.put("ShopId", ShopId);
        }

        new GetNetUtil(hashMap, Api.Mall_GetOrderListTypes, SearchOrderActivity.this) {

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
                Basebean<MyOrderTitleBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<MyOrderTitleBean>>() {
                }.getType());

                if (bean.getStatus().endsWith("200")) {
                    refuseRmearkList = bean.getObj().getRefuseRmearkList();
                } else {
                    ToastUtils.showToast(SearchOrderActivity.this, bean.getMsg());
                }
            }
        };
    }

    private void HttpEditData(int page, final int type) {
        HashMap<String, String> hashMap = new HashMap<>();
        searchData = sv.getText().toString().trim().replace(" ", "");
        // 根据unitType进行 卖家（2）传值 shopid 传值
        String unitType = GlobalApplication.getInstance().getPerson(SearchOrderActivity.this).getUnitType();
        if (unitType.equals("2")) {
            hashMap.put("ShopId", ShopId);
        }
        hashMap.put("OrderListType", "0");
        hashMap.put("KeyWord", searchData);
        hashMap.put("PageSize", "0");
        hashMap.put("PageIndex", page + "");
        showProgress(getResources().getString(R.string.http_loading_data));
        new GetNetUtil(hashMap, Api.Mall_GetOrderPageList, SearchOrderActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                hideProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy){
                    return;
                }
                Log.i("------->", "商品详情---" + basebean);
                hideProgress();
                Gson gson = new Gson();
                BaseListBean<MyOrderContentBean> bean = gson.fromJson(basebean, new TypeToken<BaseListBean<MyOrderContentBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {
                    List<MyOrderContentBean> list = bean.getObj();
                    if (null != list) {
                        switch (type) {
                            case 1:
                                if (list.size() == 0) {
                                    llNodata.setVisibility(View.VISIBLE);
                                    content_recycleview.setVisibility(View.GONE);
                                } else {
                                    llNodata.setVisibility(View.GONE);
                                    content_recycleview.setVisibility(View.VISIBLE);
                                    adapter_content.RereshData(list, "0");
                                }
                                break;

                            case 2:
                                if (list.size() == 0) {
//                                    ToastUtils.showToast(SearchOrderActivity.this, "已加载完毕");
                                } else {
                                    adapter_content.loadData(list, "0");
                                }
                                break;
                        }
                    }
                } else {
                    hideProgress();
                    ToastUtils.showToast(SearchOrderActivity.this, bean.getMsg());
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            HttpEditData(1, 1);
        }
    }

    @OnClick({R.id.search_back, R.id.iv_clear_search, R.id.function})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;

            case R.id.iv_clear_search:
                sv.setText("");
                if (adapter_content != null) {
                    adapter_content.Clear();
                }
                break;

            case R.id.function:
                if (TextUtil.isEmpty(sv.getText().toString().trim())) {
                    ToastUtils.showToast(SearchOrderActivity.this, R.string.no_search_str);
                    return;
                }
                HttpEditData(1, 1);
                hideKeyBoard();
                break;
        }
    }
}
