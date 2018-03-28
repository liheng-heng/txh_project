package com.txh.im.bean;

import java.util.List;

public class PriceClassBean {

    private List<CategoryListBean> CategoryList;

    public List<CategoryListBean> getCategoryList() {
        return CategoryList;
    }

    public void setCategoryList(List<CategoryListBean> CategoryList) {
        this.CategoryList = CategoryList;
    }

    public static class CategoryListBean {
        /**
         * category_id : 0
         * name : 全选
         */

        private String category_id;
        private String name;

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
