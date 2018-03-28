package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.GoodsDetailActivity;
import com.txh.im.activity.SearchGoodsActivity;
import com.txh.im.android.AllConstant;
import com.txh.im.bean.AddGoodsToCartBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.SearchGoodsBean;
import com.txh.im.listener.AvNumListener;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;

import java.util.HashMap;
import java.util.List;

public class SearchGoodsAdapter extends RecyclerView.Adapter<SearchGoodsAdapter.GoodsHodler> {

    private Context context;
    private List<SearchGoodsBean.ItemListBean> list;
    private LayoutInflater inflater;
    private int flag;
    int val = UIUtils.dip2px(60);
    private String shopId;
    private TextView tvGoodsNum;

    public SearchGoodsAdapter(Context context, List<SearchGoodsBean.ItemListBean> list, int flag, String shopId, TextView tvGoodsNum) {
        this.context = context;
        this.list = list;
        this.flag = flag;
        this.shopId = shopId;
        this.tvGoodsNum = tvGoodsNum;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public GoodsHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View m_View = inflater.inflate(R.layout.item_search_goods, parent, false);
        GoodsHodler hodler = new GoodsHodler(m_View);
        return hodler;
    }

    @Override
    public void onBindViewHolder(final GoodsHodler holder, final int position) {
        if (!TextUtil.isEmpty(list.get(position).getItemPic())) {
            Picasso.with(context).load(list.get(position).getItemPic()).resize(val, val).transform(
                    new RoundedTransformation(3, 0)).placeholder(R.drawable.default_good).into(holder.iv_goods_logo);
        } else {
            Picasso.with(context).load(R.drawable.default_good).resize(val, val).transform(
                    new RoundedTransformation(3, 0)).into(holder.iv_goods_logo);
        }
        holder.tv_goods_name.setText(list.get(position).getItemName());
        holder.tv_standard_sale.setText(list.get(position).getSkuProp());
        holder.tv_price.setText(list.get(position).getSPriceSign() + list.get(position).getSPrice());
        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("SkuId", list.get(position).getSkuId());
                intent.putExtra("Shopid", shopId);
                intent.putExtra("formShopCar", "fromCar");
                intent.putExtra("ActivityId", list.get(position).getActivityId());
                context.startActivity(intent);
            }
        });

        holder.av_goods_num.clearFocus();
        holder.av_goods_num.setAv_text(list.get(position).getShopNumber());
        holder.av_goods_num.setAvNumListener(new AvNumListener() {
            @Override
            public void num(String number) {
                list.get(position).setShopNumber(number);
                commitToService(shopId, list.get(position).getSkuId(), number,
                        list.get(position).getItemId(), list.get(position).getActivityId());//购物车网络数量操作
                UUUUUnotifyDataSetChanged();
            }
        });
    }

    private void UUUUUnotifyDataSetChanged() {
        boolean updateRightLists = ((SearchGoodsActivity) context).isUpdateRightLists();
        if (updateRightLists) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    class GoodsHodler extends RecyclerView.ViewHolder {

        private TextView tv_goods_name, tv_standard_sale, tv_price;
        private ImageView iv_goods_logo;
        private com.txh.im.widget.AccountView av_goods_num;
        private RelativeLayout rl_layout;

        public GoodsHodler(View itemView) {
            super(itemView);
            tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tv_standard_sale = (TextView) itemView.findViewById(R.id.tv_standard_sale);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            iv_goods_logo = (ImageView) itemView.findViewById(R.id.iv_goods_logo);
            av_goods_num = (com.txh.im.widget.AccountView) itemView.findViewById(R.id.av_goods_num);
            rl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
        }
    }

    private void commitToService(String shopId, String skuId, String number, String ItemId, String ActivityId) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);
        hashMap.put("SkuId", skuId);
        hashMap.put("ChangeType", "3");
        hashMap.put("ChangeNum", number);
        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        hashMap.put("ItemId", ItemId);
        if (!TextUtil.isEmpty(ActivityId)) {
            hashMap.put("ActivityId", ActivityId);
        }
        hashMap.put("IsReplaceOrder", "0");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);

        new GetNetUtil(hashMap, Api.Mall_ChangeShopCartItemNum, context) {

            @Override
            public void successHandle(String basebean) {
                Gson gson = new Gson();
                Basebean<AddGoodsToCartBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<AddGoodsToCartBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
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
                } else {
                    ToastUtils.showToast(context, bean.getMsg());
                }
            }
        };
    }

    public void RereshData(List<SearchGoodsBean.ItemListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void loadData(List<SearchGoodsBean.ItemListBean> list) {
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
