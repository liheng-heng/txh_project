package com.txh.im.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.txh.im.R;
import com.txh.im.adapter.AmapListAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.ToastUtils;
import com.txh.im.widget.SearchView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class SearchAddressActivity extends BasicActivity implements AMapLocationListener, PoiSearch.OnPoiSearchListener {

    @Bind(R.id.search_back)
    LinearLayout searchBack;
    @Bind(R.id.sv)
    SearchView sv;
    @Bind(R.id.iv_clear_search)
    ImageView ivClearSearch;
    @Bind(R.id.function)
    TextView function;
    @Bind(R.id.view_bottom)
    View viewBottom;
    @Bind(R.id.ll_search_head)
    LinearLayout llSearchHead;
    @Bind(R.id.lv)
    ListView lv;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker locMarker;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private String keyword;
    private AmapListAdapter amapListAdapter;
    private ArrayList<PoiItem> pois;
    private String address = "";
    private String cityCode;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_address);
    }

    @Override
    protected void initTitle() {
        sv.setmTextView("街道/大厦/小区");
    }

    @Override
    public void initData() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiItem poiItem = pois.get(i);
                address = poiItem.getProvinceName() + "/" + poiItem.getCityName() + "/" + poiItem.getAdName();
                EventBus.getDefault().post(
                        new FirstEventBus(poiItem, "amap", address));
                finish();
            }
        });
        sv.addTextChangedListener(new TextWatcher() {
            String ss;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ss = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ss.length() > 0) {
                    ivClearSearch.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void initMap(Bundle savedInstanceState) {
        initLocation();//初始化定位参数
        super.initMap(savedInstanceState);
    }

    //定位
    private void initLocation() {
        //初始化client
        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        // 设置定位监听
        mlocationClient.setLocationListener(this);
        //定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mlocationClient.startLocation();
    }

    @OnClick({R.id.search_back, R.id.sv, R.id.iv_clear_search, R.id.function})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.sv:
                break;
            case R.id.iv_clear_search:
                sv.setText("");
                ivClearSearch.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);
                break;
            case R.id.function:
                if (sv.getText().toString().trim().equals("")) {
                    ToastUtils.toast(this, "请输入搜索条件!");
                    return;
                }
                query = new PoiSearch.Query(sv.getText().toString().trim(), "", cityCode);
                //keyWord表示搜索字符串
                //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
                //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
                query.setPageSize(20);// 设置每页最多返回多少条poiitem
                query.setPageNum(0);//设置查询页码
                PoiSearch poiSearch = new PoiSearch(this, query);
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.searchPOIAsyn();
//                lv.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null
                && aMapLocation.getErrorCode() == 0) {
            cityCode = aMapLocation.getCityCode();
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            Log.e("AmapErr", errText);
//            Toast.makeText(this, errText, Toast.LENGTH_LONG).show();
        }
    }

    //返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
//                if (poiResult.getQuery().equals(query)) {
                if (pois == null) {
                    pois = poiResult.getPois();
                } else {
                    pois.clear();
                    pois.addAll(poiResult.getPois());
                }
                if (pois != null && pois.size() > 0) {
                    lv.setVisibility(View.VISIBLE);
                    if (amapListAdapter == null) {
                        amapListAdapter = new AmapListAdapter(this, pois, "SearchAddressActivity");
                        lv.setAdapter(amapListAdapter);
                    } else {
                        amapListAdapter.refresh();
                    }
                } else if (pois != null && pois.size() == 0) {
                    lv.setVisibility(View.GONE);
                    ToastUtils.toast(this, "没有找到您想要的结果");
                }
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    protected void onDestroy() {
        mlocationClient.onDestroy();//高德销毁定位客户端，同时销毁本地定位服务。
        super.onDestroy();
    }
}
