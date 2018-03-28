package com.txh.im.bean;

import java.util.List;

public class ShopClassifyPriceEditLeftBean {


    /**
     * PageTitle : 编辑价格
     * GroupId : 1
     * GroupName : A
     * CategoryList : [{"category_id":"0","name":"全部"},{"category_id":"1","name":"零食"},{"category_id":"2","name":"饮料"},{"category_id":"3","name":"乳品"},{"category_id":"4","name":"水"},{"category_id":"5","name":"食品"},{"category_id":"6","name":"酒类"}]
     */

    private String PageTitle;
    private String GroupId;
    private String GroupName;
    private List<CategoryListBean> CategoryList;

    public String getPageTitle() {
        return PageTitle;
    }

    public void setPageTitle(String PageTitle) {
        this.PageTitle = PageTitle;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public List<CategoryListBean> getCategoryList() {
        return CategoryList;
    }

    public void setCategoryList(List<CategoryListBean> CategoryList) {
        this.CategoryList = CategoryList;
    }

    public static class CategoryListBean {
        /**
         * category_id : 0
         * name : 全部
         */

        private String category_id;
        private String name;
        private boolean isSeleted = false;

        public boolean isSeleted() {
            return isSeleted;
        }

        public void setSeleted(boolean seleted) {
            isSeleted = seleted;
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
