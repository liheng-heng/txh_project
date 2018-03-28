package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/5/23.
 * 价格分组bean
 */

public class PriceGrouBean {

    /**
     * PageTitle : 价格分组
     * ShowHGroupBtn : {"IsShow":"1","ItemCode":"ShowHiddenGroup","ItemText":"显示隐藏价格分组"}
     * GroupList : [{"GroupId":"1","GroupName":"价格分组名称：A","IsDefault":"1","LatestOpUser":"最后操作人：王二","GroupStatus":"1","OpBtnList":[{"ItemCode":"EditPrice","ItemText":"编辑价格","ItemStatus":"1"},{"ItemCode":"AddOrListUnit","ItemText":"添加/查看门店","ItemStatus":"1"}]},{"GroupId":"2","GroupName":"价格分组名称：B","IsDefault":"0","LatestOpUser":"最后操作人：王二","GroupStatus":"1","OpBtnList":[{"ItemCode":"EditPrice","ItemText":"编辑价格","ItemStatus":"1"},{"ItemCode":"AddOrListUnit","ItemText":"添加/查看门店","ItemStatus":"1"},{"ItemCode":"ProhibitGroup","ItemText":"禁用","ItemStatus":"1"}]},{"GroupId":"3","GroupName":"价格分组名称：C","IsDefault":"0","LatestOpUser":"最后操作人：麻子","GroupStatus":"1","OpBtnList":[{"ItemCode":"EditPrice","ItemText":"编辑价格","ItemStatus":"1"},{"ItemCode":"AddOrListUnit","ItemText":"添加/查看门店","ItemStatus":"1"},{"ItemCode":"ProhibitGroup","ItemText":"禁用","ItemStatus":"1"}]},{"GroupId":"4","GroupName":"价格分组名称：D","IsDefault":"0","LatestOpUser":"最后操作人：麻子","GroupStatus":"0","OpBtnList":[{"ItemCode":"EditPrice","ItemText":"编辑价格","ItemStatus":"0"},{"ItemCode":"AddOrListUnit","ItemText":"添加/查看门店","ItemStatus":"0"},{"ItemCode":"EnableGroup","ItemText":"启用","ItemStatus":"1"},{"ItemCode":"HiddenGroup","ItemText":"隐藏","ItemStatus":"1"}]}]
     */

    private String PageTitle;
    private ShowHGroupBtnBean ShowHGroupBtn;
    private List<GroupListBean> GroupList;

    public String getPageTitle() {
        return PageTitle;
    }

    public void setPageTitle(String PageTitle) {
        this.PageTitle = PageTitle;
    }

    public ShowHGroupBtnBean getShowHGroupBtn() {
        return ShowHGroupBtn;
    }

    public void setShowHGroupBtn(ShowHGroupBtnBean ShowHGroupBtn) {
        this.ShowHGroupBtn = ShowHGroupBtn;
    }

    public List<GroupListBean> getGroupList() {
        return GroupList;
    }

    public void setGroupList(List<GroupListBean> GroupList) {
        this.GroupList = GroupList;
    }

    public static class ShowHGroupBtnBean {
        /**
         * IsShow : 1
         * ItemCode : ShowHiddenGroup
         * ItemText : 显示隐藏价格分组
         */

        private String IsShow;
        private String ItemCode;
        private String ItemText;

        public String getIsShow() {
            return IsShow;
        }

        public void setIsShow(String IsShow) {
            this.IsShow = IsShow;
        }

        public String getItemCode() {
            return ItemCode;
        }

        public void setItemCode(String ItemCode) {
            this.ItemCode = ItemCode;
        }

        public String getItemText() {
            return ItemText;
        }

        public void setItemText(String ItemText) {
            this.ItemText = ItemText;
        }
    }

    public static class GroupListBean {
        /**
         * GroupId : 1
         * GroupName : 价格分组名称：A
         * IsDefault : 1
         * LatestOpUser : 最后操作人：王二
         * GroupStatus : 1
         * OpBtnList : [{"ItemCode":"EditPrice","ItemText":"编辑价格","ItemStatus":"1"},{"ItemCode":"AddOrListUnit","ItemText":"添加/查看门店","ItemStatus":"1"}]
         */

        private String GroupId;
        private String GroupName;
        private String IsDefault;
        private String LatestOpUser;
        private String GroupStatus;
        private List<OpBtnListBean> OpBtnList;

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

        public String getIsDefault() {
            return IsDefault;
        }

        public void setIsDefault(String IsDefault) {
            this.IsDefault = IsDefault;
        }

        public String getLatestOpUser() {
            return LatestOpUser;
        }

        public void setLatestOpUser(String LatestOpUser) {
            this.LatestOpUser = LatestOpUser;
        }

        public String getGroupStatus() {
            return GroupStatus;
        }

        public void setGroupStatus(String GroupStatus) {
            this.GroupStatus = GroupStatus;
        }

        public List<OpBtnListBean> getOpBtnList() {
            return OpBtnList;
        }

        public void setOpBtnList(List<OpBtnListBean> OpBtnList) {
            this.OpBtnList = OpBtnList;
        }

        public static class OpBtnListBean {
            /**
             * ItemCode : EditPrice
             * ItemText : 编辑价格
             * ItemStatus : 1
             */

            private String ItemCode;
            private String ItemText;
            private String ItemStatus;

            public String getItemCode() {
                return ItemCode;
            }

            public void setItemCode(String ItemCode) {
                this.ItemCode = ItemCode;
            }

            public String getItemText() {
                return ItemText;
            }

            public void setItemText(String ItemText) {
                this.ItemText = ItemText;
            }

            public String getItemStatus() {
                return ItemStatus;
            }

            public void setItemStatus(String ItemStatus) {
                this.ItemStatus = ItemStatus;
            }
        }
    }
}
