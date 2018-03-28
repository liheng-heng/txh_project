package com.txh.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.bean.UnitBean;
import com.txh.im.listener.ShopCarMoneyOrNum;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/5/26.
 */

public class ShopSelectAdapter extends RecyclerView.Adapter<ShopSelectAdapter.ViewHolder> {
    private Context context;
    private List<UnitBean> list;
    private String isAdd;
    private ShopCarMoneyOrNum shopCarMoneyOrNum;

    public ShopSelectAdapter(Context context, List<UnitBean> list, String isAdd, ShopCarMoneyOrNum shopCarMoneyOrNum) {
        this.context = context;
        this.list = list;
        this.isAdd = isAdd;
        this.shopCarMoneyOrNum = shopCarMoneyOrNum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpater_shop_select, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UnitBean unitBean = list.get(position);
        holder.initData(context, unitBean, this, position, shopCarMoneyOrNum);
        if (unitBean.getUnitName() != null) {
            holder.tvShopName.setText(unitBean.getUnitName());
        }
        if (isAdd.equals("1")) {
            holder.tvSingleShopPrice.setVisibility(View.GONE);
        } else {
            if (unitBean.getUPriceStr() != null){
                holder.tvSingleShopPrice.setVisibility(View.VISIBLE);
                holder.tvSingleShopPrice.setText(unitBean.getUPriceStr());
            }else{
                holder.tvSingleShopPrice.setVisibility(View.GONE);
            }
        }
        if (unitBean.getIsSelected() == 0) {
            holder.ivSelect.setChecked(false);
        } else {
            holder.ivSelect.setChecked(true);
        }
    }

    public void refresh(String isAdd) {
        this.isAdd = isAdd;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.view_full)
        View viewFull;
        @Bind(R.id.iv_select)
        CheckBox ivSelect;
        @Bind(R.id.fl_select)
        FrameLayout flSelect;
        @Bind(R.id.tv_shop_name)
        TextView tvShopName;
        @Bind(R.id.tv_single_shop_price)
        TextView tvSingleShopPrice;
        private Context context;
        private UnitBean unitBean;
        private ShopSelectAdapter shopSelectAdapter;
        private int position;
        private ShopCarMoneyOrNum shopCarMoneyOrNum;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.iv_select, R.id.fl_select})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_select:
                    if (unitBean.getIsSelected() == 0) {
                        unitBean.setIsSelected(1);
                    } else {
                        unitBean.setIsSelected(0);
                    }
                    shopSelectAdapter.notifyItemChanged(position);
                    shopCarMoneyOrNum.getAllMoneyAndNum();
                    break;
                case R.id.fl_select:
                    if (unitBean.getIsSelected() == 0) {
                        unitBean.setIsSelected(1);
                    } else {
                        unitBean.setIsSelected(0);
                    }
                    shopSelectAdapter.notifyItemChanged(position);
                    shopCarMoneyOrNum.getAllMoneyAndNum();
                    break;
            }
        }

        public void initData(Context context, UnitBean unitBean, ShopSelectAdapter shopSelectAdapter, int position, ShopCarMoneyOrNum shopCarMoneyOrNum) {
            this.context = context;
            this.unitBean = unitBean;
            this.shopSelectAdapter = shopSelectAdapter;
            this.position = position;
            this.shopCarMoneyOrNum = shopCarMoneyOrNum;
        }
    }
}
