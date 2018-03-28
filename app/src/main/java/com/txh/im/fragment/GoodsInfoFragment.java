package com.txh.im.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.GoodsDetailActivity;
import com.txh.im.activity.ShopCarAcitivity;
import com.txh.im.android.AllConstant;
import com.txh.im.bean.AddGoodsToCartBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.CartShelfNumBean;
import com.txh.im.bean.GoodsDetailBean;
import com.txh.im.listener.AvNumListener;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.view.GlideImageLoader;
import com.txh.im.widget.AccountView;
import com.txh.im.widget.NoScrollViewPager;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsInfoFragment extends BasicFragment {

    public GoodsDetailActivity activity;
    public View rootView;
    public Context context;
    @Bind(R.id.ll_shop_detail)
    LinearLayout llShopDetail;
    @Bind(R.id.ll_goods_detail)
    LinearLayout llGoodsDetail;
    @Bind(R.id.rl_show_right_fragment)
    RelativeLayout rlShowRightFragment;
    @Bind(R.id.tv_goods_name)
    TextView tvGoodsName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_addtocart)
    TextView tvAddtocart;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.fl_goods)
    FrameLayout flGoods;
    @Bind(R.id.av_goods_num)
    AccountView avGoodsNum;
    @Bind(R.id.av_goods_num2)
    AccountView avGoodsNum2;
    @Bind(R.id.banner_layout)
    FrameLayout bannerLayout;
    @Bind(R.id.ll_buttom_layout)
    LinearLayout llButtomLayout;
    @Bind(R.id.tv_old_price)
    TextView tvOldPrice;
    @Bind(R.id.tv_discount)
    TextView tvDiscount;
    @Bind(R.id.iv_active_icon)
    ImageView ivActiveIcon;
    @Bind(R.id.tv_active_desc)
    TextView tvActiveDesc;
    @Bind(R.id.ll_layout_man)
    LinearLayout llLayoutMan;
    @Bind(R.id.view_man)
    View viewMan;

    private static final String TAG = "gkbn";
    List<String> strings = new ArrayList<>();
    private String SkuId;
    private String ShopId;
    private String activityId;
    private String ItemId;
    private String changed_num = "1";
    Banner banner;
    String activityTypeId = "";
    int val_active_weight = UIUtils.dip2px(20);
    int val_active_high = UIUtils.dip2px(20);

    @Override
    public View initView(LayoutInflater inflater) {
        rootView = inflater.inflate(R.layout.fragment_goods_info, null);
        banner = (Banner) rootView.findViewById(R.id.banner);
        return rootView;
    }

    @Subscribe
    @Override
    public void initTitle() {
        super.initTitle();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        if (!TextUtil.isEmpty(activity.getFormShopCar()) && activity.getFormShopCar().equals("formActiveCar")) {
            //代下单
            avGoodsNum.setVisibility(View.GONE);
            avGoodsNum2.setVisibility(View.VISIBLE);
            tvAddtocart.setClickable(false);
            tvAddtocart.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_gray_conner_3));
            avGoodsNum2.setAv_text("1");
            flGoods.setVisibility(View.GONE);
        } else {
            //非代下单
            avGoodsNum.setVisibility(View.VISIBLE);
            avGoodsNum2.setVisibility(View.GONE);
            tvAddtocart.setClickable(true);
            tvAddtocart.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_red_conner_3));
            flGoods.setVisibility(View.VISIBLE);
        }

        avGoodsNum.setAv_text("1");
        avGoodsNum.setAvNumListener(new AvNumListener() {
            @Override
            public void num(String number) {
                changed_num = number;
            }
        });

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        //取控件textView当前的布局参数
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) bannerLayout.getLayoutParams();
        linearParams.height = width;
        linearParams.width = width;
        bannerLayout.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>
    }

    @Subscribe
    public void onEventMainThread(GoodsDetailBean event) {
        if (event != null) {
            updateView(event);
        }
    }

    private void updateView(GoodsDetailBean event) {
        if (GoodsInfoFragment.this.isDestroy) {
            return;
        }
        /**
         * 展示商品名称 价格 以及库存
         */
        tvGoodsName.setText(event.getItemName());
        tvPrice.setText(event.getSPriceSign() + event.getSPrice());
        activityTypeId = event.getActivityTypeId();

        if (!TextUtil.isEmpty(event.getOldPrice())) {
            tvOldPrice.setText(event.getSPriceSign() + event.getOldPrice());
            tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (!TextUtil.isEmpty(event.getDiscount())) {
            tvDiscount.setText(event.getDiscount());
            tvDiscount.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_full_orange));
        }


        if (!TextUtil.isEmpty(event.getActivityRemarkIcon())) {
            Picasso.with(mContext).load(event.getActivityRemarkIcon())
                    .resize(val_active_weight, val_active_high).transform(
                    new RoundedTransformation(3, 0)).into(ivActiveIcon);
        }

        if (!TextUtil.isEmpty(event.getActivityRemark())) {
            tvActiveDesc.setText(event.getActivityRemark());
        }

        /**
         * 商品相关信息
         */
        List<GoodsDetailBean.ItemPropertyBean> itemProperty = event.getItemProperty();
        updategoodsinfo(itemProperty);
        /**
         * 店铺相关信息
         */
        List<GoodsDetailBean.ShopInfoBean> shopInfo = event.getShopInfo();
        updateshopinfo(shopInfo);
        /**
         * 轮播图展示
         */
        List<String> itemPics = event.getItemPics();
        updateitemPics(itemPics);
        /**
         * 展示购物车数量
         */
        tvGoodsNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(event.getCartItemNum()) > 99) {
            tvGoodsNum.setText("99+");
        } else {
            if (Integer.parseInt(event.getCartItemNum()) == 0) {
                tvGoodsNum.setVisibility(View.INVISIBLE);
            } else {
                tvGoodsNum.setText(event.getCartItemNum());
            }
        }
        SkuId = event.getSkuId();
        ShopId = event.getShopid();
        activityId = event.getActivityId();
        ItemId = event.getItemId();
