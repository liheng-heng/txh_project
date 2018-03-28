package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.txh.im.activity.ActiveRuleActivity;
import com.txh.im.activity.GoodsDetailActivity;
import com.txh.im.android.AllConstant;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.AddGoodsToCartBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.SearchGoodsBean;
import com.txh.im.listener.AvNumListener;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.widget.AccountView;

import java.util.HashMap;
import java.util.List;

/**
 * 代下单右侧数据
 */
public class GoodsReplaceOrderRightAdapter extends RecyclerView.Adapter<GoodsReplaceOrderRightAdapter.MerchantHodler> {

    private Context context;
    private List<SearchGoodsBean.ItemListBean> list;
    private LayoutInflater inflater;
    int val_weight = UIUtils.dip2px(70);
    int val_high = UIUtils.dip2px(70);
    int val_active_weight = UIUtils.dip2px(15);
    int val_active_high = UIUtils.dip2px(15);
    private String CategoryId;
    private String IsReplaceOrder;//是否代下单列表（1表示代下单，其它为正常商品列表）

    public AccountView av_goods_num;
    public int position11;
    private String userId;
    private String shopId;
    private TextView tvGoodsNum;

    public GoodsReplaceOrderRightAdapter(Context context, List<SearchGoodsBean.ItemListBean> list, String userId) {
        this.context = context;
        this.list = list;
        this.userId = userId;
        inflater = LayoutInflater.from(context);
    }

    public GoodsReplaceOrderRightAdapter(Context context, List<SearchGoodsBean.ItemListBean> list,
                                         String shopId, String isPlace, TextView tvGoodsNum, String CategoryId) {
        this.context = context;
        this.list = list;
        this.shopId = shopId;
        this.IsReplaceOrder = isPlace;
        this.CategoryId = CategoryId;
        inflater = LayoutInflater.from(context);
        this.tvGoodsNum = tvGoodsNum;
    }

