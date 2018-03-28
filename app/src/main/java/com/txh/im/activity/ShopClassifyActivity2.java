package com.txh.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.ShopClassifyLeftAdapter;
import com.txh.im.adapter.ShopClassifyRightAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.CartShelfNumBean;
import com.txh.im.bean.ChoosePersonTalkBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.SearchGoodsBean;
import com.txh.im.bean.ShopClassifyLeftBean;
import com.txh.im.bean.ShopClassifyRightBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.FlowLayout2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liheng on 2017/3/28.
 * <p>
 * 商品分类界面,//改版
 */

public class ShopClassifyActivity2 extends BasicActivity {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_search_content)
    TextView tvSearchContent;
    @Bind(R.id.rl_sv)
    RelativeLayout rlSv;
    @Bind(R.id.tv_right_nodata)
    TextView tvRightNodata;
    @Bind(R.id.rv_right)
    RecyclerView recyclerView_right;
    @Bind(R.id.iv_headright)
    ImageView ivHeadright;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.lv_leftlistview)
    ListView lvLeftlistview;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.fl_goods)
    FrameLayout flGoods;
    @Bind(R.id.srl)
    PullToRefreshLayout srl;
    @Bind(R.id.iv_headright2)
    ImageView ivHeadright2;
    @Bind(R.id.ll_head_right2)
    LinearLayout llHeadRight2;
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

    private String shopId;
    private String shopName;

    private ShopClassifyLeftAdapter left_adapter;
    private List<ShopClassifyLeftBean.ListBean> left_list = new ArrayList<>();

    private ShopClassifyRightAdapter right_adapter;
    private List<SearchGoodsBean.ItemListBean> right_list = new ArrayList<>();
    private int page = 1;
    private String CategoryId = "0";
    private boolean isItemClick = false;
    private HomeSingleListBean homeSingleListBean;
    public boolean updateRightLists = false;
    private PopupWindow locationData;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_shop_classfity);
        shopId = getIntent().getStringExtra("shopId");
        shopName = getIntent().getStringExtra("shopName");
        Log.i("========>", "分类界面--shopId---" + shopId);
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

        /**
         * 左边listview
         */
        left_adapter = new ShopClassifyLeftAdapter(this, left_list, R.layout.item_left_classfity);
        lvLeftlistview.setAdapter(left_adapter);

        /**
         * 右边recycleview
         */
        recyclerView_right.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView_right.setItemAnimator(new DefaultItemAnimator());
        right_adapter = new ShopClassifyRightAdapter(ShopClassifyActivity2.this, right_list, 0, shopId, tvGoodsNum);
        recyclerView_right.setAdapter(right_adapter);
        srl.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                // 结束刷新
                HttpRightData(1, 1, 1);
                page = 1;
            }

            @Override
            public void loadMore() {
                //加载更多功能的代码
                page++;
                HttpRightData(page, 2, 2);

            }
        });

        /**
         * 获取缓存的左侧数据
         */
        String shopIdshopClassifyBean = (String) SPUtil.get(ShopClassifyActivity2.this, shopId + "shopClassifyBean", "");
        if (!TextUtil.isEmpty(shopIdshopClassifyBean)) {
            updateLeftView_sp_data(shopIdshopClassifyBean);
            Log.i("------->>>", "分类左侧缓存数据" + "--------" + shopId + "shopClassifyBean");
        }

        HttpLeftData();

