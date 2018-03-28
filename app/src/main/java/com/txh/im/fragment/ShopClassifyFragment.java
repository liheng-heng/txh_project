package com.txh.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.squareup.picasso.Picasso;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.ActiveActivity;
import com.txh.im.activity.GoodsPlaceOrderAcitivty;
import com.txh.im.adapter.GoodsReplaceOrderRightAdapter;
import com.txh.im.adapter.ShopClassifyLeftAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.bean.ActiveNoticeBean;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.ClassAndBrandBean;
import com.txh.im.bean.SearchGoodsBean;
import com.txh.im.bean.ShopClassifyLeftBean;
import com.txh.im.bean.ShopClassifyRightBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.widget.FlowLayout2;
import com.txh.im.widget.UPMarqueeView;
import com.z_okhttp.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/6/1.
 * 商品代下单点击商品的fragment
 */

public class ShopClassifyFragment extends BasicFragment {

    @Bind(R.id.lv_leftlistview)
    ListView lvLeftlistview;
    @Bind(R.id.tv_right_nodata)
    TextView tvRightNodata;
    @Bind(R.id.rv_right)
    RecyclerView recyclerView_right;
    @Bind(R.id.ll_add)
    LinearLayout llAdd;
    GoodsPlaceOrderAcitivty acitivty;
    @Bind(R.id.tv_textview1)
    TextView tvTextview1;
    @Bind(R.id.iv_image1)
    ImageView ivImage1;
    @Bind(R.id.ll_layout1)
    LinearLayout llLayout1;
    @Bind(R.id.tv_textview2)
    TextView tvTextview2;
    @Bind(R.id.iv_image2)
    ImageView ivImage2;
    @Bind(R.id.ll_layout2)
    LinearLayout llLayout2;
    @Bind(R.id.rl_class_brand_layout)
    RelativeLayout rlClassBrandLayout;
    @Bind(R.id.srl)
    PullToRefreshLayout srl;
    //    @Bind(R.id.rl_recycle_layout)
//    RelativeLayout rlRecycleLayout;
    @Bind(R.id.upm_task_leader)
    UPMarqueeView upmTaskLeader;
    @Bind(R.id.tv_top_active_num)
    TextView tvTopActiveNum;//活动数量
    @Bind(R.id.rl_reward_top_line)
    RelativeLayout llRewardTopLine;//活动数量为0时 隐藏
    @Bind(R.id.ll_left_layout)
    LinearLayout llLeftLayout;
    @Bind(R.id.iv_top_item)
    ImageView ivTopItem;
    @Bind(R.id.tv_top_item)
    TextView tvTopItem;

    private ShopClassifyLeftAdapter left_adapter;
    private List<ShopClassifyLeftBean.ListBean> left_list = new ArrayList<>();
    private GoodsReplaceOrderRightAdapter right_adapter;
    private List<SearchGoodsBean.ItemListBean> right_list = new ArrayList<>();
    private int page = 1;
    private String userId;
    int statusBarHeight = 0;
    private String CategoryId = "0";
    private String SubCategoryId = "";
    private String BrandId = "";

    private List<ClassAndBrandBean.SubCategorylistBean> list_class = new ArrayList<>();
    private List<ClassAndBrandBean.BrandlistBean> list_brand = new ArrayList<>();
    private PopupWindow popupWindow;
    private FlowLayout2 flowlayout2;
    List<View> views = new ArrayList<>();
    List<ActiveNoticeBean> data = new ArrayList<>();
    private String basebean_active;
    private String iCon = "";
    private String title = "";
    int val_active_weight = UIUtils.dip2px(15);
    int val_active_high = UIUtils.dip2px(15);

