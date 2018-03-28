package com.txh.im.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.txh.im.android.GlobalApplication;
import com.txh.im.bean.CryptonyBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * time    : 2017/02/21
 * .net
 */
public class HttpUtils2 {

    private static String charmsg;

    public static HashMap<String, String> addHread(HashMap<String, String> head, String url, Context context) {
        if (head == null) {
            head = new HashMap<String, String>();
        }
        CryptonyBean person = GlobalApplication.getInstance().getPerson(context);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Head", getHead(context, head, url, person));
        hashMap.put("Request", getRequest(context, head, url, person));
        return hashMap;
    }

    public static String getHead(Context context, Map<String, String> map, String URL, CryptonyBean person) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Version", "2.0"); //服务器接口版本号
        hashMap.put("CertType", "MD5");
        charmsg = UUID.randomUUID().toString();
        hashMap.put("Certification", Utils.getMD5(charmsg + "werwerzxsd23234sedwe543453sddsd232434543")); //  Sign
        hashMap.put("DeviceToken", getMyUUID(context));  //设备机型
        hashMap.put("Timestamp", Utils.getCurrentTime());  //时间戳
        //登录之后传用户自己的userid,未登录传 999999999
        if (null == person || TextUtils.isEmpty(person.getUserId()) || person.getUserId().equals("N")) {
            hashMap.put("UserId", "999999999");
        } else {
            hashMap.put("UserId", person.getUserId());
        }
        //登录成功之后传递后台返回的token 否则不传
        if (null != person && !TextUtils.isEmpty(person.getUserToken())) {
            hashMap.put("Token", person.getUserToken());
        }

        hashMap.put("Plat", "Android");
        hashMap.put("OSInformation", Build.VERSION.RELEASE + "-" + Build.MODEL + "-" + Build.MANUFACTURER);//操作版本
        hashMap.put("Channle", GlobalApplication.getVersionName() + ""); //App发布版本  //需要改
        return hashMapToJson(hashMap);
    }


    public static String getRequest(Context context, HashMap<String, String> head, String url, CryptonyBean person) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (head != null && head.size() > 0) {
            hashMap.put("Content", hashMapToJson3(head));
        } else {
            hashMap.put("Content", "");
        }
        hashMap.put("RGuid", charmsg);  //32 位随机字符串
        hashMap.put("Pri", url); //接口
        hashMap.put("UIServiceId", "IM" + GlobalApplication.getVersionName());
        hashMap.put("IsTest", "1");
        //登录之后传用户自己的UIUserId,未登录传 999999999
        if (null == person || TextUtils.isEmpty(person.getUserId()) || person.getUserId().equals("N")) {
            hashMap.put("UIUserId", "999999999");
        } else {
            hashMap.put("UIUserId", person.getUserId());
        }
        return hashMapToJson(hashMap);
    }

    public static String getMyUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    /**
     * 把数据源HashMap转换成json
     *
     * @param map
     */
    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry e = (Map.Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            string += "\"" + e.getValue() + "\",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

    /**
     * 把数据源HashMap转换成json
     *
     * @param map
     */
    public static String hashMapToJson2(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry e = (Map.Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            string += "" + e.getValue() + ",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

    /**
     * 把数据源HashMap转换成json
     *
     * @param map
     */
    public static String hashMapToJson3(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry e = (Map.Entry) it.next();
            if (e.getKey().equals("items")) {
                string += "\\\"" + e.getKey() + "\\\":";
                string += "" + e.getValue() + ",";
            } else {
                string += "\\\"" + e.getKey() + "\\\":";
                string += "\\\"" + e.getValue() + "\\\",";
            }
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }
}