//        //TODO 缓存右侧数据仍需改进
        String shopIdshopClassifyRightBean = (String) SPUtil.get(ShopClassifyActivity2.this, shopId + "shopClassifyRightBean", "");
        if (!TextUtil.isEmpty(shopIdshopClassifyRightBean)) {
            updateRightView_sp_data(shopIdshopClassifyRightBean);
            Log.i("------->>>", "----分类右侧缓存数据" + "--------" + shopId + "shopClassifyRightBean");
        }

        HttpRightData(1, 1, 0);
        page = 1;

    }

    /**
     * 根据缓存展示右侧数据
     *
     * @param sp_shopId_right_data
     */
    private void updateRightView_sp_data(String sp_shopId_right_data) {
        Gson gson = new Gson();
        Basebean<ShopClassifyRightBean> bean = gson.fromJson(sp_shopId_right_data, new TypeToken<Basebean<ShopClassifyRightBean>>() {
        }.getType());

        if (bean.getStatus().equals("200")) {
            if (!TextUtil.isEmpty(bean.getObj().getCartItemNum())) {
                if (tvGoodsNum != null) {
                    tvGoodsNum.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(bean.getObj().getCartItemNum()) > 99) {
                        tvGoodsNum.setText("99+");
                    } else {
                        if (Integer.parseInt(bean.getObj().getCartItemNum()) == 0) {
                            tvGoodsNum.setVisibility(View.INVISIBLE);
                        } else {
                            tvGoodsNum.setVisibility(View.VISIBLE);
                            tvGoodsNum.setText(bean.getObj().getCartItemNum());
                        }
                    }
                }
            }

            List<SearchGoodsBean.ItemListBean> list = bean.getObj().getItemList();
            right_adapter.RereshData(list, tvGoodsNum);
            if (tvRightNodata != null) {
                if (list.size() == 0) {
                    tvRightNodata.setText("您的商户还没有上架商品！");
                    tvRightNodata.setVisibility(View.VISIBLE);
                } else {
                    tvRightNodata.setVisibility(View.GONE);
                }
            }
        } else {
            ToastUtils.showToast(ShopClassifyActivity2.this, bean.getMsg());
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

    /**
     * 获取分类右侧数据
     *
     * @param page
     * @param type
     */
    private void HttpRightData(int page, final int type, final int refreshtype) {
        //refreshtype  0 第一次或者点击条目  1 下拉刷新  2  上滑加载
        //type 1:第一次或点击条目加载  2：上滑加载  3：下拉刷新

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

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);
        hashMap.put("PageSize", "0");
        hashMap.put("PageIndex", page + "");
        hashMap.put("CategoryId", CategoryId);
        hashMap.put("KeyWord", "");
        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        hashMap.put("IsReplaceOrder", "0");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        Log.i("------>>", "PageSize---" + page);
        new GetNetUtil(hashMap, Api.Mall_SearchItemPageList, ShopClassifyActivity2.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                if (refreshtype == 1) {
                    srl.finishRefresh();
                }
                if (refreshtype == 2) {
                    // 结束加载更多
                    srl.finishLoadMore();
                }
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy){
                    return;
                }
                if (refreshtype == 1) {
                    srl.finishRefresh();
                }
                if (refreshtype == 2) {
                    // 结束加载更多
                    srl.finishLoadMore();
                }
                Log.i("------>>", "右侧数据---" + basebean);
                Gson gson = new Gson();
                Basebean<ShopClassifyRightBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopClassifyRightBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {

                    if (!TextUtil.isEmpty(bean.getObj().getCartItemNum())) {
                        if (tvGoodsNum != null) {
                            tvGoodsNum.setVisibility(View.VISIBLE);
                            if (Integer.parseInt(bean.getObj().getCartItemNum()) > 99) {
                                tvGoodsNum.setText("99+");
                            } else {
                                if (Integer.parseInt(bean.getObj().getCartItemNum()) == 0) {
                                    tvGoodsNum.setVisibility(View.INVISIBLE);
                                } else {
                                    tvGoodsNum.setVisibility(View.VISIBLE);
                                    tvGoodsNum.setText(bean.getObj().getCartItemNum());
                                }
                            }
                        }
                    }

                    List<SearchGoodsBean.ItemListBean> list = bean.getObj().getItemList();
                    switch (type) {
                        case 1:
                            right_adapter.RereshData(list, tvGoodsNum);

                            if (tvRightNodata != null) {
                                if (list.size() == 0) {
                                    tvRightNodata.setText("您的商户还没有上架商品！");
                                    tvRightNodata.setVisibility(View.VISIBLE);
                                } else {
                                    tvRightNodata.setVisibility(View.GONE);
//                                    //TODO 缓存右侧数据仍需改进  需要用shopid+CategoryId共同定义唯一的右侧数据的缓存
                                    SPUtil.remove(ShopClassifyActivity2.this, shopId + "shopClassifyRightBean");
                                    SPUtil.putAndApply(ShopClassifyActivity2.this, shopId + "shopClassifyRightBean", basebean);
//                                    Log.i("------->>>", "--------" + shopId + "shopClassifyRightBean");
                                }
                            }
                            break;

                        case 2:
                            right_adapter.loadData(list, tvGoodsNum);
                            break;

                        case 3:
                            right_adapter.RereshData(list, tvGoodsNum);
                            break;
                    }
                } else {
                    ToastUtils.showToast(ShopClassifyActivity2.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取分类左侧数据
     */
    private void HttpLeftData() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);
        new GetNetUtil(hashMap, Api.Mall_GetItemCategory, ShopClassifyActivity2.this) {

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
                Basebean<ShopClassifyLeftBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopClassifyLeftBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {

                    SPUtil.remove(ShopClassifyActivity2.this, shopId + "shopClassifyBean");
                    SPUtil.putAndApply(ShopClassifyActivity2.this, shopId + "shopClassifyBean", basebean);
                    Log.i("------->>>", "--------" + shopId + "shopClassifyBean");
                    if (bean.getObj().getShopInfo() != null && !TextUtil.isEmpty(bean.getObj().getShopInfo().getShopName())) {
                        headTitle.setText(bean.getObj().getShopInfo().getShopName());
                    }

                    List<ShopClassifyLeftBean.ListBean> list = bean.getObj().getList();
                    //设置第一个角标“全部”是红色字体
                    if (list.size() >= 1) {
                        list.get(0).setSeleted(true);
                    }
                    left_adapter.setRefrece(list);
                } else {
                    ToastUtils.showToast(ShopClassifyActivity2.this, bean.getMsg());
                }
            }
        };
    }

    @Override
    public void initData() {
        lvLeftlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isItemClick = true;
                for (int m = 0; m < left_list.size(); m++) {
                    left_list.get(m).setSeleted(false);
                }
                left_list.get(i).setSeleted(true);
                left_adapter.notifyDataSetChanged();
                CategoryId = left_list.get(i).getCategory_id();
                HttpRightData(1, 1, 0);
                page = 1;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, String> hashMap = new HashMap<>();

//        IsReturnCartNum	false	int	是否返回购物车中商品数量（0不返回，1返回）
//        IsReturnShelfNum	false	int	是否返回货架中商品数量（0不返回，1返回）
//        IsReplaceOrder	是	int	是否代理下单（0否，1是）
//        ActionVersion	false	string	不传默认1.0，当前接口版本号（2.0）
//        IsActionTest	false	string	是否测试：1为静态数据，不传或其他为动态数据

        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        hashMap.put("IsReplaceOrder", "0");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);

        new GetNetUtil(hashMap, Api.Mall_GetLatestCartShelfNum, this) {
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

    @OnClick({R.id.ll_head_back, R.id.ll_head_right, R.id.rl_sv, R.id.fl_goods, R.id.ll_head_right2, R.id.ll_layout1, R.id.ll_layout2})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_head_back:
                setResult(RESULT_OK);
                finish();
                break;

            case R.id.ll_head_right:
                HttpData();
                break;

            case R.id.rl_sv:
                Intent intent = new Intent(ShopClassifyActivity2.this, SearchGoodsActivity.class);
                intent.putExtra("shopId", shopId);
                startActivity(intent);
                break;

            case R.id.ll_head_right2:
                Intent intent2 = new Intent(ShopClassifyActivity2.this, SearchGoodsActivity.class);
                intent2.putExtra("shopId", shopId);
                startActivity(intent2);
                break;

            case R.id.fl_goods:
                Intent intent1 = new Intent(ShopClassifyActivity2.this, ShopCarAcitivity.class);
                intent1.putExtra("ShopClassifyActivity", "ShopClassifyActivity");
                startActivityForResult(intent1, 101);
                break;

            case R.id.ll_layout1:
//                ToastUtils.showToast(ShopClassifyActivity.this, "品类");
                showBrandPop(rlClassBrandLayout, "class");
                break;

            case R.id.ll_layout2:
//                ToastUtils.showToast(ShopClassifyActivity.this, "品牌");
                showBrandPop(rlClassBrandLayout, "brand");
                break;

        }
    }

    /**
     * 展示品牌
     *
     * @param view
     */
    private void showBrandPop(View view, final String type) {

//        if (locationData.isShowing()){
//            locationData.dismiss();
//        }

        View contentView = null;
        if (locationData == null) {
            contentView = getLayoutInflater().inflate(R.layout.brand_popwindow, null);
            updateBrandData(contentView);

            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            srl.measure(w, h);
            int height = srl.getMeasuredHeight();
            int width = srl.getMeasuredWidth();


            locationData = new PopupWindow(contentView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
            locationData.setOutsideTouchable(true);
            //返回必须加背景
            locationData.setBackgroundDrawable(new ColorDrawable(0));
            //这里很重要，不设置这个ListView得不到相应
            locationData.setFocusable(true);


            //设置背景色
//            setBackgroundAlpha(0.5f);


            locationData.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    if (type.equals("class")) {
                        //分类

                    } else {
                        //品牌

                    }

//                    setBackgroundAlpha(1);
//                    flag = true;
//                    iv_location.setImageResource(R.drawable.work_report_area);
//                    tv_area.setTextColor(getResources().getColor(R.color.da_gray_text_color));
//                    allowIcon.setImageResource(R.drawable.work_report_up);

                    Log.i("------->>>", "关闭了");

                }
            });
        }

        locationData.showAsDropDown(view, 0, 0);

    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = ShopClassifyActivity2.this.getWindow().getAttributes();
        lp.alpha = alpha;
        ShopClassifyActivity2.this.getWindow().setAttributes(lp);
    }

    /**
     * 填充品牌数据
     *
     * @param view
     */

    private FlowLayout2 flowlayout2;

    private void updateBrandData(View view) {

        flowlayout2 = (FlowLayout2) view.findViewById(R.id.flowlayout2);
        /**
         * 方式二
         */
        for (int m = 0; m < list_brand.size(); m++) {

//            int ranHeight = dip2px(this, 20); // 指定小条目的固定宽高
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dip2px(this, 5), dip2px(this, 10), dip2px(this, 5), dip2px(this, 10));
            TextView tv = new TextView(this);
            tv.setPadding(dip2px(this, 10), dip2px(this, 3), dip2px(this, 10), dip2px(this, 3));
            tv.setTextColor(Color.parseColor("#666666"));
            tv.setBackgroundResource(R.drawable.bg_tag_black);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setText(list_brand.get(m));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
            flowlayout2.addView(tv, lp);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                    locationData.dismiss();
                }
            });
        }

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取聊天好友信息
     */
    private void HttpData() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);
        new GetNetUtil(hashMap, Api.Mall_GetUserListForChat, ShopClassifyActivity2.this) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy){
                    return;
                }
                Log.i("-------->", basebean);
                Gson gson = new Gson();
                Basebean<ChoosePersonTalkBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ChoosePersonTalkBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {

                    ChoosePersonTalkBean choosePersonTalkBean = bean.getObj();
                    List<ChoosePersonTalkBean.UserListBean> userList = bean.getObj().getUserList();
                    if (userList != null) {
                        if (userList.size() == 1) {
                            homeSingleListBean = new HomeSingleListBean();
                            Intent intent = new Intent(ShopClassifyActivity2.this, ChatAcitivty.class);
                            homeSingleListBean.setUserId(userList.get(0).getUserId());
                            homeSingleListBean.setUserName(userList.get(0).getUserName());
                            homeSingleListBean.setImagesHead(userList.get(0).getImagesHead());
                            intent.putExtra("homeSingleListBean", gson.toJson(homeSingleListBean));
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(ShopClassifyActivity2.this, ChoosePersonTalkWithActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("choosePersonTalkBean", choosePersonTalkBean);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }

                } else {
                    ToastUtils.showToast(ShopClassifyActivity2.this, bean.getMsg());
                }
            }
        };
    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        new GetNetUtil(hashMap, Api.Mall_GetLatestCartShelfNum, ShopClassifyActivity.this) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
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
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            HttpRightData(1, 1, 0);
            page = 1;
        }
    }

    public boolean isUpdateRightLists() {
        return updateRightLists;
    }

    public void setUpdateRightLists(boolean updateRightLists) {
        this.updateRightLists = updateRightLists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    public static ArrayList<String> list_brand = new ArrayList<String>();

    static {
        list_brand.add("娃");
        list_brand.add("统");
        list_brand.add("康傅");
        list_brand.add("雀巢");
        list_brand.add("益达");
        list_brand.add("娃哈哈");
        list_brand.add("统一");
        list_brand.add("康师傅");
        list_brand.add("雀巢");
        list_brand.add("益达");
        list_brand.add("娃哈哈");
        list_brand.add("统一");
        list_brand.add("康师傅");
        list_brand.add("雀巢");
        list_brand.add("益达");
        list_brand.add("娃哈哈");
        list_brand.add("统一");
        list_brand.add("康师傅");
        list_brand.add("雀巢");
        list_brand.add("益达");
    }

}
