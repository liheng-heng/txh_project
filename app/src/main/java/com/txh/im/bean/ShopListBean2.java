package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/4/13.
 */

public class ShopListBean2 {
    private ShopBean ShopInfo;
    private List<GoodsBean> ItemList;

    public ShopListBean2(ShopBean shopInfo, List<GoodsBean> itemList) {
        ShopInfo = shopInfo;
        ItemList = itemList;
    }

    public ShopListBean2() {
    }

    public ShopBean getShopInfo() {
        return ShopInfo;
    }

    public void setShopInfo(ShopBean shopInfo) {
        ShopInfo = shopInfo;
    }

    public List<GoodsBean> getItemList() {
        return ItemList;
    }

    public void setItemList(List<GoodsBean> itemList) {
        ItemList = itemList;
    }

    @Override
    public String toString() {
        return "ShopListBean2{" +
                "ShopInfo=" + ShopInfo +
                ", ItemList=" + ItemList +
                '}';
    }
}
