package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.FriendsDataAddActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/2/24.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private Context context;
    private List<HomeSingleListBean> list;

    public FriendsListAdapter(Context context, List<HomeSingleListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HomeSingleListBean homeSingleListBean = list.get(position);
        if (position == list.size() - 1) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        holder.llUserList.setVisibility(View.VISIBLE);
        Glide.with(context).load(homeSingleListBean.getImagesHead()).into(holder.civAvatar);
        holder.tvUserName.setText(homeSingleListBean.getUserName());
        holder.tvTxhCode.setText(homeSingleListBean.getTXHCode());
        holder.llUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SearchHistoryDao.getInstance().addHistory(homeSingleListBean);
                HashMap<String, String> map = new HashMap<>();
                map.put("FriendId", homeSingleListBean.getUserId());
                new GetNetUtil(map, Api.TX_App_SNS_GetFriendDetail, context) {
                    @Override
                    public void successHandle(String base) {
                        if (context == null){
                            return;
                        }
                        Gson gson = new Gson();
                        Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
                        }.getType());
                        if (basebean.getStatus().equals("200")) {
                            Intent intent = new Intent(context, FriendsDataAddActivity.class);
                            HomeSingleListBean obj = basebean.getObj();
                            obj.setIsFriend("1");
                            intent.putExtra("homeSingleListBean", gson.toJson(obj));
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context,basebean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.civ_avatar)
        CircleImageView civAvatar;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.tv_txh_code)
        TextView tvTxhCode;
        @Bind(R.id.ll_user_list)
        LinearLayout llUserList;
        @Bind(R.id.v_line)
        View vLine;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}