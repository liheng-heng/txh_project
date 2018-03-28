package com.txh.im.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.txh.im.widget.CustomSystemToast;
import com.txh.im.widget.CustomToast;

/**
 * Created by Administrator on 2017/2/21.
 */
public class ToastUtils {

    private static CustomToast custom;
    private static Handler handler = new Handler();
    private static CustomSystemToast toast;

    /**
     * 这个地方有待改进。
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        MyToast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int text) {
        MyToast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 转圈加文字
     *
     * @param context
     * @param text
     */
    public static void showToastSmile(Context context, int text) {
        if (context == null) return;
        custom = CustomToast.makeTextSmilewitPic(context, text, Toast.LENGTH_SHORT);
        custom.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                custom.cancel();
            }
        }, 1000);
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
        if (custom != null) {
            custom.cancel();
        }
    }

    public static void toast(Context context, int msg) {
        if (context == null) return;
        if (toast == null) {
            toast = CustomSystemToast.makeText(context, msg);
        } else {//jiajia,12.14,防止多次加载弹框
            toast.setText(msg);
        }
        toast.show();
    }

    public static void toast(Context context, String msg) {
        if (context == null) return;
        if (toast == null) {
            toast = CustomSystemToast.makeText(context, msg);
        } else {//jiajia,12.14,防止多次加载弹框
            toast.setText(msg);
        }
        toast.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (custom != null){
                    custom.cancel();
                }
            }
        }, 1500);
    }
}
