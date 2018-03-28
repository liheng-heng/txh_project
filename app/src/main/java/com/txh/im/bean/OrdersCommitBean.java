package com.txh.im.bean;

import java.util.List;

/**
 * liheng
 * 04.16
 * 订单提交
 */
public class OrdersCommitBean {

    /**
     * AddressInfo : {"AddressId":"27","Name":"小明","Phone":"18601760336","ReceiveAddress":"上海市长宁区淞虹路234号"}
     * <p>
     * <p>
     * PayTypeList : [{"TypeCode":"2","TypeText":"货到现金支付"}]
     * <p>
     * CouponInfo : {"CouponCount":"3","CouponList":[{"CouponId":"122","CouponValue":"10","ConditionValue":"满300元可用","EndTime":"有效期至2017-08-30","UseRangeName":"全场可用"},{"CouponId":"123","CouponValue":"15","ConditionValue":"满400元可用","EndTime":"有效期至2017-08-30","UseRangeName":"全场可用"},{"CouponId":"124","CouponValue":"20","ConditionValue":"满500元可使用","EndTime":"有效期至2017-08-30","UseRangeName":"梦梦酒水店专享"},{"CouponId":"125","CouponValue":"5","ConditionValue":"无门槛","EndTime":"有效期至2017-08-30","UseRangeName":"梦梦酒水店专享"}]}
     * ItemList : [{"ShopId":"23","SkuId":"143687","ItemName":"顶真 复合果汁苹果汁 900ml*6瓶1","ItemPic":"http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg","SkuProp":"900ml*6瓶","SPrice":"¥ 58.50","ItemNumber":"¥ 58.50*5","ItemAmount":"¥ 250.00"},{"ShopId":"23","SkuId":"140605","ItemName":"德国冰力克无糖果粉含片糖青苹果味15g*12盒2","ItemPic":"http://img.tianxiahuo.cn/public/File1/20160818/b2f849b2dba60827f90fe9bac189e269.jpg","SkuProp":"15g*12盒","SPrice":"¥ 58.50","ItemNumber":"¥ 58.50*5","ItemAmount":"¥ 250.00"}]
     * TotalInfo : {"TotalItemAmount":"¥ 12306.00","TotalAmount":"¥ 12306.00","TotalNumber":"25件"}
     * <p>
     * "itemListGiv": [  //赠品区商品列表
     * {
     * "ShopId": "23",//商户ID
     * "SkuId": "143687",//商品SkuId
     * "ItemName": "顶真 复合果汁苹果汁 900ml*6瓶1",//商品名称
     * "ItemPic": "http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg",//商品图片
     * "SkuProp": "900ml*6瓶",//商品规格
     * "SPrice": "¥ 58.50",//商品价格
     * "ItemNumber": "¥ 58.50*5",//商品数量
     * "ItemAmount": "¥ 250.00",//商品金额
     * "OldPrice": "40.50",//原价
     * "Discount": "8.0折",//折扣描述
     * "ActivityRemark": "满100元赠送矿泉水1箱",//满赠提示信息
     * "ActivityContent":"活动规则",//活动规则
     * "ActivityTypeId": "1",//0无促销、1满赠、2限时抢购
     * "ActivityId": "1"//活动ID
     * "ActivityRemarkIcon": "http://img.tianxiahuo.cn/public/txh_newapp/User/gift_tag3x.png",//满赠提示信息的ICON
     * "DDDJIcon": "http://img.tianxiahuo.cn/public/txh_newapp/User/tag_sheet3x.png",//单店单价的ICON
     * }, ...此处省去很多
     * ],
     */

    private AddressInfoBean AddressInfo;
    private TotalInfoBean TotalInfo;
    private CouponInfoBean CouponInfo;
    private List<PayTypeListBean> PayTypeList;
    private List<ItemListBean> ItemList;
    private List<ItemListGivBean> itemListGiv;

    public List<ItemListGivBean> getItemListGiv() {
        return itemListGiv;
    }

