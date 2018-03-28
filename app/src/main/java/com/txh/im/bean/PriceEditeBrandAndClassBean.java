package com.txh.im.bean;

import java.util.List;

public class PriceEditeBrandAndClassBean {

    private List<PriceEditeBrandAndClassBean.BrandlistBean> BrandList;
    private List<PriceEditeBrandAndClassBean.SubCategorylistBean> CategoryList;

    public List<BrandlistBean> getBrandList() {
        return BrandList;
    }

    public void setBrandList(List<BrandlistBean> brandList) {
        BrandList = brandList;
    }

    public List<SubCategorylistBean> getCategoryList() {
        return CategoryList;
    }

    public void setCategoryList(List<SubCategorylistBean> categoryList) {
        CategoryList = categoryList;
    }

    public static class BrandlistBean {
        /**
         * BrandId : 11
         * BrandName : 哇哈哈
         *
         *    "BrandId":"0",
         "BrandName":"全选"
         */

        private String BrandId;
        private String BrandName;
        private boolean isselect = false;

        public boolean isselect() {
            return isselect;
        }

        public void setIsselect(boolean isselect) {
            this.isselect = isselect;
        }

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

    public static class SubCategorylistBean {
        /**
         * category_id : 50
         * name : 洗护
         *
         *                "category_id":"0",
         "parent_id":"0",
         "name":"全选"
         *
         */

        private String category_id;
        private String name;
        private String parent_id;
        private boolean isselect = false;

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public boolean isselect() {
            return isselect;
        }

        public void setIsselect(boolean isselect) {
            this.isselect = isselect;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
