package com.txh.im.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.txh.im.R;
import com.txh.im.bean.ActiveNoticeBean;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.UIUtils;

import java.util.List;

public class ActiveAdapter extends BaseListAdapter<ActiveNoticeBean> {
    Context context;
    int val = UIUtils.dip2px(18);

    public ActiveAdapter(Context context, List<ActiveNoticeBean> mDatas, int ItemLayoutId) {
        super(context, mDatas, ItemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder helper, final ActiveNoticeBean item, final int postion) {
        helper.setText(R.id.tv_textview, item.getTitle());
        if (!TextUtil.isEmpty(item.getICon())) {
            Picasso.with(mContext).load(item.getICon()).resize(val, val).into((ImageView) helper.getView(R.id.iv_icon));
        }
    }
}
