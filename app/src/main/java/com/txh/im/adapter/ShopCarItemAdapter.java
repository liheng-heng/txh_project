package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.GoodsDetailActivity;
import com.txh.im.android.AllConstant;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.GoodsBean;
import com.txh.im.bean.ShopBean;
import com.txh.im.bean.ShopCarBean;
import com.txh.im.bean.ShopListBean2;
import com.txh.im.bean.ShopNumBean;
import com.txh.im.listener.AvNumListener;
import com.txh.im.listener.FinishAcitivty;
import com.txh.im.listener.ShopCarMoneyOrNum;
import com.txh.im.utils.FormatDoubleUtil;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.AccountView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/4/7.
 */

public class ShopCarItemAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<ShopListBean2> list;
    private ShopCarBean shopCarBean;
    private ShopCarMoneyOrNum sh;
    private FinishAcitivty fc;

    public ShopCarItemAdapter(Context context, List<ShopListBean2> list, ShopCarBean shopCarBean, ShopCarMoneyOrNum sh, FinishAcitivty fc) {
        this.context = context;
        this.list = list;
        this.shopCarBean = shopCarBean;
        this.sh = sh;
        this.fc = fc;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getItemList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getItemList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        ShopListBean2 shopListBean = list.get(groupPosition);
        ShopBean shopBean = shopListBean.getShopInfo();
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shop_car_parent, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.initData(shopBean, shopCarBean);
        groupViewHolder.tvShopName.setText(shopBean.getShopName());
        if (!shopBean.getShopLogo().equals("")) {
            Glide.with(context).load(shopBean.getShopLogo()).into(groupViewHolder.ivShop);
        }
        if (shopBean.getIsSelected().equals("0")) {
            groupViewHolder.cbGroupCheck.setChecked(false);
        } else {
            groupViewHolder.cbGroupCheck.setChecked(true);
        }
        //去凑单是否显示
        List<GoodsBean> itemList = shopListBean.getItemList();
        double price = 0;
//        int num = 0;
        for (int i = 0; i < itemList.size(); i++) {
            GoodsBean goodsBean = itemList.get(i);
            if (goodsBean.getIsSelected() != null && goodsBean.getIsSelected().equals("1")) {
                price += Double.parseDouble(goodsBean.getSPrice()) * Double.parseDouble(goodsBean.getItemNumber());
            }
//            else {
//                num += 1;
//            }
        }
        double startPrice = Double.parseDouble(shopBean.getStartOrderAmount());
//        if (price >= startPrice || num == itemList.size()) {
        if (price >= startPrice) {
            groupViewHolder.tvJoinGoods.setVisibility(View.GONE);
            groupViewHolder.tvMinsendprice.setVisibility(View.GONE);
        } else {
            groupViewHolder.tvMinsendprice.setVisibility(View.VISIBLE);
            groupViewHolder.tvJoinGoods.setVisibility(View.VISIBLE);
        }
        groupViewHolder.tvJoinGoods.setText(shopCarBean.getNeedAddItemText());//去凑单
        groupViewHolder.tvMinsendprice.setText(shopBean.getStartOrderAmountText());

        groupViewHolder.llGroupTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGroupCheckedOrNo(groupPosition, groupViewHolder);
            }

        });
        groupViewHolder.cbGroupCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGroupCheckedOrNo(groupPosition, groupViewHolder);
            }
        });
        return convertView;
    }

    private void setGroupCheckedOrNo(int groupPosition, GroupViewHolder groupViewHolder) {
        ShopListBean2 shopListBean = list.get(groupPosition);
        ShopBean shopInfo = shopListBean.getShopInfo();
        if (shopInfo.getIsSelected().equals("0")) {
            shopInfo.setIsSelected("1");
        } else {
            shopInfo.setIsSelected("0");
        }
        List<GoodsBean> itemList = shopListBean.getItemList();
        for (int i = 0; i < itemList.size(); i++) {
            if (shopInfo.getIsSelected().equals("1")) {
                itemList.get(i).setIsSelected("1");
            } else {
                itemList.get(i).setIsSelected("0");
            }
        }
        sh.getAllMoneyAndNum();
        notifyDataSetChanged();
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        ShopListBean2 shopListBean2 = list.get(groupPosition);
        final ShopBean shopInfo = shopListBean2.getShopInfo();
        final GoodsBean goodsBean = shopListBean2.getItemList().get(childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shop_car_child, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.initGoodsData(goodsBean);
        if (childPosition == list.get(groupPosition).getItemList().size() - 1) {
            childViewHolder.vLine.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.vLine.setVisibility(View.GONE);
        }
        if (goodsBean.getIsSelected().equals("0")) {
            childViewHolder.shoppingCarInfoChoice.setChecked(false);
        } else {
            childViewHolder.shoppingCarInfoChoice.setChecked(true);
        }
        String mappingStatus = goodsBean.getMappingStatus();
        Integer integer = Integer.parseInt(mappingStatus);
        switch (integer) {
            case 1://正常
                childViewHolder.avGoodsNum.setVisibility(View.VISIBLE);
                childViewHolder.ivNoGoods.setVisibility(View.GONE);
                childViewHolder.tvUnderShelf.setVisibility(View.GONE);
                break;
            case 2://下架
                childViewHolder.avGoodsNum.setVisibility(View.GONE);
                childViewHolder.tvUnderShelf.setVisibility(View.GONE);
                childViewHolder.ivNoGoods.setVisibility(View.VISIBLE);
                childViewHolder.tvOldPrice.setVisibility(View.GONE);
                Glide.with(context).load(shopCarBean.getOffShelveImage()).into(childViewHolder.ivNoGoods);
                childViewHolder.tvUnderShelf.setText(shopCarBean.getOffShelveText());
                break;
            case 3://无货
                childViewHolder.avGoodsNum.setVisibility(View.GONE);
                childViewHolder.tvUnderShelf.setVisibility(View.GONE);
                childViewHolder.ivNoGoods.setVisibility(View.VISIBLE);
                Glide.with(context).load(shopCarBean.getOutOfStockImage()).into(childViewHolder.ivNoGoods);
                childViewHolder.tvOldPrice.setVisibility(View.GONE);
                childViewHolder.tvUnderShelf.setText(shopCarBean.getOutOfStockText());
                break;
        }
        childViewHolder.tvGoodsFormat.setText(goodsBean.getSkuProp());
        childViewHolder.avGoodsNum.clearFocus();
        childViewHolder.avGoodsNum.setAv_text(goodsBean.getItemNumber());
        childViewHolder.tvGoodsPrice.setText(goodsBean.getSPrice());
        //小计
        double v = Double.parseDouble(goodsBean.getItemNumber()) * Double.parseDouble(goodsBean.getSPrice());
        childViewHolder.tvGoodsTotalPrice.setText(FormatDoubleUtil.format(v));
        childViewHolder.avGoodsNum.setAvNumListener(new AvNumListener() {
            @Override
            public void num(String number) {
                goodsBean.setItemNumber(number);
                if (goodsBean.getIsSelected().equals("1")) {
                    sh.getAllMoneyAndNum();
                }
                commitToService(shopInfo.getShopId(), goodsBean.getCartId(), number, goodsBean);//购物车网络数量操作
                notifyDataSetChanged();
            }
        });
        String activityTypeId = goodsBean.getActivityTypeId();
        int parseInt = Integer.parseInt(activityTypeId);
        switch (parseInt) {//0是无促销,1是满赠,2是限时抢购
            case 0:
                childViewHolder.llOldPrice.setVisibility(View.GONE);
                childViewHolder.llRemark.setVisibility(View.GONE);
                childViewHolder.tvOldPrice.setVisibility(View.GONE);
                break;
            case 1:
                childViewHolder.llOldPrice.setVisibility(View.GONE);
                childViewHolder.llRemark.setVisibility(View.VISIBLE);
                childViewHolder.tvOldPrice.setVisibility(View.GONE);
                if (!goodsBean.getActivityRemarkIcon().equals("")) {
                    childViewHolder.ivRemark.setVisibility(View.VISIBLE);
                    Glide.with(context).load(goodsBean.getActivityRemarkIcon()).into(childViewHolder.ivRemark);
                } else {
                    childViewHolder.ivRemark.setVisibility(View.GONE);
                }
                childViewHolder.tvRemark.setText(goodsBean.getActivityRemark());
                break;
            case 2:
                childViewHolder.llOldPrice.setVisibility(View.VISIBLE);
                childViewHolder.llRemark.setVisibility(View.GONE);
                childViewHolder.tvOldPrice.setVisibility(View.VISIBLE);
                childViewHolder.tvOldPrice.setText("¥" + goodsBean.getOldPrice());
                childViewHolder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                childViewHolder.tvDiscount.setText(goodsBean.getDiscount());
                break;
        }
        childViewHolder.tvGoodsName.setText(goodsBean.getItemName());
        if (!goodsBean.getItemPic().equals("")) {
            Glide.with(context).load(goodsBean.getItemPic()).into(childViewHolder.ivGoods);
        }
        childViewHolder.shoppingCarCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChildCheckedOrNo(groupPosition, childPosition, childViewHolder);
            }
        });
        childViewHolder.shoppingCarInfoChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChildCheckedOrNo(groupPosition, childPosition, childViewHolder);
            }
        });
        childViewHolder.shoppCarDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, GoodsDetailActivity.class);
                it.putExtra("SkuId", goodsBean.getSkuId());
                it.putExtra("Shopid", shopInfo.getShopId());
                it.putExtra("formShopCar", "formShopCar");
                context.startActivity(it);
