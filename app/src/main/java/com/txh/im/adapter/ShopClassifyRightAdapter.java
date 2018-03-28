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
import com.txh.im.activity.ShopClassifyActivity2;
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
import com.txh.im.widget.AccountView;

import java.util.HashMap;
import java.util.List;

public class ShopClassifyRightAdapter extends RecyclerView.Adapter<ShopClassifyRightAdapter.MerchantHodler> {

    private Context context;
    private List<SearchGoodsBean.ItemListBean> list;
    private LayoutInflater inflater;
    private int flag;
    int val_weight = UIUtils.dip2px(70);
    int val_high = UIUtils.dip2px(70);
    private TextView tvGoodsNum;
    private String shopId;
    public com.txh.im.widget.AccountView av_goods_num;
    public int position11;

    public ShopClassifyRightAdapter(Context context, List<SearchGoodsBean.ItemListBean> list,
                                    int flag, String shopId, TextView tvGoodsNum) {
        this.context = context;
        this.list = list;
        this.flag = flag;
        this.shopId = shopId;
        this.tvGoodsNum = tvGoodsNum;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MerchantHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View m_View = inflater.inflate(R.layout.item_right_classfity, parent, false);
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
        holder.tv_standard_sale.setText("( " + list.get(position).getSkuProp() + ")");
        holder.tv_price.setText(list.get(position).getSPriceSign() + list.get(position).getSPrice());
        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("SkuId", list.get(position).getSkuId());
                intent.putExtra("Shopid", shopId);
                intent.putExtra("ActivityId",list.get(position).getActivityId());
                context.startActivity(intent);

            }
        });

        holder.av_goods_num.clearFocus();
        holder.av_goods_num.setAv_text(list.get(position).getShopNumber());
        holder.av_goods_num.setAvNumListener(new AvNumListener() {

            @Override
            public void num(String number) {
                list.get(position).setShopNumber(number);
                String itemId = list.get(position).getItemId();
                String activityId = list.get(position).getActivityId();
                commitToService(shopId, list.get(position).getSkuId(), itemId, activityId, number);
                UUUUUnotifyDataSetChanged();
            }
        });
    }

    private void UUUUUnotifyDataSetChanged() {
        try {
            boolean updateRightLists = ((ShopClassifyActivity2) context).isUpdateRightLists();//jiajia
            if (updateRightLists) {
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交订单
     *
     * @param shopId
     * @param skuId
     * @param ItemId
     * @param activityId
     * @param number
     */
    private void commitToService(String shopId, String skuId, String ItemId, String activityId, String number) {

//        CartId	        否	int	购物车ID（V2.0）
//        ShopId	        否	int	商户ID
//        SkuId	            否	int	商品SKUID
//        ItemId	        否	int	商品ID(V2.0)
//        ActivityId	    否	int	活动ID（当商品有促销活动时传此字段）
//        ChangeType	    否	int	修改类型（1增加，2减少，3直接调整的目标数量）
//        ChangeNum	        是	int	改变数量
//        IsReturnCartNum	否	int	是否返回购物车中商品数量（0不返回，1返回）
//        IsReturnShelfNum	否	int	是否返回货架中商品数量（0不返回，1返回）
//        ProUserId	        是	int	买家UserId(代下单的时候需要传入买家的UserId)
//        IsReplaceOrder	否	int	是否代下单列表（1表示代下单，其它为正常商品列表）(V2.0)
//        ActionVersion	    false	string	不传默认1.0，当前接口版本号（2.0）
//        IsActionTest	    false	string	是否测试：1为静态数据，不传或其他为动态数据

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", shopId);
        hashMap.put("SkuId", skuId);
        hashMap.put("ChangeType", "3");
        hashMap.put("ChangeNum", number);
        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        hashMap.put("IsReplaceOrder", "0");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        hashMap.put("ItemId", ItemId);
        if (!TextUtil.isEmpty(activityId)) {
            hashMap.put("ActivityId", activityId);
        }

        new GetNetUtil(hashMap, Api.Mall_ChangeShopCartItemNum, context) {

            @Override
            public void successHandle(String basebean) {
                Log.i("-------->", basebean);
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

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    class MerchantHodler extends RecyclerView.ViewHolder {

        private ImageView iv_goods_logo;
        private TextView tv_goods_name, tv_standard_sale, tv_price;
        private com.txh.im.widget.AccountView av_goods_num;
        private RelativeLayout rl_layout;

        public MerchantHodler(View itemView) {
            super(itemView);
            tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tv_standard_sale = (TextView) itemView.findViewById(R.id.tv_standard_sale);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            iv_goods_logo = (ImageView) itemView.findViewById(R.id.iv_goods_logo);
            av_goods_num = (com.txh.im.widget.AccountView) itemView.findViewById(R.id.av_goods_num);
            rl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
        }
    }

    public void RereshData(List<SearchGoodsBean.ItemListBean> list, TextView tvGoodsNum) {
        this.list = list;
        this.tvGoodsNum = tvGoodsNum;
        notifyDataSetChanged();
    }

    public void loadData(List<SearchGoodsBean.ItemListBean> list, TextView tvGoodsNum) {
        this.list.addAll(list);
        this.tvGoodsNum = tvGoodsNum;
        notifyDataSetChanged();
    }

    public int getPosition11() {
        return position11;
    }

    public void setPosition11(int position11) {
        this.position11 = position11;
    }

    public AccountView getAv_goods_num() {
        return av_goods_num;
    }

    public void setAv_goods_num(AccountView av_goods_num) {
        this.av_goods_num = av_goods_num;
    }
}
