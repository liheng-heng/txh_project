package com.txh.im.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.ChatAcitivty;
import com.txh.im.activity.ChoosePersonTalkWithActivity;
import com.txh.im.activity.ShopClassifyActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.ChoosePersonTalkBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.ShopTrolleyBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.MerchantHodler> {

    private Context context;
    private List<ShopTrolleyBean.ShopListBean> list = new ArrayList<>();
    private HomeSingleListBean homeSingleListBean;

    public ShoppingAdapter(Context context, List<ShopTrolleyBean.ShopListBean> list, int flag) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MerchantHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View m_View = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
        MerchantHodler hodler = new MerchantHodler(m_View);
        return hodler;
    }

    @Override
    public void onBindViewHolder(final MerchantHodler holder, final int position) {
        ShopTrolleyBean.ShopListBean shopListBean = list.get(position);
        holder.initData(shopListBean, context);
        String startOrderAmount = shopListBean.getStartOrderAmount();
        holder.tvShopname.setText(shopListBean.getShopName());
        holder.tvPhone.setText(shopListBean.getContactPhone());
        holder.tvMoneyName.setText(startOrderAmount.substring(0, startOrderAmount.indexOf("：") + 1));
        holder.tvMoney.setText(startOrderAmount.substring(startOrderAmount.indexOf("：") + 1));
        if (!shopListBean.getShopLogo().equals("")) {
            Glide.with(context).load(shopListBean.getShopLogo()).into(holder.ivGoodImage);
        }
        //促销活动判断，jiajia,6.9
        if (shopListBean.getActItems() != null && shopListBean.getActItems().size() > 0) {
            holder.rlItemMerLayout.setBackground(context.getResources().getDrawable(R.drawable.shop_car_parent_bg));
            holder.llPromotion.setVisibility(View.VISIBLE);
            holder.llPromotion.removeAllViews();
            LinearLayout iv_send_goods = holder.llPromotion;
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < shopListBean.getActItems().size(); i++) {
                LinearLayout newSingleRL = new LinearLayout(context);
                newSingleRL.setLayoutParams(ll);
                newSingleRL.setGravity(Gravity.CENTER);
                newSingleRL.setOrientation(LinearLayout.HORIZONTAL);
                if (shopListBean.getActItems().get(i).getIcon() != null && !shopListBean.getActItems().get(i).getIcon().equals("")) {
                    ImageView iv = new ImageView(context);
                    Glide
                            .with(context) // 指定Context,同时接受Activity、Fragment
                            .load(shopListBean.getActItems().get(i).getIcon())// 指定图片的URL（图片网址）
                            .error(R.drawable.default_good)
                            .fitCenter()//指定图片缩放类型为fitCenter
                            .override(40, 40)//指定图片的尺寸
                            .centerCrop()// 指定图片缩放类型为centerCrop
                            .into(iv);//指定显示图片的ImageView
                    newSingleRL.addView(iv);
                }
                if (shopListBean.getActItems().get(i).getRemark() != null) {
                    TextView tv = new TextView(context);
                    tv.setSingleLine();
                    tv.setEllipsize(TextUtils.TruncateAt.END);
                    tv.setText(shopListBean.getActItems().get(i).getRemark());
                    ll.setMargins(3, 3, 0, 3);
                    tv.setTextSize(12);
                    tv.setLayoutParams(ll);
                    newSingleRL.addView(tv);
                }
                iv_send_goods.addView(newSingleRL);
            }
        } else {
            holder.llPromotion.setVisibility(View.GONE);
            holder.rlItemMerLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    class MerchantHodler extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_good_image)
        ImageView ivGoodImage;
        @Bind(R.id.tv_shopname)
        TextView tvShopname;
        @Bind(R.id.tv_phone)
        TextView tvPhone;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_money_name)
        TextView tvMoneyName;
        @Bind(R.id.iv_talk)
        ImageView ivTalk;
        @Bind(R.id.ll_talk_layout)
        LinearLayout llTalkLayout;
        @Bind(R.id.ll_item)
        LinearLayout llItem;
        @Bind(R.id.rl_item_mer_layout)
        RelativeLayout rlItemMerLayout;
        @Bind(R.id.ll_promotion)
        LinearLayout llPromotion;
        @Bind(R.id.id_vten)
        View idVten;
        private ShopTrolleyBean.ShopListBean shopListBean;
        private Context context;

        public MerchantHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ll_item, R.id.ll_talk_layout, R.id.iv_talk, R.id.ll_promotion})
        public void onClick(View v) {
            String shopId = shopListBean.getShopId();
            String shopName = shopListBean.getShopName();
            Intent intent;
            switch (v.getId()) {
                case R.id.ll_item:
                    intent = new Intent(context, ShopClassifyActivity.class);
                    intent.putExtra("shopId", shopId);
                    intent.putExtra("shopName", shopName);
                    ((Activity) context).startActivityForResult(intent, 10);
                    break;
                case R.id.ll_talk_layout:
                    HttpData(shopListBean.getShopId());
                    break;
                case R.id.iv_talk:
                    HttpData(shopListBean.getShopId());
                    break;
                case R.id.ll_promotion:
                    intent = new Intent(context, ShopClassifyActivity.class);
                    intent.putExtra("shopId", shopId);
                    intent.putExtra("shopName", shopName);
                    ((Activity) context).startActivityForResult(intent, 10);
                    break;
            }
        }

        public void initData(ShopTrolleyBean.ShopListBean shopListBean, Context context) {
            if (shopListBean.getShopName() == null) {
                shopListBean.setShopName("");
            }
            if (shopListBean.getNickName() == null) {
                shopListBean.setNickName("");
            }
            if (shopListBean.getContactPhone() == null) {
                shopListBean.setContactPhone("");
            }
            if (shopListBean.getStartOrderAmount() == null) {
                shopListBean.setStartOrderAmount("");
            }
            if (shopListBean.getShopLogo() == null) {
                shopListBean.setShopLogo("");
            }
            this.shopListBean = shopListBean;
            this.context = context;
        }
    }

    /**
     * 获取聊天好友信息
     */
    private void HttpData(String shopId) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);

        new GetNetUtil(hashMap, Api.Mall_GetUserListForChat, context) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (context == null) {
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
                            Intent intent = new Intent(context, ChatAcitivty.class);
                            homeSingleListBean.setUserId(userList.get(0).getUserId());
                            homeSingleListBean.setUserName(userList.get(0).getUserName());
                            homeSingleListBean.setImagesHead(userList.get(0).getImagesHead());
                            intent.putExtra("homeSingleListBean", gson.toJson(homeSingleListBean));
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, ChoosePersonTalkWithActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("choosePersonTalkBean", choosePersonTalkBean);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }
                } else {
                    ToastUtils.showToast(context, bean.getMsg());
                }
            }
        };
    }

    public void RereshData(List<ShopTrolleyBean.ShopListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void loadData(List<ShopTrolleyBean.ShopListBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void Clear() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

}
