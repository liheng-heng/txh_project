package com.txh.im.utils;

import android.support.v4.widget.SwipeRefreshLayout;

public class SwipeRefreshHelp {

    /**
     * 自动刷新
     */
    public static void AutoRefresh(final SwipeRefreshLayout srl) {
        srl.postDelayed(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(true);
            }
        }, 0);
    }

    /**
     * 自动关闭
     */

    public static void AutoFinish(final SwipeRefreshLayout srl) {
        try {
            srl.postDelayed(new Runnable() {
                @Override
                public void run() {
                    srl.setRefreshing(false);
                }
            }, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
