package com.txh.im.photo;

//import com.crm.app.interfacs.MerchantListener;
//import com.crm.app.interfacs.PriceAuditListener;
//import com.crm.app.nodeAnnotation.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/22.
 */
public class OverallData {
    /**
     * Map Time
     */
    public static final int Map_FLAG = 147;
    /**
     * Map Time
     */
    public static String map_time = "";
    /**
     * Time
     */
    public static double day_count = 0.5;
    /**
     * 坐标x
     */
    public static float x = 0;
    /**
     * 坐标y
     */
    public static float y = 0;
    /**
     * 上午or下午-one
     */
    public static int one_F = 1;
    /**
     * 上午or下午-two
     */
    public static int two_F = 1;
    /**
     * 集合List<WSS.Img>
     */
//    public static List<WSS.Img> imgs = null;
    /**
     * 一个全局的是否刷新的标记
     */
    public static boolean refresh = false;
    /**
     * 一个全局的用户身份
     */
    public static ArrayList<Node> Users = new ArrayList<>();
    /**
     * 一个全局的是否刷新的标记
     */
    public static boolean refreshleave = false;
//    public static MerchantListener merchantListener;
//
//    public static PriceAuditListener priceauditListener;

    /**
     * 一个全局的客户名字
     */
    public static String nickname = "";

    /**
     * 一个全局的图片储存
     */
    public static List<ImageItem> uploads = new ArrayList<ImageItem>();

    /**
     * 单选选中的名字 和 UID
     */

    public static String name = null;
    public static String uid = null;
    /**
     * 判断是否刷新界面（签到）
     */
    public static boolean issign = true;
}
