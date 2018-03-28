package com.txh.im.bean;

/**
 * Created by jiajia on 2017/4/18.
 * 好友单位详情
 */

public class FriendsUnitDetailBean {

    /**
     * UnitId : 4
     * UnitName : 15222222222
     * MarkName :
     * ProvinceName :
     * CityName :
     * ZoneName :
     * CountyName :
     * Address :
     */

    private int UnitId;
    private String UnitName;
    private String MarkName;
    private String ProvinceName;
    private String CityName;
    private String ZoneName;
    private String CountyName;
    private String Address;

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

    public String getMarkName() {
        return MarkName;
    }

    public void setMarkName(String MarkName) {
        this.MarkName = MarkName;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
    }

    public String getCountyName() {
        return CountyName;
    }

    public void setCountyName(String CountyName) {
        this.CountyName = CountyName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    @Override
    public String toString() {
        return "FriendsUnitDetailBean{" +
                "UnitId=" + UnitId +
                ", UnitName='" + UnitName + '\'' +
                ", MarkName='" + MarkName + '\'' +
                ", ProvinceName='" + ProvinceName + '\'' +
                ", CityName='" + CityName + '\'' +
                ", ZoneName='" + ZoneName + '\'' +
                ", CountyName='" + CountyName + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }
}
