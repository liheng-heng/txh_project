package com.txh.im.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jiajia on 2017/3/15.
 */

@Entity
public class HomeSingleListBean {

    /**
     * UserId : 100000
     * UserName : old wang 3
     * TXHCode : oldwang2
     * ImagesHead : http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png
     * IsFriend : 1
     * IsShop : 1
     * Icon : http://img.tianxiahuo.cn/public/NetFile/20170315/shop_Icon3x.png
     * ProvinceName : 上海市
     * CityName : 上海市
     * ZoneName : 长宁区
     * CountyName : null
     * Address : 金钟路2234号
     * GroupName : null
     */
    @Id(autoincrement = true)
    private Long id;
    private String UserId;
    private String UserName;
    private String TXHCode;
    private String ImagesHead;
    private String IsFriend;
    private String IsShop;
    private String Icon;
    private String ProvinceName;
    private String CityName;
    private String ZoneName;
    private String CountyName;
    private String Address;
    private String GroupName;
    private String LastUpdateTime;
    private String LastMessage;
    private String UnreadMessageCount;
    private String MarkName;
    private String UnitName;
    private String UnitMarkName;
    private String UnitType;
    private String UnitId;
    private int RoleType;
    private String Shops;
    private String MySelfUserId;
    private String Phone;//电话号码,05.04
    private String RoleName;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    @Generated(hash = 893276906)
    public HomeSingleListBean(Long id, String UserId, String UserName, String TXHCode, String ImagesHead, String IsFriend,
            String IsShop, String Icon, String ProvinceName, String CityName, String ZoneName, String CountyName,
            String Address, String GroupName, String LastUpdateTime, String LastMessage, String UnreadMessageCount,
            String MarkName, String UnitName, String UnitMarkName, String UnitType, String UnitId, int RoleType,
            String Shops, String MySelfUserId, String Phone, String RoleName) {
        this.id = id;
        this.UserId = UserId;
        this.UserName = UserName;
        this.TXHCode = TXHCode;
        this.ImagesHead = ImagesHead;
        this.IsFriend = IsFriend;
        this.IsShop = IsShop;
        this.Icon = Icon;
        this.ProvinceName = ProvinceName;
        this.CityName = CityName;
        this.ZoneName = ZoneName;
        this.CountyName = CountyName;
        this.Address = Address;
        this.GroupName = GroupName;
        this.LastUpdateTime = LastUpdateTime;
        this.LastMessage = LastMessage;
        this.UnreadMessageCount = UnreadMessageCount;
        this.MarkName = MarkName;
        this.UnitName = UnitName;
        this.UnitMarkName = UnitMarkName;
        this.UnitType = UnitType;
        this.UnitId = UnitId;
        this.RoleType = RoleType;
        this.Shops = Shops;
        this.MySelfUserId = MySelfUserId;
        this.Phone = Phone;
        this.RoleName = RoleName;
    }

    @Generated(hash = 1089685275)
    public HomeSingleListBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getTXHCode() {
        return this.TXHCode;
    }

    public void setTXHCode(String TXHCode) {
        this.TXHCode = TXHCode;
    }

    public String getImagesHead() {
        return this.ImagesHead;
    }

    public void setImagesHead(String ImagesHead) {
        this.ImagesHead = ImagesHead;
    }

    public String getIsFriend() {
        return this.IsFriend;
    }

    public void setIsFriend(String IsFriend) {
        this.IsFriend = IsFriend;
    }

    public String getIsShop() {
        return this.IsShop;
    }

    public void setIsShop(String IsShop) {
        this.IsShop = IsShop;
    }

    public String getIcon() {
        return this.Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public String getProvinceName() {
        return this.ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public String getCityName() {
        return this.CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getZoneName() {
        return this.ZoneName;
    }

    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
    }

    public String getCountyName() {
        return this.CountyName;
    }

    public void setCountyName(String CountyName) {
        this.CountyName = CountyName;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getGroupName() {
        return this.GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getLastUpdateTime() {
        return this.LastUpdateTime;
    }

    public void setLastUpdateTime(String LastUpdateTime) {
        this.LastUpdateTime = LastUpdateTime;
    }

    public String getLastMessage() {
        return this.LastMessage;
    }

    public void setLastMessage(String LastMessage) {
        this.LastMessage = LastMessage;
    }

    public String getUnreadMessageCount() {
        return this.UnreadMessageCount;
    }

    public void setUnreadMessageCount(String UnreadMessageCount) {
        this.UnreadMessageCount = UnreadMessageCount;
    }

    public String getMarkName() {
        return this.MarkName;
    }

    public void setMarkName(String MarkName) {
        this.MarkName = MarkName;
    }

    public String getUnitName() {
        return this.UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    public String getUnitMarkName() {
        return this.UnitMarkName;
    }

    public void setUnitMarkName(String UnitMarkName) {
        this.UnitMarkName = UnitMarkName;
    }

    public String getUnitType() {
        return this.UnitType;
    }

    public void setUnitType(String UnitType) {
        this.UnitType = UnitType;
    }

    public String getUnitId() {
        return this.UnitId;
    }

    public void setUnitId(String UnitId) {
        this.UnitId = UnitId;
    }

    public int getRoleType() {
        return this.RoleType;
    }

    public void setRoleType(int RoleType) {
        this.RoleType = RoleType;
    }

    public String getShops() {
        return this.Shops;
    }

    public void setShops(String Shops) {
        this.Shops = Shops;
    }

    public String getMySelfUserId() {
        return this.MySelfUserId;
    }

    public void setMySelfUserId(String MySelfUserId) {
        this.MySelfUserId = MySelfUserId;
    }

    public String getRoleName() {
        return this.RoleName;
    }

    public void setRoleName(String RoleName) {
        this.RoleName = RoleName;
    }
}
