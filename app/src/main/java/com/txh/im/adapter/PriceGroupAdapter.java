package com.txh.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.ShopClassifyPriceEditActivity;
import com.txh.im.activity.ShopSelectActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.PriceGrouBean;
import com.txh.im.bean.UnitBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/5/23.
 */

public class PriceGroupAdapter extends RecyclerView.Adapter<PriceGroupAdapter.ViewHolder> {
    private Context context;
    private List<PriceGrouBean.GroupListBean> groupList;

    public PriceGroupAdapter(Context context, List<PriceGrouBean.GroupListBean> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_group_price, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PriceGrouBean.GroupListBean groupListBean = groupList.get(position);
        if (groupListBean.getGroupName() != null) {
            holder.tvGroupName.setText(groupListBean.getGroupName());
        }
        //是否默认（0否，1是）
        if (groupListBean.getIsDefault() != null && groupListBean.getIsDefault().equals("1")) {
            holder.tvDefaultPrice.setVisibility(View.VISIBLE);
        } else {
            holder.tvDefaultPrice.setVisibility(View.GONE);
        }
        if (groupListBean.getGroupId() == null) {
            return;
        }
        holder.initData(context, groupListBean.getGroupId(), groupListBean, position, this);
        //分组状态（0禁用，1正常）
        if (groupListBean.getGroupStatus() != null && groupListBean.getGroupStatus().equals("1")) {
            holder.btnOne.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_blue));
            holder.btnTwo.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_blue));
            holder.btnThree.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_red));
            holder.btnThree.setText("禁用");
            if (!groupListBean.getIsDefault().equals("1")) {
                holder.btnThree.setVisibility(View.VISIBLE);
            } else {
                holder.btnThree.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.btnOne.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_gray_999));
            holder.btnTwo.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_gray_999));
            holder.btnThree.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_green));
            holder.btnThree.setText("启用");
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_group_name)
        TextView tvGroupName;
        @Bind(R.id.tv_default_price)
        TextView tvDefaultPrice;
        @Bind(R.id.btn_one)
        Button btnOne;
        @Bind(R.id.btn_two)
        Button btnTwo;
        @Bind(R.id.btn_three)
        Button btnThree;
        private Context context;
        private String groupId;
        private PriceGrouBean.GroupListBean groupListBean;
        private int position;
        private PriceGroupAdapter priceGroupAdapter;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void initData(Context context, String groupId, PriceGrouBean.GroupListBean groupListBean, int position, PriceGroupAdapter priceGroupAdapter) {
            this.context = context;
            this.groupId = groupId;
            this.groupListBean = groupListBean;
            this.position = position;
            this.priceGroupAdapter = priceGroupAdapter;
        }

        @OnClick({R.id.btn_one, R.id.btn_two, R.id.btn_three})
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.btn_one:
//                    ToastUtils.toast(context, "跳转分类" + groupId);
                    if (groupListBean.getGroupStatus() != null && groupListBean.getGroupStatus().equals("0")){
                        return;
                    }
                    Intent intent1 = new Intent(context, ShopClassifyPriceEditActivity.class);
                    intent1.putExtra("groupId", groupId);
                    intent1.putExtra("typeIn", "2");
                    context.startActivity(intent1);

                    break;

                case R.id.btn_two:
                    if (groupListBean.getGroupStatus() != null && groupListBean.getGroupStatus().equals("0")){
                        return;
                    }
                    if (context == null) {
                        return;
                    }
                    intent = new Intent(context, ShopSelectActivity.class);
                    intent.putExtra("groupId", groupId);
                    context.startActivity(intent);
                    break;

                case R.id.btn_three:
                    if (groupListBean.getIsDefault().equals("1")) {
                        return;
                    } else {
                        if (groupListBean.getGroupStatus() != null && groupListBean.getGroupStatus().equals("1")) {
                            getNetRequest(groupListBean.getGroupId(), Api.PriceGroup_ProhibitGroup, groupListBean, position);
                        } else {
                            getNetRequest(groupListBean.getGroupId(), Api.PriceGroup_EnableGroup, groupListBean, position);
                        }
                    }
                    break;
            }
        }

        public void getNetRequest(String groupId, String api, final PriceGrouBean.GroupListBean groupListBean, final int position) {
            HashMap<String, String> map = new HashMap<>();
            map.put("GroupId", groupId);
            new GetNetUtil(map, api, context) {
                @Override
                public void successHandle(String base) {
                    if (context == null) {
                        return;
                    }
                    Gson gson = new Gson();
                    Basebean<UnitBean> basebean = gson.fromJson(base, new TypeToken<Basebean<UnitBean>>() {
                    }.getType());
                    if (basebean.getStatus().equals("200")) {
                        if (groupListBean.getGroupStatus().equals("1")) {
                            groupListBean.setGroupStatus("0");
                        } else if (groupListBean.getGroupStatus().equals("0")) {
                            groupListBean.setGroupStatus("1");
                        }
                        priceGroupAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtils.toast(context, basebean.getMsg());
                    }
                }
            };
        }
    }

}
