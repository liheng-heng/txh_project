package com.txh.im.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.R;
import com.txh.im.activity.ShopClassifyActivity;
import com.txh.im.bean.FriendsUnitDetailBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.txh.im.greendao.HomeSingleListBeanDao.Properties.Shops;

/**
 * Created by jiajia on 2017/4/17.
 */

public class ShopListDialog extends CenterDialog {
    @Bind(R.id.rc)
    RecyclerView rc;
    private Context context;
    private String shops;

    public ShopListDialog(Context context, String shops) {
        super(context, Shops);
        this.context = context;
        this.shops = shops;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_shop_list);
    }

    @Override
    protected void initData() {
        rc.setLayoutManager(new LinearLayoutManager(context));
        Gson gson = new Gson();
        List<FriendsUnitDetailBean> list = gson.fromJson(shops, new TypeToken<ArrayList<FriendsUnitDetailBean>>() {
        }.getType());
        if (list.size() > 0 && list.size() > 1) {
            ShopListDialogAdapter shopListDialogAdapter = new ShopListDialogAdapter(context, list, this);
            rc.setAdapter(shopListDialogAdapter);
        }
    }
}

class ShopListDialogAdapter extends RecyclerView.Adapter<ShopListDialogAdapter.ViewHolder> {
    private Context context;
    private List<FriendsUnitDetailBean> list;
    ShopListDialog shopListDialog;

    public ShopListDialogAdapter(Context context, List<FriendsUnitDetailBean> list, ShopListDialog shopListDialog) {
        this.context = context;
        this.list = list;
        this.shopListDialog = shopListDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_shop_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FriendsUnitDetailBean friendsUnitDetailBean = list.get(position);
        if (friendsUnitDetailBean.getUnitName() == null) {
            friendsUnitDetailBean.setUnitName("");
        }
        holder.tvShopName.setText(friendsUnitDetailBean.getUnitName());
        holder.tvLookover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转分类界面对应的shopid
                Intent intent = new Intent(context, ShopClassifyActivity.class);
                intent.putExtra("shopId", friendsUnitDetailBean.getUnitId() + "");
                intent.putExtra("shopName", friendsUnitDetailBean.getUnitName());
                context.startActivity(intent);
                shopListDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_shop_name)
        TextView tvShopName;
        @Bind(R.id.tv_lookover)
        TextView tvLookover;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}


