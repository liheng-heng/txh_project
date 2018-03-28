package com.txh.im.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
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
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingTrolleyAdapter extends RecyclerView.Adapter<ShoppingTrolleyAdapter.MerchantHodler> {

    private Context context;
    private List<ShopTrolleyBean.ShopListBean> list = new ArrayList<>();
    private LayoutInflater inflater;
    private int flag;
    private String shopid_str;
    innerGoodsAdapter adapter;
    List<ShopTrolleyBean.ShopListBean.ItemListBean> list_item = new ArrayList<>();
    int val = UIUtils.dip2px(55);
    private HomeSingleListBean homeSingleListBean;

    public ShoppingTrolleyAdapter(Context context, List<ShopTrolleyBean.ShopListBean> list, int flag) {
        this.context = context;
        this.list = list;
        this.flag = flag;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MerchantHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View m_View = inflater.inflate(R.layout.item_shop_trolley, parent, false);
        MerchantHodler hodler = new MerchantHodler(m_View);
        return hodler;
    }

    @Override
    public void onBindViewHolder(final MerchantHodler holder, final int position) {

        if (!TextUtil.isEmpty(list.get(position).getShopLogo())) {
            Picasso.with(context).load(list.get(position).getShopLogo()).resize(val, val).transform(
                    new RoundedTransformation(3, 0)).placeholder(R.drawable.default_good).into(holder.iv_headimage);
        } else {
            Picasso.with(context).load(R.drawable.default_good).resize(val, val).transform(
                    new RoundedTransformation(3, 0)).into(holder.iv_headimage);
        }

        holder.tv_merchant_name.setText(list.get(position).getNickName());
        holder.tv_shopname.setText(list.get(position).getShopName());
        holder.tv_phone.setText(list.get(position).getContactPhone());
        holder.tv_money.setText(list.get(position).getStartOrderAmount());

        holder.rl_item_mer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shopId = list.get(position).getShopId();
                String shopName = list.get(position).getShopName();

                Intent intent = new Intent(context, ShopClassifyActivity.class);
                intent.putExtra("shopId", shopId);
                intent.putExtra("shopName", shopName);
                ((Activity) context).startActivityForResult(intent, 10);
            }
        });

        holder.iv_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpData(list.get(position).getShopId());
            }
        });

        holder.ll_talk_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpData(list.get(position).getShopId());
            }
        });

        holder.tv_all_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shopId = list.get(position).getShopId();
                String shopName = list.get(position).getShopName();
                Intent intent = new Intent(context, ShopClassifyActivity.class);
                intent.putExtra("shopId", shopId);
                intent.putExtra("shopName", shopName);
                ((Activity) context).startActivityForResult(intent, 10);

            }
        });

        adapter = new innerGoodsAdapter(context, list_item, shopid_str, list.get(position).getNickName());
        holder.item_inner_recycleview.setAdapter(adapter);
        holder.item_inner_recycleview.setLayoutManager(new GridLayoutManager(context, 4));
        holder.item_inner_recycleview.setItemAnimator(new DefaultItemAnimator());

        List<ShopTrolleyBean.ShopListBean.ItemListBean> itemList = list.get(position).getItemList();

        if (null != list.get(position).getItemList()) {
            adapter.RereshData(list.get(position).getItemList(), list.get(position).getShopId());
        }
    }

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    class MerchantHodler extends RecyclerView.ViewHolder {

        private RelativeLayout rl_item_mer_layout;
        private TextView tv_merchant_name, tv_shopname, tv_phone, tv_money, tv_all_goods;
        private ImageView iv_headimage, iv_talk;
        private RecyclerView item_inner_recycleview;
        private LinearLayout ll_talk_layout;

        public MerchantHodler(View itemView) {
            super(itemView);
            rl_item_mer_layout = (RelativeLayout) itemView.findViewById(R.id.rl_item_mer_layout);
            tv_merchant_name = (TextView) itemView.findViewById(R.id.tv_merchant_name);
            tv_shopname = (TextView) itemView.findViewById(R.id.tv_shopname);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);
            tv_all_goods = (TextView) itemView.findViewById(R.id.tv_all_goods);
            iv_talk = (ImageView) itemView.findViewById(R.id.iv_talk);
            iv_headimage = (ImageView) itemView.findViewById(R.id.iv_good_image);
            item_inner_recycleview = (RecyclerView) itemView.findViewById(R.id.item_recycleview);
            ll_talk_layout = (LinearLayout) itemView.findViewById(R.id.ll_talk_layout);
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
