package com.txh.im.bean;

import java.util.List;

public class UpdateOrderBean {


    /**
     * OrderInfo : {"OrderId":"1234902","OrderNo":"12345678902","UnitId":"23","UnitName":"马武小超市","TotalAmount":"合计：¥ 1800.00","TotalNumber":"15件","OrderStatus":"10","OrderStatusName":"待发货","ItemList":[{"ShopId":"23","SkuId":"143687","ItemName":"顶真 复合果汁苹果汁 900ml*6瓶1","ItemPic":"http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg","SkuProp":"900ml*6瓶","SPrice":"¥55.50","ItemNumber":"15件"},{"ShopId":"23","SkuId":"140608","ItemName":"德国冰力克无糖果粉含片糖青苹果味15g*12盒7","ItemPic":"http://img.tianxiahuo.cn/public/File1/20160818/b2f849b2dba60827f90fe9bac189e269.jpg","SkuProp":"15g*12盒","SPrice":"¥50.50","ItemNumber":"5件"},{"ShopId":"25","SkuId":"1420","ItemName":"百岁山 小瓶装矿泉水纯净饮用水 整箱装348ml*24瓶(五箱送六瓶)6","ItemPic":"http://img.tianxiahuo.cn/goods/20160530/0000fec33b2a4e008e5e7dba0fae4a08.jpg","SkuProp":"348ml*24瓶","SPrice":"¥58.40","ItemNumber":"5件"}],"opBtnList":[{"Code":"RemindSellerSend","Text":"提醒商家发货","Value":"6"}]}
     */

    private MyOrderContentBean OrderInfo;

    public MyOrderContentBean getOrderInfo() {
        return OrderInfo;
    }

    public void setOrderInfo(MyOrderContentBean OrderInfo) {
        this.OrderInfo = OrderInfo;
    }

    public static class OrderInfoBean {
        /**
         * "OrderIsFinish": "0", //0订单未完成，1订单已完成
         * "FinishIcon": "", //OrderIsFinish=1时，订单完成会返回完成的图标
         * <p>
         * OrderId : 1234902
         * OrderNo : 12345678902
         * UnitId : 23
         * UnitName : 马武小超市
         * TotalAmount : 合计：¥ 1800.00
         * TotalNumber : 15件
         * OrderStatus : 10
         * OrderStatusName : 待发货
         * ItemList : [{"ShopId":"23","SkuId":"143687","ItemName":"顶真 复合果汁苹果汁 900ml*6瓶1","ItemPic":"http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg","SkuProp":"900ml*6瓶","SPrice":"¥55.50","ItemNumber":"15件"},{"ShopId":"23","SkuId":"140608","ItemName":"德国冰力克无糖果粉含片糖青苹果味15g*12盒7","ItemPic":"http://img.tianxiahuo.cn/public/File1/20160818/b2f849b2dba60827f90fe9bac189e269.jpg","SkuProp":"15g*12盒","SPrice":"¥50.50","ItemNumber":"5件"},{"ShopId":"25","SkuId":"1420","ItemName":"百岁山 小瓶装矿泉水纯净饮用水 整箱装348ml*24瓶(五箱送六瓶)6","ItemPic":"http://img.tianxiahuo.cn/goods/20160530/0000fec33b2a4e008e5e7dba0fae4a08.jpg","SkuProp":"348ml*24瓶","SPrice":"¥58.40","ItemNumber":"5件"}]
         * opBtnList : [{"Code":"RemindSellerSend","Text":"提醒商家发货","Value":"6"}]
         */

        private String OrderId;
        private String OrderNo;
        private String UnitId;
        private String UnitName;
        private String TotalAmount;
        private String TotalNumber;
        private String OrderStatus;
        private String OrderStatusName;
        private String OrderIsFinish;
        private String FinishIcon;
        private List<ItemListBean> ItemList;
        private List<OpBtnListBean> opBtnList;

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String OrderId) {
            this.OrderId = OrderId;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
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

        public String getTotalAmount() {
            return TotalAmount;
        }

        public void setTotalAmount(String TotalAmount) {
            this.TotalAmount = TotalAmount;
        }

        public String getTotalNumber() {
            return TotalNumber;
        }

        public void setTotalNumber(String TotalNumber) {
            this.TotalNumber = TotalNumber;
        }

        public String getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(String OrderStatus) {
            this.OrderStatus = OrderStatus;
        }

        public String getOrderStatusName() {
            return OrderStatusName;
        }

        public void setOrderStatusName(String OrderStatusName) {
            this.OrderStatusName = OrderStatusName;
        }

        public String getOrderIsFinish() {
            return OrderIsFinish;
        }

        public void setOrderIsFinish(String orderIsFinish) {
            OrderIsFinish = orderIsFinish;
        }

        public String getFinishIcon() {
            return FinishIcon;
        }

        public void setFinishIcon(String finishIcon) {
            FinishIcon = finishIcon;
        }

        public List<ItemListBean> getItemList() {
            return ItemList;
        }

        public void setItemList(List<ItemListBean> ItemList) {
            this.ItemList = ItemList;
        }

        public List<OpBtnListBean> getOpBtnList() {
            return opBtnList;
        }

        public void setOpBtnList(List<OpBtnListBean> opBtnList) {
            this.opBtnList = opBtnList;
        }

        public static class ItemListBean {
            /**
             * ShopId : 23
             * SkuId : 143687
             * ItemName : 顶真 复合果汁苹果汁 900ml*6瓶1
             * ItemPic : http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg
             * SkuProp : 900ml*6瓶
             * SPrice : ¥55.50
             * ItemNumber : 15件
             */

            private String ShopId;
            private String SkuId;
            private String ItemName;
            private String ItemPic;
            private String SkuProp;
            private String SPrice;
            private String ItemNumber;

            public String getShopId() {
                return ShopId;
            }

            public void setShopId(String ShopId) {
                this.ShopId = ShopId;
            }

            public String getSkuId() {
                return SkuId;
            }

            public void setSkuId(String SkuId) {
                this.SkuId = SkuId;
            }

            public String getItemName() {
                return ItemName;
            }

            public void setItemName(String ItemName) {
                this.ItemName = ItemName;
            }

            public String getItemPic() {
                return ItemPic;
            }

            public void setItemPic(String ItemPic) {
                this.ItemPic = ItemPic;
            }

            public String getSkuProp() {
                return SkuProp;
            }

            public void setSkuProp(String SkuProp) {
                this.SkuProp = SkuProp;
            }

            public String getSPrice() {
                return SPrice;
            }

            public void setSPrice(String SPrice) {
                this.SPrice = SPrice;
            }

            public String getItemNumber() {
                return ItemNumber;
            }

            public void setItemNumber(String ItemNumber) {
                this.ItemNumber = ItemNumber;
            }
        }

        public static class OpBtnListBean {
            /**
             * Code : RemindSellerSend
             * Text : 提醒商家发货
             * Value : 6
             */

            private String Code;
            private String Text;
            private String Value;

            public String getCode() {
                return Code;
            }

            public void setCode(String Code) {
                this.Code = Code;
            }

            public String getText() {
                return Text;
            }

            public void setText(String Text) {
                this.Text = Text;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String Value) {
                this.Value = Value;
            }
        }
    }
}
