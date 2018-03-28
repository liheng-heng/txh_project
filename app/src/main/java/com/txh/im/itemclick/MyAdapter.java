package com.txh.im.itemclick;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.txh.im.R;
import com.txh.im.bean.MyOrderTitleBean;

public class MyAdapter extends Adapter<MyViewHolder> {

    private List<MyOrderTitleBean.TypeListBean> mData;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private Context context;

    public MyAdapter(Context context, List<MyOrderTitleBean.TypeListBean> data) {
        this.context = context;
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyOrderTitleBean.TypeListBean typeListBean = mData.get(position);
        if (mData.get(position).isseleted()) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.buttom_color));
            holder.ll_button_line.setBackgroundColor(context.getResources().getColor(R.color.buttom_color));
        } else {
            holder.tv.setTextColor(context.getResources().getColor(R.color.color_333333));
            holder.ll_button_line.setBackgroundColor(context.getResources().getColor(R.color.huise2));

        }
        holder.tv.setText(typeListBean.getTypeText());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView, mItemClickListener, mItemLongClickListener);
        return vh;
    }

    public void RereshData(List<MyOrderTitleBean.TypeListBean> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }
}
