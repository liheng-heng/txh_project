package com.txh.im.adapter;

/**
 * liheng
 * 4.20
 */

import android.content.Context;

import com.txh.im.R;
import com.txh.im.bean.GetHistoryMerchantBean;

import java.util.List;

public class MercantHistoryListAdapter extends BaseListAdapter<GetHistoryMerchantBean.SearchKeyWordHistoryBean> {
    Context context;

    public MercantHistoryListAdapter(Context context, List<GetHistoryMerchantBean.SearchKeyWordHistoryBean> mDatas, int ItemLayoutId) {
        super(context, mDatas, ItemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder helper, final GetHistoryMerchantBean.SearchKeyWordHistoryBean item, final int postion) {
        helper.setText(R.id.tv_name, item.getKeyWord());

    }
}
