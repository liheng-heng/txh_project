package com.txh.im.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.GoodsAddressAdapter_new;
import com.txh.im.android.AllConstant;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.GoodsAddressBean_new;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收货地址界面
 */
public class GoodsAddressActivity_new extends BasicActivity {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.ll_local_layout)
    LinearLayout llLocalLayout;
    @Bind(R.id.ll_listview)
    ListView llListview;
    @Bind(R.id.rl_list)
    RelativeLayout rlList;
    @Bind(R.id.ll_local_layout333)
    LinearLayout llLocalLayout333;

    private List<GoodsAddressBean_new.ListBean> list = new ArrayList<>();
    private GoodsAddressAdapter_new adapter;
    private String OrderAddress;
    private String addressId;
    private String userId;
    private String Longitude_local;
    private String Latitude_local;

    @Override
    protected void initView() {
        OrderAddress = getIntent().getStringExtra("OrderAddress");
        addressId = getIntent().getStringExtra("addressId");
        userId = getIntent().getStringExtra("userId");
        setContentView(R.layout.activity_goods_address_new);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("选择地址");
        adapter = new GoodsAddressAdapter_new(GoodsAddressActivity_new.this, list);
        llListview.setAdapter(adapter);
        Longitude_local = (String) SPUtil.get(GoodsAddressActivity_new.this, "longitude_only", "N");
        Latitude_local = (String) SPUtil.get(GoodsAddressActivity_new.this, "latitude_only", "N");
    }

    @Override
    public void initData() {
        HttpData(1);
        llListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventBus.getDefault().post(new FirstEventBus("NearbyFriendFragement",
                        list.get(i).getReceiveAddress(),
                        list.get(i).getLongitude(),
                        list.get(i).getLatitude()));
                finish();
            }
        });
    }

    @OnClick({R.id.ll_head_back, R.id.ll_local_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;

            case R.id.ll_local_layout:
                EventBus.getDefault().post(new FirstEventBus("NearbyFriendFragement", "当前位置", Longitude_local, Latitude_local));
                finish();
                break;
        }
    }

    private void HttpData(final int type) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!TextUtil.isEmpty(userId)) {
            hashMap.put("BUserId", userId);
        }
        hashMap.put("ActionVersion", AllConstant.ActionVersion3_0);
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

                Basebean<GoodsAddressBean_new> bean = gson.fromJson(basebean, new TypeToken<Basebean<GoodsAddressBean_new>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {
                    List<GoodsAddressBean_new.ListBean> list = bean.getObj().getList();
                    String isLack = bean.getObj().getIsLack();
                    if (list != null) {
                        if (list.size() == 0) {
                            llLocalLayout333.setVisibility(View.VISIBLE);
                        } else {
                            llLocalLayout333.setVisibility(View.GONE);
                        }
                    } else {
                        llLocalLayout333.setVisibility(View.VISIBLE);
                    }

                    if (!TextUtil.isEmpty(isLack)) {
                        if (isLack.equals("1")) {
                            llLocalLayout333.setVisibility(View.VISIBLE);
                        } else {
                            llLocalLayout333.setVisibility(View.GONE);
                        }
                    } else {
                        llLocalLayout333.setVisibility(View.VISIBLE);
                    }

                    if (null != list) {
                        switch (type) {
                            case 1:
                                adapter.setRefrece(bean.getObj().getList());
                                break;

                            case 2:
                                adapter.setRefrece(bean.getObj().getList());
                                break;
                        }
                    }
                } else {
                    ToastUtils.showToast(GoodsAddressActivity_new.this, bean.getMsg());
                }
            }
        };
    }


//    /**
//     * 禁用返回键
//     *
//     * @param keyCode
//     * @param event
//     * @return
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (!TextUtil.isEmpty(OrderAddress)) {
//                //提交订单进来
//                if (list == null || list.size() == 0) {
//                    Intent intent = new Intent();
//                    intent.putExtra("AddressId", "");
//                    intent.putExtra("Name", "");
//                    intent.putExtra("Phone", "");
//                    intent.putExtra("Address", "");
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();
//                }
//
//                if (adapter != null && adapter.getList_delete_address_ids() != null) {
//                    List<String> list_delete_address_ids = adapter.getList_delete_address_ids();
//                    if (list_delete_address_ids.contains(addressId)) {
//                        Intent intent = new Intent();
//                        intent.putExtra("AddressId", "");
//                        intent.putExtra("Name", "");
//                        intent.putExtra("Phone", "");
//                        intent.putExtra("Address", "");
//                        setResult(Activity.RESULT_OK, intent);
//                        finish();
//                    }
//                }
//
//                finish();
//            } else {
//                finish();
//            }
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
