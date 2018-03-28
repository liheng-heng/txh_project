package com.txh.im.itemclick;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.im.R;


public class NearbyViewHolder extends ViewHolder implements OnClickListener, OnLongClickListener {

    public ImageView iv;
    public TextView tv;
    public LinearLayout ll_button_line;
    private MyItemClickListener mListener;
    private MyItemLongClickListener mLongClickListener;

    public NearbyViewHolder(View arg0, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
        super(arg0);
        iv = (ImageView) arg0.findViewById(R.id.item_iv);
        tv = (TextView) arg0.findViewById(R.id.item_tv);
        ll_button_line = (LinearLayout) arg0.findViewById(R.id.ll_button_line);
        this.mListener = listener;
        this.mLongClickListener = longClickListener;
        arg0.setOnClickListener(this);
        arg0.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, getPosition());
        }
    }

    @Override
    public boolean onLongClick(View arg0) {
        if (mLongClickListener != null) {
            mLongClickListener.onItemLongClick(arg0, getPosition());
        }
        return true;
    }

}
