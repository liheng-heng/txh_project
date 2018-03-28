package com.txh.im.bean;

import java.util.List;

public class PriceBrandBean {


    private List<BrandListBean> BrandList;

    public List<BrandListBean> getBrandList() {
        return BrandList;
    }

    public void setBrandList(List<BrandListBean> BrandList) {
        this.BrandList = BrandList;
    }

    public static class BrandListBean {
        /**
         * BrandId : 0
         * BrandName : 全选
         */

        private String BrandId;
        private String BrandName;

        public String getBrandId() {
            return BrandId;
        }

        public void setBrandId(String BrandId) {
            this.BrandId = BrandId;
        }

        public String getBrandName() {
            return BrandName;
        }

        public void setBrandName(String BrandName) {
            this.BrandName = BrandName;
        }
    }
}
