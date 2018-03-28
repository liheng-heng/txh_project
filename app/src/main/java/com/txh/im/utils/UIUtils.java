package com.txh.im.utils;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


import com.txh.im.android.GlobalApplication;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * ui 工具类
 */
public class UIUtils {

    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return GlobalApplication.getInstance();
    }


    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * dip转换px
     */
    public static float dipFloat(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (dip * scale + 0.5f);
    }

    /**
     * pxz转换dip
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    /**
     * 获取布局
     *
     * @param resId
     * @return
     */
    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /**
     * 获取布局
     *
     * @param resId
     * @return
     */
    public static View inflate(Context Context, int resId) {
        return LayoutInflater.from(Context).inflate(resId, null);
    }


    /**
     * 获取资源
     */
    public static Resources getResources() {

        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }


    public void showToast(String str, Context context) {
        // BaseActivity frontActivity = BaseActivity.getForegroundActivity();
        // if (frontActivity != null) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        // }
    }

    /**
     * px 转 dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 单行多色
     *
     * @param status
     * @return
     */
    public static SpannableString getSpannableString(String status, int color) {
        SpannableString ss = new SpannableString(status);
        ss.setSpan(new ForegroundColorSpan(color), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(color), status.length() - 2, status.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 请求服务器 返回值是否为 200
     */
    public static boolean isConnect(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            String status = jsonObject.getString("status");
            if (status != null && status.equals("200")) {
                return true;
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 返回服务器错误的信息
     */
    public static String errorMsg(String jsonString) {
        String errormsg = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            errormsg = jsonObject.getString("msg");
            return errormsg;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回服务器正确的信息
     */
    public static String succeedMsg(String jsonString) {
        String successmsg = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            successmsg = jsonObject.getString("data");
            return successmsg;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
