package com.txh.im.bean;

import java.util.List;

/**
 * liheng
 * 04.18
 * 订单提交
 */
public class SubmitOrderBean {


    /**
     * Title : 未满配送条件
     * NeedAddItemText : 去凑单
     * ShopList : [{"ShopId":"23","ShopName":"张三批发部","ShopLogo":"http://img.tianxiahuo.cn/public/txh_newapp/User/mall_Logo_Icon@3x.png","StartOrderAmountText":"满300起送"},{"ShopId":"24","ShopName":"王二酒水专营店","ShopLogo":"http://img.tianxiahuo.cn/public/txh_newapp/User/mall_Logo_Icon@3x.png","StartOrderAmountText":"满500起送"}]
     */

    private String Title;
    private String NeedAddItemText;
    private List<ShopListBean> ShopList;

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

    public List<ShopListBean> getShopList() {
        return ShopList;
    }

    public void setShopList(List<ShopListBean> ShopList) {
        this.ShopList = ShopList;
    }

    public static class ShopListBean {
        /**
         * ShopId : 23
         * ShopName : 张三批发部
         * ShopLogo : http://img.tianxiahuo.cn/public/txh_newapp/User/mall_Logo_Icon@3x.png
         * StartOrderAmountText : 满300起送
         */

        private String ShopId;
        private String ShopName;
        private String ShopLogo;
        private String StartOrderAmountText;

        public String getShopId() {
            return ShopId;
        }

        public void setShopId(String ShopId) {
            this.ShopId = ShopId;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getShopLogo() {
            return ShopLogo;
        }

        public void setShopLogo(String ShopLogo) {
            this.ShopLogo = ShopLogo;
        }

        public String getStartOrderAmountText() {
            return StartOrderAmountText;
        }

        public void setStartOrderAmountText(String StartOrderAmountText) {
            this.StartOrderAmountText = StartOrderAmountText;
        }
    }
}