    public CouponInfoBean getCouponInfo() {
        return CouponInfo;
    }

    public void setCouponInfo(CouponInfoBean couponInfo) {
        CouponInfo = couponInfo;
    }

    public void setItemListGiv(List<ItemListGivBean> itemListGiv) {
        this.itemListGiv = itemListGiv;
    }

    public AddressInfoBean getAddressInfo() {
        return AddressInfo;
    }

    public void setAddressInfo(AddressInfoBean AddressInfo) {
        this.AddressInfo = AddressInfo;
    }

    public TotalInfoBean getTotalInfo() {
        return TotalInfo;
    }

    public void setTotalInfo(TotalInfoBean TotalInfo) {
        this.TotalInfo = TotalInfo;
    }

    public List<PayTypeListBean> getPayTypeList() {
        return PayTypeList;
    }

    public void setPayTypeList(List<PayTypeListBean> PayTypeList) {
        this.PayTypeList = PayTypeList;
    }

    public List<ItemListBean> getItemList() {
        return ItemList;
    }

    public void setItemList(List<ItemListBean> ItemList) {
        this.ItemList = ItemList;
    }

    public static class AddressInfoBean {
        /**
         * AddressId : 27
         * Name : 小明
         * Phone : 18601760336
         * ReceiveAddress : 上海市长宁区淞虹路234号
         */

        private String AddressId;
        private String Name;
        private String Phone;
        private String ReceiveAddress;

        public String getAddressId() {
            return AddressId;
        }

        public void setAddressId(String AddressId) {
            this.AddressId = AddressId;
        }

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


    public static class TotalInfoBean {
        /**
         * TotalItemAmount : ¥ 12306.00
         * TotalAmount : ¥ 12306.00
         * TotalNumber : 25件
         * "DiscountAmount":"- ¥0.00",
         * "TotalAmountVal": "12000.00"//订单应付款的金额（数值）
         */

        private String TotalItemAmount;
        private String TotalAmount;
        private String TotalNumber;
        private String DiscountAmount;
        private String TotalAmountVal;

        public String getTotalAmountVal() {
            return TotalAmountVal;
        }

        public void setTotalAmountVal(String totalAmountVal) {
            TotalAmountVal = totalAmountVal;
        }

        public String getDiscountAmount() {
            return DiscountAmount;
        }

        public void setDiscountAmount(String discountAmount) {
            DiscountAmount = discountAmount;
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
    }

    public static class PayTypeListBean {
        /**
         * TypeCode : 2
         * TypeText : 货到现金支付
         */

        private String TypeCode;
        private String TypeText;

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

    public static class ItemListBean {
        /**
         * ShopId : 23
         * SkuId : 143687
         * ItemName : 顶真 复合果汁苹果汁 900ml*6瓶1
         * ItemPic : http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg
         * SkuProp : 900ml*6瓶
         * SPrice : ¥ 58.50
         * ItemNumber : ¥ 58.50*5
         * ItemAmount : ¥ 250.00
         * <p>
         * <p>
         * "ShopId":"1506",
         * "SkuId":"178230",
         * "ItemName":"古越龙山 状元红喜愿酒婚宴用礼盒装350ml*2瓶",
         * "ItemPic":"http://img.tianxiahuo.cn/public/file/20170512/91b95d5f683e47854ab233a9f93f3786.jpg",
         * "SkuProp":"350ml*2瓶/组",
         * "SPrice":"¥23.00",
         * "ItemAmount":"¥ 276.00",
         * "ItemNumber":"¥ 23.00*12",
         * <p>
         * "OldPrice":"23.00",
         * "Discount":"",
         * "ActivityRemark":"",
         * "ActivityTypeId":"0",
         * "ActivityId":"",
         * "ActivityRemarkIcon":"",
         * "DDDJIcon":""
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


    public static class CouponInfoBean {
        /**
         * CouponCount : 3
         * CouponList : [{"CouponId":"122","CouponValue":"10","ConditionValue":"满300元可用","EndTime":"有效期至2017-08-30","UseRangeName":"全场可用"},{"CouponId":"123","CouponValue":"15","ConditionValue":"满400元可用","EndTime":"有效期至2017-08-30","UseRangeName":"全场可用"},{"CouponId":"124","CouponValue":"20","ConditionValue":"满500元可使用","EndTime":"有效期至2017-08-30","UseRangeName":"梦梦酒水店专享"},{"CouponId":"125","CouponValue":"5","ConditionValue":"无门槛","EndTime":"有效期至2017-08-30","UseRangeName":"梦梦酒水店专享"}]
         */

