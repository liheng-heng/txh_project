package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.txh.im.R;
import com.txh.im.activity.FriendsDataAddWebviewActivity;
import com.txh.im.activity.ShopClassifyPriceEditActivity;
import com.txh.im.bean.UnitBean;
import com.txh.im.widget.CircleImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.txh.im.greendao.HomeSingleListBeanDao.Properties.UnitId;


/**
 * Created by jiajia on 2017/5/23.
 */

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> {
    private Context context;
    private List<UnitBean> list;
    int width;

    public QuoteAdapter(Context context, List<UnitBean> list) {
        this.context = context;
        this.list = list;
        width = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_quote, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UnitBean unitBean = list.get(position);
        holder.initData(context, unitBean);
        if (position == 0) {
            holder.vLineFull.setVisibility(View.GONE);
        } else {
            holder.vLineFull.setVisibility(View.VISIBLE);
        }
        if (!unitBean.getUnitLogo().equals("")) {
            Glide.with(context).load(unitBean.getUnitLogo()).into(holder.avatar);
        }
        if (!unitBean.getGroupName().equals("")) {
            ViewGroup.LayoutParams layoutParams = holder.tvPosition.getLayoutParams();
            if (unitBean.getGroupName().length() == 1) {
                layoutParams.width = 60;
                layoutParams.height = 60;
                holder.tvPosition.setBackground(context.getResources().getDrawable(R.drawable.em_unread_count_bg));
                holder.tvPosition.setPadding(0, 0, 0, 0);
                holder.tvPosition.setLayoutParams(layoutParams);
            } else {
                layoutParams.width = layoutParams.WRAP_CONTENT;
                layoutParams.width = layoutParams.WRAP_CONTENT;
                holder.tvPosition.setBackground(context.getResources().getDrawable(R.drawable.shape_full_red));
                holder.tvPosition.setPadding(3, 3, 3, 3);
                holder.tvPosition.setLayoutParams(layoutParams);
            }
            holder.tvPosition.setVisibility(View.VISIBLE);
            holder.tvPosition.setText(unitBean.getGroupName());
            holder.tvPosition.setMaxEms(8);
            holder.tvPosition.setEllipsize(TextUtils.TruncateAt.END);
        } else {
            holder.tvPosition.setVisibility(View.GONE);
        }
        ViewGroup.LayoutParams layoutParams = holder.tvName.getLayoutParams();
        layoutParams.height = layoutParams.MATCH_PARENT;
        if (unitBean.getUnitName().length() > width) {
            layoutParams.width = width - holder.tvPosition.getLayoutParams().width
                    - holder.fl.getLayoutParams().width - holder.avatar.getLayoutParams().width;
        } else {
            layoutParams.width = layoutParams.WRAP_CONTENT;
        }
        holder.tvName.setLayoutParams(layoutParams);
        holder.tvName.setText(unitBean.getUnitName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.v_line_full)
        View vLineFull;
        @Bind(R.id.avatar)
        CircleImageView avatar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_position)
        TextView tvPosition;
        @Bind(R.id.fl)
        FrameLayout fl;
        @Bind(R.id.rl)
        RelativeLayout rl;
        @Bind(R.id.ll)
        LinearLayout ll;
        private UnitBean unitBean;
        private Context context;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void initData(Context context, UnitBean unitBean) {
            this.context = context;
            if (unitBean.getUnitLogo() == null) {
                unitBean.setUnitLogo("");
            }
            if (unitBean.getGroupName() == null) {
                unitBean.setGroupName("");
            }
            if (unitBean.getUnitName() == null) {
                unitBean.setUnitName("");
            }
            this.unitBean = unitBean;
        }

        @OnClick({R.id.avatar, R.id.ll})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.avatar:
                    //已经是好友了
                    Intent intent2 = new Intent();
                    intent2.setClass(context, FriendsDataAddWebviewActivity.class);
                    intent2.putExtra("friendId", unitBean.getUserId());
                    intent2.putExtra("FriendUnitId", unitBean.getUnitId());
                    intent2.putExtra("intenttype", "3");
                    intent2.putExtra("intenttype3_where", "1");
                    context.startActivity(intent2);
                    break;

                case R.id.ll:
                    if (UnitId != null) {
                        Intent intent = new Intent(context, ShopClassifyPriceEditActivity.class);
                        intent.putExtra("shopId", unitBean.getUnitId());
                        intent.putExtra("groupId", unitBean.getGroupId());
                        intent.putExtra("typeIn", "1");
                        context.startActivity(intent);
                    }
                    break;
            }
        }

    }
}