    @Override
    public MerchantHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View m_View = inflater.inflate(R.layout.item_goods_replace_order_right, parent, false);
        MerchantHodler hodler = new MerchantHodler(m_View);
        return hodler;
    }

    @Override
    public void onBindViewHolder(final MerchantHodler holder, final int position) {
        av_goods_num = holder.av_goods_num;
        position11 = position;
        if (!TextUtil.isEmpty(list.get(position).getItemPic())) {
            Picasso.with(context).load(list.get(position).getItemPic()).resize(val_weight, val_high).transform(
                    new RoundedTransformation(3, 0)).placeholder(R.drawable.default_good).into(holder.iv_goods_logo);
        } else {
            Picasso.with(context).load(R.drawable.default_good).resize(val_weight, val_high).transform(
                    new RoundedTransformation(3, 0)).into(holder.iv_goods_logo);
        }
        holder.tv_goods_name.setText(list.get(position).getItemName());
        holder.tv_standard_sale.setText(list.get(position).getSkuProp());
        if (list.get(position).getSPrice().startsWith("¥")) {
            holder.tv_price.setText(list.get(position).getSPrice());
        } else {
            holder.tv_price.setText(list.get(position).getSPriceSign() + list.get(position).getSPrice());
        }
        if (!TextUtil.isEmpty(list.get(position).getOldPrice())) {
            holder.tv_old_price.setText(list.get(position).getSPriceSign() + list.get(position).getOldPrice());
            holder.tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (!TextUtil.isEmpty(list.get(position).getDiscount())) {
            holder.tv_discount.setText(list.get(position).getDiscount());
            holder.tv_discount.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_full_orange));
        }
        if (CategoryId.equals("-1")) {
            holder.rl_promotion_layout.setVisibility(View.VISIBLE);
            holder.ll_oldprice_discont.setVisibility(View.VISIBLE);
            if (list.get(position).getActivityTypeId().equals("1")) {
                holder.ll_active_desc.setVisibility(View.VISIBLE);
                holder.ll_oldprice_discont.setVisibility(View.GONE);
            } else if (list.get(position).getActivityTypeId().equals("2")) {
                holder.ll_active_desc.setVisibility(View.GONE);
                holder.ll_oldprice_discont.setVisibility(View.VISIBLE);
            } else {
                holder.rl_promotion_layout.setVisibility(View.GONE);
                holder.ll_oldprice_discont.setVisibility(View.GONE);
            }
        } else {
            holder.rl_promotion_layout.setVisibility(View.GONE);
            holder.ll_oldprice_discont.setVisibility(View.GONE);
        }
        if (!TextUtil.isEmpty(list.get(position).getActivityTypeId()) &&
                list.get(position).getActivityTypeId().equals("1")) {
            holder.ll_active_desc.setVisibility(View.VISIBLE);
            if (!TextUtil.isEmpty(list.get(position).getActivityRemarkIcon())) {
                Picasso.with(context).load(list.get(position).getActivityRemarkIcon())
                        .resize(val_active_weight, val_active_high).transform(
                        new RoundedTransformation(3, 0)).into(holder.iv_active_icon);
            }
            holder.tv_active_desc.setText(list.get(position).getActivityRemark());
        } else {
            holder.ll_active_desc.setVisibility(View.GONE);
        }
        holder.rl_promotion_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActiveRuleActivity.class);
                intent.putExtra("web_str", list.get(position).getActivityContent());
                context.startActivity(intent);
            }
        });
        holder.av_goods_num.clearFocus();
        holder.av_goods_num.setAv_text(list.get(position).getShopNumber());
        holder.av_goods_num.setAvNumListener(new AvNumListener() {
            @Override
            public void num(String number) {
                list.get(position).setShopNumber(number);
                commitToService(list.get(position).getSkuId(),
                        list.get(position).getItemId(), number, list.get(position).getActivityId());
            }
        });
        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("SkuId", list.get(position).getSkuId());
                if (!TextUtil.isEmpty(IsReplaceOrder) && IsReplaceOrder.equals("0")) {
                    intent.putExtra("Shopid", shopId);
                } else {
                    return;
                }
                intent.putExtra("ActivityId", list.get(position).getActivityId());
                context.startActivity(intent);
            }
        });
    }

    /**
     * 添加到代下单
     *
     * @param skuId
     * @param ItemId
     * @param number
     * @param activityId
     */
    private void commitToService(String skuId, String ItemId, String number, String activityId) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("SkuId", skuId);
        hashMap.put("ItemId", ItemId);
        hashMap.put("ChangeType", "3");
        hashMap.put("ChangeNum", number);
        if (!TextUtil.isEmpty(activityId)) {
            hashMap.put("ActivityId", activityId);
        }
        if (!TextUtil.isEmpty(IsReplaceOrder) && IsReplaceOrder.equals("0")) {
            hashMap.put("ShopId", shopId);
            hashMap.put("IsReturnCartNum", "1");
            hashMap.put("IsReturnShelfNum", "1");
            hashMap.put("IsReplaceOrder", "0");
        } else {
            hashMap.put("ProUserId", userId);
            hashMap.put("IsReplaceOrder", AllConstant.IsReplaceOrder);
            hashMap.put("ShopId", GlobalApplication.getInstance().getPerson(context).getUnitId());
        }
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        hashMap.put("IsActionTest", AllConstant.IsActionTest);

        new GetNetUtil(hashMap, Api.Mall_ChangeShopCartItemNum, context) {

            @Override
            public void successHandle(String basebean) {
                Gson gson = new Gson();
                Basebean<AddGoodsToCartBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<AddGoodsToCartBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    if (tvGoodsNum == null) {
                        return;
                    }
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

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    class MerchantHodler extends RecyclerView.ViewHolder {

        private ImageView iv_goods_logo, iv_active_icon;
        private TextView tv_goods_name, tv_standard_sale, tv_price, tv_old_price, tv_discount, tv_active_desc;
        private AccountView av_goods_num;
        private RelativeLayout rl_promotion_layout, rl_layout;
        private LinearLayout ll_active_desc, ll_oldprice_discont;

        public MerchantHodler(View itemView) {
            super(itemView);
            tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tv_standard_sale = (TextView) itemView.findViewById(R.id.tv_standard_sale);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_old_price = (TextView) itemView.findViewById(R.id.tv_old_price);
            tv_discount = (TextView) itemView.findViewById(R.id.tv_discount);
            tv_active_desc = (TextView) itemView.findViewById(R.id.tv_active_desc);
            iv_goods_logo = (ImageView) itemView.findViewById(R.id.iv_goods_logo);
            iv_active_icon = (ImageView) itemView.findViewById(R.id.iv_active_icon);
            av_goods_num = (AccountView) itemView.findViewById(R.id.av_goods_num);
            rl_promotion_layout = (RelativeLayout) itemView.findViewById(R.id.rl_promotion_layout);
            rl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
            ll_active_desc = (LinearLayout) itemView.findViewById(R.id.ll_active_desc);
            ll_oldprice_discont = (LinearLayout) itemView.findViewById(R.id.ll_oldprice_discont);
        }
    }

    public void RereshData(List<SearchGoodsBean.ItemListBean> listData, String CategoryId) {
        this.list.clear();
        this.list.addAll(listData);
        this.CategoryId = CategoryId;
        notifyDataSetChanged();
    }

    public void loadData(List<SearchGoodsBean.ItemListBean> listData, String CategoryId) {
        this.list.addAll(listData);
        this.CategoryId = CategoryId;
        notifyDataSetChanged();
    }

    public void Clear() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public int getPosition11() {
        return position11;
    }

    public void setPosition11(int position11) {
        this.position11 = position11;
    }

}
