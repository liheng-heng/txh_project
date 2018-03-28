package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.txh.im.R;
import com.txh.im.activity.GoodsDetailActivity;
import com.txh.im.bean.ShopTrolleyBean;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.UIUtils;

import java.util.List;

/**
 * 首页改版子条目布局
 */
public class innerGoodsAdapter extends RecyclerView.Adapter<innerGoodsAdapter.Holder> {

    private List<ShopTrolleyBean.ShopListBean.ItemListBean> list;
    private LayoutInflater inflater;
    private Context context;
    private int val = UIUtils.dip2px(58);
    private String shopid_str;
    private String userName;

    public innerGoodsAdapter(final Context context, List<ShopTrolleyBean.ShopListBean.ItemListBean> list,
                             String shopid_str, String userName) {
        this.context = context;
        this.list = list;
        this.shopid_str = shopid_str;
        this.userName = userName;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View m_View = inflater.inflate(R.layout.item_inner_goods, parent, false);
        Holder hodler = new Holder(m_View);
        return hodler;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        if (!TextUtil.isEmpty(list.get(position).getItemPic())) {
            Picasso.with(context).load(list.get(position).getItemPic()).resize(val, val).into(holder.iv_image);
        }
        holder.tv_goodsname.setText(list.get(position).getItemName());
        holder.tv_price.setText(list.get(position).getSPrice());

        holder.ll_layout_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("SkuId", list.get(position).getSkuId());
                intent.putExtra("Shopid", shopid_str);
                intent.putExtra("userName", userName);
                intent.putExtra("formShopCar", "fromCar");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {

        private LinearLayout ll_layout_out;
        private TextView tv_goodsname, tv_price;
        private ImageView iv_image;

        public Holder(View itemView) {
            super(itemView);
            ll_layout_out = (LinearLayout) itemView.findViewById(R.id.ll_layout_out);
            tv_goodsname = (TextView) itemView.findViewById(R.id.tv_goodsname);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }

    public void RereshData(List<ShopTrolleyBean.ShopListBean.ItemListBean> list, String shopid_str) {
        this.list = list;
        this.shopid_str = shopid_str;
        notifyDataSetChanged();
    }

    public void loadData(List<ShopTrolleyBean.ShopListBean.ItemListBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
