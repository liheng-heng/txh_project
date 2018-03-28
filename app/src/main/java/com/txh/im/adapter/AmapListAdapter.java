package com.txh.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.txh.im.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/6/16.
 */

public class AmapListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PoiItem> pois;
    private String fromactivity;

    public AmapListAdapter(Context context, List<PoiItem> pois) {
        this.context = context;
        this.pois = (ArrayList<PoiItem>) pois;
    }

    public AmapListAdapter(Context context, List<PoiItem> pois, String fromactivity) {
        this.context = context;
        this.pois = (ArrayList<PoiItem>) pois;
        this.fromactivity = fromactivity;
    }

    @Override
    public int getCount() {
        return pois.size();
    }

    @Override
    public Object getItem(int i) {
        return pois.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.address_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        PoiItem poiItem = pois.get(position);
        if (fromactivity != null && fromactivity.equals("SearchAddressActivity")) {
            viewHolder.tvCurrent.setVisibility(View.GONE);
        } else {
            if (position == 0) {
                viewHolder.tvCurrent.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvCurrent.setVisibility(View.GONE);
            }
        }
        if (position == pois.size() - 1) {
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            view.setBackground(context.getResources().getDrawable(R.drawable.shop_car_parent_bg));
        }
//        if (poiItem.getAdName() != null) {
        viewHolder.tvBigAddress.setText(poiItem.getTitle());
//        }
        viewHolder.tvSmallAddress.setText(poiItem.getSnippet());
        return view;
    }

    public void refresh() {
//        pois.clear();
//        pois.addAll(mpois);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.tv_current)
        TextView tvCurrent;
        @Bind(R.id.tv_big_address)
        TextView tvBigAddress;
        @Bind(R.id.tv_small_address)
        TextView tvSmallAddress;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
