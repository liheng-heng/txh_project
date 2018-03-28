package com.txh.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.txh.im.R;
import com.txh.im.bean.FriendsRolesBean;
import com.txh.im.bean.FriendsShopBean;
import com.txh.im.bean.FristWordBean;
import com.txh.im.listener.RefreshUi;
import com.txh.im.widget.CircleImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/4/11.
 */

public class RecommendsFriendsToOthersExpandablelistAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<FriendsShopBean> listFriendsShop;
    private List<FristWordBean> list;
    private RefreshUi refreshUi;

    public RecommendsFriendsToOthersExpandablelistAdapter(Context context, List<FriendsShopBean>
            listFriendsShop, List<FristWordBean> list, RefreshUi refreshUi) {
        this.context = context;
        this.listFriendsShop = listFriendsShop;
        this.list = list;
        this.refreshUi = refreshUi;
    }

    @Override
    public int getGroupCount() {
        return listFriendsShop.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listFriendsShop.get(i).getItem().size();
    }

    @Override
    public Object getGroup(int i) {
        return listFriendsShop.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listFriendsShop.get(i).getItem().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.recommend_friends_to_others_expadable_adapter, null);
            groupViewHolder = new GroupViewHolder(view);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        FriendsShopBean friendsShopBean = listFriendsShop.get(i);
        groupViewHolder.initData(friendsShopBean);
        if (!friendsShopBean.getIcon().equals("")) {
            Glide.with(context).load(friendsShopBean.getIcon()).into(groupViewHolder.avatar);
        }
        if (i == 0) {
            groupViewHolder.vTen.setVisibility(View.GONE);
        } else {
            groupViewHolder.vTen.setVisibility(View.VISIBLE);
        }
        groupViewHolder.tvName.setText(friendsShopBean.getUnitName());
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        final ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.recommend_friends_to_others_expadable_adapter, null);
            childViewHolder = new ChildViewHolder(view);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        FriendsShopBean friendsShopBean = listFriendsShop.get(i);
        final FriendsRolesBean friendsRolesBean = friendsShopBean.getItem().get(i1);
        childViewHolder.initData(friendsRolesBean);
        if (i1 == friendsShopBean.getItem().size() - 1) {
            childViewHolder.vLine.setVisibility(View.GONE);
        } else {
            childViewHolder.vLine.setVisibility(View.VISIBLE);
        }
        if (!friendsRolesBean.getImagesHead().equals("")) {
            Glide.with(context).load(friendsRolesBean.getImagesHead()).into(childViewHolder.avatar);
        }
        childViewHolder.cbGroupCheck.setChecked(friendsRolesBean.getIsChecked() == 1 ? true : false);
        switch (friendsRolesBean.getRoleType()) {
            case 0://0 其他
                childViewHolder.tvPosition.setVisibility(View.GONE);
                break;
            case 1:// 1主管理员
                childViewHolder.tvPosition.setVisibility(View.VISIBLE);
                childViewHolder.tvPosition.setBackground(context.getResources().getDrawable((R.drawable.shape_full_red)));
                break;
            case 2://2管理员
                childViewHolder.tvPosition.setVisibility(View.VISIBLE);
                childViewHolder.tvPosition.setBackground(context.getResources().getDrawable((R.drawable.shape_full_blue)));
                break;
        }
        childViewHolder.tvName.setText(friendsRolesBean.getUserName());
        childViewHolder.tvPosition.setText(friendsRolesBean.getRoleName());
        childViewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int a = 0; a < list.size(); a++) {
                    for (int b = 0; b < list.get(a).getList().size(); b++) {
                        for (int c = 0; c < list.get(a).getList().get(b).getItem().size(); c++) {
                            list.get(a).getList().get(b).getItem().get(c).setIsChecked(0);
                        }
                    }
                }
                if (friendsRolesBean.getIsChecked() == 1) {
                    friendsRolesBean.setIsChecked(0);
                } else {
                    friendsRolesBean.setIsChecked(1);
                }
                refreshUi.botifyUi();
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class GroupViewHolder {
        @Bind(R.id.v_ten)
        View vTen;
        @Bind(R.id.iv_buddy_expansion)
        ImageView ivBuddyExpansion;
        @Bind(R.id.avatar)
        CircleImageView avatar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_position)
        TextView tvPosition;
        @Bind(R.id.cb_group_check)
        CheckBox cbGroupCheck;
        @Bind(R.id.fl)
        FrameLayout fl;
        @Bind(R.id.v_line)
        View vLine;
        @Bind(R.id.rl)
        RelativeLayout rl;
        @Bind(R.id.ll)
        LinearLayout ll;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
            ivBuddyExpansion.setVisibility(View.GONE);
            tvPosition.setVisibility(View.GONE);
            fl.setVisibility(View.GONE);
            vLine.setVisibility(View.VISIBLE);
        }

        public void initData(FriendsShopBean friendsShopBean) {
            if (friendsShopBean.getIcon() == null) {
                friendsShopBean.setIcon("");
            }
            if (friendsShopBean.getUnitName() == null) {
                friendsShopBean.setUnitName("");
            }
        }
    }

    static class ChildViewHolder {
        @Bind(R.id.v_ten)
        View vTen;
        @Bind(R.id.iv_buddy_expansion)
        ImageView ivBuddyExpansion;
        @Bind(R.id.avatar)
        CircleImageView avatar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_position)
        TextView tvPosition;
        @Bind(R.id.cb_group_check)
        CheckBox cbGroupCheck;
        @Bind(R.id.fl)
        FrameLayout fl;
        @Bind(R.id.v_line)
        View vLine;
        @Bind(R.id.rl)
        RelativeLayout rl;
        @Bind(R.id.ll)
        LinearLayout ll;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
            ivBuddyExpansion.setVisibility(View.INVISIBLE);
        }

        public void initData(FriendsRolesBean friendsRolesBean) {
            if (friendsRolesBean.getImagesHead() == null) {
                friendsRolesBean.setImagesHead("");
            }
            if (friendsRolesBean.getUserName() == null) {
                friendsRolesBean.setUserName("");
            }
            if (friendsRolesBean.getRoleName() == null) {
                friendsRolesBean.setRoleName("");
            }
        }
    }
}
