package com.txh.im.bean;

import java.util.List;

public class MyOrderContentBean {

    /**
     * OrderIsFinish": "0", //0订单未完成，1订单已完成
     * FinishIcon": "", //OrderIsFinish=1时，订单完成会返回完成的图标
     * <p>
     * OrderId : 12345678901
     * "OrderNo":"12345678902",
     * UnitId : 23
     * UnitName : 张三批发部
     * TotalAmount : 合计：¥ 590.00
     * TotalNumber : 15件
     * OrderStatus : 1
     * OrderStatusName : 待接单、
     * UnitLogo   店铺logo
     *  "ReplaceOrderIcon":"http://img.tianxiahuo.cn/public/txh_newapp/User/replaceorder3x.png",
     * <p>
     * ItemList : [{"ShopId":"23","SkuId":"143687","ItemName":"顶真 复合果汁苹果汁 900ml*6瓶1","ItemPic":"http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg","SkuProp":"900ml*6瓶","SPrice":"¥55.50","ItemNumber":"15件"}]
     * opBtnList : [{"Code":"CancelOrder","Text":"取消订单",Disabled": "0"}]
     */

    private String OrderNo;
    private String OrderId;
    private String UnitId;
    private String UnitName;
    private String TotalAmount;
    private String TotalNumber;
    private String OrderStatus;
    private String OrderStatusName;
    private String OrderIsFinish;
    private String FinishIcon;
    private String UnitLogo;
    private String ReplaceOrderIcon;

    private List<ItemListBean> ItemList;
    private List<OpBtnListBean> opBtnList;

    public String getReplaceOrderIcon() {
        return ReplaceOrderIcon;
    }

    public void setReplaceOrderIcon(String replaceOrderIcon) {
        ReplaceOrderIcon = replaceOrderIcon;
    }

    public String getUnitLogo() {
        return UnitLogo;
    }

    public void setUnitLogo(String unitLogo) {
        UnitLogo = unitLogo;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
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
         * Code : CancelOrder
         * Text : 取消订单
         * Value : 操作类型值，用于(18.订单操作)中的参数OpType，有为空的可能
         * "Disabled": "0"//0代表按钮可用，1代表按钮禁用
         */

        private String Code;
        private String Text;
        private String Value;
        private String Disabled;

        public String getDisabled() {
            return Disabled;
        }

        public void setDisabled(String disabled) {
            Disabled = disabled;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }

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
    }
}
