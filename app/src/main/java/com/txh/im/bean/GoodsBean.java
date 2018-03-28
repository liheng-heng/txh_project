package com.txh.im.bean;

import java.util.Date;

/**
 * Created by jiajia on 2017/4/6.
 */
public class GoodsBean {

    /**
     * SkuId : 143687
     * ItemName : 顶真 复合果汁苹果汁 900ml*6瓶
     * ItemPic : http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg
     * SkuProp : 900ml*6瓶
     * SPrice : 58.50
     * ItemNumber : 5
     * ItemTotalAmount : 292.50
     * MappingStatus : 1
     * IsSelected : 0
     * MinBuyNum : 0
     * MaxBuyNum : 0
     */
    private String SkuId;
    private String ItemName;
    private String ItemPic;
    private String SkuProp;
    private String SPrice;
    private String ItemNumber;
    private String ItemTotalAmount;
    private String MappingStatus;//1正常，2,3无货
    private String IsSelected;
    private String MinBuyNum;
    private String MaxBuyNum;
    private Date date;
    private String CartId;
    private int isEdit;//0是完成状态，1是编辑状态
    private String Discount;//折扣的描述信息
    private String ActivityRemarkIcon;//满赠的icon
    private String ActivityRemark;//满赠的消息
    private String OldPrice;//原价
    private String ActivityTypeId;//0是无促销,1是满赠,2是限时抢购
    private String ActivityId;

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

    public String getItemTotalAmount() {
        return ItemTotalAmount;
    }

    public void setItemTotalAmount(String itemTotalAmount) {
        ItemTotalAmount = itemTotalAmount;
    }

    public String getMappingStatus() {
        return MappingStatus;
    }

    public void setMappingStatus(String mappingStatus) {
        MappingStatus = mappingStatus;
    }

    public String getIsSelected() {
        return IsSelected;
    }

    public void setIsSelected(String isSelected) {
        IsSelected = isSelected;
    }

    public String getMinBuyNum() {
        return MinBuyNum;
    }

    public void setMinBuyNum(String minBuyNum) {
        MinBuyNum = minBuyNum;
    }

    public String getMaxBuyNum() {
        return MaxBuyNum;
    }

    public void setMaxBuyNum(String maxBuyNum) {
        MaxBuyNum = maxBuyNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(int isEdit) {
        this.isEdit = isEdit;
    }

    public String getCartId() {
        return CartId;
    }

    public void setCartId(String cartId) {
        CartId = cartId;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getActivityRemarkIcon() {
        return ActivityRemarkIcon;
    }

    public void setActivityRemarkIcon(String activityRemarkIcon) {
        ActivityRemarkIcon = activityRemarkIcon;
    }

    public String getActivityRemark() {
        return ActivityRemark;
    }

    public void setActivityRemark(String activityRemark) {
        ActivityRemark = activityRemark;
    }

    public String getOldPrice() {
        return OldPrice;
    }

    public void setOldPrice(String oldPrice) {
        OldPrice = oldPrice;
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
}
