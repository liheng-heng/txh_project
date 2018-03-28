package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/4/15.
 */

public class ShopCarNotSetissfyBean {

    /**
     * Title : 未满配送条件
     * NeedAddItemText : 去凑单
     * ShopList : [{"ShopId":"23","ShopName":"张三批发部","ShopLogo":"http://img.tianxiahuo.cn/public/txh_newapp/User/mall_Logo_Icon@3x.png","StartOrderAmountText":"满300起送"},{"ShopId":"24","ShopName":"王二酒水专营店","ShopLogo":"","StartOrderAmountText":"满500起送"}]
     */

    private String Title;
    private String NeedAddItemText;
    private List<ShopBean> ShopList;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getNeedAddItemText() {
        return NeedAddItemText;
    }

    public void setNeedAddItemText(String NeedAddItemText) {
        this.NeedAddItemText = NeedAddItemText;
    }

    public List<ShopBean> getShopList() {
        return ShopList;
    }

    public void setShopList(List<ShopBean> ShopList) {
        this.ShopList = ShopList;
    }

    @Override
    public String toString() {
        return "ShopCarNotSetissfyBean{" +
                "Title='" + Title + '\'' +
                ", NeedAddItemText='" + NeedAddItemText + '\'' +
                ", ShopList=" + ShopList +
                '}';
    }
}
