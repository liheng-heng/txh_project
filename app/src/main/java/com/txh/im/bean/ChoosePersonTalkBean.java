package com.txh.im.bean;

import java.io.Serializable;
import java.util.List;

public class ChoosePersonTalkBean implements Serializable {

    /**
     * ShopInfo : {"ShopName":"张三小店","ContactPhone":"","ShopLogo":"http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png"}
     * UserList : [{"UserId":100001,"PCRoleId":4,"UserRoleIcon":"","UserName":"15222222222","ImagesHead":"http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png","TXHCode":"sjtvv2489587604"}]
     */

    private ShopInfoBean ShopInfo;
    private List<UserListBean> UserList;

    public ShopInfoBean getShopInfo() {
        return ShopInfo;
    }

    public void setShopInfo(ShopInfoBean ShopInfo) {
        this.ShopInfo = ShopInfo;
    }

    public List<UserListBean> getUserList() {
        return UserList;
    }

    public void setUserList(List<UserListBean> UserList) {
        this.UserList = UserList;
    }

    public static class ShopInfoBean implements Serializable {
        /**
         * ShopName : 张三小店
         * ContactPhone :
         * ShopLogo : http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png
         */

        private String ShopName;
        private String ContactPhone;
        private String ShopLogo;

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getContactPhone() {
            return ContactPhone;
        }

        public void setContactPhone(String ContactPhone) {
            this.ContactPhone = ContactPhone;
        }

        public String getShopLogo() {
            return ShopLogo;
        }

        public void setShopLogo(String ShopLogo) {
            this.ShopLogo = ShopLogo;
        }
    }

    public static class UserListBean implements Serializable {

        /**
         * UserId : 100001
         * PCRoleId : 4
         * UserRoleIcon :
         * UserName : 15222222222
         * ImagesHead : http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png
         * TXHCode : sjtvv2489587604
         */

        private String UserId;
        private String PCRoleId;
        private String UserRoleIcon;
        private String UserName;
        private String ImagesHead;
        private String TXHCode;

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public String getPCRoleId() {
            return PCRoleId;
        }

        public void setPCRoleId(String PCRoleId) {
            this.PCRoleId = PCRoleId;
        }

        public String getUserRoleIcon() {
            return UserRoleIcon;
        }

        public void setUserRoleIcon(String UserRoleIcon) {
            this.UserRoleIcon = UserRoleIcon;
        }

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

        public String getTXHCode() {
            return TXHCode;
        }

        public void setTXHCode(String TXHCode) {
            this.TXHCode = TXHCode;
        }
    }
}
