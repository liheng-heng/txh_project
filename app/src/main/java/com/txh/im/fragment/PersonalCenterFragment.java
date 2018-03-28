package com.txh.im.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.squareup.okhttp.Request;
import com.txh.im.Api;
import com.txh.im.R;
import com.txh.im.activity.MainActivity;
import com.txh.im.activity.MyInfoDetailActivity;
import com.txh.im.activity.SetTouxiangActivity;
import com.txh.im.activity.SettingActivity;
import com.txh.im.activity.UploadPapersActivity;
import com.txh.im.adapter.PersonalCenterAdapter;
import com.txh.im.android.AllConstant;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.Basebean;
import com.txh.im.bean.CryptonyBean;
import com.txh.im.bean.GetAskNotReadCountBean;
import com.txh.im.bean.HttpBean;
import com.txh.im.bean.PersonalCenterBean;
import com.txh.im.bean.UploadImagebean;
import com.txh.im.bean.UserInShopBean;
import com.txh.im.demo.DemoActivity;
import com.txh.im.listener.ShopCarMoneyOrNum;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.PicassoUtils;
import com.txh.im.utils.SPUtil;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;
import com.txh.im.utils.UIUtils;
import com.txh.im.widget.CircleImageView;
import com.txh.im.widget.QrDialog;
import com.z_okhttp.callback.ResultCallback;
import com.z_okhttp.request.OkHttpRequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiajia on 2016/12/29.
 * 个人中心数据展示
 */

public class PersonalCenterFragment extends BasicFragment implements View.OnClickListener, PopupWindow.OnDismissListener, ShopCarMoneyOrNum {

    @Bind(R.id.touxiang)
    CircleImageView touxiang;
    @Bind(R.id.tv_head_title)
    TextView tvHeadTitle;
    @Bind(R.id.headLayout)
    RelativeLayout headLayout;
    @Bind(R.id.lv_list)
    ListView lvList;
    @Bind(R.id.tv_unittype)
    TextView tvUnittype;
    @Bind(R.id.ll_head_back)
    LinearLayout llHeadBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.rl_new_myinfo)
    RelativeLayout rlNewMyinfo;
    @Bind(R.id.iv_head_back)
    ImageView ivHeadBack;
    @Bind(R.id.tv_head_back)
    TextView tvHeadBack;
    @Bind(R.id.tv_head_right)
    TextView tvHeadRight;
    @Bind(R.id.ll_head_right)
    LinearLayout llHeadRight;
    @Bind(R.id.srl)
    PullToRefreshLayout srl;

    private Uri imageUri;
    private Uri imageCropUri;
    private static final int REQUESTCODE_PICK = 0; // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1; // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2; // 图片裁切标记
    private static ProgressDialog pd;// 等待进度圈
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;
    private String itemTitle = "";
    private String itemUrl = "";
    int val = UIUtils.dip2px(130);
    List<PersonalCenterBean.ListItemBean> lists = new ArrayList<>();
    PersonalCenterAdapter adapter;
    private PopupWindow popupWindow;
    private int navigationHeight;
    private MainActivity activity;

    private String head_ison;
    private String name;
    private String shop_name;
    private String qr_code_str;

    UserInShopBean userBean = new UserInShopBean();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_personal_center2, null);
        return view;
    }

    @Override
    protected void initData() {
        if (hasCameraPermission()) {
        } else {
            applyCameraPersion();
        }
        if (has_write_external_storage_Permission()) {
        } else {
            apply_write_external_storage_Permission();
        }

        headTitle.setText("我的");
        llHeadRight.setVisibility(View.VISIBLE);
        llHeadRight.setOnClickListener(this);
        tvHeadRight.setText("设置");
        ivHeadBack.setVisibility(View.GONE);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ivHeadBack.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        linearParams.width = UIUtils.dip2px(20);
        linearParams.height = UIUtils.dip2px(20);
        ivHeadBack.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        ivHeadBack.setBackground(getResources().getDrawable(R.drawable.weima));
        llHeadBack.setVisibility(View.GONE);
        llHeadBack.setOnClickListener(this);
        touxiang.setOnClickListener(this);
        rlNewMyinfo.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }

        File file = new File(getSDCardPath() + "/temp.jpg");
        imageUri = FileProvider.getUriForFile(mContext, "com.txh.im.fileprovider", file);
        File cropFile = new File(getSDCardPath() + "/temp_crop.jpg");
        imageCropUri = Uri.fromFile(cropFile);

        /**
         * 安卓7.0适配问题  如再有问题请打开此处
         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            imageUri = FileProvider.getUriForFile(mContext, "com.txh.im.fileprovider", file);
//            Log.i("===========>>", "7.0---imageCropUri-----" + imageCropUri + "-----imageUri-----" + imageUri);
//            imageCropUri = Uri.fromFile(cropFile);
//        } else {
//            imageCropUri = Uri.fromFile(cropFile);
//            imageUri = Uri.fromFile(file);
//            Log.i("===========>>", "非7.0---imageCropUri-----" + imageCropUri + "-----imageUri-----" + imageUri);
//        }

        adapter = new PersonalCenterAdapter(mContext, lists, R.layout.item_personal_center, this, activity);
        lvList.setAdapter(adapter);
        String personalCenterBean = (String) SPUtil.get(mContext, "sp_personalCenterBean", "");
        if (!TextUtil.isEmpty(personalCenterBean)) {
            updateView_sp_data(personalCenterBean);
        } else {
            HttpListData();
        }
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = getResources().getDimensionPixelSize(resourceId);
        getUnreadCountAskMessage();

        srl.setRefreshListener(new BaseRefreshListener() {

            @Override
            public void refresh() {
                HttpListData();
            }

            @Override
            public void loadMore() {
            }
        });
        srl.setCanLoadMore(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.touxiang:
//                openPopupWindow(v);
                Intent intent_set_touxiang = new Intent(mContext, SetTouxiangActivity.class);
                intent_set_touxiang.putExtra("headimage", head_ison);
                startActivity(intent_set_touxiang);
                break;

            case R.id.tv_pick_phone:
                /**
                 * 拍照
                 */
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takeIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takeIntent.putExtra("return-data", true);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                takeIntent.putExtra("noFaceDetection", true);
                takeIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                popupWindow.dismiss();
                break;

            case R.id.tv_pick_zone:
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, REQUESTCODE_PICK);
                popupWindow.dismiss();
                break;

            case R.id.tv_cancel:
                popupWindow.dismiss();
                break;

            case R.id.rl_new_myinfo:
                Intent intent = new Intent(mContext, MyInfoDetailActivity.class);
                ((MainActivity) mContext).startActivityForResult(intent, 100);
                break;

            case R.id.ll_head_back:
                userBean.setUnitName(shop_name);
                userBean.setUserName(name);
                userBean.setImagesHead(head_ison);
                userBean.setUserQRUrl(qr_code_str);
                userBean.setUnitType(GlobalApplication.getInstance().getPerson(mContext).getUnitType());
                new QrDialog(mContext, userBean).show();
                break;

            case R.id.ll_head_right:
                Intent intent3 = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(intent3);

