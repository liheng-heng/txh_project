package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.ChatAcitivty;
import com.txh.im.activity.FriendsDataAddWebviewActivity;
import com.txh.im.activity.ShopClassifyActivity;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.FriendsShopBean2;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.Utils;
import com.txh.im.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/4/11.
 */

public class FriendsExpandableListview extends BaseExpandableListAdapter {
    private Context context;
    private List<FriendsShopBean2> listFriendsShop;
    private Gson gson;
    private final CryptonyBean person;

    public FriendsExpandableListview(Context context, List<FriendsShopBean2> listFriendsShop) {
        this.context = context;
        this.listFriendsShop = listFriendsShop;
        gson = new Gson();
        person = GlobalApplication.getInstance().getPerson(context);
    }

    @Override
    public int getGroupCount() {
        return listFriendsShop.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return listFriendsShop.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listFriendsShop.get(i).getItem();
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
            view = LayoutInflater.from(context).inflate(R.layout.hail_fellow_list, null);
            groupViewHolder = new GroupViewHolder(view);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        final FriendsShopBean2 friendsShopBean = listFriendsShop.get(i);
        HomeSingleListBean item = friendsShopBean.getItem();
        if (friendsShopBean.getIcon() == null) {
            friendsShopBean.setIcon("");
        }
        if (friendsShopBean.getUnitName() == null) {
            friendsShopBean.setUnitName("");
        }
        groupViewHolder.ivBuddyExpansion.setVisibility(View.GONE);
        if (!friendsShopBean.getIcon().equals("")) {
            Glide.with(context).load(friendsShopBean.getIcon()).into(groupViewHolder.avatar);
        }
        //1买家,2卖家
        if (friendsShopBean.getUnitType().equals("2") && item.getIsFriend().equals("1")) {
//            if (person.getUnitType().equals("2") && friendsShopBean.getUnitType().equals("2")) {
            if (person.getUnitType().equals("2")) {
                groupViewHolder.tvToShop.setVisibility(View.GONE);
            } else {
                groupViewHolder.tvToShop.setVisibility(View.VISIBLE);
            }
        } else {
            groupViewHolder.tvToShop.setVisibility(View.GONE);
        }
        groupViewHolder.tvToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopClassifyActivity.class);
                intent.putExtra("shopId", friendsShopBean.getUnitId());
                intent.putExtra("shopName", friendsShopBean.getUnitName());
                context.startActivity(intent);
            }
        });
        groupViewHolder.vTen.setVisibility(View.VISIBLE);
        groupViewHolder.tvName.setText(friendsShopBean.getUnitName());
        groupViewHolder.tvPosition.setVisibility(View.GONE);
        groupViewHolder.ivImEntrance.setVisibility(View.GONE);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.hail_fellow_list, null);
            childViewHolder = new ChildViewHolder(view);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        final HomeSingleListBean friendsRolesBean = listFriendsShop.get(i).getItem();
        final FriendsShopBean2 friendsShopBean2 = listFriendsShop.get(i);
        childViewHolder.onBindData(friendsRolesBean, friendsShopBean2, gson, context);
        childViewHolder.ivBuddyExpansion.setVisibility(View.INVISIBLE);
        if (!friendsRolesBean.getImagesHead().equals("")) {
            Glide.with(context).load(friendsRolesBean.getImagesHead()).into(childViewHolder.avatar);
        }
        childViewHolder.vLine.setVisibility(View.GONE);
        childViewHolder.tvName.setText(friendsRolesBean.getUserName());
        if (friendsRolesBean.getIsFriend().equals("0")) {
            childViewHolder.btnAddFriends.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.btnAddFriends.setVisibility(View.GONE);
        }
        switch (friendsShopBean2.getRoleType()) {
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
        if (friendsRolesBean.getIsFriend().equals("1")) {
            childViewHolder.ivImEntrance.setVisibility(View.VISIBLE);
            childViewHolder.fl.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.ivImEntrance.setVisibility(View.GONE);
            childViewHolder.fl.setVisibility(View.GONE);
            childViewHolder.tvPosition.setVisibility(View.GONE);
        }
        if (friendsShopBean2.getRoleName() != null) {
            childViewHolder.tvPosition.setText(friendsShopBean2.getRoleName());
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    static class GroupViewHolder {
        @Bind(R.id.iv_buddy_expansion)
        ImageView ivBuddyExpansion;
        @Bind(R.id.avatar)
        CircleImageView avatar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_position)
        TextView tvPosition;
        @Bind(R.id.iv_im_entrance)
        ImageView ivImEntrance;
        @Bind(R.id.v_ten)
        View vTen;
        @Bind(R.id.tv_to_shop)
        TextView tvToShop;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @Bind(R.id.iv_buddy_expansion)
        ImageView ivBuddyExpansion;
        @Bind(R.id.avatar)
        CircleImageView avatar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_position)
        TextView tvPosition;
        @Bind(R.id.iv_im_entrance)
        ImageView ivImEntrance;
        @Bind(R.id.v_line)
        View vLine;
        @Bind(R.id.btn_add_friends)
        View btnAddFriends;
        @Bind(R.id.fl)
        FrameLayout fl;
        HomeSingleListBean mBean;
        FriendsShopBean2 friendsShopBean;
        private Gson gson;
        private Context context;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void onBindData(HomeSingleListBean bean, FriendsShopBean2 friendsShopBean, Gson gson, Context context) {
            if (bean.getImagesHead() == null) {
                bean.setImagesHead("");
            }
            if (bean.getUserName() == null) {
                bean.setUserName("");
            }
            bean.setUnitId(friendsShopBean.getUnitId());
            this.mBean = bean;
            this.friendsShopBean = friendsShopBean;
            this.gson = gson;
            this.context = context;
        }

        @OnClick({R.id.fl, R.id.btn_add_friends, R.id.ll})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fl:
                    Intent intent = new Intent(v.getContext(), ChatAcitivty.class);
                    intent.putExtra("homeSingleListBean", gson.toJson(mBean));
                    v.getContext().startActivity(intent);
                    break;
//                case R.id.rl:
//                    clickTodetail(v);
//                    break;
                case R.id.btn_add_friends:
                    httpAddFriends(mBean, friendsShopBean);
                    break;
                case R.id.ll:
                    if (mBean.getIsFriend().equals("1")) {
                        clickTodetail(v);
                    } else {
                        startFriendsDataAddActivity(mBean);
                    }
                    break;
            }
        }

        private void clickTodetail(final View v) {

            //已经是好友了
            Intent intent = new Intent();
            intent.setClass(context, FriendsDataAddWebviewActivity.class);
            intent.putExtra("friendId", mBean.getUserId());
            intent.putExtra("FriendUnitId", friendsShopBean.getUnitId());
            intent.putExtra("intenttype", "3");
            intent.putExtra("intenttype3_where", "1");
            context.startActivity(intent);

            Log.v("=========>>", "5-搜索出来的好友跳转详情（已经是好友）");


//            HashMap<String, String> map = new HashMap<>();
//            map.put("FriendId", mBean.getUserId());
//            map.put("FriendUnitId", friendsShopBean.getUnitId());
//            new GetNetUtil(map, Api.TX_App_SNS_GetFriendDetail, v.getContext()) {
//                @Override
//                public void successHandle(String base) {
//                    if (context == null) {
//                        return;
//                    }
//                    Gson gson = new Gson();
//                    Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
//                    }.getType());
//                    if (basebean.getStatus().equals("200")) {
//                        Intent intent = new Intent(v.getContext(), FriendsDataAddActivity.class);
//                        HomeSingleListBean obj = basebean.getObj();
//                        obj.setIsFriend("1");
//                        intent.putExtra("homeSingleListBean", gson.toJson(obj));
//                        v.getContext().startActivity(intent);
//                    } else {
//                        Toast.makeText(v.getContext(), basebean.getMsg(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            };


        }

        private void startFriendsDataAddActivity(HomeSingleListBean obj) {

            //搜索出来，但不是好友
            Intent intent = new Intent();
            intent.setClass(context, FriendsDataAddWebviewActivity.class);
            intent.putExtra("friendId", obj.getUserId());
            intent.putExtra("FriendUnitId", obj.getUnitId());
            intent.putExtra("intenttype", "3");
            intent.putExtra("intenttype3_where", "1");
            context.startActivity(intent);

            Log.v("=========>>", "5-搜索出来的好友跳转详情（不是好友）");

//            Intent intent = new Intent(context, FriendsDataAddActivity.class);
//            intent.putExtra("homeSingleListBean", gson.toJson(obj));
//            context.startActivity(intent);

        }

        private void httpAddFriends(HomeSingleListBean friendsRolesBean, FriendsShopBean2 friendsShopBean2) {
            HashMap<String, String> map1 = new HashMap<>();
            map1.put("AddUserId", friendsRolesBean.getUserId());
            map1.put("AddUnitId", friendsShopBean2.getUnitId());
            map1.put("Channel", "1");
            if (Utils.isNetworkAvailable(context)) {
                new GetNetUtil(map1, Api.TX_App_SNS_AddFriend, context) {
                    @Override
                    public void successHandle(String base) {
                        if (context == null) {
                            return;
                        }
                        Gson gson = new Gson();
                        HttpBean basebean = gson.fromJson(base, HttpBean.class);
                        if (basebean.getStatus().equals("200")) {
                            ToastUtils.toast(context, basebean.getMsg());
                        } else {
                            ToastUtils.toast(context, basebean.getMsg());
                        }
                    }
                };
            } else {
                ToastUtils.toast(context, R.string.warm_net);
            }
        }
    }
}
