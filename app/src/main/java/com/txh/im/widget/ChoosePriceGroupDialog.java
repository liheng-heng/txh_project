package com.txh.im.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.GoodsPlaceOrderAcitivty;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.ChoosePriceGroupBean;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.UnitBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/6/2.
 * 选择价格分组弹框
 */

public class ChoosePriceGroupDialog extends CenterDialog {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.rc)
    RecyclerView rc;
    //    private List<UnitBean> groupList;
    private CryptonyBean person;
    private List<UnitBean> groupList;
    private String userId;

    //    public ChoosePriceGroupDialog(Context context, Object object) {
//        super(context, object);
//        this.groupList = (List<UnitBean>) object;
//        person = GlobalApplication.getInstance().getPerson(context);
//    }
    public ChoosePriceGroupDialog(Context context) {
        super(context);
        person = GlobalApplication.getInstance().getPerson(context);
    }

    public ChoosePriceGroupDialog(Context context, Object object) {
        super(context);
        userId = (String) object;
        person = GlobalApplication.getInstance().getPerson(context);
    }


    @Override
    protected void initData() {
        rc.setLayoutManager(new LinearLayoutManager(context));

        final HashMap<String, String> map = new HashMap<>();
        map.put("BUserId", person.getUserId());
        new GetNetUtil(map, Api.PriceGroup_GetValidGroup, context) {
            @Override
            public void successHandle(String basebean) {
                if (context == null) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<ChoosePriceGroupBean> base = gson.fromJson(basebean, new TypeToken<Basebean<ChoosePriceGroupBean>>() {
                }.getType());
                if (base.getStatus().equals("200")) {
                    ChoosePriceGroupBean obj = base.getObj();
                    if (obj == null || obj.getGroupList().size() == 0) {
                        ToastUtils.toast(context, base.getMsg());
                        return;
                    }
                    groupList = obj.getGroupList();
                    ChoosePriceGroupAdapter choosePriceGroupAdapter = new ChoosePriceGroupAdapter(context, groupList);
                    rc.setAdapter(choosePriceGroupAdapter);
                } else {
                    ToastUtils.toast(context, base.getMsg());
                    return;
                }
            }
        };
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_choose_price_group);
    }

    @OnClick({R.id.btn_cancle, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancle:
                this.dismiss();
                break;
            case R.id.btn_confirm:
//                getGroupId();
//                ToastUtils.showToast(context, "跳转groupid------" + getGroupId());
                HashMap<String, String> map = new HashMap<>();
                map.put("BUserId", person.getUserId());
                map.put("GroupId", getGroupId());
                new GetNetUtil(map, Api.PriceGroup_SaveUnitGroup, context) {
                    @Override
                    public void successHandle(String basebean) {
                        if (context == null) {
                            return;
                        }
                        Gson gson = new Gson();
                        Basebean<ChoosePriceGroupBean> base = gson.fromJson(basebean, new TypeToken<Basebean<ChoosePriceGroupBean>>() {
                        }.getType());
                        if (base.getStatus().equals("200")) {
                            Intent intent1 = new Intent(context, GoodsPlaceOrderAcitivty.class);
                            intent1.putExtra("userId", userId);
                            context.startActivity(intent1);
                            ChoosePriceGroupDialog.this.dismiss();
                        } else {
                            ToastUtils.toast(context, base.getMsg());
                            ChoosePriceGroupDialog.this.dismiss();
                        }
                    }
                };
                break;
        }
    }

    public String getGroupId() {
        for (int i = 0; i < groupList.size(); i++) {
            if (groupList.get(i).getIsSelected() == 1) {
                return groupList.get(i).getGroupId();
            }
        }
        return null;
    }
}

class ChoosePriceGroupAdapter extends RecyclerView.Adapter<ChoosePriceGroupAdapter.ViewHolder> {
    private Context context;
    private List<UnitBean> groupList;

    public ChoosePriceGroupAdapter(Context context, List<UnitBean> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.choose_price_group_dialog_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UnitBean unitBean = groupList.get(position);
        holder.initData(context, unitBean, this, groupList);
        holder.tvGroupName.setText(unitBean.getGroupName());
        if (unitBean.getIsSelected() == 0) {//0表示未选中,1表示选中
            holder.cbCheck.setChecked(false);
        } else {
            holder.cbCheck.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_group_name)
        TextView tvGroupName;
        @Bind(R.id.cb_check)
        CheckBox cbCheck;
        @Bind(R.id.ll_check)
        LinearLayout llCheck;
        private Context context;
        private UnitBean unitBean;
        private ChoosePriceGroupAdapter choosePriceGroupAdapter;
        private List<UnitBean> groupList;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void initData(Context context, UnitBean unitBean, ChoosePriceGroupAdapter choosePriceGroupAdapter, List<UnitBean> groupList) {
            if (unitBean.getGroupName() == null) {
                unitBean.setGroupName("");
            }
            this.context = context;
            this.unitBean = unitBean;
            this.choosePriceGroupAdapter = choosePriceGroupAdapter;
            this.groupList = groupList;
        }

        @OnClick({R.id.cb_check, R.id.ll_check})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cb_check:
                    for (int i = 0; i < groupList.size(); i++) {
                        groupList.get(i).setIsSelected(0);
                    }
                    unitBean.setIsSelected(1);
                    choosePriceGroupAdapter.notifyDataSetChanged();
                    break;
                case R.id.ll_check:
                    for (int i = 0; i < groupList.size(); i++) {
                        groupList.get(i).setIsSelected(0);
                    }
                    unitBean.setIsSelected(1);
                    choosePriceGroupAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }


}
