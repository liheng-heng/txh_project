package com.txh.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.ShopSelectAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.ShopSelectBean;
import com.txh.im.bean.UnitBean;
import com.txh.im.listener.ShopCarMoneyOrNum;
import com.txh.im.segmentcontrol.SegmentControl;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.SearchView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ShopSelectActivity extends BasicActivity implements ShopCarMoneyOrNum {
    @Bind(R.id.segment_control)
    SegmentControl segmentControl;
    @Bind(R.id.sv)
    SearchView sv;
    @Bind(R.id.fl)
    FrameLayout fl;
    @Bind(R.id.rl_sv)
    RelativeLayout rlSv;
    @Bind(R.id.rc)
    RecyclerView rc;
    @Bind(R.id.iv_clear_search)
    ImageView ivClearSearch;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.tv_msg)
    TextView tvMsg;
    @Bind(R.id.srl)
    PullToRefreshLayout srl;
    private String groupId;
    private List<UnitBean> unitListOut = new ArrayList<>();
    private ShopSelectAdapter shopSelect;
    private String isAdd;
    private int num = 0;
    private int initSelect;//0表示未选中,1表示选中,初始化选中状态
    private int page;
    private String keyword;
    private String skuId;
    private String price;
    public static final int MIN_CLICK_DELAY_TIME = 2000;
    private long lastClickTime = 0;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_shop_select);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        skuId = intent.getStringExtra("SkuId");
        price = intent.getStringExtra("Price");
        isAdd = "1";
        initSelect = 0;//初始化是未添加，选中状态为0
        page = 1;
        keyword = null;//关键字初始值控制为Null
        tvMsg.setVisibility(View.GONE);
        listenerSearch();
        litennersegmentControl();
        if (skuId == null) {
            getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForGroup);
        } else {
            getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForPrice);
        }
        rc.setLayoutManager(new LinearLayoutManager(this));
        srl.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.finishRefresh();
                    }
                }, 200);//给个效果就好了
            }

            @Override
            public void loadMore() {
                if (unitListOut.size() % 10 != 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            srl.finishLoadMore();
                        }
                    }, 200);//如果返回的个数小于10,给个效果就好了
                } else {
                    page++;
                    if (skuId == null) {
                        getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForGroup);
                    } else {
                        getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForPrice);
                    }
                }
            }
        });
    }

    private void litennersegmentControl() {
        segmentControl.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                fl.setVisibility(View.GONE);
                rlSv.setVisibility(View.VISIBLE);
                hideKeyBoard();
                switch (index) {
                    case 0://点击未添加
                        isAdd = "1";
                        initSelect = 0;
                        keyword = null;
                        page = 1;
                        tvMsg.setVisibility(View.GONE);
                        if (unitListOut != null && unitListOut.size() > 0) {
                            unitListOut.clear();
                        }
                        rc.setVisibility(View.GONE);
                        sv.setText("");
                        if (skuId == null) {
                            getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForGroup);
                        } else {
                            getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForPrice);
                        }
                        break;
                    case 1://点击已添加
                        isAdd = "2";
                        initSelect = 1;
                        keyword = null;
                        page = 1;
                        if (unitListOut != null && unitListOut.size() > 0) {
                            unitListOut.clear();
                        }
                        rc.setVisibility(View.GONE);
                        sv.setText("");
                        tvMsg.setVisibility(View.GONE);
                        if (skuId == null) {
                            getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForGroup);
                        } else {
                            getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForPrice);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void listenerSearch() {
        sv.addTextChangedListener(new TextWatcher() {
            String ss = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ivClearSearch.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String trim = charSequence.toString().trim();
                if (trim != null && !trim.equals("")) {
                    ss = charSequence.toString().trim();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!ss.equals("")) {
                    ivClearSearch.setVisibility(View.VISIBLE);
                } else {
                    ivClearSearch.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.ll_head_back, R.id.iv_clear_search, R.id.function, R.id.rl_sv, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;
            case R.id.iv_clear_search:
                sv.setText("");
                ivClearSearch.setVisibility(View.GONE);
                break;
            case R.id.function:
                String trim = sv.getText().toString().trim();
                if (trim.equals("")) {
                    ToastUtils.toast(this, "请输入搜索条件!");
                    return;
                }
                keyword = trim;
                page = 1;//控制页数
                if (unitListOut != null && unitListOut.size() > 0) {
                    unitListOut.clear();
                }
                if (skuId == null) {
                    getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForGroup);
                } else {
                    getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForPrice);
                }
                break;
            case R.id.rl_sv:
                rlSv.setVisibility(View.GONE);
                fl.setVisibility(View.VISIBLE);
                sv.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) sv.getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                rc.setVisibility(View.GONE);
                break;
            case R.id.btn_confirm:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (isAdd.equals("1")) {
                        if (num == 0) {
                            return;
                        } else {
                            if (skuId == null) {
                                deleteOrAdd("1", Api.PriceGroup_AddOrDelUnitOfGroup);
                            } else {
                                deleteOrAdd("1", Api.PriceGroup_AddOrDelUnitOfPrice);
                            }
                        }
                    } else {
                        if (num == unitListOut.size()) {
                            return;
                        } else {
                            if (skuId == null) {
                                deleteOrAdd("2", Api.PriceGroup_AddOrDelUnitOfGroup);
                            } else {
                                deleteOrAdd("2", Api.PriceGroup_AddOrDelUnitOfPrice);
                            }
                        }
                    }
                } else {
                    return;
                }
                break;
        }
    }

    public void getNetRequest(String keyWord, final String isAdd, String url) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("PageIndex", page + "");
        if (skuId == null) {
            hashMap.put("GroupId", groupId);
        } else {
            hashMap.put("SkuId", skuId);
        }
        if (keyWord != null) {
            hashMap.put("KeyWord", keyWord);
        }
        hashMap.put("IsAdd", isAdd);
        new GetNetUtil(hashMap, url, this) {
            @Override
            public void successHandle(String base) {
                if (ShopSelectActivity.this.isFinishing()) {
                    return;
                }
                if (page > 1) {
                    srl.finishLoadMore();
                }
                hideKeyBoard();
                Gson gson = new Gson();
                Basebean<ShopSelectBean> basebean = gson.fromJson(base, new TypeToken<Basebean<ShopSelectBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    ShopSelectBean obj = basebean.getObj();
                    if (obj != null) {
                        List<UnitBean> unitList = obj.getUnitList();
                        if (unitList != null && unitList.size() > 0) {
                            tvMsg.setVisibility(View.GONE);
                            for (int i = 0; i < unitList.size(); i++) {
                                unitList.get(i).setIsSelected(initSelect);//为其设置初始化值
                            }
                            rc.setVisibility(View.VISIBLE);
                            btnConfirm.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray_999));
                            if (unitListOut != null && unitListOut.size() > 0) {
                                unitListOut.addAll(unitList);
                                shopSelect.refresh(isAdd);
//                                shopSelect.notifyDataSetChanged();
                            } else {
                                unitListOut.addAll(unitList);
                                shopSelect = new ShopSelectAdapter(ShopSelectActivity.this, unitListOut, isAdd, ShopSelectActivity.this);
                                rc.setAdapter(shopSelect);
                            }
                        } else {
                            rc.setVisibility(View.GONE);
                            if (page == 1) {
                                tvMsg.setVisibility(View.VISIBLE);
                            }
//                            toast(basebean.getMsg());
                        }
                    }
                } else {
                    toast(basebean.getMsg());
                }
            }
        };
    }

    //添加或者删除分组
    public void deleteOrAdd(String opType, String url) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < unitListOut.size(); i++) {
            UnitBean unitBean = unitListOut.get(i);
            if (isAdd.equals("1") && unitBean.getIsSelected() == 1) {
                sb.append(unitBean.getUnitId()).append(",");
            }
            if (isAdd.equals("2") && unitBean.getIsSelected() == 0) {
                sb.append(unitBean.getUnitId()).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("BUnitIds", sb.toString());
        if (skuId == null) {
            hashMap.put("GroupId", groupId);
        } else {
            hashMap.put("SkuId", skuId);
            hashMap.put("Price", price);
        }
        hashMap.put("OpType", opType);//1向分组中添加，2从分组中删除
        new GetNetUtil(hashMap, url, this) {
            @Override
            public void successHandle(String base) {
                if (ShopSelectActivity.this.isFinishing()) {
                    return;
                }
                if (unitListOut != null && unitListOut.size() > 0) {
                    unitListOut.clear();
                }
                Gson gson = new Gson();
                Basebean<UnitBean> basebean = gson.fromJson(base, new TypeToken<Basebean<ShopSelectBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    keyword = null;
                    toast(basebean.getMsg());
                    if (skuId == null) {
                        getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForGroup);
                    } else {
                        getNetRequest(keyword, isAdd, Api.PriceGroup_GetUnitPLForPrice);
                    }
                } else {
                    toast(basebean.getMsg());
                }
            }
        };
    }

    @Override
    public void getAllMoneyAndNum() {
        getNumSelect();
        if (isAdd.equals("1")) {
            if (num == 0) {
                btnConfirm.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray_999));
            } else {
                btnConfirm.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
            }
        } else {
            if (num == unitListOut.size()) {
                btnConfirm.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray_999));
            } else {
                btnConfirm.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
            }
        }
    }

    //获取被点击的数量
    public void getNumSelect() {
        num = 0;
        for (int i = 0; i < unitListOut.size(); i++) {
            UnitBean unitBean = unitListOut.get(i);
            if (unitBean.getIsSelected() == 1) {
                ++num;
            }
        }
    }
}