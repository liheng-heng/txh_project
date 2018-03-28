package com.txh.im.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.PriceGroupAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.PriceGrouBean;
import com.txh.im.utils.GetNetUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class PriceGroupingAcitivty extends BasicActivity {

    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.rc)
    RecyclerView rc;
    private List<PriceGrouBean.GroupListBean> unitListOut = new ArrayList<>();
    private PriceGroupAdapter groupAdapter;

    @Override
    protected void initView() {
        setContentView(R.layout.head_recycle);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("价格分组");
    }

    @Override
    public void initData() {
//        getNetRequest();
        rc.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick(R.id.ll_head_back)
    public void onClick() {
        finish();
    }

    public void getNetRequest() {
        new GetNetUtil(null, Api.PriceGroup_GetPriceGroupList, this) {
            @Override
            public void successHandle(String base) {
                if (PriceGroupingAcitivty.this.isFinishing()) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<PriceGrouBean> basebean = gson.fromJson(base, new TypeToken<Basebean<PriceGrouBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    PriceGrouBean obj = basebean.getObj();
                    if (obj != null) {
                        if (obj.getPageTitle() != null) {
                            headTitle.setText(obj.getPageTitle());
                        }
                        List<PriceGrouBean.GroupListBean> groupList = obj.getGroupList();
                        if (groupList != null && groupList.size() > 0) {
                            if (unitListOut.size() > 0) {
                                unitListOut.clear();
                                unitListOut.addAll(groupList);
                                groupAdapter.notifyDataSetChanged();
                            } else {
                                unitListOut.addAll(groupList);
                                groupAdapter = new PriceGroupAdapter(PriceGroupingAcitivty.this, unitListOut);
                                rc.setAdapter(groupAdapter);
                            }

                        }
                    }
                } else {
                    toast(basebean.getMsg());
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetRequest();
    }
}
