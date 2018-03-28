package com.txh.im.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.GetShopListForOrderActivity;
import com.txh.im.activity.GoodsAddressActivity;
import com.txh.im.activity.MainActivity;
import com.txh.im.activity.MyInfoDetailActivity;
import com.txh.im.activity.MyOrderActivity;
import com.txh.im.activity.SettingActivity;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.ChangeStatusBean;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.GetShopListItemBean;
import com.txh.im.bean.PersonalCenterBean;
import com.txh.im.listener.ShopCarMoneyOrNum;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;

import java.util.HashMap;
import java.util.List;

public class PersonalCenterAdapter2 extends BaseListAdapter<PersonalCenterBean.ListItemBean> {

    int val = UIUtils.dip2px(20);
    private ShopCarMoneyOrNum sh;
    private AlertDialog mAlertDialog = null;
    private boolean isChanged = false;
    private MainActivity activity;
    private boolean isChangedUnittype = false;
    private String shopInfoIsOK;

    public PersonalCenterAdapter2(Context context, List<PersonalCenterBean.ListItemBean> mDatas,
                                  int ItemLayoutId, ShopCarMoneyOrNum sh, MainActivity activity) {
        super(context, mDatas, ItemLayoutId);
        this.sh = sh;
        this.activity = activity;
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
            /**
             * 效果一
             */
//            helper.setBackgroundRes(R.id.iv_right, R.drawable.default_address_off);
//            helper.setOnClickListener(R.id.iv_right, new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    if (HttpChangeStatus()) {
//                        helper.setBackgroundRes(R.id.iv_right, R.drawable.default_address_open);
//                    }
//                }
//            });

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
                switch (item.getItemCode()) {
                    //我的订单
                    case "MyOrder":
                        String unitType = GlobalApplication.getInstance().getPerson(mContext).getUnitType();
                        if (unitType.equals("1")) {
                            Intent intent = new Intent(mContext, MyOrderActivity.class);
                            mContext.startActivity(intent);
                        }
                        if (unitType.equals("2")) {
                            HttpData();
                        }
                        break;

                    case "MyInfo":
                        //我的资料
                        Intent intent = new Intent(mContext, MyInfoDetailActivity.class);
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
                        Intent intent3 = new Intent(mContext, SettingActivity.class);
                        mContext.startActivity(intent3);
                        break;

                    case "DeliveryAddress":
                        //收货地址
                        Intent intent4 = new Intent(mContext, GoodsAddressActivity.class);
                        mContext.startActivity(intent4);
                        break;

                    case "ChangeUnitType":
//                        ToastUtils.showToast(mContext,"身份切换");
                        /**
                         * 根据 ShopInfoIsOK 判断弹窗界面展示
                         */
                        //1买家，2卖家
                        String unitType_get = GlobalApplication.getInstance().getPerson(mContext).getUnitType();
                        if (unitType_get.equals("1")) {
                            //买家
                            HttpChangeStatus();
                        } else {
                            //卖家
                            Show333To444Dialog();
                        }
                        break;
                }
            }
        });
    }

    //买家----切换为卖家
    private void Show444To333Dialog(final boolean InfoIsOK) {

        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(R.layout.dialog_buy_to_seller);
        TextView tv_desc1 = (TextView) mAlertDialog.getWindow().findViewById(R.id.tv_desc1);
        TextView tv_desc2 = (TextView) mAlertDialog.getWindow().findViewById(R.id.tv_desc2);
        TextView tv_ok = (TextView) mAlertDialog.getWindow().findViewById(R.id.tv_ok);
        if (InfoIsOK) {
            tv_desc1.setText("您已注册商户");
            tv_desc2.setText("将为您切换商户身份");
            tv_ok.setText("确 认");
        } else {
            tv_desc1.setText("您未注册商户");
            tv_desc2.setText("如继续切换 , 请先完善资料");
            tv_ok.setText("去完善");
        }
        mAlertDialog.getWindow().findViewById(R.id.ll_cancle_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                HttpChangeStatus2222();
            }
        });

        mAlertDialog.getWindow().findViewById(R.id.iv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                HttpChangeStatus2222();
            }
        });

        mAlertDialog.getWindow().findViewById(R.id.ll_ok_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InfoIsOK){
                    mAlertDialog.dismiss();
                    sh.getAllMoneyAndNum();
                }else {
                    mAlertDialog.dismiss();
                    //资料no ok
                    Intent intent = new Intent(mContext, MyInfoDetailActivity.class);
                    intent.putExtra("changeType", "babababb");
                    mContext.startActivity(intent);
//                    activity.finish();

                }
            }
        });

        mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InfoIsOK){
                    mAlertDialog.dismiss();
                    sh.getAllMoneyAndNum();
                }else {
                    mAlertDialog.dismiss();
                    //资料no ok
                    Intent intent = new Intent(mContext, MyInfoDetailActivity.class);
                    intent.putExtra("changeType", "babababb");
                    mContext.startActivity(intent);
//                    activity.finish();
                }
            }
        });


    }

    //卖家----切换为买家
    private void Show333To444Dialog() {

        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(R.layout.dialog_seller_to_buy);
        mAlertDialog.getWindow().findViewById(R.id.ll_cancle_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });

        mAlertDialog.getWindow().findViewById(R.id.iv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog.getWindow().findViewById(R.id.ll_ok_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                HttpChangeStatus2222();
            }
        });

        mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                HttpChangeStatus2222();
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
                    List<GetShopListItemBean> listBean = basebean.getObj();
                    if (listBean != null) {
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
     * 买家切换为卖家
     * <p>
     * 身份切换请求
     */
    private void HttpChangeStatus() {

        showProgress("正在加载");

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ActionVersion", "3.0");

        new GetNetUtil(hashMap, Api.GetInfo_ChangeUnitType, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String basebean) {
                closeProgress();
                Log.i("----------->", "身份切换-----" + basebean);
                Gson gson = new Gson();
                Basebean<ChangeStatusBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ChangeStatusBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    String unitId = bean.getObj().getUserInfo().getUnitId();
                    String unitType = bean.getObj().getUserInfo().getUnitType();
                    CryptonyBean cryptonyBean = new CryptonyBean();
                    if (!TextUtil.isEmpty(unitId)) {
                        cryptonyBean.setUnitId(unitId);
                    }
                    if (!TextUtil.isEmpty(unitType)) {
                        cryptonyBean.setUnitType(unitType);
                    }
                    GlobalApplication.getInstance().savePerson(cryptonyBean, mContext);

                    String shopInfoIsOK = bean.getObj().getUserInfo().getShopInfoIsOK();
                    if (!TextUtil.isEmpty(shopInfoIsOK)) {
                        SPUtil.putAndApply(mContext, "shopInfoIsOK", shopInfoIsOK);
                    }

                    /**
                     * 根据 ShopInfoIsOK 判断是否跳转编辑资料界面
                     */

                    if (unitType.equals("2") && bean.getObj().getUserInfo().getShopInfoIsOK().equals("0")) {
                        Show444To333Dialog(false);
                    } else {

                        Show444To333Dialog(true);

                        //资料ok
//                        sh.getAllMoneyAndNum();
                    }

/*
                    if (unitType.equals("2") && bean.getObj().getUserInfo().getShopInfoIsOK().equals("0")) {
                        mAlertDialog = new AlertDialog.Builder(mContext).create();
                        mAlertDialog.show();
                        mAlertDialog.getWindow().setContentView(R.layout.dialog_change_character);
                        mAlertDialog.getWindow().findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAlertDialog.dismiss();
                            }
                        });

                        mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAlertDialog.dismiss();
                                //资料no ok
                                Intent intent = new Intent(mContext, MyInfoDetailActivity.class);
                                intent.putExtra("changeType", "babababb");
                                mContext.startActivity(intent);
                                activity.finish();
                            }
                        });
                    } else {
                        //资料ok
                        sh.getAllMoneyAndNum();
                    }

                    */
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }


    /**
     * 卖家切换为买家
     * <p>
     * 身份切换请求
     */
    private void HttpChangeStatus2222() {

        showProgress("正在加载");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ActionVersion", "3.0");
        new GetNetUtil(hashMap, Api.GetInfo_ChangeUnitType, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String basebean) {
                closeProgress();
                Log.i("----------->", "身份切换-----" + basebean);
                Gson gson = new Gson();
                Basebean<ChangeStatusBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ChangeStatusBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    String unitId = bean.getObj().getUserInfo().getUnitId();
                    String unitType = bean.getObj().getUserInfo().getUnitType();
                    CryptonyBean cryptonyBean = new CryptonyBean();
                    if (!TextUtil.isEmpty(unitId)) {
                        cryptonyBean.setUnitId(unitId);
                    }
                    if (!TextUtil.isEmpty(unitType)) {
                        cryptonyBean.setUnitType(unitType);
                    }
                    GlobalApplication.getInstance().savePerson(cryptonyBean, mContext);
                    String shopInfoIsOK = bean.getObj().getUserInfo().getShopInfoIsOK();
                    if (!TextUtil.isEmpty(shopInfoIsOK)) {
                        SPUtil.putAndApply(mContext, "shopInfoIsOK", shopInfoIsOK);
                    }
                    sh.getAllMoneyAndNum();
                    /**
                     * 根据 ShopInfoIsOK 判断是否跳转编辑资料界面
                     */
//                    if (unitType.equals("2") && bean.getObj().getUserInfo().getShopInfoIsOK().equals("0")) {
//                        mAlertDialog = new AlertDialog.Builder(mContext).create();
//                        mAlertDialog.show();
//                        mAlertDialog.getWindow().setContentView(R.layout.dialog_change_character);
//                        mAlertDialog.getWindow().findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mAlertDialog.dismiss();
//                            }
//                        });
//
//                        mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mAlertDialog.dismiss();
//                                //资料no ok
//                                Intent intent = new Intent(mContext, MyInfoDetailActivity.class);
//                                intent.putExtra("changeType", "babababb");
//                                mContext.startActivity(intent);
//                                activity.finish();
//                            }
//                        });
//                    } else {
//                        //资料ok
//
//                    }
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }

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