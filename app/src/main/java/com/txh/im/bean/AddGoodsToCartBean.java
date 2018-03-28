package com.txh.im.bean;

/**
 * Created by liheng on 2017/4/17.
 */

public class AddGoodsToCartBean {

    //"CartItemNum": "3",//加入到购物车中的商品数量（IsReturnCartNum=1返回实际数值，反之为0）
    //"ShelfItemNum": "5"//加到货架的商品数量（IsReturnCartNum=1返回实际数值，反之为0）
    private String CartItemNum;
    private String ShelfItemNum;

    public String getCartItemNum() {
        return CartItemNum;
    }

    public void setCartItemNum(String cartItemNum) {
        CartItemNum = cartItemNum;
    }

    public String getShelfItemNum() {
        return ShelfItemNum;
    }

    public void setShelfItemNum(String shelfItemNum) {
        ShelfItemNum = shelfItemNum;
    }
}
