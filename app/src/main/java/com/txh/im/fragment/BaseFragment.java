package com.txh.im.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.txh.im.utils.ToastUtils;

import butterknife.ButterKnife;

/**
 * Created by jiajia on 2017/2/8.
 */

public abstract class BaseFragment extends Fragment {
    public Context mContext;
    public View view;
    private static final int REQUEST_CAMERA_STATE = 3;
    protected boolean isDestroy = false;
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected final String TAG = "LazyLoadFragment";

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
        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTitle();
//        initData();
    }

    protected void initTitle() {
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            initData();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    // 初始化布局, 必须由子类实现
    protected abstract View initView(LayoutInflater inflater);

    // 初始化数据, 必须由子类实现
    protected abstract void initData();

    @Override
    public void onPause() {
        super.onPause();
        isInit = false;
    }

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

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroy = true;
        isInit = false;
        isLoad = false;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ButterKnife.unbind(this);
    }

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
        return;
    }
}
