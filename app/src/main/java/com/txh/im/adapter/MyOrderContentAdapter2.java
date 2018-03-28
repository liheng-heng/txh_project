package com.txh.im.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.MyOrderDetailActivity;
import com.txh.im.activity.ShopCarAcitivity;
import com.txh.im.android.AllConstant;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.MyOrderContentBean;
import com.txh.im.bean.UpdateOrderBean;
import com.txh.im.customview.MyGridView;
import com.txh.im.utils.CustomDialog;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的订单内容条目
 */

public class MyOrderContentAdapter2 extends RecyclerView.Adapter<MyOrderContentAdapter2.Holder> {

    private List<MyOrderContentBean> lists = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private Activity activity;
    private int val = UIUtils.dip2px(70);
    private int val_15 = UIUtils.dip2px(15);
    private MyGridViewAdapter GridAdapter;
    private AlertDialog mAlertDialog = null;
    RelativeLayout rlNoOrder;
    private boolean content;
    private String typecode_send;

    public MyOrderContentAdapter2(final Context context, List<MyOrderContentBean> list, RelativeLayout rlNoOrder,
                                  boolean content, String typecode_send) {
        this.context = context;
        this.activity = (Activity) context;
        this.lists = list;
        this.rlNoOrder = rlNoOrder;
        this.content = content;
        inflater = LayoutInflater.from(context);
        this.typecode_send = typecode_send;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View m_View = inflater.inflate(R.layout.item_order_content, parent, false);
        Holder hodler = new Holder(m_View);
        return hodler;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        if (content && position == 0) {
            holder.view_top.setVisibility(View.VISIBLE);
        } else {
            holder.view_top.setVisibility(View.GONE);
        }

        /**
         * OrderId : 12345678901
         * UnitId : 23
         * UnitName : 张三批发部
         * TotalAmount : 合计：¥ 590.00
         * TotalNumber : 15件
         * OrderStatus : 1
         * OrderStatusName : 待接单
         * ItemList : [{"ShopId":"23","SkuId":"143687","ItemName":"顶真 复合果汁苹果汁 900ml*6瓶1","ItemPic":"http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg","SkuProp":"900ml*6瓶","SPrice":"¥55.50","ItemNumber":"15件"}]
         * opBtnList : [{"Code":"CancelOrder","Text":"取消订单"}]
         */

        /**
         * OrderStatus 订单状态
         */

        /**
         * 设置gridview属性
         */
        int length = 80;
        int size = lists.get(position).getItemList().size();
        if (size > 4) {
            size = 4;
        }
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(gridviewWidth, RelativeLayout.LayoutParams.FILL_PARENT);
        holder.my_grid_view.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        holder.my_grid_view.setColumnWidth(itemWidth); // 设置列表项宽
        holder.my_grid_view.setHorizontalSpacing(5); // 设置列表项水平间距
        holder.my_grid_view.setStretchMode(GridView.NO_STRETCH);
        holder.my_grid_view.setNumColumns(size); // 设置列数量=列表集合数
        holder.tv_shopname.setText(lists.get(position).getUnitName());
        if (!TextUtil.isEmpty(lists.get(position).getUnitLogo())) {
            Picasso.with(context).load(lists.get(position).getUnitLogo()).resize(val_15, val_15).transform(
                    new RoundedTransformation(0, 0)).placeholder(R.drawable.shopping_cart).into(holder.iv_image);
        } else {
            Picasso.with(context).load(R.drawable.shopping_cart).resize(val_15, val_15).transform(
                    new RoundedTransformation(0, 0)).into(holder.iv_image);
        }

        holder.tv_order_desc.setText(lists.get(position).getOrderStatusName());
        holder.tv_total_num.setText("共计" + lists.get(position).getTotalNumber());
        holder.tv_total_price.setText(lists.get(position).getTotalAmount());
        if (lists.get(position).getOrderStatus().endsWith("40")) {
            holder.iv_finish.setVisibility(View.VISIBLE);
        } else {
            holder.iv_finish.setVisibility(View.GONE);
        }

        /**
         * 获取订单详情
         */
        List<MyOrderContentBean.ItemListBean> itemList = lists.get(position).getItemList();
        if (itemList.size() == 1) {
            holder.rl_one_goods.setVisibility(View.VISIBLE);
            holder.rl_gridview.setVisibility(View.GONE);
            holder.tv_good_name.setText(itemList.get(0).getItemName());
            holder.tv_good_standard.setText(itemList.get(0).getSkuProp());
            holder.tv_good_price.setText(itemList.get(0).getSPrice());

            if (!TextUtil.isEmpty(itemList.get(0).getItemPic())) {
                Picasso.with(context).load(itemList.get(0).getItemPic()).resize(val, val).transform(
                        new RoundedTransformation(3, 0)).placeholder(R.drawable.default_good).into(holder.iv_good_image);
            } else {
                Picasso.with(context).load(R.drawable.default_good).resize(val, val).transform(
                        new RoundedTransformation(3, 0)).into(holder.iv_good_image);
            }

        } else {
            holder.rl_one_goods.setVisibility(View.GONE);
            holder.rl_gridview.setVisibility(View.VISIBLE);
            GridAdapter = new MyGridViewAdapter(context, itemList);
            holder.my_grid_view.setAdapter(GridAdapter);
        }

        /**
         * 跳转详情界面
         */
        holder.iv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpOrderDetail(lists.get(position).getOrderId());
            }
        });

        holder.my_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jumpOrderDetail(lists.get(position).getOrderId());
            }
        });

        holder.rl_good_desc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpOrderDetail(lists.get(position).getOrderId());
            }
        });

        holder.rl_one_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpOrderDetail(lists.get(position).getOrderId());
            }
        });

        /**
         * 底部按钮操作订单
         */
        final List<MyOrderContentBean.OpBtnListBean> opBtnList = lists.get(position).getOpBtnList();
        if (opBtnList != null) {
            switch (opBtnList.size()) {
                case 0:
                    holder.rl_button_layout.setVisibility(View.GONE);
                    break;

                case 1:
                    holder.btn_black.setVisibility(View.GONE);
                    holder.btn_red.setVisibility(View.VISIBLE);
                    MyOrderContentBean.OpBtnListBean opBtnListBean0 = opBtnList.get(0);
                    //0代表按钮可用，1代表按钮禁用
                    String disabled = opBtnList.get(0).getDisabled();
                    holder.btn_red.setText(opBtnListBean0.getText());
                    if (disabled.equals("1")) {
                        holder.btn_red.setEnabled(false);
                    } else {
                        holder.btn_red.setEnabled(true);
                    }
                    break;

                case 2:
                    holder.btn_red.setVisibility(View.VISIBLE);
                    holder.btn_red.setText(opBtnList.get(1).getText());
                    if (opBtnList.get(1).getDisabled().equals("1")) {
                        holder.btn_red.setEnabled(false);
                    } else {
                        holder.btn_red.setEnabled(true);
                    }

                    holder.btn_black.setVisibility(View.VISIBLE);
                    holder.btn_black.setText(opBtnList.get(0).getText());
                    if (opBtnList.get(0).getDisabled().equals("1")) {
                        holder.btn_black.setEnabled(false);
                    } else {
                        holder.btn_black.setEnabled(true);
                    }
                    break;
            }
        } else {
            holder.rl_button_layout.setVisibility(View.GONE);
        }

        holder.btn_black.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String orderId_black = lists.get(position).getOrderId();
                String value_black = lists.get(position).getOpBtnList().get(0).getValue();
                String text_black = lists.get(position).getOpBtnList().get(0).getText();
                updateBtnClick(position, orderId_black, value_black, text_black);
            }
        });

        holder.btn_red.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (opBtnList.size()) {
                    case 1:
                        String orderId = lists.get(position).getOrderId();
                        String value = lists.get(position).getOpBtnList().get(0).getValue();
                        String text = lists.get(position).getOpBtnList().get(0).getText();
                        updateBtnClick(position, orderId, value, text);
                        break;

                    case 2:
                        String orderId2 = lists.get(position).getOrderId();
                        String value2 = lists.get(position).getOpBtnList().get(1).getValue();
                        String text2 = lists.get(position).getOpBtnList().get(1).getText();
                        updateBtnClick(position, orderId2, value2, text2);
                        break;
                }
            }
        });
    }

    /**
     * 根据点击事件进行处理
     *
     * @param position
     * @param orderId
     * @param value
     */
    private void updateBtnClick(int position, String orderId, String value, String dialogText) {
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
        if (!TextUtil.isEmpty(value)) {
            switch (value) {
                case "1":
                    showCustomDialog(position, orderId, value, context.getResources().getString(R.string.order_type_1));
                    break;

                case "2":
                    // 拒绝接单 = 2
                    //拒绝接单需要填写拒绝理由---二期需要修改
//                    show_refuse_dialog(position, orderId, value, context.getResources().getString(R.string.order_type_2));
//---------------------------------------------------------------------------------------------------------------------------
                    showCustomDialog(position, orderId, value, context.getResources().getString(R.string.order_type_2));
                    break;

                case "3":
                    showCustomDialog(position, orderId, value, context.getResources().getString(R.string.order_type_3));
                    break;

                case "4":
                    showCustomDialog(position, orderId, value, context.getResources().getString(R.string.order_type_4));
                    break;

                case "5":
                    showCustomDialog(position, orderId, value, context.getResources().getString(R.string.order_type_5));
                    break;

                case "6":
                    //提醒发货，直接操作请求网络
                    HttpUpdateOrder(context, position, orderId, value, "");
                    break;

                case "7":
                    HttpUpdateOrder(context, position, orderId, value, "");
                    break;

                case "8":
                    showCustomDialog(position, orderId, value, context.getResources().getString(R.string.order_type_8));
                    break;
            }
        }
    }

    /**
     * 跳转订单详情界面
     *
     * @param OrderId
     */
    private void jumpOrderDetail(String OrderId) {
        Intent intent = new Intent(context, MyOrderDetailActivity.class);
        intent.putExtra("OrderId", OrderId);
        activity.startActivityForResult(intent, 100);
    }

    /**
     * 根据点击的订单状态进行弹窗展示---发起网络请求
     *
     * @param position
     * @param orderId
     * @param value
     * @param dialogText
     */
    private void showCustomDialog(final int position, final String orderId, final String value, String dialogText) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage(dialogText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HttpUpdateOrder(context, position, orderId, value, "");
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

    private AlertDialog mAlertDialog_refuse = null;

    private void show_refuse_dialog(final int position, final String orderId, final String value, String dialogText) {

        mAlertDialog_refuse = new AlertDialog.Builder(context).create();
        mAlertDialog_refuse.show();
        mAlertDialog_refuse.getWindow().setContentView(R.layout.aldialog_refuse_orderlist);
        mAlertDialog_refuse.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mAlertDialog_refuse.getWindow().findViewById(R.id.ll_title_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog_refuse.dismiss();
            }
        });


        mAlertDialog_refuse.getWindow().findViewById(R.id.ll_ok_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) mAlertDialog_refuse.getWindow().findViewById(R.id.edittext);

                if (TextUtil.isEmpty(editText.getText().toString().trim())) {
                    ToastUtils.toast(context, "请输入拒绝理由");
                } else {
                    HttpUpdateOrder(context, position, orderId, value, editText.getText().toString().trim());
                    mAlertDialog_refuse.dismiss();

                }
            }
        });

    }

    /**
     * 操作订单
     *
     * @param context
     * @param position
     * @param OrderId
     * @param OpType
     */
    private void HttpUpdateOrder(final Context context, final int position, String OrderId, final String OpType, final String refuse_reason) {

//        OrderId	        true	string	订单编号
//        OpType	        true	int	操作类型(
//                                      接单 = 1,拒绝接单 = 2,发货 = 3,确认收货 = 4,
//                                      删除订单 = 5,提醒商家发货 = 6,再次购买 = 7,买家取消订单=8)
//        OpRemark	        false	string	操作备注
//        IsReturnOrderInfo	false	int	是否返回订单信息（默认为否0，1返回）

//                     * 接单 = 1   ok
//                     * 拒绝接单 = 2       ok
//                     * 发货 = 3          ok
//                     * 确认收货 = 4
//                     * 删除订单 = 5       ok
//                     * 提醒商家发货 = 6   ok
//                     * 再次购买 = 7       ok
//                     * 买家取消订单=8     ok

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("OrderId", OrderId);
        hashMap.put("OpType", OpType);
        if (OpType.equals("2")) {
            hashMap.put("OpRemark", refuse_reason);
        }
        hashMap.put("IsReturnOrderInfo", "1");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        showProgress("正在加载");
        new GetNetUtil(hashMap, Api.Mall_OpOrder, context) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String basebean) {
                closeProgress();
                Gson gson = new Gson();
                Basebean<UpdateOrderBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<UpdateOrderBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {

                    if (typecode_send.equals("0")) {
                        /**
                         * 全部订单，或者是订单搜索界面
                         */
                        if (!TextUtil.isEmpty(OpType) && OpType.equals("6")) {

                            //提醒发货
                            UpdateOrderBean updateOrderBean = bean.getObj();
                            if (null == updateOrderBean) {
                                return;
                            }
                            MyOrderContentBean orderInfo = updateOrderBean.getOrderInfo();
                            if (null == orderInfo) {
                                return;
                            }
                            List<MyOrderContentBean.OpBtnListBean> opBtnList = orderInfo.getOpBtnList();
                            List<MyOrderContentBean.ItemListBean> itemList = orderInfo.getItemList();
                            String orderStatusName = orderInfo.getOrderStatusName();
                            String orderStatus = orderInfo.getOrderStatus();

                            if (null != opBtnList && opBtnList.size() > 0) {
                                lists.get(position).setOpBtnList(opBtnList);
                            }
                            if (null != itemList && itemList.size() > 0) {
                                lists.get(position).setItemList(itemList);
                            }
                            if (!TextUtil.isEmpty(orderStatus)) {
                                lists.get(position).setOrderStatus(orderStatus);
                            }
                            if (!TextUtil.isEmpty(orderStatusName)) {
                                lists.get(position).setOrderStatusName(orderStatusName);
                            }
                            notifyDataSetChanged();
                            //如果是提醒发货看，操作成功时候进行弹窗成功的操作
                            showAlertDialog();

                        } else if (!TextUtil.isEmpty(OpType) && OpType.equals("7")) {
                            //再次购买
                            context.startActivity(new Intent(context, ShopCarAcitivity.class));

                        } else if (!TextUtil.isEmpty(OpType) && OpType.equals("5")) {
                            //删除订单
                            lists.remove(position);
                            notifyDataSetChanged();
                            if (lists.size() == 0) {
                                rlNoOrder.setVisibility(View.VISIBLE);
                                return;
                            }
                        } else {
                            UpdateOrderBean updateOrderBean = bean.getObj();
                            if (null == updateOrderBean) {
                                return;
                            }
                            MyOrderContentBean orderInfo = updateOrderBean.getOrderInfo();
                            if (null == orderInfo) {
                                return;
                            }
                            List<MyOrderContentBean.OpBtnListBean> opBtnList = orderInfo.getOpBtnList();
                            List<MyOrderContentBean.ItemListBean> itemList = orderInfo.getItemList();
                            String orderStatusName = orderInfo.getOrderStatusName();
                            String orderStatus = orderInfo.getOrderStatus();

                            lists.get(position).setOpBtnList(opBtnList);

                            if (null != itemList && itemList.size() > 0) {
                                lists.get(position).setItemList(itemList);
                            }
                            if (!TextUtil.isEmpty(orderStatus)) {
                                lists.get(position).setOrderStatus(orderStatus);
                            }
                            if (!TextUtil.isEmpty(orderStatusName)) {
                                lists.get(position).setOrderStatusName(orderStatusName);
                            }
                            notifyDataSetChanged();
                        }

                    } else {
                        if (!TextUtil.isEmpty(OpType) && OpType.equals("6")) {

                            //提醒发货
                            UpdateOrderBean updateOrderBean = bean.getObj();
                            if (null == updateOrderBean) {
                                return;
                            }
                            MyOrderContentBean orderInfo = updateOrderBean.getOrderInfo();
                            if (null == orderInfo) {
                                return;
                            }
                            List<MyOrderContentBean.OpBtnListBean> opBtnList = orderInfo.getOpBtnList();
                            List<MyOrderContentBean.ItemListBean> itemList = orderInfo.getItemList();
                            String orderStatusName = orderInfo.getOrderStatusName();
                            String orderStatus = orderInfo.getOrderStatus();

                            if (null != opBtnList && opBtnList.size() > 0) {
                                lists.get(position).setOpBtnList(opBtnList);
                            }
                            if (null != itemList && itemList.size() > 0) {
                                lists.get(position).setItemList(itemList);
                            }
                            if (!TextUtil.isEmpty(orderStatus)) {
                                lists.get(position).setOrderStatus(orderStatus);
                            }
                            if (!TextUtil.isEmpty(orderStatusName)) {
                                lists.get(position).setOrderStatusName(orderStatusName);
                            }
                            notifyDataSetChanged();
                            //如果是提醒发货看，操作成功时候进行弹窗成功的操作
                            showAlertDialog();

                        } else if (!TextUtil.isEmpty(OpType) && OpType.equals("7")) {
                            //再次购买
                            context.startActivity(new Intent(context, ShopCarAcitivity.class));

                        } else {
                            lists.remove(position);
                            notifyDataSetChanged();
                            if (lists.size() == 0) {
                                rlNoOrder.setVisibility(View.VISIBLE);
                                return;
                            }
                        }
                    }

//                    45830	买家已取消
//                    45831	商家已拒单
//                    45832	商家已接单
//                    45833	商家已发货
//                    45834	订单已完成
//                    45835	商家未接单
                } else if (bean.getStatus().equals("45830")) {
                    UUUUpppppDataView(bean, position);
                } else if (bean.getStatus().equals("45831")) {
                    UUUUpppppDataView(bean, position);
                } else if (bean.getStatus().equals("45832")) {
                    UUUUpppppDataView(bean, position);
                } else if (bean.getStatus().equals("45833")) {
                    UUUUpppppDataView(bean, position);
                } else if (bean.getStatus().equals("45834")) {
                    UUUUpppppDataView(bean, position);
                } else if (bean.getStatus().equals("45835")) {
                    UUUUpppppDataView(bean, position);
                } else {
                    ToastUtils.showToast(context, bean.getMsg());
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return (lists != null && lists.size() > 0) ? lists.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {

        private TextView tv_shopname, tv_order_desc, tv_good_name, tv_good_standard,
                tv_good_price, tv_total_num, tv_total_price;
        private ImageView iv_good_image, iv_finish, iv_image;
        private Button btn_black, btn_red;
        private MyGridView my_grid_view;
        private RelativeLayout rl_good_desc_layout, rl_button_layout, rl_one_goods, rl_layout, rl_gridview;
        private View view_top;
        private LinearLayout ll_button_layout;

        public Holder(View itemView) {
            super(itemView);
            view_top = itemView.findViewById(R.id.view_top);
            tv_shopname = (TextView) itemView.findViewById(R.id.tv_shopname);
            tv_order_desc = (TextView) itemView.findViewById(R.id.tv_order_desc);
            tv_good_name = (TextView) itemView.findViewById(R.id.tv_good_name);
            tv_good_standard = (TextView) itemView.findViewById(R.id.tv_good_standard);
            tv_good_price = (TextView) itemView.findViewById(R.id.tv_good_price);
            tv_total_num = (TextView) itemView.findViewById(R.id.tv_total_num);
            tv_total_price = (TextView) itemView.findViewById(R.id.tv_total_price);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            iv_good_image = (ImageView) itemView.findViewById(R.id.iv_good_image);
            iv_finish = (ImageView) itemView.findViewById(R.id.iv_finish);
            btn_black = (Button) itemView.findViewById(R.id.btn_black);
            btn_red = (Button) itemView.findViewById(R.id.btn_red);
            my_grid_view = (MyGridView) itemView.findViewById(R.id.my_grid_view);
            rl_good_desc_layout = (RelativeLayout) itemView.findViewById(R.id.rl_good_desc_layout);
            rl_button_layout = (RelativeLayout) itemView.findViewById(R.id.rl_button_layout);
            rl_one_goods = (RelativeLayout) itemView.findViewById(R.id.rl_one_goods);
            rl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
            rl_gridview = (RelativeLayout) itemView.findViewById(R.id.rl_gridview);
            ll_button_layout = (LinearLayout) itemView.findViewById(R.id.ll_button_layout);

        }
    }

    private void UUUUpppppDataView(Basebean<UpdateOrderBean> bean, int position) {
        UpdateOrderBean updateOrderBean = bean.getObj();
        if (null == updateOrderBean) {
            return;
        }

        MyOrderContentBean orderInfo = updateOrderBean.getOrderInfo();
        if (null == orderInfo) {
            return;
        }

        List<MyOrderContentBean.OpBtnListBean> opBtnList = orderInfo.getOpBtnList();
        List<MyOrderContentBean.ItemListBean> itemList = orderInfo.getItemList();
        String orderStatusName = orderInfo.getOrderStatusName();
        String orderStatus = orderInfo.getOrderStatus();

        if (null != opBtnList && opBtnList.size() > 0) {
            lists.get(position).setOpBtnList(opBtnList);
        }

        if (null != itemList && itemList.size() > 0) {
            lists.get(position).setItemList(itemList);
        }

        if (!TextUtil.isEmpty(orderStatus)) {
            lists.get(position).setOrderStatus(orderStatus);
        }

        if (!TextUtil.isEmpty(orderStatusName)) {
            lists.get(position).setOrderStatusName(orderStatusName);
        }
        notifyDataSetChanged();
    }

    public void RereshData(List<MyOrderContentBean> list, String typecode_send) {
        lists.clear();
        this.lists = list;
        this.typecode_send = typecode_send;
        notifyDataSetChanged();
    }

    public void loadData(List<MyOrderContentBean> list, String typecode_send) {
        this.lists.addAll(list);
        this.typecode_send = typecode_send;
        notifyDataSetChanged();
    }

    public void Clear() {
        if (lists != null) {
            lists.clear();
            notifyDataSetChanged();
        }
    }

    private ProgressDialog mProgressDialog;

    private void showProgress(String showstr) {
        mProgressDialog = ProgressDialog.show(context, "", showstr);
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 展示已经提醒过发货弹窗--每天只能提醒一次
     */
    private void showAlertDialog() {
        mAlertDialog = new AlertDialog.Builder(context).create();
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(R.layout.dialog_callto_send_goods);
        mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
    }

}
