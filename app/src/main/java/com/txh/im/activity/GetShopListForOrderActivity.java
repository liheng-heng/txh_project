package com.txh.im.activity;

import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.GetShopListAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.GetShopListItemBean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liheng on 2017/4/20.
 * 卖家身份时获取我关联的商户列表（有用户登录信息验证）
 */

public class GetShopListForOrderActivity extends BasicActivity {

    @Bind(R.id.ll_listview)
    ListView llListview;

    List<GetShopListItemBean> list = new ArrayList<>();
    private GetShopListAdapter adapter;
    private String shopId;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_get_shoplist_fororder);
    }

    @Override
    protected void initTitle() {
        adapter = new GetShopListAdapter(GetShopListForOrderActivity.this, list, R.layout.item_get_shop_list);
        llListview.setAdapter(adapter);
    }

    @Override
    public void initData() {
        HttpData();
    }

    private void HttpData() {

        HashMap<String, String> map = new HashMap<>();

        new GetNetUtil(map, Api.Mall_GetShopListOfUser, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String base) {
                if (isDestroy){
                    return;
                }
                Log.i("----------->", base);
                Gson gson = new Gson();
                BaseListBean<GetShopListItemBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<GetShopListItemBean>>() {
                }.getType());

                if (basebean.getStatus().equals("200")) {
                    List<GetShopListItemBean> listBean = basebean.getObj();
                    if (listBean != null) {
                        adapter.setRefrece(listBean);
                    }
                } else {
                    ToastUtils.showToast(GetShopListForOrderActivity.this, basebean.getMsg());
                }
            }
        };
    }

}
