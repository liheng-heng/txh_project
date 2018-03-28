package com.txh.im.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.activity.ShopClassifyActivity;
import com.txh.im.bean.ShopBean;
import com.txh.im.bean.ShopCarNotSetissfyBean;
import com.txh.im.listener.RefreshUi;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/4/17.
 */

public class DeliverDialog extends CenterDialog {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.rc)
    RecyclerView rc;
    private Context context;
    private ShopCarNotSetissfyBean obj;
    private RefreshUi refreshUi;

    public DeliverDialog(Context context, ShopCarNotSetissfyBean obj) {
        super(context, obj);
        this.context = context;
        this.obj = obj;
    }

    public DeliverDialog(Context context, ShopCarNotSetissfyBean obj, RefreshUi refreshUi) {
        super(context, obj);
        this.context = context;
        this.obj = obj;
        this.refreshUi = refreshUi;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.delivery_dialog);
    }

    @Override
    protected void initData() {
        if (obj.getTitle() == null) {
            obj.setTitle("");
        }
        title.setText(obj.getTitle());
        rc.setLayoutManager(new LinearLayoutManager(context));
        DeliverDialogAdapter deliverDialogAdapter = new DeliverDialogAdapter(context, obj, this, refreshUi);
        rc.setAdapter(deliverDialogAdapter);
    }
}

class DeliverDialogAdapter extends RecyclerView.Adapter<DeliverDialogAdapter.ViewHolder> {
    private Context context;
    private ShopCarNotSetissfyBean obj;
    DeliverDialog deliverDialog;
    private RefreshUi refreshUi;

    public DeliverDialogAdapter(Context context, ShopCarNotSetissfyBean obj, DeliverDialog deliverDialog, RefreshUi refreshUi) {
        this.context = context;
        this.obj = obj;
        this.deliverDialog = deliverDialog;
        this.refreshUi = refreshUi;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (obj.getNeedAddItemText() == null) {
            obj.setNeedAddItemText("");
        }
        final ShopBean shopBean = obj.getShopList().get(position);
        if (shopBean.getShopLogo() == null) {
            shopBean.setShopLogo("");
        }
        if (shopBean.getStartOrderAmount() == null) {
            shopBean.setStartOrderAmount("");
        }
        if (shopBean.getShopName() == null) {
            shopBean.setShopName("");
        }
        holder.tvShopName.setText(shopBean.getShopName());
        holder.tvStartDelive.setText("(" + shopBean.getStartOrderAmountText() + ")");
        holder.tvJoinGoods.setText(obj.getNeedAddItemText());
        holder.tvJoinGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (refreshUi != null) {
                    refreshUi.botifyUi();
                    deliverDialog.dismiss();
                    return;
                }
                //跳转分类界面对应的shopid
                String shopId = shopBean.getShopId();
                String shopName = shopBean.getShopName();
                Intent intent = new Intent(context, ShopClassifyActivity.class);
                intent.putExtra("shopId", shopId);
                intent.putExtra("shopName", shopName);
                context.startActivity(intent);
                deliverDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return obj.getShopList().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_shop_name)
        TextView tvShopName;
        @Bind(R.id.tv_start_delive)
        TextView tvStartDelive;
        @Bind(R.id.tv_join_goods)
        TextView tvJoinGoods;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}


