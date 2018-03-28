package com.txh.im.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.adapter.CityAdapter;
import com.txh.im.adapter.ZoneAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.ZoneBean;
import com.txh.im.listener.ZoneListener;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ChooseZoneAcitivty extends BasicActivity {

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
    @Bind(R.id.id_location_city)
    TextView idLocationCity;
    //定位城市
    @Bind(R.id.rc_location_city)
    RecyclerView rcLocationCity;
    @Bind(R.id.tv_location_zone)
    TextView tvLocationZone;
    //区域
    @Bind(R.id.rc_location_zone)
    RecyclerView rcLocationZone;

    private String location_id;
    private GridLayoutManager mgr;
    private ZoneListener listener;
    private ZoneListener listenerLocation;
    public List<ZoneBean> mObj;
    public GridLayoutManager mM;
    private List<ZoneBean> list;
    private String name_content;
    private String id_content;

    @Override
    protected void initView() {
        setContentView(R.layout.activtiy_choose_activity);
    }

    @Override
    protected void initTitle() {
        ivHeadBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.delete_button));
        headTitle.setText("选择城市");
    }

    @Override
    public void initData() {
        ZoneBean z = new ZoneBean();
        z.setLocationId("0");
        z.setLocationType("1");
        z.setLocationName("全国");
        name_content = "";
        id_content = "";
        list = new ArrayList<>();
        list.add(z);
        tvLocationZone.setText(list.get(list.size() - 1).getLocationName());
        mgr = new GridLayoutManager(this, 3);
        mM = new GridLayoutManager(this, 3);
        rcLocationCity.setLayoutManager(mgr);
        rcLocationZone.setLayoutManager(mM);
        getZoneData("2", "1");
        CityAdapter cityAdapter = new CityAdapter(this, list, listenerLocation);
        rcLocationCity.setAdapter(cityAdapter);
        listener = new ZoneListener() {

            @Override
            public void refreshPriorityUI(List<ZoneBean> content, String locationId, String locationType) {
                tvLocationZone.setText(list.get(list.size() - 1).getLocationName());
                id_content = id_content + list.get(list.size() - 1).getLocationId() + ",";
                CityAdapter cityAdapter = new CityAdapter(ChooseZoneAcitivty.this, list, listenerLocation);
                rcLocationCity.setAdapter(cityAdapter);
                int i = Integer.parseInt(locationType);
                int ii = i + 1;
                if (!TextUtil.isEmpty(locationType) && locationType.equals("4")) {
                    String str_id = "";
                    String str_name = "";
                    for (int m = 0; m < list.size(); m++) {
                        if (m < list.size()) {
                            str_id += list.get(m).getLocationId() + ",";
                            str_name += list.get(m).getLocationName() + ",";
                        }
                    }
                    if (str_id.length() > 0) {
                        str_id = str_id.substring(0, str_id.length() - 1);
                    }
                    if (str_name.length() > 0) {
                        str_name = str_name.substring(0, str_name.length() - 1);
                    }
//                    //数据是使用Intent返回
                    Intent intent = new Intent();
//                    //把返回数据存入Intent
                    intent.putExtra("str_id", str_id);
                    intent.putExtra("str_name", str_name);
//                    //设置返回数据
                    setResult(RESULT_OK, intent);
//                    //关闭Activity
                    finish();
                } else {
                    getZoneData(ii + "", locationId);
                }
            }
        };

        listenerLocation = new ZoneListener() {

            @Override
            public void refreshPriorityUI(List<ZoneBean> content, String locationId, String locationType) {
                tvLocationZone.setText(list.get(list.size() - 1).getLocationName());
                CityAdapter cityAdapter = new CityAdapter(ChooseZoneAcitivty.this, list, listenerLocation);
                rcLocationCity.setAdapter(cityAdapter);
                int i = Integer.parseInt(locationType);
                int ii = i + 1;
                if (locationType.equals("1")) {
                    //获取全国的数据
                    getZoneData("2", "1");
                } else {
                    getZoneData(ii + "", locationId);
                }
            }
        };
    }

    @OnClick(R.id.ll_head_back)
    public void onClick() {
        finish();
    }

    private void getZoneData(String location_type, String location_id) {
        showProgress("正在加载");
        HashMap<String, String> map = new HashMap<>();
        map.put("LocationType", location_type);
        map.put("LocationId", location_id);

        new GetNetUtil(map, Api.GetInfo_GetLocationsByPId, this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String base) {
                if (isDestroy){
                    return;
                }
                closeProgress();
                Log.i("----------->", base);
                Gson gson = new Gson();
                BaseListBean<ZoneBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<ZoneBean>>() {
                }.getType());
                mObj = basebean.getObj();
                ZoneAdapter zoneAdapter = new ZoneAdapter(ChooseZoneAcitivty.this, mObj, list, listener);
                rcLocationZone.setAdapter(zoneAdapter);

            }
        };
    }

    private ProgressDialog mProgressDialog;

    protected void showProgress(String showstr) {
        mProgressDialog = ProgressDialog.show(ChooseZoneAcitivty.this, "", showstr);
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
    }
}