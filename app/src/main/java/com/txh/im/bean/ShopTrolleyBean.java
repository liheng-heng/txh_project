package com.txh.im.bean;

import java.util.List;

/**
 * Created by liheng on 2017/3/27.
 */

public class ShopTrolleyBean {

    private List<ShopListBean> ShopList;
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

    public List<ShopListBean> getShopList() {
        return ShopList;
    }

    public void setShopList(List<ShopListBean> ShopList) {
        this.ShopList = ShopList;
    }

    public static class ShopListBean {
        /**
         * ShopId : 6000010101
         * NickName : 张三的店铺
         * ShopName : 店铺名称：张三小店
         * ContactPhone : 联系电话：18601760331
         * ShopLogo : http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png
         * StartOrderAmount : 起订金额：200元起送
         * ItemList : [{"ItemId":"631201001","SkuId":"13120154001","ItemPic":"http://img.tianxiahuo.cn/goods/20160623/f573343ffa1677ba30e2712902a0cd20.jpg","SPrice":"¥32","ItemName":"青岛啤酒"}]
         */

        private String ShopId;
        private String NickName;
        private String ShopName;
        private String ContactPhone;
        private String ShopLogo;
        private String StartOrderAmount;
        private List<ItemListBean> ItemList;
        private List<ActItemsBean> actItems;

        public String getShopId() {
            return ShopId;
        }

        public void setShopId(String ShopId) {
            this.ShopId = ShopId;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getContactPhone() {
            return ContactPhone;
        }

        public void setContactPhone(String ContactPhone) {
            this.ContactPhone = ContactPhone;
        }

        public String getShopLogo() {
            return ShopLogo;
        }

        public void setShopLogo(String ShopLogo) {
            this.ShopLogo = ShopLogo;
        }

        public String getStartOrderAmount() {
            return StartOrderAmount;
        }

        public void setStartOrderAmount(String StartOrderAmount) {
            this.StartOrderAmount = StartOrderAmount;
        }

        public List<ItemListBean> getItemList() {
            return ItemList;
        }

        public void setItemList(List<ItemListBean> ItemList) {
            this.ItemList = ItemList;
        }

        public static class ItemListBean {
            /**
             * ItemId : 631201001
             * SkuId : 13120154001
             * ItemPic : http://img.tianxiahuo.cn/goods/20160623/f573343ffa1677ba30e2712902a0cd20.jpg
             * SPrice : ¥32
             * ItemName : 青岛啤酒
             */

            private String ItemId;
            private String SkuId;
            private String ItemPic;
            private String SPrice;
            private String ItemName;

            public String getItemId() {
                return ItemId;
            }

            public void setItemId(String ItemId) {
                this.ItemId = ItemId;
            }

            public String getSkuId() {
                return SkuId;
            }

            public void setSkuId(String SkuId) {
                this.SkuId = SkuId;
            }

            public String getItemPic() {
                return ItemPic;
            }

            public void setItemPic(String ItemPic) {
                this.ItemPic = ItemPic;
            }

            public String getSPrice() {
                return SPrice;
            }

            public void setSPrice(String SPrice) {
                this.SPrice = SPrice;
            }

            public String getItemName() {
                return ItemName;
            }

            public void setItemName(String ItemName) {
                this.ItemName = ItemName;
            }
        }

        public List<ActItemsBean> getActItems() {
            return actItems;
        }

        public void setActItems(List<ActItemsBean> actItems) {
            this.actItems = actItems;
        }
    }
}
