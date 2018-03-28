package com.txh.im.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.txh.im.utils.NetUtil;
import com.txh.im.utils.ToastUtils;

import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/2/8.
 */

public abstract class BasicFragment extends Fragment {
    public Context mContext;
    public View view;
    private static final int REQUEST_CAMERA_STATE = 3;
    protected boolean isDestroy = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initView(inflater);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTitle();
        initData();
    }

    protected void initTitle() {
    }

    // 初始化布局, 必须由子类实现
    protected abstract View initView(LayoutInflater inflater);

    // 初始化数据, 必须由子类实现
    protected abstract void initData();


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void applyCameraPersion() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_STATE);
    }

    protected void toast(int msg) {
//        MyToast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        ToastUtils.toast(mContext, msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroy = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ButterKnife.unbind(this);
    }
}
