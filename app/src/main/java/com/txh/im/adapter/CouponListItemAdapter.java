package com.txh.im.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txh.im.R;
import com.txh.im.activity.CouponListActivity;
import com.txh.im.bean.OrdersCommitBean;
import com.txh.im.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import static com.txh.im.utils.UIUtils.getResources;

public class CouponListItemAdapter extends BaseAdapter {

    private List<OrdersCommitBean.CouponInfoBean.CouponListBean> list;
    private CouponListActivity context;
    private LayoutInflater inflater;
    private AlertDialog mAlertDialog = null;

    public CouponListItemAdapter(CouponListActivity context, List<OrdersCommitBean.CouponInfoBean.CouponListBean> list) {
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
            convertView = inflater.inflate(R.layout.item_coupon_list, null);
            holder = new ViewHolder();
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_user_area = (TextView) convertView.findViewById(R.id.tv_user_area);
            holder.tv_coupon_detail = (TextView) convertView.findViewById(R.id.tv_coupon_detail);
            holder.tv_use_time = (TextView) convertView.findViewById(R.id.tv_use_time);
            holder.ll_item_layout = (LinearLayout) convertView.findViewById(R.id.ll_item_layout);
            holder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            holder.rl_all_coupon_layout = (RelativeLayout) convertView.findViewById(R.id.rl_all_coupon_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /**
         * CouponId : 122
         * CouponValue : 10
         * ConditionValue : 满300元可用
         * EndTime : 有效期至2017-08-30
         * UseRangeName : 全场可用
         */
        holder.tv_money.setText(list.get(position).getCouponValue());
        holder.tv_user_area.setText(list.get(position).getUseRangeName());
        holder.tv_coupon_detail.setText(list.get(position).getConditionValue());
        holder.tv_use_time.setText(list.get(position).getEndTime());

        if (list.get(position).getSelect()) {
            holder.iv_select.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_address));
        } else {
            holder.iv_select.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_address_box));
        }

        holder.rl_all_coupon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getSelect()) {
                    list.get(position).setSelect(false);
                } else {
                    list.get(position).setSelect(true);
                }
                notifyDataSetChanged();
            }
        });

//        holder.ll_item_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ToastUtils.toast(context, list.get(position).getConditionValue());
//                Intent intent = new Intent();
//                intent.putExtra("CouponId", list.get(position).getCouponId());
//                intent.putExtra("CouponValue", list.get(position).getCouponValue());
//                intent.putExtra("ConditionValue", list.get(position).getConditionValue());
//                context.setResult(Activity.RESULT_OK, intent);
//                context.finish();
//            }
//        });

        return convertView;
    }

    static class ViewHolder {
        private TextView tv_money, tv_user_area, tv_coupon_detail, tv_use_time;
        private LinearLayout ll_item_layout;
        private ImageView iv_select;
        private RelativeLayout rl_all_coupon_layout;
    }

    /**
     * 获取选中的CouponIds集合
     *
     * @return
     */
    public List<String> getSelectedCouponIds() {
        List<String> li = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSelect()) {
                li.add(list.get(i).getCouponId() + "~" + list.get(i).getShopId());
            }
        }
        return li;
    }

    /**
     * ConditionValue : 满300元可用
     * 获取选中的ConditionValue集合
     *
     * @return
     */
    public List<String> getSelectedConditionValue() {
        List<String> li = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSelect()) {
                li.add(list.get(i).getConditionValue());
            }
        }
        return li;
    }

    /**
     * 获取选中的优惠券价格的集合
     *
     * @return
     */
    public List<Integer> getSelectedIntCoupon() {
        List<Integer> list_int = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSelect()) {
                String couponValue = list.get(i).getCouponValue();
                if (!TextUtil.isEmpty(couponValue)) {
                    list_int.add(Integer.parseInt(couponValue));
                }
            }
        }
        return list_int;
    }

    public void Clear() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public void setDataType(List<OrdersCommitBean.CouponInfoBean.CouponListBean> Dates) {
        this.list.addAll(Dates);
        notifyDataSetChanged();
    }

    public void setRefrece(List<OrdersCommitBean.CouponInfoBean.CouponListBean> Dates) {
        this.list.clear();
        this.list.addAll(Dates);
        notifyDataSetChanged();
    }
}
