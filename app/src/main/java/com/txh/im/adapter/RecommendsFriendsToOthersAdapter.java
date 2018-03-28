package com.txh.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.bean.FristWordBean;
import com.txh.im.listener.RefreshUi;
import com.txh.im.widget.MyExpandableListview;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/4/10.
 */

public class RecommendsFriendsToOthersAdapter extends RecyclerView.Adapter<RecommendsFriendsToOthersAdapter.ViewHolder> {
    private Context context;
    private List<FristWordBean> list;
    private RecommendsFriendsToOthersExpandablelistAdapter recommendsFriendsToOthersExpandablelistAdapter;
    private RefreshUi refreshUi;

    public RecommendsFriendsToOthersAdapter(Context context, List<FristWordBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecommendsFriendsToOthersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_recommend_freinds_to_others, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecommendsFriendsToOthersAdapter.ViewHolder holder, int position) {
        FristWordBean fristWordBean = list.get(position);
        if (fristWordBean.getFristWord() == null) {
            fristWordBean.setFristWord("");
        }
        holder.tv.setText(fristWordBean.getFristWord());
        holder.elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        if (fristWordBean.getList() != null && fristWordBean.getList().size() > 0) {
            refreshUi = new RefreshUi() {
                @Override
                public void botifyUi() {
                    notifyDataSetChanged();
                }
            };
            recommendsFriendsToOthersExpandablelistAdapter = new RecommendsFriendsToOthersExpandablelistAdapter(context, fristWordBean.getList(), list, refreshUi);
            holder.elv.setAdapter(recommendsFriendsToOthersExpandablelistAdapter);
            for (int i = 0; i < fristWordBean.getList().size(); i++) {
                holder.elv.expandGroup(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv)
        TextView tv;
        @Bind(R.id.elv)
        MyExpandableListview elv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}