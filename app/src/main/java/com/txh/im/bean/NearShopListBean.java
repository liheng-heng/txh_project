package com.txh.im.bean;

/**
 * Created by liheng on 2017/6/14.
 */

public class NearShopListBean {


    /**
     * UserId : 1
     * UnitId : 56
     * ShopName : 哇哈哈
     * Head : http://www.baidu.com/jgi.jpg
     * Distance : 200
     * Unit 米
     */

    private String UserId;
    private String UnitId;
    private String ShopName;
    private String Head;
    private String Distance;
    private String Unit;

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String UnitId) {
        this.UnitId = UnitId;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String ShopName) {
        this.ShopName = ShopName;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String Head) {
        this.Head = Head;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String Distance) {
        this.Distance = Distance;
    }
}