        private String CouponCount;
        private List<CouponInfoBean.CouponListBean> CouponList;

        public String getCouponCount() {
            return CouponCount;
        }

        public void setCouponCount(String CouponCount) {
            this.CouponCount = CouponCount;
        }

        public List<CouponInfoBean.CouponListBean> getCouponList() {
            return CouponList;
        }

        public void setCouponList(List<CouponInfoBean.CouponListBean> CouponList) {
            this.CouponList = CouponList;
        }

        public static class CouponListBean {
            /**
             * CouponId : 122
             * CouponValue : 10
             * ConditionValue : 满300元可用
             * EndTime : 有效期至2017-08-30
             * UseRangeName : 全场可用
             * "ShopId": "12" //供应商Id
             * select:
             */

            private String CouponId;
            private String CouponValue;
            private String ConditionValue;
            private String EndTime;
            private String UseRangeName;
            private String ShopId;
            private Boolean isSelect = false;

            public Boolean getSelect() {
                return isSelect;
            }

            public void setSelect(Boolean select) {
                isSelect = select;
            }

            public String getShopId() {
                return ShopId;
            }

            public void setShopId(String shopId) {
                ShopId = shopId;
            }

            public String getCouponId() {
                return CouponId;
            }

            public void setCouponId(String CouponId) {
                this.CouponId = CouponId;
            }

            public String getCouponValue() {
                return CouponValue;
            }

            public void setCouponValue(String CouponValue) {
                this.CouponValue = CouponValue;
            }

            public String getConditionValue() {
                return ConditionValue;
            }

            public void setConditionValue(String ConditionValue) {
                this.ConditionValue = ConditionValue;
            }

            public String getEndTime() {
                return EndTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public String getUseRangeName() {
                return UseRangeName;
            }

            public void setUseRangeName(String UseRangeName) {
                this.UseRangeName = UseRangeName;
            }
        }
    }


    public static class ItemListGivBean {
        /**
         * "ShopId": "23",//商户ID
         * "SkuId": "143687",//商品SkuId
         * "ItemName": "顶真 复合果汁苹果汁 900ml*6瓶1",//商品名称
         * "ItemPic": "http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg",//商品图片
         * "SkuProp": "900ml*6瓶",//商品规格
         * "SPrice": "¥ 58.50",//商品价格
         * "ItemNumber": "¥ 58.50*5",//商品数量
         * "ItemAmount": "¥ 250.00",//商品金额
         * "OldPrice": "40.50",//原价
         * "Discount": "8.0折",//折扣描述
         * <p>
         * "ActivityRemark": "满100元赠送矿泉水1箱",//满赠提示信息
         * "ActivityContent":"活动规则",//活动规则
         * "ActivityTypeId": "1",//0无促销、1满赠、2限时抢购
         * "ActivityId": "1"//活动ID
         * "ActivityRemarkIcon": "http://img.tianxiahuo.cn/public/txh_newapp/User/gift_tag3x.png",//满赠提示信息的ICON
         * "DDDJIcon": "http://img.tianxiahuo.cn/public/txh_newapp/User/tag_sheet3x.png",//单店单价的ICON
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
        private String ActivityContent;
        private String ActivityTypeId;
        private String ActivityId;
        private String ActivityRemarkIcon;
        private String DDDJIcon;

        public String getActivityContent() {
            return ActivityContent;
        }

        public void setActivityContent(String activityContent) {
            ActivityContent = activityContent;
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

}
