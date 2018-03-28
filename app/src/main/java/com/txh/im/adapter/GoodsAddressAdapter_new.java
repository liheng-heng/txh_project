package com.txh.im.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.bean.GoodsAddressBean;
import com.txh.im.bean.GoodsAddressBean_new;
import com.txh.im.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class GoodsAddressAdapter_new extends BaseAdapter {

    private List<GoodsAddressBean_new.ListBean> list;
    private Context context;
    private LayoutInflater inflater;
    private AlertDialog mAlertDialog = null;
    String OrderAddress;
    List<String> list_delete_address_ids = new ArrayList<>();

    public GoodsAddressAdapter_new(Context context, List<GoodsAddressBean_new.ListBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_goods_address_new, null);
            holder = new ViewHolder();
            holder.tv_default = (TextView) convertView.findViewById(R.id.tv_default);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtil.isEmpty(list.get(position).getIsDefault())) {
            if (list.get(position).getIsDefault().equals("1")) {
                holder.tv_default.setVisibility(View.VISIBLE);
            } else {
                holder.tv_default.setVisibility(View.GONE);
            }
        }
        holder.tv_address.setText(list.get(position).getReceiveAddress());


//        holder.tv_name.setText(list.get(position).getName());
//        holder.tv_phone.setText(list.get(position).getPhone());
//        holder.tv_detail_address.setText(list.get(position).getReceiveAddress());

//        if (null != list.get(position).getIsDefault() && list.get(position).getIsDefault().equals("1")) {
//            holder.iv_default.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_address));
//        } else {
//            holder.iv_default.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_address_box));
//        }

        return convertView;
    }

    static class ViewHolder {

        private TextView tv_default, tv_address;
        private ImageView iv_image;
    }


    private ProgressDialog mProgressDialog;

    private void showProgress(String showstr) {
        mProgressDialog = ProgressDialog.show(context, "", showstr);
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void Clear() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public void setDataType(List<GoodsAddressBean_new.ListBean> Dates) {
        this.list.addAll(Dates);
        notifyDataSetChanged();
    }

    public void setRefrece(List<GoodsAddressBean_new.ListBean> Dates) {
        this.list.clear();
        this.list.addAll(Dates);
        notifyDataSetChanged();
    }

    public List<String> getList_delete_address_ids() {
        return list_delete_address_ids;
    }

    public void setList_delete_address_ids(List<String> list_delete_address_ids) {
        this.list_delete_address_ids = list_delete_address_ids;
    }
}
