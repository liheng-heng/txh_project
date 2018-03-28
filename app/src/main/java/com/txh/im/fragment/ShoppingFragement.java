package com.txh.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.util.NetUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.MainActivity;
import com.txh.im.activity.MyOrderActivity;
import com.txh.im.activity.QRCodeFriendsAcitivty;
import com.txh.im.activity.SearchMerchantActivity;
import com.txh.im.activity.ShopCarAcitivity;
import com.txh.im.adapter.ShoppingAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.CartShelfNumBean;
import com.txh.im.bean.CouponBean;
import com.txh.im.bean.GetAskNotReadCountBean;
import com.txh.im.bean.MainButtonBean;
import com.txh.im.bean.ShopTrolleyBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.utils.Utils;
import com.txh.im.widget.RedDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiajia on 2017/6/7.
 * 商城首页改版,促销
 */

public class ShoppingFragement extends BaseFragment {

    @Bind(R.id.myrecycleview)
    RecyclerView my_merchant_rv;
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.iv_headright)
    ImageView iv_headRight;
    @Bind(R.id.tv_search_content)
    TextView tvSearchContent;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.rl_sv)
    RelativeLayout rlSv;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.ll_nodata)
    LinearLayout llNodata;
    @Bind(R.id.ll_neterror_layout)
    LinearLayout llNeterrorLayout;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.fl_goods)
    FrameLayout flGoods;
    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
    @Bind(R.id.srl)
    PullToRefreshLayout srl;

    private ShoppingAdapter adapter;
    private List<ShopTrolleyBean.ShopListBean> list = new ArrayList<>();

    private int page = 1;
    private MainActivity activity;
    private List<MainButtonBean> listBean;
    List<CouponBean.CouponListBean> couponList;
    String userCouponPLUrl;
    String unitId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        listBean = activity.getListBean();
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_shopping_trolley, null);
        return view;
    }

    @Override
    protected void initTitle() {
        if (listBean != null && listBean.size() > 0) {
            for (int i = 0; i < listBean.size(); i++) {
                if (listBean.get(i).getMenuCode().equals("ShopMall")) {
                    headTitle.setText(listBean.get(i).getTitle());
                }
            }
        }

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ivHeadBack.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        linearParams.width = UIUtils.dip2px(18);
        linearParams.height = UIUtils.dip2px(18);
        ivHeadBack.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        ivHeadBack.setBackground(getResources().getDrawable(R.drawable.mall_home_sweep));
        iv_headRight.setVisibility(View.VISIBLE);
        iv_headRight.setBackground(getResources().getDrawable(R.drawable.home_order));
        tvSearchContent.setText(mContext.getResources().getString(R.string.merchant_search));
        my_merchant_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        my_merchant_rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new ShoppingAdapter(mContext, list, 1);
        my_merchant_rv.setAdapter(adapter);

        srl.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                // 结束刷新
                if (NetUtils.hasNetwork(mContext)) {
                    // 结束刷新
                    HttpData(1, 1, 1);
                    page = 1;
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            srl.finishRefresh();
                        }
                    }, 200);//无网的时候给个效果就好了
                }
            }

            @Override
            public void loadMore() {
                //加载更多
                page++;
                HttpData(page, 2, 2);
            }
        });

        unitId = GlobalApplication.getInstance().getPerson(mContext).getUnitId();
        String saveData = (String) SPUtil.get(mContext, unitId + "ShoppingFragement", "");
        if (!TextUtil.isEmpty(saveData)) {
            showSaveData(saveData);
        }
    }

    @Override
    protected void initData() {
        HttpData(1, 1, 0);
        page = 1;
        getUnreadCountAskMessage();
        /**
         * 获取商城优惠列表--广告
         */
        HttpCouponData();
        tvSearchContent.moveCursorToVisibleOffset();
    }

    private void showSaveData(String basebean) {
        if (ShoppingFragement.this.isDestroy) {
            return;
        }
        Gson gson = new Gson();
        Basebean<ShopTrolleyBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopTrolleyBean>>() {
        }.getType());
        if (bean.getStatus().equals("200")) {
            if (llNodata != null) {
                llNodata.setVisibility(View.GONE);
            }
            ShopTrolleyBean shopTrolleyBean = bean.getObj();
            if (shopTrolleyBean != null) {
                List<ShopTrolleyBean.ShopListBean> shopList = shopTrolleyBean.getShopList();
                if (null != shopList) {
                    if (shopList.size() == 0) {
                        llNodata.setVisibility(View.VISIBLE);
                    } else {
                        llNodata.setVisibility(View.GONE);
                        adapter.RereshData(shopList);
                    }
                }
            } else {
                tvGoodsNum.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void HttpCouponData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ActionVersion", AllConstant.ActionVersionFour);
        hashMap.put("IsActionTest", AllConstant.ActionTestFour);
        new GetNetUtil(hashMap, Api.Coupon_GetRemindCouponList, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (ShoppingFragement.this.isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<CouponBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<CouponBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    userCouponPLUrl = bean.getObj().getUserCouponPLUrl();
                    couponList = bean.getObj().getCouponList();
                    if (null != couponList && couponList.size() > 0) {
                        new RedDialog(mContext, couponList, userCouponPLUrl).show();
                    }
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }


    /**
     * 获取商城首页数据
     *
     * @param page
     * @param type
     */
    private void HttpData(int page, final int type, final int refreshtype) {
        //refreshtype  0 第一次或者点击条目  1 下拉刷新  2  上滑加载
        /**
         * 网络可用
         */
        if (Utils.isNetworkAvailable(mContext)) {
            llNeterrorLayout.setVisibility(View.GONE);
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("PageSize", "0");
            hashmap.put("PageIndex", page + "");
            hashmap.put("ShopItemCount", "0");
            hashmap.put("PageType", "1");
            hashmap.put("IsReturnCartNum", "1");
            hashmap.put("IsReturnShelfNum", "1");
            hashmap.put("ActionVersion", AllConstant.ActionVersion);
            hashmap.put("IsActionTest", AllConstant.IsActionTest);
            new GetNetUtil(hashmap, Api.Mall_SearchDataForMallFPage, mContext) {

                @Override
                public void errorHandle() {
                    super.errorHandle();
                    if (refreshtype == 1) {
                        srl.finishRefresh();
                    }
                    if (refreshtype == 2) {
                        srl.finishLoadMore();
                    }
                }

                @Override
                public void successHandle(String basebean) {
                    if (refreshtype == 1) {
                        srl.finishRefresh();
                    }
                    if (refreshtype == 2) {
                        srl.finishLoadMore();
                    }
                    if (ShoppingFragement.this.isDestroy) {
                        return;
                    }
                    Log.i("----------->", basebean);
                    Gson gson = new Gson();
                    Basebean<ShopTrolleyBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ShopTrolleyBean>>() {
                    }.getType());
                    if (bean.getStatus().equals("200")) {
                        if (llNodata != null) {
                            llNodata.setVisibility(View.GONE);
                        }


                        ShopTrolleyBean shopTrolleyBean = bean.getObj();
                        if (shopTrolleyBean != null) {
                            List<ShopTrolleyBean.ShopListBean> shopList = shopTrolleyBean.getShopList();
                            switch (type) {
                                case 1:
                                    if (shopList.size() == 0) {
                                        llNodata.setVisibility(View.VISIBLE);
                                    } else {
                                        llNodata.setVisibility(View.GONE);
                                        adapter.RereshData(shopList);
                                    }
                                    SPUtil.remove(mContext, unitId + "ShoppingFragement");
                                    SPUtil.putAndApply(mContext, unitId + "ShoppingFragement", basebean);
                                    break;

                                case 2:
                                    if (shopList.size() > 0) {
                                        adapter.loadData(shopList);
                                    } else {
                                    }
                                    break;
                            }

                            if (!TextUtil.isEmpty(shopTrolleyBean.getCartItemNum())) {
                                tvGoodsNum.setVisibility(View.VISIBLE);
                                if (Integer.parseInt(shopTrolleyBean.getCartItemNum()) > 99) {
                                    tvGoodsNum.setText("99+");
                                } else {
                                    if (Integer.parseInt(shopTrolleyBean.getCartItemNum()) == 0) {
                                        tvGoodsNum.setVisibility(View.INVISIBLE);
                                    } else {
                                        tvGoodsNum.setText(shopTrolleyBean.getCartItemNum());
                                    }
                                }
                            }
                        } else {
                            tvGoodsNum.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        llNodata.setVisibility(View.VISIBLE);
                        ToastUtils.showToast(mContext, bean.getMsg());
                    }
                }
            };
        } else {
            /**
             * 网络不可用
             */
            llNeterrorLayout.setVisibility(View.VISIBLE);
            llNodata.setVisibility(View.GONE);
            tvGoodsNum.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.ll_head_right, R.id.rl_sv, R.id.ll_neterror_layout, R.id.fl_goods, R.id.ll_head_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_right:
                /**
                 * 我的订单
                 * 情况一 ：买家进来的时候，直接跳转进入我的订单界面
                 * 情况二 ：卖家进来的时候，请求网络，获取各个店铺的订单，获取shopid进行传值请求
                 */
                String unitType = GlobalApplication.getInstance().getPerson(mContext).getUnitType();
                if (unitType.equals("1")) {
                    Intent intent = new Intent(mContext, MyOrderActivity.class);
                    startActivity(intent);
                } else if (unitType.equals("2")) {
                } else {
                }
                break;

            case R.id.rl_sv:
                startActivity(new Intent(mContext, SearchMerchantActivity.class));
                break;

            case R.id.ll_neterror_layout:
                HttpData(1, 1, 0);
                page = 1;
                break;

            case R.id.fl_goods:
                startActivity(new Intent(mContext, ShopCarAcitivity.class));
                break;

            case R.id.ll_head_back:
                if (hasCameraPermission()) {
                    Intent intent = new Intent(mContext, QRCodeFriendsAcitivty.class);
                    startActivity(intent);
                } else {
                    applyCameraPersion();
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        hashMap.put("IsReplaceOrder", "0");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        hashMap.put("IsActionTest", AllConstant.IsActionTest);
        new GetNetUtil(hashMap, Api.Mall_GetLatestCartShelfNum, mContext) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (ShoppingFragement.this.isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<CartShelfNumBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<CartShelfNumBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    if (!TextUtil.isEmpty(bean.getObj().getCartItemNum())) {
                        tvGoodsNum.setVisibility(View.VISIBLE);
                        if (Integer.parseInt(bean.getObj().getCartItemNum()) > 99) {
                            tvGoodsNum.setText("99+");
                        } else {
                            if (Integer.parseInt(bean.getObj().getCartItemNum()) == 0) {
                                tvGoodsNum.setVisibility(View.INVISIBLE);
                            } else {
                                tvGoodsNum.setText(bean.getObj().getCartItemNum());
                            }
                        }
                    }
                }
            }
        };
    }

    private void getUnreadCountAskMessage() {
        new GetNetUtil(null, Api.TX_App_SNS_GetAskNotReadCount, mContext) {
            @Override
            public void successHandle(String base) {
                if (ShoppingFragement.this.isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                GetAskNotReadCountBean basebean = gson.fromJson(base, GetAskNotReadCountBean.class);
                int obj = basebean.getObj();
                if (basebean.getStatus().equals("200")) {
                    if (obj > 0) {
                        activity.getNum(obj, "FriendList");
                    }
                }
            }
        };
    }
}



