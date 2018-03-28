package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.ChatAcitivty;
import com.txh.im.activity.FriendsDataAddWebviewActivity;
import com.txh.im.activity.FriendsUnitDetailAcitivty;
import com.txh.im.activity.ShopClassifyActivity;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.FriendsShopBean3;
import com.txh.im.bean.FriendsUnitDetailBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/4/11.
 */

public class FriendsAddExpandableListview extends BaseExpandableListAdapter {
    private Context context;
    private List<FriendsShopBean3> listFriendsShop;
    private Gson gson;
    private final CryptonyBean person;

    public FriendsAddExpandableListview(Context context, List<FriendsShopBean3> listFriendsShop) {
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
            view = LayoutInflater.from(context).inflate(R.layout.hail_fellow_list, null);
            groupViewHolder = new GroupViewHolder(view);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        FriendsShopBean3 friendsShopBean3 = listFriendsShop.get(i);
        groupViewHolder.onBindData(friendsShopBean3, context);
        groupViewHolder.ivBuddyExpansion.setVisibility(View.GONE);
        if (friendsShopBean3.getUnitType().equals("2")) {
            if (person.getUnitType().equals("2")) {
                groupViewHolder.tvToShop.setVisibility(View.GONE);
            } else {
                groupViewHolder.tvToShop.setVisibility(View.VISIBLE);
            }
        } else {
            groupViewHolder.tvToShop.setVisibility(View.GONE);
        }
        if (!friendsShopBean3.getIcon().equals("")) {
            Glide.with(context).load(friendsShopBean3.getIcon()).into(groupViewHolder.avatar);
        }
        groupViewHolder.vTen.setVisibility(View.VISIBLE);
        groupViewHolder.tvName.setText(friendsShopBean3.getUnitName());
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
        FriendsShopBean3 friendsShopBean3 = listFriendsShop.get(i);
        HomeSingleListBean friendsRolesBean = friendsShopBean3.getItem().get(i1);
        childViewHolder.onBindData(friendsRolesBean, friendsShopBean3, gson, context);
        childViewHolder.ivBuddyExpansion.setVisibility(View.INVISIBLE);
        if (i1 == friendsShopBean3.getItem().size() - 1) {
            childViewHolder.vLine.setVisibility(View.GONE);
        } else {
            childViewHolder.vLine.setVisibility(View.VISIBLE);
        }
        if (!friendsRolesBean.getImagesHead().equals("")) {
            Glide.with(context).load(friendsRolesBean.getImagesHead()).into(childViewHolder.avatar);
        }
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
//        if (childViewHolder.tvPosition.)
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
        @Bind(R.id.tv_to_shop)
        TextView tvToShop;
        @Bind(R.id.rl)
        RelativeLayout rl;
        @Bind(R.id.v_line_full)
        View vLineFull;
        @Bind(R.id.v_line)
        View vLine;
        @Bind(R.id.v_ten)
        View vTen;
        FriendsShopBean3 friendsShopBean;
        Context context;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
            ivImEntrance.setVisibility(View.GONE);
        }

        @OnClick({R.id.tv_to_shop})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_to_shop:
                    Intent intent = new Intent(context, ShopClassifyActivity.class);
                    intent.putExtra("shopId", friendsShopBean.getUnitId());
                    intent.putExtra("shopName", friendsShopBean.getUnitName());
                    context.startActivity(intent);
                    break;
                case R.id.rl:
                    HashMap<String, String> map = new HashMap<>();
                    map.put("FriendUnitId", friendsShopBean.getUnitId());
                    new GetNetUtil(map, Api.TX_App_SNS_FriendUnitDetail, context) {
                        @Override
                        public void successHandle(String base) {
                            if (context == null) {
                                return;
                            }
                            Gson gson = new Gson();
                            Basebean<FriendsUnitDetailBean> basebean = gson.fromJson(base, new TypeToken<Basebean<FriendsUnitDetailBean>>() {
                            }.getType());
                            if (basebean.getStatus().equals("200")) {
                                FriendsUnitDetailBean obj = basebean.getObj();
                                if (obj == null) {
                                    return;
                                }
                                Intent intent = new Intent(context, FriendsUnitDetailAcitivty.class);
                                intent.putExtra("friendsunit", gson.toJson(obj));
                                intent.putExtra("FriendUnitId", friendsShopBean.getUnitId());
                                intent.putExtra("FriendUnitType", friendsShopBean.getUnitType());
                                context.startActivity(intent);
                            } else {
                                ToastUtils.toast(context, basebean.getMsg());
                            }

                        }
                    };
                    break;
            }
        }

        public void onBindData(FriendsShopBean3 friendsShopBean, Context context) {
            if (friendsShopBean.getUnitName() == null) {
                friendsShopBean.setUnitName("");
            }
            if (friendsShopBean.getIcon() == null) {
                friendsShopBean.setIcon("");
            }
            if (friendsShopBean.getUnitLogo() == null) {
                friendsShopBean.setUnitLogo("");
            }
            this.friendsShopBean = friendsShopBean;
            this.context = context;
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

        HomeSingleListBean mBean;
        FriendsShopBean3 friendsShopBean;
        private Gson gson;
        Context context;


        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void onBindData(HomeSingleListBean bean, FriendsShopBean3 friendsShopBean, Gson gson, Context context) {
//            mBean = new HomeSingleListBean();
//            gson = new Gson();
//            mBean.setUserName(bean.getUserName());
//            mBean.setUserId(bean.getUserId());
//            mBean.setImagesHead(bean.getImagesHead());
            if (bean.getImagesHead() == null) {
                bean.setImagesHead("");
            }
            if (bean.getUserName() == null) {
                bean.setUserName("");
            }
            this.mBean = bean;
            this.friendsShopBean = friendsShopBean;
            this.gson = gson;
            this.context = context;
        }

        @OnClick({R.id.fl, R.id.rl})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fl:
                    Intent intent = new Intent(v.getContext(), ChatAcitivty.class);
                    intent.putExtra("homeSingleListBean", gson.toJson(mBean));
                    v.getContext().startActivity(intent);
                    break;
                case R.id.rl:
                    clickTodetail(v);
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
//            HashMap<String, String> map = new HashMap<>();
//            map.put("FriendId", mBean.getUserId());
//            map.put("FriendUnitId", friendsShopBean.getUnitId());
//            new GetNetUtil(map, Api.TX_App_SNS_GetFriendDetail, v.getContext()) {
//                @Override
//                public void successHandle(String base) {
//                    if (v.getContext() == null) {
//                        return;
//                    }
//                    Gson gson = new Gson();
//                    Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
//                    }.getType());
//                    if (basebean.getStatus().equals("200")) {
//                        Intent intent = new Intent(v.getContext(), FriendsDataAddActivity.class);
//                        HomeSingleListBean obj = basebean.getObj();
//                        SearchHistoryStringDao.getInstance().addHistory(new SearchHistoryBean(null, obj.getUserName(), GlobalApplication.getInstance().getPerson(context).getUserId()));
//                        obj.setIsFriend("1");
//                        intent.putExtra("homeSingleListBean", gson.toJson(obj));
//                        v.getContext().startActivity(intent);
//                    } else {
//                        Toast.makeText(v.getContext(), basebean.getMsg(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            };
        }


    }
}
