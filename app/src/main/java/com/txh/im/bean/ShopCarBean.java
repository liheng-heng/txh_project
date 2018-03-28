package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/4/6.
 */

public class ShopCarBean{


    /**
     * TotalAmount : 0.00
     * TotalNumber : 0
     * NeedAddItemText : 去凑单
     * OffShelveImage :
     * OffShelveText : 商品下架
     * OutOfStockImage :
     * OutOfStockText : 库存为0
     * MoneySign : ¥
     * ItemTotalPreText : 小计：
     * TotalPreText : 合计：
     * ItemNumPreText : ×
     * ShopList : [{"ShopInfo":{"ShopId":"27","ShopName":"张三小店","ShopLogo":"http://img.tianxiahuo.cn/public/txh_newapp/User/mall_Logo_Icon@3x.png","StartOrderAmount":"500.00","StartOrderAmountText":"（满500配送）","IsNeedAddItem":"0","IsSelected":"0"},"ItemList":[{"SkuId":"143687","ItemName":"顶真 复合果汁苹果汁 900ml*6瓶","ItemPic":"http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg","SkuProp":"900ml*6瓶","SPrice":"58.50","ItemNumber":"5","ItemTotalAmount":"292.50","MappingStatus":"1","IsSelected":"0","MinBuyNum":"0","MaxBuyNum":"0"},{"SkuId":"143687","ItemName":"恒大冰泉长白山天然矿泉水1.5L*12瓶/箱","ItemPic":"http://img.tianxiahuo.cn/public/File1/20160818/c2a9b0d7dff77f69b7f96e0db13b1698.jpg","SkuProp":"1.5L*12瓶/箱","SPrice":"36.00","ItemNumber":"4","ItemTotalAmount":"144.00","MappingStatus":"1","IsSelected":"0","MinBuyNum":"0","MaxBuyNum":"0"},{"SkuId":"140605","ItemName":"德国冰力克无糖果粉含片糖青苹果味15g*12盒","ItemPic":"http://img.tianxiahuo.cn/public/File1/20160818/b2f849b2dba60827f90fe9bac189e269.jpg","SkuProp":"15g*12盒","SPrice":"60.00","ItemNumber":"3","ItemTotalAmount":"180.00","MappingStatus":"2","IsSelected":"0","MinBuyNum":"0","MaxBuyNum":"0"}]},{"ShopInfo":{"ShopId":"50","ShopName":"王府井老王店","ShopLogo":"http://img.tianxiahuo.cn/public/txh_newapp/User/mall_Logo_Icon@3x.png","StartOrderAmount":"1500.55","StartOrderAmountText":"（满1500.55配送）","IsNeedAddItem":"1","IsSelected":"0"},"ItemList":[{"SkuId":"143687","ItemName":"顶真 复合果汁苹果汁 900ml*6瓶","ItemPic":"http://img.tianxiahuo.cn/public/file/20160824/2e241a85f77504d5f143ca14d4bc9698.jpg","SkuProp":"900ml*6瓶","SPrice":"58.50","ItemNumber":"5","ItemTotalAmount":"292.50","ItemStatus":"1","IsSelected":"0","MinBuyNum":"0","MaxBuyNum":"0"},{"SkuId":"1419","ItemName":"百岁山 小瓶装矿泉水纯净饮用水 整箱装348ml*24瓶(五箱送六瓶)","ItemPic":"http://img.tianxiahuo.cn/goods/20160530/0000fec33b2a4e008e5e7dba0fae4a08.jpg","SkuProp":"348ml*24瓶","SPrice":"44.05","ItemNumber":"10","ItemTotalAmount":"440.5","ItemStatus":"1","IsSelected":"0","MinBuyNum":"0","MaxBuyNum":"0"},{"SkuId":"140605","ItemName":"德国冰力克无糖果粉含片糖青苹果味15g*12盒","ItemPic":"http://img.tianxiahuo.cn/public/File1/20160818/b2f849b2dba60827f90fe9bac189e269.jpg","SkuProp":"15g*12盒","SPrice":"60.00","ItemNumber":"3","ItemTotalAmount":"180.00","ItemStatus":"2","IsSelected":"0","MinBuyNum":"0","MaxBuyNum":"0"}]}]
     */

    private String TotalAmount;
    private String TotalNumber;
    private String NeedAddItemText;
    private String OffShelveImage;
    private String OffShelveText;
    private String OutOfStockImage;
    private String OutOfStockText;
    private String MoneySign;
    private String ItemTotalPreText;
    private String TotalPreText;
    private String ItemNumPreText;
    private List<ShopListBean2> ShopList;

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getTotalNumber() {
        return TotalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        TotalNumber = totalNumber;
    }

    public String getNeedAddItemText() {
        return NeedAddItemText;
    }

    public void setNeedAddItemText(String needAddItemText) {
        NeedAddItemText = needAddItemText;
    }

    public String getOffShelveImage() {
        return OffShelveImage;
    }

    public void setOffShelveImage(String offShelveImage) {
        OffShelveImage = offShelveImage;
    }

    public String getOffShelveText() {
        return OffShelveText;
    }

    public void setOffShelveText(String offShelveText) {
        OffShelveText = offShelveText;
    }

    public String getOutOfStockImage() {
        return OutOfStockImage;
    }

    public void setOutOfStockImage(String outOfStockImage) {
        OutOfStockImage = outOfStockImage;
    }

    public String getOutOfStockText() {
        return OutOfStockText;
    }

    public void setOutOfStockText(String outOfStockText) {
        OutOfStockText = outOfStockText;
    }

    public String getMoneySign() {
        return MoneySign;
    }

    public void setMoneySign(String moneySign) {
        MoneySign = moneySign;
    }

    public String getItemTotalPreText() {
        return ItemTotalPreText;
    }

    public void setItemTotalPreText(String itemTotalPreText) {
        ItemTotalPreText = itemTotalPreText;
    }

    public String getTotalPreText() {
        return TotalPreText;
    }

    public void setTotalPreText(String totalPreText) {
        TotalPreText = totalPreText;
    }

    public String getItemNumPreText() {
        return ItemNumPreText;
    }

    public void setItemNumPreText(String itemNumPreText) {
        ItemNumPreText = itemNumPreText;
    }

    public List<ShopListBean2> getShopList() {
        return ShopList;
    }

    public void setShopList(List<ShopListBean2> shopList) {
        ShopList = shopList;
    }

    @Override
    public String toString() {
        return "ShopCarBean{" +
                "TotalAmount='" + TotalAmount + '\'' +
                ", TotalNumber='" + TotalNumber + '\'' +
                ", NeedAddItemText='" + NeedAddItemText + '\'' +
                ", OffShelveImage='" + OffShelveImage + '\'' +
                ", OffShelveText='" + OffShelveText + '\'' +
                ", OutOfStockImage='" + OutOfStockImage + '\'' +
                ", OutOfStockText='" + OutOfStockText + '\'' +
                ", MoneySign='" + MoneySign + '\'' +
                ", ItemTotalPreText='" + ItemTotalPreText + '\'' +
                ", TotalPreText='" + TotalPreText + '\'' +
                ", ItemNumPreText='" + ItemNumPreText + '\'' +
                ", ShopList=" + ShopList +
                '}';
    }
}
