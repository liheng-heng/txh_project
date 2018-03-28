package com.txh.im.bean;

public class GoodsAddressBean {


    /**
     * AddressId : 1
     * Name : 王二
     * Phone : 18601760336
     * ProvinceCityZoneText : 上海市 上海市 长宁区
     * ReceiveAddress : 金钟路188号
     * IsDefault : 1
     * IsSelf
     *
     "Longitude":"121.489612",
     "Latitude":"31.405457"
     *
     */

    private String AddressId;
    private String Name;
    private String Phone;
    private String ProvinceCityZoneText;
    private String ReceiveAddress;
    private String IsDefault;
    private String IsSelf;
    private String Longitude;
    private String Latitude;

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getIsSelf() {
        return IsSelf;
    }

    public void setIsSelf(String isSelf) {
        IsSelf = isSelf;
    }

    public String getAddressId() {
        return AddressId;
    }

    public void setAddressId(String AddressId) {
        this.AddressId = AddressId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getProvinceCityZoneText() {
        return ProvinceCityZoneText;
    }

    public void setProvinceCityZoneText(String ProvinceCityZoneText) {
        this.ProvinceCityZoneText = ProvinceCityZoneText;
    }

    public String getReceiveAddress() {
        return ReceiveAddress;
    }

    public void setReceiveAddress(String ReceiveAddress) {
        this.ReceiveAddress = ReceiveAddress;
    }

    public String getIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(String IsDefault) {
        this.IsDefault = IsDefault;
    }
}
