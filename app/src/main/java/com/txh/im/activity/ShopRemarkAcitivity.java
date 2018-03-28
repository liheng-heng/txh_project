package com.txh.im.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.FriendsUnitDetailBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.utils.GetNetUtil;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

public class ShopRemarkAcitivity extends BasicActivity {

    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.et_shop_name)
    EditText etShopName;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.tv_shop_type)
    TextView tvShopType;
    private String friendUnitId;
    private String friendUnitType;
    private String unitname;
    private boolean isChecked;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_remark_acitivity);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("备注信息");
        llHeadRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        isChecked = false;
        Intent intent = getIntent();
        friendUnitId = intent.getStringExtra("FriendUnitId");
        friendUnitType = intent.getStringExtra("FriendUnitType");
        unitname = intent.getStringExtra("unitname");
//        friendsunit = intent.getStringExtra("friendsunit");
        if (friendUnitType.equals("2")) {
            tvShopType.setText("供应商备注");
        } else if (friendUnitType.equals("1")) {
            tvShopType.setText("店铺备注");
        }
        if (unitname != null){
            etShopName.setText(unitname);
            etShopName.setSelection(unitname.length());//将光标移至文字末尾
        }
    }

    @OnClick({R.id.ll_head_back, R.id.ll_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finishCurrent();
                break;
            case R.id.ll_head_right:
                String trim = etShopName.getText().toString().trim();
                String regexStr = "^[-0-9_a-zA-Z\\u4e00-\\u9fa5]{2,20}$";
                if (!trim.matches(regexStr) && !trim.equals("")){
                    toast("备注支持2-20位中英文,数字'-'和'_'任意组合!");
                    return;
                }
                if (isChecked){
                    return;
                }
                isChecked = true;
                showProgress("正在加载");
                HashMap<String, String> map = new HashMap<>();
                map.put("FriendUnitId", friendUnitId);
                map.put("MarkName", etShopName.getText().toString().trim());
                new GetNetUtil(map, Api.TX_App_SNS_EditUnitMark, this) {
                    @Override
                    public void successHandle(String base) {
                        if (isDestroy){
                            return;
                        }
                        Gson gson = new Gson();
                        HttpBean basebean = gson.fromJson(base, HttpBean.class);
                        if (basebean.getStatus().equals("200")) {
                            toast(basebean.getMsg());
                            finishCurrent();
                        } else {
                            toast(basebean.getMsg());
                            isChecked = false;
                            hideProgress();
                        }
                    }
                };
                break;
        }
    }

    private void finishCurrent() {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("FriendUnitId", friendUnitId);
        new GetNetUtil(map1, Api.TX_App_SNS_FriendUnitDetail, ShopRemarkAcitivity.this) {
            @Override
            public void successHandle(String base) {
                if (isDestroy){
                    return;
                }
                Gson gson = new Gson();
                Basebean<FriendsUnitDetailBean> basebean = gson.fromJson(base, new TypeToken<Basebean<FriendsUnitDetailBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    hideProgress();
                    FriendsUnitDetailBean obj = basebean.getObj();
                    if (obj == null) {
                        return;
                    }
                    Intent intent = new Intent(ShopRemarkAcitivity.this, FriendsUnitDetailAcitivty.class);
                    intent.putExtra("friendsunit", gson.toJson(obj));
                    intent.putExtra("FriendUnitId", friendUnitId);
                    intent.putExtra("FriendUnitType", friendUnitType);
                    startActivity(intent);
                    finish();
                } else {
                    toast(basebean.getMsg());
                    isChecked = false;
                    hideProgress();
                }

            }
        };
    }
}