    private boolean isShowTopView = false;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_shop_classify, null);
        return view;
    }

    @Override
    protected void initTitle() {
        acitivty = (GoodsPlaceOrderAcitivty) mContext;
        userId = acitivty.getUserId();
        statusBarHeight = acitivty.getStatusBarHeight();

        /**
         * 请求活动数据
         */
        getActiveData();
        /**
         * 获取品牌 和 分类数据
         */
        getClassAndBrandData();
        /**
         * 左边listview
         */
        left_adapter = new ShopClassifyLeftAdapter(mContext, left_list, R.layout.item_left_classfity);
        lvLeftlistview.setAdapter(left_adapter);
        /**
         * 右边recycleview
         */
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView_right.setLayoutManager(linearLayoutManager);
        recyclerView_right.setItemAnimator(new DefaultItemAnimator());
        right_adapter = new GoodsReplaceOrderRightAdapter(mContext, right_list, userId);
        recyclerView_right.setAdapter(right_adapter);
        srl.setRefreshListener(new BaseRefreshListener() {

            @Override
            public void refresh() {
                // 结束刷新
                HttpRightData(1, 1, 1, "", false);
                page = 1;
            }

            @Override
            public void loadMore() {
                //加载更多功能的代码
                page++;
                HttpRightData(page, 2, 2, "", false);
            }
        });

        String shopLeftList = (String) SPUtil.get(mContext, userId + "fragment", "");
        if (!TextUtil.isEmpty(shopLeftList)) {
            showLeftSaveData(shopLeftList);
        }

        HttpLeftData();
//        HttpRightData(1, 1, 0, "");
        page = 1;
    }

    private boolean isItemClick = true;

    @Override
    protected void initData() {
        lvLeftlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        list_brand.clear();
                        list_class.clear();

                        for (int m = 0; m < left_list.size(); m++) {
                            left_list.get(m).setSeleted(false);
                        }
                        left_list.get(i).setSeleted(true);
                        left_adapter.notifyDataSetChanged();
                        CategoryId = left_list.get(i).getCategory_id();
                        SubCategoryId = "0";
                        BrandId = "0";
                        if (CategoryId.equals("-1")) {
                            rlClassBrandLayout.setVisibility(View.GONE);
                        } else {
                            rlClassBrandLayout.setVisibility(View.VISIBLE);
                        }
                        if (left_list.get(i).getCategory_id().equals("0")) {
                            llLayout1.setVisibility(View.GONE);
                        } else {
                            llLayout1.setVisibility(View.VISIBLE);
                        }
                        if (isItemClick) {
                            HttpRightData(1, 1, 0, "", true);
                        }
                        page = 1;
//                        /**
//                         * 获取品牌品类数据
//                         */
//                        getClassAndBrandData();
                    }
                }, 200);
            }
        });
    }

    private void showLeftSaveData(String basebean) {
        if (ShopClassifyFragment.this.isDestroy) {
            return;
        }
        Gson gson = new Gson();
        Basebean<ShopClassifyLeftBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopClassifyLeftBean>>() {
        }.getType());
        if (bean.getStatus().equals("200")) {
            List<ShopClassifyLeftBean.ListBean> list = bean.getObj().getList();
            //设置第一个角标“全部”是红色字体
            if (list.size() >= 1) {
                list.get(0).setSeleted(true);
                CategoryId = list.get(0).getCategory_id();
                if (CategoryId.equals("-1")) {
                    rlClassBrandLayout.setVisibility(View.GONE);
                }
            }
            left_adapter.setRefrece(list);
        }
    }

    //请求促销头条的数据
    private void getActiveData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("BUserId", userId);
        hashMap.put("IsReplaceOrder", "1");
        hashMap.put("IsActionTest", AllConstant.IsActionTest);

        new GetNetUtil(hashMap, Api.Mall_GetActivityInfo, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (ShopClassifyFragment.this.isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                BaseListBean<ActiveNoticeBean> bean = gson.fromJson(basebean, new TypeToken<BaseListBean<ActiveNoticeBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {
                    data = bean.getObj();
                    basebean_active = basebean;
                    if (data != null && data.size() > 0) {
                        llRewardTopLine.setVisibility(View.VISIBLE);
                        tvTopActiveNum.setText(data.size() + "个活动");
                        iCon = data.get(0).getICon();
                        title = data.get(0).getTitle();
                        if (!TextUtil.isEmpty(iCon)) {
                            Picasso.with(mContext).load(iCon).resize(val_active_weight, val_active_high).into(ivTopItem);
                        }
                        tvTopItem.setText(title);
                        isShowTopView = true;
                    } else {
                        llRewardTopLine.setVisibility(View.GONE);
                        isShowTopView = false;
                    }

//                    setView();
//                    upmTaskLeader.setViews(views);

                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取品牌 品类数据
     */
    public void getClassAndBrandData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("BUserId", userId);
        hashMap.put("CategoryId", CategoryId);
        hashMap.put("IsReplaceOrder", "1");
        hashMap.put("ActionVersion", "2.0");

        new GetNetUtil(hashMap, Api.Mall_GetSubCategoryAndBrand, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (ShopClassifyFragment.this.isDestroy) {
                    return;
                }
                Log.i("-------->", basebean);
                Gson gson = new Gson();
                Basebean<ClassAndBrandBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ClassAndBrandBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    List<ClassAndBrandBean.BrandlistBean> brandlist = bean.getObj().getBrandlist();
                    if (brandlist != null && brandlist.size() >= 1) {
                        brandlist.get(0).setIsselect(true);
                    }
                    list_brand.clear();
                    list_brand.addAll(brandlist);
                    List<ClassAndBrandBean.SubCategorylistBean> subCategorylist = bean.getObj().getSubCategorylist();
                    if (subCategorylist != null && subCategorylist.size() >= 1) {
                        subCategorylist.get(0).setIsselect(true);
                    }
                    list_class.clear();
                    list_class.addAll(subCategorylist);
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }

    /**
     * 初始化需要循环的View
     * 为了灵活的使用滚动的View，所以把滚动的内容让用户自定义
     * 假如滚动的是三条或者一条，或者是其他，只需要把对应的布局，和这个方法稍微改改就可以了，
     * 一次显示两条数据 i=i+2
     */
    private void setView() {
        for (int i = 0; i < data.size(); i++) {
            //设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_view, null);
            TextView tv_desc = (TextView) moreView.findViewById(R.id.tv_desc);
            ImageView iv_icon = (ImageView) moreView.findViewById(R.id.iv_icon);
            tv_desc.setText(data.get(i).getTitle());
            if (!TextUtil.isEmpty(data.get(i).getICon())) {
                Picasso.with(mContext).load(data.get(i).getICon()).into(iv_icon);
                Log.i("====>>", "图片url-----" + data.get(i).getICon());
            }
            views.add(moreView);
        }
    }

    /**
     * 获取左侧一级分类数据
     */
    private void HttpLeftData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("BUserId", userId);
        hashMap.put("IsReplaceOrder", "1");
        hashMap.put("ActionVersion", "2.0");
        new GetNetUtil(hashMap, Api.Mall_GetItemCategory, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (ShopClassifyFragment.this.isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<ShopClassifyLeftBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopClassifyLeftBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {

                    SPUtil.remove(mContext, userId + "fragment");
                    SPUtil.putAndApply(mContext, userId + "fragment", basebean);

                    List<ShopClassifyLeftBean.ListBean> list = bean.getObj().getList();
                    //设置第一个角标“全部”是红色字体
                    if (list.size() >= 1) {
                        list.get(0).setSeleted(true);
                        CategoryId = list.get(0).getCategory_id();
                        if (CategoryId.equals("-1")) {
                            rlClassBrandLayout.setVisibility(View.GONE);
                        }
                        HttpRightData(1, 1, 0, "", false);
                    }
                    left_adapter.setRefrece(list);
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取右侧数据
     *
     * @param page
     * @param type
     * @param refreshtype
     */
    public void HttpRightData(int page, final int type, final int refreshtype, String brandAndclass, final boolean itemclick) {
        //refreshtype  0 第一次或者点击条目  1 下拉刷新  2  上滑加载
        //type 1:第一次或点击条目加载  2：上滑加载  3：下拉刷新
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("BUserId", userId);
        hashMap.put("PageSize", "0");
        hashMap.put("PageIndex", page + "");
        hashMap.put("CategoryId", CategoryId);
        hashMap.put("SubCategoryId", SubCategoryId);
        hashMap.put("BrandId", BrandId);
        hashMap.put("KeyWord", "");
        hashMap.put("IsReplaceOrder", "1");
        hashMap.put("ActionVersion", "2.0");
        hashMap.put("IsActionTest", AllConstant.IsActionTest);
        isItemClick = false;

        new GetNetUtil(hashMap, Api.Mall_SearchItemPageList, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                if (refreshtype == 1) {
                    srl.finishRefresh();
                }
                if (refreshtype == 2) {
                    srl.finishLoadMore();
                }
                isItemClick = true;
            }

            @Override
            public void successHandle(String basebean) {
                isItemClick = true;
                if (ShopClassifyFragment.this.isDestroy) {
                    return;
                }
                if (refreshtype == 1) {
                    srl.finishRefresh();
                }
                if (refreshtype == 2) {
                    srl.finishLoadMore();
                }
                Gson gson = new Gson();
                Basebean<ShopClassifyRightBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopClassifyRightBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {
                    List<SearchGoodsBean.ItemListBean> list = bean.getObj().getItemList();
                    switch (type) {
                        case 1:
                            right_adapter.RereshData(list, CategoryId);
                            if (tvRightNodata != null) {
                                if (list.size() == 0) {
                                    tvRightNodata.setText("您的商户还没有上架商品！");
                                    tvRightNodata.setVisibility(View.VISIBLE);
                                } else {
                                    tvRightNodata.setVisibility(View.GONE);
                                    move(0);
                                }
                            }
                            break;

                        case 2:
                            right_adapter.loadData(list, CategoryId);
                            break;

                        case 3:
                            right_adapter.RereshData(list, CategoryId);
                            break;
                    }

                    if (itemclick) {
                        /**
                         * 获取品牌品类数据
                         */
                        getClassAndBrandData();
                    }
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private boolean move = false;
    private int mIndex = 0;

    private void move(int n) {
        mIndex = n;
        recyclerView_right.stopScroll();
        moveToPosition(n);
//        smoothMoveToPosition(n);
    }


    private void moveToPosition(int n) {
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            recyclerView_right.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = recyclerView_right.getChildAt(n - firstItem).getTop();
            recyclerView_right.scrollBy(0, top);
        } else {
            recyclerView_right.scrollToPosition(n);
            move = true;
        }
    }

    private void smoothMoveToPosition(int n) {
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            recyclerView_right.smoothScrollToPosition(n);
        } else if (n <= lastItem) {
            int top = recyclerView_right.getChildAt(n - firstItem).getTop();
            recyclerView_right.smoothScrollBy(0, top);
        } else {
            recyclerView_right.smoothScrollToPosition(n);
            move = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_layout1, R.id.ll_layout2, R.id.ll_add, R.id.rl_reward_top_line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_layout1:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                if (null != list_class && list_class.size() > 0) {
                    showClassPop(rlClassBrandLayout);
                }
                break;

            case R.id.ll_layout2:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                if (null != list_brand && list_brand.size() > 0) {
                    showBrandPop(rlClassBrandLayout);
                }
                break;

            case R.id.ll_add:
                acitivty.selectIndx(1);
                acitivty.setTabSelection(1);
                break;

            case R.id.rl_reward_top_line:
                Intent intent = new Intent(mContext, ActiveActivity.class);
                intent.putExtra("basebean_active", basebean_active);
                startActivity(intent);
                break;
        }
    }


    /**
     * 展示品牌弹窗
     *
     * @param view
     */

    int width_window;
    int height_window;

    private void showBrandPop(View view) {
        View contentView = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.brand_popwindow, null);
        contentView.findViewById(R.id.pop_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        updateBrandData(contentView);
        llLeftLayout.measure(0, 0);
        int width = llLeftLayout.getMeasuredWidth();
        int height = llLeftLayout.getMeasuredHeight();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        width_window = wm.getDefaultDisplay().getWidth();
        height_window = wm.getDefaultDisplay().getHeight();
        Log.i("------->>>", "width_window-------" + width_window + "--------height_window----------" + height_window);
        int statusBarHeight = acitivty.getStatusBarHeight();
        Log.i("------->>>", "手机主题citivty.getStatusBarHeight-------" + statusBarHeight);
        Log.i("------->>>", "showHeight-------" + getdp2px());
        if (isShowTopView) {
            int hight = dip2px(mContext, 185);
            Log.i("------->>>", "减去高度-------" + hight);
        } else {
            int hight = dip2px(mContext, 145);
            Log.i("------->>>", "减去高度-------" + hight);
        }
        if (isShowTopView) {
            popupWindow = new PopupWindow(contentView, width_window - width - dip2px(mContext, 3),
                    height_window - statusBarHeight - dip2px(mContext, 185));
        } else {
            popupWindow = new PopupWindow(contentView, width_window - width - dip2px(mContext, 3),
                    height_window - statusBarHeight - dip2px(mContext, 145));
        }
        popupWindow.setOutsideTouchable(true);
        //返回必须加背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //这里很重要，不设置这个ListView得不到相应
        popupWindow.setFocusable(true);
        //设置背景色
//        setBackgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                tvTextview2.setTextColor(getResources().getColor(R.color.color_666666));
                ivImage2.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_arrow));
//                setBackgroundAlpha(1);
            }
        });
        if (list_brand != null && list_brand.size() > 0) {
            tvTextview2.setTextColor(getResources().getColor(R.color.text_red));
            ivImage2.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_arrow));
            popupWindow.showAsDropDown(view, 0, 0);
        }
    }

    private int getdp2px() {
        if (isShowTopView) {
            return height_window - statusBarHeight - dip2px(mContext, 185);
        } else {
            return height_window - statusBarHeight - dip2px(mContext, 145);
        }
    }

    HashMap<String, String> brandHashMap = new HashMap<>();

    private void updateBrandData(View view) {
        brandHashMap.clear();
        flowlayout2 = (FlowLayout2) view.findViewById(R.id.flowlayout2);
        flowlayout2.removeAllViews();
        for (int m = 0; m < list_brand.size(); m++) {
            brandHashMap.put(list_brand.get(m).getBrandName(), list_brand.get(m).getBrandId() + "-" + m);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dip2px(mContext, 5), dip2px(mContext, 10), dip2px(mContext, 5), dip2px(mContext, 10));
            TextView tv = new TextView(mContext);
            tv.setPadding(dip2px(mContext, 10), dip2px(mContext, 3), dip2px(mContext, 10), dip2px(mContext, 3));
            if (list_brand.get(m).isselect()) {
                tv.setTextColor(Color.parseColor("#e65252"));
                tv.setBackgroundResource(R.drawable.bg_tag_red);
            } else {
                tv.setTextColor(Color.parseColor("#666666"));
                tv.setBackgroundResource(R.drawable.bg_tag_black);
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setText(list_brand.get(m).getBrandName());
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
            flowlayout2.addView(tv, lp);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence text = ((TextView) v).getText();
                    String hashMap_brand_str = brandHashMap.get(text);
                    String[] split = hashMap_brand_str.split("-");
                    BrandId = split[0];
                    String position_str = split[1];
                    int position_int = Integer.parseInt(position_str);
                    for (int i = 0; i < list_brand.size(); i++) {
                        if (i == position_int) {
                            list_brand.get(i).setIsselect(true);
                        } else {
                            list_brand.get(i).setIsselect(false);
                        }
                    }
//                    SubCategoryId = "0";
                    HttpRightData(1, 1, 0, "brand", false);
                    popupWindow.dismiss();
                }
            });
        }
    }

    /**
     * 展示品类
     *
     * @param view
     */
    private void showClassPop(View view) {
        View contentView = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.brand_popwindow, null);
        contentView.findViewById(R.id.pop_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        updateClassData(contentView);
        llLeftLayout.measure(0, 0);
        int width = llLeftLayout.getMeasuredWidth();
        int height = llLeftLayout.getMeasuredHeight();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        width_window = wm.getDefaultDisplay().getWidth();
        height_window = wm.getDefaultDisplay().getHeight();
        Log.i("------->>>", "width_window-------" + width_window + "--------height_window----------" + height_window);
        int statusBarHeight = acitivty.getStatusBarHeight();
        Log.i("------->>>", "手机主题citivty.getStatusBarHeight-------" + statusBarHeight);
        Log.i("------->>>", "showHeight-------" + getdp2px());
        if (isShowTopView) {
            int hight = dip2px(mContext, 185);
            Log.i("------->>>", "减去高度-------" + hight);
        } else {
            int hight = dip2px(mContext, 145);
            Log.i("------->>>", "减去高度-------" + hight);
        }
        if (isShowTopView) {
            popupWindow = new PopupWindow(contentView, width_window - width - dip2px(mContext, 3),
                    height_window - statusBarHeight - dip2px(mContext, 185));
        } else {
            popupWindow = new PopupWindow(contentView, width_window - width - dip2px(mContext, 3),
                    height_window - statusBarHeight - dip2px(mContext, 145));
        }

        popupWindow.setOutsideTouchable(true);
        //返回必须加背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //这里很重要，不设置这个ListView得不到相应
        popupWindow.setFocusable(true);
        //设置背景色
//        setBackgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                setBackgroundAlpha(1);
                Log.i("------->>>", "关闭了");
                tvTextview1.setTextColor(getResources().getColor(R.color.color_666666));
                ivImage1.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_arrow));
            }
        });
        if (list_class != null && list_class.size() > 0) {
            tvTextview1.setTextColor(getResources().getColor(R.color.text_red));
            ivImage1.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_arrow));
            popupWindow.showAsDropDown(view, 0, 0);
        }
    }

    HashMap<String, String> classHashMap = new HashMap<>();

    private void updateClassData(View view) {
        classHashMap.clear();
        flowlayout2 = (FlowLayout2) view.findViewById(R.id.flowlayout2);
        flowlayout2.removeAllViews();
        for (int m = 0; m < list_class.size(); m++) {
            classHashMap.put(list_class.get(m).getName(), list_class.get(m).getCategory_id() + "-" + m);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dip2px(mContext, 5), dip2px(mContext, 10), dip2px(mContext, 5), dip2px(mContext, 10));

            TextView tv = new TextView(mContext);
            tv.setPadding(dip2px(mContext, 10), dip2px(mContext, 3), dip2px(mContext, 10), dip2px(mContext, 3));

            if (list_class.get(m).isselect()) {
                tv.setTextColor(Color.parseColor("#e65252"));
                tv.setBackgroundResource(R.drawable.bg_tag_red);
            } else {
                tv.setTextColor(Color.parseColor("#666666"));
                tv.setBackgroundResource(R.drawable.bg_tag_black);
            }

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setText(list_class.get(m).getName());
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
            flowlayout2.addView(tv, lp);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence text = ((TextView) v).getText();
                    String hashmap_class_str = classHashMap.get(text);
                    String[] split = hashmap_class_str.split("-");
                    SubCategoryId = split[0];
                    String position_str = split[1];
                    int position_int = Integer.parseInt(position_str);
                    for (int i = 0; i < list_class.size(); i++) {
                        if (i == position_int) {
                            list_class.get(i).setIsselect(true);
                        } else {
                            list_class.get(i).setIsselect(false);
                        }
                    }
//                    BrandId = "0";
                    HttpRightData(1, 1, 0, "class", false);
                    popupWindow.dismiss();
                }
            });
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
