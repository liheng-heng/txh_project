package com.txh.im.bean;

/**
 * Created by jiajia on 2017/4/19.
 */

public class UserInShopBean {

    /**
     * UserName : 18817384535
     * ImagesHead : http://img.tianxiahuo.cn/public/NetFile/20170419/41cc2d7c6c4e5d57c813351e9279023.jpg
     * RoleId : 4
     * RoleName : 主管理员
     * UnitId : 37
     * UnitType : 2
     * UnitLogo : http://img.tianxiahuo.cn/public/txh_newapp/User/mall_Logo_Icon@3x.png
     * UnitName : 18817384535
     * UserQRUrl : http://img.tianxiahuo.cn/public/NetFile/20170419/acaa2bb297b184ef483221dea11b9b29.png
     * UnitTypeLogo : http://img.tianxiahuo.cn/public/txh_newapp/User/business_friends@3x.png
     */

    private String UserName;
    private String ImagesHead;
    private String RoleId;
    private String RoleName;
    private String UnitId;
    private String UnitType;
    private String UnitLogo;
    private String UnitName;
    private String UserQRUrl;
    private String UnitTypeLogo;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getImagesHead() {
        return ImagesHead;
    }

    public void setImagesHead(String ImagesHead) {
        this.ImagesHead = ImagesHead;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String RoleId) {
        this.RoleId = RoleId;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String RoleName) {
        this.RoleName = RoleName;
    }

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String UnitId) {
        this.UnitId = UnitId;
    }

    public String getUnitType() {
        return UnitType;
    }

    public void setUnitType(String UnitType) {
        this.UnitType = UnitType;
    }

    public String getUnitLogo() {
        return UnitLogo;
    }

    public void setUnitLogo(String UnitLogo) {
        this.UnitLogo = UnitLogo;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    public String getUserQRUrl() {
        return UserQRUrl;
    }

    public void setUserQRUrl(String UserQRUrl) {
        this.UserQRUrl = UserQRUrl;
    }

    public String getUnitTypeLogo() {
        return UnitTypeLogo;
    }

    public void setUnitTypeLogo(String UnitTypeLogo) {
        this.UnitTypeLogo = UnitTypeLogo;
    }

    @Override
    public String toString() {
        return "UserInShopBean{" +
                "UserName='" + UserName + '\'' +
                ", ImagesHead='" + ImagesHead + '\'' +
                ", RoleId='" + RoleId + '\'' +
                ", RoleName='" + RoleName + '\'' +
                ", UnitId='" + UnitId + '\'' +
                ", UnitType='" + UnitType + '\'' +
                ", UnitLogo='" + UnitLogo + '\'' +
                ", UnitName='" + UnitName + '\'' +
                ", UserQRUrl='" + UserQRUrl + '\'' +
                ", UnitTypeLogo='" + UnitTypeLogo + '\'' +
                '}';
    }
}
