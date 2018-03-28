package com.txh.im.activity;

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
import android.view.Display;
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
import com.txh.im.adapter.GoodsReplaceOrderRightAdapter;
import com.txh.im.adapter.SearchGoodsAdapter;
import com.txh.im.adapter.ShopClassifyLeftAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.ActiveNoticeBean;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.CartShelfNumBean;
import com.txh.im.bean.ChoosePersonTalkBean;
import com.txh.im.bean.ClassAndBrandBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.SearchGoodsBean;
import com.txh.im.bean.ShopClassifyLeftBean;
import com.txh.im.bean.ShopClassifyRightBean;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.widget.FlowLayout2;
import com.txh.im.widget.UPMarqueeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liheng on 2017/3/28.
 * <p>
 * 商品分类界面,//改版
 */

public class ShopClassifyActivity extends BasicActivity {

    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
    @Bind(R.id.tv_head_back)
    TextView tvHeadBack;
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.iv_headright2)
    ImageView ivHeadright2;
    @Bind(R.id.ll_head_right2)
    LinearLayout llHeadRight2;
    @Bind(R.id.iv_headright)
    ImageView ivHeadright;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.tv_search_content)
    TextView tvSearchContent;
    @Bind(R.id.rl_sv)
    RelativeLayout rlSv;
    @Bind(R.id.lv_leftlistview)
    ListView lvLeftlistview;
    @Bind(R.id.tv_right_nodata)
    TextView tvRightNodata;
    @Bind(R.id.rv_right)
    RecyclerView recyclerView_right;
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
    @Bind(R.id.rl_recycle_layout)
    RelativeLayout rlRecycleLayout;
    @Bind(R.id.upm_task_leader)
    UPMarqueeView upmTaskLeader;
    @Bind(R.id.tv_top_active_num)
    TextView tvTopActiveNum;//活动数量
    @Bind(R.id.rl_reward_top_line)
    RelativeLayout llRewardTopLine;//活动数量为0时 隐藏
    @Bind(R.id.ll_left_layout)
    LinearLayout llLeftLayout;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.iv_top_item)
    ImageView ivTopItem;
    @Bind(R.id.tv_top_item)
    TextView tvTopItem;

    private String shopId;
    private String shopName;
    List<ActiveNoticeBean> data = new ArrayList<>();
    List<View> views = new ArrayList<>();
    private ShopClassifyLeftAdapter left_adapter;
    private List<ShopClassifyLeftBean.ListBean> left_list = new ArrayList<>();
    private List<SearchGoodsBean.ItemListBean> right_list = new ArrayList<>();
    private HomeSingleListBean homeSingleListBean;
    private PopupWindow locationData;
    private SearchGoodsAdapter adapter;
    private List<SearchGoodsBean.ItemListBean> list = new ArrayList<>();
    private String searchData = "";
    private int page = 1;
    private GoodsReplaceOrderRightAdapter right_adapter;
    public boolean updateRightLists = false;
    private String basebean_active;
    private List<ClassAndBrandBean.SubCategorylistBean> list_class = new ArrayList<>();
    private List<ClassAndBrandBean.BrandlistBean> list_brand = new ArrayList<>();
    private Context mContext;
    private String CategoryId = "0";
    private String SubCategoryId = "0";
    private String BrandId = "0";
    private String iCon = "";
    private String title = "";
    int val_active_weight = UIUtils.dip2px(15);
    int val_active_high = UIUtils.dip2px(15);
    LinearLayoutManager linearLayoutManager;
    private boolean isShowTopView = false;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_shop_classfity_change);
        shopId = getIntent().getStringExtra("shopId");
        shopName = getIntent().getStringExtra("shopName");
        mContext = this;
    }

    @Override
    protected void initTitle() {
        ivHeadright.setVisibility(View.VISIBLE);
        ivHeadright.setBackground(getResources().getDrawable(R.drawable.home_chat_icon));
        /**
         * 二期搜索是将 llHeadRight2.setVisibility(View.VISIBLE);
         */
        llHeadRight2.setVisibility(View.VISIBLE);
        ivHeadright2.setVisibility(View.VISIBLE);
        ivHeadright2.setBackground(getResources().getDrawable(R.drawable.order_search));
        tvSearchContent.setText("商品名称");
        headTitle.setText(shopName);
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
        left_adapter = new ShopClassifyLeftAdapter(this, left_list, R.layout.item_left_classfity);
        lvLeftlistview.setAdapter(left_adapter);
        /**
         * 右边recycleview
         */
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_right.setLayoutManager(linearLayoutManager);
        recyclerView_right.setItemAnimator(new DefaultItemAnimator());
        right_adapter = new GoodsReplaceOrderRightAdapter(this, right_list, shopId, "0", tvGoodsNum, CategoryId);
        recyclerView_right.setAdapter(right_adapter);

        srl.setRefreshListener(new BaseRefreshListener() {

            @Override
            public void refresh() {
                if (isItemClick) {
                    HttpRightData(1, 1, 1, false);
                }
                page = 1;
            }

            @Override
            public void loadMore() {
                page++;
                if (isItemClick) {
                    HttpRightData(page, 2, 2, false);
                }
            }
        });

        String shopLeftList = (String) SPUtil.get(mContext, shopId, "");
        if (!TextUtil.isEmpty(shopLeftList)) {
            showLeftSaveData(shopLeftList);
        }
        HttpLeftData();
//        HttpRightData(1, 1, 0);
        page = 1;
//        recyclerView_right.addOnScrollListener(new RecyclerViewListener());


        List<String> list = new ArrayList<>();
        list.add("1111");
        String s = list.get(2);
        Log.i("====>>", "-----" + s);

    }

    private void showLeftSaveData(String basebean) {
        Gson gson = new Gson();
        Basebean<ShopClassifyLeftBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopClassifyLeftBean>>() {
        }.getType());

        if (bean.getStatus().equals("200")) {
            if (bean.getObj().getShopInfo().getShopName() != null) {
                headTitle.setText(bean.getObj().getShopInfo().getShopName());
            }
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

    private boolean isItemClick = true;

    @Override
    public void initData() {
        lvLeftlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (isItemClick) {
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
                            HttpRightData(1, 1, 0, true);
                            page = 1;
                        }
                    }
                }, 200);
            }
        });
        getShopNum();
        EventBus.getDefault().register(this);
    }

    //请求促销头条的数据
    private void getActiveData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);
        hashMap.put("IsActionTest", AllConstant.IsActionTest);

        new GetNetUtil(hashMap, Api.Mall_GetActivityInfo, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                IsFinish();
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
                            Picasso.with(ShopClassifyActivity.this).load(iCon).resize(val_active_weight, val_active_high).into(ivTopItem);
                        }
                        tvTopItem.setText(title);
                        isShowTopView = true;
                    } else {
                        llRewardTopLine.setVisibility(View.GONE);
                        isShowTopView = false;
                    }

                    /**
                     * 屏蔽防淘宝头条
                     */
