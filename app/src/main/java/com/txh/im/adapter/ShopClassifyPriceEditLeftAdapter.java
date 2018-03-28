package com.txh.im.adapter;

import android.content.Context;

import com.txh.im.R;
import com.txh.im.bean.ShopClassifyPriceEditLeftBean;

import java.util.List;

public class ShopClassifyPriceEditLeftAdapter extends BaseListAdapter<ShopClassifyPriceEditLeftBean.CategoryListBean> {
    List<ShopClassifyPriceEditLeftBean.CategoryListBean> list;
    Context context;

    public ShopClassifyPriceEditLeftAdapter(Context context, List<ShopClassifyPriceEditLeftBean.CategoryListBean> mDatas, int ItemLayoutId) {
        super(context, mDatas, ItemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder helper, ShopClassifyPriceEditLeftBean.CategoryListBean item, int postion) {
        helper.setText(R.id.tv_textview, item.getName());
        if (item.isSeleted()) {
            helper.setTextColor(R.id.tv_textview, context.getResources().getColor(R.color.buttom_color));
        } else {
            helper.setTextColor(R.id.tv_textview, context.getResources().getColor(R.color.color_333333));
        }
    }

    public void RereshData(List<ShopClassifyPriceEditLeftBean.CategoryListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
