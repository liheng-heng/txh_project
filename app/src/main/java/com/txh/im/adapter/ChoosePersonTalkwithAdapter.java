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
import com.txh.im.bean.ChoosePersonTalkBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.UIUtils;

import java.util.List;

public class ChoosePersonTalkwithAdapter extends BaseListAdapter<ChoosePersonTalkBean.UserListBean> {
    Context context;
    int val = UIUtils.dip2px(20);
    private HomeSingleListBean homeSingleListBean;

    public ChoosePersonTalkwithAdapter(Context context, List<ChoosePersonTalkBean.UserListBean> mDatas, int ItemLayoutId) {
        super(context, mDatas, ItemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder helper, final ChoosePersonTalkBean.UserListBean item, final int postion) {

        helper.setText(R.id.tv_left, item.getUserName());

        if (!TextUtil.isEmpty(item.getUserRoleIcon())) {
            Picasso.with(mContext).load(item.getUserRoleIcon()).resize(val, val)
                    .placeholder(R.drawable.wode).into((ImageView) helper.getView(R.id.iv_left_redpoint));
        }

        helper.setOnClickListener(R.id.tv_talkwith, new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /**
                 * UserId : 100001
                 * PCRoleId : 4
                 * UserRoleIcon :
                 * UserName : 15222222222
                 * ImagesHead : http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png
                 * TXHCode : sjtvv2489587604
                 */
                Gson gson = new Gson();
                homeSingleListBean = new HomeSingleListBean();
                Intent intent = new Intent(context, ChatAcitivty.class);
                homeSingleListBean.setUserId(item.getUserId());
                homeSingleListBean.setUserName(item.getUserName());
                homeSingleListBean.setImagesHead(item.getImagesHead());
                intent.putExtra("homeSingleListBean", gson.toJson(homeSingleListBean));
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }
}
