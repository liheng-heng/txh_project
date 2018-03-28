package com.txh.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.MyOrderContentAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.MyOrderContentBean;
import com.txh.im.bean.MyOrderTitleBean;
import com.txh.im.itemclick.MyAdapter;
import com.txh.im.itemclick.MyItemClickListener;
import com.txh.im.itemclick.MyItemLongClickListener;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的订单
 */
public class MyOrderActivity extends BasicActivity implements MyItemClickListener, MyItemLongClickListener {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.iv_headright)
    ImageView ivHeadright;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.rl_no_order)
    RelativeLayout rlNoOrder;
    @Bind(R.id.iv_headright2)
    ImageView ivHeadright2;
    @Bind(R.id.ll_head_right2)
    LinearLayout llHeadRight2;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.srl)
    PullToRefreshLayout srl;

    private int title_item_size = 0;
    private RecyclerView title_recycleview;
    private MyAdapter mAdapter;
    List<MyOrderTitleBean.TypeListBean> titleList = new ArrayList<>();
    List<MyOrderTitleBean.RefuseRmearkListBean> refuseRmearkList = new ArrayList<>();

    private RecyclerView content_recycleview;
    private MyOrderContentAdapter adapter_content;
    private List<MyOrderContentBean> list_content = new ArrayList<>();
    private int page = 1;
    private String typecode_send = "0";
    private String ShopId;
    private String intent_where;
    private boolean isDataUpdated = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_order);
        title_recycleview = (RecyclerView) findViewById(R.id.title_recycleview);
        content_recycleview = (RecyclerView) findViewById(R.id.content_recycleview);
        content_recycleview.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this, LinearLayoutManager.VERTICAL, false));
        content_recycleview.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initTitle() {
        ShopId = getIntent().getStringExtra("ShopId");
        intent_where = getIntent().getStringExtra("intent_where");
        ivHeadright.setVisibility(View.VISIBLE);
        ivHeadright.setBackgroundDrawable(getResources().getDrawable(R.drawable.search));
        HttpTitleData();

        srl.setRefreshListener(new BaseRefreshListener() {

            @Override
            public void refresh() {
                // 结束刷新
                HttpContentData(1, 1, 1);
                page = 1;
            }

            @Override
            public void loadMore() {
                //加载更多功能的代码
                page++;
                HttpContentData(page, 2, 2);
            }
        });

    }

    @Override
    public void initData() {
        mAdapter = new MyAdapter(MyOrderActivity.this, titleList);
        title_recycleview.setAdapter(mAdapter);
        adapter_content = new MyOrderContentAdapter(MyOrderActivity.this, list_content, rlNoOrder, true, "0");
        content_recycleview.setAdapter(adapter_content);
        this.mAdapter.setOnItemClickListener(this);
        this.mAdapter.setOnItemLongClickListener(this);
        HttpContentData(1, 1, 1);
        page = 1;
    }

    /**
     * 获取订单头状态--  全部 待发货 已完成·····
     */
    private void HttpTitleData() {
        HashMap<String, String> hashMap = new HashMap<>();
        //ShopId	false	int	商户ID（卖家身份时必传，买家不用传）
        String unitType = GlobalApplication.getInstance().getPerson(MyOrderActivity.this).getUnitType();
        if (unitType.equals("2")) {
            hashMap.put("ShopId", ShopId);
        }

        new GetNetUtil(hashMap, Api.Mall_GetOrderListTypes, MyOrderActivity.this) {

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
                Basebean<MyOrderTitleBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<MyOrderTitleBean>>() {
                }.getType());

                if (bean.getStatus().endsWith("200")) {
                    List<MyOrderTitleBean.TypeListBean> typeList = bean.getObj().getTypeList();
                    if (!TextUtil.isEmpty(bean.getObj().getMyOrderTitle())) {
                        headTitle.setText(bean.getObj().getMyOrderTitle());
                    }
                    refuseRmearkList = bean.getObj().getRefuseRmearkList();
                    if (null != typeList) {
                        title_item_size = typeList.size();
                        title_recycleview.setLayoutManager(new GridLayoutManager(MyOrderActivity.this, title_item_size));
                        title_recycleview.setItemAnimator(new DefaultItemAnimator());
                        if (typeList.size() >= 0) {
                            typeList.get(0).setIsseleted(true);
                        }
                        titleList = typeList;
                        mAdapter.RereshData(typeList);
                    }
                } else {
                    ToastUtils.showToast(MyOrderActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * Item click
     */
    @Override
    public void onItemClick(View view, int postion) {
        if (isDataUpdated) {
            for (int m = 0; m < titleList.size(); m++) {
                titleList.get(m).setIsseleted(false);
            }
            titleList.get(postion).setIsseleted(true);
            mAdapter.notifyDataSetChanged();
            typecode_send = titleList.get(postion).getTypeCode();
            HttpContentData(1, 1, 1);
            page = 1;
        }
    }

    /**
     * 获取订单内容
     *
     * @param page
     * @param type
     */
    private void HttpContentData(int page, final int type, final int refreshtype) {
        isDataUpdated = false;
        if (MyOrderActivity.this.isFinishing()) {
            return;
        }
        if (list_content != null) {
            list_content.clear();
        }
        HashMap<String, String> hashMap = new HashMap<>();
        //根据unitType进行(买家或者卖家) shopid 传值
        String unitType = GlobalApplication.getInstance().getPerson(MyOrderActivity.this).getUnitType();
        if (unitType.equals("2")) {
            hashMap.put("ShopId", ShopId);
        }
        hashMap.put("OrderListType", typecode_send);
        hashMap.put("KeyWord", "");
        hashMap.put("PageSize", "0");
        hashMap.put("PageIndex", page + "");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        hashMap.put("IsActionTest", AllConstant.IsActionTest);


        new GetNetUtil(hashMap, Api.Mall_GetOrderPageList, MyOrderActivity.this) {
            @Override
            public void errorHandle() {
                super.errorHandle();
                isDataUpdated = true;
                if (refreshtype == 1) {
                    srl.finishRefresh();
                }
                if (refreshtype == 2) {
                    srl.finishLoadMore();
                }
            }

            @Override
            public void successHandle(String basebean) {
                isDataUpdated = true;
                if (isDestroy) {
                    return;
                }
                if (refreshtype == 1) {
                    srl.finishRefresh();
                }
                if (refreshtype == 2) {
                    srl.finishLoadMore();
                }
                Log.i("------>", "订单内容-------" + basebean);

                if (basebean.contains("200")) {
                    Gson gson = new Gson();
                    BaseListBean<MyOrderContentBean> bean = gson.fromJson(basebean, new TypeToken<BaseListBean<MyOrderContentBean>>() {
                    }.getType());

                    if (bean.getStatus().equals("200")) {
                        List<MyOrderContentBean> list = bean.getObj();
                        if (null != list) {
                            switch (type) {
                                case 1:
                                    adapter_content.RereshData(list, typecode_send);
                                    if (list.size() == 0) {
                                        rlNoOrder.setVisibility(View.VISIBLE);
                                        srl.setVisibility(View.GONE);
                                    } else {
                                        rlNoOrder.setVisibility(View.GONE);
                                        srl.setVisibility(View.VISIBLE);
                                    }
                                    break;

                                case 2:
                                    adapter_content.loadData(list, typecode_send);
                                    break;
                            }
                        }
                    } else {
                        ToastUtils.showToast(MyOrderActivity.this, bean.getMsg());
                    }
                } else {
                    rlNoOrder.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                HttpContentData(1, 1, 1);
                page = 1;
            }
        }
    }

    @OnClick({R.id.ll_head_back, R.id.ll_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                if (!TextUtil.isEmpty(intent_where)) {
                    if (intent_where.equals("shoppingtrolley")) {
                        EventBus.getDefault().post(new FirstEventBus("GoodsPlace"));
                    } else {
                        startActivity(new Intent(MyOrderActivity.this, MainActivity.class));
                    }
                    finish();
                } else {
                    finish();
                }
                break;

            case R.id.ll_head_right:
                Intent intent = new Intent(MyOrderActivity.this, SearchOrderActivity.class);
                intent.putExtra("ShopId", ShopId);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!TextUtil.isEmpty(intent_where)) {
                if (intent_where.equals("shoppingtrolley")) {
                    EventBus.getDefault().post(new FirstEventBus("GoodsPlace"));
                } else {
                    startActivity(new Intent(MyOrderActivity.this, MainActivity.class));
                }
                finish();
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
