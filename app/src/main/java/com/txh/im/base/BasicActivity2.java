package com.txh.im.base;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;

import com.txh.im.Broadcast.NetBroadcastReceiver;
import com.txh.im.android.GlobalApplication;
import com.txh.im.utils.NetUtil;
import com.txh.im.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/21.
 */

public abstract class BasicActivity2 extends AppCompatActivity implements NetBroadcastReceiver.NetEvevt {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private static final int REQUEST_CAMERA_STATE = 3;
    private static final int REQUEST_RECORD_AUDIO_STATE = 4;
    private InputMethodManager mInputMethodManager;
    private ProgressDialog mProgressDialog;
    public boolean isDestroy = false;
    /**
     * 权限请求码
     */
    private int REQUEST_CODE_PERMISSION = 0x00099;

    public static NetBroadcastReceiver.NetEvevt evevt;
    /**
     * 网络类型
     */
    private int netMobile;

    @Override
    @JavascriptInterface
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        GlobalApplication.getInstance().addActivity(this);
        initView();
        ButterKnife.bind(this);
        initTitle();
        initMap(savedInstanceState);
        initData();
        evevt = this;
        inspectNet();
    }

    protected void initMap(Bundle savedInstanceState) {
    }

    ;

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(BasicActivity2.this);
        return isNetConnect();

        // if (netMobile == 1) {
        // System.out.println("inspectNet：连接wifi");
        // } else if (netMobile == 0) {
        // System.out.println("inspectNet:连接移动数据");
        // } else if (netMobile == -1) {
        // System.out.println("inspectNet:当前没有网络");
        //
        // }
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
        // TODO Auto-generated method stub
        this.netMobile = netMobile;
        isNetConnect();

    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;

        }
        return false;
    }

    /**
     * 在setContentView之前触发的方法
     */
    protected void beforeSetContentView() {

    }

    protected abstract void initView();

    protected abstract void initTitle();

    public abstract void initData();


    @Override
    @JavascriptInterface
    protected void onResume() {
        super.onResume();
    }

    @Override
    @JavascriptInterface
    protected void onPause() {
        super.onPause();
        ToastUtils.cancelToast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        GlobalApplication.getInstance().removeActivity(this);
        isDestroy = true;
    }

    protected boolean hasWriteExternalStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void applyPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    protected boolean hasReadPhoneStatePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void applyPhonePersion() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
    }


    protected void toast(int msg) {

        ToastUtils.toast(this, msg);

    }

    protected void toast(String msg) {

        ToastUtils.toast(this, msg);

    }

    /**
     * 权限请求
     *
     * @param permissions
     * @param requstCode
     */
    public void requestPermission(String[] permissions, int requstCode) {
        this.REQUEST_CODE_PERMISSION = requstCode;
        if (checkSelfPermissions(permissions)) {
            permissionSuccess(requstCode);
        } else {
            List<String> needPermissions = getPermissionList(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    private boolean checkSelfPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    @JavascriptInterface
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_PERMISSION == requestCode) {
            if (verifyPermission(grantResults)) {
                permissionSuccess(REQUEST_CODE_PERMISSION);
            } else {
                permissionFail(REQUEST_CODE_PERMISSION);
                showTipsDialog();
            }
        }
    }

    /**
     * 确认权限是否授权成功
     *
     * @param grantsResults
     * @return
     */
    public boolean verifyPermission(int[] grantsResults) {
        for (int grant : grantsResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将授权未通过的权限放入集合
     *
     * @param permissions
     * @return
     */
    public List<String> getPermissionList(String[] permissions) {
        List<String> requestList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                requestList.add(permission);
            }
        }
        return requestList;
    }

    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    /**
     * 权限授权成功
     *
     * @param requestCode
     */

    public void permissionSuccess(int requestCode) {

    }

    /**
     * 权限授权失败
     *
     * @param requestCode
     */

    public void permissionFail(int requestCode) {
    }

    protected boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void applyCameraPersion() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_STATE);
    }

    protected boolean hasRECORD_AUDIOPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void applyRECORD_AUDIOPersion() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_STATE);
    }

    protected void hideKeyBoard() {
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (mInputMethodManager == null) {
            return;
        }
        if (getCurrentFocus() != null) {
            mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        //jiajia,4.7,防止activity已不存在还会打开dialog
        if (!this.isFinishing()) {
            mProgressDialog.show();
        }
    }

    protected void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
        }
        // mProgressDialog.setMessage(msg);
        mProgressDialog.show();

    }

    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
