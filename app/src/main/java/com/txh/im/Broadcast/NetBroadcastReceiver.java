package com.txh.im.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.txh.im.base.BasicActivity;
import com.txh.im.utils.NetUtil;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushView;

/**
 * Created by liheng on 2017/6/9.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    public NetEvevt evevt = BasicActivity.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            if (evevt != null) {
                evevt.onNetChange(netWorkState);
            }
        }

    }

    // 自定义接口
    public interface NetEvevt {
        public void onNetChange(int netMobile);
    }
}
