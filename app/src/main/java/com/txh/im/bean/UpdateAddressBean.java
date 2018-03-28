package com.txh.im.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jiajia on 2017/3/15.
 */

@Entity
public class UpdateAddressBean {

    /**
     * AddressId : 85
     * Name : 王五
     * Phone : 13555555555
     * ProvinceCityZoneText : 上海市/上海市/长宁区
     * ProvinceCityZoneName : 上海市,上海市,长宁区
     * ProvinceCityZoneValue : 2,5,30
     * ReceiveAddress : 测试地址王五
     * IsDefault : 0
     */

    private String AddressId;
    private String Name;
    private String Phone;
    private String ProvinceCityZoneText;
    private String ProvinceCityZoneName;
    private String ProvinceCityZoneValue;
    private String ReceiveAddress;
    private String IsDefault;

    @Generated(hash = 553928864)
    public UpdateAddressBean(String AddressId, String Name, String Phone,
            String ProvinceCityZoneText, String ProvinceCityZoneName,
            String ProvinceCityZoneValue, String ReceiveAddress, String IsDefault) {
        this.AddressId = AddressId;
        this.Name = Name;
        this.Phone = Phone;
        this.ProvinceCityZoneText = ProvinceCityZoneText;
        this.ProvinceCityZoneName = ProvinceCityZoneName;
        this.ProvinceCityZoneValue = ProvinceCityZoneValue;
        this.ReceiveAddress = ReceiveAddress;
        this.IsDefault = IsDefault;
    }

    @Generated(hash = 974327449)
    public UpdateAddressBean() {
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

    public String getProvinceCityZoneName() {
        return ProvinceCityZoneName;
    }

    public void setProvinceCityZoneName(String ProvinceCityZoneName) {
        this.ProvinceCityZoneName = ProvinceCityZoneName;
    }

    public String getProvinceCityZoneValue() {
        return ProvinceCityZoneValue;
    }

    public void setProvinceCityZoneValue(String ProvinceCityZoneValue) {
        this.ProvinceCityZoneValue = ProvinceCityZoneValue;
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
