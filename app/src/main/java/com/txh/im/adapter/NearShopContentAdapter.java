package com.txh.im.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.FriendsDataAddWebviewActivity;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.HttpBean;
import com.txh.im.bean.NearShopListBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.widget.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NearShopContentAdapter extends RecyclerView.Adapter<NearShopContentAdapter.Holder> {

    private List<NearShopListBean> list = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private int val = UIUtils.dip2px(40);
    private AlertDialog mAlertDialog = null;
    private Activity activity;
    Gson g = new Gson();

    public NearShopContentAdapter(final Context context, List<NearShopListBean> list) {
        this.context = context;
        this.activity = (Activity) context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View m_View = inflater.inflate(R.layout.item_near_content, parent, false);
        Holder hodler = new Holder(m_View);
        return hodler;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

//                "UserId":"1",
//                "UnitId":"56",
//                "ShopName":"哇哈哈",
//                "Head":"http://www.baidu.com/jgi.jpg",
//                "Distance":"200",  //距离（米）

        holder.tv_name.setText(list.get(position).getShopName());
        holder.tv_distance.setText(list.get(position).getDistance()+list.get(position).getUnit());
        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpAddFriend(list.get(position).getUnitId(), list.get(position).getUserId());
            }
        });

        if (!TextUtil.isEmpty(list.get(position).getHead())) {
            Picasso.with(context).load(list.get(position).getHead()).resize(val, val).transform(
                    new RoundedTransformation(3, 0)).placeholder(R.drawable.wode).into(holder.iv_image);
        } else {
            Picasso.with(context).load(R.drawable.wode).resize(val, val).transform(
                    new RoundedTransformation(3, 0)).into(holder.iv_image);
        }

        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, FriendsDataAddWebviewActivity.class);
//                ToUnitId  商户UnitId,     Distance: 距离
                intent.putExtra("ToUnitId", list.get(position).getUnitId());
                intent.putExtra("Distance", list.get(position).getDistance());
                intent.putExtra("intenttype", "1");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_distance, tv_add;
        private CircleImageView iv_image;
        private LinearLayout rl_layout;

        public Holder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            tv_add = (TextView) itemView.findViewById(R.id.tv_add);
            iv_image = (CircleImageView) itemView.findViewById(R.id.iv_image);
            rl_layout = (LinearLayout) itemView.findViewById(R.id.rl_layout);
        }
    }

    /**
     * 添加好友
     *
     * @param unitId
     * @param userId
     */
    private void HttpAddFriend(String unitId, String userId) {

        HashMap<String, String> map = new HashMap<>();
        map.put("AddUserId",userId);
        map.put("AddUnitId", unitId);
        map.put("Channel", "2");

        new GetNetUtil(map, Api.TX_App_SNS_AddFriend, context) {
            @Override
            public void successHandle(String base) {

                HttpBean basebean = g.fromJson(base, HttpBean.class);
                if (basebean.getStatus().equals("200")) {
                    toast(basebean.getMsg());
                } else {
                    toast(basebean.getMsg());
                }
            }
        };

    }


    protected void toast(String msg) {

        ToastUtils.toast(context, msg);

    }

    public void RereshData(List<NearShopListBean> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    public void loadData(List<NearShopListBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void Clear() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
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

    /**
     * 展示已经提醒过发货弹窗--每天只能提醒一次
     */
    private void showAlertDialog() {
        mAlertDialog = new AlertDialog.Builder(context).create();
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(R.layout.dialog_callto_send_goods);
        mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
