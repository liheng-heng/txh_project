package com.txh.im.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.GoodsAddressActivity;
import com.txh.im.activity.LoginActivity_new;
import com.txh.im.activity.SettingActivity;
import com.txh.im.activity.UpdateAddressActivity;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.GoodsAddressBean;
import com.txh.im.utils.CustomDialog;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.txh.im.utils.UIUtils.getResources;

public class GoodsAddressAdapter extends BaseAdapter {

    private ArrayList<GoodsAddressBean> list;
    private Context context;
    private LayoutInflater inflater;
    String OrderAddress;
    List<String> list_delete_address_ids = new ArrayList<>();
    private String userId;

    public GoodsAddressAdapter(Context context, ArrayList<GoodsAddressBean> list, String OrderAddress, String userId) {
        this.context = context;
        this.list = list;
        this.userId = userId;
        this.OrderAddress = OrderAddress;
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
            convertView = inflater.inflate(R.layout.item_goods_address, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_detail_address = (TextView) convertView.findViewById(R.id.tv_detail_address);
            holder.iv_default = (ImageView) convertView.findViewById(R.id.iv_default);
            holder.ll_default = (LinearLayout) convertView.findViewById(R.id.ll_default);
            holder.ll_delete = (LinearLayout) convertView.findViewById(R.id.ll_delete);
            holder.ll_edit = (LinearLayout) convertView.findViewById(R.id.ll_edit);
            holder.ll_choose_address = (LinearLayout) convertView.findViewById(R.id.ll_choose_address);
            holder.iv_choose_address = (ImageView) convertView.findViewById(R.id.iv_choose_address);
            holder.ll_choose_address2 = (LinearLayout) convertView.findViewById(R.id.ll_choose_address2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(list.get(position).getName());
        holder.tv_phone.setText(list.get(position).getPhone());
        holder.tv_address.setText(list.get(position).getProvinceCityZoneText());
        holder.tv_detail_address.setText(list.get(position).getReceiveAddress());
        if (null != list.get(position).getIsDefault() && list.get(position).getIsDefault().equals("1")) {
            holder.iv_default.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_address));
        } else {
            holder.iv_default.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_address_box));
        }

        holder.ll_choose_address2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!TextUtil.isEmpty(OrderAddress)) {
                    if (Activity.class.isInstance(context)) {
                        Intent intent = new Intent();
                        intent.putExtra("AddressId", list.get(position).getAddressId());
                        intent.putExtra("Name", list.get(position).getName());
                        intent.putExtra("Phone", list.get(position).getPhone());
                        intent.putExtra("Address", list.get(position).getProvinceCityZoneText() + list.get(position).getReceiveAddress());
                        // 转化为activity，然后finish就行了
                        Activity activity = (Activity) context;
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    }
                }
            }
        });

        holder.ll_default.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (list.get(position).getIsDefault().equals("0")) {
                            setDefault(list.get(position).getAddressId(),
                                    list.get(position).getName(),
                                    list.get(position).getPhone(),
                                    list.get(position).getProvinceCityZoneText() + list.get(position).getReceiveAddress()
                            );
                        }
                    }
                }
        );

        holder.ll_edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UpdateAddressActivity.class);
                        intent.putExtra("addressid", list.get(position).getAddressId());
                        intent.putExtra("isAdd", "1");
                        intent.putExtra("IsSelf", list.get(position).getIsSelf());
                        intent.putExtra("userId", userId);
                        ((GoodsAddressActivity) context).startActivityForResult(intent, 101);
                    }
                }
        );

        holder.ll_delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomDialog.Builder builder = new CustomDialog.Builder(context);
                        builder.setMessage("是否删除地址？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                setDelete(list.get(position).getAddressId());
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();

//                        mAlertDialog = new AlertDialog.Builder(context).create();
//                        mAlertDialog.show();
//                        mAlertDialog.getWindow().setContentView(R.layout.dialog_delete);
//                        mAlertDialog.getWindow().findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mAlertDialog.dismiss();
//                            }
//                        });
//                        mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mAlertDialog.dismiss();
//                                setDelete(list.get(position).getAddressId());
//                            }
//                        });

                    }
                }
        );
        return convertView;
    }

    static class ViewHolder {
        private TextView tv_name, tv_phone, tv_address, tv_detail_address;
        private LinearLayout ll_default, ll_delete, ll_edit, ll_choose_address, ll_choose_address2;
        private ImageView iv_default, iv_choose_address;
    }

    /**
     * 设置默认收货地址
     *
     * @param addressid
     */

    private void setDefault(final String addressid, final String name, final String phone, final String address) {
        showProgress("正在加载");
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("AddressId", addressid);
        hashmap.put("IsReturnList", "1");
        hashmap.put("BUserId", userId);

        new GetNetUtil(hashmap, Api.GetInfo_SetUAddressDefaultById, context) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (context == null) {
                    return;
                }
                closeProgress();
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                BaseListBean<GoodsAddressBean> bean = gson.fromJson(basebean, new TypeToken<BaseListBean<GoodsAddressBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    setRefrece(bean.getObj());
                    if (!TextUtil.isEmpty(OrderAddress)) {
                        if (Activity.class.isInstance(context)) {
                            Intent intent = new Intent();
                            intent.putExtra("AddressId", addressid);
                            intent.putExtra("Name", name);
                            intent.putExtra("Phone", phone);
                            intent.putExtra("Address", address);
                            // 转化为activity，然后finish就行了
                            Activity activity = (Activity) context;
                            activity.setResult(Activity.RESULT_OK, intent);
                            activity.finish();
                        }
                    }
                } else {
                    ToastUtils.showToast(context, bean.getMsg());
                }
            }
        };
    }

    /**
     * 删除收货地址
     *
     * @param addressid
     */
    private void setDelete(final String addressid) {
        showProgress("正在加载");
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("AddressId", addressid);
        hashmap.put("IsReturnList", "1");

        new GetNetUtil(hashmap, Api.GetInfo_DeleteUAddressById, context) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (context == null) {
                    return;
                }
                Log.i("----------->", basebean);
                closeProgress();
                Gson gson = new Gson();
                BaseListBean<GoodsAddressBean> bean = gson.fromJson(basebean, new TypeToken<BaseListBean<GoodsAddressBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    list_delete_address_ids.add(addressid + "");
                    setRefrece(bean.getObj());
                } else {
                    ToastUtils.showToast(context, bean.getMsg());
                }
            }
        };
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

    public void Clear() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public void setDataType(List<GoodsAddressBean> Dates) {
        this.list.addAll(Dates);
        notifyDataSetChanged();
    }

    public void setRefrece(List<GoodsAddressBean> Dates) {
        this.list.clear();
        this.list.addAll(Dates);
        notifyDataSetChanged();
    }

    public List<String> getList_delete_address_ids() {
        return list_delete_address_ids;
    }

    public void setList_delete_address_ids(List<String> list_delete_address_ids) {
        this.list_delete_address_ids = list_delete_address_ids;
    }
}
