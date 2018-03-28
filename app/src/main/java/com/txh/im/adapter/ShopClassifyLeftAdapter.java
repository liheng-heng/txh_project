package com.txh.im.adapter;

import android.content.Context;

import com.txh.im.R;
import com.txh.im.bean.ShopClassifyLeftBean;

import java.util.List;

public class ShopClassifyLeftAdapter extends BaseListAdapter<ShopClassifyLeftBean.ListBean> {
    List<ShopClassifyLeftBean.ListBean> list;
    Context context;

    public ShopClassifyLeftAdapter(Context context, List<ShopClassifyLeftBean.ListBean> mDatas, int ItemLayoutId) {
        super(context, mDatas, ItemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder helper, ShopClassifyLeftBean.ListBean item, int postion) {
        helper.setText(R.id.tv_textview, item.getName());
        if (item.isSeleted()) {
            helper.setTextColor(R.id.tv_textview, context.getResources().getColor(R.color.buttom_color));
        } else {
            helper.setTextColor(R.id.tv_textview, context.getResources().getColor(R.color.color_333333));
        }
    }

    public void RereshData(List<ShopClassifyLeftBean.ListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
