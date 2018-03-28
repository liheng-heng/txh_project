package com.txh.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.ShopClassifyPriceEditLeftAdapter;
import com.txh.im.adapter.ShopClassifyPriceEditRightAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.HttpErrorBean;
import com.txh.im.bean.PriceEditeBrandAndClassBean;
import com.txh.im.bean.ShopClassifyPriceEditLeftBean;
import com.txh.im.bean.ShopClassifyPriceEditRightBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.FlowLayout2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liheng on 2017/5/25.
 * 商品分类界面
 */

public class ShopClassifyPriceEditActivity extends BasicActivity {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
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
    @Bind(R.id.et_query)
    EditText etQuery;
    @Bind(R.id.ll_edit_group_name)
    LinearLayout llEditGroupName;
    @Bind(R.id.ll_left_layout)
    LinearLayout llLeftLayout;
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.tv_edittext)
    TextView tvEdittext;
    @Bind(R.id.tv_edit_show_name)
    TextView tvEditShowName;

    private ShopClassifyPriceEditLeftAdapter left_adapter;
    private List<ShopClassifyPriceEditLeftBean.CategoryListBean> left_list = new ArrayList<>();

    private ShopClassifyPriceEditRightAdapter right_adapter;
    private List<ShopClassifyPriceEditRightBean.ItemListBean> right_list = new ArrayList<>();

    private int page = 1;
    private String CategoryId = "0";
    private String ChildCategoryId = "0";
    private String BrandId = "0";
    private boolean isItemClick = false;
    private HomeSingleListBean homeSingleListBean;
    public boolean updateRightLists = false;
    private PopupWindow popupWindow;
    private FlowLayout2 flowlayout2;
    private String shopId = "";
    private String groupId;
    private String typeIn;
    private List<PriceEditeBrandAndClassBean.SubCategorylistBean> classList = new ArrayList<>();
    private List<PriceEditeBrandAndClassBean.BrandlistBean> brandList = new ArrayList<>();
    private String trim_str;
    private String groupName;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_price_edit_classfity);
        shopId = getIntent().getStringExtra("shopId");
        groupId = getIntent().getStringExtra("groupId");
        typeIn = getIntent().getStringExtra("typeIn");//1 单店单价  2 编辑价格

    }

    @Override
    protected void initTitle() {
        flGoods.setVisibility(View.GONE);
        ivHeadright.setVisibility(View.VISIBLE);
        ivHeadright.setBackground(getResources().getDrawable(R.drawable.order_search));
        /**
         * 左边listview
         */
        left_adapter = new ShopClassifyPriceEditLeftAdapter(this, left_list, R.layout.item_left_classfity);
        lvLeftlistview.setAdapter(left_adapter);
        /**
         * 右边recycleview
         */
        recyclerView_right.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView_right.setItemAnimator(new DefaultItemAnimator());
        right_adapter = new ShopClassifyPriceEditRightAdapter(ShopClassifyPriceEditActivity.this, right_list, typeIn, groupId, shopId);
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
        HttpLeftData();
        HttpRightData(1, 1, 0);
        page = 1;
        if (typeIn.equals("1")) {
            /**
             * 单店单价
             */
            etQuery.setVisibility(View.GONE);
            tvEdittext.setVisibility(View.VISIBLE);
            tvEditShowName.setText("价格分组名称：");

        } else {
            /**
             * 编辑价格
             */
            etQuery.setVisibility(View.VISIBLE);
            tvEdittext.setVisibility(View.GONE);
            tvEditShowName.setText("修改价格分组名称：");

        }
        hideKeyBoard();//隐藏键盘
        etQuery.addTextChangedListener(mTextWatcher1);
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalListerner = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {// 当layout执行结束后回调
            if (!isKeyboardShown(ll)) {
                if (etQuery.getText() == null) {
                    return;
                }
                if (etQuery.getText().length() == 0) {
                    if (groupName == null) {
                        return;
                    }
//                    etQuery.setCursorVisible(false);
                    etQuery.setText(groupName);
                    etQuery.setSelection(groupName.length());
                    HttpChangePriceGroupName(groupName);
                } else {
//                    etQuery.setCursorVisible(false);
                    HttpChangePriceGroupName(etQuery.getText().toString().trim());
                }
            }
        }
    };

    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    TextWatcher mTextWatcher1 = new TextWatcher() {

        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            editStart = etQuery.getSelectionStart();
            editEnd = etQuery.getSelectionEnd();

            if (temp.length() > 8) {
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                etQuery.setText(s);
                etQuery.setSelection(temp.length());
                //给出文字限制提示
                ToastUtils.toast(ShopClassifyPriceEditActivity.this, R.string.price_group_name_error);
            }

            trim_str = etQuery.getText().toString().trim();
//            if (!TextUtil.isEmpty(trim_str)) {
//                HttpChangePriceGroupName(trim_str);
//            } else {
////                ToastUtils.showToast(ShopClassifyPriceEditActivity.this, R.string.price_group_name_kong);
//            }

        }
    };


    private void HttpChangePriceGroupName(String GroupName) {
        String strPattern = "^[0-9a-zA-Z\\u4e00-\\u9fa5]{1,8}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(GroupName);
        if (!m.matches()) {
            ToastUtils.showToast(ShopClassifyPriceEditActivity.this, R.string.price_group_name_error);
            return;

        }

//        GroupId	true	int	分组ID
//        GroupName	true	string	分组名称
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("GroupId", groupId);
        hashMap.put("GroupName", GroupName);
        new GetNetUtil(hashMap, Api.PriceGroup_UpdateGroupName, ShopClassifyPriceEditActivity.this) {

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
                HttpErrorBean httpErrorBean = gson.fromJson(basebean, HttpErrorBean.class);
                if (httpErrorBean.getStatus().equals("200")) {
                } else {
                    ToastUtils.showToast(ShopClassifyPriceEditActivity.this, httpErrorBean.getMsg());
                }
            }
        };
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
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("PageSize", "0");
        hashMap.put("PageIndex", page + "");
        hashMap.put("CategoryId", CategoryId);
        hashMap.put("ChildCategoryId", ChildCategoryId);
        hashMap.put("BrandId", BrandId);
        hashMap.put("KeyWord", "");
        if (typeIn.equals("1")) {
            /**
             * 单店单价
             */
            hashMap.put("BUnitId", shopId);
        } else {
            /**
             * 编辑价格
             */
            hashMap.put("GroupId", groupId);
        }
        Log.i("------>>", "PageSize---" + page);
        new GetNetUtil(hashMap, getRightApi(), ShopClassifyPriceEditActivity.this) {

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
                if (isDestroy) {
                    return;
                }
                if (refreshtype == 1) {
                    srl.finishRefresh();
                }
                if (refreshtype == 2) {
                    srl.finishLoadMore();
                }
                Log.i("------>>", "右侧数据---" + basebean);
                Gson gson = new Gson();
                Basebean<ShopClassifyPriceEditRightBean> bean = gson.fromJson(basebean,
                        new TypeToken<Basebean<ShopClassifyPriceEditRightBean>>() {
                        }.getType());

                if (bean.getStatus().equals("200")) {

                    List<ShopClassifyPriceEditRightBean.ItemListBean> itemList = bean.getObj().getItemList();
                    switch (type) {
                        case 1:
                            if (null != itemList) {
                                right_adapter.RereshData(itemList, CategoryId, ChildCategoryId, BrandId, "");
                            }
                            if (tvRightNodata != null) {
                                if (itemList.size() == 0) {
                                    if ("-1".equals(CategoryId + "")) {
                                        tvRightNodata.setText("您的供应商还没有上架商品！");
                                    } else {
                                        tvRightNodata.setText("您的商户还没有上架商品！");
                                    }
                                    tvRightNodata.setVisibility(View.VISIBLE);
                                } else {
                                    tvRightNodata.setVisibility(View.GONE);
                                }
                            }
                            break;
                        case 2:
                            if (null != itemList) {
                                right_adapter.loadData(itemList, CategoryId, ChildCategoryId, BrandId, "");
                            }
                            break;
                        case 3:
                            if (null != itemList) {
                                right_adapter.RereshData(itemList, CategoryId, ChildCategoryId, BrandId, "");
                            }
                            break;
                    }
                } else {
                    ToastUtils.showToast(ShopClassifyPriceEditActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取分类左侧数据
     */
    private void HttpLeftData() {
        HashMap<String, String> hashMap = new HashMap<>();
        if (typeIn.equals("1")) {
//            单店单价
//            BUnitId	true	int	门店ID
            hashMap.put("BUnitId", shopId);
        } else {
//            编辑价格
//            GroupId	true	int	价格分组ID
            hashMap.put("GroupId", groupId);
        }

        new GetNetUtil(hashMap, getLeftApi(), ShopClassifyPriceEditActivity.this) {

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
                Basebean<ShopClassifyPriceEditLeftBean> bean = gson.fromJson(
                        basebean, new TypeToken<Basebean<ShopClassifyPriceEditLeftBean>>() {
                        }.getType());
                if (bean.getStatus().equals("200")) {
                    ShopClassifyPriceEditLeftBean beanObj = bean.getObj();
                    if (null == beanObj) return;
                    headTitle.setText(beanObj.getPageTitle());

                    if (typeIn.equals("1")) {
                        //单店单价
                        tvEdittext.setText(beanObj.getGroupName());
                    } else {
                        //编辑价格
                        groupName = beanObj.getGroupName();
                        if (groupName.length() > 8) {
                            groupName = groupName.substring(0, 8);
                        }
                        etQuery.setText(groupName);
                        etQuery.setSelection(groupName.length());

                    }
                    List<ShopClassifyPriceEditLeftBean.CategoryListBean> categoryList = beanObj.getCategoryList();
                    if (categoryList.size() >= 1) {
                        categoryList.get(0).setSeleted(true);
                        updateRightView(categoryList.get(0).getCategory_id());
                        CategoryId = categoryList.get(0).getCategory_id();
                        HttpRightData(1, 1, 0);

                        HttpClassData();
                        HttpBrandData();

                    }
                    left_adapter.setRefrece(categoryList);
                } else {
                    ToastUtils.showToast(ShopClassifyPriceEditActivity.this, bean.getMsg());
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
                ChildCategoryId = "0";
                BrandId = "0";
                updateRightView(CategoryId);
                HttpRightData(1, 1, 0);

                HttpClassData();
                HttpBrandData();

                page = 1;
            }
        });
//        etQuery.setCursorVisible(false);
//        etQuery.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                etQuery.setCursorVisible(true);
//                return false;
//            }
//        });
        ll.getViewTreeObserver().addOnGlobalLayoutListener(globalListerner);
    }

    private void updateRightView(String categoryId) {

        if (categoryId.equals("0") || categoryId.equals("-1")) {
            llLayout1.setVisibility(View.GONE);
            HttpBrandData();
        } else {
            llLayout1.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取品类数据
     */
    private void HttpClassData() {
        HashMap<String, String> hashMap = new HashMap<>();
//        CategoryId	true	int	商品一级分类ID（大于0）
        hashMap.put("CategoryId", CategoryId);
        new GetNetUtil(hashMap, Api.PriceGroup_GetChildCategory, ShopClassifyPriceEditActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                Gson gson = new Gson();
                Basebean<PriceEditeBrandAndClassBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<PriceEditeBrandAndClassBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {
                    List<PriceEditeBrandAndClassBean.SubCategorylistBean> category_List = bean.getObj().getCategoryList();
                    if (category_List != null && category_List.size() > 0) {
                        category_List.get(0).setIsselect(true);
                    }
                    classList.clear();
                    classList.addAll(category_List);

                } else {
                    ToastUtils.showToast(ShopClassifyPriceEditActivity.this, bean.getMsg());
                }
            }
        };

    }

    /**
     * 获取二级品牌数据
     */
    private void HttpBrandData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("CategoryId", CategoryId);
        new GetNetUtil(hashMap, Api.PriceGroup_GetItemBrand, ShopClassifyPriceEditActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                Gson gson = new Gson();
                Basebean<PriceEditeBrandAndClassBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<PriceEditeBrandAndClassBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {
                    List<PriceEditeBrandAndClassBean.BrandlistBean> brand_List = bean.getObj().getBrandList();
                    if (brand_List != null && brand_List.size() > 0) {
                        brand_List.get(0).setIsselect(true);
                    }
                    brandList.clear();
                    brandList.addAll(brand_List);

                } else {
                    ToastUtils.showToast(ShopClassifyPriceEditActivity.this, bean.getMsg());
                }
            }
        };
    }

    @OnClick({R.id.ll_head_back, R.id.ll_head_right, R.id.fl_goods, R.id.ll_layout1, R.id.ll_layout2})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_head_back:
                setResult(RESULT_OK);
                if (etQuery.getText() == null) {
                    finish();
                    return;
                }
                if (etQuery.getText().length() == 0) {
                    if (groupName == null) {
                        finish();
                        return;
                    }
                    etQuery.setText(groupName);
                    etQuery.setSelection(groupName.length());
                    HttpChangePriceGroupName(groupName);
                } else {
                    HttpChangePriceGroupName(etQuery.getText().toString().trim());
                }
                finish();
                break;

            case R.id.ll_head_right:
                //搜索价格分组商品界面
                Intent intent = new Intent(this, SearchEditPriceGoodsActivity.class);
                intent.putExtra("typeIn", typeIn);
                intent.putExtra("GroupId", groupId);
                intent.putExtra("shopId", shopId);
                startActivity(intent);
                break;

            case R.id.fl_goods:
                Intent intent1 = new Intent(ShopClassifyPriceEditActivity.this, ShopCarAcitivity.class);
                intent1.putExtra("ShopClassifyActivity", "ShopClassifyActivity");
                startActivityForResult(intent1, 101);
                break;

            case R.id.ll_layout1:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                showClassPop(rlClassBrandLayout);
                break;

            case R.id.ll_layout2:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                showBrandPop(rlClassBrandLayout);
                break;
        }
    }


    /**
     * 展示品牌弹窗
     *
     * @param view
     */
    private void showBrandPop(View view) {
        View contentView = getLayoutInflater().inflate(R.layout.brand_popwindow, null);
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
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width_window = wm.getDefaultDisplay().getWidth();
        int height_window = wm.getDefaultDisplay().getHeight();
        Log.i("------->>>", "width_window-------" + width_window + "--------height_window----------" + height_window);
        popupWindow = new PopupWindow(contentView, width_window - width - dip2px(this, 3),
                height_window - dip2px(this, 150));
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
        if (brandList != null && brandList.size() > 0) {
            tvTextview2.setTextColor(getResources().getColor(R.color.text_red));
            ivImage2.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_arrow));
            popupWindow.showAsDropDown(view, 0, 0);
        }
    }

    HashMap<String, String> brandHashMap = new HashMap<>();

    private void updateBrandData(View view) {
        brandHashMap.clear();
        flowlayout2 = (FlowLayout2) view.findViewById(R.id.flowlayout2);
        flowlayout2.removeAllViews();
        for (int m = 0; m < brandList.size(); m++) {
            brandHashMap.put(brandList.get(m).getBrandName(), brandList.get(m).getBrandId() + "-" + m);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dip2px(this, 5), dip2px(this, 10), dip2px(this, 5), dip2px(this, 10));
            TextView tv = new TextView(this);
            tv.setPadding(dip2px(this, 10), dip2px(this, 3), dip2px(this, 10), dip2px(this, 3));
            if (brandList.get(m).isselect()) {
                tv.setTextColor(Color.parseColor("#e65252"));
                tv.setBackgroundResource(R.drawable.bg_tag_red);
            } else {
                tv.setTextColor(Color.parseColor("#666666"));
                tv.setBackgroundResource(R.drawable.bg_tag_black);
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setText(brandList.get(m).getBrandName());
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
                    for (int i = 0; i < brandList.size(); i++) {
                        if (i == position_int) {
                            brandList.get(i).setIsselect(true);
                        } else {
                            brandList.get(i).setIsselect(false);
                        }
                    }
//                    SubCategoryId = "0";
                    HttpRightData(1, 1, 0);
                    popupWindow.dismiss();
                }
            });
        }

    }

    /**
     * 展示品类弹窗
     *
     * @param view
     */
    private void showClassPop(View view) {
        View contentView = getLayoutInflater().inflate(R.layout.brand_popwindow, null);
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
        popupWindow = new PopupWindow(contentView, width_window - width - dip2px(ShopClassifyPriceEditActivity.this, 3),
                height_window - dip2px(ShopClassifyPriceEditActivity.this, 150));
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
        if (classList != null && classList.size() > 0) {
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
        for (int m = 0; m < classList.size(); m++) {
            classHashMap.put(classList.get(m).getName(), classList.get(m).getCategory_id() + "-" + m);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dip2px(this, 5), dip2px(this, 10), dip2px(this, 5), dip2px(this, 10));

            TextView tv = new TextView(this);
            tv.setPadding(dip2px(this, 10), dip2px(this, 3), dip2px(this, 10), dip2px(this, 3));

            if (classList.get(m).isselect()) {
                tv.setTextColor(Color.parseColor("#e65252"));
                tv.setBackgroundResource(R.drawable.bg_tag_red);
            } else {
                tv.setTextColor(Color.parseColor("#666666"));
                tv.setBackgroundResource(R.drawable.bg_tag_black);
            }

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setText(classList.get(m).getName());
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
            flowlayout2.addView(tv, lp);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence text = ((TextView) v).getText();
                    String hashmap_class_str = classHashMap.get(text);
                    String[] split = hashmap_class_str.split("-");
                    ChildCategoryId = split[0];
                    String position_str = split[1];
                    int position_int = Integer.parseInt(position_str);
                    for (int i = 0; i < classList.size(); i++) {
                        if (i == position_int) {
                            classList.get(i).setIsselect(true);
                        } else {
                            classList.get(i).setIsselect(false);
                        }
                    }
                    HttpRightData(1, 1, 0);
                    popupWindow.dismiss();
                }
            });
        }

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private String getLeftApi() {
        if (typeIn.equals("1")) {
            return Api.PriceGroup_GetCategoryForUPrice;
        } else {
            return Api.PriceGroup_GetItemCategoryForGroup;
        }
    }

    private String getRightApi() {
        if (typeIn.equals("1")) {
            return Api.PriceGroup_SearchItemPLForUPrice;
        } else {
            return Api.PriceGroup_SearchItemPageListForGroup;
        }
    }

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
    protected void onDestroy() {
        ll.getViewTreeObserver().removeOnGlobalLayoutListener(globalListerner);//Added in API level 16
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
