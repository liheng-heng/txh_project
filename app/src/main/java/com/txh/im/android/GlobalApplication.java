package com.txh.im.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.db.DBManager;
import com.txh.im.easeui.DemoHelper;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.z_okhttp.OkHttpClientManager;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/2/21.
 */
public class GlobalApplication extends Application  {

    {
        PlatformConfig.setWeixin("wxbf932561a0315877", "24aaa9986546361740638dff19ded222");
        Config.DEBUG = true;
    }

    private static GlobalApplication sInstance;
    private String TAG = "GlobalApplication";
    //双向链表
    private LinkedList<Activity> mActivities = new LinkedList<>();
    private long mLastPressBackKeyTime = 0;
    public static int isOpen = 0;//0是默认打开,1是默认关闭
    public static String HuanxinUserId = null;//环信id
    public static String HuanxinPwd = null;//环信密码

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //环信初始化
        initHuanxin();
        //友盟初始化
        UMShareAPI.get(this);
        OkHttpClientManager.getInstance().getOkHttpClient().setConnectTimeout(50000, TimeUnit.MILLISECONDS);
        //bugly配置
        CrashReport.initCrashReport(getApplicationContext(), "e0fbfab533", false);
        //二维码初始化
        ZXingLibrary.initDisplayOpinion(this);
        //greendao的初始化
        DBManager.init(this);
        //高德地图初始化
        initAMap();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

//        Thread mainThread = Thread.currentThread(); // 获得主线程
//        // 设置一个未捕获的异常的，统一处理
//        mainThread.setUncaughtExceptionHandler(this);

        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static GlobalApplication getInstance() {
        if (sInstance == null) {
            sInstance = new GlobalApplication();
        }
        return sInstance;
    }

    public void addActivity(Activity activity) {
        if (activity != null) {
            mActivities.add(activity);
        }
    }

    /**
     * 释放指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void clearAllActivity() {
        try {
            for (Activity activity : mActivities) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void exit() {
        Activity activity;
        while (mActivities.size() > 0) {
            activity = mActivities.poll();
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    /**
     * 双击后完全退出应用程序
     */
    public void exitWithDoubleClick() {
        if ((System.currentTimeMillis() - mLastPressBackKeyTime) > 2000) {
            mLastPressBackKeyTime = System.currentTimeMillis();
        } else {
            exit();
        }
    }

    /**
     * 点击退出程序
     */
    public void exitWithSingleClick() {
        clearAllActivity();
        mLocationClient.onDestroy();//高德销毁定位客户端，同时销毁本地定位服务。
        exit();
    }

    /**
     * 保存用户信息
     */
    public void savePerson(CryptonyBean user, Context context) {

        /**
         "UserId": "73",//用户ID
         "UserName": "",//昵称
         "Sex": "0",//性别：1男，2女，0未知
         "Phone": "18601760347",//手机号
         "ImagesHead": "",//用户头像
         "UserQRUrl": "http://img.tianxiahuo.cn/public/NetFile/20170307/da843ec5ff9f91e847133ca45385bc9.png",//用户二维码图片url
         "IsSetLoginPwd": "0",//是否设置了登录密码
         "IsSetPayPwd": "0",//是否设置了支付密码
         "RoleId": "",
         "TXHCode": "nslbu248885****",//天下货号
         "UserToken": "34445da9-ec37-44ca-9944-3a48f806c044",//用户自动登录后usertoken
         "HXIMPwd":"cb4e8ca283f3bdf9489454177db3b076"
         "UnitType":"1",//1买家，2卖家
         "UnitId":"10"//UnitType对应的商户ID，如配送商的ID
         longitude;
         latitude;
         */

        if (!TextUtil.isEmpty(user.getLongitude())) {
            SPUtil.putAndApply(context, "longitude", user.getLongitude());
        }

        if (!TextUtil.isEmpty(user.getLatitude())) {
            SPUtil.putAndApply(context, "latitude", user.getLatitude());
        }

        if (!TextUtil.isEmpty(user.getUnitType())) {
            SPUtil.putAndApply(context, "UnitType", user.getUnitType());
        }

        if (!TextUtil.isEmpty(user.getUnitId())) {
            SPUtil.putAndApply(context, "UnitId", user.getUnitId());
        }

        if (!TextUtil.isEmpty(user.getUserId())) {
            SPUtil.putAndApply(context, "UserId", user.getUserId());
        }

        if (!TextUtil.isEmpty(user.getUserName())) {
            SPUtil.putAndApply(context, "UserName", user.getUserName());
        }

        if (!TextUtil.isEmpty(user.getSex())) {
//        SPUtil.putAndApply(context, "Sex", user.getSex());
        }

        if (!TextUtil.isEmpty(user.getPhone())) {
            SPUtil.putAndApply(context, "Phone", user.getPhone());
        }

        if (!TextUtil.isEmpty(user.getImagesHead())) {
            SPUtil.putAndApply(context, "ImagesHead", user.getImagesHead());
        }

        if (!TextUtil.isEmpty(user.getUserQRUrl())) {
            SPUtil.putAndApply(context, "UserQRUrl", user.getUserQRUrl());
        }

        if (!TextUtil.isEmpty(user.getIsSetLoginPwd())) {
            SPUtil.putAndApply(context, "IsSetLoginPwd", user.getIsSetLoginPwd());
        }

        if (!TextUtil.isEmpty(user.getIsSetPayPwd())) {
            SPUtil.putAndApply(context, "IsSetPayPwd", user.getIsSetPayPwd());
        }

        if (!TextUtil.isEmpty(user.getRoleId())) {
            SPUtil.putAndApply(context, "RoleId", user.getRoleId());
        }

        if (!TextUtil.isEmpty(user.getTXHCode())) {
            SPUtil.putAndApply(context, "TXHCode", user.getTXHCode());
        }

        if (!TextUtil.isEmpty(user.getUserToken())) {
            SPUtil.putAndApply(context, "UserToken", user.getUserToken());
        }

        if (!TextUtil.isEmpty(user.getHXIMPwd())) {
            SPUtil.putAndApply(context, "HXIMPwd", user.getHXIMPwd());
        }
    }

