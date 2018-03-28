package com.txh.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.bean.SearchHistoryBean;
import com.txh.im.listener.HistoryStringListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/2/24.
 */

public class SearchHistoryListAdapter extends RecyclerView.Adapter<SearchHistoryListAdapter.ViewHolder> {

    private Context context;
    private List<SearchHistoryBean> list;
    private HistoryStringListener historyStringListener;

    public SearchHistoryListAdapter(Context context, List<SearchHistoryBean> list, HistoryStringListener historyStringListener) {
        this.context = context;
        this.list = list;
        this.historyStringListener = historyStringListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_search_history_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SearchHistoryBean searchHistoryBean = list.get(position);
        if (position == list.size() - 1) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        holder.tvUserName.setText(searchHistoryBean.getUsername());
        holder.tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyStringListener.refreshPriorityUI(searchHistoryBean.getUsername());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.v_line)
        View vLine;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}