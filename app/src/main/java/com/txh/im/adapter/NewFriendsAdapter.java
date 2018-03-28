package com.txh.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gxz.library.SwapWrapperUtils;
import com.gxz.library.SwipeMenuBuilder;
import com.gxz.library.view.SwipeMenuLayout;
import com.gxz.library.view.SwipeMenuView;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.bean.GetAskListBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/4/11.
 */

public class NewFriendsAdapter extends RecyclerView.Adapter<NewFriendsAdapter.ViewHolder> {
    private Context context;
    private List<GetAskListBean> list;
    Gson gson = new Gson();
    private SwipeMenuBuilder swipeMenuBuilder;

    public NewFriendsAdapter(Context context, List<GetAskListBean> list) {
        this.context = context;
        this.list = list;
        swipeMenuBuilder = (SwipeMenuBuilder) this.context;
    }

    @Override
    public NewFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据数据创建右边的操作view
        SwipeMenuView menuView = swipeMenuBuilder.create();
        //包装用户的item布局
        SwipeMenuLayout swipeMenuLayout = SwapWrapperUtils.wrap(parent, R.layout.new_friends_list,
                menuView, new BounceInterpolator(), new LinearInterpolator());
        ViewHolder vh = new ViewHolder(swipeMenuLayout);
        setListener(parent, vh, viewType);
        return vh;
    }

    @Override
    public void onBindViewHolder(NewFriendsAdapter.ViewHolder holder, int position) {
        final GetAskListBean getAskListBean = list.get(position);
        if (getAskListBean.getImagesHead() == null) {
            getAskListBean.setImagesHead("");
        }
        if (getAskListBean.getUserName() == null) {
            getAskListBean.setUserName("");
        }
        if (getAskListBean.getTXHCode() == null) {
            getAskListBean.setTXHCode("");
        }
        if (!getAskListBean.getImagesHead().equals("")) {
            Glide.with(context).load(getAskListBean.getImagesHead()).into(holder.civAvatar);
        }
        if (!getAskListBean.getUserName().equals("")) {
            holder.tvUserName.setText(getAskListBean.getUserName());
        } else {
            holder.tvUserName.setText(getAskListBean.getTXHCode());
        }
        holder.tvAddFriends.setVisibility(View.VISIBLE);
        if (position == list.size() - 1) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        switch (getAskListBean.getStatus()) {
            case -1:
                holder.tvAddFriends.setBackground(context.getResources().getDrawable(R.drawable.shape_full_gray));
                holder.tvAddFriends.setText("已过期");
                holder.tvAddFriends.setTextColor(Color.WHITE);
                break;
            case 0:
                holder.tvAddFriends.setText("接受");
                holder.tvAddFriends.setBackground(context.getResources().getDrawable(R.drawable.shape_full_red));
                holder.tvAddFriends.setTextColor(Color.WHITE);
                holder.tvAddFriends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TakeFriend(getAskListBean.getFId(), getAskListBean);
                    }
                });
                break;
            case 1:
                holder.tvAddFriends.setBackground(context.getResources().getDrawable(R.drawable.shape_full_white));
                holder.tvAddFriends.setText("已添加");
                holder.tvAddFriends.setTextColor(context.getResources().getColor(R.color.grey_text));
                break;
            default:
                break;
        }
    }

    private void TakeFriend(String fId, final GetAskListBean getAskListBean) {
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("FId", fId);
        new GetNetUtil(hashmap, Api.TX_App_SNS_TakeFriend, context) {
            @Override
            public void successHandle(String base) {
                Gson gson = new Gson();
                HttpBean basebean = gson.fromJson(base, HttpBean.class);
                if (basebean.getStatus().equals("200")) {
                    getAskListBean.setStatus(1);
                    notifyDataSetChanged();
                } else {
                    ToastUtils.toast(context, basebean.getMsg());
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refresh(List<GetAskListBean> list){
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.civ_avatar)
        CircleImageView civAvatar;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.tv_add_friends)
        TextView tvAddFriends;
        @Bind(R.id.ll_new_friends)
        LinearLayout llNewFriends;
        @Bind(R.id.v_line)
        View vLine;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void remove(int pos) {
        this.notifyItemRemoved(pos);
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, list.get(position), position);
                }
            }
        });
    }


    public OnItemClickListener<GetAskListBean> mOnItemClickListener;

    public interface OnItemClickListener<GetAskListBean> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, GetAskListBean o, int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

}
