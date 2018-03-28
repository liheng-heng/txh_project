package com.txh.im.bean;

/**
 * Created by jiajia on 2017/4/26.
 */

public class HomeShopBean {

    /**
     * UnitId : 11
     * UnitName : 测试
     */

    private int UnitId;
    private String UnitName;

    public int getUnitId() {
        return UnitId;
    }

    public void setUnitId(int UnitId) {
        this.UnitId = UnitId;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    @Override
    public String toString() {
        return "HomeShopBean{" +
                "UnitId=" + UnitId +
                ", UnitName='" + UnitName + '\'' +
                '}';
    }
}
