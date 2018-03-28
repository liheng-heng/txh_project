package com.txh.im.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.HttpBean;
import com.txh.im.bean.UpdateBean;
import com.txh.im.easeui.DemoHelper;
import com.txh.im.easeui.DemoModel;
import com.txh.im.interfaces.ICallbackResult;
import com.txh.im.service.DownloadService;
import com.txh.im.utils.CallPrpgressDialog;
import com.txh.im.utils.CustomDialog;
import com.txh.im.utils.DialogHelp;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.Utils;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的资料界面
 */
public class SettingActivity extends BasicActivity {

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
    @Bind(R.id.tv_cache_data)
    TextView tvCacheData;
    @Bind(R.id.rl_cache)
    RelativeLayout rlCache;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.rl_about_us)
    RelativeLayout rlAboutUs;
    @Bind(R.id.switch_notification)
    EaseSwitchButton notifySwitch;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.activity_forget_password)
    LinearLayout activityForgetPassword;
    CallPrpgressDialog progeressDialog;
    @Bind(R.id.iv_about_us)
    ImageView ivAboutUs;
    @Bind(R.id.rl_version_us)
    RelativeLayout rlVersionUs;
    @Bind(R.id.iv_app_update)
    ImageView ivAppUpdate;

    private File file;
    private DemoModel model;

    private boolean isCanClick = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("设置");
        progeressDialog = new CallPrpgressDialog(SettingActivity.this);
        //通过Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
        file = getCacheDir();
        String sxx = file.getAbsolutePath();
        Log.v("--------------------", sxx);
        try {
            tvCacheData.setText(getFormatSize(Double.valueOf(getFolderSize(file))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpCheckVersion();

        if (has_write_external_storage_Permission()) {
        } else {
            apply_write_external_storage_Permission();
        }
    }

    @Override
    public void initData() {
        model = DemoHelper.getInstance().getModel();
        if (model.getSettingMsgNotification()) {
            notifySwitch.openSwitch();
        } else {
            notifySwitch.closeSwitch();
        }
    }

    private UpdateBean mUpdate;

    public void HttpCheckVersion() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("VersionApp", "txh");
        hashMap.put("ApplicationId", Utils.GetPhoneNum(SettingActivity.this));
        new GetNetUtil(hashMap, Api.AppSys_GetVersionUpdate, SettingActivity.this) {
            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                Gson gson = new Gson();
                Basebean<UpdateBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<UpdateBean>>() {
                }.getType());
                if (bean.getStatus().equals("200")) {
                    mUpdate = bean.getObj();
                    if (!TextUtil.isEmpty(mUpdate.getUpdateType()) && !"0".equals(mUpdate.getUpdateType())) {
                        updateData();
                    } else {
                        tvVersion.setText("目前版本" + GlobalApplication.getVersionName());
                        ivAppUpdate.setVisibility(View.GONE);
                        rlVersionUs.setEnabled(false);
                    }
                } else {
                    ToastUtils.showToast(SettingActivity.this, bean.getMsg());
                }
            }
        };
    }

    private void updateData() {
        float curVersionCode = getVersionName(GlobalApplication.getInstance().getPackageName());//100.0
        String versionCode = mUpdate.getVersionCode();//1.0.1
        String replace_versionCode = versionCode.replace(".", "");//101.0
        float replace_versionCode_float = Float.parseFloat(replace_versionCode);
        if (curVersionCode < replace_versionCode_float) {
            tvVersion.setText("新版本" + versionCode + "  去更新");
            ivAppUpdate.setVisibility(View.VISIBLE);
            rlVersionUs.setEnabled(true);
        } else {
            tvVersion.setText("目前版本" + GlobalApplication.getVersionName());
            ivAppUpdate.setVisibility(View.GONE);
            rlVersionUs.setEnabled(false);
        }
    }

    @OnClick({R.id.ll_head_back, R.id.rl_cache, R.id.btn_next, R.id.rl_about_us, R.id.rl_switch_notification, R.id.rl_version_us})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;

            case R.id.rl_cache:
                progeressDialog.StartProgress();
                handler.sendEmptyMessage(0);
                break;

            case R.id.btn_next:
                EMClient.getInstance().logout(true);
                Loginout();
                break;

            case R.id.rl_about_us:
                intent = new Intent(this, WebAboutUsAcitivty.class);
                startActivity(intent);
                break;

            case R.id.rl_switch_notification:
                if (notifySwitch.isSwitchOpen()) {
                    notifySwitch.closeSwitch();//消息不提醒
                    model.setSettingMsgNotification(false);
                    model.setSettingMsgSound(false);
                    GlobalApplication.isOpen = 1;//1是关闭
                } else {
                    notifySwitch.openSwitch();//消息可以提醒
                    model.setSettingMsgNotification(true);
                    model.setSettingMsgSound(true);
                    GlobalApplication.isOpen = 0;//0是打开
                }
                break;

            case R.id.rl_version_us:
                if (mUpdate == null) {
                    return;
                }
                if (!isCanClick) {
                    return;
                }
                /**
                 * 0或空时无需更新，1不需要强更，2强更
                 */
                if (TextUtil.isEmpty(mUpdate.getUpdateType()) || mUpdate.getUpdateType().equals("0")) {
                } else {
                    float curVersionCode = getVersionName(GlobalApplication.getInstance().getPackageName());//5.3.0
                    String versionCode = mUpdate.getVersionCode();//1.0.1
                    if (!TextUtil.isEmpty(versionCode)) {
                        String replace_versionCode = versionCode.replace(".", "");//101.0
                        float replace_versionCode_float = Float.parseFloat(replace_versionCode);
                        if (curVersionCode < replace_versionCode_float) {
                            alertDialog = DialogHelp.getCustomerViewUpdateApk(SettingActivity.this, mUpdate, false, new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    openDownLoadService(SettingActivity.this, mUpdate.getVersionUrl(), "天下货");
                                    isCanClick = false;
                                    alertDialog.cancel();
                                }
                            });
                        }
                    }
                }
                break;
        }
    }

    private android.support.v7.app.AlertDialog alertDialog;

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

    private AlertDialog mAlertDialog = null;

    private void Loginout() {
        CustomDialog.Builder builder = new CustomDialog.Builder(SettingActivity.this);
        builder.setMessage("确定要退出登录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HttpLoOut();
                GlobalApplication.getInstance().clearUserData(SettingActivity.this);
                finish();
                startActivity(new Intent(SettingActivity.this, LoginActivity_new.class));
                SPUtil.remove(SettingActivity.this, "sp_personalCenterBean");
                SPUtil.remove(SettingActivity.this, "sp_MyInfoBean");
                SPUtil.remove(SettingActivity.this, "sp_shoppingTrolleyBean");
                SPUtil.putAndApply(SettingActivity.this, "callBackNearbyAddress", "当前位置");
                SPUtil.putAndApply(SettingActivity.this, "last_callback_longitude", GlobalApplication.getInstance().getlongitude());
                SPUtil.putAndApply(SettingActivity.this, "last_callback_latitude", GlobalApplication.getInstance().getlatitude() + "");
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

//        mAlertDialog = new AlertDialog.Builder(SettingActivity.this).create();
//        mAlertDialog.show();
//        mAlertDialog.getWindow().setContentView(R.layout.dialog_loginout);
//        mAlertDialog.getWindow().findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//
//        mAlertDialog.getWindow().findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HttpLoOut();
//                GlobalApplication.getInstance().clearUserData(SettingActivity.this);
//                finish();
//                startActivity(new Intent(SettingActivity.this, LoginActivity_new.class));
//                SPUtil.remove(SettingActivity.this, "sp_personalCenterBean");
//                SPUtil.remove(SettingActivity.this, "sp_MyInfoBean");
//                SPUtil.remove(SettingActivity.this, "sp_shoppingTrolleyBean");
//                SPUtil.putAndApply(SettingActivity.this, "callBackNearbyAddress", "当前位置");
//                SPUtil.putAndApply(SettingActivity.this, "last_callback_longitude", GlobalApplication.getInstance().getlongitude());
//                SPUtil.putAndApply(SettingActivity.this, "last_callback_latitude", GlobalApplication.getInstance().getlatitude() + "");
//                mAlertDialog.dismiss();
//            }
//        });
    }

    private void HttpLoOut() {
        HashMap<String, String> hashMap = new HashMap<>();
        new GetNetUtil(hashMap, Api.GetInfo_ExitLogin, SettingActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
            }

            @Override
            public void successHandle(String basebean) {
                Log.i("----------->", "退出登录-----" + basebean);
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(basebean, HttpBean.class);
                if (httpBean.getStatus().equals("200")) {

                } else {
//                    ToastUtils.showToast(SettingActivity.this, httpBean.getMsg());
                }
            }
        };

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                DeleteFile(file);
                tvCacheData.setText(getFormatSize(Double.valueOf(getFolderSize(file))));
                ToastUtils.showToastSmile(SettingActivity.this, R.string.clearedCache);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progeressDialog.DismissProgress();
        }
    };

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    protected boolean has_write_external_storage_Permission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void apply_write_external_storage_Permission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }


}