//          //0无促销、1满赠、2限时抢购
        if (!TextUtil.isEmpty(activityTypeId)) {
            if (activityTypeId.equals("0")) {
                tvOldPrice.setVisibility(View.GONE);
                tvDiscount.setVisibility(View.GONE);
                llLayoutMan.setVisibility(View.GONE);
                viewMan.setVisibility(View.GONE);

            } else if (activityTypeId.equals("1")) {
                tvOldPrice.setVisibility(View.GONE);
                tvDiscount.setVisibility(View.GONE);
                llLayoutMan.setVisibility(View.VISIBLE);
                viewMan.setVisibility(View.VISIBLE);

            } else if (activityTypeId.equals("2")) {
                tvOldPrice.setVisibility(View.VISIBLE);
                tvDiscount.setVisibility(View.VISIBLE);
                llLayoutMan.setVisibility(View.GONE);
                viewMan.setVisibility(View.GONE);
            } else {

            }
        } else {

        }

    }

    /**
     * 轮播图展示
     */
    private void updateitemPics(List<String> itemPics) {
/**
 * 轮播不屏蔽
 */
        strings.addAll(itemPics);
/**
 * 屏蔽轮播只取第一个值
 */
//        if (itemPics.size() > 0) {
//            String str_imag = itemPics.get(0);
//            strings.add(itemPics.get(0));
//        }

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(strings);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    /**
     * 商品相关信息
     */
    private void updategoodsinfo(List<GoodsDetailBean.ItemPropertyBean> itemProperty) {
        int size = itemProperty.size();
        llGoodsDetail.removeAllViews();
        if (size > 0 && itemProperty != null) {
            for (int i = 0; i < size; i++) {
                View view = View.inflate(mContext, R.layout.inner_item_shop_detail, null);
                TextView tv_key = (TextView) view.findViewById(R.id.tv_key);
                tv_key.setTextColor(getResources().getColor(R.color.color_333333));
                tv_key.setTextSize(16);
                tv_key.setText(itemProperty.get(i).getPropertyTitle() + itemProperty.get(i).getPropertyText());
                llGoodsDetail.addView(view);
            }
        }
    }

    /**
     * 店铺相关信息
     */
    private void updateshopinfo(List<GoodsDetailBean.ShopInfoBean> shopInfo) {
        int size = shopInfo.size();
        llShopDetail.removeAllViews();
        if (size > 0 && shopInfo != null) {
            for (int i = 0; i < size; i++) {
                View view = View.inflate(mContext, R.layout.inner_item_shop_detail, null);
                TextView tv_key = (TextView) view.findViewById(R.id.tv_key);
                tv_key.setTextSize(15);
                tv_key.setTextColor(getResources().getColor(R.color.color_666666));
                tv_key.setText(shopInfo.get(i).getPropertyTitle() + shopInfo.get(i).getPropertyText());
                llShopDetail.addView(view);
            }
        }
    }

    @OnClick({R.id.rl_show_right_fragment, R.id.tv_addtocart, R.id.fl_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_show_right_fragment:
                NoScrollViewPager vp_content = activity.getVp_content();
                vp_content.setCurrentItem(1);
                break;

            case R.id.tv_addtocart:
                AddGoodsToCart();
                break;

            case R.id.fl_goods:
                startActivity(new Intent(mContext, ShopCarAcitivity.class));
                break;
        }
    }

    private void AddGoodsToCart() {

//        CartId	        否	int	购物车ID（V2.0）
//        ShopId	        否	int	商户ID
//        SkuId	            否	int	商品SKUID
//        ItemId	        否	int	商品ID(V2.0)
//        ActivityId	    否	int	活动ID（当商品有促销活动时传此字段）
//        ChangeType	    否	int	修改类型（1增加，2减少，3直接调整的目标数量）
//        ChangeNum	        是	int	改变数量
//        IsReturnCartNum	否	int	是否返回购物车中商品数量（0不返回，1返回）
//        IsReturnShelfNum	否	int	是否返回货架中商品数量（0不返回，1返回）
//        ProUserId	        是	int	买家UserId(代下单的时候需要传入买家的UserId)
//        IsReplaceOrder	否	int	是否代下单列表（1表示代下单，其它为正常商品列表）(V2.0)
//        ActionVersion	    false	string	不传默认1.0，当前接口版本号（2.0）
//        IsActionTest	    false	string	是否测试：1为静态数据，不传或其他为动态数据

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", ShopId);
        hashMap.put("SkuId", SkuId);
        hashMap.put("ChangeType", "1");
        hashMap.put("ChangeNum", changed_num);
        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        hashMap.put("IsReplaceOrder", "0");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
//        hashMap.put("ItemId", activity.getItemId());
        hashMap.put("ItemId", ItemId);
        if (!TextUtil.isEmpty(activityId)) {
            hashMap.put("ActivityId", activityId);
        }
        /**
         * 版本2  从代下单界面跳转进来暂时屏蔽----不可打开
         */
//        hashMap.put("IsReplaceOrder", AllConstant.IsReplaceOrder);
//        hashMap.put("ActionVersion", AllConstant.ActionVersion);
//        hashMap.put("ItemId", activity.getItemId());
//        hashMap.put("ProUserId", activity.getBUserId());


        Log.i("------>>", "changed_num-------" + changed_num);
        showProgress(getString(R.string.http_loading_data));
        new GetNetUtil(hashMap, Api.Mall_ChangeShopCartItemNum, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                hideProgress();
            }

            @Override
            public void successHandle(String basebean) {
                hideProgress();
                Log.i("-------->", basebean);
                Gson gson = new Gson();
                Basebean<AddGoodsToCartBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<AddGoodsToCartBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {

                    /**
                     * 点击添加购物车之后----数量变为1----此功能暂时屏蔽
                     *
                     * avGoodsNum.setAv_text("1");
                     * changed_num = 1 + "";
                     */

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

                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();

        HashMap<String, String> hashMap = new HashMap<>();

//        IsReturnCartNum	false	int	是否返回购物车中商品数量（0不返回，1返回）
//        IsReturnShelfNum	false	int	是否返回货架中商品数量（0不返回，1返回）
//        IsReplaceOrder	是	int	是否代理下单（0否，1是）
//        ActionVersion	false	string	不传默认1.0，当前接口版本号（2.0）
//        IsActionTest	false	string	是否测试：1为静态数据，不传或其他为动态数据

        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        hashMap.put("IsReplaceOrder", "0");
        hashMap.put("ActionVersion", AllConstant.ActionVersion);

        new GetNetUtil(hashMap, Api.Mall_GetLatestCartShelfNum, mContext) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
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

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (GoodsDetailActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }


    private ProgressDialog mProgressDialog;

    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}