//                fc.finishAcitvity();
            }
        });
        childViewHolder.fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });
        return convertView;
    }

    private void commitToService(String shopId, String cardId, String number, GoodsBean goodsBean) {
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("ShopId", shopId);
//        hashMap.put("SkuId", skuId);
        hashMap.put("CartId", cardId);
        hashMap.put("ChangeType", "3");
        hashMap.put("ChangeNum", number);
        hashMap.put("IsReturnCartNum", "0");
        if (goodsBean.getActivityId() != null) {
            hashMap.put("ActivityId", goodsBean.getActivityId());
        }
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        hashMap.put("IsActionTest", AllConstant.IsActionTest);
        new GetNetUtil(hashMap, Api.Mall_ChangeShopCartItemNum, context) {
            @Override
            public void successHandle(String base) {
                Gson gson = new Gson();
                Basebean<ShopNumBean> basebean = gson.fromJson(base, new TypeToken<Basebean<ShopCarBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    Log.i("-------------->", base);
                } else {
                    ToastUtils.toast(context, basebean.getMsg());
                }
            }
        };
    }

    private void setChildCheckedOrNo(int groupPosition, int childPosition, ChildViewHolder childViewHolder) {
        ShopListBean2 shopListBean = list.get(groupPosition);
        List<GoodsBean> itemList = shopListBean.getItemList();
        GoodsBean goodsBean = itemList.get(childPosition);
        int num = 0;
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getIsSelected().equals("1")) {
                num += 1;
            }
        }
        if (itemList.size() - 1 == num) {
            if (goodsBean.getIsSelected().equals("1")) {
                goodsBean.setIsSelected("0");
                shopListBean.getShopInfo().setIsSelected("0");
            } else {
                goodsBean.setIsSelected("1");
                shopListBean.getShopInfo().setIsSelected("1");
            }
        } else if (itemList.size() == num) {
            goodsBean.setIsSelected("0");
            shopListBean.getShopInfo().setIsSelected("0");
        } else {
            if (goodsBean.getIsSelected().equals("1")) {
                goodsBean.setIsSelected("0");
            } else {
                goodsBean.setIsSelected("1");
            }
        }
        sh.getAllMoneyAndNum();
        notifyDataSetChanged();
    }

    public void refresh(List<ShopListBean2> list, ShopCarBean shopCarBean) {
        this.list = list;
        this.shopCarBean = shopCarBean;
        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class GroupViewHolder {
        @Bind(R.id.cb_group_check)
        CheckBox cbGroupCheck;
        @Bind(R.id.ll_group_top)
        LinearLayout llGroupTop;
        @Bind(R.id.iv_shop)
        ImageView ivShop;
        @Bind(R.id.tv_shop_name)
        TextView tvShopName;
        @Bind(R.id.tv_minsendprice)
        TextView tvMinsendprice;
        @Bind(R.id.tv_join_goods)
        TextView tvJoinGoods;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void initData(ShopBean shopBean, ShopCarBean shopCarBean) {
            if (shopBean.getShopName() == null) {
                shopBean.setShopName("");
            }
            if (shopBean.getShopLogo() == null) {
                shopBean.setShopName("");
            }
            if (shopBean.getIsSelected() == null) {
                shopBean.setIsSelected("0");//0是不选，1是选择
            }
            if (shopCarBean.getNeedAddItemText() == null) {
                shopCarBean.setNeedAddItemText("");
            }
        }
    }

    static class ChildViewHolder {
        @Bind(R.id.shopping_car_info_choice)
        CheckBox shoppingCarInfoChoice;
        @Bind(R.id.shopping_car_check)
        LinearLayout shoppingCarCheck;
        @Bind(R.id.iv_goods)
        ImageView ivGoods;
        @Bind(R.id.iv_no_goods)
        ImageView ivNoGoods;
        @Bind(R.id.tv_goods_name)
        TextView tvGoodsName;
        @Bind(R.id.tv_goods_format)
        TextView tvGoodsFormat;
        @Bind(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @Bind(R.id.tv_under_shelf)
        TextView tvUnderShelf;
        @Bind(R.id.av_goods_num)
        AccountView avGoodsNum;
        @Bind(R.id.shopp_car_detail)
        LinearLayout shoppCarDetail;
        @Bind(R.id.tv_goods_total_price)
        TextView tvGoodsTotalPrice;
        @Bind(R.id.v_line)
        View vLine;
        @Bind(R.id.tv_old_price)
        TextView tvOldPrice;
        @Bind(R.id.tv_discount)
        TextView tvDiscount;
        @Bind(R.id.ll_old_price)
        LinearLayout llOldPrice;
        @Bind(R.id.iv_remark)
        ImageView ivRemark;
        @Bind(R.id.tv_remark)
        TextView tvRemark;
        @Bind(R.id.ll_remark)
        LinearLayout llRemark;
        @Bind(R.id.fl)
        FrameLayout fl;
        //        @Bind(R.id.tv_click_car)
//        View tvClickCar;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void initGoodsData(GoodsBean goodsBean) {
            if (goodsBean.getItemName() == null) {
                goodsBean.setItemNumber("");
            }
            if (goodsBean.getItemName() == null) {
                goodsBean.setItemName("");
            }
            if (goodsBean.getItemPic() == null) {
                goodsBean.setItemPic("");
            }
            if (goodsBean.getIsSelected() == null) {
                goodsBean.setIsSelected("0");
            }
            if (goodsBean.getSkuProp() == null) {
                goodsBean.setSkuProp("");
            }
            if (goodsBean.getMappingStatus() == null) {
                goodsBean.setMappingStatus("1");
            }
            if (goodsBean.getActivityRemark() == null) {
                goodsBean.setActivityRemark("");
            }
            if (goodsBean.getActivityRemarkIcon() == null) {
                goodsBean.setActivityRemarkIcon("");
            }
            if (goodsBean.getOldPrice() == null) {
                goodsBean.setOldPrice("'");
            }
            if (goodsBean.getDiscount() == null) {
                goodsBean.setDiscount("");
            }
            if (goodsBean.getActivityTypeId() == null || goodsBean.getActivityTypeId().equals("")) {
                goodsBean.setActivityTypeId("0");
            }
        }
    }
}
