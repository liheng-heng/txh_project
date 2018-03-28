package com.txh.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.activity.SearchGoodsActivity;
import com.txh.im.bean.SearchGoodsBean;
import com.txh.im.utils.UIUtils;

import java.util.List;

public class SearchActiveGoodsAdapter extends RecyclerView.Adapter<SearchActiveGoodsAdapter.GoodsHodler> {

    private Context context;
    private List<SearchGoodsBean.ItemListBean> list;
    private LayoutInflater inflater;
    int val = UIUtils.dip2px(60);

    public SearchActiveGoodsAdapter(Context context, List<SearchGoodsBean.ItemListBean> list) {
        this.context = context;
        this.list = list;
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

//                "ItemId":"10",
//                "SkuId":"1",
//                "ItemBrandName":"娃哈哈",
//                "ItemCategoryName":"乳品",
//                "ItemName":"娃哈哈 小AD钙奶儿童营养奶乳饮品 整箱100ml*24瓶",
//                "SkuProp":"100ml*24瓶",
//                "ItemPic":"http://img.tianxiahuo.cn/goods/20160714/7ccdd4c80ca0236a9a8ff77c3d94861e.jpg",
//                "SPrice":"36.55",
//                "SPriceSign":"¥",
//                "ShopNumber":"0"

//        if (!TextUtil.isEmpty(list.get(position).getItemPic())) {
//            Picasso.with(context).load(list.get(position).getItemPic()).resize(val, val).transform(
//                    new RoundedTransformation(3, 0)).placeholder(R.drawable.default_good).into(holder.iv_goods_logo);
//        } else {
//            Picasso.with(context).load(R.drawable.default_good).resize(val, val).transform(
//                    new RoundedTransformation(3, 0)).into(holder.iv_goods_logo);
//        }
//
//        holder.tv_goods_name.setText(list.get(position).getItemName());
//        holder.tv_standard_sale.setText(list.get(position).getSkuProp());
//        holder.tv_price.setText(list.get(position).getSPriceSign() + list.get(position).getSPrice());
//        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, GoodsDetailActivity.class);
//                intent.putExtra("SkuId", list.get(position).getSkuId());
//                intent.putExtra("Shopid", shopId);
//                context.startActivity(intent);
//
//            }
//        });
//
//        holder.av_goods_num.clearFocus();
//        holder.av_goods_num.setAv_text(list.get(position).getShopNumber());
//        holder.av_goods_num.setAvNumListener(new AvNumListener() {
//            @Override
//            public void num(String number) {
//                list.get(position).setShopNumber(number);
//                commitToService(shopId, list.get(position).getSkuId(), number);//购物车网络数量操作
//                UUUUUnotifyDataSetChanged();
//            }
//        });
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
