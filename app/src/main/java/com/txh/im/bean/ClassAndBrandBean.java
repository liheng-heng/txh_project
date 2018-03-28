package com.txh.im.bean;

import java.util.List;

/**
 * 二期改版---获取品牌 品类数据集合
 */
public class ClassAndBrandBean {
    private List<BrandlistBean> Brandlist;
    private List<SubCategorylistBean> SubCategorylist;

    public List<BrandlistBean> getBrandlist() {
        return Brandlist;
    }

    public void setBrandlist(List<BrandlistBean> Brandlist) {
        this.Brandlist = Brandlist;
    }

    public List<SubCategorylistBean> getSubCategorylist() {
        return SubCategorylist;
    }

    public void setSubCategorylist(List<SubCategorylistBean> SubCategorylist) {
        this.SubCategorylist = SubCategorylist;
    }

    public static class BrandlistBean {
        /**
         * BrandId : 11
         * BrandName : 哇哈哈
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
         */

        private String category_id;
        private String name;
        private boolean isselect = false;

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
