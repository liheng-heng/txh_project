package com.txh.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.bean.MyOrderTitleBean;
import com.txh.im.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单标题条目
 */

public class MyOrderTitleAdapter extends RecyclerView.Adapter<MyOrderTitleAdapter.Holder> implements View.OnClickListener {

    private List<MyOrderTitleBean.TypeListBean> list = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private int val = UIUtils.dip2px(30);

    public MyOrderTitleAdapter(final Context context, List<MyOrderTitleBean.TypeListBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View m_View = inflater.inflate(R.layout.item_order_title, parent, false);
        Holder hodler = new Holder(m_View);
        m_View.setOnClickListener(this);
        return hodler;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        /**
         * TypeCode : 0
         * TypeText : 全部
         */


        if (list.get(position).isseleted()) {
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.buttom_color));
            holder.ll_button_line.setBackgroundColor(context.getResources().getColor(R.color.buttom_color));

        } else {
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_333333));
            holder.ll_button_line.setBackgroundColor(context.getResources().getColor(R.color.huise4));
        }

        holder.tv_name.setText(list.get(position).getTypeText());

        holder.ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int m = 0; m < list.size(); m++) {
                    list.get(m).setIsseleted(false);
                }
                list.get(position).setIsseleted(true);
                notifyDataSetChanged();
            }
        });

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(list.get(position));

    }

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (List<MyOrderTitleBean.TypeListBean>) v.getTag());
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        private LinearLayout ll_button_line, ll_layout;
        private TextView tv_name;

        public Holder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            ll_button_line = (LinearLayout) itemView.findViewById(R.id.ll_button_line);
            ll_layout = (LinearLayout) itemView.findViewById(R.id.rl_class_brand_layout);
        }
    }

    public void RereshData(List<MyOrderTitleBean.TypeListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, List<MyOrderTitleBean.TypeListBean> list);
    }

}
