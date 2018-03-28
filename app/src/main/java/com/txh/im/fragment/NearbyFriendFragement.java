package com.txh.im.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.GoodsAddressActivity_new;
import com.txh.im.activity.MainActivity;
import com.txh.im.adapter.NearShopContentAdapter;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.MainButtonBean;
import com.txh.im.bean.NearShopListBean;
import com.txh.im.bean.NearbyTitleBean;
import com.txh.im.itemclick.MyItemClickListener;
import com.txh.im.itemclick.NearbyTitleAdapter;
import com.txh.im.listener.FirstEventBus;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liheng on 2017/2/23.
 */

public class NearbyFriendFragement extends BaseFragment implements MyItemClickListener {

    @Bind(R.id.title_recycleview)
    RecyclerView titleRecycleview;
    @Bind(R.id.content_recycleview)
    RecyclerView contentRecycleview;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.ll_chooseaddress)
    LinearLayout llChooseaddress;
    @Bind(R.id.srl)
    PullToRefreshLayout srl;
    @Bind(R.id.tv_headtitle)
    TextView tvHeadtitle;
    @Bind(R.id.rl_no_order)
    RelativeLayout rlNoOrder;

    private NearbyTitleAdapter adapter;
    List<NearbyTitleBean.ListBean> title_list = new ArrayList<>();
    private int title_item_size = 0;

    private NearShopContentAdapter contentAdapter;
    List<NearShopListBean> listContent = new ArrayList<>();
    private String Radius_str = "";

    private String Longitude_http;
    private String Latitude_http;

    private String Longitude_local;
    private String Latitude_local;

    private AlertDialog mAlertDialog = null;
    private MainActivity activity;

    private boolean had_show = false;

    double Longitude_pull_dou;
    double Latitude_pull_dou;

    String callBackNearbyAddress;
    private List<MainButtonBean> listBean;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        listBean = activity.getListBean();
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_nearby_friend, null);
        return view;
    }

    @Override
    protected void initTitle() {

//        if (listBean != null && listBean.size() > 0) {
//            for (int i = 0; i < listBean.size(); i++) {
//                if (listBean.get(i).getMenuCode().equals("SearchNear")) {
//                    tvHeadtitle.setText(listBean.get(i).getTitle());
//                }
//            }
//        }

        //121.505874  31.238028 外滩环球中心
        Longitude_local = (String) SPUtil.get(mContext, "longitude_only", "121.505874");
        Latitude_local = (String) SPUtil.get(mContext, "latitude_only", "31.238028");
        EventBus.getDefault().register(this);
        HttpTitleData();
    }

    @Override
    protected void initData() {
        adapter = new NearbyTitleAdapter(mContext, title_list);
        titleRecycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        contentAdapter = new NearShopContentAdapter(mContext, listContent);
        contentRecycleview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        contentRecycleview.setItemAnimator(new DefaultItemAnimator());
        contentRecycleview.setAdapter(contentAdapter);
        srl.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                // 结束刷新
                HttpContentData(1, Longitude_local_dou, Latitude_local_dou);
            }

            @Override
            public void loadMore() {
            }
        });
        srl.setCanLoadMore(false);//禁止加载更多
    }

    /**
     * 获取title数据
     */
    private void HttpTitleData() {

        HashMap<String, String> hashMap = new HashMap<>();
        new GetNetUtil(hashMap, Api.Near_GetNearHead, GlobalApplication.getInstance().getApplicationContext()) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                if (isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<NearbyTitleBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<NearbyTitleBean>>() {
                }.getType());

                if (bean.getStatus().endsWith("200")) {
                    List<NearbyTitleBean.ListBean> list = bean.getObj().getList();
                    String location = bean.getObj().getLocation();
                    if (!TextUtil.isEmpty(location) && location.length() > 1) {
                        // "Location":"121.648686,31.465868", //经纬度 以","隔开的
                        String[] split = location.split(",");
                        Longitude_http = split[0];
                        Latitude_http = split[1];
                    } else {
                        Longitude_http = Longitude_local;
                        Latitude_http = Latitude_local;
                    }

                    if (null != list) {
                        title_item_size = list.size();
                        titleRecycleview.setLayoutManager(new GridLayoutManager(mContext, title_item_size));
                        titleRecycleview.setItemAnimator(new DefaultItemAnimator());
                        if (list.size() > 0) {
                            list.get(0).setIsselect(true);
                            Radius_str = list.get(0).getRadius();
                        }
                        title_list = list;
                        if (adapter == null) {
                            return;
                        }
                        adapter.RereshData(list);
                    }
                    compareHttpAndLocalLaLo();
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }

    double Longitude_local_dou;
    double Latitude_local_dou;
    double Longitude_http_dou;
    double Latitude_http_dou;

    /**
     * 后台和定位之间的距离
     */
    private void compareHttpAndLocalLaLo() {
        /**
         * 转化本地经纬度
         */
        Longitude_local_dou = Double.parseDouble(Longitude_local);
        Latitude_local_dou = Double.parseDouble(Latitude_local);
        LatLng latLng_local = new LatLng(Latitude_local_dou, Longitude_local_dou);

        /**
         * 转化获取经纬度
         */
        Longitude_http_dou = Double.parseDouble(Longitude_http);
        Latitude_http_dou = Double.parseDouble(Latitude_http);
        LatLng latLng_http = new LatLng(Latitude_http_dou, Longitude_http_dou);
        Log.i("========>>>>>", "本地-------" + Longitude_local_dou + "----" + Latitude_local_dou);
        Log.i("========>>>>>", "网络-------" + Longitude_http_dou + "----" + Latitude_http_dou);
        float distance = AMapUtils.calculateLineDistance(latLng_local, latLng_http);
        Log.i("========>>>>>", "distance-------" + distance);

        if (distance < 500) {
            tvAddress.setText("当前位置");
            HttpContentData(1, Longitude_local_dou, Latitude_local_dou);
            Longitude_pull_dou = Longitude_local_dou;
            Latitude_pull_dou = Latitude_local_dou;

        } else {
            had_show = (Boolean) SPUtil.get(mContext, "once_show", true);
            if (!had_show) {
                showDialog();
            } else {
                callBackNearbyAddress = (String) SPUtil.get(mContext, "callBackNearbyAddress", "当前位置");
                if (!TextUtil.isEmpty(callBackNearbyAddress)) {
                    tvAddress.setText(callBackNearbyAddress);
                }
                //获取上一次保存的经纬度，请求数据
                String last_callback_longitude = (String) SPUtil.get(mContext, "last_callback_longitude", Longitude_local);
                String last_callback_latitude = (String) SPUtil.get(mContext, "last_callback_latitude", Latitude_local);
                HttpContentData(1, Double.parseDouble(last_callback_longitude), Double.parseDouble(last_callback_latitude));
                Longitude_pull_dou = Double.parseDouble(last_callback_longitude);
                Latitude_pull_dou = Double.parseDouble(last_callback_latitude);
            }
        }
    }

    Intent intent;

    private void showDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(R.layout.dialog_choose_address);
        mAlertDialog.getWindow().findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                HttpContentData(1, Longitude_local_dou, Latitude_local_dou);
                SPUtil.putAndApply(mContext, "once_show", true);

            }
        });

        mAlertDialog.getWindow().findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                intent = new Intent(mContext, GoodsAddressActivity_new.class);
                intent.putExtra("userId", GlobalApplication.getInstance().getPerson(mContext).getUserId());
                activity.startActivityForResult(intent, 10);
                SPUtil.putAndApply(mContext, "once_show", true);
            }
        });
    }

    /**
     * 获取内容数据
     */
    private void HttpContentData(int type, double Longitude, double Latitude) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Radius", "3000");
        hashMap.put("N_Longitude", Longitude + "");
        hashMap.put("N_Latitude", Latitude + "");

        new GetNetUtil(hashMap, Api.Near_GetShopList, mContext) {
            @Override
            public void errorHandle() {
                super.errorHandle();
                srl.finishRefresh();
            }

            @Override
            public void successHandle(String basebean) {
                srl.finishRefresh();
                if (isDestroy) {
                    return;
                }
                if (basebean.contains("200")) {
                    Gson gson = new Gson();
                    BaseListBean<NearShopListBean> bean = gson.fromJson(basebean,
                            new TypeToken<BaseListBean<NearShopListBean>>() {
                            }.getType());
                    if (bean.getStatus().equals("200")) {
                        List<NearShopListBean> listContent = bean.getObj();
                        if (null != listContent) {
                            contentAdapter.RereshData(listContent);
                        }
                        if (null != listContent && listContent.size() == 0) {
                            srl.setVisibility(View.GONE);
                            rlNoOrder.setVisibility(View.VISIBLE);
                        } else {
                            srl.setVisibility(View.VISIBLE);
                            rlNoOrder.setVisibility(View.GONE);
                        }
                    } else {
                        ToastUtils.showToast(mContext, bean.getMsg());
                    }
                } else {
                }
            }
        };
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            HttpTitleData();
        }
    }

    /**
     * 条目点击
     *
     * @param view
     * @param postion
     */
    @Override
    public void onItemClick(View view, int postion) {
        for (int m = 0; m < title_list.size(); m++) {
            title_list.get(m).setIsselect(false);
        }
        title_list.get(postion).setIsselect(true);
        adapter.notifyDataSetChanged();
        Radius_str = title_list.get(postion).getRadius();
        HttpContentData(1, Longitude_local_dou, Latitude_local_dou);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

    @Subscribe
    public void onEventMainThread(FirstEventBus event) {
        if (event != null && event.getMsg().equals("NearbyFriendFragement")) {
            String callback_longitude = event.getCallback_longitude();
            String callback_latitude = event.getCallback_latitude();
            String callback_receiveAddress = event.getCallback_receiveAddress();
            tvAddress.setText(callback_receiveAddress);
            SPUtil.putAndApply(mContext, "callBackNearbyAddress", callback_receiveAddress + "");
            SPUtil.putAndApply(mContext, "last_callback_longitude", callback_longitude + "");
            SPUtil.putAndApply(mContext, "last_callback_latitude", callback_latitude + "");
            Longitude_local_dou = Double.parseDouble(callback_longitude);
            Latitude_local_dou = Double.parseDouble(callback_latitude);
            HttpContentData(1, Longitude_local_dou, Latitude_local_dou);

        }
    }

    @OnClick(R.id.ll_chooseaddress)
    public void onClick() {
        intent = new Intent(mContext, GoodsAddressActivity_new.class);
        intent.putExtra("userId", GlobalApplication.getInstance().getPerson(mContext).getUserId());
        activity.startActivityForResult(intent, 10);

    }

}



