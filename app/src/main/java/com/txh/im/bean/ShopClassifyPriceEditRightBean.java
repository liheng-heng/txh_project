package com.txh.im.bean;

import java.util.List;

public class ShopClassifyPriceEditRightBean {


    /**
     * PageSize : 10
     * ItemList : [{"SkuId":"19","ItemName":"娃哈哈 矿泉水活性含氧饮用水纯净水 整箱550ml*15瓶","SkuProp":"规格：550ml*15瓶","ItemPic":"http://img.tianxiahuo.cn/goods/20160621/1a6296b3dc8a13e50ada520d5da24539.jpg","PriceStr":"销售价：36.50","Price":"35.00"},{"SkuId":"21","ItemName":"娃哈哈 营养快线原味乳饮品乳饮料 整箱500ml*15瓶","SkuProp":"规格：550ml*15瓶","ItemPic":"http://img.tianxiahuo.cn/goods/20160621/96e802326e7708fe572a3fc4f8327fde.jpg","PriceStr":"销售价：120.00","Price":"115.00"},{"SkuId":"23","ItemName":"娃哈哈 营养快线菠萝味乳饮料饮品 整箱500ml*15瓶","SkuProp":"规格：550ml*15瓶","ItemPic":"http://img.tianxiahuo.cn/goods/20160621/382a53653ec5f0166df75a7b5f3ff95b.jpg","PriceStr":"销售价：65.00","Price":"61.00"}]
     */

    private int PageSize;
    private List<ItemListBean> ItemList;

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public List<ItemListBean> getItemList() {
        return ItemList;
    }

    public void setItemList(List<ItemListBean> ItemList) {
        this.ItemList = ItemList;
    }

    public static class ItemListBean {
        /**
         * SkuId : 19
         * ItemName : 娃哈哈 矿泉水活性含氧饮用水纯净水 整箱550ml*15瓶
         * SkuProp : 规格：550ml*15瓶
         * ItemPic : http://img.tianxiahuo.cn/goods/20160621/1a6296b3dc8a13e50ada520d5da24539.jpg
         * PriceStr : 销售价：36.50
         * Price : 35.00
         */

        /**
         * 单店单价
         */
//                 "SkuId": "19",//商品SkuId
//                "ItemName": "娃哈哈 矿泉水活性含氧饮用水纯净水 整箱550ml*15瓶",//商品名称
//                "SkuProp": "规格：550ml*15瓶",//商品规格
//                "ItemPic": "http://img.tianxiahuo.cn/goods/20160621/1a6296b3dc8a13e50ada520d5da24539.jpg",//商品图片
//                "PriceStr": "销售价：36.50",//销售价格文本
//                "Price": "35.00",//销售价格
//                "IsUPrice": "0"//是否是单店单价（0否，1是）

        /**
         * 编辑价格
         */
//                "SkuId": "21",
//                "ItemName": "娃哈哈  营养快线原味乳饮品乳饮料 整箱500ml*15瓶",
//                "SkuProp": "规格：550ml*15瓶",
//                "ItemPic": "http://img.tianxiahuo.cn/goods/20160621/96e802326e7708fe572a3fc4f8327fde.jpg",
//                "PriceStr": "销售价：120.00",
//                "Price": "115.00"

        private String SkuId;
        private String ItemName;
        private String SkuProp;
        private String ItemPic;
        private String PriceStr;
        private String Price;
        private String IsUPrice;

        public String getIsUPrice() {
            return IsUPrice;
        }

        public void setIsUPrice(String isUPrice) {
            IsUPrice = isUPrice;
        }

        public String getSkuId() {
            return SkuId;
        }

        public void setSkuId(String SkuId) {
            this.SkuId = SkuId;
        }

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String ItemName) {
            this.ItemName = ItemName;
        }

        public String getSkuProp() {
            return SkuProp;
        }

        public void setSkuProp(String SkuProp) {
            this.SkuProp = SkuProp;
        }

        public String getItemPic() {
            return ItemPic;
        }

        public void setItemPic(String ItemPic) {
            this.ItemPic = ItemPic;
        }

        public String getPriceStr() {
            return PriceStr;
        }

        public void setPriceStr(String PriceStr) {
            this.PriceStr = PriceStr;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }
    }
}
