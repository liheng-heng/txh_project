package com.txh.im.bean;

import java.util.List;

public class ShopClassifyRightBean {


    /**
     * ItemList : [{"ItemId":"32","SkuId":"21","ItemBrandName":"娃哈哈","ItemCategoryName":"乳饮料","ItemName":"娃哈哈  营养快线原味乳饮品乳饮料 整箱500ml*15瓶","SkuProp":"500ml*15瓶","ItemPic":"http://img.tianxiahuo.cn/goods/20160621/96e802326e7708fe572a3fc4f8327fde.jpg","SPrice":"36.55","SPriceSign":"¥","ShopNumber":"0"},{"ItemId":"34","SkuId":"23","ItemBrandName":"娃哈哈","ItemCategoryName":"乳饮料","ItemName":"娃哈哈  营养快线菠萝味乳饮料饮品 整箱500ml*15瓶","SkuProp":"500ml*15瓶","ItemPic":"http://img.tianxiahuo.cn/goods/20160621/382a53653ec5f0166df75a7b5f3ff95b.jpg","SPrice":"36.55","SPriceSign":"¥","ShopNumber":"0"}]
     * CartItemNum : 3
     * ShelfItemNum : 5
     */

    private String CartItemNum;
    private String ShelfItemNum;
    private List<SearchGoodsBean.ItemListBean> ItemList;

    public String getCartItemNum() {
        return CartItemNum;
    }

    public void setCartItemNum(String CartItemNum) {
        this.CartItemNum = CartItemNum;
    }

    public String getShelfItemNum() {
        return ShelfItemNum;
    }

    public void setShelfItemNum(String ShelfItemNum) {
        this.ShelfItemNum = ShelfItemNum;
    }

    public List<SearchGoodsBean.ItemListBean> getItemList() {
        return ItemList;
    }

    public void setItemList(List<SearchGoodsBean.ItemListBean> ItemList) {
        this.ItemList = ItemList;
    }

    public static class ItemListBean {
        /**
         * ItemId : 32
         * SkuId : 21
         * ItemBrandName : 娃哈哈
         * ItemCategoryName : 乳饮料
         * ItemName : 娃哈哈  营养快线原味乳饮品乳饮料 整箱500ml*15瓶
         * SkuProp : 500ml*15瓶
         * ItemPic : http://img.tianxiahuo.cn/goods/20160621/96e802326e7708fe572a3fc4f8327fde.jpg
         * SPrice : 36.55
         * SPriceSign : ¥
         * ShopNumber : 0
         *
         *
         "OldPrice": "40.50",//原价
         "Discount": "8.0折",//折扣描述
         "ActivityRemark": "满100元赠送矿泉水1箱",//满赠提示信息
         "ActivityContent":"活动规则",//活动规则
         "ActivityTypeId": "1",//0无促销、1满赠、2限时抢购
         "ActivityId": "1"//活动ID
         "ActivityRemarkIcon": "http://img.tianxiahuo.cn/public/txh_newapp/User/gift_tag3x.png",//满赠提示信息的ICON
         "DDDJIcon": "http://img.tianxiahuo.cn/public/txh_newapp/User/tag_sheet3x.png",//单店单价的ICON
         "IsReplaceOrder": "1"//是否代下单（1表示代下单，其它为正常商品列表）
         *
         */

        private String ItemId;
        private String SkuId;
        private String ItemBrandName;
        private String ItemCategoryName;
        private String ItemName;
        private String SkuProp;
        private String ItemPic;
        private String SPrice;
        private String SPriceSign;
        private String ShopNumber;

        private String OldPrice;
        private String Discount;
        private String ActivityRemark;
        private String ActivityContent;
        private String ActivityTypeId;
        private String ActivityId;
        private String ActivityRemarkIcon;
        private String DDDJIcon;
        private String IsReplaceOrder;

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

        public String getIsReplaceOrder() {
            return IsReplaceOrder;
        }

        public void setIsReplaceOrder(String isReplaceOrder) {
            IsReplaceOrder = isReplaceOrder;
        }

        public String getItemId() {
            return ItemId;
        }

        public void setItemId(String ItemId) {
            this.ItemId = ItemId;
        }

        public String getSkuId() {
            return SkuId;
        }

        public void setSkuId(String SkuId) {
            this.SkuId = SkuId;
        }

        public String getItemBrandName() {
            return ItemBrandName;
        }

        public void setItemBrandName(String ItemBrandName) {
            this.ItemBrandName = ItemBrandName;
        }

        public String getItemCategoryName() {
            return ItemCategoryName;
        }

        public void setItemCategoryName(String ItemCategoryName) {
            this.ItemCategoryName = ItemCategoryName;
        }

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String ItemName) {
            this.ItemName = ItemName;
        }

        public String getSkuProp() {
            return SkuProp;
        }

        public void setSkuProp(String SkuProp) {
            this.SkuProp = SkuProp;
        }

        public String getItemPic() {
            return ItemPic;
        }

        public void setItemPic(String ItemPic) {
            this.ItemPic = ItemPic;
        }

        public String getSPrice() {
            return SPrice;
        }

        public void setSPrice(String SPrice) {
            this.SPrice = SPrice;
        }

        public String getSPriceSign() {
            return SPriceSign;
        }

        public void setSPriceSign(String SPriceSign) {
            this.SPriceSign = SPriceSign;
        }

        public String getShopNumber() {
            return ShopNumber;
        }

        public void setShopNumber(String ShopNumber) {
            this.ShopNumber = ShopNumber;
        }
    }
}