//                    setView();
//                    upmTaskLeader.setViews(views);
                } else {
                    ToastUtils.showToast(ShopClassifyActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取品牌 品类数据
     */
    public void getClassAndBrandData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);
        hashMap.put("CategoryId", CategoryId);
        hashMap.put("ActionVersion", AllConstant.ActionVersion);

        new GetNetUtil(hashMap, Api.Mall_GetSubCategoryAndBrand, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                IsFinish();
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
                    ToastUtils.showToast(ShopClassifyActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取左侧一级分类数据
     */
    private void HttpLeftData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        hashMap.put("IsActionTest", AllConstant.IsActionTest);
        new GetNetUtil(hashMap, Api.Mall_GetItemCategory, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                IsFinish();
                Gson gson = new Gson();
                Basebean<ShopClassifyLeftBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopClassifyLeftBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {

                    SPUtil.remove(mContext, shopId);
                    SPUtil.putAndApply(mContext, shopId, basebean);

                    if (bean.getObj().getShopInfo().getShopName() != null) {
                        headTitle.setText(bean.getObj().getShopInfo().getShopName());
                    }
                    List<ShopClassifyLeftBean.ListBean> list = bean.getObj().getList();
                    //设置第一个角标“全部”是红色字体
                    if (list.size() >= 1) {
                        list.get(0).setSeleted(true);
                        CategoryId = list.get(0).getCategory_id();
                        if (CategoryId.equals("-1")) {
                            rlClassBrandLayout.setVisibility(View.GONE);
                        }
                        if (isItemClick) {
                            HttpRightData(1, 1, 0, false);
                        }
                    }
                    left_adapter.setRefrece(list);
                } else {
                    ToastUtils.showToast(ShopClassifyActivity.this, bean.getMsg());
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
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_view, null);
            TextView tv_desc = (TextView) moreView.findViewById(R.id.tv_desc);
            ImageView iv_icon = (ImageView) moreView.findViewById(R.id.iv_icon);
            tv_desc.setText(data.get(i).getTitle());
            if (!TextUtil.isEmpty(data.get(i).getICon())) {
                Picasso.with(this).load(data.get(i).getICon()).into(iv_icon);
                Log.i("====>>", "图片url-----" + data.get(i).getICon());
            }
            views.add(moreView);
        }
    }

    /**
     * 根据缓存数据，展示左侧界面
     *
     * @param sp_shopId_left_data
     */
    private void updateLeftView_sp_data(String sp_shopId_left_data) {
        Gson gson = new Gson();
        Basebean<ShopClassifyLeftBean> bean = gson.fromJson(sp_shopId_left_data, new TypeToken<Basebean<ShopClassifyLeftBean>>() {
        }.getType());
        if (bean.getObj().getShopInfo() != null && !TextUtil.isEmpty(bean.getObj().getShopInfo().getShopName())) {
            headTitle.setText(bean.getObj().getShopInfo().getShopName());
        }
        List<ShopClassifyLeftBean.ListBean> list = bean.getObj().getList();
        //设置第一个角标“全部”是红色字体
        if (list.size() >= 1) {
            list.get(0).setSeleted(true);
        }
        left_adapter.setRefrece(list);
    }

    @OnClick({R.id.ll_head_back, R.id.ll_head_right, R.id.rl_sv, R.id.ll_layout2, R.id.ll_layout1,
            R.id.ll_head_right2, R.id.fl_goods, R.id.rl_reward_top_line})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.ll_head_back:
                setResult(RESULT_OK);
                finish();
                break;

            case R.id.ll_head_right:
                HttpData();
                break;

            case R.id.rl_sv:
                intent = new Intent(ShopClassifyActivity.this, SearchGoodsActivity.class);
                intent.putExtra("shopId", shopId);
                startActivity(intent);
                break;

            case R.id.ll_layout2:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                if (isItemClick && null != list_brand && list_brand.size() > 0) {
                    showBrandPop(rlClassBrandLayout);
                }
                break;

            case R.id.ll_layout1:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                if (isItemClick && null != list_class && list_class.size() > 0) {
                    showClassPop(rlClassBrandLayout);
                }

                break;

            case R.id.ll_head_right2:
                Intent intent2 = new Intent(ShopClassifyActivity.this, SearchGoodsActivity.class);
                intent2.putExtra("shopId", shopId);
                startActivity(intent2);
                break;

            case R.id.fl_goods:
                Intent intent1 = new Intent(mContext, ShopCarAcitivity.class);
                intent1.putExtra("ShopClassifyActivity", "ShopClassifyActivity");
                startActivityForResult(intent1, 101);
                break;

            case R.id.rl_reward_top_line:
                intent = new Intent(mContext, ActiveActivity.class);
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
    private PopupWindow popupWindow;

    private void showBrandPop(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.brand_popwindow, null);
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
        rlRecycleLayout.measure(0, 0);
        int measuredWidth = rlRecycleLayout.getMeasuredWidth();
        int measuredHeight = rlRecycleLayout.getMeasuredHeight();
        Log.i("------->>>", "measuredWidth---" + measuredWidth + "-------measuredHeight---------" + measuredHeight);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        Log.i("------->>>", "screenWidth-------" + screenWidth + "--------screenHeight----------" + screenHeight);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width_window = wm.getDefaultDisplay().getWidth();
        int height_window = wm.getDefaultDisplay().getHeight();
        Log.i("------->>>", "手机屏幕width_window-------" + width_window + "--------手机屏幕height_window----------" + height_window);

        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("------->>>", "手机主题statusBarHeight-------" + statusBarHeight);
        int idip2px = dip2px(this, 135);
        Log.i("------->>>", "手机idip2px-------" + idip2px);
        int showHeight = height_window - statusBarHeight - dip2px(this, 135);
        Log.i("------->>>", "showHeight-------" + showHeight);
        if (isShowTopView) {
            popupWindow = new PopupWindow(contentView, width_window - width - dip2px(this, 3),
                    height_window - statusBarHeight - dip2px(this, 135));
        } else {
            popupWindow = new PopupWindow(contentView, width_window - width - dip2px(this, 3),
                    height_window - statusBarHeight - dip2px(this, 95));
        }

        popupWindow.setOutsideTouchable(true);
        //返回必须加背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //这里很重要，不设置这个ListView得不到相应
        popupWindow.setFocusable(true);
        //设置背景色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                tvTextview2.setTextColor(getResources().getColor(R.color.color_666666));
                ivImage2.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_arrow));
            }
        });
        if (list_brand != null && list_brand.size() > 0) {
            tvTextview2.setTextColor(getResources().getColor(R.color.text_red));
            ivImage2.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_arrow));
            popupWindow.showAsDropDown(view, 0, 0);

        }
    }

    HashMap<String, String> brandHashMap = new HashMap<>();
    private FlowLayout2 flowlayout2;

    private void updateBrandData(View view) {
        brandHashMap.clear();
        flowlayout2 = (FlowLayout2) view.findViewById(R.id.flowlayout2);
        flowlayout2.removeAllViews();
        for (int m = 0; m < list_brand.size(); m++) {
            brandHashMap.put(list_brand.get(m).getBrandName(), list_brand.get(m).getBrandId() + "-" + m);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dip2px(this, 5), dip2px(mContext, 10), dip2px(mContext, 5), dip2px(mContext, 10));
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
                    if (isItemClick) {
                        HttpRightData(1, 1, 0, false);
                    }
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
        View contentView = LayoutInflater.from(this).inflate(R.layout.brand_popwindow, null);
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
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width_window = wm.getDefaultDisplay().getWidth();
        int height_window = wm.getDefaultDisplay().getHeight();
        rlRecycleLayout.measure(0, 0);
        int measuredWidth = rlRecycleLayout.getMeasuredWidth();
        int measuredHeight = rlRecycleLayout.getMeasuredHeight();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int showHeight = height_window - statusBarHeight - dip2px(this, 135);
        if (isShowTopView) {
            popupWindow = new PopupWindow(contentView, width_window - width - dip2px(mContext, 3),
                    height_window - statusBarHeight - dip2px(this, 135));
        } else {
            popupWindow = new PopupWindow(contentView, width_window - width - dip2px(mContext, 3),
                    height_window - statusBarHeight - dip2px(this, 95));
        }

        popupWindow.setOutsideTouchable(true);
        //返回必须加背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //这里很重要，不设置这个ListView得不到相应
        popupWindow.setFocusable(true);
        //设置背景色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
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

    /**
     * 获取右侧数据
     *
     * @param page
     * @param type
     * @param refreshtype
     */
    public void HttpRightData(int page, final int type, final int refreshtype, final boolean itemclick) {
        //refreshtype  0 第一次或者点击条目  1 下拉刷新  2  上滑加载
        //type 1:第一次或点击条目加载  2：上滑加载  3：下拉刷新
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);
        hashMap.put("PageSize", "0");
        hashMap.put("PageIndex", page + "");
        hashMap.put("CategoryId", CategoryId);
        hashMap.put("SubCategoryId", SubCategoryId);
        hashMap.put("BrandId", BrandId);
//        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("KeyWord", "");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        hashMap.put("IsActionTest", AllConstant.IsActionTest);
        hashMap.put("IsReplaceOrder", "0");
        isItemClick = false;
        new GetNetUtil(hashMap, Api.Mall_SearchItemPageList, mContext) {

            @Override
            public void errorHandle() {
                isItemClick = true;
                super.errorHandle();
                if (refreshtype == 1) {
                    srl.finishRefresh();
                }
                if (refreshtype == 2) {
                    srl.finishLoadMore();
                }
            }

            @Override
            public void successHandle(String basebean) {
                isItemClick = true;
                IsFinish();
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
                    if (null != list) {
                        switch (type) {
                            case 1:
                                right_adapter.RereshData(list, CategoryId);
                                if (tvRightNodata != null) {
                                    if (list.size() == 0) {
                                        tvRightNodata.setVisibility(View.VISIBLE);
                                        tvRightNodata.setText("您的商户还没有上架商品！");
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
                    }

                    if (itemclick) {
                        /**
                         * 获取品牌品类数据
                         * */
                        getClassAndBrandData();
                    }
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }

            }
        };
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
                    if (isItemClick) {
                        HttpRightData(1, 1, 0, false);
                    }

                    popupWindow.dismiss();
                }
            });
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void IsFinish() {
        if (isDestroy) {
            return;
        }
    }

    /**
     * 获取聊天好友信息
     */
    private void HttpData() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);
        new GetNetUtil(hashMap, Api.Mall_GetUserListForChat, mContext) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                IsFinish();
                Gson gson = new Gson();
                Basebean<ChoosePersonTalkBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ChoosePersonTalkBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {

                    ChoosePersonTalkBean choosePersonTalkBean = bean.getObj();
                    List<ChoosePersonTalkBean.UserListBean> userList = bean.getObj().getUserList();
                    if (userList != null) {
                        if (userList.size() == 1) {
                            homeSingleListBean = new HomeSingleListBean();
                            Intent intent = new Intent(mContext, ChatAcitivty.class);
                            homeSingleListBean.setUserId(userList.get(0).getUserId());
                            homeSingleListBean.setUserName(userList.get(0).getUserName());
                            homeSingleListBean.setImagesHead(userList.get(0).getImagesHead());
                            intent.putExtra("homeSingleListBean", gson.toJson(homeSingleListBean));
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, ChoosePersonTalkWithActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("choosePersonTalkBean", choosePersonTalkBean);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }

                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }

    public void getShopNum() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        hashMap.put("IsReplaceOrder", "0");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        hashMap.put("IsActionTest", AllConstant.IsActionTest);
        new GetNetUtil(hashMap, Api.Mall_GetLatestCartShelfNum, mContext) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {


                IsFinish();


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

    @Subscribe
    public void onEventMainThread(FirstEventBus event) {
        if (event != null && event.getMsg().equals("ShopClassfiy")) {
            HttpRightData(1, 1, 0, false);
            getShopNum();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);//反注册EventBus
        super.onDestroy();
    }
}
