package com.txh.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.txh.im.R;
import com.txh.im.adapter.AmapListAdapter;
import com.txh.im.base.BasicActivity;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * jiajia,搜索地址
 */

public class AmapActivity extends BasicActivity implements AMapLocationListener, LocationSource, GeocodeSearch.OnGeocodeSearchListener {

    @Bind(R.id.search_back)
    LinearLayout searchBack;
    @Bind(R.id.tv_search_content)
    TextView tvSearchContent;
    @Bind(R.id.rl_sv)
    RelativeLayout rlSv;
    @Bind(R.id.function)
    TextView function;
    @Bind(R.id.view_bottom)
    View viewBottom;
    @Bind(R.id.ll_search_head)
    LinearLayout llSearchHead;
    @Bind(R.id.map)
    MapView map;
    @Bind(R.id.lv)
    ListView lv;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMap aMap;
    private Marker locMarker;
    private BitmapDescriptor chooseDescripter, movingDescriptor;
    private AmapListAdapter amapListAdapter;
    private ArrayList<PoiItem> pois;
    private String address = "";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_amap);
    }

    @Override
    protected void initTitle() {
        tvSearchContent.setText("街道/大厦/小区");
    }

    @Override
    public void initData() {
        aMap = map.getMap();
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setZoomControlsEnabled(false);
        if (locMarker == null) {
            locMarker = aMap.addMarker(new MarkerOptions());
        }
        locMarker.setIcon(movingDescriptor);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiItem poiItem = pois.get(i);
                EventBus.getDefault().post(
                        new FirstEventBus(poiItem, "amap", address));
//                finish();
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initMap(Bundle savedInstanceState) {
        super.initMap(savedInstanceState);
        initLocation();//初始化定位参数
        map.onCreate(savedInstanceState);
        chooseDescripter = BitmapDescriptorFactory.fromResource(R.drawable.map_location);
        movingDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.map_location);
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null
                && aMapLocation.getErrorCode() == 0) {
            double longitude = aMapLocation.getLongitude();
            double latitude = aMapLocation.getLatitude();
            LatLng location = new LatLng(latitude, longitude);
            LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
            changeLocation(location);
//            aMapLocation.getAoiName();//获取当前定位点的AOI信息
            GeocodeSearch geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(this);
            // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
//            GeocodeQuery query = new GeocodeQuery(aMapLocation.getAoiName(), aMapLocation.getCityCode());
// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500, GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);

        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            Log.e("AmapErr", errText);
        }
    }

    private void changeLocation(LatLng location) {

        MarkerOptions centerMarkerOption = new MarkerOptions().position(location).icon(chooseDescripter);
        locMarker = aMap.addMarker(centerMarkerOption);
        locMarker.setPosition(location);
        locMarker.setIcon(movingDescriptor);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        map.onDestroy();
        mlocationClient.onDestroy();//高德销毁定位客户端，同时销毁本地定位服务。
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.search_back, R.id.rl_sv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.rl_sv:
                startActivity(new Intent(this, SearchAddressActivity.class));
                break;
        }
    }

    //2）返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        //解析result获取地址描述信息
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null) {
//                if (regeocodeResult.regeocodeResult.getRegeocodeQuery().equals(query)) {
                RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                address = regeocodeAddress.getProvince() + "/" + regeocodeAddress.getCity() + "/" + regeocodeAddress.getDistrict();
                if (pois == null) {
                    pois = (ArrayList<PoiItem>) regeocodeAddress.getPois();
                } else {
                    pois.clear();
                    pois.addAll(regeocodeAddress.getPois());
                }
                if (pois != null && pois.size() > 0) {
                    if (amapListAdapter == null) {
                        amapListAdapter = new AmapListAdapter(this, pois);
                        lv.setAdapter(amapListAdapter);
                    } else {
                        amapListAdapter.refresh();
                    }
                } else if (pois != null && pois.size() == 0){
                    ToastUtils.toast(this, "无搜索结果");
                }
//                }
            }
        }
    }


    //返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        //解析result获取坐标信息
    }

    @Subscribe
    public void onEventMainThread(FirstEventBus event) {
        if (event != null && event.getMsg().equals("amap")) {
            finish();
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}
