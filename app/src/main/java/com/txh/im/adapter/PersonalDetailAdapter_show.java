package com.txh.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.PersonalDetailBean;
import com.txh.im.bean.UserInShopBean;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.QrDialog;

import java.util.List;

public class PersonalDetailAdapter_show extends BaseListAdapter<PersonalDetailBean.ShowInfoBean> {

    Context context;
    String bgColor_str;
    int bgColor_int;
    UserInShopBean userBean = new UserInShopBean();

    public PersonalDetailAdapter_show(Context context, List<PersonalDetailBean.ShowInfoBean> mDatas, int ItemLayoutId) {
        super(context, mDatas, ItemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder helper, final PersonalDetailBean.ShowInfoBean item, int position) {
        helper.setText(R.id.tv_left, item.getItemTitle());
        helper.setText(R.id.tv_right, item.getItemText());

        /**
         * 暂时不设置后台返回的字体颜色
         */
//        if (TextUtil.isEmpty(item.getTextColor()) || item.getTextColor().length() == 0) {
//            bgColor_str = "#333333";//默认字体颜色
//        } else {
//            if (item.getTextColor().length() == 6) {
//                bgColor_str = "#" + item.getTextColor();
//            }
//            bgColor_str = item.getTextColor();
//        }
//        bgColor_int = Color.parseColor(bgColor_str);
//        helper.setTextColor(R.id.tv_right, bgColor_int);

        switch (item.getItemCode()) {

            case "IdentityCheck":
                //法人身份认证
//                ll_right_layout
                helper.setVisible(R.id.ll_right_layout, true);
                helper.setVisible(R.id.iv_right, true);
                break;

            case "QR":
                //二维码
                helper.setVisible(R.id.ll_right_layout, true);
                helper.setVisible(R.id.iv_right, true);
                helper.setVisible(R.id.iv_right2, true);
                helper.setVisible(R.id.tv_right, false);
                helper.setImageByUrlWithLength(R.id.iv_right, item.getItemValue(), 25);
                break;

            case "StartOrderAmount":
                bgColor_str = "#e65252";//默认字体颜色
                bgColor_int = Color.parseColor(bgColor_str);
                helper.setTextColor(R.id.tv_right, bgColor_int);
                break;

        }

        helper.setOnClickListener(R.id.rl_layout, new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                switch (item.getItemCode()) {
                    //我的二维码
                    case "QR":
                        userBean.setUnitName(item.getUnitName());
                        userBean.setUserName(item.getUserName());
                        userBean.setImagesHead(item.getImagesHead());
                        userBean.setUserQRUrl(item.getItemValue());
                        userBean.setUnitType(GlobalApplication.getInstance().getPerson(mContext).getUnitType());
                        new QrDialog(context, userBean).show();
                        break;
                }
            }
        });
    }

    public void refareshData(List<PersonalDetailBean.ShowInfoBean> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }
}
