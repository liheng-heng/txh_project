package com.txh.im.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.ChangeStatusBean;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * Created by liheng on 2017/5/6.
 * <p>
 * 身份切换---弹窗效果
 */

public class ChangeTypeActivity extends BasicActivity {

    private ImageView iv_cancle;
    private RelativeLayout ll_cancle_layout;
    private ImageView iv_left1;
    private TextView tv_name1;
    private ImageView iv_right1;
    private RelativeLayout relativelayout1;
    private ImageView iv_left2;
    private TextView tv_name2;
    private ImageView iv_right2;
    private RelativeLayout relativelayout2;
    private String unitType;

    private String shopInfoIsOK;//商户信息是否ok（0否，1是）（当身份为卖家unittype=2且该值为0时跳到我的资料编辑页面）

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_type);
        iv_cancle = (ImageView) findViewById(R.id.iv_cancle);
        ll_cancle_layout = (RelativeLayout) findViewById(R.id.ll_cancle_layout);
        tv_name1 = (TextView) findViewById(R.id.tv_name1);
        iv_left1 = (ImageView) findViewById(R.id.iv_left1);
        iv_right1 = (ImageView) findViewById(R.id.iv_right1);
        relativelayout1 = (RelativeLayout) findViewById(R.id.relativelayout1);
        tv_name2 = (TextView) findViewById(R.id.tv_name2);
        iv_left2 = (ImageView) findViewById(R.id.iv_left2);
        iv_right2 = (ImageView) findViewById(R.id.iv_right2);
        relativelayout2 = (RelativeLayout) findViewById(R.id.relativelayout2);
        unitType = GlobalApplication.getInstance().getPerson(this).getUnitType();
        if (unitType.equals("1")) {
            iv_left1.setBackground(getResources().getDrawable(R.drawable.dian_no_select));
            tv_name1.setText("我是店家");
            tv_name1.setTextColor(getResources().getColor(R.color.white));
            relativelayout1.setBackground(getResources().getDrawable(R.drawable.edit_ok));
            iv_left2.setBackground(getResources().getDrawable(R.drawable.shang_select));
            tv_name2.setText("我是供应商");
            tv_name2.setTextColor(getResources().getColor(R.color.blue_more));
            relativelayout2.setBackground(getResources().getDrawable(R.drawable.bg_buy_or_seller_3));
        } else {
            iv_left1.setBackground(getResources().getDrawable(R.drawable.dian_select));
            tv_name1.setText("我是店家");
            tv_name1.setTextColor(getResources().getColor(R.color.blue_more));
            relativelayout1.setBackground(getResources().getDrawable(R.drawable.bg_buy_or_seller_3));
            iv_left2.setBackground(getResources().getDrawable(R.drawable.shang_no_select));
            tv_name2.setText("我是供应商");
            tv_name2.setTextColor(getResources().getColor(R.color.white));
            relativelayout2.setBackground(getResources().getDrawable(R.drawable.edit_ok));
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public void initData() {
    }

    @OnClick({R.id.iv_cancle, R.id.ll_cancle_layout, R.id.relativelayout1, R.id.relativelayout2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cancle:
                finish();
                break;

            case R.id.ll_cancle_layout:
                finish();
                break;

            case R.id.relativelayout1:
                if (unitType.equals("1")) {
                    finish();
                } else {
                    ToBuy();
                    finish();
                }
                break;

            case R.id.relativelayout2:
                if (unitType.equals("1")) {
                    ToSeller();
                } else {
                    finish();
                }
                finish();
                break;
        }
    }

    /**
     * 卖家切换为买家
     * <p>
     * 身份切换请求
     */
    private void ToBuy() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ActionVersion", "3.0");
        new GetNetUtil(hashMap, Api.GetInfo_ChangeUnitType, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                Gson gson = new Gson();
                Basebean<ChangeStatusBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ChangeStatusBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    SPUtil.remove(ChangeTypeActivity.this, "sp_personalCenterBean");
                    SPUtil.remove(ChangeTypeActivity.this, "sp_MyInfoBean");
                    SPUtil.remove(ChangeTypeActivity.this, "sp_shoppingTrolleyBean");
                    String unitId = bean.getObj().getUserInfo().getUnitId();
                    String unitType = bean.getObj().getUserInfo().getUnitType();
                    CryptonyBean cryptonyBean = new CryptonyBean();
                    if (!TextUtil.isEmpty(unitId)) {
                        cryptonyBean.setUnitId(unitId);
                    }
                    if (!TextUtil.isEmpty(unitType)) {
                        cryptonyBean.setUnitType(unitType);
                    }
                    GlobalApplication.getInstance().savePerson(cryptonyBean, ChangeTypeActivity.this);
                    if (!TextUtil.isEmpty(bean.getObj().getUserInfo().getShopInfoIsOK())) {
                        SPUtil.putAndApply(ChangeTypeActivity.this, "shopInfoIsOK", bean.getObj().getUserInfo().getShopInfoIsOK());
                    }
                    startActivity(new Intent(ChangeTypeActivity.this, MainActivity.class));

                } else {
                    ToastUtils.showToast(ChangeTypeActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 切换为卖家
     */
    private void ToSeller() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ActionVersion", "3.0");
        new GetNetUtil(hashMap, Api.GetInfo_ChangeUnitType, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                Gson gson = new Gson();
                Basebean<ChangeStatusBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<ChangeStatusBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    SPUtil.remove(ChangeTypeActivity.this, "sp_personalCenterBean");
                    SPUtil.remove(ChangeTypeActivity.this, "sp_MyInfoBean");
                    SPUtil.remove(ChangeTypeActivity.this, "sp_shoppingTrolleyBean");
                    String unitId = bean.getObj().getUserInfo().getUnitId();
                    String unitType = bean.getObj().getUserInfo().getUnitType();
                    CryptonyBean cryptonyBean = new CryptonyBean();
                    if (!TextUtil.isEmpty(unitId)) {
                        cryptonyBean.setUnitId(unitId);
                    }
                    if (!TextUtil.isEmpty(unitType)) {
                        cryptonyBean.setUnitType(unitType);
                    }
                    GlobalApplication.getInstance().savePerson(cryptonyBean, ChangeTypeActivity.this);
                    String shopInfoIsOK = bean.getObj().getUserInfo().getShopInfoIsOK();
                    if (!TextUtil.isEmpty(shopInfoIsOK)) {
                        SPUtil.putAndApply(ChangeTypeActivity.this, "shopInfoIsOK", shopInfoIsOK);
                    }
                    if (shopInfoIsOK.equals("0")) {
                        //买家身份没有完善   跳转资料编辑界面
                        Intent intent = new Intent(ChangeTypeActivity.this, MyInfoDetailActivity.class);
                        intent.putExtra("changeType", "babababb");
                        startActivity(intent);
                    } else {
                        //调用切换接口  跳转主界面
                        startActivity(new Intent(ChangeTypeActivity.this, MainActivity.class));
                    }
                    finish();
                } else {
                    ToastUtils.showToast(ChangeTypeActivity.this, bean.getMsg());
                }
            }
        };
    }
}








