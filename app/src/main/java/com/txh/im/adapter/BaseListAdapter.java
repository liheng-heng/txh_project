package com.txh.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

/**
 * 通用的baseadapter。  基类
 *
 * @param <T>
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    protected int clickpostion = -1;

    public BaseListAdapter(Context context, List<T> mDatas, int ItemLayoutId) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas = mDatas;
        this.mItemLayoutId = ItemLayoutId;
    }

    public void setData(List<T> mdata) {
        this.mDatas = mdata;
        notifyDataSetChanged();
    }

    public void Clear() {
        if (mDatas != null) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    public void setDataType(List<T> Dates) {
        this.mDatas.addAll(Dates);
        notifyDataSetChanged();
    }

    public void setRefrece(List<T> Dates) {
        this.mDatas.clear();
        this.mDatas.addAll(Dates);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mDatas;

    }

    public void setClickPostion(int postion) {
        this.clickpostion = postion;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper, T item, int position);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }

}
