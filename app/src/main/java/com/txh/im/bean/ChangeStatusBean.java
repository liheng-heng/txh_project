package com.txh.im.bean;

import java.util.List;

/**
 * Created by liheng on 2017/3/28.
 */

public class ChangeStatusBean {


    /**
     * UserInfo : {"UnitId":29,"UnitType":2}
     * MenuList : [{"Title":"首页","ImgUrl":"http://img.tianxiahuo.cn/public/NetFile/20170315/3ff2f14c0762cc734e64b528e3b415d.png","SelectedImgUrl":"http://img.tianxiahuo.cn/public/NetFile/20170315/9f446692aa8012f87a9a9126d1ac3d2f.png","ToURL":"index.html","MenuCode":"FirstPage","OrderId":"1","DefaultColor":"#666666","SelectedColor":"#E75858"},{"Title":"朋友","ImgUrl":"http://img.tianxiahuo.cn/public/NetFile/20170315/aa6aeac430f24ac6e683ac923b48b1a.png","SelectedImgUrl":"http://img.tianxiahuo.cn/public/NetFile/20170315/d2fc5c5c6f9384da5cb0d1b276cbd3b7.png","ToURL":"friend.html","MenuCode":"FriendList","OrderId":"2","DefaultColor":"#666666","SelectedColor":"#E75858"},{"Title":"我的","ImgUrl":"http://img.tianxiahuo.cn/public/NetFile/20170315/b5c5d3162a5cb998e01a2d24fcd99657.png","SelectedImgUrl":"http://img.tianxiahuo.cn/public/NetFile/20170315/9f54bb274ee824afa89bb67b8c8c9b2.png","ToURL":"myProfile.html","MenuCode":"MyInfo","OrderId":"4","DefaultColor":"#666666","SelectedColor":"#E75858"}]
     */

    private UserInfoBean UserInfo;
    private List<MainButtonBean> MenuList;

    public UserInfoBean getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(UserInfoBean UserInfo) {
        this.UserInfo = UserInfo;
    }

    public static class UserInfoBean {
        /**
         * UnitId : 29
         * UnitType : 2
         * "ShopInfoIsOK":"0"
         */

        private String UnitId;
        private String UnitType;
        private String ShopInfoIsOK;

        public String getShopInfoIsOK() {
            return ShopInfoIsOK;
        }

        public void setShopInfoIsOK(String shopInfoIsOK) {
            ShopInfoIsOK = shopInfoIsOK;
        }

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
    }

    public List<MainButtonBean> getMenuList() {
        return MenuList;
    }

    public void setMenuList(List<MainButtonBean> menuList) {
        MenuList = menuList;
    }
}
