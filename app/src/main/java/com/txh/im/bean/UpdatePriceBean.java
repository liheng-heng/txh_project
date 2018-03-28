package com.txh.im.bean;

/**
 * Created by liheng on 2017/4/17.
 */

public class UpdatePriceBean {
    //    "PriceStr": "销售价：125.10",//修改后的分组价格文字
    //     "Price": "120.00"//分组价格
    private String PriceStr;
    private String Price;

    public String getPriceStr() {
        return PriceStr;
    }

    public void setPriceStr(String priceStr) {
        PriceStr = priceStr;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
