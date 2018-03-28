package com.txh.im.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.txh.im.bean.ChoosePersonTalkBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.MyOrderContentBean;
import com.txh.im.bean.MyOrderTitleBean;
import com.txh.im.bean.OrdersDetailBean;
import com.txh.im.bean.UpdateOrderBean;
import com.txh.im.utils.CustomDialog;
import com.txh.im.utils.GetNetUtil;
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

/**
 * 订单详情
 * liheng---4.12
 */
public class MyOrderDetailActivity extends BasicActivity {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.iv_headright)
    ImageView ivHeadright;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.tv_OrderStatusName)
    TextView tvOrderStatusName;
    @Bind(R.id.tv_Name)
    TextView tvName;
    @Bind(R.id.tv_Phone)
    TextView tvPhone;
    @Bind(R.id.ll_user_info)
    LinearLayout llUserInfo;
    @Bind(R.id.tv_ReceiveAddress)
    TextView tvReceiveAddress;
    @Bind(R.id.tv_order_list_title)
    TextView tvOrderListTitle;
    @Bind(R.id.tv_PayTypeName)
    TextView tvPayTypeName;
    @Bind(R.id.tv_TotalNumber)
    TextView tvTotalNumber;
    @Bind(R.id.tv_ItemAmount)
    TextView tvTotalAmount;
    @Bind(R.id.ll_OrderInfo)
    LinearLayout llOrderInfo;
    @Bind(R.id.tv_Remark)
    TextView tvRemark;
    @Bind(R.id.tv_black)
    TextView tvBlack;
    @Bind(R.id.tv_red)
    TextView tvRed;
    @Bind(R.id.ll_button_layout)
    LinearLayout llButtonLayout;
    @Bind(R.id.iv_headright2)
    ImageView ivHeadright2;
    @Bind(R.id.ll_head_right2)
    LinearLayout llHeadRight2;
    @Bind(R.id.tv_order_type)
    TextView tvOrderType;
    @Bind(R.id.tv_refuse)
    TextView tvRefuse;
    @Bind(R.id.tv_DiscountAmount)
    TextView tvDiscountAmount;
    @Bind(R.id.tv_TotalAmount_new)
    TextView tvTotalAmountNew;
    @Bind(R.id.rl_refuse_layout)
    RelativeLayout rlRefuseLayout;
    @Bind(R.id.rl_zengpin_zone)
    RelativeLayout rlZengpinZone;
    @Bind(R.id.view_coupon)
    View viewCoupon;
    @Bind(R.id.tv_coupon)
    TextView tvCoupon;
    @Bind(R.id.rl_coupon_layout)
    RelativeLayout rlCouponLayout;

    private String OrderId;
    private OrdersDetailBean ordersDetailBean;
    private List<OrdersDetailBean.ItemListBean> itemList;

    private LinearLayout ll_footer;
    private View footerview;
    private TextView goods_num;
    private ImageView spread;
    private boolean isshow = false;
    private ListViewAdapter listadapter;
    private int val = UIUtils.dip2px(60);
    private AlertDialog mAlertDialog = null;
    private ChildListView listView;
    boolean btn_list_more = false;
    boolean updated_btn = false;
    private String shopId;

    List<OrdersDetailBean.ItemListGivBean> itemList2 = new ArrayList<>();
    ChildListView list_view2;
    private LinearLayout ll_footer2;
    private View footerview2;
    private TextView goods_num2;
    private ImageView spread2;
    private boolean isshow2 = false;
    private ListViewAdapter2 listadapter2;
    int val_active_weight = UIUtils.dip2px(15);
    int val_active_high = UIUtils.dip2px(15);
    private String first_refuse_str = "";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_order_detail);
        shopId = getIntent().getStringExtra("shopId");
        listView = (ChildListView) findViewById(R.id.list_view);
        listView.setFocusable(false);
        footerview = View.inflate(MyOrderDetailActivity.this, R.layout.order_details_footerview, null);
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
                listadapter = new ListViewAdapter(itemList, isshow, MyOrderDetailActivity.this);
                listView.setAdapter(listadapter);
            }
        });

        list_view2 = (ChildListView) findViewById(R.id.list_view_2);
        list_view2.setFocusable(false);
        footerview2 = View.inflate(MyOrderDetailActivity.this, R.layout.order_details_footerview2, null);
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
                listadapter2 = new ListViewAdapter2(itemList2, isshow2, MyOrderDetailActivity.this);
                list_view2.setAdapter(listadapter2);
            }
        });
    }

    @Override
    protected void initTitle() {
        ivHeadright.setVisibility(View.GONE);
        llHeadRight2.setVisibility(View.VISIBLE);
        ivHeadright2.setBackground(getResources().getDrawable(R.drawable.home_chat_icon));
        headTitle.setText("订单详情");
    }

    @Override
    public void initData() {
        OrderId = getIntent().getStringExtra("OrderId");
        HttpData();
        HttpTitleData();
    }

    private void HttpTitleData() {
        HashMap<String, String> hashMap = new HashMap<>();
        //ShopId	false	int	商户ID（卖家身份时必传，买家不用传）
        String unitType = GlobalApplication.getInstance().getPerson(MyOrderDetailActivity.this).getUnitType();
        if (unitType.equals("2")) {
            hashMap.put("ShopId", shopId);
        }

        new GetNetUtil(hashMap, Api.Mall_GetOrderListTypes, MyOrderDetailActivity.this) {

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
                    refuseRmearkList = bean.getObj().getRefuseRmearkList();

                    if (null != refuseRmearkList && refuseRmearkList.size() > 0) {
                        refuseRmearkList.get(0).setIsseleted(true);
                        itemcode = refuseRmearkList.get(0).getItemCode();
                        first_refuse_str = refuseRmearkList.get(0).getItemText();
                    }

                } else {
                    ToastUtils.showToast(MyOrderDetailActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取订单详情数据
     */
    private void HttpData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("OrderId", OrderId);
        hashMap.put("ActionVersion", AllConstant.ActionVersionFour);
        hashMap.put("IsActionTest", AllConstant.ActionTestFour);
        showProgress(getString(R.string.http_loading_data));
        new GetNetUtil(hashMap, Api.Mall_GetOrderDetail, MyOrderDetailActivity.this) {

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
                Log.i("------>", "订单详情-------" + basebean);
                Gson gson = new Gson();
                Basebean<OrdersDetailBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<OrdersDetailBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    ordersDetailBean = bean.getObj();
                    itemList = bean.getObj().getItemList();
                    itemList2 = bean.getObj().getItemListGiv();
                    if (itemList2.size() == 0) {
                        rlZengpinZone.setVisibility(View.GONE);
                    } else {
                        rlZengpinZone.setVisibility(View.VISIBLE);
                    }
                    updateView(ordersDetailBean);
                } else {
                    ToastUtils.showToast(MyOrderDetailActivity.this, bean.getMsg());
                }
            }
        };
    }

    private void updateView(OrdersDetailBean detailBean) {
        /**
         * 基本信息的显示
         */
        tvOrderStatusName.setText(detailBean.getOrderStatusName());
        tvName.setText(detailBean.getAddressInfo().getName());
        tvPhone.setText(detailBean.getAddressInfo().getPhone());
        tvReceiveAddress.setText(detailBean.getAddressInfo().getReceiveAddress());
        tvPayTypeName.setText(detailBean.getPayTypeName());
        tvTotalAmount.setText(detailBean.getTotalItemAmount());
        tvTotalNumber.setText(detailBean.getTotalNumber());
        tvRemark.setText(detailBean.getRemark());
        tvDiscountAmount.setText(detailBean.getDiscountAmount());
        tvTotalAmountNew.setText(detailBean.getTotalAmount());
        tvOrderType.setText(detailBean.getOrderModel());

        if (TextUtil.isEmpty(detailBean.getRefuseRemark())) {
            rlRefuseLayout.setVisibility(View.GONE);
        } else {
            rlRefuseLayout.setVisibility(View.VISIBLE);
            tvRefuse.setText(detailBean.getRefuseRemark());
        }

        if (TextUtil.isEmpty(detailBean.getCouponPrice())) {
            rlCouponLayout.setVisibility(View.GONE);
            viewCoupon.setVisibility(View.GONE);
        } else {
            rlCouponLayout.setVisibility(View.VISIBLE);
            viewCoupon.setVisibility(View.VISIBLE);
            tvCoupon.setText(detailBean.getCouponPrice());
        }

        /**
         * ll_OrderInfo----填充的内容
         */
        List<OrdersDetailBean.OrderInfoBean> orderInfo = detailBean.getOrderInfo();
        updateOrderInfo(orderInfo);

        /**
         * 底部按钮数据
         */
        List<OrdersDetailBean.OpBtnListBean> opBtnList = detailBean.getOpBtnList();
        updateOpBtnList(opBtnList);

        /**
         * childlistview---数据源
         */
        List<OrdersDetailBean.ItemListBean> itemList = detailBean.getItemList();

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
        List<OrdersDetailBean.ItemListGivBean> itemListGiv = detailBean.getItemListGiv();
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
     * 底部按钮数据
     */
    private void updateOpBtnList(List<OrdersDetailBean.OpBtnListBean> opBtnList) {

        /**
         * 方式二
         */
        if (null != opBtnList && opBtnList.size() > 0) {
            llButtonLayout.setVisibility(View.VISIBLE);
            switch (opBtnList.size()) {
                case 1:
                    btn_list_more = false;
                    tvBlack.setVisibility(View.GONE);
                    tvRed.setVisibility(View.VISIBLE);
                    tvRed.setText(opBtnList.get(0).getText());
                    String disabled = opBtnList.get(0).getDisabled();
                    //0代表按钮可用，1代表按钮禁用
                    if (disabled.equals("1")) {
                        tvRed.setEnabled(false);
                    } else {
                        tvRed.setEnabled(true);
                    }
                    break;

                case 2:
                    btn_list_more = true;
                    //0代表按钮可用，1代表按钮禁用
                    tvBlack.setVisibility(View.VISIBLE);
                    tvRed.setVisibility(View.VISIBLE);
                    tvRed.setText(opBtnList.get(1).getText());
                    if (opBtnList.get(1).getDisabled().equals("1")) {
                        tvRed.setEnabled(false);
                    } else {
                        tvRed.setEnabled(true);
                    }
                    tvBlack.setText(opBtnList.get(0).getText());
                    if (opBtnList.get(0).getDisabled().equals("1")) {
                        tvBlack.setEnabled(false);
                    } else {
                        tvBlack.setEnabled(true);
                    }
                    break;
            }
        } else {
            llButtonLayout.setVisibility(View.GONE);
        }
    }

    /**
     * ll_OrderInfo----填充的内容
     */
    private void updateOrderInfo(List<OrdersDetailBean.OrderInfoBean> shopInfo) {
        int size = shopInfo.size();
        llOrderInfo.removeAllViews();
        if (size > 0 && shopInfo != null) {
            for (int i = 0; i < size; i++) {
                View view = View.inflate(MyOrderDetailActivity.this, R.layout.inner_item_shop_detail, null);
                TextView tv_key = (TextView) view.findViewById(R.id.tv_key);
                tv_key.setSingleLine();
                tv_key.setTextSize(13);
                tv_key.setPadding(5, 5, 5, 5);
                tv_key.setTextColor(getResources().getColor(R.color.color_666666));
                tv_key.setText(shopInfo.get(i).getTitle() + shopInfo.get(i).getText());
                llOrderInfo.addView(view);
            }
        }
    }

    @OnClick({R.id.ll_head_back, R.id.tv_black, R.id.tv_red, R.id.ll_head_right2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                if (updated_btn) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    finish();
                }
                break;

            case R.id.tv_red:
                /**
                 * 方式二
                 */
                if (btn_list_more) {
                    Log.i("------->>", "红色点击--- 多个");
                    String text_red = ordersDetailBean.getOpBtnList().get(1).getText();
                    String value1_red = ordersDetailBean.getOpBtnList().get(1).getValue();
                    updateBtnClick(value1_red, text_red);
                } else {
                    Log.i("------->>", "红色点击--- 一个");
                    String text_red = ordersDetailBean.getOpBtnList().get(0).getText();
                    String value1_red = ordersDetailBean.getOpBtnList().get(0).getValue();
                    updateBtnClick(value1_red, text_red);
                }
                break;

            case R.id.tv_black:
                String text_black = ordersDetailBean.getOpBtnList().get(0).getText();
                String value_black = ordersDetailBean.getOpBtnList().get(0).getValue();
                updateBtnClick(value_black, text_black);
                break;

            case R.id.ll_head_right2:
                HttpChatData();
                break;

        }
    }

    /**
     * 获取聊天好友信息
     */
    private HomeSingleListBean homeSingleListBean;

    private void HttpChatData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);

        Log.i("======================", "接收---------------" + shopId);

        new GetNetUtil(hashMap, Api.Mall_GetUserListForChat, MyOrderDetailActivity.this) {
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
                Basebean<ChoosePersonTalkBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ChoosePersonTalkBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    ChoosePersonTalkBean choosePersonTalkBean = bean.getObj();
                    List<ChoosePersonTalkBean.UserListBean> userList = bean.getObj().getUserList();
                    if (userList != null) {
                        if (userList.size() == 1) {
                            homeSingleListBean = new HomeSingleListBean();
                            Intent intent = new Intent(MyOrderDetailActivity.this, ChatAcitivty.class);
                            homeSingleListBean.setUserId(userList.get(0).getUserId());
                            homeSingleListBean.setUserName(userList.get(0).getUserName());
                            homeSingleListBean.setImagesHead(userList.get(0).getImagesHead());
                            intent.putExtra("homeSingleListBean", gson.toJson(homeSingleListBean));
                            MyOrderDetailActivity.this.startActivity(intent);
                        } else {
                            Intent intent = new Intent(MyOrderDetailActivity.this, ChoosePersonTalkWithActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("choosePersonTalkBean", choosePersonTalkBean);
                            intent.putExtras(bundle);
                            MyOrderDetailActivity.this.startActivity(intent);
                        }
                    }
                } else {
                    ToastUtils.showToast(MyOrderDetailActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 处理点击事件
     *
     * @param value
     * @param dialogText
     */
    private void updateBtnClick(final String value, final String dialogText) {
        if (!TextUtil.isEmpty(value)) {
            switch (value) {
//                     *接单 = 1
//                     * 拒绝接单 = 2
//                     * 发货 = 3
//                     * 确认收货 = 4
//                     * 删除订单 = 5
//                     * 提醒商家发货 = 6
//                     * 再次购买 = 7
//                     * 买家取消订单=8
                case "1":
                    showCustomDialog(value, getResources().getString(R.string.order_type_1));
                    break;

                case "2":
                    show_refuse_dialog(value);


                    break;

                case "3":
                    showCustomDialog(value, getResources().getString(R.string.order_type_3));
                    break;

                case "4":
                    showCustomDialog(value, getResources().getString(R.string.order_type_4));
                    break;

                case "5":
                    showCustomDialog(value, getResources().getString(R.string.order_type_5));
                    break;

                case "6":
                    //提醒发货，直接请求数据
                    HttpUpdateOrder(value, "");
                    break;

                case "7":
                    // 对再次购买进行处理
                    HttpUpdateOrder(value, "");
                    break;

                case "8":
                    showCustomDialog(value, getResources().getString(R.string.order_type_8));
                    break;
            }
        }
    }


    private AlertDialog dialog_refuse = null;
    Boolean isShow = true;
    RelativeLayout rl_desc;
    TextView tv_refuse, tv_ok;
    LinearLayout ll_refuse_list;
    ImageView iv_image;
    String position_str;
    String itemcode = "";
    CharSequence text_refuse_id;
    HashMap<String, String> HashMap = new HashMap<>();
    List<MyOrderTitleBean.RefuseRmearkListBean> refuseRmearkList = new ArrayList<>();
    Button button;

    private void show_refuse_dialog(final String value) {
        dialog_refuse = new AlertDialog.Builder(MyOrderDetailActivity.this).create();
        dialog_refuse.show();
        dialog_refuse.getWindow().setContentView(R.layout.aldialog_refuse_orderlist);
        dialog_refuse.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog_refuse.getWindow().findViewById(R.id.ll_title_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_refuse.dismiss();
            }
        });
        rl_desc = (RelativeLayout) dialog_refuse.getWindow().findViewById(R.id.rl_desc);
        tv_refuse = (TextView) dialog_refuse.getWindow().findViewById(R.id.tv_refuse);
        tv_ok = (TextView) dialog_refuse.getWindow().findViewById(R.id.tv_ok);
        ll_refuse_list = (LinearLayout) dialog_refuse.getWindow().findViewById(R.id.ll_refuse_list);
        iv_image = (ImageView) dialog_refuse.getWindow().findViewById(R.id.iv_image);
        button = (Button) dialog_refuse.getWindow().findViewById(R.id.button);
        tv_refuse.setText(first_refuse_str);
//        button.setEnabled(false);
//        button.setBackground(getResources().getDrawable(R.drawable.shape_radius5_gray));

        updateDataView();
        rl_desc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isShow) {
                    ll_refuse_list.setVisibility(View.VISIBLE);
                    button.setVisibility(View.GONE);
                    iv_image.setBackgroundDrawable(MyOrderDetailActivity.this.getResources().getDrawable(R.drawable.reasons_for_refuse_up));
                    isShow = false;
                } else {
                    ll_refuse_list.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
                    iv_image.setBackgroundDrawable(MyOrderDetailActivity.this.getResources().getDrawable(R.drawable.reasons_for_refuse_dowm));
                    isShow = true;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = tv_refuse.getText().toString();
                if (TextUtil.isEmpty(itemcode)) {
                    ToastUtils.toast(MyOrderDetailActivity.this, "请选择拒绝接单的原因！");
                } else {
                    dialog_refuse.dismiss();
                    HttpUpdateOrder(value, string);
                }
            }
        });

        dialog_refuse.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isShow = true;
            }
        });
    }

    private void updateDataView() {
        ll_refuse_list.removeAllViews();
        for (int i = 0; i < refuseRmearkList.size(); i++) {
            HashMap.put(refuseRmearkList.get(i).getItemText(), refuseRmearkList.get(i).getItemCode() + "-" + i);

            View view = View.inflate(MyOrderDetailActivity.this, R.layout.inner_item_refuse, null);
            final TextView tv = (TextView) view.findViewById(R.id.tv_text);
//            final TextView tv = new TextView(context);
            tv.setPadding(dip2px(MyOrderDetailActivity.this, 10), dip2px(MyOrderDetailActivity.this, 3),
                    dip2px(MyOrderDetailActivity.this, 10), dip2px(MyOrderDetailActivity.this, 3));
            tv.setText(refuseRmearkList.get(i).getItemText());
            if (refuseRmearkList.get(i).isseleted()) {
                tv.setTextColor(Color.parseColor("#e65252"));
            } else {
                tv.setTextColor(Color.parseColor("#666666"));
            }
            ll_refuse_list.addView(view);
            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    iv_image.setBackgroundDrawable(MyOrderDetailActivity.this.getResources().getDrawable(R.drawable.reasons_for_refuse_dowm));
                    tv.setTextColor(Color.parseColor("#e65252"));
                    text_refuse_id = ((TextView) v).getText();
                    String hashmap_str = HashMap.get(text_refuse_id);
                    String[] split = hashmap_str.split("-");
                    itemcode = split[0];
                    position_str = split[1];
                    int position_int = Integer.parseInt(position_str);
                    for (int i = 0; i < refuseRmearkList.size(); i++) {
                        if (i == position_int) {
                            refuseRmearkList.get(i).setIsseleted(true);
                        } else {
                            refuseRmearkList.get(i).setIsseleted(false);
                        }
                    }
                    ll_refuse_list.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
                    isShow = true;
                    updateDataView();
                    tv_refuse.setText(text_refuse_id);
                    button.setEnabled(true);
                    button.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
                }
            });
        }
    }

    private void showCustomDialog(final String value, final String dialogText) {
        /**
         * 接单 = 1
         * 拒绝接单 = 2
         * 发货 = 3
         * 确认收货 = 4
         * 删除订单 = 5
         * 提醒商家发货 = 6
         * 再次购买 = 7
         * 买家取消订单=8
         */
        CustomDialog.Builder builder = new CustomDialog.Builder(MyOrderDetailActivity.this);
        builder.setMessage(dialogText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HttpUpdateOrder(value, "");
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 请求网络更新订单状态
     *
     * @param value
     */
    private void HttpUpdateOrder(final String value, final String refuse_reason) {
//                * 接单 = 1
//                * 拒绝接单 = 2
//                * 发货 = 3
//                * 确认收货 = 4
//                * 删除订单 = 5
//                * 提醒商家发货 = 6
//                * 再次购买 = 7
//                * 买家取消订单 = 8
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("OrderId", OrderId);
        if (value.equals("2")) {
            hashMap.put("OpRemark", refuse_reason);
            hashMap.put("OpRemarkId", itemcode);
        }
        hashMap.put("OpType", value);
        hashMap.put("IsReturnOrderInfo", "1");
        hashMap.put("FromPageType", "1");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        showProgress(getString(R.string.http_loading_data));
        new GetNetUtil(hashMap, Api.Mall_OpOrder, MyOrderDetailActivity.this) {

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
                Basebean<UpdateOrderBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<UpdateOrderBean>>() {
                }.getType());

                if (bean.getStatus().endsWith("200")) {
                    if (!TextUtil.isEmpty(value) && value.equals("1")) {
                        updated_btn = true;

                    } else if (!TextUtil.isEmpty(value) && value.equals("2")) {
                        updated_btn = true;

                    } else if (!TextUtil.isEmpty(value) && value.equals("3")) {
                        updated_btn = true;

                    } else if (!TextUtil.isEmpty(value) && value.equals("4")) {
                        updated_btn = true;

                    } else if (!TextUtil.isEmpty(value) && value.equals("5")) {
                        //删除订单
                        setResult(RESULT_OK);
                        finish();
                    } else if (!TextUtil.isEmpty(value) && value.equals("6")) {
                        updated_btn = true;
                        //如果是提醒发货看，操作成功时候进行弹窗成功的操作
                        showAlertDialog();

                    } else if (!TextUtil.isEmpty(value) && value.equals("7")) {
                        //再次购买添加购物车成功
                        startActivity(new Intent(MyOrderDetailActivity.this, ShopCarAcitivity.class));

                    } else if (!TextUtil.isEmpty(value) && value.equals("8")) {
                        updated_btn = true;

                    } else {

                    }
//                    /**
//                     * 获取订单操作后的数据，对订单详情界面进行操作
//                     */
//                    UpdateOrderBean updateOrderBean = bean.getObj();
//                    if (null == updateOrderBean) {
//                        return;
//                    }
//                    MyOrderContentBean orderInfo = updateOrderBean.getOrderInfo();
//                    if (null == orderInfo) {
//                        return;
//                    }
                    //局部刷新
//                    String orderStatusName = orderInfo.getOrderStatusName();
//                    List<MyOrderContentBean.OpBtnListBean> opBtnList = orderInfo.getOpBtnList();
//                    updateUpdateData(orderStatusName, opBtnList);

                    //全局刷新
                    HttpData();

                } else if (bean.getStatus().equals("45830")) {
                    HttpData();
                } else if (bean.getStatus().equals("45831")) {
                    HttpData();
                } else if (bean.getStatus().equals("45832")) {
                    HttpData();
                } else if (bean.getStatus().equals("45833")) {
                    HttpData();
                } else if (bean.getStatus().equals("45834")) {
                    HttpData();
                } else if (bean.getStatus().equals("45835")) {
                    HttpData();
                } else {
                    ToastUtils.showToast(MyOrderDetailActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取操作订单成功后的数据，进行详情界面数据的更新
     * 主要更新的事订单的状态和订单底部的订单操作按钮
     *
     * @param statusName
     * @param opBtnList
     */
    private void updateUpdateData(String statusName, List<MyOrderContentBean.OpBtnListBean> opBtnList) {
        tvOrderStatusName.setText(statusName);
        List<OrdersDetailBean.OpBtnListBean> btnDetailList = new ArrayList<>();
        if (opBtnList != null) {
            for (int i = 0; i < opBtnList.size(); i++) {
                OrdersDetailBean.OpBtnListBean detailBtnBean = new OrdersDetailBean.OpBtnListBean();
                String code = opBtnList.get(i).getCode();
                if (!TextUtil.isEmpty(code)) {
                    detailBtnBean.setCode(code);
                }
                String disabled = opBtnList.get(i).getDisabled();
                if (!TextUtil.isEmpty(disabled)) {
                    detailBtnBean.setDisabled(disabled);
                }
                String text = opBtnList.get(i).getText();
                if (!TextUtil.isEmpty(text)) {
                    detailBtnBean.setText(text);
                }
                String value = opBtnList.get(i).getValue();
                if (!TextUtil.isEmpty(value)) {
                    detailBtnBean.setValue(value);
                }
                btnDetailList.add(i, detailBtnBean);
            }
        }
        updateOpBtnList(btnDetailList);
    }

    /**
     * 展示已经提醒过发货弹窗--每天只能提醒一次
     */
    private void showAlertDialog() {
        mAlertDialog = new AlertDialog.Builder(MyOrderDetailActivity.this).create();
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(R.layout.dialog_callto_send_goods);
        mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
    }


    /**
     * GirdView 数据适配器---订单中商品列表
     */
    public class ListViewAdapter extends BaseAdapter {

        private List<OrdersDetailBean.ItemListBean> ddsuccess = new ArrayList<>();
        private boolean isshwo = false;
        private Context context;

        public ListViewAdapter(List<OrdersDetailBean.ItemListBean> list, boolean isshwo, Context context) {
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
                convertView = LayoutInflater.from(MyOrderDetailActivity.this).inflate(R.layout.order_details_item, null);
                holder.iv_ItemPic = (ImageView) convertView.findViewById(R.id.iv_ItemPic);
                holder.tv_ItemName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_price_and_num = (TextView) convertView.findViewById(R.id.tv_price_and_num);
                holder.tv_zong_price = (TextView) convertView.findViewById(R.id.tv_zong_price);
                holder.view1 = (View) convertView.findViewById(R.id.view1);
                holder.tv_old_price = (TextView) convertView.findViewById(R.id.tv_old_price);
                holder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);
                holder.ll_layout_man = (LinearLayout) convertView.findViewById(R.id.ll_layout_man);
                holder.ll_layout_discount = (LinearLayout) convertView.findViewById(R.id.ll_layout_discount);
                holder.tv_active_desc = (TextView) convertView.findViewById(R.id.tv_active_desc);
                holder.iv_active_icon = (ImageView) convertView.findViewById(R.id.iv_active_icon);
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
            private TextView tv_zong_price, tv_old_price, tv_discount, tv_active_desc;
            private TextView tv_price_and_num;
            private ImageView iv_ItemPic, iv_active_icon;
            private View view1;
            private LinearLayout ll_layout_man, ll_layout_discount;
        }
    }

    /**
     * GirdView 数据适配器---赠品
     */
    public class ListViewAdapter2 extends BaseAdapter {

        private List<OrdersDetailBean.ItemListGivBean> ddsuccess = new ArrayList<>();
        private boolean isshwo = false;
        private Context context;

        public ListViewAdapter2(List<OrdersDetailBean.ItemListGivBean> list, boolean isshwo, Context context) {
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
                convertView = LayoutInflater.from(MyOrderDetailActivity.this).inflate(R.layout.order_details_item_zeng, null);
                holder.iv_ItemPic = (ImageView) convertView.findViewById(R.id.iv_ItemPic);
                holder.tv_ItemName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_price_and_num = (TextView) convertView.findViewById(R.id.tv_price_and_num);
                holder.tv_zong_price = (TextView) convertView.findViewById(R.id.tv_zong_price);
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
            return convertView;
        }

        public class ViewHolder {
            private TextView tv_ItemName;
            private TextView tv_zong_price;
            private TextView tv_price_and_num;
            private ImageView iv_ItemPic;
            private View view1;
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
