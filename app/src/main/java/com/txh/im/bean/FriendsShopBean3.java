package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/4/11.
 */

public class FriendsShopBean3 {
    /**
     * UnitId : 13
     * UnitType : 1
     * IsTrue : 1
     * UnitName : null
     * FromUnitId : 0
     * UserMapId : 100010
     * Icon :
     * UnitLogo: null,
     * Item : {"UserId":100010,"UserName":"18817384535","RoleId":2,"RoleType":0,"RoleName":"主管理员","ImagesHead":"http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png"}
     */
    private String UnitId;
    private String UnitType;
    private String IsTrue;
    private String UnitName;
    private String FromUnitId;
    private String UserMapId;
    private String Icon;
    private String UnitLogo;
    private List<HomeSingleListBean> Item;

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

    public String getUnitLogo() {
        return UnitLogo;
    }

    public void setUnitLogo(String unitLogo) {
        UnitLogo = unitLogo;
    }

    public List<HomeSingleListBean> getItem() {
        return Item;
    }

    public void setItem(List<HomeSingleListBean> item) {
        Item = item;
    }

    @Override
    public String toString() {
        return "FriendsShopBean3{" +
                "UnitId='" + UnitId + '\'' +
                ", UnitType='" + UnitType + '\'' +
                ", IsTrue='" + IsTrue + '\'' +
                ", UnitName='" + UnitName + '\'' +
                ", FromUnitId='" + FromUnitId + '\'' +
                ", UserMapId='" + UserMapId + '\'' +
                ", Icon='" + Icon + '\'' +
                ", UnitLogo='" + UnitLogo + '\'' +
                ", Item=" + Item +
                '}';
    }
}
