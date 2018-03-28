package com.txh.im.bean;

/**
 * Created by jiajia on 2017/4/15.
 */

public class ShopNumBean {

    /**
     * CartItemNum : 3
     * ShelfItemNum : 5
     */

    private String CartItemNum;
    private String ShelfItemNum;

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

    @Override
    public String toString() {
        return "ShopNumBean{" +
                "CartItemNum='" + CartItemNum + '\'' +
                ", ShelfItemNum='" + ShelfItemNum + '\'' +
                '}';
    }
}
