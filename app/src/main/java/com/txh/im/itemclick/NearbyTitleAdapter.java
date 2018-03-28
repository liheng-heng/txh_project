package com.txh.im.itemclick;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.txh.im.R;
import com.txh.im.bean.MyOrderTitleBean;
import com.txh.im.bean.NearbyTitleBean;

import java.util.List;

public class NearbyTitleAdapter extends Adapter<NearbyViewHolder> {

    private List<NearbyTitleBean.ListBean> mData;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private Context context;

    public NearbyTitleAdapter(Context context, List<NearbyTitleBean.ListBean> data) {
        this.context = context;
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(NearbyViewHolder holder, int position) {

        NearbyTitleBean.ListBean listBean = mData.get(position);
        if (mData.get(position).isselect()) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.buttom_color));
            holder.ll_button_line.setBackgroundColor(context.getResources().getColor(R.color.buttom_color));
        } else {
            holder.tv.setTextColor(context.getResources().getColor(R.color.color_333333));
            holder.ll_button_line.setBackgroundColor(context.getResources().getColor(R.color.view_bg_color));
        }
        holder.tv.setText(listBean.getText());


//        MyOrderTitleBean.TypeListBean typeListBean = mData.get(position);
//        if (mData.get(position).isseleted()) {
//            holder.tv.setTextColor(context.getResources().getColor(R.color.buttom_color));
//            holder.ll_button_line.setBackgroundColor(context.getResources().getColor(R.color.buttom_color));
//        } else {
//            holder.tv.setTextColor(context.getResources().getColor(R.color.color_333333));
//            holder.ll_button_line.setBackgroundColor(context.getResources().getColor(R.color.huise2));
//
//        }
//        holder.tv.setText(typeListBean.getTypeText());


    }

    @Override
    public NearbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_nearby, parent, false);
        NearbyViewHolder vh = new NearbyViewHolder(itemView, mItemClickListener, mItemLongClickListener);
        return vh;
    }

    public void RereshData(List<NearbyTitleBean.ListBean> list) {
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
