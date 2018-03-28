package com.txh.im.bean;

import java.util.List;

/**
 * liheng
 * 04.12
 * 订单详情
 */
public class OrdersDetailBean {

    /**
     * OrderId : 16
     * OrderNo : 1491996833982498
     * ShopId : 29
     * OrderStatus : 20
     * OrderStatusName : 待收货
     * <p>
     * "RefuseRemark":"",//拒绝接单理由
     * "OrderModel":"自下单",//订单方式
     * <p>
     * AddressInfo : {"Name":"张三3737","Phone":"18601760331","ReceiveAddress":"上海市上海市宝山区长江南路188号3737"}
     * ItemList : [{"ShopId":"29","SkuId":"3","ItemName":"恒大冰泉 长白山天然矿泉水纯净水 整箱500ml*24瓶","ItemPic":"http://img.tianxiahuo.cn/goods/20160714/125a912d9de4dc64aec8a248fae40747.jpg","SkuProp":"500ml*24瓶","SPrice":"¥36.55","ItemNumber":"10件","ItemAmount":"¥ 365.50"},{"ShopId":"29","SkuId":"2","ItemName":"娃哈哈 大AD钙奶儿童营养奶乳饮品 整箱220ml*24瓶","ItemPic":"http://img.tianxiahuo.cn/goods/20160714/1751da9c04e3789aa12f9fe1592b0fe2.jpg","SkuProp":"220ml*24瓶","SPrice":"¥36.55","ItemNumber":"10件","ItemAmount":"¥ 365.50"},{"ShopId":"29","SkuId":"1","ItemName":"娃哈哈 小AD钙奶儿童营养奶乳饮品 整箱100ml*24瓶","ItemPic":"http://img.tianxiahuo.cn/goods/20160714/7ccdd4c80ca0236a9a8ff77c3d94861e.jpg","SkuProp":"100ml*24瓶","SPrice":"¥36.55","ItemNumber":"2件","ItemAmount":"¥ 73.10"}]
     * <p>
     * PayTypeName : 货到现金支付
     * TotalItemAmount : ¥ 804.10
     * TotalAmount : ¥ 804.10
     * TotalNumber : 22件
     * "CouponPrice": "-¥20.00",//使用优惠券的金额
     * DiscountAmount": "-¥306.00",//优惠总金额
     * <p>
     * OrderInfo : [{"Title":"订单编号：","Text":"1491996833982498"},{"Title":"创建时间：","Text":"2017-04-12 11:33"},{"Title":"接单时间：","Text":"2017-04-13 13:59"},{"Title":"发货时间：","Text":"2017-04-13 14:05"}]
     * Remark : 34534534534536让他y675678678
     * OpBtnList : [{"Code":"ConfirmReceipt","Text":"确认收货","Value":"4","Disabled":"0"}]
     */

    private String OrderId;
    private String OrderNo;
    private String ShopId;
    private String OrderStatus;
    private String OrderStatusName;
    private String RefuseRemark;
    private String OrderModel;
    private AddressInfoBean AddressInfo;
    private String PayTypeName;
    private String TotalItemAmount;
    private String TotalAmount;
    private String TotalNumber;
    private String CouponPrice;
    private String DiscountAmount;
    private String Remark;
    private List<ItemListBean> ItemList;
    private List<ItemListGivBean> itemListGiv;
    private List<OrderInfoBean> OrderInfo;
    private List<OpBtnListBean> OpBtnList;

    public String getCouponPrice() {
        return CouponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        CouponPrice = couponPrice;
    }

    public String getRefuseRemark() {
        return RefuseRemark;
    }

    public void setRefuseRemark(String refuseRemark) {
        RefuseRemark = refuseRemark;
    }

    public String getOrderModel() {
        return OrderModel;
    }

    public void setOrderModel(String orderModel) {
        OrderModel = orderModel;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        DiscountAmount = discountAmount;
    }

    public List<ItemListGivBean> getItemListGiv() {
        return itemListGiv;
    }

