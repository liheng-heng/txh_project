package com.txh.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.GoodsAddressAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.GoodsAddressBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收货地址界面
 */
public class GoodsAddressActivity extends BasicActivity {

    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
    @Bind(R.id.tv_head_back)
    TextView tvHeadBack;
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_right)
    ImageView headRight;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.ll_listview)
    ListView llListview;
    @Bind(R.id.rl_list)
    RelativeLayout rlList;
    @Bind(R.id.ll_add)
    LinearLayout llAdd;
    @Bind(R.id.tv_nodata)
    TextView tvNodata;

    private ArrayList<GoodsAddressBean> list = new ArrayList<>();
    private GoodsAddressAdapter adapter;
    private String OrderAddress;
    private String addressId;
    private String userId;
    private String commitAndAdd;

    @Override
    protected void initView() {
        OrderAddress = getIntent().getStringExtra("OrderAddress");
        addressId = getIntent().getStringExtra("addressId");
        userId = getIntent().getStringExtra("userId");
        commitAndAdd = getIntent().getStringExtra("commitAndAdd");
        setContentView(R.layout.activity_goods_address);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("收货地址");
        adapter = new GoodsAddressAdapter(GoodsAddressActivity.this, list, OrderAddress, userId);
        llListview.setAdapter(adapter);
    }

    @Override
    public void initData() {
        HttpData(1);
    }

    @OnClick({R.id.ll_head_back, R.id.ll_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                if (!TextUtil.isEmpty(OrderAddress)) {
                    //提交订单进来
                    if (list == null || list.size() == 0) {
                        Intent intent = new Intent();
                        intent.putExtra("AddressId", "");
                        intent.putExtra("Name", "");
                        intent.putExtra("Phone", "");
                        intent.putExtra("Address", "");
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                    if (adapter != null && adapter.getList_delete_address_ids() != null) {
                        List<String> list_delete_address_ids = adapter.getList_delete_address_ids();
                        if (list_delete_address_ids.contains(addressId)) {
                            Intent intent = new Intent();
                            intent.putExtra("AddressId", "");
                            intent.putExtra("Name", "");
                            intent.putExtra("Phone", "");
                            intent.putExtra("Address", "");
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    }
                    finish();
                } else {
                    finish();
                }
                break;

            case R.id.ll_add:
                Intent intent = new Intent(GoodsAddressActivity.this, UpdateAddressActivity.class);
                intent.putExtra("isAdd", "0");
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                //添加--数据回调成功
                if (resultCode == Activity.RESULT_OK) {
                    if (!TextUtil.isEmpty(OrderAddress) && !TextUtil.isEmpty(commitAndAdd)) {
                        //提交订单进来
                        String update_addressId = data.getStringExtra("update_AddressId");
                        String update_name = data.getStringExtra("update_Name");
                        String update_phone = data.getStringExtra("update_Phone");
                        String update_address = data.getStringExtra("update_Address");
                        Intent intent = new Intent();
                        intent.putExtra("AddressId", update_addressId);
                        intent.putExtra("Name", update_name);
                        intent.putExtra("Phone", update_phone);
                        intent.putExtra("Address", update_address);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        HttpData(2);
                    }
                }
                break;

            case 101:
                //编辑--数据回调成功
                if (resultCode == Activity.RESULT_OK) {
//                    HttpData(2);
                    if (!TextUtil.isEmpty(OrderAddress)) {
                        //提交订单进来
                        String isEditAndDefault = data.getStringExtra("isEditAndDefault");
                        if (!TextUtil.isEmpty(isEditAndDefault) && isEditAndDefault.equals("1")) {
                            String update_addressId = data.getStringExtra("update_AddressId");
                            String update_name = data.getStringExtra("update_Name");
                            String update_phone = data.getStringExtra("update_Phone");
                            String update_address = data.getStringExtra("update_Address");
                            Intent intent = new Intent();
                            intent.putExtra("AddressId", update_addressId);
                            intent.putExtra("Name", update_name);
                            intent.putExtra("Phone", update_phone);
                            intent.putExtra("Address", update_address);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } else {
                            HttpData(2);
                        }
                    } else {
                        //正常编辑地址回调
                        HttpData(2);
                    }
                }
                break;
        }
    }

    private void HttpData(final int type) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!TextUtil.isEmpty(userId)) {
            hashMap.put("BUserId", userId);
        }
        hashMap.put("ActionVersion", AllConstant.ActionVersion);
        showProgress(getResources().getString(R.string.http_loading_data));
        new GetNetUtil(hashMap, Api.GetInfo_GetUserAddressList, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                hideProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy) {
                    return;
                }
                hideProgress();
                Log.i("----------->", basebean);
                Gson gson = new Gson();
                BaseListBean<GoodsAddressBean> bean = gson.fromJson(basebean, new TypeToken<BaseListBean<GoodsAddressBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    if (tvNodata != null) {
                        if (bean.getObj().size() == 0) {
                            tvNodata.setVisibility(View.VISIBLE);
                        } else {
                            tvNodata.setVisibility(View.GONE);
                        }
                    }
                    switch (type) {
                        case 1:
                            adapter.setRefrece(bean.getObj());
                            break;
                        case 2:
                            adapter.setRefrece(bean.getObj());
                            break;
                    }
                } else {
                    ToastUtils.showToast(GoodsAddressActivity.this, bean.getMsg());
                }
            }
        };
    }

    /**
     * 禁用返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtil.isEmpty(OrderAddress)) {
                //提交订单进来
                if (list == null || list.size() == 0) {
                    Intent intent = new Intent();
                    intent.putExtra("AddressId", "");
                    intent.putExtra("Name", "");
                    intent.putExtra("Phone", "");
                    intent.putExtra("Address", "");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

                if (adapter != null && adapter.getList_delete_address_ids() != null) {
                    List<String> list_delete_address_ids = adapter.getList_delete_address_ids();
                    if (list_delete_address_ids.contains(addressId)) {
                        Intent intent = new Intent();
                        intent.putExtra("AddressId", "");
                        intent.putExtra("Name", "");
                        intent.putExtra("Phone", "");
                        intent.putExtra("Address", "");
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }

                finish();
            } else {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