    /**
     * 退出 清空数据
     */
    public void clearUserData(Context context) {
        CryptonyBean user = new CryptonyBean();
        user.setUserId("N");
        user.setUserName("N");
//        user.setSex("N");
        user.setPhone("N");
        user.setImagesHead("N");
        user.setUserQRUrl("N");
        user.setIsSetLoginPwd("N");
        user.setIsSetPayPwd("N");
        user.setRoleId("N");
        user.setTXHCode("N");
        user.setUserToken("N");
        user.setHXIMPwd("N");
        user.setUnitType("N");
        user.setUnitId("N");
        user.setLatitude("N");
        user.setLongitude("N");
        savePerson(user, context);
    }

    /**
     * 获取 person 信息
     *
     * @return
     */
    public CryptonyBean getPerson(Context context) {
        CryptonyBean user = new CryptonyBean();
        user.setUnitType((String) SPUtil.get(context, "longitude", "N"));
        user.setUnitType((String) SPUtil.get(context, "latitude", "N"));
        user.setUnitType((String) SPUtil.get(context, "UnitType", "N"));
        user.setUnitId((String) SPUtil.get(context, "UnitId", "N"));
        user.setUserId((String) SPUtil.get(context, "UserId", "N"));
        user.setUserName((String) SPUtil.get(context, "UserName", "N"));
//        user.setSex((String) SPUtil.get(context, "Sex", "N"));
        user.setPhone((String) SPUtil.get(context, "Phone", "N"));
        user.setImagesHead((String) SPUtil.get(context, "ImagesHead", "N"));
        user.setUserQRUrl((String) SPUtil.get(context, "UserQRUrl", "N"));
        user.setIsSetLoginPwd((String) SPUtil.get(context, "IsSetLoginPwd", "N"));
        user.setIsSetPayPwd((String) SPUtil.get(context, "IsSetPayPwd", "N"));
        user.setRoleId((String) SPUtil.get(context, "RoleId", "N"));
        user.setTXHCode((String) SPUtil.get(context, "TXHCode", "N"));
        user.setUserToken((String) SPUtil.get(context, "UserToken", "N"));
        user.setHXIMPwd((String) SPUtil.get(context, "HXIMPwd", "N"));


        return user;
    }

    public static String getVersionName() {
        String appVersion = "0";
        PackageManager manager = sInstance.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(sInstance.getPackageName(), 0);
            appVersion = info.versionName; // 版本名
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appVersion;
    }

    public static int getVersion() {
        int appVersion = 0;
        PackageManager manager = sInstance.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(sInstance.getPackageName(), 0);
            appVersion = info.versionCode; // 版本名
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appVersion;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public void saveLaLo(double Latitude, double Longitude) {
        if (!TextUtil.isEmpty(Longitude + "")) {
            String longitude_only = (String) SPUtil.get(getApplicationContext(), "longitude_only", "");
            if (!TextUtil.isEmpty(longitude_only)) {
                SPUtil.remove(getApplicationContext(), "longitude_only");
            }
            SPUtil.putAndApply(getApplicationContext(), "longitude_only", Longitude + "");
        }

        if (!TextUtil.isEmpty(Latitude + "")) {
            String latitude_only_only = (String) SPUtil.get(getApplicationContext(), "latitude_only", "");
            if (!TextUtil.isEmpty(latitude_only_only)) {
                SPUtil.remove(getApplicationContext(), "latitude_only");
            }
            SPUtil.putAndApply(getApplicationContext(), "latitude_only", Latitude + "");
        }
    }

    public String getlongitude() {
        String longitude_only = (String) SPUtil.get(getApplicationContext(), "longitude_only", "");
        return longitude_only;

    }

    public String getlatitude() {
        String latitude_only_only = (String) SPUtil.get(getApplicationContext(), "latitude_only", "");
        return latitude_only_only;

    }

    private void initHuanxin() {
        DemoHelper.getInstance().init(this);
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    saveLaLo(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };
    //声明AMapLocationClientOption对象,AMapLocationClientOption对象用来设置发起定位的模式和相关参数
    public AMapLocationClientOption mLocationOption = null;

    private void initAMap() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(3000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


}