    public void setItemListGiv(List<ItemListGivBean> itemListGiv) {
        this.itemListGiv = itemListGiv;
    }

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

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String ShopId) {
        this.ShopId = ShopId;
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

    public AddressInfoBean getAddressInfo() {
        return AddressInfo;
    }

    public void setAddressInfo(AddressInfoBean AddressInfo) {
        this.AddressInfo = AddressInfo;
    }

    public String getPayTypeName() {
        return PayTypeName;
    }

    public void setPayTypeName(String PayTypeName) {
        this.PayTypeName = PayTypeName;
    }

    public String getTotalItemAmount() {
        return TotalItemAmount;
    }

    public void setTotalItemAmount(String TotalItemAmount) {
        this.TotalItemAmount = TotalItemAmount;
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

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public List<ItemListBean> getItemList() {
        return ItemList;
    }

    public void setItemList(List<ItemListBean> ItemList) {
        this.ItemList = ItemList;
    }

    public List<OrderInfoBean> getOrderInfo() {
        return OrderInfo;
    }

    public void setOrderInfo(List<OrderInfoBean> OrderInfo) {
        this.OrderInfo = OrderInfo;
    }

    public List<OpBtnListBean> getOpBtnList() {
        return OpBtnList;
    }

    public void setOpBtnList(List<OpBtnListBean> OpBtnList) {
        this.OpBtnList = OpBtnList;
    }

    public static class AddressInfoBean {
        /**
         * Name : 张三3737
         * Phone : 18601760331
         * ReceiveAddress : 上海市上海市宝山区长江南路188号3737
         */

        private String Name;
        private String Phone;
        private String ReceiveAddress;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String Phone) {
            this.Phone = Phone;
        }

        public String getReceiveAddress() {
            return ReceiveAddress;
        }

        public void setReceiveAddress(String ReceiveAddress) {
            this.ReceiveAddress = ReceiveAddress;
        }
    }

    public static class ItemListBean {
        /**
         * ShopId : 29
         * SkuId : 3
         * ItemName : 恒大冰泉 长白山天然矿泉水纯净水 整箱500ml*24瓶
         * ItemPic : http://img.tianxiahuo.cn/goods/20160714/125a912d9de4dc64aec8a248fae40747.jpg
         * SkuProp : 500ml*24瓶
         * SPrice : ¥36.55
         * ItemNumber : 10件
         * ItemAmount : ¥ 365.50
         */

        private String ShopId;
        private String SkuId;
        private String ItemName;
        private String ItemPic;
        private String SkuProp;
        private String SPrice;
        private String ItemNumber;
        private String ItemAmount;

        private String OldPrice;
        private String Discount;
        private String ActivityRemark;
        private String ActivityTypeId;
        private String ActivityId;
        private String ActivityRemarkIcon;
        private String DDDJIcon;

        public String getOldPrice() {
            return OldPrice;
        }

        public void setOldPrice(String oldPrice) {
            OldPrice = oldPrice;
        }

        public String getDiscount() {
            return Discount;
        }

        public void setDiscount(String discount) {
            Discount = discount;
        }

        public String getActivityRemark() {
            return ActivityRemark;
        }

        public void setActivityRemark(String activityRemark) {
            ActivityRemark = activityRemark;
        }

        public String getActivityTypeId() {
            return ActivityTypeId;
        }

        public void setActivityTypeId(String activityTypeId) {
            ActivityTypeId = activityTypeId;
        }

        public String getActivityId() {
            return ActivityId;
        }

        public void setActivityId(String activityId) {
            ActivityId = activityId;
        }

        public String getActivityRemarkIcon() {
            return ActivityRemarkIcon;
        }

        public void setActivityRemarkIcon(String activityRemarkIcon) {
            ActivityRemarkIcon = activityRemarkIcon;
        }

        public String getDDDJIcon() {
            return DDDJIcon;
        }

        public void setDDDJIcon(String DDDJIcon) {
            this.DDDJIcon = DDDJIcon;
        }

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

        public String getItemAmount() {
            return ItemAmount;
        }

        public void setItemAmount(String ItemAmount) {
            this.ItemAmount = ItemAmount;
        }
    }


    public static class ItemListGivBean {
        //                "ShopId": "23",//商户ID
//                "SkuId": "143687",//商品SkuId
//                "ItemName": "顶真 复合果汁苹果汁 900ml*6瓶1",//商品名称
//                "ItemPic": "http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg",//商品图片
//                "SkuProp": "900ml*6瓶",//商品规格
//                "SPrice": "¥ 58.50",//商品价格
//                "ItemNumber": "¥ 58.50*5",//商品数量
//                "ItemAmount": "¥ 250.00",//商品金额
//                "OldPrice": "40.50",//原价
//                "Discount": "8.0折",//折扣描述
//                "ActivityRemark": "满100元赠送矿泉水1箱",//满赠提示信息
//                "ActivityContent":"活动规则",//活动规则
//                "ActivityTypeId": "1",//0无促销、1满赠、2限时抢购
//                "ActivityId": "1"//活动ID
//                "ActivityRemarkIcon": "http://img.tianxiahuo.cn/public/txh_newapp/User/gift_tag3x.png",//满赠提示信息的ICON
//                "DDDJIcon": "http://img.tianxiahuo.cn/public/txh_newapp/User/tag_sheet3x.png",//单店单价的ICON

        private String ShopId;
        private String SkuId;
        private String ItemName;
        private String ItemPic;
        private String SkuProp;
        private String SPrice;
        private String ItemNumber;
        private String ItemAmount;
        private String OldPrice;
        private String Discount;
        private String ActivityRemark;
        private String ActivityContent;
        private String ActivityTypeId;
        private String ActivityId;
        private String ActivityRemarkIcon;
        private String DDDJIcon;

        public String getShopId() {
            return ShopId;
        }

        public void setShopId(String shopId) {
            ShopId = shopId;
        }

        public String getSkuId() {
            return SkuId;
        }

        public void setSkuId(String skuId) {
            SkuId = skuId;
        }

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String itemName) {
            ItemName = itemName;
        }

        public String getItemPic() {
            return ItemPic;
        }

        public void setItemPic(String itemPic) {
            ItemPic = itemPic;
        }

        public String getSkuProp() {
            return SkuProp;
        }

        public void setSkuProp(String skuProp) {
            SkuProp = skuProp;
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

        public void setItemNumber(String itemNumber) {
            ItemNumber = itemNumber;
        }

        public String getItemAmount() {
            return ItemAmount;
        }

        public void setItemAmount(String itemAmount) {
            ItemAmount = itemAmount;
        }

        public String getOldPrice() {
            return OldPrice;
        }

        public void setOldPrice(String oldPrice) {
            OldPrice = oldPrice;
        }

        public String getDiscount() {
            return Discount;
        }

        public void setDiscount(String discount) {
            Discount = discount;
        }

        public String getActivityRemark() {
            return ActivityRemark;
        }

        public void setActivityRemark(String activityRemark) {
            ActivityRemark = activityRemark;
        }

        public String getActivityContent() {
            return ActivityContent;
        }

        public void setActivityContent(String activityContent) {
            ActivityContent = activityContent;
        }

        public String getActivityTypeId() {
            return ActivityTypeId;
        }

        public void setActivityTypeId(String activityTypeId) {
            ActivityTypeId = activityTypeId;
        }

        public String getActivityId() {
            return ActivityId;
        }

        public void setActivityId(String activityId) {
            ActivityId = activityId;
        }

        public String getActivityRemarkIcon() {
            return ActivityRemarkIcon;
        }

        public void setActivityRemarkIcon(String activityRemarkIcon) {
            ActivityRemarkIcon = activityRemarkIcon;
        }

        public String getDDDJIcon() {
            return DDDJIcon;
        }

        public void setDDDJIcon(String DDDJIcon) {
            this.DDDJIcon = DDDJIcon;
        }
    }

    public static class OrderInfoBean {
        /**
         * Title : 订单编号：
         * Text : 1491996833982498
         */

        private String Title;
        private String Text;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getText() {
            return Text;
        }

        public void setText(String Text) {
            this.Text = Text;
        }
    }

    public static class OpBtnListBean {
        /**
         * Code : ConfirmReceipt
         * Text : 确认收货
         * Value : 4
         * Disabled : 0
         */

        private String Code;
        private String Text;
        private String Value;
        private String Disabled;

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

        public String getDisabled() {
            return Disabled;
        }

        public void setDisabled(String Disabled) {
            this.Disabled = Disabled;
        }
    }
}
