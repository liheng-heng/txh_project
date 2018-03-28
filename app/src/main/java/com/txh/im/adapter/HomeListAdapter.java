package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gxz.library.SwapWrapperUtils;
import com.gxz.library.SwipeMenuBuilder;
import com.gxz.library.view.SwipeMenuLayout;
import com.gxz.library.view.SwipeMenuView;
import com.hyphenate.chat.EMClient;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.ChatAcitivty;
import com.txh.im.activity.FriendsDataAddActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.dao.SearchHistoryDao;
import com.txh.im.fragment.HomeFragment;
import com.txh.im.utils.GetNetUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by jiajia on 2017/2/24.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    public Context mContext;
    public List<HomeSingleListBean> list;
    private SwipeMenuBuilder swipeMenuBuilder;


    public HomeListAdapter(Context mContext, List<HomeSingleListBean> list, HomeFragment homeFragment) {
        this.mContext = mContext;
        this.list = list;
        swipeMenuBuilder = (SwipeMenuBuilder) homeFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据数据创建右边的操作view
        SwipeMenuView menuView = swipeMenuBuilder.create();
        //包装用户的item布局
        SwipeMenuLayout swipeMenuLayout = SwapWrapperUtils.wrap(parent, R.layout.adapter_home_list, menuView, new BounceInterpolator(), new LinearInterpolator());
        ViewHolder vh = new ViewHolder(swipeMenuLayout);
        setListener(parent, vh, viewType);
        return vh;
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HomeSingleListBean homeSingleListBean = list.get(position);
        holder.initData(homeSingleListBean);
        if (!homeSingleListBean.getImagesHead().equals("")) {
            Glide.with(mContext).load(homeSingleListBean.getImagesHead()).
                    placeholder(R.drawable.default_head_fang)//加载中显示的图片
                    .into(holder.ivAvatar);
        }
        holder.ivMerchantIcon.setVisibility(View.INVISIBLE);
//        if (!homeSingleListBean.getIcon().equals("")) {
//            Glide.with(mContext).load(homeSingleListBean.getIcon()).into(holder.ivMerchantIcon);
//        }
        //false是买家,true是卖家,商家显示,店家不显示
//        if (homeSingleListBean.getIsShop().equals("true")) {
//            holder.ivMerchantIcon.setVisibility(View.VISIBLE);
//        } else {
//            holder.ivMerchantIcon.setVisibility(View.GONE);
//        }
        if (!homeSingleListBean.getUnreadMessageCount().equals("")) {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
            holder.tvUnreadCount.setText(homeSingleListBean.getUnreadMessageCount());
        } else {
            holder.tvUnreadCount.setVisibility(GONE);
        }
        if (!homeSingleListBean.getLastUpdateTime().equals("")) {
            holder.tvTimestamp.setVisibility(View.VISIBLE);
            holder.tvTimestamp.setText(homeSingleListBean.getLastUpdateTime());
        }
        if (position == list.size() - 1) {
            holder.vLine.setVisibility(GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        holder.tvUserName.setText(homeSingleListBean.getUserName());
        holder.tvLastMessage.setText(homeSingleListBean.getLastMessage());
        //点击商家弹出dialog
//        holder.ivMerchantIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String unitType = GlobalApplication.getInstance().getPerson(mContext).getUnitType();
//                if (homeSingleListBean.getIsShop().equals("true") && unitType.equals("2")) {
//                    return;
//                }
//                if (homeSingleListBean.getIsShop().equals("true") && !unitType.equals("2")) {
//                    Gson gson = new Gson();
//                    List<FriendsUnitDetailBean> list_brand = gson.fromJson(homeSingleListBean.getShops(), new TypeToken<ArrayList<FriendsUnitDetailBean>>() {
//                    }.getType());
//                    if (list_brand.size() > 0 && list_brand.size() > 1) {
//                        new ShopListDialog(mContext, homeSingleListBean.getShops()).show();
//                    } else if (list_brand.size() == 1) {
//                        //跳转分类界面对应的shopid
//                        Intent intent = new Intent(mContext, ShopClassifyActivity.class);
//                        intent.putExtra("shopId", list_brand.get(0).getUnitId() + "");
//                        intent.putExtra("shopName", list_brand.get(0).getUnitName());
//                        mContext.startActivity(intent);
//                    }
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<HomeSingleListBean> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    public void removeItem(int position) {
        HomeSingleListBean homeSingleListBean = list.get(position);
        list.remove(position);
        SearchHistoryDao.getInstance().delete(homeSingleListBean);
        EMClient.getInstance().chatManager().getConversation(homeSingleListBean.getUserId()).markAllMessagesAsRead();
        EMClient.getInstance().chatManager().deleteConversation(homeSingleListBean.getUserId(), true);
        remove(position);
    }

    public void remove(int pos) {
        this.notifyItemRemoved(pos);
    }

    public void onClickContent(int position) {
        Gson gson = new Gson();
        HomeSingleListBean homeSingleListBean = list.get(position);
        Intent intent = new Intent(mContext, ChatAcitivty.class);
        intent.putExtra("homeSingleListBean", gson.toJson(homeSingleListBean));
        mContext.startActivity(intent);
    }

    public void onClickPortrait(int position) {
        HomeSingleListBean homeSingleListBean = list.get(position);
        final HashMap<String, String> map = new HashMap<>();
        map.put("FriendId", homeSingleListBean.getUserId());
        new GetNetUtil(map, Api.TX_App_SNS_GetFriendDetail, mContext) {
            @Override
            public void successHandle(String base) {
                Gson gson = new Gson();
                Basebean<HomeSingleListBean> basebean = gson.fromJson(base, new TypeToken<Basebean<HomeSingleListBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    Intent intent = new Intent(mContext, FriendsDataAddActivity.class);
                    HomeSingleListBean obj = basebean.getObj();
                    obj.setIsFriend("1");
                    intent.putExtra("homeSingleListBean", gson.toJson(obj));
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, basebean.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.civ_avatar)
        ImageView ivAvatar;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.tv_last_message)
        TextView tvLastMessage;
        @Bind(R.id.tv_timestamp)
        TextView tvTimestamp;
        @Bind(R.id.ll_right)
        public LinearLayout llRight;
        @Bind(R.id.rl_conversation_item)
        public LinearLayout rlConversationItem;
        @Bind(R.id.tv_unread_count)
        TextView tvUnreadCount;
        @Bind(R.id.iv_merchant_icon)
        ImageView ivMerchantIcon;
        @Bind(R.id.rl_click)
        public RelativeLayout rlClick;
        @Bind(R.id.ll_click)
        public LinearLayout llClick;
        @Bind(R.id.fl_merchant_icon)
        FrameLayout flMerchantIcon;
        @Bind(R.id.v_line)
        View vLine;
//        @Bind(R.id.tv_item_delete)
//        public TextView tvItemDelete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void initData(HomeSingleListBean homeSingleListBean) {
            if (homeSingleListBean.getLastUpdateTime() == null) {
                homeSingleListBean.setLastUpdateTime("");
            }
            if (homeSingleListBean.getLastMessage() == null) {
                homeSingleListBean.setLastMessage("");
            }
            if (homeSingleListBean.getImagesHead() == null) {
                homeSingleListBean.setImagesHead("");
            }
            if (homeSingleListBean.getIcon() == null) {
                homeSingleListBean.setIcon("");
            }
            if (homeSingleListBean.getIsShop() == null) {
                homeSingleListBean.setIsShop("");
            }
            if (homeSingleListBean.getUserName() == null) {
                homeSingleListBean.setUserName("");
            }
            if (homeSingleListBean.getUnreadMessageCount() == null || homeSingleListBean.getUnreadMessageCount().equals("0")) {
                homeSingleListBean.setUnreadMessageCount("");
            }
            if (homeSingleListBean.getLastUpdateTime() == null) {
                homeSingleListBean.setLastUpdateTime("");
            }
        }
    }

    public OnItemClickListener<HomeSingleListBean> mOnItemClickListener;

    public interface OnItemClickListener<HomeSingleListBean> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, HomeSingleListBean o, int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}