package com.txh.im.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.GoodsPlaceOrderAcitivty;
import com.txh.im.activity.OrderCommitActivity;
import com.txh.im.adapter.GoodsPlaceCarItemAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.GoodsBean;
import com.txh.im.bean.ShopBean;
import com.txh.im.bean.ShopCarBean;
import com.txh.im.bean.ShopCarNotSetissfyBean;
import com.txh.im.bean.ShopListBean2;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.listener.RefreshUi;
import com.txh.im.listener.ShopCarMoneyOrNum;
import com.txh.im.utils.CustomDialog;
import com.txh.im.utils.FormatDoubleUtil;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.DeliverDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/6/1.
 * 商品代下单点击代下单的fragment
 */

public class ShopCarFragment extends BasicFragment implements ShopCarMoneyOrNum, RefreshUi {
    @Bind(R.id.elv_shop_car)
    ExpandableListView elvShopCar;
    @Bind(R.id.tv_all_price)
    TextView tvAllPrice;
    @Bind(R.id.tv_all_num)
    TextView tvAllNum;
    @Bind(R.id.cb_all_checked)
    CheckBox cbAllChecked;
    @Bind(R.id.car_buttom)
    LinearLayout carButtom;
    @Bind(R.id.ll_no_goods)
    LinearLayout llNoGoods;
    @Bind(R.id.ll_bottom_shopcar)
    LinearLayout llBottomShopcar;
    @Bind(R.id.tv_no_goods)
    TextView tvNoGoods;
    private GoodsPlaceCarItemAdapter shopCarItemAdapter;
    private List<ShopListBean2> shopList;
    private Gson gson;
    private String ShopClassifyActivity;
    private Context context;
    private GoodsPlaceOrderAcitivty activity;
    private String userId;

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_shop_car, null);
        return view;
    }

    @Override
    protected void initData() {
        tvNoGoods.setText("请选择商品!");
        gson = new Gson();
        context = getContext();
        activity = (GoodsPlaceOrderAcitivty) getActivity();
        userId = activity.getUserId();//代理用户ID(V2.0)
        getNetRequest();
        elvShopCar.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                activity.selectIndx(0);
                activity.setTabSelection(0);
                return true;
            }
        });
        initBottom();
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    public void initBottom() {
        cbAllChecked.setChecked(false);
        tvAllPrice.setText("0.00");
        llBottomShopcar.setVisibility(View.VISIBLE);
        tvAllNum.setText("去结算(0)");
        carButtom.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.ll_allcheck_car, R.id.tv_all_num,
            R.id.cb_all_checked})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_allcheck_car:
                checked();
                getAllMoneyAndNum();//全选或者全部不选获取总数量和总价格
                shopCarItemAdapter.notifyDataSetChanged();
                break;

            case R.id.cb_all_checked:
                cbAllCheckedJudge(cbAllChecked.isChecked());
                getAllMoneyAndNum();
                shopCarItemAdapter.notifyDataSetChanged();
                break;

            //判断是否是删除
            case R.id.tv_all_num:
                String s = tvAllNum.getText().toString();
                //删除操作
                if (s.equals("删除")) {
                    //弹出个询问的对话框
                    StringBuffer stringBuffer = chooseOrNo();
                    if (stringBuffer.length() == 0) {
                        ToastUtils.toast(context, "请先选择商品!");
                        return;
                    }
                    getdeletecartDialog();
                }
                //去结算操作
                else {
                    toBalance();
                }
                break;
        }
    }

    //全选按钮
    private void checked() {
        if (cbAllChecked.isChecked()) {
            cbAllChecked.setChecked(false);
        } else {
            cbAllChecked.setChecked(true);
        }
        cbAllCheckedJudge(cbAllChecked.isChecked());
    }

    //商品状态（1正常，2下架，3无货)
    private void cbAllCheckedJudge(boolean checked) {
        String s = tvAllNum.getText().toString();
        for (int group = 0; group < shopList.size(); group++) {
            ShopListBean2 shopListBean2 = shopList.get(group);
            ShopBean shopInfo = shopListBean2.getShopInfo();
            if (checked) {
                shopInfo.setIsSelected("1");
            } else {
                shopInfo.setIsSelected("0");
            }
            List<GoodsBean> itemList = shopListBean2.getItemList();
            if (s.equals("删除")) {
                for (int child = 0; child < itemList.size(); child++) {
                    GoodsBean goodsBean = itemList.get(child);
                    if (checked) {
                        goodsBean.setIsSelected("1");
                    } else {
                        goodsBean.setIsSelected("0");
                        if (itemList.size() == 1) {
                            shopInfo.setIsSelected("0");
                        }
                    }
                }
            } else {
                for (int child = 0; child < itemList.size(); child++) {
                    GoodsBean goodsBean = itemList.get(child);
                    if (checked && goodsBean.getMappingStatus().equals("1")) {
                        goodsBean.setIsSelected("1");
                    } else {
                        goodsBean.setIsSelected("0");
                        if (itemList.size() == 1) {
                            shopInfo.setIsSelected("0");
                        }
                    }
                }
            }
        }
    }

    //商品总价和总数
    @Override
    public void getAllMoneyAndNum() {
        String s = tvAllNum.getText().toString();
        int goodsnum = 0;
        int shopnum = 0;
        double allPrice = 0;
        for (int group = 0; group < shopList.size(); group++) {
            ShopListBean2 shopListBean2 = shopList.get(group);
            ShopBean shopInfo = shopListBean2.getShopInfo();
            List<GoodsBean> itemList = shopListBean2.getItemList();
            if (shopInfo.getIsSelected().equals("1")) {
                shopnum = shopnum + 1;
            }
            for (int child = 0; child < itemList.size(); child++) {
                GoodsBean goodsBean = itemList.get(child);
                if (!s.equals("删除") && !goodsBean.getMappingStatus().equals("1")) {
                    goodsBean.setIsSelected("0");
                }
                if (goodsBean.getIsSelected().equals("1") && goodsBean.getMappingStatus().equals("1")) {
//                if (goodsBean.getIsSelected().equals("1")) {
                    goodsnum += Integer.parseInt(goodsBean.getItemNumber());
                    allPrice += Double.parseDouble(goodsBean.getItemNumber()) * Double.parseDouble(goodsBean.getSPrice());
                }
            }
        }
        if (shopnum == shopList.size()) {
            cbAllChecked.setChecked(true);
        } else {
            cbAllChecked.setChecked(false);
        }
        if (!s.equals("删除")) {
            tvAllNum.setText("去结算(" + goodsnum + ")");
            //tvAllNum.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
        }
//        if (s.equals("删除")) {
//            if (goodsnum > 0) {
//                tvAllNum.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
//            } else {
//                tvAllNum.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
//                return;
//            }
//        }
        tvAllPrice.setText(FormatDoubleUtil.format(allPrice) + "");
    }

    //请求购物车数据
    public void getNetRequest() {
        HashMap<String, String> map = new HashMap<>();
        map.put("IsReplaceOrder", "1");
        map.put("proUserId", userId);
        map.put("ActionVersion", AllConstant.ActionVersion);
        map.put("IsActionTest", AllConstant.IsActionTest);
        new GetNetUtil(map, Api.TX_App_User_GetShopCartData, context) {
            @Override
            public void successHandle(String base) {
                Log.i("----->>", "购物车数据-----" + base);
                if (ShopCarFragment.this.isDestroy) {
                    return;
                }
                Basebean<ShopCarBean> basebean = gson.fromJson(base, new TypeToken<Basebean<ShopCarBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    ShopCarBean shopCarBean = basebean.getObj();
                    if (shopCarBean == null || shopCarBean.getShopList() == null || shopCarBean.getShopList().size() == 0) {
                        llNoGoods.setVisibility(View.VISIBLE);
                        activity.getLlHeadRight().setVisibility(View.GONE);
//                            llHeadRight.setVisibility(View.GONE);
                    } else {
//                            llHeadRight.setVisibility(View.VISIBLE);
                        llNoGoods.setVisibility(View.GONE);
                        activity.getLlHeadRight().setVisibility(View.VISIBLE);
                        if (shopList != null && shopList.size() > 0) {
                            shopList.clear();
                            shopList.addAll(shopCarBean.getShopList());
                            shopCarItemAdapter.refresh(shopList, shopCarBean);
                            return;
                        }
                        shopList = shopCarBean.getShopList();
                        shopCarItemAdapter = new GoodsPlaceCarItemAdapter(context, shopList, shopCarBean, ShopCarFragment.this, userId);
                        elvShopCar.setAdapter(shopCarItemAdapter);
                        for (int i = 0; i < shopList.size(); i++) {
                            elvShopCar.expandGroup(i);
                        }
                    }
                } else {
                    ToastUtils.toast(context, basebean.getMsg());
                }
            }
        };
    }

    //去结算
    private void toBalance() {

//        ShopItems	        true	string	要去结算的商户商品shop、skuid及数量的集合（使用下划线“_”连接商户id与SkuId，
//                                          后面用"~"连接数量，使用英文逗号将多个商品隔开；“ShopId_SkuId~Number,ShopId_SkuId~Numbe,ShopId_SkuId~Numbe”，
//                                          如“1203_23434324~15,1243_343243244~13,1345_33432324~3”；
//                                          其中1203、1243、1345为商户ID，23434324、343243244、33432324为商品skuid，15、13、3为对应商品的数量）
//        TestParameter	    false	string	测试参数（如果这个值传入1时，返回数据中地址信息为空；
//                                         （传入2或3时提示要去凑单）传入TestParameter=2时返回2条去凑单商户列表信息，传入TestParameter=3时返回8条去凑单商户列表信息）
//        CartIds	        false	string	购物车主键ID集合，格式为："1,2,3,4"
//        ActionVersion	    false	string	不传默认1.0，当前接口版本号（2.0）
//        IsActionTest	    false	string	是否测试：1为静态数据，不传或其他为动态数据


//        BUserId	false	int	购买人UserId
//        ShopItems	true	string	要去结算的商户商品shop、skuid及数量的集合（使用下划线“_”连接商户id与SkuId，后面用"~"连接数量，使用英文逗号将多个商品隔开；“ShopId_SkuId~Number,ShopId_SkuId~Numbe,ShopId_SkuId~Numbe”，如“1203_23434324~15,1243_343243244~13,1345_33432324~3”；其中1203、1243、1345为商户ID，23434324、343243244、33432324为商品skuid，15、13、3为对应商品的数量）
//        TestParameter	false	string	测试参数（如果这个值传入1时，返回数据中地址信息为空；（传入2或3时提示要去凑单）传入TestParameter=2时返回2条去凑单商户列表信息，传入TestParameter=3时返回8条去凑单商户列表信息）
//        CartIds	false	string	购物车主键ID集合，格式为："1,2,3,4"
//        ActionVersion	false	string	不传默认1.0，当前接口版本号（2.0）
//        IsActionTest	false	string	是否测试：1为静态数据，不传或其他为动态数据

        final StringBuffer sb = chooseOrNo();
        if (sb.length() == 0) {
            ToastUtils.toast(context, "请先选择商品!");
            return;
        }
        sb.deleteCharAt(sb.length() - 1);
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("CartIds", sb.toString());
        hashmap.put("ActionVersion", AllConstant.ActionVersion);
        hashmap.put("BUserId", userId);
        hashmap.put("IsActionTest", AllConstant.IsActionTest);

        new GetNetUtil(hashmap, Api.TX_App_User_GetCartPreSubmitData, context) {

            @Override
            public void successHandle(String base) {
                Basebean<ShopCarNotSetissfyBean> basebean = gson.fromJson(base, new TypeToken<Basebean<ShopCarNotSetissfyBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    //去结算，需要bean，并且重新解析base,ShopCarSetissfyBean里面还没有字段
                    Intent intent = new Intent(context, OrderCommitActivity.class);
                    String sb_str = sb.toString();
                    intent.putExtra("sb_str", sb_str);
                    intent.putExtra("OrdersCommitBean", base);
                    intent.putExtra("userId", userId);
                    intent.putExtra("CartIds", sb.toString());
                    intent.putExtra("type", "2");
                    startActivity(intent);
//                    setResult(RESULT_OK);
                } else if (basebean.getStatus().equals("45825")) {
                    ShopCarNotSetissfyBean obj = basebean.getObj();
                    //弹出的dialog
                    new DeliverDialog(mContext, obj, ShopCarFragment.this).show();

                } else {
                    ToastUtils.toast(mContext, basebean.getMsg());
                }
            }
        };
    }

    //删除或者去结算的时候判断是否选择了商品
    private StringBuffer chooseOrNo() {
        final StringBuffer sb = new StringBuffer();
        for (int group = 0; group < shopList.size(); group++) {
            ShopListBean2 shopListBean2 = shopList.get(group);
            ShopBean shopInfo = shopListBean2.getShopInfo();
            List<GoodsBean> itemList = shopListBean2.getItemList();
            for (int child = 0; child < itemList.size(); child++) {
                GoodsBean goodsBean = itemList.get(child);
                if (goodsBean.getIsSelected().equals("1")) {
                    sb.append(goodsBean.getCartId())
                            .append(",");
                }
            }
        }
        return sb;
    }

    //删除
    private void toDeleteGoods() {
        final StringBuffer sb = chooseOrNo();
        sb.deleteCharAt(sb.length() - 1);
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("CartIds", sb.toString());
        hashmap.put("ActionVersion", AllConstant.ActionVersion);
        hashmap.put("IsActionTest", AllConstant.IsActionTest);
        new GetNetUtil(hashmap, Api.TX_App_User_RemoveItemsFromCart, context) {
            @Override
            public void successHandle(String base) {
                Basebean<ShopCarNotSetissfyBean> basebean = gson.fromJson(base, new TypeToken<Basebean<ShopCarNotSetissfyBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    getNetRequest();
                } else {
                    ToastUtils.toast(context, basebean.getMsg());
                }
            }

        };
    }

    public void getdeletecartDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage("是否删除该商品？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 请求服务器（删除数据）
                //如果确定执行toDeleteGoods()
                toDeleteGoods();
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

    //编辑状态被点击
    public void editShopCar() {
        if (shopList == null || shopList.size() == 0) {
            return;
        }
        carButtom.setVisibility(View.INVISIBLE);
        tvAllNum.setText("删除");
        cbAllCheckedJudge(false);
        getAllMoneyAndNum();
        shopCarItemAdapter.notifyDataSetChanged();
    }

    //完成状态被点击
    public void finishShopCar() {
        if (shopList == null || shopList.size() == 0) {
            return;
        }
        carButtom.setVisibility(View.VISIBLE);
        tvAllNum.setText("去结算(0)");
        cbAllCheckedJudge(false);
        getAllMoneyAndNum();
        shopCarItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void botifyUi() {
        activity.selectIndx(0);
        activity.setTabSelection(0);
    }

    @Subscribe
    public void onEventMainThread(FirstEventBus event) {
        if (event != null && event.getMsg().equals("GoodsPlace")) {
            getNetRequest();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
}
