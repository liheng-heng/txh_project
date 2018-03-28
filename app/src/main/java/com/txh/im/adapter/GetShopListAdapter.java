package com.txh.im.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.txh.im.R;
import com.txh.im.activity.ChatAcitivty;
import com.txh.im.activity.MyOrderActivity;
import com.txh.im.bean.ChoosePersonTalkBean;
import com.txh.im.bean.GetShopListItemBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.UIUtils;

import java.util.List;

public class GetShopListAdapter extends BaseListAdapter<GetShopListItemBean> {
    Context context;
    int val = UIUtils.dip2px(20);

    public GetShopListAdapter(Context context, List<GetShopListItemBean> mDatas, int ItemLayoutId) {
        super(context, mDatas, ItemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder helper, final GetShopListItemBean item, final int postion) {

        helper.setText(R.id.tv_left, item.getShopName());

//        if (!TextUtil.isEmpty(item.getImagesHead())) {
//            Picasso.with(mContext).load(item.getImagesHead()).resize(val, val)
//                    .placeholder(R.drawable.wode).into((ImageView) helper.getView(R.id.iv_left_redpoint));
//        }

        helper.setOnClickListener(R.id.tv_click, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /**
                 *             "ShopId": "12",//商户ID
                 //            "ShopName": "张三的供货铺",//商户名称
                 //            "ShopLogo": "http://img.tianxiahuo.cn/public/txh_newapp/User/mall_Logo_Icon@3x.png"//商户logo
                 */
                Intent intent = new Intent(context, MyOrderActivity.class);
                intent.putExtra("ShopId", item.getShopId());
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }
}
