package com.txh.im.bean;

/**
 * Created by jiajia on 2017/5/9.
 */

public class UnitBean {

    /**
     * UnitId : 100
     * UnitName : 张三小店
     * UnitLogo :
     * GroupId : 13
     * GroupName : A组
     *
     "UserId":"500842",
     *
     */

    private String UnitId;
    private String UnitName;
    private String UnitLogo;
    private String GroupId;
    private String GroupName;
    private String IsAdd;
    private String IsOrder;//是否显示代下单 0否，1是
    private int IsSelected;//0表示未选中,1表示选中
    private String UPriceStr;
    private String UserId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String UnitId) {
        this.UnitId = UnitId;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    public String getUnitLogo() {
        return UnitLogo;
    }

    public void setUnitLogo(String UnitLogo) {
        this.UnitLogo = UnitLogo;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getIsAdd() {
        return IsAdd;
    }

    public void setIsAdd(String isAdd) {
        IsAdd = isAdd;
    }

    public int getIsSelected() {
        return IsSelected;
    }

    public void setIsSelected(int isSelected) {
        IsSelected = isSelected;
    }

    public String getIsOrder() {
        return IsOrder;
    }

    public void setIsOrder(String isOrder) {
        IsOrder = isOrder;
    }

    public String getUPriceStr() {
        return UPriceStr;
    }

    public void setUPriceStr(String UPriceStr) {
        this.UPriceStr = UPriceStr;
    }
}
