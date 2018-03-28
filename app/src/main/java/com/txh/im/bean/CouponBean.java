package com.txh.im.bean;

import java.util.List;

public class CouponBean {
    /**
     * UserCouponPLUrl : http://h5.new.tianxiahuo.cn/my_coupons.html
     * CouponList : [{"CouponId":"123","CouponValue":"10","ConditionValue":"满200可使用","EndTime":"有效期7天","UseRange":"123","UseRangeName":"单店可用"},{"CouponId":"125","CouponValue":"20","ConditionValue":"满200可使用","EndTime":"有效期7天","UseRange":"0","UseRangeName":"全场可用"}]
     */
    private String UserCouponPLUrl;
    private List<CouponListBean> CouponList;

    public String getUserCouponPLUrl() {
        return UserCouponPLUrl;
    }

    public void setUserCouponPLUrl(String UserCouponPLUrl) {
        this.UserCouponPLUrl = UserCouponPLUrl;
    }

    public List<CouponListBean> getCouponList() {
        return CouponList;
    }

    public void setCouponList(List<CouponListBean> CouponList) {
        this.CouponList = CouponList;
    }

    public static class CouponListBean {
        /**
         * CouponId : 123
         * CouponValue : 10
         * ConditionValue : 满200可使用
         * EndTime : 有效期7天
         * UseRange : 123
         * UseRangeName : 单店可用
         */

        private String CouponId;
        private String CouponValue;
        private String ConditionValue;
        private String EndTime;
        private String UseRange;
        private String UseRangeName;

        public String getCouponId() {
            return CouponId;
        }

        public void setCouponId(String CouponId) {
            this.CouponId = CouponId;
        }

        public String getCouponValue() {
            return CouponValue;
        }

        public void setCouponValue(String CouponValue) {
            this.CouponValue = CouponValue;
        }

        public String getConditionValue() {
            return ConditionValue;
        }

        public void setConditionValue(String ConditionValue) {
            this.ConditionValue = ConditionValue;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getUseRange() {
            return UseRange;
        }

        public void setUseRange(String UseRange) {
            this.UseRange = UseRange;
        }

        public String getUseRangeName() {
            return UseRangeName;
        }

        public void setUseRangeName(String UseRangeName) {
            this.UseRangeName = UseRangeName;
        }
    }
}
