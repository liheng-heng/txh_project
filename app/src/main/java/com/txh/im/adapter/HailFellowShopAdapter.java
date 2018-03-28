package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.txh.im.bean.FriendsRolesBean;
import com.txh.im.bean.FriendsShopBean;
import com.txh.im.bean.FriendsUnitDetailBean;
import com.txh.im.bean.HailFellowBean;
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

public class HailFellowShopAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<FriendsShopBean> listFriendsShop;
    private String index;
    private final CryptonyBean person;
    List<HailFellowBean> list;

    public HailFellowShopAdapter(Context context, List<FriendsShopBean> listFriendsShop, String index, List<HailFellowBean> list) {
        this.context = context;
        this.listFriendsShop = listFriendsShop;
        this.index = index;
        person = GlobalApplication.getInstance().getPerson(context);
        this.list = list;
    }

    public HailFellowShopAdapter(Context context, List<FriendsShopBean> listFriendsShop) {
        this.context = context;
        this.listFriendsShop = listFriendsShop;
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
        FriendsShopBean friendsShopBean = listFriendsShop.get(i);
//        if (friendsShopBean.getIcon() == null) {
//            friendsShopBean.setIcon("");
//        }
        if (friendsShopBean.getUnitName() == null) {
            friendsShopBean.setUnitName("");
        }
        groupViewHolder.ivBuddyExpansion.setVisibility(View.GONE);
//        if (!friendsShopBean.getIcon().equals("")) {
//            Glide.with(context).load(friendsShopBean.getIcon()).into(groupViewHolder.avatar);
//        }
        if (friendsShopBean.getUnitType().equals("1")) {
            groupViewHolder.tvToShop.setVisibility(View.GONE);
        } else {
            if (person.getUnitType().equals("2") && friendsShopBean.getUnitType().equals("2")) {
                groupViewHolder.tvToShop.setVisibility(View.GONE);
            } else {
                groupViewHolder.tvToShop.setVisibility(View.VISIBLE);
            }
        }
        if (i == 0) {
            groupViewHolder.vLineFull.setVisibility(View.GONE);
        } else {
            groupViewHolder.vLineFull.setVisibility(View.VISIBLE);
        }
        //1代表店铺，2代表商户
        if (friendsShopBean.getExpandable() == 1) {
            groupViewHolder.vLine.setVisibility(View.VISIBLE);
            if (friendsShopBean.getIconSub() != null) {
                Glide.with(context).load(friendsShopBean.getIconSub()).into(groupViewHolder.avatar);
            }
//            if (person.getUnitId().equals(friendsShopBean.getUnitId())) {
//                groupViewHolder.avatar.setImageResource(R.drawable.organizational_structure_friends22);
//            } else if (friendsShopBean.getUnitType().equals("1")) {
//                groupViewHolder.avatar.setImageResource(R.drawable.friends_shop22);
//            } else if (friendsShopBean.getUnitType().equals("2")) {
//                groupViewHolder.avatar.setImageResource(R.drawable.business_friends22);
//            }
        } else {
            groupViewHolder.vLine.setVisibility(View.GONE);
            if (friendsShopBean.getIconAdd() != null) {
                Glide.with(context).load(friendsShopBean.getIconAdd()).into(groupViewHolder.avatar);
            }
//            if (person.getUnitId().equals(friendsShopBean.getUnitId())) {
//                groupViewHolder.avatar.setImageResource(R.drawable.organizational_structure_friends2);
//            } else if (friendsShopBean.getUnitType().equals("1")) {
//                groupViewHolder.avatar.setImageResource(R.drawable.friends_shop2);
//            } else if (friendsShopBean.getUnitType().equals("2")) {
//                groupViewHolder.avatar.setImageResource(R.drawable.business_friends2);
//            }
        }
        groupViewHolder.onBindData(friendsShopBean, context, person);
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
        FriendsShopBean friendsShopBean = listFriendsShop.get(i);
        FriendsRolesBean friendsRolesBean = friendsShopBean.getItem().get(i1);
        childViewHolder.onBindData(friendsRolesBean, friendsShopBean, person);
        childViewHolder.ivBuddyExpansion.setVisibility(View.INVISIBLE);
        if (i1 == friendsShopBean.getItem().size() - 1) {
            childViewHolder.vLine.setVisibility(View.GONE);
        } else {
            childViewHolder.vLine.setVisibility(View.VISIBLE);
        }
        if (friendsRolesBean.getImagesHead() == null) {
            friendsRolesBean.setImagesHead("");
        }
        if (friendsRolesBean.getUserName() == null) {
            friendsRolesBean.setUserName("");
        }
        if (friendsRolesBean.getRoleName() == null) {
            friendsRolesBean.setRoleName("");
        }
        if (!friendsRolesBean.getImagesHead().equals("")) {
            Glide.with(context).load(friendsRolesBean.getImagesHead())
                    .placeholder(R.drawable.default_head_fang)//加载中显示的图片
                    .into(childViewHolder.avatar);
        }
        if (friendsRolesBean.getUserId().equals(person.getUserId())) {
            childViewHolder.ivImEntrance.setVisibility(View.GONE);
            childViewHolder.tvName.setTextColor(context.getResources().getColor(R.color.red3));
        } else {
            childViewHolder.ivImEntrance.setVisibility(View.VISIBLE);
            childViewHolder.tvName.setTextColor(context.getResources().getColor(R.color.color_666666));
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
        return true;
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
        FriendsShopBean friendsShopBean;
        Context context;
        CryptonyBean person;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
            ivImEntrance.setVisibility(View.GONE);
        }

        @OnClick({R.id.tv_to_shop})
        public void onClick(final View view) {
            switch (view.getId()) {
                case R.id.tv_to_shop:
                    Intent intent = new Intent(context, ShopClassifyActivity.class);
                    intent.putExtra("shopId", friendsShopBean.getUnitId());
                    intent.putExtra("shopName", friendsShopBean.getUnitName());
                    context.startActivity(intent);
                    break;

                case R.id.rl:
                    if (friendsShopBean.getUnitId().equals(person.getUnitId())) {
                        return;
                    }
                    HashMap<String, String> map = new HashMap<>();
                    map.put("FriendUnitId", friendsShopBean.getUnitId());
                    new GetNetUtil(map, Api.TX_App_SNS_FriendUnitDetail, context) {
                        @Override
                        public void successHandle(String base) {
                            if (view == null) {
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

        public void onBindData(FriendsShopBean friendsShopBean, Context context, CryptonyBean person) {
            this.friendsShopBean = friendsShopBean;
            this.context = context;
            this.person = person;
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
        FriendsShopBean friendsShopBean;
        private Gson gson;
        CryptonyBean person;


        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void onBindData(FriendsRolesBean bean, FriendsShopBean friendsShopBean, CryptonyBean person) {
            mBean = new HomeSingleListBean();
            gson = new Gson();
            mBean.setUserName(bean.getUserName());
            mBean.setUserId(bean.getUserId());
            mBean.setImagesHead(bean.getImagesHead());
            this.friendsShopBean = friendsShopBean;
            this.person = person;
        }

        @OnClick({R.id.fl, R.id.rl, R.id.avatar})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fl:
                    if (mBean.getUserId().equals(person.getUserId())) {
                        return;
                    }
                    Intent intent = new Intent(v.getContext(), ChatAcitivty.class);
                    intent.putExtra("homeSingleListBean", gson.toJson(mBean));
                    v.getContext().startActivity(intent);
                    break;

                case R.id.rl:
                    if (mBean.getUserId().equals(person.getUserId())) {
                        return;
                    }
                    clickTodetail(v);
                    break;

                case R.id.avatar:
                    if (mBean.getUserId().equals(person.getUserId())) {
                        return;
                    }
                    clickTodetail(v);
                    break;
            }
        }

        private void clickTodetail(final View v) {

            //已经是好友了
            Intent intent = new Intent();
            intent.setClass(v.getContext(), FriendsDataAddWebviewActivity.class);
            intent.putExtra("friendId", mBean.getUserId());
            intent.putExtra("FriendUnitId", friendsShopBean.getUnitId());
            intent.putExtra("intenttype", "3");
            intent.putExtra("intenttype3_where", "1");
            intent.putExtra("from", "friendlist");
            v.getContext().startActivity(intent);

            Log.v("=========>>", "6-点击好友列表展开条目");

//            HashMap<String, String> map = new HashMap<>();
//            map.put("FriendId", mBean.getUserId());
//            map.put("FriendUnitId", friendsShopBean.getUnitId());
//            new GetNetUtil(map, Api.TX_App_SNS_GetFriendDetail, v.getContext()) {
//                @Override
//                public void successHandle(String base) {
//                    if (v == null) {
//                        return;
//                    }
//                    Gson gson = new Gson();
//                    Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
//                    }.getType());
//                    if (basebean.getStatus().equals("200")) {
//
//                        Intent intent = new Intent(v.getContext(), FriendsDataAddActivity.class);
//                        HomeSingleListBean obj = basebean.getObj();
//                        obj.setIsFriend("1");
//                        intent.putExtra("homeSingleListBean", gson.toJson(obj));
//                        v.getContext().startActivity(intent);
//
//                    } else {
//                        Toast.makeText(v.getContext(), basebean.getMsg(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            };


        }
    }
}