//                Intent intent3 = new Intent(mContext, DemoActivity.class);
//                intent3.putExtra("shopId", GlobalApplication.getInstance().getPerson(mContext).getUnitId());
//                mContext.startActivity(intent3);
                break;
        }
    }

    /**
     * 缓存个人中心数据
     *
     * @param basebean
     */
    private void updateView_sp_data(String basebean) {
        Gson gson = new Gson();
        Basebean<PersonalCenterBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<PersonalCenterBean>>() {
        }.getType());

        if (bean.getStatus().equals("200")) {
            Log.v("-=-=-=-=-=-=-=-=", "解析缓存数据");
            PersonalCenterBean personalCenterBean = bean.getObj();
            PersonalCenterBean.TopHeadBean topHead = personalCenterBean.getTopHead();
            List<PersonalCenterBean.ListItemBean> listItem = personalCenterBean.getListItem();

            if (listItem != null) {
                adapter.refareshData(listItem);
            }
            try {
                updateHeadView(topHead);
            } catch (Exception e) {
                e.printStackTrace();
            }
            head_ison = topHead.getItemValue();
            qr_code_str = topHead.getItemIcon();
            shop_name = topHead.getUnitName();
            name = topHead.getItemTitle();

            HttpListData();

        } else {
            ToastUtils.showToast(mContext, bean.getMsg());
        }
    }

    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(this);
        setOnPopupViewClick(view);
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view) {
        TextView tv_pick_phone, tv_pick_zone, tv_cancel;
        View view1;
        tv_pick_phone = (TextView) view.findViewById(R.id.tv_pick_phone);
        tv_pick_zone = (TextView) view.findViewById(R.id.tv_pick_zone);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        view1 = view.findViewById(R.id.view1);
        tv_pick_phone.setOnClickListener(this);
        tv_pick_zone.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    private void HttpListData() {
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("ActionVersion", AllConstant.ActionVersionFour);
        hashmap.put("IsActionTest", AllConstant.IsActionTest);
        new GetNetUtil(hashmap, Api.GetInfo_GetMyPerson, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                srl.finishRefresh();
            }

            @Override
            public void successHandle(String basebean) {
                srl.finishRefresh();
                if (PersonalCenterFragment.this.isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                Basebean<PersonalCenterBean> bean = gson.fromJson(basebean, new TypeToken<Basebean<PersonalCenterBean>>() {
                }.getType());

                if (bean.getStatus().equals("200")) {
                    Log.v("-=-=-=-=-=-=-=-=", "请求数据数据");
                    SPUtil.remove(mContext, "sp_personalCenterBean");
                    SPUtil.putAndApply(mContext, "sp_personalCenterBean", basebean);
                    PersonalCenterBean personalCenterBean = bean.getObj();
                    PersonalCenterBean.TopHeadBean topHead = personalCenterBean.getTopHead();
                    List<PersonalCenterBean.ListItemBean> listItem = personalCenterBean.getListItem();
                    if (adapter == null) {
                        return;
                    }
                    adapter.refareshData(listItem);
                    try {
                        updateHeadView(topHead);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    head_ison = topHead.getItemValue();
                    qr_code_str = topHead.getItemIcon();
                    shop_name = topHead.getUnitName();
                    name = topHead.getItemTitle();
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
    }

    private void updateHeadView(PersonalCenterBean.TopHeadBean topHead) {
        itemTitle = topHead.getItemTitle();
        itemUrl = topHead.getItemValue();
        if (!TextUtil.isEmpty(itemTitle)) {
            tvHeadTitle.setText(itemTitle);
        }

        if (!TextUtil.isEmpty(topHead.getItemText())) {
            tvUnittype.setVisibility(View.VISIBLE);
            tvUnittype.setText(topHead.getItemText());
        }

        if (!TextUtil.isEmpty(itemUrl)) {
            PicassoUtils.getDefault().CommonUrl(mContext, itemUrl, val, val, R.drawable.wode, touxiang);
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
                    startPhotoZoom(data.getData(), 1);
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;

            case REQUESTCODE_TAKE:// 调用相机拍照
                if (resultCode == 0) {
                    return;
                }
                startPhotoZoom(imageUri, 2);
                break;

            case REQUESTCODE_CUTTING:
                if (data == null) {
                    return;
                }
                // 取得裁剪后的图片
                if (imageCropUri != null) {
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
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
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
            pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
            File file = new File(uri.getPath());

            new OkHttpRequest.Builder().url(Api.head_image).files(new Pair<String, File>("images_head", file)).upload(new ResultCallback<UploadImagebean>() {

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
                        ToastUtils.showToast(mContext, response.getMsg());
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
        new GetNetUtil(map, Api.GetInfo_SaveImagesHead, mContext) {

            @Override
            public void errorHandle() {
                super.errorHandle();
                closeProgress();
            }

            @Override
            public void successHandle(String basebean) {
                if (PersonalCenterFragment.this.isDestroy) {
                    return;
                }
                closeProgress();
                Gson gson = new Gson();
                HttpBean bean = gson.fromJson(basebean, HttpBean.class);
                if (bean.getStatus().equals("200")) {
                    String imagesHead = bean.getObj().getImagesHead();
                    if (!TextUtil.isEmpty(imagesHead)) {
                        PicassoUtils.getDefault().CommonUrl(mContext, imagesHead, val, val, R.drawable.wode, touxiang);
                    }
                    CryptonyBean cryptonyBean = bean.getObj();
                    if (null != cryptonyBean) {
                        GlobalApplication.getInstance().savePerson(cryptonyBean, mContext);
                    }
                    head_ison = bean.getObj().getImagesHead();
                    qr_code_str = bean.getObj().getUserQRUrl();
                } else {
                    ToastUtils.showToast(mContext, bean.getMsg());
                }
            }
        };
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
                // 获得命令执行后在控制台的输出信息
                if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure", "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }

    private ProgressDialog mProgressDialog;

    private void showProgress(String showstr) {
        mProgressDialog = ProgressDialog.show(mContext, "", showstr);
    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public TextView getTvHeadTitle() {
        return tvHeadTitle;
    }

    public void setTvHeadTitle(TextView tvHeadTitle) {
        this.tvHeadTitle = tvHeadTitle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeProgress();
    }

    @Override
    public void getAllMoneyAndNum() {
        activity.httpChangeDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected boolean has_write_external_storage_Permission() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void apply_write_external_storage_Permission() {
        ActivityCompat.requestPermissions((MainActivity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void getUnreadCountAskMessage() {
        new GetNetUtil(null, Api.TX_App_SNS_GetAskNotReadCount, mContext) {
            @Override
            public void successHandle(String base) {
                if (PersonalCenterFragment.this.isDestroy) {
                    return;
                }
                Gson gson = new Gson();
                GetAskNotReadCountBean basebean = gson.fromJson(base, GetAskNotReadCountBean.class);
                int obj = basebean.getObj();
                if (basebean.getStatus().equals("200")) {
                    if (obj > 0) {
                        activity.getNum(obj, "FriendList");
                    }
                }
            }
        };
    }
}


