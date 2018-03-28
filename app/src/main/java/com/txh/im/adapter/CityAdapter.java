package com.txh.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.bean.ZoneBean;
import com.txh.im.listener.ZoneListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context context;
    private List<ZoneBean> mList;
    private List<ZoneBean> obj = new ArrayList<>();
    private ZoneListener listenerLocation;

    public CityAdapter(Context context, List<ZoneBean> List, ZoneListener listenerLocation) {
        this.context = context;
        this.mList = List;
        this.listenerLocation = listenerLocation;
        obj.clear();
    }

    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_city, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvName.setText(mList.get(position).getLocationName());

        if (mList.size() == position + 1) {
            holder.tvName.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_zone_item));
            holder.tvName.setTextColor(context.getResources().getColor(R.color.text_red));
        }

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obj.addAll(mList.subList(0, position + 1));
                mList.clear();
                mList.addAll(obj);
                if (listenerLocation == null) {
                    return;
                }
                listenerLocation.refreshPriorityUI(mList, mList.get(position).getLocationId(), mList.get(position).getLocationType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
