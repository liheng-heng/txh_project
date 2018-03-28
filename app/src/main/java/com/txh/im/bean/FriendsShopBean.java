package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/4/10.
 */

public class FriendsShopBean {
    /**
     * UnitId : 13
     * UnitType : 1
     * IsTrue : 1
     * UnitName : null
     * FromUnitId : 0
     * UserMapId : 100010
     * Icon :
     * UnitLogo: null,
     * Item : [{"UserId":100010,"UserName":"18817384535","RoleId":2,"RoleType":0,"RoleName":"主管理员","ImagesHead":"http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png"}]
     */
    private String UnitId;
    private String UnitType;
    private String IsTrue;
    private String UnitName;
    private String FromUnitId;
    private String UserMapId;
    private String Icon;
    private String IconAdd;
    private String IconSub;
    private String UnitLogo;
    private int Expandable;//0代表默认收缩,1代表展开
    private List<FriendsRolesBean> Item;

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    public String getUnitType() {
        return UnitType;
    }

    public void setUnitType(String unitType) {
        UnitType = unitType;
    }

    public String getIsTrue() {
        return IsTrue;
    }

    public void setIsTrue(String isTrue) {
        IsTrue = isTrue;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getFromUnitId() {
        return FromUnitId;
    }

    public void setFromUnitId(String fromUnitId) {
        FromUnitId = fromUnitId;
    }

    public String getUserMapId() {
        return UserMapId;
    }

    public void setUserMapId(String userMapId) {
        UserMapId = userMapId;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getIconAdd() {
        return IconAdd;
    }

    public void setIconAdd(String iconAdd) {
        IconAdd = iconAdd;
    }

    public String getIconSub() {
        return IconSub;
    }

    public void setIconSub(String iconSub) {
        IconSub = iconSub;
    }

    public String getUnitLogo() {
        return UnitLogo;
    }

    public void setUnitLogo(String unitLogo) {
        UnitLogo = unitLogo;
    }

    public int getExpandable() {
        return Expandable;
    }

    public void setExpandable(int expandable) {
        Expandable = expandable;
    }

    public List<FriendsRolesBean> getItem() {
        return Item;
    }

    public void setItem(List<FriendsRolesBean> item) {
        Item = item;
    }

    @Override
    public String toString() {
        return "FriendsShopBean{" +
                "UnitId='" + UnitId + '\'' +
                ", UnitType='" + UnitType + '\'' +
                ", IsTrue='" + IsTrue + '\'' +
                ", UnitName='" + UnitName + '\'' +
                ", FromUnitId='" + FromUnitId + '\'' +
                ", UserMapId='" + UserMapId + '\'' +
                ", Icon='" + Icon + '\'' +
                ", IconAdd='" + IconAdd + '\'' +
                ", IconSub='" + IconSub + '\'' +
                ", UnitLogo='" + UnitLogo + '\'' +
                ", Expandable=" + Expandable +
                ", Item=" + Item +
                '}';
    }
}
