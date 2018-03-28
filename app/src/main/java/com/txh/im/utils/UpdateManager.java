package com.txh.im.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.activity.MainActivity;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.UpdateBean;
import com.txh.im.interfaces.ICallbackResult;
import com.txh.im.service.DownloadService;

import java.util.HashMap;

/**
 * creator :  liheng
 * time    :  2017/4/25
 * content :
 */
public class UpdateManager {

    private UpdateBean mUpdate;
    private Context mContext;
    private boolean isShow = false;
    private ProgressDialog _waitDialog;
    private AlertDialog alertDialog;

    private String UpdateType_str = "";//0或空时无需更新，1不需要强更，2强更

    public UpdateManager(Context context, boolean isShow) {
        this.mContext = context;
        this.isShow = isShow;
    }

    private void onFinshCheck() {
        if (haveNew()) {
            showUpdateInfo();
        } else {
            if (isShow) {
                showLatestDialog();
            }
        }
    }

    public void checkUpdate() {
        if (isShow) {
            showCheckDialog();
        }
        HttpCheckVersion();
    }

    public void HttpCheckVersion() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("VersionApp", "txh");
        hashMap.put("ApplicationId", Utils.GetPhoneNum(mContext));
        new GetNetUtil(hashMap, Api.AppSys_GetVersionUpdate, mContext) {
            @Override
            public void errorHandle() {
                super.errorHandle();
                hideCheckDialog();
                if (isShow) {
                    showFaileDialog();
                }
            }

            @Override
            public void successHandle(String basebean) {
                /**
                 "VersionCode": "1.0.1", //版本号
                 "VersionUrl": "", //下载地址
                 "VersionContent": "", //版本更新内容
                 "UpdateType": "1" //0或空时无需更新，1不需要强更，2强更
                 */
                Gson gson = new Gson();
                Basebean<UpdateBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<UpdateBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    hideCheckDialog();
                    mUpdate = bean.getObj();
                    if (!TextUtil.isEmpty(mUpdate.getUpdateType()) && !"0".equals(mUpdate.getUpdateType())) {
                        onFinshCheck();
                    }
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }

    public boolean haveNew() {
        if (this.mUpdate == null) {
            return false;
        }
        boolean haveNew = false;

        float curVersionCode = getVersionName(GlobalApplication.getInstance().getPackageName());//100.0
        String versionCode = mUpdate.getVersionCode();//1.0.1
        String replace_versionCode = versionCode.replace(".", "");//101.0
        float replace_versionCode_float = Float.parseFloat(replace_versionCode);
        if (curVersionCode < replace_versionCode_float) {
            haveNew = true;
        }
        return isNext(haveNew);
    }

    /**
     * 判断版本是否以后更新
     *
     * @param flag
     * @return
     */
    public boolean isNext(boolean flag) {
        //手动版本更新
        if (isShow) {
            return flag;
        } else {
            String codeNext = (String) SPUtil.get(mContext, "codeNextUsersion", "-1");
            if (!codeNext.equals("-1")) {
                if (codeNext.equals(mUpdate.getVersionCode())) {
                    return true;
                }
            }
            return flag;
        }
    }

    public static int getVersionCode(String packageName) {
        int versionCode = 0;
        try {
            versionCode = GlobalApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    public static float getVersionName(String packageName) {
        float versionCode = 0;
        try {
            String versionName = GlobalApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0).versionName;
            String versionName_replace = versionName.replace(".", "");
            versionCode = Float.parseFloat(versionName_replace);
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    private void showUpdateInfo() {
        if (mUpdate == null) {
            return;
        }
        /**
         * 0或空时无需更新，1不需要强更，2强更
         */
        if (TextUtil.isEmpty(mUpdate.getUpdateType()) || mUpdate.getUpdateType().equals("0")) {
        } else {
            boolean flag = false;
            if (mUpdate.getUpdateType().equals("1")) {
                //非强制更新
                flag = false;
            }
            if (mUpdate.getUpdateType().equals("2")) {
                //强更新
                flag = true;
            }
            alertDialog = DialogHelp.getCustomerViewUpdateApk(mContext, mUpdate, flag, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    /**
                     * 模拟apk下载
                     */
//                    String down_url = "http://img.tianxiahuo.cn/version/app-release.apk";
//                    openDownLoadService(mContext, down_url, "天下货IM");
//                    if (has_write_external_storage_Permission()) {
//                        openDownLoadService(mContext, mUpdate.getVersionUrl(), "天下货");
//                        Log.i("------>>", "有权限");
//                    } else {
//                        apply_write_external_storage_Permission();
//                        Log.i("------>>", "没有权限");
//                    }
                    openDownLoadService(mContext, mUpdate.getVersionUrl(), "天下货");
                    alertDialog.cancel();
                }
            });
        }
    }

    protected boolean has_write_external_storage_Permission() {
        return ContextCompat.checkSelfPermission(
                mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void apply_write_external_storage_Persion() {
        ActivityCompat.requestPermissions((MainActivity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    public static void openDownLoadService(Context context, String downurl, String tilte) {

        final ICallbackResult callback = new ICallbackResult() {

            @Override
            public void OnBackResult(Object s) {

            }
        };

        ServiceConnection conn = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("aa", "onServiceDisconnected");
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
                binder.addCallback(callback);
                binder.start();
                Log.d("aa", "onServiceConnected");
            }
        };
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_DOWNLOAD_URL, downurl);
        intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
        context.startService(intent);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private void showLatestDialog() {
        DialogHelp.getMessageDialog(mContext, "已经是新版本了").show();
    }

    private void showFaileDialog() {
        DialogHelp.getMessageDialog(mContext, "网络异常，无法获取新版本信息").show();
    }

    private void hideCheckDialog() {
        if (_waitDialog != null) {
            _waitDialog.dismiss();
        }
    }

    private void showCheckDialog() {
        if (_waitDialog == null) {
            _waitDialog = DialogHelp.getWaitDialog((Activity) mContext, "正在获取新版本信息...");
        }
        _waitDialog.show();
    }
}
