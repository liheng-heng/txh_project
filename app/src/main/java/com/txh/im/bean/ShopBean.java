package com.txh.im.bean;

/**
 * Created by jiajia on 2017/4/6.
 */
public class ShopBean{

    /**
     * ShopId : 27
     * ShopName : 张三小店
     * ShopLogo : http://img.tianxiahuo.cn/public/txh_newapp/User/mall_Logo_Icon@3x.png
     * StartOrderAmount : 500.00
     * StartOrderAmountText : （满500配送）
     * IsNeedAddItem : 0
     * IsSelected : 0
     */
    private String ShopId;
    private String ShopName;
    private String ShopLogo;
    private String StartOrderAmount;
    private String StartOrderAmountText;
    private String IsNeedAddItem;
    private String IsSelected;

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShopLogo() {
        return ShopLogo;
    }

    public void setShopLogo(String shopLogo) {
        ShopLogo = shopLogo;
    }

    public String getStartOrderAmount() {
        return StartOrderAmount;
    }

    public void setStartOrderAmount(String startOrderAmount) {
        StartOrderAmount = startOrderAmount;
    }

    public String getStartOrderAmountText() {
        return StartOrderAmountText;
    }

    public void setStartOrderAmountText(String startOrderAmountText) {
        StartOrderAmountText = startOrderAmountText;
    }

    public String getIsNeedAddItem() {
        return IsNeedAddItem;
    }

    public void setIsNeedAddItem(String isNeedAddItem) {
        IsNeedAddItem = isNeedAddItem;
    }

    public String getIsSelected() {
        return IsSelected;
    }

    public void setIsSelected(String isSelected) {
        IsSelected = isSelected;
    }

    @Override
    public String toString() {
        return "ShopBean{" +
                "ShopId='" + ShopId + '\'' +
                ", ShopName='" + ShopName + '\'' +
                ", ShopLogo='" + ShopLogo + '\'' +
                ", StartOrderAmount='" + StartOrderAmount + '\'' +
                ", StartOrderAmountText='" + StartOrderAmountText + '\'' +
                ", IsNeedAddItem='" + IsNeedAddItem + '\'' +
                ", IsSelected='" + IsSelected + '\'' +
                '}';
    }
}
