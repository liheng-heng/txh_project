package com.txh.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.bean.FriendsShopBean;
import com.txh.im.bean.HailFellowBean;
import com.txh.im.widget.MyExpandableListview;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/4/10.
 */

public class HailFellowAdapter extends RecyclerView.Adapter<HailFellowAdapter.ViewHolder> {
    private Context context;
    private List<HailFellowBean> list;

    public HailFellowAdapter(Context context, List<HailFellowBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HailFellowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_hail_fellow, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public void refrsh(List<HailFellowBean> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(final HailFellowAdapter.ViewHolder holder, int position) {
        HailFellowBean hailFellowBean = list.get(position);
        holder.tvName.setText(hailFellowBean.getName());
        String index = hailFellowBean.getIndex();
        final List<FriendsShopBean> listFriendsShop = hailFellowBean.getList();
        final HailFellowShopAdapter hailFellowShopAdapter = new HailFellowShopAdapter(context, listFriendsShop, index, list);
        holder.elv.setAdapter(hailFellowShopAdapter);
        holder.elv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                listFriendsShop.get(i).setExpandable(0);
            }
        });
        holder.elv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                listFriendsShop.get(i).setExpandable(1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.elv)
        MyExpandableListview elv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
/*
public class HailFellowAdapter extends BaseAdapter{
    private Context context;
    private List<HailFellowBean> list_brand;

    public HailFellowAdapter(Context context, List<HailFellowBean> list_brand) {
        this.context = context;
        this.list_brand = list_brand;
    }


    @Override
    public int getCount() {
        return list_brand.size();
    }

    @Override
    public Object getItem(int i) {
        return list_brand.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.adapter_hail_fellow, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        HailFellowBean hailFellowBean = list_brand.get(position);
        holder.tvName.setText(hailFellowBean.getName());
        String index = hailFellowBean.getIndex();
        final List<FriendsShopBean> listFriendsShop = hailFellowBean.getList();
        final HailFellowShopAdapter hailFellowShopAdapter = new HailFellowShopAdapter(context, listFriendsShop, index,list_brand);
        holder.elv.setAdapter(hailFellowShopAdapter);
        holder.elv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                listFriendsShop.get(i).setExpandable(0);
            }
        });
        holder.elv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                listFriendsShop.get(i).setExpandable(1);
            }
        });

        return view;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.elv)
        MyExpandableListview elv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}*/
