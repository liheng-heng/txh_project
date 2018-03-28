package com.txh.im.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.txh.im.AppConfig;
import com.txh.im.R;
import com.txh.im.interfaces.ICallbackResult;
import com.txh.im.utils.TextUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * content   : download service
 * reference : http://blog.leanote.com/post/554ca3792168cb3a61000001
 */
public class DownloadService extends Service {

    /**
     * 传递过来的标示
     */
    public static final String BUNDLE_DOWNLOAD_URL = "download_url";

    public static final String BUNDLE_KEY_TITLE = "title";

    private final String tag = "download";

    /**
     * NOTIFY_ID  标示
     */
    private static final int NOTIFY_ID = 0;
    /**
     * 进度数值
     */
    private int progress;
    private NotificationManager mNotificationManager;
    private boolean canceled;
    /**
     * 下载url
     */
    private String downloadUrl;

    private String mTitle = "正在下载%s";
    private String saveFileName = AppConfig.DEFAULT_SAVE_IMAGE_PATH;
    private DownloadBinder binder;
    private Thread downLoadThread;
    private boolean serviceIsDestroy = false;
    private ICallbackResult callback;
    private NotificationCompat.Builder NotificationBuilder;
    private Context mContext = this;
    private Notification a;
    RemoteViews contentView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        downloadUrl = intent.getStringExtra(BUNDLE_DOWNLOAD_URL);
        saveFileName = saveFileName + getSaveFileName(downloadUrl);
        mTitle = String.format(mTitle, intent.getStringExtra(BUNDLE_KEY_TITLE));
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("aa", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("aa", "destroy");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("aa", "onCreate");
        binder = new DownloadBinder();
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        stopForeground(true);// 这个不确定是否有作用
    }

    private Handler mHandler = new Handler() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // 下载完毕
                    mNotificationManager.cancel(NOTIFY_ID);
                    installApk();
                    break;
                case 2:
                    // 取消通知
                    mNotificationManager.cancel(NOTIFY_ID);
                    break;
                case 1:
                    int rate = msg.arg1;
                    if (rate < 100) {
                        contentView.setTextViewText(R.id.tv_download_state, mTitle + "(" + rate + "%" + ")");
                        contentView.setProgressBar(R.id.pb_download, 100, rate, false);
                    } else {
                        // 下载完毕后变换通知形式
                        NotificationBuilder.setAutoCancel(true);
                        serviceIsDestroy = true;
                        stopSelf();// 停掉服务自身
                    }
                    mNotificationManager.notify(NOTIFY_ID, NotificationBuilder.build());
                    break;
            }
        }
    };

    private void installApk() {
        File apkfile = new File(saveFileName);
        Log.d("----->>service", "saveFileName=" + saveFileName);
        if (!apkfile.exists()) {
            return;
        }
//        installAPK(mContext, apkfile);
        openFile(mContext, apkfile);
    }

    /**
     * 重点在这里
     */
    public void openFile(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uriForFile = FileProvider.getUriForFile(context, "com.txh.im.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
        } else {
            intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
        }
        try {
            context.startActivity(intent);
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }

    }

    public String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }


    public static void installAPK(Context context, File file) {

        /**
         * 7.0安装
         */
        if (file == null || !file.exists())
            return;
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.txh.im.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            uri = Uri.fromFile(file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);


        /**
         * 低版本安装
         */
//        if (file == null || !file.exists())
//            return;
//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        context.startActivity(intent);

    }


    /**
     * 获取apk url
     *
     * @param downloadUrl
     * @return
     */
    private String getSaveFileName(String downloadUrl) {
        if (downloadUrl == null || TextUtil.isEmpty(downloadUrl)) {
            return "";
        }
        String a = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
        return a;
    }

    public class DownloadBinder extends Binder {

        public void start() {
            Log.d("aa", "Binder");
            if (downLoadThread == null || !downLoadThread.isAlive()) {
                progress = 0;
                setUpNotification();
                new Thread() {
                    public void run() {
                        // 下载
                        startDownload();
                    }
                }.start();
            }
        }

        public void cancel() {
            canceled = true;
        }

        public int getProgress() {
            return progress;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public boolean serviceIsDestroy() {
            return serviceIsDestroy;
        }

        public void cancelNotification() {
            mHandler.sendEmptyMessage(2);
        }

        public void addCallback(ICallbackResult callback) {
            DownloadService.this.callback = callback;
        }
    }

    /**
     * 准备安装apk
     */
    private void startDownload() {
        canceled = false;
        downloadApk();
    }

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            File file = new File(AppConfig.DEFAULT_SAVE_IMAGE_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            String apkFile = saveFileName;
            Log.d("service", "apkFile=" + apkFile);
            File saveFile = new File(apkFile);
            try {
                downloadUpdateFile(downloadUrl, saveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 下载文件
     *
     * @param downloadUrl
     * @param saveFile
     * @return
     * @throws IOException
     */
    private long downloadUpdateFile(String downloadUrl, File saveFile) throws IOException {
        int downloadCount = 0;
        int currentSize = 0;
        long totalSize = 0;
        int updateTotalSize = 0;

        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
            if (currentSize > 0) {
                httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
            }
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            httpConnection.setRequestMethod("GET");
            updateTotalSize = httpConnection.getContentLength();
            if (httpConnection.getResponseCode() == 404) {
                throw new Exception("fail!");
            }
            is = httpConnection.getInputStream();
            fos = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[1024];
            int readsize = 0;
            while ((readsize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readsize);
                totalSize += readsize;
                // 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
                if ((downloadCount == 0) || (int) (totalSize * 100 / updateTotalSize) - 10 >= downloadCount) {
                    downloadCount += 10;
                    // 更新进度
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = downloadCount;
                    mHandler.sendMessage(msg);
                    if (callback != null)
                        callback.OnBackResult(progress);
                }
            }

            // 下载完成通知安装
            mHandler.sendEmptyMessage(0);
            // 下载完了，cancelled也要设置
            canceled = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return totalSize;
    }


    /**
     * 设置notification
     */
    private void setUpNotification() {
        CharSequence tickerText = "准备下载";
        long when = System.currentTimeMillis();
        NotificationBuilder = new NotificationCompat.Builder(mContext);
        /**
         * 这个属性一定要加.不然显示不了
         */
        NotificationBuilder.setSmallIcon(R.mipmap.logoicon);
        NotificationBuilder.setWhen(when);
        NotificationBuilder.setTicker(tickerText);
        NotificationBuilder.setOngoing(true);
        /**
         * 自定义试图
         */
        contentView = new RemoteViews(getPackageName(), R.layout.notification_download_show);
        contentView.setTextViewText(R.id.tv_download_state, mTitle);
        NotificationBuilder.setContent(contentView);

        /**
         * 设置隐身跳转----
         */
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFY_ID, NotificationBuilder.build());
    }

}
