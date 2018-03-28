package com.txh.im.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.QuoteAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.QuoteBean;
import com.txh.im.bean.UnitBean;
import com.txh.im.utils.GetNetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 报价单管理
 */
public class QuoteAcitivty extends BasicActivity {

    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.rc)
    RecyclerView rc;
    @Bind(R.id.pto_refresh)
    PullToRefreshLayout ptoRefresh;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.ll_no_goods)
    LinearLayout llNoGoods;
    private int page;
    private List<UnitBean> unitListOut = new ArrayList<>();
    private QuoteAdapter quoteAdapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_quote_acitivty);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("报价单管理");
        tvHeadRight.setText("价格分组");
        llHeadRight.setVisibility(View.VISIBLE);
        llNoGoods.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
//        page = 1;
//        if (unitListOut != null && unitListOut.size() > 0) {
//            unitListOut.clear();
//        }
//        getNetRequest(0);
        rc.setLayoutManager(new LinearLayoutManager(this));
        ptoRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                if (unitListOut != null && unitListOut.size() > 0) {
                    unitListOut.clear();
                }
                page = 1;
                getNetRequest(1);//1表示下拉刷新
//                // 结束刷新
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 结束加载更多
//                        ptoRefresh.finishRefresh();
//                    }
//                }, 1);
            }

            @Override
            public void loadMore() {
                if (unitListOut.size() % 10 != 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptoRefresh.finishLoadMore();
                        }
                    }, 200);//如果返回的个数小于10,给个效果就好了
                } else {
                    page++;
                    getNetRequest(2);//2表示加载更多
                }
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 结束加载更多
//                        ptoRefresh.finishLoadMore();
//                    }
//                }, 1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        if (unitListOut != null && unitListOut.size() > 0) {
            unitListOut.clear();
        }
        getNetRequest(0);
    }

    @OnClick({R.id.ll_head_back, R.id.ll_head_right})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;

            case R.id.ll_head_right:
                intent = new Intent(this, PriceGroupingAcitivty.class);
                startActivity(intent);
                break;
        }
    }

    public void getNetRequest(final int i) {
        HashMap<String, String> map = new HashMap<>();
        map.put("PageIndex", page + "");
        new GetNetUtil(map, Api.PriceGroup_GetUPriceUnitPageList, this) {
            @Override
            public void successHandle(String base) {
                if (QuoteAcitivty.this.isFinishing()) {
                    return;
                }
                if (i == 1) {
                    ptoRefresh.finishRefresh();
                }
                if (i == 2) {
                    ptoRefresh.finishLoadMore();
                }
                Gson gson = new Gson();
                Basebean<QuoteBean> basebean = gson.fromJson(base, new TypeToken<Basebean<QuoteBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    QuoteBean obj = basebean.getObj();
                    if (obj != null) {
                        if (obj.getPageTitle() != null) {
                            headTitle.setText(obj.getPageTitle());
                        }

                        List<UnitBean> unitList = obj.getUnitList();
                        if (unitList != null && unitList.size() > 0) {
                            llNoGoods.setVisibility(View.GONE);
                            if (unitListOut.size() == 0) {
                                unitListOut.addAll(unitList);
                                quoteAdapter = new QuoteAdapter(QuoteAcitivty.this, unitListOut);
                                rc.setAdapter(quoteAdapter);
                            } else {
                                unitListOut.addAll(unitList);
                                quoteAdapter.refresh();
                            }

                        } else {
                            if (page == 1) {
                                llNoGoods.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    toast(basebean.getMsg());
                }
            }
        };
    }
}
