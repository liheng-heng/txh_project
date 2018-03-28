package com.txh.im.bean;

import java.util.List;

public class GoodsDetailBean {

    /**
     * ItemId : 112
     * ItemName : 娃哈哈 矿泉水活性含氧饮用水纯净水 整箱550ml*15瓶
     * ItemPics : ["http://img.tianxiahuo.cn/goods/20160621/1a6296b3dc8a13e50ada520d5da24539.jpg","http://img.tianxiahuo.cn/goods/20160630/1e5bf7c676635478fb6d9b7eacb8c0a0.jpg"]
     * SPrice : 36.55
     * SPriceSign : ¥
     * StockNum : 库存：9999
     * ItemProperty : [{"PropertyTitle":"条形码：","PropertyText":"6902083901622"},{"PropertyTitle":"规　格：","PropertyText":"550ml*15瓶"},{"PropertyTitle":"类　别：","PropertyText":"水"}]
     * ShopInfo : [{"PropertyTitle":"店铺名：","PropertyText":"18601760332"},{"PropertyTitle":"地　址：","PropertyText":""},{"PropertyTitle":"电　话：","PropertyText":"18601760332"}]
     * ItemInfo :
     * CartItemNum : 3
     * ShelfItemNum : 5
     * OldPrice : 40.50
     * Discount : 8.0折
     * ActivityRemark : 满100元赠送矿泉水1箱
     * ActivityContent : 活动规则
     * ActivityTypeId : 1
     * ActivityId : 1
     * ActivityRemarkIcon : http://img.tianxiahuo.cn/public/txh_newapp/User/gift_tag3x.png
     * DDDJIcon : http://img.tianxiahuo.cn/public/txh_newapp/User/tag_sheet3x.png
     *   "LimitNum": "1",//限购数量
     "LimitNumRemark": "(每单限购200件)"//限购描述
     */

    private String ItemId;
    private String ItemName;
    private String SPrice;
    private String SPriceSign;
    private String StockNum;
    private String ItemInfo;
    private String CartItemNum;
    private String ShelfItemNum;
    private String OldPrice;
    private String Discount;
    private String ActivityRemark;
    private String ActivityContent;
    private String ActivityTypeId;
    private String ActivityId;
    private String ActivityRemarkIcon;
    private String DDDJIcon;
    private List<String> ItemPics;
    private List<ItemPropertyBean> ItemProperty;
    private List<ShopInfoBean> ShopInfo;
    private String SkuId;
    private String Shopid;

    private String LimitNum;
    private String LimitNumRemark;

    public String getLimitNum() {
        return LimitNum;
    }

    public void setLimitNum(String limitNum) {
        LimitNum = limitNum;
    }

    public String getLimitNumRemark() {
        return LimitNumRemark;
    }

    public void setLimitNumRemark(String limitNumRemark) {
        LimitNumRemark = limitNumRemark;
    }

    public String getSkuId() {
        return SkuId;
    }

    public void setSkuId(String skuId) {
        SkuId = skuId;
    }

    public String getShopid() {
        return Shopid;
    }

    public void setShopid(String shopid) {
        Shopid = shopid;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String ItemId) {
        this.ItemId = ItemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
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

    public String getStockNum() {
        return StockNum;
    }

    public void setStockNum(String StockNum) {
        this.StockNum = StockNum;
    }

    public String getItemInfo() {
        return ItemInfo;
    }

    public void setItemInfo(String ItemInfo) {
        this.ItemInfo = ItemInfo;
    }

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

    public String getOldPrice() {
        return OldPrice;
    }

    public void setOldPrice(String OldPrice) {
        this.OldPrice = OldPrice;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String Discount) {
        this.Discount = Discount;
    }

    public String getActivityRemark() {
        return ActivityRemark;
    }

    public void setActivityRemark(String ActivityRemark) {
        this.ActivityRemark = ActivityRemark;
    }

    public String getActivityContent() {
        return ActivityContent;
    }

    public void setActivityContent(String ActivityContent) {
        this.ActivityContent = ActivityContent;
    }

    public String getActivityTypeId() {
        return ActivityTypeId;
    }

    public void setActivityTypeId(String ActivityTypeId) {
        this.ActivityTypeId = ActivityTypeId;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String ActivityId) {
        this.ActivityId = ActivityId;
    }

    public String getActivityRemarkIcon() {
        return ActivityRemarkIcon;
    }

    public void setActivityRemarkIcon(String ActivityRemarkIcon) {
        this.ActivityRemarkIcon = ActivityRemarkIcon;
    }

    public String getDDDJIcon() {
        return DDDJIcon;
    }

    public void setDDDJIcon(String DDDJIcon) {
        this.DDDJIcon = DDDJIcon;
    }

    public List<String> getItemPics() {
        return ItemPics;
    }

    public void setItemPics(List<String> ItemPics) {
        this.ItemPics = ItemPics;
    }

    public List<ItemPropertyBean> getItemProperty() {
        return ItemProperty;
    }

    public void setItemProperty(List<ItemPropertyBean> ItemProperty) {
        this.ItemProperty = ItemProperty;
    }

    public List<ShopInfoBean> getShopInfo() {
        return ShopInfo;
    }

    public void setShopInfo(List<ShopInfoBean> ShopInfo) {
        this.ShopInfo = ShopInfo;
    }

    public static class ItemPropertyBean {
        /**
         * PropertyTitle : 条形码：
         * PropertyText : 6902083901622
         */

        private String PropertyTitle;
        private String PropertyText;

        public String getPropertyTitle() {
            return PropertyTitle;
        }

        public void setPropertyTitle(String PropertyTitle) {
            this.PropertyTitle = PropertyTitle;
        }

        public String getPropertyText() {
            return PropertyText;
        }

        public void setPropertyText(String PropertyText) {
            this.PropertyText = PropertyText;
        }
    }

    public static class ShopInfoBean {
        /**
         * PropertyTitle : 店铺名：
         * PropertyText : 18601760332
         */

        private String PropertyTitle;
        private String PropertyText;

        public String getPropertyTitle() {
            return PropertyTitle;
        }

        public void setPropertyTitle(String PropertyTitle) {
            this.PropertyTitle = PropertyTitle;
        }

        public String getPropertyText() {
            return PropertyText;
        }

        public void setPropertyText(String PropertyText) {
            this.PropertyText = PropertyText;
        }
    }
}
