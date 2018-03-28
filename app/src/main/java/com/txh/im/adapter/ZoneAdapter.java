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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ViewHolder> {
    private Context context;
    private List<ZoneBean> obj;
    private List<ZoneBean> list;
    private ZoneListener listener;

    private String country_str;
    private String province_str;
    private String city_str;
    private String district_str;

    public ZoneAdapter(Context context, List<ZoneBean> obj, List<ZoneBean> list, ZoneListener listener) {
        this.context = context;
        this.obj = obj;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ZoneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_city, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvName.setText(obj.get(position).getLocationName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.add(obj.get(position));
                listener.refreshPriorityUI(list, obj.get(position).getLocationId(), obj.get(position).getLocationType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return obj.size();
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
