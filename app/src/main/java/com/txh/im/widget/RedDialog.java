package com.txh.im.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.activity.CouponWebviewActivity;
import com.txh.im.bean.CouponBean;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.Utils;

import java.util.List;

/**
 * Created by jiajia on 2017/6/22.
 */

public class RedDialog extends CenterDialog {

    LinearLayout ll_list_layout;
    LinearLayout ll_close_layout, ll_add_coupon_item;
    TextView tv_choose_coupon;
    ImageView iv_person_image, iv_close;
    RedPacketsLayout packets_layout;
    List<CouponBean.CouponListBean> couponList;
    String userCouponPLUrl;
    Context context;

    public RedDialog(Context context) {
        super(context);
        this.context = context;
    }

    public RedDialog(Context context, List<CouponBean.CouponListBean> couponList) {
        super(context);
        this.context = context;
        this.couponList = couponList;
    }


    public RedDialog(Context context, List<CouponBean.CouponListBean> couponList, String userCouponPLUrl) {
        super(context);
        this.context = context;
        this.couponList = couponList;
        this.userCouponPLUrl = userCouponPLUrl;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_red);
        packets_layout = (RedPacketsLayout) findViewById(R.id.packets_layout);
        ll_list_layout = (LinearLayout) findViewById(R.id.ll_list_layout);
        ll_close_layout = (LinearLayout) findViewById(R.id.ll_close_layout);
        ll_add_coupon_item = (LinearLayout) findViewById(R.id.ll_add_coupon_item);
        tv_choose_coupon = (TextView) findViewById(R.id.tv_choose_coupon);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        getWindow().setAttributes(attributes);
        setCancelable(false);
    }

    @Override
    protected void initData() {

//        packets_layout.post(new Runnable() {
//            @Override
//            public void run() {
//                packets_layout.startRain();
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        packets_layout.stopRain();
//                    }
//                }, 2000);
//            }
//        });

        tv_choose_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkAvailable(context)) {
                    if (!TextUtil.isEmpty(userCouponPLUrl)) {
                        Intent intent = new Intent(context, CouponWebviewActivity.class);
                        intent.putExtra("uri", userCouponPLUrl);
                        context.startActivity(intent);
                        dismiss();
                    } else {
                    }
                } else {
                    ToastUtils.showToast(context, R.string.warm_net);
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        updateshopinfo();
    }

    private void updateshopinfo() {
        if (null != couponList) {
            int size = couponList.size();
            ll_add_coupon_item.removeAllViews();
            for (int i = 0; i < size; i++) {
                View view = View.inflate(context, R.layout.inner_item_coupon, null);
                TextView tv_dUseRangeName = (TextView) view.findViewById(R.id.tv_dUseRangeName);
                TextView tv_CouponValue = (TextView) view.findViewById(R.id.tv_CouponValue);
                TextView tv_EndTime = (TextView) view.findViewById(R.id.tv_EndTime);
                TextView tv_ConditionValue = (TextView) view.findViewById(R.id.tv_ConditionValue);
                tv_dUseRangeName.setText(couponList.get(i).getUseRangeName());
                tv_CouponValue.setText(couponList.get(i).getCouponValue());
                tv_EndTime.setText(couponList.get(i).getEndTime());
                tv_ConditionValue.setText(couponList.get(i).getConditionValue());
                ll_add_coupon_item.addView(view);
            }
        }
    }

}
