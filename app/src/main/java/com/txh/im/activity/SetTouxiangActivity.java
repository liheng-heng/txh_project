package com.txh.im.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.android.GlobalApplication;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.bean.UploadImagebean;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.PicassoUtils;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.z_okhttp.callback.ResultCallback;
import com.z_okhttp.request.OkHttpRequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by liheng on 2017/6/13.
 */

public class SetTouxiangActivity extends BasicActivity implements View.OnClickListener {

    ImageView iv_image;
    TextView tv_pick_phone;
    TextView tv_pick_zone;
    LinearLayout ll_head_back;
    TextView head_title;
    LinearLayout ll_image_layour;
    private Uri imageUri;
    private Uri imageCropUri;
    private static final int REQUESTCODE_PICK = 0; // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1; // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2; // 图片裁切标记
    private static ProgressDialog pd;// 等待进度圈
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;
    int val = UIUtils.dip2px(130);
    private String headimage = "";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_set_touxiang);
    }

    @Override
    protected void initTitle() {
        headimage = getIntent().getStringExtra("headimage");
        iv_image = (ImageView) findViewById(R.id.iv_image);
        head_title = (TextView) findViewById(R.id.head_title);
        ll_image_layour = (LinearLayout) findViewById(R.id.ll_image_layour);
        ll_head_back = (LinearLayout) findViewById(R.id.ll_head_back);
        tv_pick_phone = (TextView) findViewById(R.id.tv_pick_phone);
        tv_pick_zone = (TextView) findViewById(R.id.tv_pick_zone);
        ll_head_back.setOnClickListener(this);
        tv_pick_phone.setOnClickListener(this);
        tv_pick_zone.setOnClickListener(this);
    }

    @Override
    public void initData() {
        head_title.setText("设置个人头像");
        if (hasCameraPermission()) {
        } else {
            applyCameraPersion();
        }
        if (has_write_external_storage_Permission()) {
        } else {
            apply_write_external_storage_Permission();
        }
        if (ContextCompat.checkSelfPermission(SetTouxiangActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SetTouxiangActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }

        File file = new File(getSDCardPath() + "/temp.jpg");
//        imageUri = FileProvider.getUriForFile(SetTouxiangActivity.this, "com.txh.im.fileprovider", file);
        File cropFile = new File(getSDCardPath() + "/temp_crop.jpg");
//        imageCropUri = Uri.fromFile(cropFile);

        /**
         * 安卓7.0适配问题  如再有问题请打开此处
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(SetTouxiangActivity.this, "com.txh.im.fileprovider", file);
            imageCropUri = Uri.fromFile(cropFile);
            Log.i("===========>>", "7.0---imageCropUri-----" + imageCropUri + "-----imageUri-----" + imageUri);
        } else {
            imageUri = Uri.fromFile(file);
            imageCropUri = Uri.fromFile(cropFile);
            Log.i("===========>>", "非7.0---imageCropUri-----" + imageCropUri + "-----imageUri-----" + imageUri);
        }

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll_image_layour.getLayoutParams();
        linearParams.height = width;
        linearParams.width = width;
        ll_image_layour.setLayoutParams(linearParams);
        if (!TextUtil.isEmpty(headimage)) {
            PicassoUtils.getDefault().CommonUrl(SetTouxiangActivity.this, headimage, val, val, R.drawable.default_head_fang, iv_image);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head_back:
                finish();
                break;

            case R.id.tv_pick_phone:
                /**
                 * 拍照
                 */
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takeIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takeIntent.putExtra("return-data", true);
                // 下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                takeIntent.putExtra("noFaceDetection", true);
                takeIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                break;

            case R.id.tv_pick_zone:
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, REQUESTCODE_PICK);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                if (data == null) {
                    return;
                }
                try {
                    Log.i("===========>>", "相册回调路径data.getData()----" + data.getData());
                    startPhotoZoom(data.getData(), 1);
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;

            case REQUESTCODE_TAKE:// 调用相机拍照
                if (resultCode == 0) {
                    return;
                }
                Log.i("===========>>", "相机回调路径--imageUri----" + imageUri);
                startPhotoZoom(imageUri, 2);
                break;

            case REQUESTCODE_CUTTING:
                if (data == null) {
                    return;
                }
                // 取得裁剪后的图片
                if (imageCropUri != null) {
                    Log.i("===========>>", "取得裁剪后的图片路径imageCropUri----" + imageCropUri);
                    setPicToView(imageCropUri);
                }
                break;

            case 5:
                break;
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     * @param type 1,相册  2，相机
     */
    public void startPhotoZoom(Uri uri, int type) {
        //选择存储位置
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true); //设置为不返回数据
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void setPicToView(Uri uri) {

        if (uri != null) {
            // 新线程后台上传服务端
            pd = ProgressDialog.show(SetTouxiangActivity.this, null, "正在上传图片，请稍候...");
            // 要上传的图片文件
            File file = new File(uri.getPath());
            new OkHttpRequest.Builder().url(Api.head_image).files(
                    new Pair<String, File>("images_head", file)).upload(new ResultCallback<UploadImagebean>() {

                @Override
                public void onError(Request arg0, Exception arg1) {
                    arg1.printStackTrace();
                    pd.cancel();
                }

                @Override
                public void onResponse(UploadImagebean response) {
                    pd.cancel();
                    if (!TextUtil.isEmpty(response.getStatus()) && response.getStatus().equals("200")) {
                        String url = response.getUrl();
                        httpPicData(url);
                    } else {
                        ToastUtils.showToast(SetTouxiangActivity.this, response.getMsg());
                    }
                }
            });
        }
    }

    /**
     * 根据服务器返回的头像url，将该url传到中转服务器
     *
     * @param url
     */
    private void httpPicData(String url) {
        showProgress("正在加载");
        HashMap<String, String> map = new HashMap<>();
        map.put("ImagesHead", url);
        new GetNetUtil(map, Api.GetInfo_SaveImagesHead, SetTouxiangActivity.this) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                hideProgress();
            }

            @Override
            public void successHandle(String basebean) {
                hideProgress();
                if (SetTouxiangActivity.this.isFinishing()) {
                    return;
                }
                Log.i("===========>>", basebean);
                Gson gson = new Gson();
                HttpBean bean = gson.fromJson(basebean, HttpBean.class);
                if (bean.getStatus().equals("200")) {
                    String imagesHead = bean.getObj().getImagesHead();
                    if (!TextUtil.isEmpty(imagesHead)) {
                        PicassoUtils.getDefault().CommonUrl(SetTouxiangActivity.this, imagesHead, val, val, R.drawable.default_head_fang, iv_image);
                    }
                    CryptonyBean cryptonyBean = bean.getObj();
                    if (null != cryptonyBean) {
                        GlobalApplication.getInstance().savePerson(cryptonyBean, SetTouxiangActivity.this);
                    }
                } else {
                    ToastUtils.showToast(SetTouxiangActivity.this, bean.getMsg());
                }
            }
        };
    }

    protected boolean has_write_external_storage_Permission() {
        return ContextCompat.checkSelfPermission(SetTouxiangActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void apply_write_external_storage_Permission() {
        ActivityCompat.requestPermissions(SetTouxiangActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure", "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return Environment.getExternalStorageDirectory().getPath();
    }


//    一、从相册截大图：
//
//    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//    intent.setType("image/*");
//    intent.putExtra("crop", "true");
//    intent.putExtra("aspectX", 2);
//    intent.putExtra("aspectY", 1);
//    intent.putExtra("outputX", 600);
//    intent.putExtra("outputY", 300);
//    intent.putExtra("scale", true);
//    intent.putExtra("return-data", false);
//    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//    intent.putExtra("noFaceDetection", true); // no face detection
//    startActivityForResult(intent, CHOOSE_BIG_PICTURE);
//
//    二、从相册截小图
//    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//    intent.setType("image/*");
//    intent.putExtra("crop", "true");
//    intent.putExtra("aspectX", 2);
//    intent.putExtra("aspectY", 1);
//    intent.putExtra("outputX", 200);
//    intent.putExtra("outputY", 100);
//    intent.putExtra("scale", true);
//    intent.putExtra("return-data", true);
//    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//    intent.putExtra("noFaceDetection", true); // no face detection
//    startActivityForResult(intent, CHOOSE_SMALL_PICTURE);


}
