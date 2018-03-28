package com.txh.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.R;
import com.txh.im.adapter.CouponListItemAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.OrdersCommitBean;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 优惠券列表
 * Created by liheng on 2017/6/26.
 */

public class CouponListActivity extends BasicActivity {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.ll_listview)
    ListView llListview;
    @Bind(R.id.tv_add_coupon)
    TextView tvAddCoupon;

    private String OrdersCommitBean_str;
    List<OrdersCommitBean.CouponInfoBean.CouponListBean> couponList = new ArrayList<>();
    CouponListItemAdapter adapter;
    private String callBackcouponId;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_coupon_list);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("优惠券");
        OrdersCommitBean_str = getIntent().getStringExtra("OrdersCommitBean_str");
        callBackcouponId = getIntent().getStringExtra("callBackcouponId");
        llHeadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        adapter = new CouponListItemAdapter(CouponListActivity.this, couponList);
        llListview.setAdapter(adapter);
        HttpData();
    }

    private void HttpData() {
        Gson gson = new Gson();
        Basebean<OrdersCommitBean> bean = gson.fromJson(OrdersCommitBean_str, new TypeToken<Basebean<OrdersCommitBean>>() {
        }.getType());
        if (bean.getStatus().equals("200")) {
            couponList = bean.getObj().getCouponInfo().getCouponList();
            if (!TextUtil.isEmpty(callBackcouponId)) {
                for (int i = 0; i < couponList.size(); i++) {
                    if (callBackcouponId.contains(couponList.get(i).getCouponId())) {
                        couponList.get(i).setSelect(true);
                    }
                }
            }
            adapter.setRefrece(couponList);
        } else {
            ToastUtils.showToast(CouponListActivity.this, bean.getMsg());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private int selectAllCount = 0;
    private String ConditionValue;

    @OnClick(R.id.tv_add_coupon)
    public void onViewClicked() {
        selectAllCount = 0;
        List<String> selectedCouponIds = adapter.getSelectedCouponIds();
        List<Integer> selectedIntCoupon = adapter.getSelectedIntCoupon();
        Log.i("=========", "pinjieselectedCouponIds(selectedCouponIds)--------" +
                pinjieselectedCouponIds(selectedCouponIds));
        if (null == selectedIntCoupon || selectedIntCoupon.size() == 0) {

//            ToastUtils.toast(CouponListActivity.this, "请至少选择一张优惠券！");
//            return;

            Intent intent = new Intent();
            intent.putExtra("CouponId", "");
            intent.putExtra("CouponValue", "");
            intent.putExtra("ConditionValue", "");
            intent.putExtra("ConditionCouponListSize", couponList.size() + "");
            setResult(Activity.RESULT_OK, intent);
            finish();

        } else {
            for (int i = 0; i < selectedIntCoupon.size(); i++) {
                selectAllCount += selectedIntCoupon.get(i);
            }
            List<String> selectedConditionValue = adapter.getSelectedConditionValue();
            if (null != selectedConditionValue) {
                if (selectedConditionValue.size() == 1) {
                    ConditionValue = selectedConditionValue.get(0);
                } else {
                    ConditionValue = "已选" + selectedConditionValue.size() + "张";
                }
            }
            Intent intent = new Intent();
            intent.putExtra("CouponId", pinjieselectedCouponIds(selectedCouponIds));
            intent.putExtra("CouponValue", selectAllCount + "");
            intent.putExtra("ConditionValue", ConditionValue);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    public String pinjieselectedCouponIds(List<String> selectedCouponIds) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < selectedCouponIds.size(); i++) {
            if (i == selectedCouponIds.size()) {
                sb.append(selectedCouponIds.get(i));
            } else {
                sb.append(selectedCouponIds.get(i)).append(",");
            }
        }
        return sb.toString();
    }

}
