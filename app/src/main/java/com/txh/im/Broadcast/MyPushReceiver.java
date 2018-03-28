package com.txh.im.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txh.im.Api;
import com.txh.im.activity.LoginActivity_new;
import com.txh.im.activity.MainActivity;
import com.txh.im.activity.NewFriendsAcitivty;
import com.txh.im.adapter.NewFriendsAdapter;
import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.BaseListBean;
import com.txh.im.bean.GetAskListBean;
import com.txh.im.easeui.DemoHelper;
import com.txh.im.utils.GetNetUtil;
import com.txh.im.utils.Logger;
import com.txh.im.utils.TextUtil;
import com.txh.im.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyPushReceiver extends BroadcastReceiver {

    private static final String TAG = "=======JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String string_all_message = printBundle(bundle);
            Logger.d(TAG, "[MyPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyPushReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//				processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyPushReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d(TAG, "[MyPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

                int notificationNum = DemoHelper.getInstance().getNotifier().getNotificationNum();


            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyPushReceiver] 用户点击打开了通知");
                //打开自定义的Activity
//				Intent i = new Intent(context, TestActivity.class);
//				i.putExtras(bundle);
//				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//				context.startActivity(i);


//                key（其他）:cn.jpush.android.ALERT, value:【天下货】15634567890，您好！您于2017-07-07下的订单（测试-00843002店铺名称）已接单，订单号：H150820170707005！
//                key（EXTRA_EXTRA）:cn.jpush.android.EXTRA, value: [TemplateCode - ConfirmOrder]

//                key（其他）:cn.jpush.android.ALERT, value:您好，测试-0084002昵称已经通过您的请求，快去找他聊天吧
//                key（EXTRA_EXTRA）:cn.jpush.android.EXTRA, value: [TemplateCode - AddFriendOk]


//                AddFriendRequest    添加好友请求 --------跳转新的朋友界面
//                AddFriendOk    发起好友申请后对方收到提醒------打开app即可
                if (string_all_message.contains("AddFriendRequest")) {
                    HttpNewFriends(context, bundle);
//                    Logger.d(TAG, "字段匹配成功-------添加好友请求");
//                    Intent i = new Intent(context, NewFriendsAcitivty.class);
//                    i.putExtras(bundle);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);

                } else {
                    String userToken = GlobalApplication.getInstance().getPerson(context).getUserToken();
                    if (!TextUtil.isEmpty(userToken) && !userToken.equals("N")) {
                        Intent intent_intent = new Intent(context, MainActivity.class);
                        intent_intent.putExtras(bundle);
                        intent_intent.putExtra("home_fragment", "home_fragment");
                        intent_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent_intent);
                    } else {
                        Intent intent_intent2 = new Intent(context, LoginActivity_new.class);
                        intent_intent2.putExtras(bundle);
                        intent_intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent_intent2);
                    }
                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MyPushReceiver]" + intent.getAction() + " connected state change to " + connected);

            } else {
                Logger.d(TAG, "[MyPushReceiver] Unhandled intent - " + intent.getAction());

            }
        } catch (Exception e) {

        }
    }

    //请求新的朋友列表
    private void HttpNewFriends(final Context context, final Bundle bundle) {

        new GetNetUtil(null, Api.TX_App_SNS_GetAskList, context) {
            @Override
            public void successHandle(String base) {
                Gson gson = new Gson();
                BaseListBean<GetAskListBean> basebean = gson.fromJson(base, new TypeToken<BaseListBean<GetAskListBean>>() {
                }.getType());
                if (basebean.getStatus().equals("200")) {
                    if (basebean.getObj() == null || basebean.getObj().size() == 0) {
                        return;
                    }
                    List<GetAskListBean> list = new ArrayList<>();
                    list.addAll(basebean.getObj());
                    Logger.d(TAG, "字段匹配成功-------添加好友请求");
                    Intent i = new Intent(context, NewFriendsAcitivty.class);
                    i.putExtra("asklist", gson.toJson(list));
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);

                } else {
                    ToastUtils.toast(context, basebean.getMsg());
                }
            }
        };
    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey（EXTRA_NOTIFICATION_ID）:" + key + ", value:" + bundle.getInt(key));

            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey（EXTRA_CONNECTION_CHANGE）:" + key + ", value:" + bundle.getBoolean(key));

            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey（EXTRA_EXTRA）:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey（其他）:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
//	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//				}
//			}
//			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//		}
//	}


}
