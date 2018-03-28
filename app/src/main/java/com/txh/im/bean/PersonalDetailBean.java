package com.txh.im.bean;

import java.util.List;

public class PersonalDetailBean {
// "IsCanEidt": "1"//0不可编辑，1可以编辑
    private String IsCanEidt;
    private List<ShowInfoBean> ShowInfo;
    private List<EditInfoBean> EditInfo;
    CryptonyBean UserInfo;

    public String getIsCanEidt() {
        return IsCanEidt;
    }

    public void setIsCanEidt(String isCanEidt) {
        IsCanEidt = isCanEidt;
    }

    public List<ShowInfoBean> getShowInfo() {
        return ShowInfo;
    }

    public void setShowInfo(List<ShowInfoBean> ShowInfo) {
        this.ShowInfo = ShowInfo;
    }

    public List<EditInfoBean> getEditInfo() {
        return EditInfo;
    }

    public void setEditInfo(List<EditInfoBean> EditInfo) {
        this.EditInfo = EditInfo;
    }

    public CryptonyBean getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(CryptonyBean userInfo) {
        UserInfo = userInfo;
    }

    public static class ShowInfoBean {
        /**
         * ItemCode : UserName
         * ItemType : 1
         * ItemIcon :
         * ItemTitle : 昵称
         * ItemText : 15637939253
         * TextColor :
         * ItemValue :
         * ValueName :
         * ItemKey :
         * IsRequired :
         * "ImagesHead":"http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png",//用户头像
         * "UserName":"张三",//用户昵称
         * UnitName":"张三小店",//店铺或供应商名称"
         */

        private String ItemCode;
        private String ItemType;
        private String ItemIcon;
        private String ItemTitle;
        private String ItemText;
        private String TextColor;
        private String ItemValue;
        private String ValueName;
        private String ItemKey;
        private String IsRequired;
        private String ImagesHead;
        private String UserName;
        private String UnitName;

        public String getImagesHead() {
            return ImagesHead;
        }

        public void setImagesHead(String imagesHead) {
            ImagesHead = imagesHead;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getUnitName() {
            return UnitName;
        }

        public void setUnitName(String unitName) {
            UnitName = unitName;
        }

        public String getItemCode() {
            return ItemCode;
        }

        public void setItemCode(String ItemCode) {
            this.ItemCode = ItemCode;
        }

        public String getItemType() {
            return ItemType;
        }

        public void setItemType(String ItemType) {
            this.ItemType = ItemType;
        }

        public String getItemIcon() {
            return ItemIcon;
        }

        public void setItemIcon(String ItemIcon) {
            this.ItemIcon = ItemIcon;
        }

        public String getItemTitle() {
            return ItemTitle;
        }

        public void setItemTitle(String ItemTitle) {
            this.ItemTitle = ItemTitle;
        }

        public String getItemText() {
            return ItemText;
        }

        public void setItemText(String ItemText) {
            this.ItemText = ItemText;
        }

        public String getTextColor() {
            return TextColor;
        }

        public void setTextColor(String TextColor) {
            this.TextColor = TextColor;
        }

        public String getItemValue() {
            return ItemValue;
        }

        public void setItemValue(String ItemValue) {
            this.ItemValue = ItemValue;
        }

        public String getValueName() {
            return ValueName;
        }

        public void setValueName(String ValueName) {
            this.ValueName = ValueName;
        }

        public String getItemKey() {
            return ItemKey;
        }

        public void setItemKey(String ItemKey) {
            this.ItemKey = ItemKey;
        }

        public String getIsRequired() {
            return IsRequired;
        }

        public void setIsRequired(String IsRequired) {
            this.IsRequired = IsRequired;
        }
    }

    public static class EditInfoBean {
        /**
         * ItemCode : 1
         * ItemType : 2
         * ItemIcon :
         * ItemTitle : 昵称
         * ItemText : 15637939253
         * TextColor :
         * ItemValue : 15637939253
         * ValueName :
         * ItemKey : UserName
         * IsRequired : 1
         */

        private String ItemCode;
        private String ItemType;
        private String ItemIcon;
        private String ItemTitle;
        private String ItemText;
        private String TextColor;
        private String ItemValue;
        private String ValueName;
        private String ItemKey;
        private String IsRequired;

        public String getItemCode() {
            return ItemCode;
        }

        public void setItemCode(String ItemCode) {
            this.ItemCode = ItemCode;
        }

        public String getItemType() {
            return ItemType;
        }

        public void setItemType(String ItemType) {
            this.ItemType = ItemType;
        }

        public String getItemIcon() {
            return ItemIcon;
        }

        public void setItemIcon(String ItemIcon) {
            this.ItemIcon = ItemIcon;
        }

        public String getItemTitle() {
            return ItemTitle;
        }

        public void setItemTitle(String ItemTitle) {
            this.ItemTitle = ItemTitle;
        }

        public String getItemText() {
            return ItemText;
        }

        public void setItemText(String ItemText) {
            this.ItemText = ItemText;
        }

        public String getTextColor() {
            return TextColor;
        }

        public void setTextColor(String TextColor) {
            this.TextColor = TextColor;
        }

        public String getItemValue() {
            return ItemValue;
        }

        public void setItemValue(String ItemValue) {
            this.ItemValue = ItemValue;
        }

        public String getValueName() {
            return ValueName;
        }

        public void setValueName(String ValueName) {
            this.ValueName = ValueName;
        }

        public String getItemKey() {
            return ItemKey;
        }

        public void setItemKey(String ItemKey) {
            this.ItemKey = ItemKey;
        }

        public String getIsRequired() {
            return IsRequired;
        }

        public void setIsRequired(String IsRequired) {
            this.IsRequired = IsRequired;
        }
    }
}
