package com.txh.im.bean;

import java.util.List;

public class PersonalCenterBean {

    /**
     * TopHead : {"ItemCode":"ImagesHead","ItemType":"6","ItemIcon":"","ItemTitle":"18601760336","ItemText":"","TextColor":"","ItemValue":"http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png","ValueName":"","ItemKey":"ImagesHead","IsRequired":""}
     * ListItem : [{"ItemCode":"MyInfo","ItemType":"3","ItemIcon":"","ItemTitle":"我的资料","ItemText":"","TextColor":"","ItemValue":"","ValueName":"","ItemKey":"","IsRequired":""},{"ItemCode":"IdentityCheck","ItemType":"3","ItemIcon":"","ItemTitle":"身份认证","ItemText":"","TextColor":"","ItemValue":"","ValueName":"","ItemKey":"","IsRequired":""},{"ItemCode":"ChangeLoginPwd","ItemType":"3","ItemIcon":"","ItemTitle":"重置密码","ItemText":"","TextColor":"","ItemValue":"","ValueName":"","ItemKey":"","IsRequired":""},{"ItemCode":"AppSet","ItemType":"3","ItemIcon":"","ItemTitle":"设置","ItemText":"","TextColor":"","ItemValue":"","ValueName":"","ItemKey":"","IsRequired":""},{"ItemCode":"DeliveryAddress","ItemType":"3","ItemIcon":"","ItemTitle":"收货地址","ItemText":"","TextColor":"","ItemValue":"","ValueName":"","ItemKey":"","IsRequired":""}]
     */

    private TopHeadBean TopHead;
    private List<ListItemBean> ListItem;

    public TopHeadBean getTopHead() {
        return TopHead;
    }

    public void setTopHead(TopHeadBean TopHead) {
        this.TopHead = TopHead;
    }

    public List<ListItemBean> getListItem() {
        return ListItem;
    }

    public void setListItem(List<ListItemBean> ListItem) {
        this.ListItem = ListItem;
    }

    public static class TopHeadBean {
        /**
         * ItemCode : ImagesHead
         * ItemType : 6
         * ItemIcon :
         * ItemTitle : 18601760336
         * ItemText :
         * TextColor :
         * ItemValue : http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png
         * ValueName :
         * ItemKey : ImagesHead
         * IsRequired :
         * UnitName ：店铺或商户名称
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
        private String UnitName;

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

    public static class ListItemBean {

        /**
         * ItemCode : MyInfo
         * ItemType : 3
         * ItemIcon :
         * ItemTitle : 我的资料
         * ItemText :
         * TextColor :
         * ItemValue :
         * ValueName :
         * ItemKey :
         * IsRequired :
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
