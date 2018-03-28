package com.txh.im.bean;

import java.util.List;

public class ShopClassifyLeftBean {

    /**
     * ShopInfo : {"ShopName":"张三小店","ContactPhone":"","ShopLogo":"http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png"}
     * list_brand : [{"category_id":"0","parent_id":"0","name":"全部"},{"category_id":"1","parent_id":"0","name":"乳品"},{"category_id":"2","parent_id":"0","name":"饮料"},{"category_id":"3","parent_id":"0","name":"水"},{"category_id":"34","parent_id":"0","name":"食品"},{"category_id":"38","parent_id":"0","name":"酒类"},{"category_id":"42","parent_id":"0","name":"粮油"},{"category_id":"50","parent_id":"0","name":"洗护"},{"category_id":"57","parent_id":"0","name":"调味"},{"category_id":"60","parent_id":"0","name":"纸类"}]
     */

    private ShopInfoBean ShopInfo;
    private List<ListBean> list;

    public ShopInfoBean getShopInfo() {
        return ShopInfo;
    }

    public void setShopInfo(ShopInfoBean ShopInfo) {
        this.ShopInfo = ShopInfo;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ShopInfoBean {
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

    public static class ListBean {
        /**
         * category_id : 0
         * parent_id : 0
         * name : 全部
         */

        private String category_id;
        private String parent_id;
        private String name;
        private boolean isSeleted = false;

        public boolean isSeleted() {
            return isSeleted;
        }

        public void setSeleted(boolean seleted) {
            isSeleted = seleted;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
