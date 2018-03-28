package com.txh.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.txh.im.R;
import com.txh.im.bean.MyOrderContentBean;
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class MyGridViewAdapter extends BaseAdapter {

    /**
     * 上下文对象
     */
    private List<MyOrderContentBean.ItemListBean> goods = new ArrayList<>();
    private Context context;
    private int val = UIUtils.dip2px(70);

    public MyGridViewAdapter(Context context, List<MyOrderContentBean.ItemListBean> goods) {
        this.goods = goods;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (goods.size() > 4) {
            return 4;
        } else {
            return goods.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return goods.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.ordres_item_gridview, null);
        ImageView itemImage = (ImageView) convertView.findViewById(R.id.iv_item_iamge);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_item_num);

        String itemPic = goods.get(position).getItemPic();
        String itemNumber = goods.get(position).getItemNumber();

        textView.setText(itemNumber);

        if (!TextUtil.isEmpty(itemPic)) {
            Picasso.with(context).load(itemPic).resize(val, val).transform(
                    new RoundedTransformation(3, 0)).placeholder(R.drawable.default_good).into(itemImage);
        } else {
            Picasso.with(context).load(R.drawable.default_good).resize(val, val).transform(
                    new RoundedTransformation(3, 0)).into(itemImage);
        }

        return convertView;
    }

    public static class ViewHolder {
        public ImageView ItemImage = null;
        public TextView textView = null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
