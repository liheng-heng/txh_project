package com.txh.im.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.base.BasicActivity;
import com.txh.im.bean.UploadImagebean;
import com.txh.im.utils.DialogHelp;
import com.txh.im.utils.RoundedTransformation;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.z_okhttp.callback.ResultCallback;
import com.z_okhttp.request.OkHttpRequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liheng on 2017/3/29.
 */

public class UploadPapersActivity extends BasicActivity {

    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.iv_yingye)
    ImageView ivYingye;
    @Bind(R.id.tv_yingye_upload)
    TextView tvYingyeUpload;
    @Bind(R.id.ll_yingye_layout)
    LinearLayout llYingyeLayout;
    @Bind(R.id.iv_menpai)
    ImageView ivMenpai;
    @Bind(R.id.tv_menpai_upload)
    TextView tvMenpaiUpload;
    @Bind(R.id.ll_menpai_layout)
    LinearLayout llMenpaiLayout;
    @Bind(R.id.et_menpaihao)
    EditText etMenpaihao;
    @Bind(R.id.ll_editmenpai_layout)
    LinearLayout llEditmenpaiLayout;
    @Bind(R.id.tv_ok)
    TextView tvOk;

    private Uri imageUri;
    private Uri imageCropUri;
    private static final int REQUESTCODE_PICK = 0; // 营业执照相册选图标记
    private static final int REQUESTCODE_TAKE = 1; // 营业执照相机拍照标记
    private static final int REQUESTCODE_PICK_OTHER = 10; // 门牌照相册选图标记
    private static final int REQUESTCODE_TAKE_OTHER = 11; // 门牌照相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2; // 图片裁切标记
    private static ProgressDialog pd;// 等待进度圈
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;
    int val = UIUtils.dip2px(75);

    private String yingye_picture = "";
    private String menpai_picture = "";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_upload_papers);
    }

    @Override
    protected void initTitle() {
        headTitle.setText("证件上传");

        if (hasCameraPermission()) {
        } else {
            applyCameraPersion();
        }
        if (has_write_external_storage_Permission()) {
        } else {
            apply_write_external_storage_Permission();
        }
        if (ContextCompat.checkSelfPermission(UploadPapersActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadPapersActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }

        String s = getSDCardPath() + "/temp.jpg";
        Log.i("===========>>", "getSDCardPath()temp.jpg-----" + s);

        File file = new File(getSDCardPath() + "/temp.jpg");
//        imageUri = FileProvider.getUriForFile(SetTouxiangActivity.this, "com.txh.im.fileprovider", file);
        File cropFile = new File(getSDCardPath() + "/temp_crop.jpg");
//        imageCropUri = Uri.fromFile(cropFile);

        /**
         * 安卓7.0适配问题  如再有问题请打开此处
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(UploadPapersActivity.this, "com.txh.im.fileprovider", file);
            imageCropUri = Uri.fromFile(cropFile);
            Log.i("===========>>", "7.0---imageCropUri-----" + imageCropUri + "-----imageUri-----" + imageUri);
        } else {
            imageUri = Uri.fromFile(file);
            imageCropUri = Uri.fromFile(cropFile);
            Log.i("===========>>", "非7.0---imageCropUri-----" + imageCropUri + "-----imageUri-----" + imageUri);
        }

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_yingye, R.id.tv_yingye_upload, R.id.iv_menpai, R.id.tv_menpai_upload, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_yingye:
                break;

            case R.id.tv_yingye_upload:
                choose_yingye();
                break;

            case R.id.iv_menpai:
                break;

            case R.id.tv_menpai_upload:
                choose_menpai();
                break;

            case R.id.tv_ok:
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

                    Uri data1 = data.getData();
                    Log.i("===========>>", "data1-----" + data1);

                    setPicToView(data.getData(), "1");
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;

            case REQUESTCODE_TAKE:// 调用相机拍照
                if (resultCode == 0) {
                    return;
                }
                setPicToView(imageUri, "1");
                break;

            case REQUESTCODE_PICK_OTHER:// 直接从相册获取
                if (data == null) {
                    return;
                }
                try {
                    setPicToView(data.getData(), "2");
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;

            case REQUESTCODE_TAKE_OTHER:// 调用相机拍照
                if (resultCode == 0) {
                    return;
                }
                setPicToView(imageUri, "2");
                break;

            case 5:
                break;
        }
    }

    /**
     * 上传图片
     */
    private void setPicToView(Uri uri, final String type) {
        if (uri != null) {
            pd = ProgressDialog.show(UploadPapersActivity.this, null, "正在上传图片，请稍候...");
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
                        Log.i("===========>>", "上传成功后台返回url----" + url);
                        if (type.equals("1")) {
                            //营业执照
                            yingye_picture = response.getUrl();
                            Picasso.with(UploadPapersActivity.this).load(yingye_picture).resize(val, val).transform(
                                    new RoundedTransformation(5, 0)).placeholder(R.drawable.default_head_fang).into(ivYingye);

                        } else {
                            //门牌照
                            menpai_picture = response.getUrl();
                            Picasso.with(UploadPapersActivity.this).load(menpai_picture).resize(val, val).transform(
                                    new RoundedTransformation(5, 0)).placeholder(R.drawable.default_head_fang).into(ivMenpai);

                        }
                    } else {
                        ToastUtils.showToast(UploadPapersActivity.this, response.getMsg());
                    }
                }
            });
        }
    }

    private void choose_yingye() {

        String[] tile = {"拍照", "选择照片"};
        AlertDialog.Builder b = DialogHelp.getSelectDialog(UploadPapersActivity.this,
                "请您选择要进行的操作", tile, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
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

                        } else {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                            // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(pickIntent, REQUESTCODE_PICK);

                        }
                    }
                });
        b.show();

    }

    private void choose_menpai() {

        String[] tile2 = {"拍照", "选择照片"};
        AlertDialog.Builder b2 = DialogHelp.getSelectDialog(UploadPapersActivity.this,
                "请您选择要进行的操作", tile2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
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
                            startActivityForResult(takeIntent, REQUESTCODE_TAKE_OTHER);

                        } else {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                            // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(pickIntent, REQUESTCODE_PICK_OTHER);

                        }
                    }
                });
        b2.show();

    }


    protected boolean has_write_external_storage_Permission() {
        return ContextCompat.checkSelfPermission(UploadPapersActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void apply_write_external_storage_Permission() {
        ActivityCompat.requestPermissions(UploadPapersActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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


}


