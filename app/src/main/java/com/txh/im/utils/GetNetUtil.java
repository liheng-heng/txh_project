package com.txh.im.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.txh.im.Api;
import com.txh.im.activity.LoginActivity_new;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.Basebean_Net;
import com.txh.im.bean.HttpErrorBean;
import com.z_okhttp.callback.ResultCallback;
import com.z_okhttp.request.OkHttpRequest;

import java.util.HashMap;

/**
 * Created by jiajia on 2017/2/10.
 */

public abstract class GetNetUtil {
    private OkHttpRequest post;

    public GetNetUtil() {

    }

    public GetNetUtil(HashMap<String, String> map, final String url, final Context context) {

        post = new OkHttpRequest.Builder()
                .url(Api.head_net)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(HttpUtils.addHread(map, url, context))
                .post(new ResultCallback<Basebean_Net>() {

                    @Override
                    public void onError(Request arg0, Exception arg1) {
                        errorHandle();
                    }

                    @Override
                    public void onResponse(Basebean_Net response) {
                        if (response == null || response.getStatus() == null || response.getData().trim().equals("") || response.getData() == null) {
                            ToastUtils.toast(context, "您的网络开小差啦");
                            return;
                        }
                        if (response.getStatus().equals("0") && response.getData() != null) {
                            String data = response.getData();
                            Log.v("--------->", "GetNetUtil-------url---" + url + "--------data---------" + data);
                            Gson gson = new Gson();
                            HttpErrorBean httpErrorBean = gson.fromJson(data, HttpErrorBean.class);
                            if (httpErrorBean.getStatus().equals("20404")) {
                                Log.v("--------->", "20404---url---" + url);
                                /**
                                 * 执行退出操作
                                 */
                                GlobalApplication.getInstance().clearUserData(context);
                                SPUtil.remove(context, "sp_personalCenterBean");
                                SPUtil.remove(context, "sp_MyInfoBean");
                                SPUtil.remove(context, "sp_shoppingTrolleyBean");
                                EMClient.getInstance().logout(true);
                                context.startActivity(new Intent(context, LoginActivity_new.class));
                                ToastUtils.toast(context, httpErrorBean.getMsg());
                                Log.v("--------->", "20404---getMsg---" + httpErrorBean.getMsg());
                            } else {
                                successHandle(data);
                            }
                        } else {
                            Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //网络请求的失败处理
    public void errorHandle() {
    }

    //网络请求成功处理
    public abstract void successHandle(String basebean);

    //取消网络请求
    public void cancleHttp() {
        post.cancel();
    }
}
