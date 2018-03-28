package com.txh.im.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.ChangeTypeActivity;
import com.txh.im.activity.CouponWebviewActivity;
import com.txh.im.activity.GetShopListForOrderActivity;
import com.txh.im.activity.GoodsAddressActivity;
import com.txh.im.activity.MainActivity;
import com.txh.im.activity.MyInfoDetailActivity;
import com.txh.im.activity.MyOrderActivity;
import com.txh.im.activity.PromotionWebviewActivity;
import com.txh.im.activity.QrWebViewAcitivty;
import com.txh.im.activity.QuoteAcitivty;
import com.txh.im.activity.SettingActivity;
import com.txh.im.activity.WebAppShareAcitivty;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.GetShopListItemBean;
import com.txh.im.bean.PersonalCenterBean;
import com.txh.im.listener.ShopCarMoneyOrNum;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonalCenterAdapter extends BaseListAdapter<PersonalCenterBean.ListItemBean> {

    int val = UIUtils.dip2px(20);
    private ShopCarMoneyOrNum sh;
    private AlertDialog mAlertDialog = null;
    private boolean isChanged = false;
    private MainActivity activity;
    private boolean isChangedUnittype = false;
    private String shopInfoIsOK;
    List<GetShopListItemBean> listBean = new ArrayList<>();

    public PersonalCenterAdapter(Context context, List<PersonalCenterBean.ListItemBean> mDatas, int ItemLayoutId, ShopCarMoneyOrNum sh, MainActivity activity) {
        super(context, mDatas, ItemLayoutId);
        this.sh = sh;
        this.activity = activity;

        if (!TextUtil.isEmpty(GlobalApplication.getInstance().getPerson(mContext).getUnitType()) &&
                GlobalApplication.getInstance().getPerson(mContext).getUnitType().equals("2")) {
        }
    }

    @Override
    public void convert(final ViewHolder helper, final PersonalCenterBean.ListItemBean item, int position) {
        if (!TextUtil.isEmpty(item.getItemIcon())) {
            Picasso.with(mContext).load(item.getItemIcon()).resize(val, val)
                    .placeholder(R.drawable.wode).into((ImageView) helper.getView(R.id.iv_left_redpoint));
        }
        helper.setText(R.id.tv_left, item.getItemTitle());
        helper.setText(R.id.tv_right, item.getItemText());
        if (!TextUtil.isEmpty(item.getTextColor())) {
            int color_int = Color.parseColor(item.getTextColor());
            helper.setTextColor(R.id.tv_right, color_int);
        }

        /**
         * 身份切换
         */
        if (item.getItemType().equals("7")) {
        } else if (item.getItemType().equals("3")) {
            helper.setBackgroundRes(R.id.iv_right, R.drawable.content_return_arrow);
            helper.setVisible(R.id.tv_right, true);
            helper.setTextSize(R.id.tv_right, 10);
        } else {
            helper.setVisible(R.id.tv_right, false);
            helper.setBackgroundRes(R.id.iv_right, R.drawable.content_return_arrow);
        }

        helper.setOnClickListener(R.id.rl_layout, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent;

                switch (item.getItemCode()) {
                    //我的订单
                    case "MyOrder":
                        String unitType = GlobalApplication.getInstance().getPerson(mContext).getUnitType();
                        if (unitType.equals("1")) {
                            intent = new Intent(mContext, MyOrderActivity.class);
                            mContext.startActivity(intent);
                        }
                        if (unitType.equals("2")) {
                            HttpData();
//                            if (listBean != null) {
//                                if (listBean.size() == 1) {
//                                    Log.i("----------->>>>", "size等于1");
//                                    intent = new Intent(mContext, MyOrderActivity.class);
//                                    intent.putExtra("ShopId", listBean.get(0).getShopId());
//                                    mContext.startActivity(intent);
//                                } else {
//                                    Log.i("----------->>>>", "size不等于1");
//                                    intent = new Intent(mContext, GetShopListForOrderActivity.class);
//                                    mContext.startActivity(intent);
//                                }
//                            }
                        }
                        break;

                    case "MyInfo":
                        //我的资料
                        intent = new Intent(mContext, MyInfoDetailActivity.class);
                        mContext.startActivity(intent);
                        break;

                    case "IdentityCheck":
                        //身份认证
                        break;

                    case "ChangeLoginPwd":
                        //重置密码
//                        ToastUtils.showToast(mContext, "重置密码");
//                        Intent intent2 = new Intent(mContext, PasswordResetActivity.class);
//                        mContext.startActivity(intent2);
                        break;

                    case "GoodsManage":
                        //商品管理
//                        ToastUtils.showToast(mContext, "商品管理");
                        break;

                    case "AppSet":
                        //设置
                        intent = new Intent(mContext, SettingActivity.class);
                        mContext.startActivity(intent);
                        break;

                    case "DeliveryAddress":
                        //收货地址
                        intent = new Intent(mContext, GoodsAddressActivity.class);
                        mContext.startActivity(intent);
                        break;

                    case "ChangeUnitType":
                        intent = new Intent(mContext, ChangeTypeActivity.class);
                        mContext.startActivity(intent);
                        //身份切换
                        break;

                    //app下载
                    case "AppDownload":
                        intent = new Intent(mContext, QrWebViewAcitivty.class);
                        intent.putExtra("app_qr", item.getItemValue());
                        mContext.startActivity(intent);
                        break;

                    //报价单管理
                    case "PriceGroupSet":
                        intent = new Intent(mContext, QuoteAcitivty.class);
                        mContext.startActivity(intent);
                        break;

                    //商城管理
                    case "MallManage":
                        if (Utils.isNetworkAvailable(mContext)) {
                            intent = new Intent(mContext, PromotionWebviewActivity.class);
                            intent.putExtra("uri", item.getItemValue());
                            mContext.startActivity(intent);
                        } else {
                            ToastUtils.showToast(mContext, R.string.warm_net);
                        }
                        break;
                    case "AppShare"://分享
                        intent = new Intent(mContext, WebAppShareAcitivty.class);
                        intent.putExtra("app_share", item.getItemValue());
                        mContext.startActivity(intent);
                        break;
                    //优惠券
                    case "MyCoupon":
                        if (Utils.isNetworkAvailable(mContext)) {
                            intent = new Intent(mContext, CouponWebviewActivity.class);
                            intent.putExtra("uri", item.getItemValue());
                            mContext.startActivity(intent);
                        } else {
                            ToastUtils.showToast(mContext, R.string.warm_net);
                        }
                        break;
                }
            }
        });
    }

    /**
     * 获取可以查看订单的店铺
     */
    private void HttpData() {
        HashMap<String, String> map = new HashMap<>();
        new GetNetUtil(map, Api.Mall_GetShopListOfUser, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String base) {
                Log.i("----------->", base);
                Gson gson = new Gson();

                BaseListBean<GetShopListItemBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<GetShopListItemBean>>() {
                }.getType());

                if (basebean.getStatus().equals("200")) {
                    listBean = basebean.getObj();
                    /**
                     * 已优化，一进来直接请求数据
                     */
                    if (listBean != null) {
                        Log.i("----------->>>>", "请求数据---listBean不为空");
                        if (listBean.size() == 1) {
                            Intent intent = new Intent(mContext, MyOrderActivity.class);
                            intent.putExtra("ShopId", listBean.get(0).getShopId());
                            mContext.startActivity(intent);
                        } else {
                            Intent intent0 = new Intent(mContext, GetShopListForOrderActivity.class);
                            mContext.startActivity(intent0);
                        }
                    }
                } else {
                    ToastUtils.showToast(mContext, basebean.getMsg());
                }
            }
        };
    }


    /**
     * 卖家切换为买家
     * <p>
     * 身份切换请求
     */

    private ProgressDialog mProgressDialog;

    protected void showProgress(String showstr) {
        mProgressDialog = ProgressDialog.show(mContext, "", showstr);
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void refareshData(List<PersonalCenterBean.ListItemBean> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }
}
