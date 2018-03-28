package com.txh.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.math.BigDecimal;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.AllConstant;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.OrdersCommitBean;
import com.txh.im.bean.ShopCarNotSetissfyBean;
import com.txh.im.bean.SubmitOrderBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.MaxLengthWatcher;
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.widget.ChildListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderCommitActivity extends BasicActivity {

    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
    @Bind(R.id.tv_head_back)
    TextView tvHeadBack;
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_right)
    ImageView headRight;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.ll_add_address)
    LinearLayout llAddAddress;
    @Bind(R.id.rl_choose_address)
    RelativeLayout rlChooseAddress;
    @Bind(R.id.ll_update_addrees)
    LinearLayout llUpdateAddrees;
    @Bind(R.id.tv_order_pay_title)
    TextView tvOrderPayTitle;
    @Bind(R.id.view0)
    View view0;
    @Bind(R.id.ll_pay_type)
    LinearLayout llPayType;
    @Bind(R.id.tv_order_list_title)
    TextView tvOrderListTitle;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.tv_TotalNumber)
    TextView tvTotalNumber;
    @Bind(R.id.tv_ItemAmount)
    TextView tvItemAmount;
    @Bind(R.id.tv_TotalAmount)
    TextView tvTotalAmount;
    @Bind(R.id.tv_commit)
    TextView tvCommit;
    @Bind(R.id.ll_button_layout)
    RelativeLayout llButtonLayout;
    @Bind(R.id.tv_receiver_name)
    TextView tvReceiverName;
    @Bind(R.id.tv_receiver_phone)
    TextView tvReceiverPhone;
    @Bind(R.id.tv_receiver_address)
    TextView tvReceiverAddress;
    @Bind(R.id.ll_scroll_view_layout)
    LinearLayout llScrollViewLayout;
    @Bind(R.id.ll_detail_layout)
    LinearLayout llDetailLayout;
    @Bind(R.id.tv_DiscountAmount)
    TextView tvDiscountAmount;
    @Bind(R.id.tv_allmoney)
    TextView tvAllmoney;

    ChildListView listView;
    List<OrdersCommitBean.ItemListBean> itemList;
    @Bind(R.id.rl_zengpin_zone)
    RelativeLayout rlZengpinZone;
    @Bind(R.id.tv_coupon_desc)
    TextView tvCouponDesc;
    @Bind(R.id.rl_choose_coupon_layout)
    RelativeLayout rlChooseCouponLayout;
    @Bind(R.id.iv_coupon_iamge)
    ImageView ivCouponIamge;
    @Bind(R.id.tv_coupon_button)
    TextView tvCouponButton;
    @Bind(R.id.rl_coupon_layout_button)
    RelativeLayout rlCouponLayoutButton;
    @Bind(R.id.view_coupon)
    View viewCoupon;

    private LinearLayout ll_footer;
    private View footerview;
    private TextView goods_num;
    private ImageView spread;
    private boolean isshow = false;
    private ListViewAdapter listadapter;
    private int val = UIUtils.dip2px(60);
    int val_active_weight = UIUtils.dip2px(15);
    int val_active_high = UIUtils.dip2px(15);

    List<OrdersCommitBean.ItemListGivBean> itemList2 = new ArrayList<>();
    ChildListView list_view2;
    private LinearLayout ll_footer2;
    private View footerview2;
    private TextView goods_num2;
    private ImageView spread2;
    private boolean isshow2 = false;
    private ListViewAdapter2 listadapter2;

    OrdersCommitBean.AddressInfoBean addressInfo;
    OrdersCommitBean.TotalInfoBean totalInfo;
    private String addressId;
    private String OrdersCommitBean_str;
    private String sb_str;
    private String callBack_name;
    private String callBack_phone;
    private String callBack_address;
    private EditText etRemark;
    private String CartIds;
    private String userId = "";
    private String type;//购物车进来 1  代下单 进来 2
    List<OrdersCommitBean.CouponInfoBean.CouponListBean> couponList;
    private String callBackcouponId;
    private String callBackcouponValue;
    private String callBackConditionValue;
    private String callBackCouponListSize;
    private String totalAmountVal_str;
    OrdersCommitBean FirstOrdersCommitBean;

    @Override
    protected void initView() {
        OrdersCommitBean_str = getIntent().getStringExtra("OrdersCommitBean");
        sb_str = getIntent().getStringExtra("sb_str");
        CartIds = getIntent().getStringExtra("CartIds");
        userId = getIntent().getStringExtra("userId");
        type = getIntent().getStringExtra("type");
        setContentView(R.layout.activity_order_commit);
        etRemark = (EditText) findViewById(R.id.et_Remark);
        etRemark.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        listView = (ChildListView) findViewById(R.id.list_view);
        listView.setFocusable(false);
        footerview = View.inflate(OrderCommitActivity.this, R.layout.order_details_footerview, null);
        spread = (ImageView) footerview.findViewById(R.id.spread);
        goods_num = (TextView) footerview.findViewById(R.id.goods_num);
        ll_footer = (LinearLayout) footerview.findViewById(R.id.ll_footer);
        ll_footer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isshow) {
                    spread.setBackgroundResource(R.drawable.indent_arrowsm);
                    goods_num.setVisibility(View.VISIBLE);
                    int num = itemList.size() - 4;
                    goods_num.setText("还有" + num + "件未展开");
                    isshow = false;
                } else {
                    goods_num.setVisibility(View.GONE);
                    spread.setBackgroundResource(R.drawable.indent_arrows);
                    isshow = true;
                }
                listadapter = new ListViewAdapter(itemList, isshow, OrderCommitActivity.this);
                listView.setAdapter(listadapter);
            }
        });

        list_view2 = (ChildListView) findViewById(R.id.list_view_2);
        list_view2.setFocusable(false);
        footerview2 = View.inflate(OrderCommitActivity.this, R.layout.order_details_footerview2, null);
        spread2 = (ImageView) footerview2.findViewById(R.id.spread2);
        goods_num2 = (TextView) footerview2.findViewById(R.id.goods_num2);
        ll_footer2 = (LinearLayout) footerview2.findViewById(R.id.ll_footer2);
        ll_footer2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isshow2) {
                    spread2.setBackgroundResource(R.drawable.indent_arrowsm);
                    goods_num2.setVisibility(View.VISIBLE);
                    int num = itemList2.size() - 4;
                    goods_num2.setText("还有" + num + "件未展开");
                    isshow2 = false;
                } else {
                    goods_num2.setVisibility(View.GONE);
                    spread2.setBackgroundResource(R.drawable.indent_arrows);
                    isshow2 = true;
                }
                listadapter2 = new ListViewAdapter2(itemList2, isshow2, OrderCommitActivity.this);
                list_view2.setAdapter(listadapter2);
            }
        });

        etRemark.addTextChangedListener(new MaxLengthWatcher(OrderCommitActivity.this, 30, etRemark));

    }

    @Override
    protected void initTitle() {
        headTitle.setText("填写订单");
    }

    @Override
    public void initData() {
        HttpData(OrdersCommitBean_str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    /**
     * 获取订单详情数据
     */
    private void HttpData(String OrdersCommitBean_str) {
        Gson gson = new Gson();
        Basebean<OrdersCommitBean> bean = gson.fromJson(OrdersCommitBean_str, new TypeToken<Basebean<OrdersCommitBean>>() {
        }.getType());
        if (bean.getStatus().equals("200")) {
            FirstOrdersCommitBean = bean.getObj();
            addressInfo = bean.getObj().getAddressInfo();
            List<OrdersCommitBean.PayTypeListBean> payTypeList = bean.getObj().getPayTypeList();
            itemList = bean.getObj().getItemList();
            itemList2 = bean.getObj().getItemListGiv();
            if (itemList2.size() == 0) {
                rlZengpinZone.setVisibility(View.GONE);
            } else {
                rlZengpinZone.setVisibility(View.VISIBLE);
            }
            totalInfo = bean.getObj().getTotalInfo();
            updateAddressView();
            updateView(bean.getObj());

        } else {
            ToastUtils.showToast(OrderCommitActivity.this, bean.getMsg());
        }
    }

    private void updateAddressView() {
        /**
         * 更新收货人基本信息
         */

        if (addressInfo != null && !TextUtil.isEmpty(addressInfo.getAddressId())) {
            llAddAddress.setVisibility(View.GONE);
            llUpdateAddrees.setVisibility(View.VISIBLE);
            tvReceiverName.setText("收货人 ： " + addressInfo.getName());
            tvReceiverPhone.setText("手机 ： " + addressInfo.getPhone());
            tvReceiverAddress.setText("收货地址 ： " + addressInfo.getReceiveAddress());
            addressId = addressInfo.getAddressId();
        } else {
            llAddAddress.setVisibility(View.VISIBLE);
            llUpdateAddrees.setVisibility(View.GONE);
        }

    }

    private void updateView(OrdersCommitBean ordersCommitBean) {
        rlCouponLayoutButton.setVisibility(View.GONE);
        viewCoupon.setVisibility(View.GONE);
        tvCouponDesc.setTextColor(getResources().getColor(R.color.color_333333));

        if (totalInfo != null) {
            totalAmountVal_str = totalInfo.getTotalAmountVal();
            tvTotalNumber.setText(totalInfo.getTotalNumber());
            tvItemAmount.setText(totalInfo.getTotalItemAmount());
            tvTotalAmount.setText("合计 ：" + totalInfo.getTotalAmount());
            tvAllmoney.setText(totalInfo.getTotalAmount());
            tvDiscountAmount.setText(totalInfo.getDiscountAmount());
        }

        /**
         * 更新支付方式
         */
        List<OrdersCommitBean.PayTypeListBean> payTypeList = ordersCommitBean.getPayTypeList();
        updatePayType(payTypeList);

        /**
         * 更新优惠券数据
         */
        OrdersCommitBean.CouponInfoBean couponInfo = ordersCommitBean.getCouponInfo();
        if (couponInfo != null) {
            updateCouponView(couponInfo);
        }

        /**
         * childlistview---数据源
         */
        List<OrdersCommitBean.ItemListBean> itemList = ordersCommitBean.getItemList();
        /**
         * 内部商品条目展示
         */
        if (itemList.size() > 4) {
            spread.setBackgroundResource(R.drawable.indent_arrowsm);
            int num = itemList.size() - 4;
            goods_num.setText("还有" + num + "件未展开");
            listView.addFooterView(footerview);
        }
        listadapter = new ListViewAdapter(itemList, false, this);
        listView.setAdapter(listadapter);

        /**
         * childlistview---数据源---赠品
         */
        List<OrdersCommitBean.ItemListGivBean> itemListGiv = ordersCommitBean.getItemListGiv();
        if (itemListGiv.size() > 4) {
            spread2.setBackgroundResource(R.drawable.indent_arrowsm);
            int num = itemListGiv.size() - 4;
            goods_num2.setText("还有" + num + "件未展开");
            list_view2.addFooterView(footerview2);
        }
        listadapter2 = new ListViewAdapter2(itemListGiv, false, this);
        list_view2.setAdapter(listadapter2);

    }

    /**
     * 更新优惠券信息
     *
     * @param couponInfo
     */
    private void updateCouponView(OrdersCommitBean.CouponInfoBean couponInfo) {
        couponList = couponInfo.getCouponList();
        if (!TextUtil.isEmpty(couponInfo.getCouponCount()) && !couponInfo.getCouponCount().equals("0")) {
            tvCouponDesc.setText(couponInfo.getCouponCount() + "张可用");
            ivCouponIamge.setVisibility(View.VISIBLE);
        } else {
            tvCouponDesc.setText("暂无可用");
            ivCouponIamge.setVisibility(View.GONE);
        }
    }

    private void updatePayType(List<OrdersCommitBean.PayTypeListBean> payTypeList) {
        int size = payTypeList.size();
        llPayType.removeAllViews();
        if (size > 0 && payTypeList != null) {
            for (int i = 0; i < size; i++) {
                View view = View.inflate(OrderCommitActivity.this, R.layout.item_pay_type, null);
                TextView tv_key = (TextView) view.findViewById(R.id.tv_name);
                tv_key.setSingleLine();
                tv_key.setTextSize(13);
                tv_key.setPadding(5, 5, 5, 5);
                tv_key.setTextColor(getResources().getColor(R.color.text_red));
                tv_key.setText(payTypeList.get(i).getTypeText());
                llPayType.addView(view);
            }
        }
    }

    @OnClick({R.id.tv_head_back, R.id.ll_head_back, R.id.ll_add_address, R.id.ll_detail_layout,
            R.id.tv_commit, R.id.rl_choose_address, R.id.rl_choose_coupon_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_head_back:
                finish();
                break;

            case R.id.ll_head_back:
                finish();
                break;

            case R.id.ll_add_address:
                Intent intent2 = new Intent(OrderCommitActivity.this, GoodsAddressActivity.class);
                intent2.putExtra("OrderAddress", "blblblblblblb");
                intent2.putExtra("userId", userId);
                intent2.putExtra("commitAndAdd", "babababa");
                startActivityForResult(intent2, 101);
                break;

            case R.id.ll_detail_layout:
                Intent intent = new Intent(OrderCommitActivity.this, GoodsAddressActivity.class);
                intent.putExtra("OrderAddress", "blblblblblblb");
                intent.putExtra("userId", userId);
                if (!TextUtil.isEmpty(addressId)) {
                    intent.putExtra("addressId", addressId);
                }
                startActivityForResult(intent, 100);
                break;

            case R.id.rl_choose_address:
                Intent intent3 = new Intent(OrderCommitActivity.this, GoodsAddressActivity.class);
                intent3.putExtra("OrderAddress", "blblblblblblb");
                intent3.putExtra("userId", userId);
                if (!TextUtil.isEmpty(addressId)) {
                    intent3.putExtra("addressId", addressId);
                }
                startActivityForResult(intent3, 100);
                break;

            case R.id.tv_commit:
                HttpCommitData();
                break;

            case R.id.rl_choose_coupon_layout:
                if (couponList != null && couponList.size() > 0) {
                    Intent intent1 = new Intent(OrderCommitActivity.this, CouponListActivity.class);
                    intent1.putExtra("OrdersCommitBean_str", OrdersCommitBean_str);
                    intent1.putExtra("callBackcouponId", callBackcouponId);
                    startActivityForResult(intent1, 103);
                } else {
//                    ToastUtils.toast(OrderCommitActivity.this, "暂无优惠券可用");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    addressId = data.getStringExtra("AddressId");
                    callBack_name = data.getStringExtra("Name");
                    callBack_phone = data.getStringExtra("Phone");
                    callBack_address = data.getStringExtra("Address");
                    updateCallBackAddress();
                }
                break;

            case 101:
                if (resultCode == RESULT_OK) {
                    addressId = data.getStringExtra("AddressId");
                    callBack_name = data.getStringExtra("Name");
                    callBack_phone = data.getStringExtra("Phone");
                    callBack_address = data.getStringExtra("Address");
                    updateCallBackAddress();
                }
                break;

            case 103:
                if (resultCode == RESULT_OK) {

                    callBackcouponId = data.getStringExtra("CouponId");
                    callBackcouponValue = data.getStringExtra("CouponValue");
                    callBackConditionValue = data.getStringExtra("ConditionValue");
                    callBackCouponListSize = data.getStringExtra("ConditionCouponListSize");

                    Log.i("--------->>>>", "callBackcouponId-------" + callBackcouponId);
                    Log.i("--------->>>>", "callBackcouponValue-------" + callBackcouponValue);
                    Log.i("--------->>>>", "callBackConditionValue-------" + callBackConditionValue);

                    if (!TextUtil.isEmpty(callBackcouponId) && !TextUtil.isEmpty(callBackcouponValue)) {
                        updateCallBackCouponView();
                    } else {
                        tvCouponDesc.setText(callBackCouponListSize + "张可用");
                        ivCouponIamge.setVisibility(View.VISIBLE);
                        updateView(FirstOrdersCommitBean);
                    }
                }
                break;
        }
    }

    private int callBackcouponValue_int;
    private Double totalAmountVal_double;

    private void updateCallBackCouponView() {
        rlCouponLayoutButton.setVisibility(View.VISIBLE);
        viewCoupon.setVisibility(View.VISIBLE);
        tvCouponDesc.setText(callBackConditionValue);
        tvCouponDesc.setTextColor(getResources().getColor(R.color.text_red));
        callBackcouponValue_int = Integer.parseInt(callBackcouponValue);
        tvCouponButton.setText("- ¥" + callBackcouponValue_int + ".00");
        totalAmountVal_double = Double.parseDouble(totalAmountVal_str);
        totalAmountVal_double = totalAmountVal_double - callBackcouponValue_int;

        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        String totalAmountVal_double_format = df.format(totalAmountVal_double);
        totalAmountVal_double = Double.parseDouble(totalAmountVal_double_format);

        String totalAmountVal_double_str;
        if (totalAmountVal_double <= 0) {
            totalAmountVal_double_str = "0.00";
        } else {
            totalAmountVal_double_str = totalAmountVal_double + "";
        }
        if (totalAmountVal_double_str.endsWith("00")) {
            //tv_allmoney  订单应付款
            //tv_TotalAmount 提交订单按钮底部数据
            tvAllmoney.setText("¥" + totalAmountVal_double_str);
            tvTotalAmount.setText("合计 ：" + "¥" + totalAmountVal_double_str);
        } else {
            tvAllmoney.setText("¥" + totalAmountVal_double_str + "0");
            tvTotalAmount.setText("合计 ：" + "¥" + totalAmountVal_double_str + "0");
        }
    }

    private void updateCallBackAddress() {
        if (TextUtil.isEmpty(addressId)) {
            //地址列表回调---地址列表没有数据
            llAddAddress.setVisibility(View.VISIBLE);
            llUpdateAddrees.setVisibility(View.GONE);
        } else {
            llAddAddress.setVisibility(View.GONE);
            llUpdateAddrees.setVisibility(View.VISIBLE);
            tvReceiverName.setText("收货人 ： " + callBack_name);
            tvReceiverPhone.setText("手机 ： " + callBack_phone);
            tvReceiverAddress.setText("收货地址 ： " + callBack_address);
        }
    }

    /**
     * 提交订单
     */
    private void HttpCommitData() {
        HashMap<String, String> hashmap = new HashMap<>();

//        ShopItems	true	string	要去结算的商户商品shop、skuid及数量的集合（使用下划线“_”连接商户id与SkuId，后面用"~"连接数量，使用英文逗号将多个商品隔开；“ShopId_SkuId~Number,ShopId_SkuId~Numbe,ShopId_SkuId~Numbe”，如“1203_23434324~15,1243_343243244~13,1345_33432324~3”；其中1203、1243、1345为商户ID，23434324、343243244、33432324为商品skuid，15、13、3为对应商品的数量）
//        AddressId	true	int	收货地址ID
//        PayType	true	int	支付方式（货到现金支付=2）
//        Remark	false	string	订单备注

//        TestParameter	false	string	测试参数（传入2或3时提示要去凑单）传入TestParameter=2时返回2条去凑单商户列表信息，传入TestParameter=3时返回8条去凑单商户列表信息）
//        CartIds	false	string	购物车Id集合（2.0及以上的接口必须传入此项，ShopItems可以不传）
//        BUserId	false	int	买家的UserId(代下单的时候必填)
//        IsReplaceOrder	false	int	是否代下单列表（1表示代下单，其它为正常商品列表）
//        ActionVersion	false	string	不传默认1.0，当前接口版本号（2.0）
//        IsActionTest	false	string	是否测试：1为静态数据，不传或其他为动态数据
//        CouponIds	false	string	使用的优惠券Id和供应商Id集合（优惠券ID~供应商ID,优惠券ID~供应商ID,优惠券ID~供应商ID，如12~1202,13~1203,14~1204）


        if (TextUtil.isEmpty(addressId)) {
            ToastUtils.toast(OrderCommitActivity.this, "请添加收货地址");
            return;
        }
        hashmap.put("ShopItems", sb_str);
        hashmap.put("AddressId", addressId);
        hashmap.put("PayType", "2");
        hashmap.put("Remark", etRemark.getText().toString().trim());
        hashmap.put("CartIds", CartIds);
        if (type.equals("2")) {
            //代下单
            hashmap.put("BUserId", userId);
            hashmap.put("IsReplaceOrder", AllConstant.IsReplaceOrder);
        }
        hashmap.put("ActionVersion", AllConstant.ActionVersionFour);
        hashmap.put("IsActionTest", AllConstant.ActionTestFour);

        if (!TextUtil.isEmpty(callBackcouponId)) {
            hashmap.put("CouponIds", callBackcouponId);
        }

        showProgress(getResources().getString(R.string.http_loading_data));
        new GetNetUtil(hashmap, Api.Mall_SubmitOrder, this) {

            @Override
            public void successHandle(String base) {
                if (isDestroy) {
                    return;
                }
                hideProgress();
                Gson gson = new Gson();
                Basebean<SubmitOrderBean> basebean = gson.fromJson(base, new TypeToken<Basebean<SubmitOrderBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    ToastUtils.toast(OrderCommitActivity.this, basebean.getMsg());
                    Intent intent = new Intent(OrderCommitActivity.this, MyOrderActivity.class);
                    intent.putExtra("ShopId", GlobalApplication.getInstance().getPerson(OrderCommitActivity.this).getUnitId());
                    if (!TextUtil.isEmpty(type) && type.equals("2")) {
                        intent.putExtra("intent_where", "shoppingtrolley");
                    }
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.toast(OrderCommitActivity.this, basebean.getMsg());
                    if (basebean.getStatus().equals("45845")) {
                        //请检测这个状态，如果是这个状态，则表明使用的优惠券无效，
                        // 需要返回重新选择（新的优惠券列表和数量在obj中返回了）
                        //重新刷新数据
                        HttpBalanceAgain();
                    }
                }
            }
        };
    }

    private void HttpBalanceAgain() {
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("CartIds", CartIds);
        hashmap.put("ActionVersion", AllConstant.ActionVersionFour);
        hashmap.put("IsActionTest", AllConstant.ActionTestFour);
        showProgress(getResources().getString(R.string.http_loading_data));
        new GetNetUtil(hashmap, Api.TX_App_User_GetCartPreSubmitData, this) {

            @Override
            public void successHandle(String base) {
                if (isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<ShopCarNotSetissfyBean> basebean = gson.fromJson(base, new TypeToken<Basebean<ShopCarNotSetissfyBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    OrdersCommitBean_str = base;
                    HttpData(base);
                } else {
//                    ToastUtils.toast(ShopCarAcitivity.this, basebean.getMsg());
                }
            }
        };
    }

    /**
     * 订单提交赠品区
     */
    public class ListViewAdapter2 extends BaseAdapter {

        private List<OrdersCommitBean.ItemListGivBean> ddsuccess = new ArrayList<>();
        private boolean isshwo = false;
        private Context context;

        public ListViewAdapter2(List<OrdersCommitBean.ItemListGivBean> list, boolean isshwo, Context context) {
            this.context = context;
            this.ddsuccess = list;
            this.isshwo = isshwo;
        }

        @Override
        public int getCount() {
            if (isshwo) {
                return ddsuccess.size();
            } else {
                if (ddsuccess.size() > 4) {
                    return 4;
                } else {
                    return ddsuccess.size();
                }
            }
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(OrderCommitActivity.this).inflate(R.layout.order_details_item2, null);
                holder.iv_ItemPic = (ImageView) convertView.findViewById(R.id.iv_ItemPic);
                holder.tv_ItemName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_price_and_num = (TextView) convertView.findViewById(R.id.tv_price_and_num);
                holder.tv_zong_price = (TextView) convertView.findViewById(R.id.tv_zong_price);
                holder.tv_old_price = (TextView) convertView.findViewById(R.id.tv_old_price);
                holder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);
                holder.tv_active_desc = (TextView) convertView.findViewById(R.id.tv_active_desc);
                holder.iv_active_icon = (ImageView) convertView.findViewById(R.id.iv_active_icon);
                holder.view1 = (View) convertView.findViewById(R.id.view1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == ddsuccess.size() - 1) {
                holder.view1.setVisibility(View.GONE);
            } else {
                holder.view1.setVisibility(View.VISIBLE);
            }
            if (isshwo) {
            } else {
                if (position == 3) {
                    holder.view1.setVisibility(View.GONE);
                }
            }
            if (!TextUtil.isEmpty(ddsuccess.get(position).getItemPic())) {
                Picasso.with(context).load(ddsuccess.get(position).getItemPic()).resize(val, val).transform(
                        new RoundedTransformation(3, 0)).placeholder(R.drawable.default_good).into(holder.iv_ItemPic);
            } else {
                Picasso.with(context).load(R.drawable.default_good).resize(val, val).transform(
                        new RoundedTransformation(3, 0)).into(holder.iv_ItemPic);
            }

            holder.tv_ItemName.setText(ddsuccess.get(position).getItemName());

            holder.tv_price_and_num.setText(ddsuccess.get(position).getItemNumber());
            holder.tv_zong_price.setText(ddsuccess.get(position).getItemAmount());

            if (!TextUtil.isEmpty(ddsuccess.get(position).getOldPrice())) {
                holder.tv_old_price.setText("¥" + ddsuccess.get(position).getOldPrice());
                holder.tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if (!TextUtil.isEmpty(ddsuccess.get(position).getDiscount())) {
                holder.tv_discount.setText(ddsuccess.get(position).getDiscount());
                holder.tv_discount.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_full_orange));
            }
            if (!TextUtil.isEmpty(ddsuccess.get(position).getActivityTypeId()) &&
                    !ddsuccess.get(position).getActivityTypeId().equals("0")) {
                if (!TextUtil.isEmpty(ddsuccess.get(position).getActivityRemarkIcon())) {
                    Picasso.with(context).load(ddsuccess.get(position).getActivityRemarkIcon())
                            .resize(val_active_weight, val_active_high).transform(
                            new RoundedTransformation(3, 0)).into(holder.iv_active_icon);
                }
                holder.tv_active_desc.setText(ddsuccess.get(position).getActivityRemark());
            }
            return convertView;
        }

        public class ViewHolder {
            private TextView tv_ItemName;
            private TextView tv_zong_price, tv_old_price, tv_discount, tv_price_and_num, tv_active_desc;
            private ImageView iv_ItemPic, iv_active_icon;
            private View view1;
        }
    }

    /**
     * GirdView 数据适配器
     */
    public class ListViewAdapter extends BaseAdapter {

        private List<OrdersCommitBean.ItemListBean> ddsuccess = new ArrayList<>();
        private boolean isshwo = false;
        private Context context;

        public ListViewAdapter(List<OrdersCommitBean.ItemListBean> list, boolean isshwo, Context context) {
            this.context = context;
            this.ddsuccess = list;
            this.isshwo = isshwo;
        }

        @Override
        public int getCount() {
            if (isshwo) {
                return ddsuccess.size();
            } else {
                if (ddsuccess.size() > 4) {
                    return 4;
                } else {
                    return ddsuccess.size();
                }
            }
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(OrderCommitActivity.this).inflate(R.layout.order_details_item, null);
                holder.iv_ItemPic = (ImageView) convertView.findViewById(R.id.iv_ItemPic);
                holder.tv_ItemName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_price_and_num = (TextView) convertView.findViewById(R.id.tv_price_and_num);
                holder.tv_zong_price = (TextView) convertView.findViewById(R.id.tv_zong_price);
                holder.tv_old_price = (TextView) convertView.findViewById(R.id.tv_old_price);
                holder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);
                holder.tv_active_desc = (TextView) convertView.findViewById(R.id.tv_active_desc);
                holder.iv_active_icon = (ImageView) convertView.findViewById(R.id.iv_active_icon);
                holder.view1 = (View) convertView.findViewById(R.id.view1);
                holder.ll_layout_man = (LinearLayout) convertView.findViewById(R.id.ll_layout_man);
                holder.ll_layout_discount = (LinearLayout) convertView.findViewById(R.id.ll_layout_discount);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == ddsuccess.size() - 1) {
                holder.view1.setVisibility(View.GONE);
            } else {
                holder.view1.setVisibility(View.VISIBLE);
            }
            if (isshwo) {
            } else {
                if (position == 3) {
                    holder.view1.setVisibility(View.GONE);
                }
            }
            if (!TextUtil.isEmpty(ddsuccess.get(position).getItemPic())) {
                Picasso.with(context).load(ddsuccess.get(position).getItemPic()).resize(val, val).transform(
                        new RoundedTransformation(3, 0)).placeholder(R.drawable.default_good).into(holder.iv_ItemPic);
            } else {
                Picasso.with(context).load(R.drawable.default_good).resize(val, val).transform(
                        new RoundedTransformation(3, 0)).into(holder.iv_ItemPic);
            }
            holder.tv_ItemName.setText(ddsuccess.get(position).getItemName());
            holder.tv_price_and_num.setText(ddsuccess.get(position).getItemNumber());
            holder.tv_zong_price.setText(ddsuccess.get(position).getItemAmount());

            if (!TextUtil.isEmpty(ddsuccess.get(position).getOldPrice())) {
                holder.tv_old_price.setText("¥" + ddsuccess.get(position).getOldPrice());
                holder.tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (!TextUtil.isEmpty(ddsuccess.get(position).getDiscount())) {
                holder.tv_discount.setText(ddsuccess.get(position).getDiscount());
                holder.tv_discount.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_full_orange));
            }

            if (!TextUtil.isEmpty(ddsuccess.get(position).getActivityTypeId()) &&
                    !ddsuccess.get(position).getActivityTypeId().equals("0")) {
                if (ddsuccess.get(position).getActivityTypeId().equals("1")) {
                    holder.ll_layout_man.setVisibility(View.VISIBLE);
                    holder.ll_layout_discount.setVisibility(View.GONE);
                } else {
                    holder.ll_layout_man.setVisibility(View.GONE);
                    holder.ll_layout_discount.setVisibility(View.VISIBLE);
                }
            } else {
                holder.ll_layout_man.setVisibility(View.GONE);
                holder.ll_layout_discount.setVisibility(View.GONE);
            }

            if (!TextUtil.isEmpty(ddsuccess.get(position).getActivityTypeId()) &&
                    !ddsuccess.get(position).getActivityTypeId().equals("0")) {
                if (!TextUtil.isEmpty(ddsuccess.get(position).getActivityRemarkIcon())) {
                    Picasso.with(context).load(ddsuccess.get(position).getActivityRemarkIcon())
                            .resize(val_active_weight, val_active_high).transform(
                            new RoundedTransformation(3, 0)).into(holder.iv_active_icon);
                }
                holder.tv_active_desc.setText(ddsuccess.get(position).getActivityRemark());
            }

            return convertView;
        }

        public class ViewHolder {
            private TextView tv_ItemName;
            private TextView tv_zong_price, tv_old_price, tv_discount, tv_price_and_num, tv_active_desc;
            private ImageView iv_ItemPic, iv_active_icon;
            private View view1;
            private LinearLayout ll_layout_man, ll_layout_discount;
        }
    }


    /**
     * 解决在页面底部置输入框，输入法弹出遮挡部分输入框的问题
     *
     * @param root       页面根元素
     * @param editLayout 被软键盘遮挡的输入框
     */
    public static void controlKeyboardLayout(final View root, final View editLayout) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInVisibleHeigh = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInVisibleHeigh > 100) {
                    Log.v("hjb", "不可视区域高度大于100，则键盘显示");
                    int[] location = new int[2];
                    //获取editLayout在窗体的坐标
                    editLayout.getLocationInWindow(location);
                    //计算root滚动高度，使editLayout在可见区域
                    int srollHeight = (location[1] + editLayout.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    Log.v("hjb", "不可视区域高度小于100，则键盘隐藏");
                    root.scrollTo(0, 0);
                }
            }
        });
    }
}



