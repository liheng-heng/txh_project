package com.txh.im.bean;

import java.util.List;

public class MyOrderTitleBean {

    /**
     * ShopName :
     * MyOrderTitle :"我的采购单",
     * TypeList : [{"TypeCode":"0","TypeText":"全部"},{"TypeCode":"2","TypeText":"待发货"},{"TypeCode":"3","TypeText":"待收货"},{"TypeCode":"4","TypeText":"已完成"}]
     */

    private String ShopName;
    private String MyOrderTitle;
    private List<TypeListBean> TypeList;
    private List<RefuseRmearkListBean> RefuseRmearkList;

    public List<RefuseRmearkListBean> getRefuseRmearkList() {
        return RefuseRmearkList;
    }

    public void setRefuseRmearkList(List<RefuseRmearkListBean> refuseRmearkList) {
        RefuseRmearkList = refuseRmearkList;
    }

    public String getMyOrderTitle() {
        return MyOrderTitle;
    }

    public void setMyOrderTitle(String myOrderTitle) {
        MyOrderTitle = myOrderTitle;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String ShopName) {
        this.ShopName = ShopName;
    }

    public List<TypeListBean> getTypeList() {
        return TypeList;
    }

    public void setTypeList(List<TypeListBean> TypeList) {
        this.TypeList = TypeList;
    }

    public static class TypeListBean {
        /**
         * TypeCode : 0
         * TypeText : 全部
         */

        private String TypeCode;
        private String TypeText;
        private boolean isseleted = false;

        public boolean isseleted() {
            return isseleted;
        }

        public void setIsseleted(boolean isseleted) {
            this.isseleted = isseleted;
        }

        public String getTypeCode() {
            return TypeCode;
        }

        public void setTypeCode(String TypeCode) {
            this.TypeCode = TypeCode;
        }

        public String getTypeText() {
            return TypeText;
        }

        public void setTypeText(String TypeText) {
            this.TypeText = TypeText;
        }
    }


    public static class RefuseRmearkListBean {
        /**
         * ItemCode : 4
         * ItemText : 订单缺货
         */

        private String ItemCode;
        private String ItemText;
        private boolean isseleted = false;

        public boolean isseleted() {
            return isseleted;
        }

        public void setIsseleted(boolean isseleted) {
            this.isseleted = isseleted;
        }


        public String getItemCode() {
            return ItemCode;
        }

        public void setItemCode(String ItemCode) {
            this.ItemCode = ItemCode;
        }

        public String getItemText() {
            return ItemText;
        }

        public void setItemText(String ItemText) {
            this.ItemText = ItemText;
        }
    }
}
