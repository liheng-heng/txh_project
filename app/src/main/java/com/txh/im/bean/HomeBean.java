package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/3/15.
 */

public class HomeBean {
    private String UserId;
    private String UserName;
    private String ImagesHead;
    private String IsShop;
    private String Icon;
    private List<FriendsUnitDetailBean> Shops;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getImagesHead() {
        return ImagesHead;
    }

    public void setImagesHead(String imagesHead) {
        ImagesHead = imagesHead;
    }

    public String getIsShop() {
        return IsShop;
    }

    public void setIsShop(String isShop) {
        IsShop = isShop;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public List<FriendsUnitDetailBean> getShops() {
        return Shops;
    }

    public void setShops(List<FriendsUnitDetailBean> shops) {
        Shops = shops;
    }

    @Override
    public String toString() {
        return "HomeBean{" +
                "UserId='" + UserId + '\'' +
                ", UserName='" + UserName + '\'' +
                ", ImagesHead='" + ImagesHead + '\'' +
                ", IsShop='" + IsShop + '\'' +
                ", Icon='" + Icon + '\'' +
                ", Shops=" + Shops +
                '}';
    }
}
