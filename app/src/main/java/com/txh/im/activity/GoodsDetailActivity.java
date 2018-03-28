package com.txh.im.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gxz.PagerSlidingTabStrip;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.ItemTitlePagerAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.ChoosePersonTalkBean;
import com.txh.im.bean.GoodsDetailBean;
import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.fragment.GoodsDetailFragment;
import com.txh.im.fragment.GoodsInfoFragment;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * liheng--20170405
 */

public class GoodsDetailActivity extends BasicActivity implements View.OnClickListener {

    public PagerSlidingTabStrip psts_tabs;
    public NoScrollViewPager vp_content;
    public TextView tv_title;
    private List<Fragment> fragmentList = new ArrayList<>();
    private GoodsInfoFragment goodsInfoFragment;
    private GoodsDetailFragment goodsDetailFragment;
    private LinearLayout ll_back;
    private ImageView iv_talk;
    private ItemTitlePagerAdapter adapter;
    private String SkuId;
    private String Shopid;
    private String userName;
    GoodsDetailBean goodsDetailBean;
    private SharedPreferences sp;
    private HomeSingleListBean homeSingleListBean;
    private String formShopCar;
    private String BUserId;
    private String ItemId;
    private String ActivityId;

    @Override
    public void initView() {
        setContentView(R.layout.activity_goods_detail);
        SkuId = getIntent().getStringExtra("SkuId");
        Shopid = getIntent().getStringExtra("Shopid");
        userName = getIntent().getStringExtra("userName");
        formShopCar = getIntent().getStringExtra("formShopCar");
        BUserId = getIntent().getStringExtra("BUserId");
        ItemId = getIntent().getStringExtra("ItemId");
        ActivityId = getIntent().getStringExtra("ActivityId");
    }

    @Subscribe
    @Override
    public void initTitle() {
        sp = getSharedPreferences("shop", MODE_PRIVATE);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_talk = (ImageView) findViewById(R.id.iv_talk);
        psts_tabs = (PagerSlidingTabStrip) findViewById(R.id.psts_tabs);
        vp_content = (NoScrollViewPager) findViewById(R.id.vp_content);
        tv_title = (TextView) findViewById(R.id.tv_title);
        goodsInfoFragment = new GoodsInfoFragment();
        goodsDetailFragment = new GoodsDetailFragment();
        fragmentList.add(goodsInfoFragment);
        fragmentList.add(goodsDetailFragment);
        adapter = new ItemTitlePagerAdapter(getSupportFragmentManager(), fragmentList, new String[]{"商品", "详情"});
        vp_content.setAdapter(adapter);
        vp_content.setOffscreenPageLimit(2);
        psts_tabs.setViewPager(vp_content);
        ll_back.setOnClickListener(this);
        iv_talk.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (!TextUtil.isEmpty(formShopCar) && formShopCar.equals("formActiveCar")) {
            iv_talk.setVisibility(View.GONE);
        } else {
            iv_talk.setVisibility(View.VISIBLE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("SkuId", SkuId);
        edit.putString("Shopid", Shopid);
        edit.commit();
        HttpData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
//                if (!TextUtil.isEmpty(formShopCar) && formShopCar.equals("formShopCar")) {
//                    startActivity(new Intent(GoodsDetailActivity.this, ShopCarAcitivity.class));
//                }
                EventBus.getDefault().post(
                        new FirstEventBus("ShopClassfiy"));
                finish();
                break;


            case R.id.iv_talk:
                ChoosePersonTalk();
                break;
        }
    }

    private void ChoosePersonTalk() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", Shopid);

        new GetNetUtil(hashMap, Api.Mall_GetUserListForChat, GoodsDetailActivity.this) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy){
                    return;
                }
                Log.i("-------->", basebean);
                Gson gson = new Gson();
                Basebean<ChoosePersonTalkBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ChoosePersonTalkBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    ChoosePersonTalkBean choosePersonTalkBean = bean.getObj();
                    List<ChoosePersonTalkBean.UserListBean> userList = bean.getObj().getUserList();
                    if (userList != null) {
                        if (userList.size() == 1) {
                            homeSingleListBean = new HomeSingleListBean();
                            homeSingleListBean.setUserId(userList.get(0).getUserId());
                            homeSingleListBean.setUserName(userList.get(0).getUserName());
                            homeSingleListBean.setImagesHead(userList.get(0).getImagesHead());
                            Intent intent = new Intent(GoodsDetailActivity.this, ChatAcitivty.class);
                            intent.putExtra("homeSingleListBean", gson.toJson(homeSingleListBean));
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(GoodsDetailActivity.this, ChoosePersonTalkWithActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("choosePersonTalkBean", choosePersonTalkBean);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                } else {
                    ToastUtils.showToast(GoodsDetailActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 获取商品详细信息
     */
    public void HttpData() {
//        BUserId	        false	int	买家UserId(代下单的时候需要传入买家的UserId)
//        ShopId	        true	int	商户ID
//        SkuId	            true	int	商品SKUID
//        IsReturnCartNum	false	int	是否返回购物车中商品数量（0不返回，1返回）
//        IsReturnShelfNum	false	int	是否返回货架中商品数量（0不返回，1返回）
//        ActionVersion	    false	string	不传默认1.0，当前接口版本号（2.0）
//        IsActionTest	    false	string	是否测试：1为静态数据，不传或其他为动态数据
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ShopId", Shopid);
        hashMap.put("SkuId", SkuId);
        hashMap.put("IsReturnCartNum", "1");
        hashMap.put("IsReturnShelfNum", "1");
        if (!TextUtil.isEmpty(formShopCar) && formShopCar.equals("formActiveCar")) {
            hashMap.put("BUserId", BUserId);
        }
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        if (!TextUtil.isEmpty(ActivityId)) {
            hashMap.put("ActivityId", ActivityId);
        }
        showProgress(getResources().getString(R.string.http_loading_data));
        new GetNetUtil(hashMap, Api.Mall_GetShopItemDetail, GoodsDetailActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                hideProgress();
            }

            @Override
            public void successHandle(String basebean) {
                Log.i("-------->", basebean);
                if (isDestroy){
                    return;
                }
                hideProgress();
                Gson gson = new Gson();
                Basebean<GoodsDetailBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<GoodsDetailBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {
                    goodsDetailBean = bean.getObj();
                    goodsDetailBean.setShopid(Shopid);
                    goodsDetailBean.setSkuId(SkuId);
                    EventBus.getDefault().post(goodsDetailBean);
                    /**
                     * 保存详情信息----出错
                     */
                    String itemInfo = bean.getObj().getItemInfo();
                    if (!TextUtil.isEmpty(itemInfo)) {
                        EventBus.getDefault().post(itemInfo);
                    }

                } else {
                    ToastUtils.showToast(GoodsDetailActivity.this, bean.getMsg());
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(GoodsDetailActivity.this);//反注册EventBus

    }

    public NoScrollViewPager getVp_content() {
        return vp_content;
    }

    public void setVp_content(NoScrollViewPager vp_content) {
        this.vp_content = vp_content;
    }

    public String getBUserId() {
        return BUserId;
    }

    public void setBUserId(String BUserId) {
        this.BUserId = BUserId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getFormShopCar() {
        return formShopCar;
    }

    public void setFormShopCar(String formShopCar) {
        this.formShopCar = formShopCar;
    }
}



