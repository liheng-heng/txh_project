package com.txh.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.txh.im.R;
import com.txh.im.bean.UserInShopBean;
import com.txh.im.widget.CircleImageView;
import com.txh.im.widget.QrDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/4/19.
 */

public class QrListAdapter extends BaseAdapter {
    private Context context;
    private List<UserInShopBean> list;

    public QrListAdapter(Context context, List<UserInShopBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_qr_list, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        UserInShopBean userInShopBean = list.get(i);
        viewHolder.onBindData(context,userInShopBean);
        if (userInShopBean.getUnitTypeLogo() != null && !userInShopBean.getUnitTypeLogo().equals("")) {
            Glide.with(context).load(userInShopBean.getUnitTypeLogo()).into(viewHolder.civAvatar);
        }
        if (userInShopBean.getUnitName() != null) {
            viewHolder.tvShopName.setText(userInShopBean.getUnitName());
        }
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.civ_avatar)
        CircleImageView civAvatar;
        @Bind(R.id.tv_shop_name)
        TextView tvShopName;
        @Bind(R.id.iv_qr)
        ImageView ivQr;
        UserInShopBean userInShopBean;
        Context context;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.iv_qr)
        public void OnClick(View v) {
            switch (v.getId()) {
                case R.id.iv_qr:
                    new QrDialog(context,userInShopBean).show();
                    break;
            }
        }

        public void onBindData(Context context, UserInShopBean userInShopBean) {
            this.userInShopBean = userInShopBean;
            this.context = context;
        }
    }
}
